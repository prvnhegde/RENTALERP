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
package com.kwoksys.biz.reports.dao;

import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.biz.system.core.ObjectTypes;

/**
 * ReportQueries
 */
public class ReportQueries {

    public static String getSoftwareUsageOrderByColumn(String column) {
        if (column.equals("software_name")) {
            return "lower(software_name)";

        } else if (column.equals("hardware_name")) {
            return "lower(hardware_name)";

        } else {
            return column;
        }
    }

    public static String getHardwareMemberOrderByColumn(String column) {
        if (column.equals("hardware_name")) {
            return "lower(hm.hardware_name)";
        } else {
            return column;
        }
    }

    public static String getHardwareLicenseOrderByColumn(String column) {
        if (column.equals("hardware_name")) {
            return "lower(ah.hardware_name)";
        } else if (column.equals("software_name")) {
            return "lower(s.software_name)";
        } else if (column.equals("license_key")) {
            return "asl.license_key";
        } else {
            return column;
        }
    }

    public static String selectHardwareMembersCountQuery(QueryCriteria searchQuery) {
        return "select count(om.object_id) as row_count " +
            "from object_map om " +
                "left join asset_hardware hm on om.object_id=hm.hardware_id " +
            "where om.object_type_id=" + ObjectTypes.HARDWARE + " and om.linked_object_type_id=" + ObjectTypes.HARDWARE
            + selectHardwareMembersCriteria(searchQuery);
    }

    public static String selectHardwareMembersQuery(QueryCriteria searchQuery, QueryCriteria query) {
        return "select hm.hardware_id, hm.hardware_name, lhm.hardware_id as hardware_member_id, lhm.hardware_name as hardware_member_name " +
            "from object_map om " +
                "left join asset_hardware hm on om.object_id=hm.hardware_id " +
                "left join asset_hardware lhm on om.linked_object_id=lhm.hardware_id " +
            "where om.object_type_id=" + ObjectTypes.HARDWARE + " and om.linked_object_type_id=" + ObjectTypes.HARDWARE
                + selectHardwareMembersCriteria(searchQuery)
                + query.createAndClause();
    }

    public static String getHardwareLicenseCountQuery(QueryCriteria query) {
        return "select count(ah.hardware_id) as row_count " +
                "from asset_hardware ah " +
                "left join asset_map am on ah.hardware_id = am.hardware_id " +
                "left join asset_software s on am.software_id = s.software_id " +
                "left join asset_software_licenses asl on am.license_id = asl.license_id "
                + query.createWhereCountClause();
    }

    public static String getHardwareLicensesQuery(QueryCriteria query) {
        return "select ah.hardware_id, ah.hardware_name, am.map_id, am.software_id, am.license_id, s.software_name, " +
                "asl.license_key, asl.license_note " +
                "from asset_hardware ah " +
                "left join asset_map am on ah.hardware_id = am.hardware_id " +
                "left join asset_software s on am.software_id = s.software_id " +
                "left join asset_software_licenses asl on am.license_id = asl.license_id "
                + query.createWhereClause();
    }

    private static String selectHardwareMembersCriteria(QueryCriteria query) {
        return " and om.object_id in (select hardware_id from asset_hardware_view ah " +
            "left join company mftr on ah.manufacturer_company_id = mftr.company_id " +
            "left join company vndr on ah.vendor_company_id = vndr.company_id " +
            "left join attribute_field_view hwloc on hwloc.attribute_field_id = ah.hardware_location "
            + query.createWhereClause() + ")";
    }

    public static String selectSoftwareUsageCountQuery(QueryCriteria query) {
        return "select count(s.software_id) as row_count from asset_software s " +
                "left join asset_map m on s.software_id=m.software_id " +
                "left join asset_hardware_view h on m.hardware_id=h.hardware_id " + query.createWhereCountClause();
    }

    public static String selectSoftwareUsageQuery(QueryCriteria query) {
        return "select s.software_id, s.software_name, s.software_description, h.hardware_id, h.hardware_name, h.hardware_owner_id, " +
                "h.hardware_owner_display_name, st.attribute_field_name as software_type_name " +
                "from asset_software s " +
                "left join asset_map m on s.software_id=m.software_id " +
                "left join asset_hardware_view h on m.hardware_id=h.hardware_id " +
                "left join attribute_field_view st on st.attribute_field_id = s.software_type "
                + query.createWhereClause();
    }
}
