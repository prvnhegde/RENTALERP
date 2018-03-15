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
package com.kwoksys.biz.issues.dao;

import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.biz.system.core.configs.ConfigManager;

/**
 * This is for storing all Issue sql queries.
 * Format: select_, insert_, update_, delete_, _Query
 */
public class IssueQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals(Issue.TITLE)) {
            return "lower(issue_name)";

        } else if (column.equals(Issue.ASSIGNEE_NAME)) {
            return ConfigManager.system.getUsernameDisplay().equals(AdminUtils.USER_USERNAME) ?
                    "lower(i.assignee_username)" : "lower(i.assignee_display_name)";

        } else if (column.equals(Issue.CREATOR_NAME)) {
            return ConfigManager.system.getUsernameDisplay().equals(AdminUtils.USER_USERNAME) ?
                    "lower(i.creator_username)" : "lower(i.creator_display_name)";

        } else if (column.equals("attribute_field_name")) {
            return "lower(af.attribute_field_name)";
            
        } else {
            return column;
        }
    }

    /**
     * Return all Issues that match the search criteria.
     *
     * @return ..
     */
    public static String selectIssueListQuery(QueryCriteria query) {
        return "select i.issue_id, i.issue_name, i.issue_description, i.assignee_id, i.assignee_username, i.assignee_display_name, i.issue_url, " +
                "i.issue_type, v.attribute_field_name as issue_status_name, v2.attribute_field_name as issue_priority_name, " +
                "it.attribute_field_name as issue_type_name, " +
                "i.creator, i.creator_username, i.creator_display_name, i.creation_date, " +
                "i.modifier, i.modifier_username, i.modifier_display_name, i.modification_date, i.issue_due_date " +
                "from issue_view i " +
                "left outer join attribute_field_view v on v.attribute_field_id = i.issue_status " +
                "left outer join attribute_field_view v2 on v2.attribute_field_id = i.issue_priority " +
                "left outer join attribute_field_view it on it.attribute_field_id = i.issue_type "
                + query.createWhereClause();
    }

    /**
     * Return all Issues for a particular Hardware.
     */
    public static String selectLinkedIssuesQuery(QueryCriteria query) {
        return selectIssueListQuery(new QueryCriteria()) +
                "join object_map om on i.issue_id=om.linked_object_id and om.object_id=? and om.object_type_id=? " +
                "and om.linked_object_type_id=? "
                + query.createWhereClause();
    }

    public static String selectIssueIdsQuery(QueryCriteria query) {
        return "select i.issue_id from issue_view i" + query.createWhereClause();
    }

    /**
     * Return all Issues that match the search criteria.
     *
     * @return ..
     */
    public static String selectIssueCountQuery(QueryCriteria query) {
        return "select count(i.issue_id) as row_count from issue_view i" + query.createWhereCountClause();
    }

    public static String selectIssueDetailQuery() {
        return "select i.issue_id, i.issue_name, i.issue_description, i.assignee_id, i.assignee_username, i.assignee_display_name, i.issue_url, " +
                "i.issue_type, i.issue_status, i.issue_priority, i.issue_resolution, i.duplicate_id, i.creator_ip, " +
                "i.issue_created_from_email, i.issue_due_date, " +
                "i.creator, i.creator_username, i.creator_display_name, i.creation_date, " +
                "i.modifier, i.modifier_username, i.modifier_display_name, i.modification_date " +
                "from issue_view i " +
                "where i.issue_id =? ";
    }

    public static String selectIssueHistoryQuery(QueryCriteria query) {
        return "select * from (" +
                "select ic.issue_change_id, ic.issue_change_field, ic.issue_change_varchar_old, ic.issue_change_varchar_new, oldv.attribute_field_name as issue_change_int_old, newv.attribute_field_name as issue_change_int_new, ic.issue_id, ic.creation_date, " +
                "ic.issue_created_from_email, cu.username as creator_username, cu.display_name as creator_display_name, ic.creator as creator, " +
                "c.issue_comment_description, " +
                "f.file_id, f.file_name, f.file_friendly_name, f.file_byte_size " +
                "from access_user u, issue_change ic " +
                "left outer join issue_comment c on ic.issue_comment_id = c.issue_comment_id " +
                "left outer join (select * from file_view where object_type_id = ? and object_id = ?) f on ic.file_id = f.file_id " +
                "left outer join attribute_field_view oldv on ic.issue_change_int_old = oldv.attribute_field_id " +
                "left outer join attribute_field_view newv on ic.issue_change_int_new = newv.attribute_field_id " +
                "left outer join access_user cu on ic.creator = cu.user_id " +
                "where ic.issue_id=? " +
                "and ic.issue_change_field in ('subject', 'comment', 'status', 'type', 'priority', 'resolution', 'file') " +
                "union " +
                "select ic.issue_change_id, ic.issue_change_field, oldv.display_name as issue_change_varchar_old, newv.display_name as issue_change_varchar_new, cast(ic.issue_change_int_old as varchar), cast(ic.issue_change_int_new as varchar), ic.issue_id, ic.creation_date, " +
                "ic.issue_created_from_email, cu.username as creator_username, cu.display_name as creator_display_name, ic.creator as creator, " +
                "'' as issue_comment_description, " +
                "null as file_id, '' as file_name, '' as file_friendly_name, 0 as file_byte_size " +
                "from issue_change ic " +
                "left outer join access_user oldv on ic.issue_change_int_old = oldv.user_id " +
                "left outer join access_user newv on ic.issue_change_int_new = newv.user_id " +
                "left outer join access_user cu on ic.creator = cu.user_id " +
                "where ic.issue_id=? " +
                "and ic.issue_change_field = 'assignee'" +
                ") temp " + query.createWhereClause();
    }

    public static String selectGroupByRecentCreatedIssues(QueryCriteria query, int numDays, double offsetHours) {
        return "select date(creation_date::timestamp + '" + offsetHours + " hour'::interval) created_date, count(issue_id) issue_count " +
                "from issue " +
                "where creation_date > (now()::timestamp + '-" + numDays + " day'::interval) " +
                "group by date(creation_date::timestamp + '" + offsetHours + " hour'::interval) " +
                 query.createAndClause();
    }

    /**
     * Returns Issues grouped by Issue status.
     */
    public static String selectIssueCountGoupByStatusQuery(QueryCriteria query) {
        return "select af.attribute_field_id, coalesce(i.issue_count, 0) as status_count " +
                "from attribute_view an, attribute_field_view af " +
                "left outer join (select issue_status, count(issue_id) as issue_count from issue group by issue_status) i on af.attribute_field_id = i.issue_status " +
                "where an.object_key='issue' and an.attribute_name='issue_status' " +
                "and an.attribute_id = af.attribute_id"
                + query.createAndClause();
    }

    /**
     * Returns open issues grouped by priority.
     */
    public static String selectIssueCountByPriorityQuery(QueryCriteria query) {
        return "select af.attribute_field_id, coalesce(i.issue_count, 0) as priority_count " +
                "from attribute_view an, attribute_field_view af " +
                "left outer join (select issue_priority, count(issue_id) as issue_count from issue where issue_status not in " +
                "(select attribute_field_id from attribute_field where field_key='closed') group by issue_priority) i " +
                "on af.attribute_field_id = i.issue_priority " +
                "where an.object_key='issue' and an.attribute_name='issue_priority' " +
                "and an.attribute_id = af.attribute_id"
                + query.createAndClause();
    }

    /**
     * Returns open issues grouped by type.
     */
    public static String selectIssueCountByTypeQuery(QueryCriteria query) {
        return "select af.attribute_field_id, coalesce(i.issue_count, 0) as type_count " +
                "from attribute_view an, attribute_field_view af " +
                "left outer join (select issue_type, count(issue_id) as issue_count from issue where issue_status not in " +
                "(select attribute_field_id from attribute_field where field_key='closed') group by issue_type) i " +
                "on af.attribute_field_id = i.issue_type " +
                "where an.object_key='issue' and an.attribute_name='issue_type' " +
                "and an.attribute_id = af.attribute_id"
                + query.createAndClause();
    }

    /**
     * Returns Open Issues grouped by Issue Assignee.
     */
    public static String selectIssueCountGoupByAssigneeQuery(QueryCriteria query) {
        return "select i.assignee_username, i.assignee_display_name, i.assignee_id, count(i.issue_id) as issue_count " +
                "from issue_view i where i.issue_status not in (select attribute_field_id from attribute_field where field_key='closed') " +
                "group by i.assignee_username, i.assignee_display_name, i.assignee_id"
                + query.createWhereClause();
    }

    /**
     * Update an Issue, which would also update Issue history.
     *
     * @return ..
     */
    public static String updateIssueQuery() {
        return "{call sp_issue_update(?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Add a new Issue.
     *
     * @return ..
     */
    public static String insertIssueQuery() {
        return "{call sp_issue_add(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Delete an Issue.
     *
     * @return ..
     */
    public static String deleteIssueQuery() {
        return "{call sp_issue_delete(?,?)}";
    }

    /**
     * Add an Issue File, which would also update Issue history.
     *
     * @return ..
     */
    public static String insertIssueFileQuery() {
        return "{call sp_issue_file_add(?,?,?)}";
    }

    /**
     * Add issue subscribers.
     *
     * @return ..
     */
    public static String addSubscriberQuery() {
        return "{call sp_issue_subscriber_add(?,?,?)}";
    }

    public static String deleteSubscriberQuery() {
        return "{call sp_issue_subscriber_delete(?,?)}";
    }
}
