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
package com.kwoksys.biz.hardware.dao;

import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.biz.system.core.configs.ConfigManager;

/**
 * Hardware queries
 */
public class HardwareQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals(Hardware.HARDWARE_NAME)) {
            return "lower(hardware_name)";

        } else if (column.equals(Hardware.LOCATION)) {
            return "lower(hwloc.attribute_field_name)";

        } else if (column.equals("software_name")) {
            return "lower(software_name)";

        } else if (column.equals("license_key")) {
            return "lower(asl.license_key)";

        } else if (column.equals("hardware_serial_number")) {
            return "lower(hardware_serial_number)";

        } else if (column.equals(Hardware.MODEL_NAME)) {
            return "lower(hardware_model_name)";

        } else if (column.equals(Hardware.MODEL_NUMBER)) {
            return "lower(hardware_model_number)";

        } else if (column.equals(Hardware.OWNER_NAME)) {
            return ConfigManager.system.getUsernameDisplay().equals(AdminUtils.USER_USERNAME) ?
                    "lower(hardware_owner_username)" : "lower(hardware_owner_display_name)";

        } else if (column.equals("attribute_field_name")) {
            return "lower(af.attribute_field_name)";

        } else if (column.equals("comp_name")) {
            // Hardware component name
            return "lower(af.attribute_field_name)";

        } else {
            return column;
        }
    }

    /**
     * Return all hardware.
     */
    public static String selectHardwareListQuery(QueryCriteria query) {
        return "select ah.hardware_id, ah.hardware_number, ah.hardware_name, ah.hardware_description, " +
                "ah.hardware_owner_id, " +
                "ah.hardware_type, ah.hardware_status, ah.hardware_model_name, " +
                "ah.hardware_model_number, ah.hardware_serial_number, ah.hardware_cost as hardware_purchase_price, ah.hardware_last_service_date, " +
                "ah.hardware_purchase_date, ah.hardware_warranty_expire_date, ah.hardware_location, " +
                "ah.software_count, ah.file_count, ah.component_count," +
                "ah.manufacturer_company_id, mftr.company_name as hardware_manufacturer_name, " +
                "ah.vendor_company_id, vndr.company_name as hardware_vendor_name, " +                                        
                "ah.creator, ah.creation_date, ah.creator_username, ah.creator_display_name, " +
                "ah.modifier, ah.modification_date, ah.modifier_username, ah.modifier_display_name " +
                "from asset_hardware_view ah " +
                "left outer join company mftr on ah.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on ah.vendor_company_id = vndr.company_id " +
                "left outer join attribute_field_view hwloc on hwloc.attribute_field_id = ah.hardware_location " 
                + query.createWhereClause();
    }

    /**
     * Return all linked Hardware.
     */
    public static String selectLinkedHardwareListQuery(QueryCriteria query) {
        return selectHardwareListQuery(new QueryCriteria()) +
                "where ah.hardware_id in (select om.linked_object_id from object_map om where om.object_id=? and om.object_type_id=? " +
                "and om.linked_object_type_id=?) "
                + query.createWhereClause();
    }

    public static String selectObjectHardwareListQuery(QueryCriteria query) {
        return selectHardwareListQuery(new QueryCriteria()) +
                "where ah.hardware_id in (select om.object_id from object_map om where om.linked_object_id=? and om.linked_object_type_id=? " +
                "and om.object_type_id=?) "
                + query.createAndClause();
    }

    /**
     * Return Hardware count.
     *
     * @return ..
     */
    public static String getHardwareCountQuery(QueryCriteria query) {
        return "select count(ah.hardware_id) as row_count " +
                "from asset_hardware_view ah " +  
                "left outer join company mftr on ah.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on ah.vendor_company_id = vndr.company_id "
                + query.createWhereCountClause();
    }

    /**
     * Return detail for a specific hardware.
     */
    public static String selectHardwareDetailQuery() {
        return selectHardwareListQuery(new QueryCriteria()) +
                "where ah.hardware_id = ?";
    }

    public static String selectWarrantyExpirationCountQuery() {
        return "select '0_30' as interval, count(hardware_id) as count from asset_hardware h " +
                "where h.hardware_warranty_expire_date > now()::timestamp + '+0 day'::interval " +
                "and h.hardware_warranty_expire_date < now()::timestamp + '+30 day'::interval " +
                "union " +
                "select '30_60' as interval, count(hardware_id) from asset_hardware h " +
                "where h.hardware_warranty_expire_date > now()::timestamp + '+30 day'::interval " +
                "and h.hardware_warranty_expire_date < now()::timestamp + '+60 day'::interval " +
                "union " +
                "select '60_90' as interval, count(hardware_id) from asset_hardware h " +
                "where h.hardware_warranty_expire_date > now()::timestamp + '+60 day'::interval " +
                "and h.hardware_warranty_expire_date < now()::timestamp + '+90 day'::interval " +
                "union " +
                "select '0' as interval, count(hardware_id) from asset_hardware h " +
                "where h.hardware_warranty_expire_date < now()::timestamp + '+0 day'::interval";
    }

    /**
     * Return number of hardware grouped by type.
     */
    public static String selectHardwareTypeCountQuery(QueryCriteria query) {
        return "select h.hardware_type, count(h.hardware_id) as hardware_count " +
                "from asset_hardware h " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='hardware' and an.attribute_name='hardware_type' " +
                "and an.attribute_id = af.attribute_id) af on h.hardware_type = af.attribute_field_id " +
                "group by hardware_type, af.attribute_field_name " + query.createClause();
    }

    /**
     * Return number of hardware grouped by status.
     */
    public static String selectHardwareCountByStatusQuery(QueryCriteria query) {
        return "select h.hardware_status, count(h.hardware_id) as hardware_count " +
                "from asset_hardware h " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='hardware' and an.attribute_name='hardware_status' " +
                "and an.attribute_id = af.attribute_id) af on h.hardware_status = af.attribute_field_id " +
                "group by hardware_status, af.attribute_field_name " + query.createClause();
    }

    /**
     * Return number of hardware grouped by location.
     */
    public static String selectHardwareCountByLocationQuery(QueryCriteria query) {
        return "select h.hardware_location, count(h.hardware_id) as hardware_count " +
                "from asset_hardware h " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='hardware' and an.attribute_name='hardware_location' " +
                "and an.attribute_id = af.attribute_id) af on h.hardware_location = af.attribute_field_id " +
                "group by hardware_location, af.attribute_field_name " + query.createClause();
    }

    /**
     * Return all Software in the system.
     *
     * @return ..
     */
    public static String selectHardwareAvailableSoftwareQuery(QueryCriteria query) {
        return "select software_id, software_name from asset_software "+ query.createWhereClause();
    }

    public static String selectHardwareAvailableLicensesQuery(QueryCriteria query) {
        return "select asl.software_id, asl.license_id, asl.license_key, asl.license_note, asl.license_entitlement " +
                "from asset_software_licenses asl " +
                "left outer join asset_map am on asl.license_id = am.license_id " +
                "where asl.software_id = ? " +
                "group by asl.software_id, asl.license_id, asl.license_key, asl.license_note, asl.license_entitlement " +
                "having count(am.license_entitlement) < asl.license_entitlement " + query.createClause();
    }

    /**
     * Return Software Licenses installed on this Hardware.
     *
     * @return ..
     */
    public static String selectInstalledLicenseQuery(QueryCriteria query) {
        return "select am.map_id, am.software_id, am.license_id, s.software_name, asl.license_key, asl.license_note " +
                "from asset_map am " +
                "left outer join asset_software s on am.software_id = s.software_id " +
                "left outer join asset_software_licenses asl on am.license_id = asl.license_id " +
                "where am.hardware_id  =?" + query.createAndClause();
    }

    /**
     * Return Components for a particular Hardware.
     *
     * @return ..
     */
    public static String selectHardwareComponentsQuery(QueryCriteria query) {
        return "select hc.comp_id, af.attribute_field_name as comp_name, comp_description " +
                "from asset_hardware_component hc " +
                "left outer join attribute_field_view af on hc.hardware_component_type = af.attribute_field_id " +
                "where hc.hardware_id = ?" + query.createAndClause();
    }

    /**
     * Return a specific hardware component.
     *
     * @return ..
     */
    public static String selectHardwareComponentDetailQuery() {
        return "select hc.comp_id, hc.hardware_id, hc.hardware_component_type, hc.comp_description " +
                "from asset_hardware_component hc " +
                "where hc.hardware_id = ? and hc.comp_id = ?";
    }

    /**
     * Add a new Hardware.
     *
     * @return ..
     */
    public static String insertHardwareQuery() {
        return "{call sp_hardware_add(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Update the detail for a specific hardware.
     *
     * @return ..
     */
    public static String updateHardwareQuery() {
        return "{call sp_hardware_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Delete hardware.
     */
    public static String deleteHardwareQuery() {
        return "{call sp_hardware_delete(?,?)}";
    }

    /**
     * Rent Out hardware.
     */
    public static String rentOutHardwareQuery() {
        return "{call sp_rental_add(?,?,?,?,?,?,?,?,?)}";
    }
    
    /**
     * Hardware Return.
     */
    public static String hardwareReturnQuery() {
        return "{call sp_hardware_rental_update(?,?)}";
    }
    
    /**
     * This would assign a software license to a hardware.
     *
     * @return ..
     */
    public static String insertAssignLicenseQuery() {
        return "{call sp_software_license_assign(?,?,?,?,?)}";
    }

    /**
     * This would unassign a software license to a hardware.
     */
    public static String deleteAssignedLicenseQuery() {
        return "{call sp_asset_map_delete(?)}";
    }

    /**
     * Update the detail for a specific hardware component.
     *
     * @return ..
     */
    public static String insertHardwareComponentQuery() {
        return "{call sp_hardware_component_add(?,?,?,?,?)}";
    }

    /**
     * Update the detail for a specific hardware component.
     *
     * @return ..
     */
    public static String updateHardwareComponentQuery() {
        return "{call sp_hardware_component_update(?,?,?,?,?)}";
    }

    /**
     * Delete a hardware component.
     *
     * @return ..
     */
    public static String deleteHardwareComponentQuery() {
        return "{call sp_hardware_component_delete(?,?,?)}";
    }

    /**
     * Reset Hardware assigned Software counter.
     *
     * @return ..
     */
    public static String updateHardwareSoftwareCountQuery() {
        return "{call sp_hardware_count_software_update(?)}";
    }

    /**
     * Reset Hardware File counter.
     *
     * @return ..
     */
    public static String updateHardwareFileCountQuery() {
        return "{call sp_hardware_count_file_update(?,?)}";
    }

    /**
     * Reset Hardware Component counter.
     *
     * @return ..
     */
    public static String updateHardwareComponentCountQuery() {
        return "{call sp_hardware_count_component_update(?)}";
    }
    
    
//Rental - START
    
    public static String selectHardwareRenturnListQuery(QueryCriteria query) {
        return "select ah.hardware_id, ah.hardware_number, ah.hardware_name, ah.hardware_description, " +
                "ah.hardware_owner_id, " +
                "ah.hardware_type, ah.hardware_status, ah.hardware_model_name, " +
                "ah.hardware_model_number, ah.hardware_serial_number, ah.hardware_cost as hardware_purchase_price, ah.hardware_last_service_date, " +
                "ah.hardware_purchase_date, ah.hardware_warranty_expire_date, ah.hardware_location, " +
                "ah.software_count, ah.file_count, ah.component_count," +
                "ah.manufacturer_company_id, mftr.company_name as hardware_manufacturer_name, " +
                "ah.vendor_company_id, vndr.company_name as hardware_vendor_name, " +                                        
                "ah.creator, ah.creation_date, ah.creator_username, ah.creator_display_name, " +
                "ah.modifier, ah.modification_date, ah.modifier_username, ah.modifier_display_name " +
                "from (Select * from asset_hardware_view ah where ah.hardware_id in (Select hardware_id from asset_rental where rented='Y')) as ah " +
                "left outer join company mftr on ah.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on ah.vendor_company_id = vndr.company_id " +
                "left outer join attribute_field_view hwloc on hwloc.attribute_field_id = ah.hardware_location "
                + query.createWhereClause();
    }
    
    
    /**
     * Return Hardware count.
     *
     * @return ..
     */
    public static String getHardwareReturnCountQuery(QueryCriteria query) {
        return "select count(ah.hardware_id) as row_count " +
                "from (Select * from asset_hardware_view ah where ah.hardware_id in (Select hardware_id from asset_rental where rented='Y')) as ah " +  
                "left outer join company mftr on ah.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on ah.vendor_company_id = vndr.company_id " 
                + query.createWhereCountClause();
    }
    
    public static String selectHardwareRentoutListQuery(QueryCriteria query) {
        return "select ah.hardware_id, ah.hardware_number, ah.hardware_name, ah.hardware_description, " +
                "ah.hardware_owner_id, " +
                "ah.hardware_type, ah.hardware_status, ah.hardware_model_name, " +
                "ah.hardware_model_number, ah.hardware_serial_number, ah.hardware_cost as hardware_purchase_price, ah.hardware_last_service_date, " +
                "ah.hardware_purchase_date, ah.hardware_warranty_expire_date, ah.hardware_location, " +
                "ah.software_count, ah.file_count, ah.component_count," +
                "ah.manufacturer_company_id, mftr.company_name as hardware_manufacturer_name, " +
                "ah.vendor_company_id, vndr.company_name as hardware_vendor_name, " +                                        
                "ah.creator, ah.creation_date, ah.creator_username, ah.creator_display_name, " +
                "ah.modifier, ah.modification_date, ah.modifier_username, ah.modifier_display_name " +
                "from asset_hardware_view ah " +
                "left outer join company mftr on ah.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on ah.vendor_company_id = vndr.company_id " +
                "left outer join attribute_field_view hwloc on hwloc.attribute_field_id = ah.hardware_location " +
                " where ah.hardware_status = 53 and ah.hardware_id not in (Select hardware_id from asset_rental where rented='Y')"
                + query.createWhereClause();
    }
    
    public static String selectHardwareRentoutCountQuery(QueryCriteria query) {
        return "select count(ah.hardware_id) as row_count " +
                "from asset_hardware_view ah " +  
                "left outer join company mftr on ah.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on ah.vendor_company_id = vndr.company_id " +
                " where ah.hardware_status = 53 and ah.hardware_id not in (Select hardware_id from asset_rental where rented='Y') "
                + query.createWhereCountClause();
    }
    
    public static String selectRentoutHardwareTypeCountQuery(QueryCriteria query) {
        return "select h.hardware_type, count(h.hardware_id) as hardware_count " +
                "from asset_hardware h " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='hardware' and an.attribute_name='hardware_type' " +
                "and an.attribute_id = af.attribute_id) af on h.hardware_type = af.attribute_field_id " +
                "and h.hardware_id in (select hardware_id from asset_rental) "+
                "group by hardware_type, af.attribute_field_name " + query.createClause();
    }
    
    public static String selectRentoutHardwareByCustomerCountQuery(QueryCriteria query) {
        return "select h.hardware_type, count(h.hardware_id) as hardware_count " +
                "from asset_hardware h " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='hardware' and an.attribute_name='hardware_type' " +
                "and an.attribute_id = af.attribute_id) af on h.hardware_type = af.attribute_field_id " +
                "and h.hardware_id in (select hardware_id from asset_rental) "+
                "group by hardware_type, af.attribute_field_name " + query.createClause();
    }
    
    public static String selectRentoutHardwareByDurationCountQuery(QueryCriteria query) {
        return "select h.hardware_type, count(h.hardware_id) as hardware_count " +
                "from asset_hardware h " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='hardware' and an.attribute_name='hardware_type' " +
                "and an.attribute_id = af.attribute_id) af on h.hardware_type = af.attribute_field_id " +
                "and h.hardware_id in (select hardware_id from asset_rental) "+
                "group by hardware_type, af.attribute_field_name " + query.createClause();
    }
    
    public static String selectHardwareReturnDetailQuery() {
        return "select rental_id from asset_rental where hardware_id = ? and rented = 'Y'";
    }
    
//Rental - END
}
