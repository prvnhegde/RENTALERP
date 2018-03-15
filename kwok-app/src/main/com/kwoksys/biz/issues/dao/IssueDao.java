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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * IssueDao class.
 */
public class IssueDao extends BaseDao {

    private static final Logger LOGGER = Logger.getLogger(IssueDao.class.getName());

    public IssueDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Gets number of issues created in last x days.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public Map<String, String> getGroupByRecentCreatedIssues(QueryCriteria query, int numDays, double offsetHours) throws DatabaseException {
        Map<String, String> map = new HashMap<>();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectGroupByRecentCreatedIssues(query, numDays, offsetHours)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                // The query already factors in timezone diff, using DatetimeUtils.toShortDate() is fine here. 
                map.put(DatetimeUtils.toShortDate(DatetimeUtils.getDate(rs, "created_date")), rs.getString("issue_count"));
            }
        };
        
        executeQuery(queryHelper);

        return map;
    }

    /**
     * Get number of issues group by status.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getGroupByStatusCount(QueryCriteria query) throws DatabaseException {
        List<AttributeFieldCount> counts = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueCountGoupByStatusQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("attribute_field_id"));
                count.setObjectCount(rs.getInt("status_count"));

                counts.add(count);
            }
        };

        executeQuery(queryHelper);
        
        return counts;
    }

    /**
     * Get open issues by priority.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getGroupByPriorityCount(QueryCriteria queryCriteria) throws DatabaseException {
        List<AttributeFieldCount> counts = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueCountByPriorityQuery(queryCriteria)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("attribute_field_id"));
                count.setObjectCount(rs.getInt("priority_count"));

                counts.add(count);
            }
        };
        
        executeQuery(queryHelper);

        return counts;
    }

    /**
     * Get open issues by type.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getGroupByTypeCount(QueryCriteria query) throws DatabaseException {
        List<AttributeFieldCount> counts = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueCountByTypeQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("attribute_field_id"));
                count.setObjectCount(rs.getInt("type_count"));

                counts.add(count);
            }
        };
        
        executeQuery(queryHelper);

        return counts;
    }

    /**
     * Gets number of issues group by Assignee.
     *
     * @return ..
     */
    public List<Map<String, String>> getGroupByAssigneeCount(QueryCriteria query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueCountGoupByAssigneeQuery(query));

        return executeQueryReturnList(queryHelper);
    }

    /**
     * Get a list of Issues
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<Issue> getIssues(QueryCriteria queryCriteria, ObjectLink objectMap) throws DatabaseException {
        List<Issue> issues = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper() {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Issue issue = new Issue();
                issue.setId(rs.getInt("issue_id"));
                issue.setSubject(StringUtils.replaceNull(rs.getString("issue_name")));
                issue.setDescription(StringUtils.replaceNull(rs.getString("issue_description")));
                issue.setTypeName(StringUtils.replaceNull(rs.getString("issue_type_name")));
                issue.setStatusName(StringUtils.replaceNull(rs.getString("issue_status_name")));
                issue.setPriorityName(StringUtils.replaceNull(rs.getString("issue_priority_name")));
                issue.setUrl(StringUtils.replaceNull(rs.getString("issue_url")));
                issue.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                issue.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));
                issue.setDueDate(DatetimeUtils.getDate(rs, "issue_due_date"));

                issue.setAssignee(new AccessUser());
                issue.getAssignee().setId(rs.getInt("assignee_id"));
                issue.getAssignee().setUsername(rs.getString("assignee_username"));
                issue.getAssignee().setDisplayName(rs.getString("assignee_display_name"));

                issue.setCreator(new AccessUser());
                issue.getCreator().setId(rs.getInt("creator"));
                issue.getCreator().setUsername(rs.getString("creator_username"));
                issue.getCreator().setDisplayName(rs.getString("creator_display_name"));

                issue.setModifier(new AccessUser());
                issue.getModifier().setId(rs.getInt("modifier"));
                issue.getModifier().setUsername(rs.getString("modifier_username"));
                issue.getModifier().setDisplayName(rs.getString("modifier_display_name"));

                issues.add(issue);
            }
        };

        if (objectMap != null) {
            queryHelper.setSqlStatement(IssueQueries.selectLinkedIssuesQuery(queryCriteria));
            queryHelper.addInputInt(objectMap.getObjectId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());

        } else {
            queryHelper.setSqlStatement(IssueQueries.selectIssueListQuery(queryCriteria));
        }

        executeQuery(queryHelper);
        
        return issues;
    }

    /**
     * Get a list of linked Issues.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<Issue> getLinkedIssueList(QueryCriteria query, ObjectLink objectMap) throws DatabaseException {
        return getIssues(query, objectMap);
    }

    public int getCount(QueryCriteria query) throws DatabaseException {
        return getRowCount(IssueQueries.selectIssueCountQuery(query));
    }

    public Set<Integer> getIssueIds(QueryCriteria query) throws DatabaseException {
        Set<Integer> set = new HashSet<>();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueIdsQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                set.add(rs.getInt("issue_id"));
            }
        };
        
        executeSingleRecordQuery(queryHelper);

        return set;
    }

    /**
     * Returns details for a specific issue
     *
     * @param requestedIssueId
     * @return ..
     */
    public Issue getIssue(Integer issueId) throws DatabaseException, ObjectNotFoundException {
        List<Issue> issues = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueDetailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Issue issue = new Issue();
                issue.setId(rs.getInt("issue_id"));
                issue.setSubject(StringUtils.replaceNull(rs.getString("issue_name")));
                issue.setDescription(StringUtils.replaceNull(rs.getString("issue_description")));
                issue.setStatus(rs.getInt("issue_status"));
                issue.setType(rs.getInt("issue_type"));
                issue.setPriority(rs.getInt("issue_priority"));
                issue.setResolution(rs.getInt("issue_resolution"));
                issue.setUrl(StringUtils.replaceNull(rs.getString("issue_url")));
                issue.setDueDate(DatetimeUtils.getDate(rs, "issue_due_date"));
                issue.setHasDueDate(issue.getDueDate() != null);
                issue.setFromEmail(StringUtils.replaceNull(rs.getString("issue_created_from_email")));
                issue.setCreatorIP(StringUtils.replaceNull(rs.getString("creator_ip")));
                issue.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                issue.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                issue.setAssignee(new AccessUser());
                issue.getAssignee().setId(rs.getInt("assignee_id"));
                issue.getAssignee().setUsername(rs.getString("assignee_username"));
                issue.getAssignee().setDisplayName(rs.getString("assignee_display_name"));

                issue.setCreator(new AccessUser());
                issue.getCreator().setId(rs.getInt("creator"));
                issue.getCreator().setUsername(rs.getString("creator_username"));
                issue.getCreator().setDisplayName(rs.getString("creator_display_name"));

                issue.setModifier(new AccessUser());
                issue.getModifier().setId(rs.getInt("modifier"));
                issue.getModifier().setUsername(rs.getString("modifier_username"));
                issue.getModifier().setDisplayName(rs.getString("modifier_display_name"));
                
                issues.add(issue);
            }
        };
        
        queryHelper.addInputInt(issueId);

        executeSingleRecordQuery(queryHelper);

        if (!issues.isEmpty()) {
            return issues.get(0);
            
        } else {
            throw new ObjectNotFoundException("Issue ID: " + issueId);
        }
    }

    /**
     * Get a list of issue history for a given issueId.
     *
     * @param query
     * @param issueId
     * @return ..
     */
    public List<Map<String, String>> getHistory(QueryCriteria query, Integer issueId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.selectIssueHistoryQuery(query));
        queryHelper.addInputInt(ObjectTypes.ISSUE);
        queryHelper.addInputInt(issueId);
        queryHelper.addInputInt(issueId);
        queryHelper.addInputInt(issueId);

        return executeQueryReturnList(queryHelper);
    }

    public ActionMessages addIssueSimple(Issue issue) throws DatabaseException {
        LOGGER.log(Level.INFO, "Add new issue from Report Issue/Email");
        Connection conn = getConnection();
        
        QueryHelper queryHelper = new QueryHelper(IssueQueries.insertIssueQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(issue.getSubject());
        queryHelper.addInputStringConvertNull(issue.getDescription());
        queryHelper.addInputStringConvertNull(issue.getUrl());
        queryHelper.addInputIntegerConvertNull(issue.getType());
        queryHelper.addInputIntegerConvertNull(issue.getStatus());
        queryHelper.addInputIntegerConvertNull(issue.getPriority());
        queryHelper.addInputIntegerConvertNull(issue.getResolution());
        queryHelper.addInputIntegerConvertNull(issue.getAssignee().getId());
        queryHelper.addInputStringConvertNull(issue.isHasDueDate() ? DatetimeUtils.createDatetimeString(
                issue.getDueDateYear(), issue.getDueDateMonth(), issue.getDueDateDate()) : null);
        queryHelper.addInputStringConvertNull(issue.getCreatorIP());
        queryHelper.addInputStringConvertNull(issue.getFromEmail());
        queryHelper.addInputInt(requestContext.getUser().getId());
        queryHelper.addInputIntegerConvertNull(null);

        try {
            queryHelper.executeProcedure(conn);

            // Put some values in the result.
            issue.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Since this is an add, we only need to check if selected subscribers is null.
            if (!issue.getSelectedSubscribers().isEmpty()) {
                // Update issue subscriber list
                updateSubscribers(requestContext, conn, issue);
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }

        // Put some values in the result.
        if (errors.isEmpty()) {
            issue.setId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    public ActionMessages add(RequestContext requestContext, Issue issue) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.insertIssueQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(issue.getSubject());
        queryHelper.addInputStringConvertNull(issue.getDescription());
        queryHelper.addInputStringConvertNull(issue.getUrl());
        queryHelper.addInputIntegerConvertNull(issue.getType());
        queryHelper.addInputIntegerConvertNull(issue.getStatus());
        queryHelper.addInputIntegerConvertNull(issue.getPriority());
        queryHelper.addInputIntegerConvertNull(issue.getResolution());
        queryHelper.addInputIntegerConvertNull(issue.getAssignee().getId());
        queryHelper.addInputStringConvertNull(issue.isHasDueDate() ? DatetimeUtils.createDatetimeString(
                issue.getDueDateYear(), issue.getDueDateMonth(), issue.getDueDateDate()) : null);
        queryHelper.addInputStringConvertNull(issue.getCreatorIP());
        queryHelper.addInputStringConvertNull(issue.getFromEmail());

        Integer creator = requestContext.getUser().getId();
        Integer proxyCreator = null;

        if (issue.getProxyUserId() != null && issue.getProxyUserId() != 0) {
            creator = issue.getProxyUserId();
            proxyCreator = requestContext.getUser().getId();
        }

        queryHelper.addInputInt(creator);
        queryHelper.addInputIntegerConvertNull(proxyCreator);

        try {
            queryHelper.executeProcedure(conn);

            // Put some values in the result.
            issue.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update issue subscriber list
            addSubscribers(requestContext, conn, issue);

            // Update custom fields
            if (!issue.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, issue.getId(), issue.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * This is to edit a issue.
     * We also update issue log after editing a issue.
     *
     * @param issue
     * @return ..
     */
    public ActionMessages update(RequestContext requestContext, Issue issue, boolean updateSubscribers) throws DatabaseException {
        LOGGER.log(Level.INFO, "Update issue ID " + issue.getId() + " from Report Issue/Email");
        
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(IssueQueries.updateIssueQuery());
        queryHelper.addInputInt(issue.getId());
        queryHelper.addInputStringConvertNull(issue.getSubject());
        queryHelper.addInputStringConvertNull(issue.getFollowup());
        queryHelper.addInputIntegerConvertNull(issue.getType());
        queryHelper.addInputIntegerConvertNull(issue.getStatus());
        queryHelper.addInputIntegerConvertNull(issue.getPriority());
        queryHelper.addInputIntegerConvertNull(issue.getResolution());
        queryHelper.addInputIntegerConvertNull(issue.getAssignee().getId());
        queryHelper.addInputStringConvertNull(issue.isHasDueDate() ? DatetimeUtils.createDatetimeString(
                issue.getDueDateYear(), issue.getDueDateMonth(), issue.getDueDateDate()) : null);
        queryHelper.addInputStringConvertNull(issue.getFromEmail());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            if (updateSubscribers) {
                // Update issue subscriber list
                updateSubscribers(requestContext, conn, issue);
            }

            // Update custom fields
            if (issue.getCustomValues() != null && !issue.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, issue.getId(), issue.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Delete an Issue.
     *
     * @return ..
     */
    public ActionMessages delete(Integer issueId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.deleteIssueQuery());
        queryHelper.addInputInt(ObjectTypes.ISSUE);
        queryHelper.addInputInt(issueId);

        return executeProcedure(queryHelper);
    }

    public List<AccessUser> getSelectedSubscribers(Integer issueId) throws DatabaseException {
        // Get a list of selected subscribers.
        UserSearch userSearch = new UserSearch();
        userSearch.put("issueSelectedSubscribers", issueId);

        QueryCriteria query = new QueryCriteria(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        return adminService.getUsers(query);
    }

    /**
     * Updates subscribers
     * @param requestContext
     * @param conn
     * @param issue
     * @throws DatabaseException
     */
    private void updateSubscribers(RequestContext requestContext, Connection conn, Issue issue) throws DatabaseException {
        List<Integer> selectedSubscribers = new ArrayList<>();
        
        selectedSubscribers.addAll(issue.getSelectedSubscribers());
        
        // Loop through the current subscriber list
        // If the subscriber is not the in list, run a procedure to remove the member
        for (AccessUser user: getSelectedSubscribers(issue.getId())) {
            if (selectedSubscribers.contains(user.getId())) {
                selectedSubscribers.remove(user.getId());
            } else {
                deleteSubscriber(conn, issue, user.getId());
            }
        }

        // The remaining list has the users we want to add
        for (Integer userId: selectedSubscribers) {
            addSubscriber(requestContext, conn, issue, userId);
        }
    }

    /**
     * Adds subscribers.
     */
    private void addSubscribers(RequestContext requestContext, Connection conn, Issue issue) throws DatabaseException {
        for (Integer subscriberUserId : issue.getSelectedSubscribers()) {
            addSubscriber(requestContext, conn, issue, subscriberUserId);
        }
    }

    /**
     * Add issue subscriber.
     *
     * @param issue
     * @return ..
     */
    private void addSubscriber(RequestContext requestContext, Connection conn, Issue issue, Integer subscriberUserId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.addSubscriberQuery());
        queryHelper.addInputInt(issue.getId());
        queryHelper.addInputInt(subscriberUserId);
        queryHelper.addInputInt(requestContext.getUser().getId());

        queryHelper.executeProcedure(conn);
    }

    private void deleteSubscriber(Connection conn, Issue issue, Integer subscriberUserId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.deleteSubscriberQuery());
        queryHelper.addInputInt(issue.getId());
        queryHelper.addInputInt(subscriberUserId);

        queryHelper.executeProcedure(conn);
    }

    /**
     * This is to add an Issue file.
     *
     * @param issue
     * @return ..
     */
    public ActionMessages addIssueFile(Issue issue) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(IssueQueries.insertIssueFileQuery());
        queryHelper.addInputInt(issue.getId());
        queryHelper.addInputIntegerConvertNull(issue.getFileId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }
}
