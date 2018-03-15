/*
 * Copyright 2015 Kwoksys
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
package com.kwoksys.biz.hardware.core;

import com.kwoksys.action.hardware.HardwareSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.SqlUtils;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is for building search queries.
 */
public class HardwareSearch extends BaseSearch {

    public static final String HARDWARE_ID_EQUALS = "hardwareIdEquals";

    public static final String HARDWARE_ID_NOT_EQUALS = "hardwareIdNotEquals";

    public static final String HARDWARE_NAME_EQUALS = "hardwareNameEquals";

    public static final String HARDWARE_NAME_BEGINS_WITH = "hardwareNameBeginsWith";

    public static final String HARDWARE_TYPE_CONTAINS = "hardwareTypeContains";

    public static final String HARDWARE_PURCHASED_BEFORE = "purchasedBefore";

    public static final String HARDWARE_PURCHASED_AFTER = "purchasedAfter";

    public static final String HARDWARE_MODEL_NAME_CONTAINS = "modelNameContains";
    public static final String HARDWARE_MODEL_NAME_EQUALS = "modelNameEquals";

    public static final String HARDWARE_MODEL_NUMBER_EQUALS = "modelNumberEquals";

    public static final String HARDWARE_SERIAL_NUMBER_CONTAINS = "serialNumberContains";
    public static final String HARDWARE_SERIAL_NUMBER_EQUALS = "serialNumberEquals";

    public static final String HARDWARE_WARRANTY_EXPIRED = "warrantyExpired";
    public static final String HARDWARE_WARRANTY_NOT_EXPIRED = "warrantyNotExpired";
    public static final String HARDWARE_WARRANTY_NOT_SET = "warrantyNotSet";

    public static final String HARDWARE_VENDOR_EQUALS = "vendorIdEquals";
    public static final String HARDWARE_MANUFACTURER_EQUALS = "manufacturerIdEquals";

    public HardwareSearch(RequestContext requestContext, String sessionKey) {
        super(requestContext, sessionKey);
    }

    public HardwareSearch() {}

