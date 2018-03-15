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

import java.util.logging.Logger;

import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.util.DatetimeUtils;

/**
 * Class to run tasks.
 */
public class TaskRunner {
    
    private static final Logger LOGGER = Logger.getLogger(TaskRunner.class.getName());

    private static long lastIssueMailRunTime = 0;
    
    private static long lastContractNotificationRunTime = 0;
    
    public void execute() {
        long currentTime = System.currentTimeMillis();

        LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.SCHEDULER_PREFIX), "{0} Scheduler running in the background, current system time: {1}",
                new String[] {LogConfigManager.SCHEDULER_PREFIX, String.valueOf(currentTime)});
        
        runIssueEmailTask(currentTime);
        
        runContractNotificationTask(currentTime);
    }
    
    private void runIssueEmailTask(long currentTime) {
        int issueEmailRepeatInterval = ConfigManager.email.getPopRepeatInterval();

        if (issueEmailRepeatInterval != 0) {
            // Add a second in the equation since there's some difference in time when running the scheduler.
            if (lastIssueMailRunTime + issueEmailRepeatInterval < currentTime + DatetimeUtils.ONE_SECOND_MILLISECONDS) {
                lastIssueMailRunTime = currentTime;
                new FetchIssueMailTask().execute();
            }
        }
    }
    
    private void runContractNotificationTask(long currentTime) {
        if (ConfigManager.email.isContractExpireNotificationEnabled()) {
            long contractNotificationInterval = ConfigManager.email.getContractNotificationInterval();
            
            if (lastContractNotificationRunTime + contractNotificationInterval < currentTime + DatetimeUtils.ONE_SECOND_MILLISECONDS) {
                lastContractNotificationRunTime = currentTime;

                new ContractExpirationNotificationTask().execute();
            }
        } else {
            LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.SCHEDULER_PREFIX), LogConfigManager.SCHEDULER_PREFIX 
                    + " Sending contract expiration notification emails... not configured, skipping");
        }
    }
}
