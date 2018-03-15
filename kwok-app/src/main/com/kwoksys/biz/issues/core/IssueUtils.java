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
package com.kwoksys.biz.issues.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.IssueAccess;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.AppConfigManager;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.parsers.email.IssueEmailParser;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Issue utilities.
 */
public class IssueUtils {

    public static final String ISSUE_TAB_HISTORY = "history";

    public static final String ISSUE_TAB_RELATIONSHIP = "rel";

    public static String[] getIssuesDefaultColumns() {
        return new String[] {Issue.ROWNUM, Issue.ID, Issue.TITLE, Issue.TYPE, Issue.STATUS, Issue.PRIORITY,
                Issue.ASSIGNEE_NAME, Issue.CREATOR_NAME, Issue.CREATION_DATE, Issue.MODIFICATION_DATE, Issue.DUE_DATE};
    }

    /**
     * Returns column headers for list view.
     *
     * @return ..
     */
    public static List<String> getIssueColumnHeaders() {
        return ConfigManager.app.getIssuesColumns();
    }

    public static List<String> getSortableColumns() {
        return Arrays.asList(Issue.ID, Issue.ASSIGNEE_NAME, Issue.TITLE, Issue.STATUS, Issue.PRIORITY,
                Issue.CREATOR_NAME, Issue.CREATION_DATE, Issue.MODIFICATION_DATE, Issue.DUE_DATE);
    }

    /**
     * Returns whether a column is sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableColumn(String columnName) {
        return getSortableColumns().contains(columnName);
    }

    public static List<DataRow> formatIssues(RequestContext requestContext, List<Issue> issueList, IssueAccess access, Counter counter)
            throws DatabaseException {

        AccessUser user = requestContext.getUser();
        List<DataRow> list = new ArrayList<>();

        boolean hasUserDetailAccess = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);
        boolean hasIssueDetailAccess = Access.hasPermission(user, AppPaths.ISSUES_DETAIL);

        for (Issue issue : issueList) {
            List<String> columns = new ArrayList<>();

            for (String column : getIssueColumnHeaders()) {
                if (column.equals(Issue.ROWNUM)) {
                    columns.add(counter.incr() + ".");

                } else if (column.equals(Issue.ID)) {
                    columns.add(String.valueOf(issue.getId()));
                    
                } else if (column.equals(Issue.TITLE)) {
                    Link issueLink = new Link(requestContext);
                    issueLink.setTitle(issue.getSubject());

                    if (hasIssueDetailAccess && access.hasPermission(issue.getId())) {
                        issueLink.setAjaxPath(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());
                    }

                    columns.add(issueLink.getString());

                } else if (column.equals(Issue.ASSIGNEE_NAME)) {
                    String assigneeName = IssueUtils.getAssigneeIconLink(requestContext, hasUserDetailAccess, issue.getAssignee());
                    columns.add(assigneeName);

                } else if (column.equals(Issue.CREATOR_NAME)) {
                    columns.add(Links.getUserIconLink(requestContext, issue.getCreator(), hasUserDetailAccess, true).getString());
                    
                } else if (column.equals(Issue.CREATION_DATE)) {
                    columns.add(issue.getCreationDate());

                } else if (column.equals(Issue.MODIFIER_NAME)) {
                    columns.add(Links.getUserIconLink(requestContext, issue.getModifier(), hasUserDetailAccess, true).getString());

                } else if (column.equals(Issue.MODIFICATION_DATE)) {
                    columns.add(issue.getModificationDate());

                } else if (column.equals(Issue.DUE_DATE)) {
                    columns.add(issue.getDueDateShort());

                } else if (column.equals(Issue.STATUS)) {
                    columns.add(HtmlUtils.encode(issue.getStatusName()));

                } else if (column.equals(Issue.TYPE)) {
                    columns.add(HtmlUtils.encode(issue.getTypeName()));

                } else if (column.equals(Issue.PRIORITY)) {
                    columns.add(HtmlUtils.encode(issue.getPriorityName()));
                }
            }
            
            DataRow dataRow = new DataRow();
            dataRow.setRowId(String.valueOf(issue.getId()));
            dataRow.setColumns(columns);
            list.add(dataRow);
        }
        return list;
    }

    /**
     * @param request
     * @param issue
     * @return
     * @throws DatabaseException
     */
    public static List<Link> getIssueTabs(RequestContext requestContext, Issue issue, Integer relationshipCount)
            throws DatabaseException {

        AccessUser user = requestContext.getUser();

        List<Link> links = new ArrayList<>();

        // Issue History tab
        if (Access.hasPermission(user, AppPaths.ISSUES_DETAIL)) {
            links.add(new Link(requestContext).setName(ISSUE_TAB_HISTORY)
                    .setAjaxPath(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId())
                    .setTitleKey("issues.tab.history"));
        }
        
        // relationshipCount is optional, if not given, run a new query to get it.
        if (relationshipCount == null) {
            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            List<Integer> types = Arrays.asList(ObjectTypes.COMPANY, ObjectTypes.HARDWARE, ObjectTypes.SOFTWARE);
            List<Integer> linkedTypes = Arrays.asList(ObjectTypes.ISSUE);
            relationshipCount = systemService.getObjectMapCount(types, issue.getId(), linkedTypes);
        }

        // Issue History tab
        if (Access.hasPermission(user, AppPaths.ISSUES_RELATIONSHIP)) {
            links.add(new Link(requestContext).setName(ISSUE_TAB_RELATIONSHIP)
                    .setAppPath(AppPaths.ISSUES_RELATIONSHIP + "?issueId=" + issue.getId())
                    .setTitle(Localizer.getText(requestContext, "common.tab.relationships", new Integer[]{relationshipCount})));
        }
        return links;
    }

