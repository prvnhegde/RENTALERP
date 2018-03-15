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
package com.kwoksys.biz.software;

import com.kwoksys.action.software.SoftwareSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.SqlUtils;
import com.kwoksys.framework.http.RequestContext;

import java.util.List;

/**
 * This is for building search queries.
 */
public class SoftwareSearch extends BaseSearch {

    public static final String SOFTWARE_ID_EQUALS = "softwareIdEquals";

    public static final String SOFTWARE_TYPES_CONTAIN = "softwareTypesContain";

    public SoftwareSearch(RequestContext requestContext, String sessionKey) {
        super(requestContext, sessionKey);
    }

    public SoftwareSearch() {}

    /**
     * Generates searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(SoftwareSearchForm softwareSearchForm) {
        String cmd = requestContext.getParameterString("cmd");

        if (!cmd.isEmpty()) {
            reset();

            if (cmd.equals("filter")) {
                String manufacturerId = requestContext.getParameterString("manufacturerIdFilter");
                if (!manufacturerId.isEmpty()) {
                    softwareSearchForm.setManufacturerIdFilter(manufacturerId);
                    searchCriteriaMap.put("manufacturerIdEquals", manufacturerId);
                }
            } else {
                softwareSearchForm.setRequest(requestContext);

                if (cmd.equals("search")) {
                    // Search by manufacturer Id equals something.
                    if (!softwareSearchForm.getManufacturerId().isEmpty()) {
                        searchCriteriaMap.put("manufacturerIdEquals", softwareSearchForm.getManufacturerId());
                    }
                    // Search by Software Id equals something.
                    if (!softwareSearchForm.getSoftwareId().isEmpty()) {
                        searchCriteriaMap.put(SOFTWARE_ID_EQUALS, softwareSearchForm.getSoftwareId());
                    }
                    // Search by Software type.
                    List<Integer> typeList = softwareSearchForm.getSoftwareTypes();
                    if (!typeList.isEmpty()) {
                        searchCriteriaMap.put(SOFTWARE_TYPES_CONTAIN, typeList);
                    }

                    // Search by custom fields
                    if (!softwareSearchForm.getAttrId().isEmpty() && !softwareSearchForm.getAttrValue().isEmpty()) {
                        searchCriteriaMap.put("attrId", softwareSearchForm.getAttrId());
                        searchCriteriaMap.put("attrValue", softwareSearchForm.getAttrValue());
                    }
                }
            }
        }
    }

    /**
     * This would take searchCriteriaMap and compose the sql queries.
     */
    public void applyMap(QueryCriteria query) {
        if (searchCriteriaMap == null) {
            return;
        }
        // For manufacturer ID
        if (searchCriteriaMap.containsKey("manufacturerIdEquals")) {
            query.appendWhereClause("s.manufacturer_company_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get("manufacturerIdEquals")));
        }
        // For Software ID
        if (searchCriteriaMap.containsKey(SOFTWARE_ID_EQUALS)) {
            query.appendWhereClause("s.software_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(SOFTWARE_ID_EQUALS)));
        }
        // For Software type
        if (searchCriteriaMap.containsKey(SOFTWARE_TYPES_CONTAIN)) {
            query.appendWhereClause("s.software_type in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get(SOFTWARE_TYPES_CONTAIN)) + ")");
        }
        // For custom fields
        if (searchCriteriaMap.containsKey("attrId") && searchCriteriaMap.containsKey("attrValue")) {
            query.appendWhereClause("s.software_id in (select object_id from object_attribute_value where attribute_id = "+
                    SqlUtils.encodeInteger(searchCriteriaMap.get("attrId")) + " and lower(attr_value) like lower('%"
                    + SqlUtils.encodeString(searchCriteriaMap.get("attrValue")) +"%'))");
        }
    }
}
