/*
 * Copyright 2016 Kwoksys
 *
 * http://www.kwoksys.com/LICENSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kwoksys.biz.system.core.scheduler;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractSearch;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.Callback;

/**
 * ContractExpirationNotificationTask.
 */
public class ContractExpirationNotificationTask {
    
    private static final Logger LOGGER = Logger.getLogger(ContractExpirationNotificationTask.class.getName());

    public void execute() {
        try {
            LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.SCHEDULER_PREFIX), LogConfigManager.SCHEDULER_PREFIX 
                    + " Checking contract for expiration notification emails...");

            RequestContext requestContext = new RequestContext();
            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            
            requestContext.setSysdate(systemService.getSystemInfo().getSysdate());
            ContractService contractService = ServiceProvider.getContractService(requestContext);

            String[] thresholds = ConfigManager.email.getContractExpirationThresholds();
            Counter thresholdCounter = new Counter();
            
            // Set a limit to control how many email messages can be sent out an once.  
            int notificationLimit = ConfigManager.email.getContractNotificationBatchSize();
            Counter notificationCounter = new Counter();
            
            while (thresholdCounter.getCurrent() < thresholds.length - 1 && notificationCounter.getCurrent() < notificationLimit) {
                String currentThreshold = thresholds[thresholdCounter.getCurrent()];
                String nextThreshold = thresholds[thresholdCounter.incr()];
                
                ContractSearch contractSearch = new ContractSearch();

                if (currentThreshold.equals("-1")) {
                    // Retrieve expired contracts
                    contractSearch.put(ContractSearch.CONTRACT_EXPIRED_KEY, true);
                } else {
                    // Retrieve non-expired contracts.
                    contractSearch.put(ContractSearch.CONTRACT_EXPIRE_AFTER_KEY, currentThreshold);
                    contractSearch.put(ContractSearch.CONTRACT_EXPIRE_BEFORE_KEY, nextThreshold);
                }

                // Retrieve contracts that have not been notified
                contractSearch.put(ContractSearch.CONTRACT_EXPIRED_NOTIFIED_KEY, nextThreshold);
                
                QueryCriteria queryCriteria = new QueryCriteria(contractSearch);
                queryCriteria.setLimit(notificationLimit, 0);

                queryCriteria.setCallback(new Callback() {
                    @Override
                    public void run(Object object) throws Exception {
                        Contract contract = (Contract) object;
                        notificationCounter.incr();
                        contractService.sendContractExpirationReminder(contract, Integer.parseInt(nextThreshold));
                    }
                });

                contractService.fetchContracts(queryCriteria);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, LogConfigManager.SCHEDULER_PREFIX + " Problem sending contract expiration "
                    + "notification emails ", e);
        }
    }
}
