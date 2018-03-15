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
package com.kwoksys.biz.admin.dao;

import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AccessUser;

/**
 * AdminQueries
 */
public class AdminQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals("username")) {
            return "lower(username)";

        } else if (column.equals(AccessUser.FIRST_NAME)) {
            return "lower(first_name)";

        } else if (column.equals(AccessUser.LAST_NAME)) {
            return "lower(last_name)";

        } else if (column.equals(AccessUser.DISPLAY_NAME)) {
            return "lower(display_name)";

        } else if (column.equals(AccessUser.EMAIL)) {
            return "lower(email)";

        } else if (column.equals("attribute_field_name")) {
            return "lower(af.attribute_field_name)";

        } else if (column.equals(Attribute.ATTR_NAME)) {
            return "lower(a.attribute_name)";
            
        } else {
            return column;
        }
    }

    /**
     * Query for showing databases.
     * Use column name "Database" to retrieve data.
     */
    public static String selectDatabases() {
        return "select datname as database_name from pg_database order by lower(datname)";
    }

    public static String selectGroupListQuery(QueryCriteria query) {
        return "select group_id, group_name, group_description from access_group " + query.createWhereClause();
    }

    public static String selectGroupDetailQuery() {
        return "select g.group_id, g.group_name, g.group_description, " +
                "g.creator, g.creation_date, g.creator_username, g.creator_display_name," +
                "g.modifier, g.modification_date, g.modifier_username, g.modifier_display_name " +
                "from group_view g " +
                "where g.group_id =?";
    }

    /**
     * This is for getting group access.
     *
     * @return ..
     */
    public static String selectGroupAccessQuery(QueryCriteria query) {
        return "select p.perm_id, p.perm_name, pm.perm_id as has_permission " +
                "from access_permission p " +
                "left outer join access_group_perm_map pm on pm.group_id = ? and p.perm_id = pm.perm_id " +
                "where p.perm_is_enabled !=0 " + query.createAndClause();
    }

    /**
     * This is for getting user_id given a username.
     *
     * @return ..
     */
    public static String selectUserIdByNameQuery() {
        return "select user_id from access_user where lower(username) = lower(?) ";
    }

    public static String selectUserIdByEmailQuery() {
        return "select user_id from access_user where lower(email) = lower(?) ";
    }

    /**
     * Return a list of Users
     *
     * @return ..
     */
    public static String selectUserListQuery(QueryCriteria query) {
        return "select u.user_id, u.username, u.first_name, u.last_name, u.display_name, u.email, u.status " +
                "from access_user u" + query.createWhereClause();
    }

    /**
     * Return a list of Users with expanded detail.
     *
     * @return ..
     */
    public static String selectUserExportListQuery(QueryCriteria query) {
        return "select u.user_id, u.username, u.first_name, u.last_name, u.display_name, u.email, u.status, " +
                "u.creator, u.creator_username, u.creator_display_name, u.creation_date, " +
                "u.modifier, u.modifier_username, modifier_display_name, u.modification_date, " +
                "u.contact_id, u.last_logon, u.last_visit " +
                "from user_view u " + query.createWhereClause();
    }

    /**
     * Return a count of Users.
     *
     * @return ..
     */
    public static String selectUserCountQuery(QueryCriteria query) {
        return "select count(u.user_id) as row_count " +
                " from access_user u" + query.createWhereCountClause();
    }

    /**
     * This is for getting User detail.
     *
     * @return ..
     */
    public static String selectUserDetailQuery() {
        return "select u.user_id, u.username, u.password, u.first_name, u.last_name, u.display_name, " +
                "u.email, u.status, u.hardware_count, u.is_default_user, u.invalid_logon_count, u.invalid_logon_date, " +
                "u.creator, u.creator_username, u.creator_display_name, u.creation_date, " +
                "u.modifier, u.modifier_username, modifier_display_name, u.modification_date, " +
                "u.contact_id, u.session_key, u.last_logon, u.last_visit, agm.group_id, g.group_name " +
                "from user_view u " +
                "left join access_group_user_map agm on agm.user_id=u.user_id " +
                "left join access_group g on agm.group_id = g.group_id " +
                "where u.user_id =?";
    }

    /**
     * This is for getting User access.
     *
     * @return ..
     */
    public static String selectUserAccessQuery(QueryCriteria query) {
        return "select p.perm_id, p.perm_name, pm.perm_id as has_permission " +
                "from access_permission p " +
                "left outer join access_user_perm_map pm on pm.user_id = ? and p.perm_id = pm.perm_id " +
                "where p.perm_is_enabled !=0 "
                + query.createAndClause();
    }

    /**
     * Gets a list of attribute groups
     * @param query
     * @return
     */
    public static String selectAttributeGroupsQuery() {
        return "select attribute_group_id, attribute_group_name from attribute_group " +
                "where object_type_id = ? order by lower(attribute_group_name)";
    }

    public static String selectAttributeGroupQuery() {
        return "select attribute_group_id, attribute_group_name from attribute_group " +
                "where attribute_group_id = ? and object_type_id = ? ";
    }

    public static String selectAttributeListQuery(QueryCriteria query) {
        return "select a.attribute_id, a.attribute_name, a.object_type_id, a.object_key, a.default_attribute_field_id, " +
                "a.attribute_group_id, a.attribute_type, a.attribute_option, a.attribute_convert_url, a.attribute_url, " +
                "a.is_required, a.input_mask, a.description, a.type_currency_symbol " +
                "from attribute_view a " + query.createWhereClause();
    }

    /**
     * Get detail of an Attribute.
     *
     * @return ..
     */
    public static String selectAttributeDetailQuery() {
        return "select a.attribute_id, a.attribute_name, a.object_type_id, a.object_key, a.default_attribute_field_id, " +
                "a.attribute_group_id, a.attribute_type, a.attribute_option, a.is_editable, a.is_required, " +
                "a.is_custom_attr, a.attribute_convert_url, a.attribute_url, a.input_mask, a.description, a.type_currency_symbol " +
                "from attribute_view a " +
                "where a.attribute_id = ?";
    }

    /**
     * Get a list of attribute fields
     * @param query
     * @return
     */
    public static String selectAttributeFieldListQuery(QueryCriteria query) {
        return "select a.attribute_id, af.attribute_field_id, af.attribute_field_name, af.attribute_field_description, " +
                "af.is_disabled, i.icon_id, i.icon_path, i.is_system_icon " +
                "from attribute_view a, attribute_field_view af left outer join icon i on i.icon_id = af.icon_id " +
                "where a.attribute_id = af.attribute_id " + query.createAndClause();
    }

    public static String selectAttributeFieldQuery(QueryCriteria query) {
        return "select a.attribute_id, af.attribute_field_id, af.attribute_field_name, af.attribute_field_description, " +
                "af.is_disabled, i.icon_id, i.icon_path, i.is_system_icon " +
                "from attribute_view a, attribute_field_view af left outer join icon i on i.icon_id = af.icon_id " +
                "where a.attribute_id = af.attribute_id " +
                "and af.attribute_field_id = ? " + query.createAndClause();
    }

    /**
     * Get custom attribute name and value
     * @param query
     * @return
     */
    public static String selectAttributeValuesQuery(QueryCriteria query) {
        return "select a.attribute_id, a.attribute_type, a.attr_value, a.attribute_field_id " +
                "from attribute_value_view a "
                + query.createWhereClause();
    }

    /**
     * Returns the mappings of an attribute type and custom attributes
     * @param query
     * @return
     */
    public static String selectAttributeFieldTypesQuery(QueryCriteria query) {
        return "select attribute_id, attribute_field_id, linked_attribute_id " +
                "from attribute_field_map " +
                "where attribute_field_id = ? "
                + query.createAndClause();
    }

    public static String selectAttributeFieldTypesByLinkedAttrQuery(QueryCriteria query) {
        return "select attribute_id, attribute_field_id, linked_attribute_id " +
                "from attribute_field_map " +
                "where linked_attribute_id = ? "
                + query.createAndClause();
    }

    /**
     * Gets a list of attribute icons
     */
     public static String selectAttributeIconsQuery() {
        return "select icon_id, icon_path, is_system_icon, attribute_id " +
                "from icon " +
                "where attribute_id=? order by icon_path";
    }

    /**
     * Insert a new group.
     *
     * @return ..
     */
    public static String addGroupQuery() {
        return "{call sp_group_add(?,?,?,?)}";
    }

    public static String updateGroupQuery() {
        return "{call sp_group_update(?,?,?,?)}";
    }

    public static String deleteGroupQuery() {
        return "{call sp_group_delete(?)}";
    }

    public static String addGroupMembersQuery() {
        return "{call sp_group_members_add(?,?,?)}";
    }

    public static String removeGroupMembersQuery() {
        return "{call sp_group_members_remove(?,?)}";
    }

    /**
     * Update group access.
     *
     * @return ..
     */
    public static String updateGroupAccessQuery() {
        return "{call sp_group_perm_map_update(?,?,?)}";
    }

    /**
     * Insert a new user.
     *
     * @return ..
     */
    public static String insertUserQuery() {
        return "{call sp_user_add(?,?,?,?,?,?,?,?,?)}";
    }

    public static String updateUserQuery() {
        return "{call sp_user_update(?,?,?,?,?,?,?,?)}";
    }

    public static String updateUserContact() {
        return "{call sp_user_contact_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Update user access.
     *
     * @return ..
     */
    public static String updateUserAccessQuery() {
        return "{call sp_user_perm_map_update(?,?,?)}";
    }

    /**
     * Deletes a user
     * @return
     */
    public static String deleteUserQuery() {
        return "{call sp_user_delete(?,?,?)}";
    }

    /**
     * Update a user's password.
     */
    public static String updateUserPasswordQuery() {
        return "{call sp_user_password_update(?, ?)}";
    }

    /**
     * Adds attribute group
     */
    public static String addAttributeGroupQuery() {
        return "{call sp_attribute_group_add(?,?,?)}";
    }

    /**
     * Updates attribute group
     */
    public static String updateAttributeGroupQuery() {
        return "{call sp_attribute_group_update(?,?,?)}";
    }

    /**
     * Deletes attribute group
     */
    public static String deleteAttributeGroupQuery() {
        return "{call sp_attribute_group_delete(?,?)}";
    }

    /**
     * Insert Custom Attribute.
     *
     * @return ..
     */
    public static String getAttributeAddQuery() {
        return "{call sp_attribute_add(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Update Custom Attribute.
     *
     * @return ..
     */
    public static String getAttributeUpdateQuery() {
        return "{call sp_attribute_update(?,?,?,?,?,?,?,?,?,?,?)}";
    }

    public static String updateSystemAttributeQuery() {
        return "{call sp_system_attribute_update(?,?,?)}";
    }

    /**
     * Delete Custom Attribute.
     *
     * @return ..
     */
    public static String deleteAttributeQuery() {
        return "{call sp_attribute_delete(?)}";
    }

    /**
     * Insert Attribute Field local content.
     *
     * @return ..
     */
    public static String insertAttributeFieldQuery() {
        return "{call sp_attribute_field_add(?,?,?,?,?,?)}";
    }

    /**
     * Update Attribute Field.
     *
     * @return ..
     */
    public static String updateAttributeFieldQuery() {
        return "{call sp_attribute_field_update(?,?,?,?,?,?)}";
    }

    /**
     * Update Attribute Value.
     *
     * @return ..
     */
    public static String updateAttributeValueQuery() {
        return "{call sp_attribute_value_update(?,?,?)}";
    }

    public static String addAttributeValueQuery() {
        return "{call sp_attribute_value_add(?,?,?,?)}";
    }

    public static String deleteAttributeFieldMapQuery() {
        return "{call sp_attribute_field_map_delete(?,?,?)}";
    }

    public static String updateAttributeFieldMapQuery() {
        return "{call sp_attribute_field_map_update(?,?,?)}";
    }

    /**
     * Update application configuration.
     *
     * @return ..
     */
    public static String updateApplicationConfigQuery() {
        return "{call sp_system_config_update(?,?)}";
    }
}
