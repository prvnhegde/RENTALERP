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
package com.kwoksys.biz.auth.dao;

/**
 * Sql queries for Auth service.
 * Format: select_, insert_, update_, delete_, _Query
 */
public class AuthQueries {

    public static String selectPermPages() {
        return "select perm_id, page_id from access_perm_page_map";
    }

    public static String selectGroupPerms() {
        return "select group_id, perm_id from access_group_perm_map where group_id = ?";
    }

    public static String selectUserPerms() {
        return "select user_id, perm_id from access_user_perm_map where user_id = ?";
    }

    public static String selectAccessPages() {
        return "select page_name, page_id, module_id from access_page";
    }

    /**
     * Check valid username, password is checked against ldap.
     *
     * @return ..
     */
    public static String selectValidUsernameQuery() {
        return "select user_id, status, password, invalid_logon_count, invalid_logon_date " +
                "from access_user " +
                "where lower(username) = lower(?) ";
    }

    /**
     * The purpose of validating is to see if the user_id and session_id
     * match our records. If the combination matches, we update the
     * last_visit field.
     *
     * @return ..
     */
    public static String validateUserSessionQuery() {
        return "{call sp_user_session_validate(?,?,?,?)}";
    }

    /**
     * Update User session when the user successfully logged on to the system.
     *
     * @return ..
     */
    public static String updateUserLogonSessionQuery() {
        return "{call sp_user_logon_valid_update(?,?)}";
    }

    /**
     * Updates user invalid logon.
     * @return
     */
    public static String updateUserInvalidLogonQuery() {
        return "{call sp_user_logon_invalid_update(?,?)}";
    }

    /**
     * Update User session when the user successfully logged out of the system.
     *
     * @return ..
     */
    public static String updateUserLogoutSessionQuery() {
        return "{call sp_user_logout(?)}";
    }
}
