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
package com.kwoksys.biz.contracts.core;

import com.kwoksys.action.contracts.ContractSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.SqlUtils;
import com.kwoksys.framework.http.RequestContext;

/**
 * This is for building search queries.
 */
public class ContractSearch extends BaseSearch {
    public static final String CONTRACT_PROVIDER_ID_KEY = "contractProviderId";

    public static final String CONTRACT_STAGE_KEY = "stage";

    public static final String CONTRACT_EXPIRE_BEFORE_KEY = "contractExpireBefore";
    
    public static final String CONTRACT_EXPIRE_AFTER_KEY = "contractExpireAfter";
    
    public static final String CONTRACT_NON_EXPIRED_KEY = "nonExpiredContracts";
    
    public static final String CONTRACT_EXPIRED_KEY = "expiredContracts";
    
    public static final String CONTRACT_EXPIRED_NOTIFIED_KEY = "expirationNotified";
    
    public ContractSearch(RequestContext requestContext, String sessionKey) {
        super(requestContext, sessionKey);
    }

    public ContractSearch() {}

    /**
     * Generates searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(ContractSearchForm contractSearchForm) {
        String cmd = requestContext.getParameterString("cmd");

        // Getting search criteria map from session variable.
        if (!cmd.isEmpty()) {
            reset();

            if (cmd.equals("filter")) {
                String stageFilter = requestContext.getParameterString("stageFilter");
                contractSearchForm.setStageFilter(stageFilter);
                if (!stageFilter.isEmpty() && !stageFilter.equals("0")) {
                    searchCriteriaMap.put(ContractSearch.CONTRACT_STAGE_KEY, stageFilter);
                }
            } else {
                contractSearchForm.setRequest(requestContext);

                if (cmd.equals("groupBy")) {
                    String contractExpire = requestContext.getParameterString("contractExpire");
                    if (!contractExpire.isEmpty()) {
                        // ContractExpire url variable is in the format of "start_end", e.g. "30_60" means contract expires
                        // between 30 to 60 days. Or in the format of "end", e.g. "30", meaning contract expires in 30 days.
                        String[] interval = contractExpire.split("_");
                        if (interval.length == 1) {
                            searchCriteriaMap.put(CONTRACT_EXPIRE_BEFORE_KEY, interval[0]);
                        } else {
                            searchCriteriaMap.put(CONTRACT_EXPIRE_AFTER_KEY, interval[0]);
                            searchCriteriaMap.put(CONTRACT_EXPIRE_BEFORE_KEY, interval[1]);
                        }
                    }
                } else if (cmd.equals("showNonExpired")) {
                    searchCriteriaMap.put(CONTRACT_NON_EXPIRED_KEY, true);

                } else if (cmd.equals("search")) {
                    // We are expecting user to enter some search criteria.
                    if (!contractSearchForm.getContractName().isEmpty()) {
                        searchCriteriaMap.put("contractNameContains", contractSearchForm.getContractName());
                    }
                     if (!contractSearchForm.getDescription().isEmpty()) {
                        searchCriteriaMap.put("description", contractSearchForm.getDescription());
                    }
                    if (contractSearchForm.getContractTypeId() != 0) {
                        searchCriteriaMap.put("contractTypeId", contractSearchForm.getContractTypeId());
                    }
                    if (contractSearchForm.getContractProviderId() != 0) {
                        searchCriteriaMap.put(CONTRACT_PROVIDER_ID_KEY, contractSearchForm.getContractProviderId());
                    }
                    if (contractSearchForm.getStage() != 0) {
                        searchCriteriaMap.put(CONTRACT_STAGE_KEY, contractSearchForm.getStage());
                    }
                    // Search by custom fields
                    String attrValue = contractSearchForm.getAttrValue();
                    if (!contractSearchForm.getAttrId().isEmpty() && !attrValue.isEmpty()) {
                        searchCriteriaMap.put("attrId", contractSearchForm.getAttrId());
                        searchCriteriaMap.put("attrValue", attrValue);
                    }
                }
            }
        }
    }

    public void applyMap(QueryCriteria query) {
        if (searchCriteriaMap == null) {
            return;
        }
        // Search by non-expired contracts.
        if (searchCriteriaMap.containsKey(CONTRACT_NON_EXPIRED_KEY)) {
            query.appendWhereClause("c.contract_expiration_date is null or c.contract_expiration_date>now()");
        }
        // Search by expired contracts.
        if (searchCriteriaMap.containsKey(CONTRACT_EXPIRED_KEY)) {
            query.appendWhereClause("c.contract_expiration_date < now()");
        }
        // For contract name
        if (searchCriteriaMap.containsKey("contractNameContains")) {
            query.appendWhereClause("lower(c.contract_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("contractNameContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey("contractTypeId")) {
            query.appendWhereClause("c.contract_type = " + SqlUtils.encodeInteger(searchCriteriaMap.get("contractTypeId")));
        }
        if (searchCriteriaMap.containsKey(CONTRACT_PROVIDER_ID_KEY)) {
            query.appendWhereClause("c.contract_provider_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(CONTRACT_PROVIDER_ID_KEY)));
        }
        if (searchCriteriaMap.containsKey(CONTRACT_STAGE_KEY)) {
            query.appendWhereClause("c.contract_stage = " + SqlUtils.encodeInteger(searchCriteriaMap.get(CONTRACT_STAGE_KEY)));
        }
        // For contract description
        if (searchCriteriaMap.containsKey("description")) {
            query.appendWhereClause("lower(c.contract_description) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("description")) + "%')");
        }
        // For custom fields
        if (searchCriteriaMap.containsKey("attrId") && searchCriteriaMap.containsKey("attrValue")) {
            query.appendWhereClause("c.contract_id in (select object_id from object_attribute_value where attribute_id = "+
                    SqlUtils.encodeInteger(searchCriteriaMap.get("attrId")) + " and lower(attr_value) like lower('%"
                    + SqlUtils.encodeString(searchCriteriaMap.get("attrValue")) +"%'))");
        }
        if (searchCriteriaMap.containsKey(CONTRACT_EXPIRE_AFTER_KEY)) {
            query.appendWhereClause("c.contract_expiration_date > now()::timestamp + '+"
                    + SqlUtils.encodeInteger(searchCriteriaMap.get(CONTRACT_EXPIRE_AFTER_KEY)) +" day'::interval");
        }
        if (searchCriteriaMap.containsKey(CONTRACT_EXPIRE_BEFORE_KEY)) {
            query.appendWhereClause("c.contract_expiration_date < now()::timestamp + '+"
                    + SqlUtils.encodeInteger(searchCriteriaMap.get(CONTRACT_EXPIRE_BEFORE_KEY)) +" day'::interval");
        }
        if (searchCriteriaMap.containsKey(CONTRACT_EXPIRED_NOTIFIED_KEY)) {
            query.appendWhereClause("(c.contract_owner_id is not null)");
            query.appendWhereClause("(c.contract_expiration_notified is null or c.contract_expiration_notified <> "
                    + SqlUtils.encodeInteger(searchCriteriaMap.get(CONTRACT_EXPIRED_NOTIFIED_KEY)) + ")");
        }
    }
}