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
package com.kwoksys.biz.admin.core;

import com.kwoksys.action.admin.manageusers.UserSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.SqlUtils;
import com.kwoksys.framework.http.RequestContext;

/**
 * This is for building search queries.
 */
public class UserSearch extends BaseSearch {

    public static final String USERNAME = "username";

    public static final String USERNAME_EQUALS = "usernameEquals";

    public static final String USER_EMAIL = "email";

    public static final String FIRST_NAME = "firstName";

    public static final String LAST_NAME = "lastName";

    public static final String DISPLAY_NAME = "displayName";

    public static final String USER_STATUS = "userStatus";

    public static final String LOGGED_IN_USERS = "loggedInUsers";

    public static final String NON_DISABLED = "nonDisabledExceptSpecified";

    public void prepareMap(UserSearchForm userSearchForm, RequestContext requestContext) {
        String email = userSearchForm.getEmail();
        String username = userSearchForm.getUsername();
        String firstName = userSearchForm.getFirstName();
        String lastName = userSearchForm.getLastName();
        String displayName = userSearchForm.getDisplayName();
        int status = userSearchForm.getStatus();

        // Search by username
        if (!username.isEmpty()) {
            searchCriteriaMap.put(USERNAME, username);
        }
        // Search by user first name
        if (!firstName.isEmpty()) {
            searchCriteriaMap.put(FIRST_NAME, firstName);
        }
        // Search by user last name
        if (!lastName.isEmpty()) {
            searchCriteriaMap.put(LAST_NAME, lastName);
        }
        // Search by user display name
        if (!displayName.isEmpty()) {
            searchCriteriaMap.put(DISPLAY_NAME, displayName);
        }
        // Search by user email
        if (!email.isEmpty()) {
            searchCriteriaMap.put(USER_EMAIL, email);
        }
        // Search by user status
        if (status != 0) {
            searchCriteriaMap.put(USER_STATUS, status);
        }
    }

    /**
     * This would take searchCriteriaMap and compose the sql queries.
     */
    public void applyMap(QueryCriteria query) {
        if (searchCriteriaMap == null) {
            return;
        }

        // For getting non-disabled users, plus the specified userId.
        if (searchCriteriaMap.containsKey(NON_DISABLED)) {
            query.appendWhereClause("(status = "+ AttributeFieldIds.USER_STATUS_ENABLED + " or user_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get("nonDisabledExceptSpecified")) + ")");
        }
        // For searching by User status
        if (searchCriteriaMap.containsKey(USER_STATUS)) {
            query.appendWhereClause("status = " + SqlUtils.encodeInteger(searchCriteriaMap.get(USER_STATUS)));
        }
        // For searching by username
        if (searchCriteriaMap.containsKey(USERNAME_EQUALS)) {
            query.appendWhereClause("lower(u.username) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(USERNAME_EQUALS)) + "')");
        }
        // For searching by username
        if (searchCriteriaMap.containsKey(USERNAME)) {
            query.appendWhereClause("lower(u.username) like lower('" + SqlUtils.encodeString(searchCriteriaMap.get(USERNAME)) + "%')");
        }
        // For searching by user first name
        if (searchCriteriaMap.containsKey(FIRST_NAME)) {
            query.appendWhereClause("lower(u.first_name) like lower('" + SqlUtils.encodeString(searchCriteriaMap.get(FIRST_NAME)) + "%')");
        }
        // For searching by user last name
        if (searchCriteriaMap.containsKey(LAST_NAME)) {
            query.appendWhereClause("lower(u.last_name) like lower('" + SqlUtils.encodeString(searchCriteriaMap.get(LAST_NAME)) + "%')");
        }
        // For searching by user display name
        if (searchCriteriaMap.containsKey(DISPLAY_NAME)) {
            query.appendWhereClause("lower(u.display_name) like lower('" + SqlUtils.encodeString(searchCriteriaMap.get(DISPLAY_NAME)) + "%')");
        }
        // For searching by User email
        if (searchCriteriaMap.containsKey(USER_EMAIL)) {
            query.appendWhereClause("lower(u.email) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(USER_EMAIL)) + "')");
        }
        // For getting users except default-administrator
        if (searchCriteriaMap.containsKey("excludedDefaultAdmin")) {
            query.appendWhereClause("u.user_id != 1");
        }
        if (searchCriteriaMap.containsKey(LOGGED_IN_USERS)) {
            query.appendWhereClause("u.user_id in (select user_id from access_user where session_key is not null and extract(epoch from (now() - last_visit))<" + ConfigManager.auth.getSessionTimeoutSeconds() + " )");
        }
        // For getting users who're on Issue subscription list.
        if (searchCriteriaMap.containsKey("issueSelectedSubscribers")) {
            query.appendWhereClause("user_id in (select user_id from issue_subscription where issue_id= " + SqlUtils.encodeInteger(searchCriteriaMap.get("issueSelectedSubscribers")) + ")");
        }
        // For getting users who're not on Issue subscription list.
        if (searchCriteriaMap.containsKey("issueAvailableSubscribers")) {
            query.appendWhereClause("user_id not in (select user_id from issue_subscription where issue_id= " + SqlUtils.encodeInteger(searchCriteriaMap.get("issueAvailableSubscribers")) + ")");
        }
        // Get users in a particular user group
        if (searchCriteriaMap.containsKey("inGroupId")) {
            query.appendWhereClause("u.user_id in (select user_id from access_group_user_map where group_id= " + SqlUtils.encodeInteger(searchCriteriaMap.get("inGroupId")) + ")");
        }
        // Get users not in any group
        if (searchCriteriaMap.containsKey("notInGroup")) {
            query.appendWhereClause("u.user_id not in (select user_id from access_group_user_map)");
        }
    }
}
