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
package com.kwoksys.action.issues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.auth.core.Permissions;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueSearch;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dao.IssueQueries;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.FeatureManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * Action class for dao module index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();
        HttpSession session = request.getSession();

        if (requestContext.getParameterString("cmd").equals("clear")) {
            session.setAttribute(SessionManager.ISSUE_SEARCH_CRITERIA_MAP, null);
            clearSessionBaseForm(IssueSearchForm.class);
        }

        getSessionBaseForm(IssueSearchForm.class);

        // For advanced access control
        boolean canViewAllIssues = accessUser.hasPermission(Permissions.ISSUE_READ_PERMISSION);

        List<Link> links = new ArrayList<>();

        // The search criteria map is not null, that means the user has performed a search.
        if (session.getAttribute(SessionManager.ISSUE_SEARCH_CRITERIA_MAP) != null) {
            links.add(new Link(requestContext).setTitleKey("common.search.showLastSearch")
                    .setAjaxPath(AppPaths.ISSUES_LIST));
        }

        links.add(new Link(requestContext).setTitleKey("issueMgmt.index.showNonClosed")
                .setAjaxPath(AppPaths.ISSUES_LIST + "?cmd=showNonClosed"));

        links.add(new Link(requestContext).setTitleKey("issueMgmt.index.showOpenUnassigned")
                .setAjaxPath(AppPaths.ISSUES_LIST + "?cmd=showOpenUnassigned"));

        links.add(new Link(requestContext).setTitleKey("issueMgmt.index.showMyIssues")
                .setAjaxPath(AppPaths.ISSUES_LIST + "?cmd=showMyIssues"));

        links.add(new Link(requestContext).setTitleKey("issueMgmt.index.showMyReportedIssues")
                .setAjaxPath(AppPaths.ISSUES_LIST + "?cmd=showMyReportedIssues"));

        links.add(new Link(requestContext).setTitleKey("issueMgmt.index.showAll")
                .setAjaxPath(AppPaths.ISSUES_LIST + "?cmd=showAll"));

        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        // Get the number of all issues
        int numIssueRecords = issueService.getCount(new QueryCriteria());

        IssueSearch issueSearch = new IssueSearch();

        if (!canViewAllIssues) {
            issueSearch.put(IssueSearch.ISSUE_PERMITTED_USER_ID, accessUser.getId());
        }

        // Get another count on the number of issues the user is allowed to see
        int numIssuesHasPerm = canViewAllIssues ? numIssueRecords : issueService.getCount(new QueryCriteria(issueSearch));

        List<Map> statusCounts = new ArrayList<>();
        List<Map> priorityCounts = new ArrayList<>();
        List<Map> typeCounts = new ArrayList<>();
        List<Map> assigneeCounts = new ArrayList<>();

        AttributeManager attributeManager = new AttributeManager(requestContext);

        if (canViewAllIssues) {
            // This is for the Issue count grouped by status section.
            Map<Integer, AttributeField> attrFieldStatusMap = attributeManager.getAttrFieldMapCache(Attributes.ISSUE_STATUS);
            Map<Integer, AttributeField> attrFieldPriorityMap = attributeManager.getAttrFieldMapCache(Attributes.ISSUE_PRIORITY);
            Map<Integer, AttributeField> attrFieldTypeMap = attributeManager.getAttrFieldMapCache(Attributes.ISSUE_TYPE);

            // We'll use the same queryCriteria for a few different queries below
            QueryCriteria countsQuery = new QueryCriteria();
            countsQuery.addSortColumn(IssueQueries.getOrderByColumn(AttributeField.NAME));

            for (AttributeFieldCount status : issueService.getGoupByStatusCount(countsQuery)) {
                AttributeField attrField = (AttributeField) attrFieldStatusMap.get(status.getAttrFieldId());

                // Make this a link.
                int count = status.getObjectCount();
                // If the count is 0 and attribute is disabled, it's not useful to show the attribute
                if (count == 0 && attrField.isDisabled()) {
                    continue;
                }

                Link countKey = new Link(requestContext).setTitle(attrField.getName());
                Link countValue = new Link(requestContext).setTitle(String.valueOf(count));

                if (count != 0) {
                    String path = AppPaths.ISSUES_LIST + "?cmd=search&status=" + attrField.getId();
                    countKey.setAjaxPath(path);
                    countValue.setAjaxPath(path);
                }

                Map<String, String> map = new HashMap<>();
                map.put("countKey", countKey.getString());
                map.put("countValue", countValue.getString());
                statusCounts.add(map);
            }

            // This is for the issue count by priority section.
            for (AttributeFieldCount priority : issueService.getGoupByPriorityCount(countsQuery)) {
                AttributeField attrField = (AttributeField) attrFieldPriorityMap.get(priority.getAttrFieldId());

                // Make this a link.
                int count = priority.getObjectCount();
                // If the count is 0 and attribute is disabled, it's not useful to show the attribute
                if (count == 0 && attrField.isDisabled()) {
                    continue;
                }

                Link countKey = new Link(requestContext).setTitle(attrField.getName());
                Link countValue = new Link(requestContext).setTitle(String.valueOf(count));

                if (count != 0) {
                    String path = AppPaths.ISSUES_LIST + "?cmd=search&statusNotEquals=closed&priority=" + priority.getAttrFieldId();
                    countKey.setAjaxPath(path);
                    countValue.setAjaxPath(path);
                }

                Map<String, String> map = new HashMap<>();
                map.put("countKey", countKey.getString());
                map.put("countValue", countValue.getString());
                priorityCounts.add(map);
            }

            // This is for the issue count by type section.
            for (AttributeFieldCount type : issueService.getGoupByTypeCount(countsQuery)) {
                AttributeField attrField = (AttributeField) attrFieldTypeMap.get(type.getAttrFieldId());

                // Make this a link.
                int count = type.getObjectCount();
                // If the count is 0 and attribute is disabled, it's not useful to show the attribute
                if (count == 0 && attrField.isDisabled()) {
                    continue;
                }

                Link countKey = new Link(requestContext).setTitle(attrField.getName());
                Link countValue = new Link(requestContext).setTitle(String.valueOf(count));

                if (count != 0) {
                    String path = AppPaths.ISSUES_LIST + "?cmd=search&statusNotEquals=closed&type=" + type.getAttrFieldId();
                    countKey.setAjaxPath(path);
                    countValue.setAjaxPath(path);
                }

                Map<String, String> map = new HashMap<>();
                map.put("countKey", countKey.getString());
                map.put("countValue", countValue.getString());
                typeCounts.add(map);
            }

            // This is for the Issue count grouped by assignee section.
            QueryCriteria assigneeCountQuery = new QueryCriteria();
            assigneeCountQuery.addSortColumn(IssueQueries.getOrderByColumn(Issue.ASSIGNEE_NAME));

            for (Map<String, String> assignee : issueService.getGoupByAssigneeCount(assigneeCountQuery)) {
                // Make this a link.
                int count = NumberUtils.replaceNull(assignee.get("issue_count"));

                // assigneeId: Null for unassigned, 0 for user removed.
                AccessUser assigneeUser = new AccessUser();
                assigneeUser.setId(NumberUtils.replaceNull(assignee.get("assignee_id")));
                assigneeUser.setUsername(StringUtils.replaceNull(assignee.get("assignee_username")));
                assigneeUser.setDisplayName(StringUtils.replaceNull(assignee.get("assignee_display_name")));

                String path = assigneeUser.getId() == 0 ? AppPaths.ISSUES_LIST + "?cmd=showOpenUnassigned"
                            : AppPaths.ISSUES_LIST + "?cmd=search&statusNotEquals=closed&assignedTo=" + assigneeUser.getId();

                Link countKey = new Link(requestContext).setTitle(IssueUtils.formatAssigneeName(requestContext, assigneeUser)).setAjaxPath(path);
                Link countValue = new Link(requestContext).setTitle(String.valueOf(count)).setAjaxPath(path);

                Map<String, String> map = new HashMap<>();
                map.put("countKey", countKey.getString());
                map.put("countValue", countValue.getString());
                assigneeCounts.add(map);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("hasIssueReadPermission", canViewAllIssues);
        standardTemplate.setAttribute("numIssuesHasPerm", numIssuesHasPerm);
        standardTemplate.setAttribute("links", links);
        standardTemplate.setAttribute("statusCountList", statusCounts);
        standardTemplate.setAttribute("priorityCountList", priorityCounts);
        standardTemplate.setAttribute("typeCountList", typeCounts);
        standardTemplate.setAttribute("assigneeCountList", assigneeCounts);
        standardTemplate.setAttribute("numIssueRecords", numIssueRecords);

        // Number of days to go back to show the number of issues created.
        int numDays = 14;
        Map<String, String> recentlyCreatedIssues = issueService.getGroupByRecentCreatedIssues(requestContext, new QueryCriteria(), numDays);

        List<String> createdIssueDates = new ArrayList<>();
        List<String> createdIssueCounts = new ArrayList<>();

        for (Map.Entry<String, String> entry : recentlyCreatedIssues.entrySet()) {
            createdIssueDates.add("'" + entry.getKey() + "'");
            createdIssueCounts.add(entry.getValue());
        }

        standardTemplate.setAttribute("chartLabels", StringUtils.join(createdIssueDates, ","));
        standardTemplate.setAttribute("chartData", StringUtils.join(createdIssueCounts, ","));
        standardTemplate.setAttribute("numDays", numDays);

        //
        // Template: IssueSearchTemplate
        //
        IssueSearchTemplate searchTemplate = standardTemplate.addTemplate(new IssueSearchTemplate());
        searchTemplate.setFormAction(AppPaths.ISSUES_LIST);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.4");
        header.setTitleClassNoLine();
        
        // Add Issue.
        if (accessUser.hasPermission(AppPaths.ISSUES_ADD)) {
            header.addHeaderCmds(new Link(requestContext).setAjaxPath(AppPaths.ISSUES_ADD)
                    .setTitleKey("issueMgmt.cmd.issueAdd"));
        }

        if (FeatureManager.isIssueGuestSubmitModuleEnabled(accessUser)) {
            header.addHeaderCmds(new Link(requestContext).setAjaxPath(AppPaths.ISSUE_PLUGIN_ADD)
                    .setTitleKey("issuePlugin.issueAdd.title"));
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