    public static String formatAssigneeName(RequestContext requestContext, AccessUser assignee) {
        if (assignee == null || assignee.getId() == 0) {
            return Localizer.getText(requestContext, "issueMgmt.colName.unassigned");
        } else {
            return AdminUtils.getSystemUsername(requestContext, assignee);
        }
    }

    /**
     * Returns an assignee icon with name
     * @param request
     * @param hasPermission
     * @param assigneeName
     * @param assigneeId
     * @return
     */
    public static String getAssigneeIconLink(RequestContext requestContext, boolean hasPermission, AccessUser assignee) {
        if (assignee != null && assignee.getId() != 0) {
            return Links.getUserIconLink(requestContext, assignee, hasPermission, true).getString();
        } else {
            return IssueUtils.formatAssigneeName(requestContext, assignee);
        }
    }

    /**
     * Returns formatted email subject.
     * @param subject
     * @return
     */
    public static String formatEmailSubject(RequestContext requestContext, Issue issue) {
        return Localizer.getText(requestContext, "issues.issueAdd.emailSubject",
                new String[]{String.valueOf(issue.getId()), issue.getSubject()});
    }

    public static String formatEmailBody(RequestContext requestContext, String bodyField) {
        return IssueEmailParser.EMAIL_BODY_SEPARATOR
                + "\n"
                + Localizer.getText(requestContext, "issues.email.bodySeparatorMessage")
                + "\n\n"
                + bodyField;
    }

    public static boolean isHtmlEmail(String content) {
        content = content.toLowerCase();
        return (content.startsWith("<html") || content.startsWith("<!doctype html")) && content.endsWith("</html>");
    }

    /**
     * Application Settings > "Default Due Date" allows setting number of days by default the issue is due, e.g. 3 days. 
     */
    public static void setDefaultDueDate(RequestContext requestContext, Issue issue) {
        int diff = ConfigManager.app.getIssueDueDateDiff();
        
        if (diff != AppConfigManager.ISSUE_DUE_DATE_DEFAULT) {
            // DatetimeUtils.newLocalCalendar already includes timezone offset and daylight savings. 
            Calendar cal = DatetimeUtils.newLocalCalendar(requestContext);
            cal.add(Calendar.DATE, diff);
            
            issue.setDueDate(cal.getTime());
            issue.setHasDueDate(true);
        }
    }
}
