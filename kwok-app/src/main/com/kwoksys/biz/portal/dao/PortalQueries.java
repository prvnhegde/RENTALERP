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
package com.kwoksys.biz.portal.dao;

import com.kwoksys.framework.connections.database.QueryCriteria;

/**
 * Format: select_, insert_, update_, delete_, _Query
 */
public class PortalQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals("category_name")) {
            return "lower(category_name)";

        } else if (column.equals("site_name")) {
            return "lower(site_name)";

        } else if (column.equals("site_path")) {
            return "lower(site_path)";

        } else {
            return column;
        }
    }

    /**
     * Return a list of Portal Sites.
     *
     * @return ..
     */
    public static String selectSiteListQuery(QueryCriteria query) {
        return "select s.site_id, s.site_name, s.site_path, s.site_placement, s.site_support_iframe, category_id, " +
                "category_name " + 
                "from site_view s " + query.createWhereClause();
    }

    /**
     * Return the number of Portal Sites.
     *
     * @return ..
     */
    public static String selectSiteCountQuery(QueryCriteria query) {
        return "select count(s.site_id) as row_count from portal_site s " + query.createWhereCountClause();
    }

    /**
     * Returns Portal Site detail.
     *
     * @return ..
     */
    public static String selectSiteDetailQuery() {
        return "select site_id, site_name, site_path, site_description, site_placement, site_support_iframe," +
                "category_id, category_name, " +
                "creator, creation_date, creator_username, creator_display_name, " +
                "modifier, modification_date, modifier_username, modifier_display_name  " +
                "from site_view where site_id=?";
    }

    /**
     * Adds Portal Site.
     */
    public static String insertSiteQuery() {
        return "{call sp_site_add(?,?,?,?,?,?,?,?)}";
    }

    /**
     * Updates Portal Site.
     */
    public static String updateSiteQuery() {
        return "{call sp_site_update(?,?,?,?,?,?,?,?)}";
    }

    /**
     * Deletes Portal Site.
     */
    public static String deleteSiteQuery() {
        return "{call sp_site_delete(?)}";
    }
}
