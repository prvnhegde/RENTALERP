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
package com.kwoksys.biz.contacts.core;

import com.kwoksys.action.contacts.ContactSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.SqlUtils;
import com.kwoksys.framework.http.RequestContext;

/**
 * This is for building search queries.
 */
public class CompanySearch extends BaseSearch {

    public static final String COMPANY_TYPE_EQUALS = "companyTypeEquals";

    public static final String COMPANY_ID_EQUALS = "companyIdEquals";

    public static final String COMPANY_NAME_EQUALS = "companyNameEquals";

    public static final String CONTRACT_PROVIDERS = "contractProviders";

    public CompanySearch() {}

    public CompanySearch(RequestContext requestContext, String sessionKey) {
        super(requestContext, sessionKey);
    }

    /**
     * This would generate searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(ContactSearchForm contactSearchForm) {
        String attrId = contactSearchForm.getAttrId();
        String attrValue = contactSearchForm.getAttrValue();
        String companyName = contactSearchForm.getCompanyName();
        String description = contactSearchForm.getDescription();
        String companyTag = contactSearchForm.getCompanyTag();

        // Search by Company name contains something.
        if (!companyName.isEmpty()) {
            searchCriteriaMap.put("companyNameContains", companyName);
        }
        // Search by company description
        if (!description.isEmpty()) {
            searchCriteriaMap.put("companyDescription", description);
        }
        // Search by tag name equals.
        if (!companyTag.isEmpty()) {
            searchCriteriaMap.put("tagNameEquals", companyTag);
        }
        // Search by custom fields
        if (!attrId.isEmpty() && !attrValue.isEmpty()) {
            searchCriteriaMap.put("attrId", attrId);
            searchCriteriaMap.put("attrValue", attrValue);
        }
    }

    public void applyMap(QueryCriteria query) {
        if (searchCriteriaMap == null) {
            return;
        }
        // For Company name contains.
        if (searchCriteriaMap.containsKey("companyNameContains")) {
            query.appendWhereClause("lower(c.company_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("companyNameContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey(COMPANY_NAME_EQUALS)) {
            query.appendWhereClause("lower(c.company_name) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(COMPANY_NAME_EQUALS)) + "')");
        }
        // For company description
        if (searchCriteriaMap.containsKey("companyDescription")) {
            query.appendWhereClause("lower(c.company_description) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("companyDescription")) + "%')");
        }
        // For tag name equals.
        if (searchCriteriaMap.containsKey("tagNameEquals")) {
            query.appendWhereClause("c.company_id in (select distinct ctmv.company_id from company_tag_map_view ctmv where lower(ctmv.tag_name) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get("tagNameEquals")) + "'))");
        }
        // For custom fields
        if (searchCriteriaMap.containsKey("attrId") && searchCriteriaMap.containsKey("attrValue")) {
            query.appendWhereClause("c.company_id in (select object_id from object_attribute_value where attribute_id = "+
                    SqlUtils.encodeInteger(searchCriteriaMap.get("attrId")) + " and lower(attr_value) like lower('%"
                    + SqlUtils.encodeString(searchCriteriaMap.get("attrValue")) +"%'))");
        }
        if (searchCriteriaMap.containsKey(COMPANY_TYPE_EQUALS)) {
            if (searchCriteriaMap.containsKey(COMPANY_ID_EQUALS)) {
                query.appendWhereClause("(c.company_id in (select object_id from object_attribute_value where attribute_id="
                        + Attributes.COMPANY_TYPES + " and attribute_field_id=" + SqlUtils.encodeInteger(searchCriteriaMap.get(COMPANY_TYPE_EQUALS)) + ") " +
                        "or c.company_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(COMPANY_ID_EQUALS)) + ")");
            } else {
                query.appendWhereClause("c.company_id in (select object_id from object_attribute_value where attribute_id="
                        + Attributes.COMPANY_TYPES + " and attribute_field_id=" + SqlUtils.encodeInteger(searchCriteriaMap.get(COMPANY_TYPE_EQUALS)) + ")");
            }
        }
        if (searchCriteriaMap.containsKey(CONTRACT_PROVIDERS)) {
            query.appendWhereClause("c.company_id in (select contract_provider from contract)");            
        }
    }
}
