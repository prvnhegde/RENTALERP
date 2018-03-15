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
package com.kwoksys.biz.software.dao;

import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.framework.connections.database.QueryCriteria;

/**
 * Queries for Software module.
 */
public class SoftwareQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals(Software.NAME)) {
            return "lower(software_name)";

        } else if (column.equals(Software.VERSION)) {
            return "lower(software_version)";

        } else if (column.equals("software_manufacturer")) {
            return "lower(mftr.company_name)";

        } else if (column.equals("license_key")) {
            return "lower(license_key)";

        } else if (column.equals("hardware_name")) {
            return "lower(hardware_name)";

        } else {
            return column;
        }
    }

    /**
     * Return License count.
     */
    public static String selectSoftwareLicenseCountQuery() {
        return "select license_id, count(license_id) as license_id_count from asset_map where software_id =?  group by" +
                " license_id";
    }

    /**
     * Return all Software.
     */
    public static String selectSoftwareListQuery(QueryCriteria query) {
        // lp: license purchased
        // li: license installed
        return "select s.software_id, s.software_name, s.software_version, s.software_expire_date, " +
                "s.software_description, s.quoted_retail_price, s.quoted_oem_price, " +
                "s.software_owner_id, s.software_owner_username, s.software_owner_display_name, " +
                "s.software_type as software_type_id, st.attribute_field_name as software_type, " +
                "s.operating_system as software_platform_id, sp.attribute_field_name as software_platform, " +
                "s.manufacturer_company_id, mftr.company_name as software_manufacturer, s.vendor_company_id, vndr.company_name as software_vendor, " +
                "coalesce(lp.purchased,0) as license_purchased, coalesce(li.installed,0) as license_installed, (coalesce(lp.purchased,0)-coalesce(li.installed,0)) as license_available " +
                "from asset_software_view s " +
                "left outer join company mftr on s.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on s.vendor_company_id = vndr.company_id " +
                "left outer join (select software_id, sum(license_entitlement) as purchased from asset_software_licenses group by software_id) lp on s.software_id = lp.software_id " +
                "left outer join (select software_id, sum(license_entitlement) as installed from asset_map group by software_id) li on s.software_id = li.software_id " +
                "left outer join attribute_field_view st on st.attribute_field_id = s.software_type " +
                "left outer join attribute_field_view sp on sp.attribute_field_id = s.operating_system  "
                + query.createWhereClause();
    }

    /**
     * Return all linked Software.
     */
    public static String selectLinkedSoftwareListQuery(QueryCriteria query) {
        return selectSoftwareListQuery(new QueryCriteria()) +
                "where s.software_id in (select om.linked_object_id from object_map om where om.object_id=? and om.object_type_id=? " +
                "and om.linked_object_type_id=?) "
                + query.createWhereClause();
    }

    public static String selectObjectSoftwareListQuery(QueryCriteria query) {
        return selectSoftwareListQuery(new QueryCriteria()) +
                "where s.software_id in (select om.object_id from object_map om where om.linked_object_id=? and om.linked_object_type_id=? " +
                "and om.object_type_id=?) "
                + query.createAndClause();
    }

    /**
     * Return Software count.
     */
    public static String getSoftwareCountQuery(QueryCriteria query) {
        return "select count(s.software_id) as row_count from asset_software s " + query.createWhereCountClause();
    }

    /**
     * Return detail for a specific Software.
     */
    public static String selectSoftwareDetailQuery() {
        return "select s.software_id, s.software_name, s.software_description, " +
                "s.software_owner_id, s.software_owner_username, s.software_owner_display_name, " +
                "s.software_type, s.operating_system as software_platform, s.quoted_retail_price, s.quoted_oem_price, " +
                "s.software_version, s.software_expire_date, " + 
                "s.manufacturer_company_id, mftr.company_name as software_manufacturer, s.vendor_company_id, vndr.company_name as software_vendor, coalesce(lp.purchased,0) as license_purchased, coalesce(li.installed,0) as license_installed, (coalesce(lp.purchased,0)-coalesce(li.installed,0)) as license_available, " +
                "s.license_count, s.file_count, s.bookmark_count, " +
                "s.creator, s.creation_date, s.creator_username, s.creator_display_name, " +
                "s.modifier, s.modification_date, s.modifier_username, s.modifier_display_name " +
                "from " +
                "(select sum(license_entitlement) as installed from asset_map where software_id = ?) li, " +
                "(select sum(license_entitlement) as purchased from asset_software_licenses where software_id = ?) lp, " +
                "asset_software_view s left outer join company mftr on s.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on s.vendor_company_id = vndr.company_id " +
                "where s.software_id = ?";
    }

    /**
     * Return all Software Licenses.
     */
    public static String selectSoftwareLicenseListQuery(QueryCriteria query) {
        return "select asl.software_id, asl.license_id, asl.license_key, asl.license_note, asl.license_entitlement, ahm.hardware_id, ahm.hardware_name " +
                "from asset_software_licenses asl " +
                "left outer join (select am.software_id, am.license_id, ah.hardware_id, ah.hardware_name from asset_map am, asset_hardware ah where am.hardware_id = ah.hardware_id and am.software_id =?) ahm on asl.license_id = ahm.license_id " +
                "where asl.software_id =? " + query.createAndClause();
    }

    public static String selectSoftwareLicense() {
        return "select asl.license_id, asl.license_key, asl.license_note, asl.license_entitlement " +
                "from asset_software_licenses asl " +
                "where asl.software_id =? and asl.license_id =?";
    }

    /**
     * Return all Hardware that has this Software installed, but without licenses.
     */
    public static String selectSoftwareLicenseHardwareListQuery(QueryCriteria query) {
        return "select ah.hardware_id, ah.hardware_name " +
                "from asset_map am, asset_hardware ah " +
                "where am.hardware_id = ah.hardware_id " +
                "and am.software_id =? " +
                "and am.license_id not in (select distinct(license_id) from asset_software_licenses where software_id =?)"
                + query.createAndClause();
    }

    /**
     * Return a list of company names with the number of software made by them.
     *
     * @return ..
     */
    public static String selectSoftwareCountGroupByCompanyQuery(QueryCriteria query) {
        return "select s.manufacturer_company_id, mftr.company_name as software_manufacturer, count(s.software_id) as software_count " +
                "from asset_software s " +
                "left outer join company mftr on s.manufacturer_company_id = mftr.company_id " +
                "group by s.manufacturer_company_id, mftr.company_name " + query.createClause();
    }

    /**
     * Return all Software made by a particular company.
     */
    public static String selectCompanySoftwareQuery(QueryCriteria query) {
        return "select s.software_id, s.software_name from asset_software s " +
                "where s.manufacturer_company_id = ? "
                + query.createAndClause();
    }

    /**
     * Add a new Software.
     *
     * @return ..
     */
    public static String insertSoftwareQuery() {
        return "{call sp_software_add(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Update the detail for a specific hardware.
     *
     * @return ..
     */
    public static String updateSoftwareQuery() {
        return "{call sp_software_update(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Delete a Software.
     */
    public static String deleteSoftwareQuery() {
        return "{call sp_software_delete(?,?)}";
    }

    /**
     * Add a Software License.
     *
     * @return ..
     */
    public static String insertSoftwareLicenseQuery() {
        return "{call sp_software_license_add(?,?,?,?,?,?)}";
    }

    /**
     * Update a Software License.
     *
     * @return ..
     */
    public static String updateSoftwareLicenseQuery() {
        return "{call sp_software_license_update(?,?,?,?,?,?)}";
    }

    /**
     * Delete a Software license.
     */
    public static String deleteSoftwareLicenseQuery() {
        return "{call sp_software_license_delete(?,?,?)}";
    }

    /**
     * Reset Software License counter.
     *
     * @return ..
     */
    public static String updateSoftwareLicenseCountQuery() {
        return "{call sp_software_count_license_update(?)}";
    }

    /**
     * Reset Software Bookmark counter.
     *
     * @return ..
     */
    public static String updateSoftwareBookmarkCountQuery() {
        return "{call sp_software_count_bookmark_update(?,?)}";
    }

    /**
     * Reset Software File counter.
     *
     * @return ..
     */
    public static String updateSoftwareFileCountQuery() {
        return "{call sp_software_count_file_update(?,?)}";
    }
}