    /**
     * This would generate searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(HardwareSearchForm hardwareSearchForm) {
        String cmd = requestContext.getParameterString("cmd");

        if (!cmd.isEmpty()) {
            reset();

            if (cmd.equals("filter")) {
                String hardwareTypeFilter = requestContext.getParameterString("hardwareTypeFilter");
                hardwareSearchForm.setHardwareTypeFilter(hardwareTypeFilter);

                if (!hardwareTypeFilter.isEmpty()) {
                    Integer hardwareType = NumberUtils.replaceNull(hardwareTypeFilter);
                    searchCriteriaMap.put(HardwareSearch.HARDWARE_TYPE_CONTAINS, Arrays.asList(hardwareType));
                }
            } else {
                hardwareSearchForm.setRequest(requestContext);

                if (cmd.equals("groupBy")) {
                    String warrantyExpire = requestContext.getParameterString("warrantyExpire");
                    if (!warrantyExpire.isEmpty()) {
                        // warrantyExpire url parameter is in the format of "start_end", e.g. "30_60" means warranty expires
                        // between 30 to 60 days. Or in the format of "end", e.g. "30", meaning warranty expires in 30 days.
                        String[] interval = warrantyExpire.split("_");
                        if (interval.length == 1) {
                            searchCriteriaMap.put("warrantyExpireBefore", interval[0]);
                        } else {
                            searchCriteriaMap.put("warrantyExpireAfter", interval[0]);
                            searchCriteriaMap.put("warrantyExpireBefore", interval[1]);
                        }
                    }

                    String hardwareTypeFilter = requestContext.getParameterString("hardwareType");
                    hardwareSearchForm.setHardwareTypeFilter(hardwareTypeFilter);

                    if (!hardwareTypeFilter.isEmpty()) {
                        Integer hardwareType = NumberUtils.replaceNull(hardwareTypeFilter);
                        hardwareSearchForm.setHardwareTypes(Arrays.asList(hardwareType));
                        searchCriteriaMap.put(HardwareSearch.HARDWARE_TYPE_CONTAINS, hardwareSearchForm.getHardwareTypes());
                    }
                    Integer hardwareStatus = requestContext.getParameterInteger("hardwareStatus");
                    if (hardwareStatus != null) {
                        hardwareSearchForm.setHardwareStatus(Arrays.asList(hardwareStatus));
                        searchCriteriaMap.put("hardwareStatusContains", hardwareSearchForm.getHardwareStatus());
                    }
                    Integer hardwareLocation = requestContext.getParameterInteger("hardwareLocation");
                    if (hardwareLocation != null) {
                        hardwareSearchForm.setHardwareLocation(Arrays.asList(hardwareLocation));
                        searchCriteriaMap.put("hardwareLocationContains", hardwareSearchForm.getHardwareLocation());
                    }

                } else if (cmd.equals("search")) {
                    // Search by Hardware Id equals something.
                    if (!hardwareSearchForm.getHardwareId().isEmpty()) {
                        searchCriteriaMap.put(HARDWARE_ID_EQUALS, hardwareSearchForm.getHardwareId());
                    }

                    // Search by Hardware model name equals something.
                    if (!hardwareSearchForm.getHardwareName().isEmpty()) {
                        if (hardwareSearchForm.getHardwareNameCriteria().equals("equals")) {
                            searchCriteriaMap.put(HARDWARE_NAME_EQUALS, hardwareSearchForm.getHardwareName());
                        } else if (hardwareSearchForm.getHardwareNameCriteria().equals("contains")) {
                            searchCriteriaMap.put("hardwareNameContains", hardwareSearchForm.getHardwareName());
                        } else if (hardwareSearchForm.getHardwareNameCriteria().equals("begins")) {
                            searchCriteriaMap.put(HARDWARE_NAME_BEGINS_WITH, hardwareSearchForm.getHardwareName());
                        }
                    }
                    // Search by hardware description
                    if (!hardwareSearchForm.getDescription().isEmpty()) {
                        searchCriteriaMap.put("hardwareDescription", hardwareSearchForm.getDescription());
                    }

                    // Search by Hardware model name equals something.
                    if (!hardwareSearchForm.getHardwareModelName().isEmpty()) {
                        searchCriteriaMap.put(HARDWARE_MODEL_NAME_CONTAINS, hardwareSearchForm.getHardwareModelName());
                    }

                    // Search by Hardware model number equals something.
                    if (!hardwareSearchForm.getHardwareModelNumber().isEmpty()) {
                        searchCriteriaMap.put("modelNumberContains", hardwareSearchForm.getHardwareModelNumber());
                    }

                    // Search by Hardware serial number equals something.
                    if (!hardwareSearchForm.getSerialNumber().isEmpty()) {
                        searchCriteriaMap.put(HARDWARE_SERIAL_NUMBER_CONTAINS, hardwareSearchForm.getSerialNumber());
                    }

                    // Search by Hardware Owner (userDisplayName) contains something.
                    if (!hardwareSearchForm.getHardwareOwner().isEmpty()) {
                        searchCriteriaMap.put("hardwareOwnerContains", hardwareSearchForm.getHardwareOwner());
                    }

                    // Search by Hardware type.
                    List<Integer> typeList = hardwareSearchForm.getHardwareTypes();
                    if (!typeList.isEmpty()) {
                        searchCriteriaMap.put(HARDWARE_TYPE_CONTAINS, typeList);
                    }
                    // Search by Hardware status.
                    List<Integer> statusList = hardwareSearchForm.getHardwareStatus();
                    if (!statusList.isEmpty()) {
                        searchCriteriaMap.put("hardwareStatusContains", statusList);
                    }
                    // Search by Hardware location.
                    List<Integer> locationList = hardwareSearchForm.getHardwareLocation();
                    if (!locationList.isEmpty()) {
                        searchCriteriaMap.put("hardwareLocationContains", locationList);
                    }
                    // Search by Hardware Manufacturer
                    if (!hardwareSearchForm.getManufacturerId().equals(0)) {
                        searchCriteriaMap.put(HARDWARE_MANUFACTURER_EQUALS, hardwareSearchForm.getManufacturerId());
                    }
                    // Search by Hardware Manufacturer
                    if (!hardwareSearchForm.getVendorId().equals(0)) {
                        searchCriteriaMap.put(HARDWARE_VENDOR_EQUALS, hardwareSearchForm.getVendorId());
                    }

                    // Search by purchase date
                    String purchasedAfterDate = hardwareSearchForm.getPurchasedAfterDate();
                    String purchasedAfterMonth = hardwareSearchForm.getPurchasedAfterMonth();
                    String purchasedAfterYear = hardwareSearchForm.getPurchasedAfterYear();

                    if (DatetimeUtils.isValidDate(purchasedAfterYear, purchasedAfterMonth, purchasedAfterDate)) {
                        searchCriteriaMap.put(HARDWARE_PURCHASED_AFTER, purchasedAfterYear + "-" + purchasedAfterMonth + "-" + purchasedAfterDate);
                    }

                    // Search by purchased before
                    String purchasedBeforeDate = hardwareSearchForm.getPurchasedBeforeDate();
                    String purchasedBeforeMonth = hardwareSearchForm.getPurchasedBeforeMonth();
                    String purchasedBeforeYear = hardwareSearchForm.getPurchasedBeforeYear();

                    if (DatetimeUtils.isValidDate(purchasedBeforeYear, purchasedBeforeMonth, purchasedBeforeDate)) {
                        searchCriteriaMap.put(HARDWARE_PURCHASED_BEFORE, purchasedBeforeYear + "-" + purchasedBeforeMonth + "-" + purchasedBeforeDate);
                    }

                    boolean warrantyExpired = requestContext.getParameterBoolean(HARDWARE_WARRANTY_EXPIRED);
                    boolean warrantyNotExpired = requestContext.getParameterBoolean(HARDWARE_WARRANTY_NOT_EXPIRED);
                    boolean warrantyNotSet = requestContext.getParameterBoolean(HARDWARE_WARRANTY_NOT_SET);

                    if (warrantyExpired && warrantyNotExpired && warrantyNotSet) {
                        // Search all
                    } else {
                        if (warrantyExpired) {
                            searchCriteriaMap.put(HARDWARE_WARRANTY_EXPIRED, true);
                        }
                        if (warrantyNotExpired) {
                            searchCriteriaMap.put(HARDWARE_WARRANTY_NOT_EXPIRED, true);
                        }
                        if (warrantyNotSet) {
                            searchCriteriaMap.put(HARDWARE_WARRANTY_NOT_SET, true);
                        }
                    }

                    // Search by custom fields
                    if (!hardwareSearchForm.getAttrId().isEmpty() && !hardwareSearchForm.getAttrValue().isEmpty()) {
                        searchCriteriaMap.put("attrId", hardwareSearchForm.getAttrId());
                        searchCriteriaMap.put("attrValue", hardwareSearchForm.getAttrValue());
                    }

                    // Search by hardware components
                    if (!hardwareSearchForm.getCompTypeId().isEmpty() && !hardwareSearchForm.getCompValue().isEmpty()) {
                        searchCriteriaMap.put("compTypeId", hardwareSearchForm.getCompTypeId());
                        searchCriteriaMap.put("compValue", hardwareSearchForm.getCompValue());
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
        // For Hardware Id
        if (searchCriteriaMap.containsKey(HARDWARE_ID_EQUALS)) {
            query.appendWhereClause("ah.hardware_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(HARDWARE_ID_EQUALS)));
        }
        // For custom fields
        if (searchCriteriaMap.containsKey("attrId") && searchCriteriaMap.containsKey("attrValue")) {
            query.appendWhereClause("ah.hardware_id in (select object_id from object_attribute_value where attribute_id = "+
                    SqlUtils.encodeInteger(searchCriteriaMap.get("attrId")) + " and lower(attr_value) like lower('%"
                    + SqlUtils.encodeString(searchCriteriaMap.get("attrValue")) +"%'))");
        }
        // For hardware components
        if (searchCriteriaMap.containsKey("compTypeId") && searchCriteriaMap.containsKey("compValue")) {
            query.appendWhereClause("ah.hardware_id in (select hardware_id from asset_hardware_component where hardware_component_type = "+
                    SqlUtils.encodeInteger(searchCriteriaMap.get("compTypeId")) + " and lower(comp_description) like lower('%"
                    + SqlUtils.encodeString(searchCriteriaMap.get("compValue")) +"%'))");
        }
        // For Hardware name (without "%")
        if (searchCriteriaMap.containsKey(HARDWARE_NAME_EQUALS)) {
            query.appendWhereClause("lower(ah.hardware_name) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_NAME_EQUALS)) + "')");
        }

        if (searchCriteriaMap.containsKey(HARDWARE_ID_NOT_EQUALS)) {
            query.appendWhereClause("ah.hardware_id != " + SqlUtils.encodeInteger(searchCriteriaMap.get(HARDWARE_ID_NOT_EQUALS)));
        }        

        // For Hardware name
        if (searchCriteriaMap.containsKey("hardwareNameContains")) {
            query.appendWhereClause("lower(ah.hardware_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("hardwareNameContains")) + "%')");
        }

        // For Hardware name
        if (searchCriteriaMap.containsKey(HARDWARE_NAME_BEGINS_WITH)) {
            query.appendWhereClause("lower(ah.hardware_name) like lower('" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_NAME_BEGINS_WITH)) + "%')");
        }

        // For hardware description
        if (searchCriteriaMap.containsKey("hardwareDescription")) {
            query.appendWhereClause("lower(ah.hardware_description) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("hardwareDescription")) + "%')");
        }
        // For Hardware model name
        if (searchCriteriaMap.containsKey(HARDWARE_MODEL_NAME_CONTAINS)) {
            query.appendWhereClause("lower(ah.hardware_model_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_MODEL_NAME_CONTAINS)) + "%')");
        }
        if (searchCriteriaMap.containsKey(HARDWARE_MODEL_NAME_EQUALS)) {
            if (((String)searchCriteriaMap.get(HARDWARE_MODEL_NAME_EQUALS)).isEmpty()) {
                query.appendWhereClause("ah.hardware_model_name is null");
            } else {
                query.appendWhereClause("lower(ah.hardware_model_name) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_MODEL_NAME_EQUALS)) + "')");
            }
        }
        // For Hardware model number
        if (searchCriteriaMap.containsKey("modelNumberContains")) {
            query.appendWhereClause("lower(ah.hardware_model_number) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("modelNumberContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey(HARDWARE_MODEL_NUMBER_EQUALS)) {
            if (((String)searchCriteriaMap.get(HARDWARE_MODEL_NUMBER_EQUALS)).isEmpty()) {
                query.appendWhereClause("ah.hardware_model_number is null");
            } else {
                query.appendWhereClause("lower(ah.hardware_model_number) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_MODEL_NUMBER_EQUALS)) + "')");
            }
        }
        // For Hardware serial number
        if (searchCriteriaMap.containsKey(HARDWARE_SERIAL_NUMBER_CONTAINS)) {
            query.appendWhereClause("lower(ah.hardware_serial_number) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("serialNumberContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey(HARDWARE_SERIAL_NUMBER_EQUALS)) {
            query.appendWhereClause("lower(ah.hardware_serial_number) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_SERIAL_NUMBER_EQUALS)) + "')");
        }
        // For Hardware owner number
        if (searchCriteriaMap.containsKey("hardwareOwnerContains")) {
            query.appendWhereClause(HardwareQueries.getOrderByColumn("hardware_owner_name") + " like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("hardwareOwnerContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey("hardwareOwnerId")) {
            query.appendWhereClause("ah.hardware_owner_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get("hardwareOwnerId")));
        }
        // For Hardware type
        if (searchCriteriaMap.containsKey(HARDWARE_TYPE_CONTAINS)) {
            query.appendWhereClause("ah.hardware_type in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get(HARDWARE_TYPE_CONTAINS)) + ")");
        }
        // For Hardware status
        if (searchCriteriaMap.containsKey("hardwareStatusContains")) {
            query.appendWhereClause("ah.hardware_status in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get("hardwareStatusContains")) + ")");
        }
        // For Hardware location
        if (searchCriteriaMap.containsKey("hardwareLocationContains")) {
            query.appendWhereClause("ah.hardware_location in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get("hardwareLocationContains")) + ")");
        }
        // For manufacturer by id
        if (searchCriteriaMap.containsKey(HARDWARE_MANUFACTURER_EQUALS)) {
            if (searchCriteriaMap.get(HARDWARE_MANUFACTURER_EQUALS).equals(0)) {
                query.appendWhereClause("mftr.company_id is null");
            } else {
                query.appendWhereClause("mftr.company_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(HARDWARE_MANUFACTURER_EQUALS)));
            }
        }
        // For vendor by id
        if (searchCriteriaMap.containsKey(HARDWARE_VENDOR_EQUALS)) {
            if (searchCriteriaMap.get(HARDWARE_VENDOR_EQUALS).equals(0)) {
                query.appendWhereClause("vndr.company_id is null");
            } else {
                query.appendWhereClause("vndr.company_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(HARDWARE_VENDOR_EQUALS)));
            }
        }

        // Hardware purchased after this date.
        if (searchCriteriaMap.containsKey(HARDWARE_PURCHASED_AFTER)) {
            query.appendWhereClause("ah.hardware_purchase_date >= '" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_PURCHASED_AFTER)) + "'");
        }
        // Hardware purchased before this date.
        if (searchCriteriaMap.containsKey(HARDWARE_PURCHASED_BEFORE)) {
            query.appendWhereClause("ah.hardware_purchase_date <= '" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_PURCHASED_BEFORE)) + "'");
        }

        // Warranty expiration filter
        Set<String> warranty = new HashSet<>();
        if (searchCriteriaMap.containsKey(HARDWARE_WARRANTY_EXPIRED)) {
            warranty.add("ah.hardware_warranty_expire_date < now()");
        }
        if (searchCriteriaMap.containsKey(HARDWARE_WARRANTY_NOT_EXPIRED)) {
            warranty.add("ah.hardware_warranty_expire_date > now()");

        } else if (searchCriteriaMap.containsKey(HARDWARE_WARRANTY_NOT_SET)) {
            warranty.add("ah.hardware_warranty_expire_date is null");
        }

        String warrantyWhereClause = StringUtils.join(warranty, " or ");

        if (!warrantyWhereClause.isEmpty()) {
            query.appendWhereClause("(" + warrantyWhereClause + ")");
        }

        if (searchCriteriaMap.containsKey("warrantyExpireAfter")) {
            query.appendWhereClause("ah.hardware_warranty_expire_date > now()::timestamp + '+"
                    + SqlUtils.encodeInteger(searchCriteriaMap.get("warrantyExpireAfter")) +" day'::interval");
        }
        if (searchCriteriaMap.containsKey("warrantyExpireBefore")) {
            query.appendWhereClause("ah.hardware_warranty_expire_date < now()::timestamp + '+"
                    + SqlUtils.encodeInteger(searchCriteriaMap.get("warrantyExpireBefore")) +" day'::interval");
        }
    }
}