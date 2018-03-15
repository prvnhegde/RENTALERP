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
package com.kwoksys.biz.reports.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.auth.core.Permissions;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueSearch;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dao.IssueQueries;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.reports.Report;
import com.kwoksys.biz.reports.writers.ReportWriter;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.util.StringUtils;

/**
 * IssueReport
 */
public class IssueReport extends Report {

    private String reportCriteriaMapKey;

    public IssueReport(RequestContext requestContext, String reportCriteriaMapKey) {
        this.requestContext = requestContext;
        this.reportCriteriaMapKey = reportCriteriaMapKey;
    }

    public String getFilename() {
        String filename = ConfigManager.reports.getIssuesReportFilename();
        return StringUtils.isEmpty(filename) ? Localizer.getText(requestContext, "reports.workflow.type.issue_report") : filename;
    }

    public String getReportFormName() {
        return "IssueSearchForm";
    }

    public String getReportPath() {
        return AppPaths.REPORTS_ISSUE_SEARCH;
    }

    @Override
    public void populateData(ReportWriter reportWriter) throws Exception {
        List<String> columnKeys = computeColumns(ConfigManager.app.getIssueExportColumns());

        // This is for column header.
        for (String column : columnKeys) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column." + column));
        }

        // Print custom field headers
        Collection<Attribute> attrs = computeCustFieldColumns(requestContext, ObjectTypes.ISSUE);

        for (Attribute attr : attrs) {
            getColumnHeaders().add(attr.getName());
        }

        reportWriter.addHeaderRow(getColumnHeaders());

        // Getting search criteria map from session variable.
        IssueSearch issueSearch = new IssueSearch(requestContext, reportCriteriaMapKey);

        // For advanced access control
        AccessUser user = requestContext.getUser();
        if (!user.hasPermission(Permissions.ISSUE_READ_PERMISSION)) {
            issueSearch.put(IssueSearch.ISSUE_PERMITTED_USER_ID, user.getId());
        }

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria(issueSearch);

        String orderBy = getReportColumnOrderBy();
        if (orderBy == null) {
            orderBy = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.ISSUES_ORDER_BY, Issue.CREATION_DATE);
        }

        String order = getReportColumnOrder();
        if (order == null) {
            order = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.ISSUES_ORDER, QueryCriteria.DESCENDING);
        }

        if (IssueUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(IssueQueries.getOrderByColumn(orderBy), order);
        }

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        List<Issue> issues = issueService.getIssues(query);

        for (Issue issue : issues) {
            List<String> columns = new ArrayList<>();
            for (String column : columnKeys) {
                if (column.equals(Issue.ID)) {
                    columns.add(String.valueOf(issue.getId()));

                } else if (column.equals(Issue.ASSIGNEE_NAME)) {
                    columns.add(IssueUtils.formatAssigneeName(requestContext, issue.getAssignee()));

                } else if (column.equals(Issue.TITLE)) {
                    columns.add(issue.getSubject());

                } else if (column.equals(Issue.DESCRIPTION)) {
                    columns.add(issue.getDescription());

                } else if (column.equals(Issue.CREATOR_NAME)) {
                    columns.add(AdminUtils.getSystemUsername(requestContext, issue.getCreator()));

                } else if (column.equals(Issue.CREATION_DATE)) {
                    columns.add(issue.getCreationDate());

                } else if (column.equals(Issue.MODIFIER_NAME)) {
                    columns.add(AdminUtils.getSystemUsername(requestContext, issue.getModifier()));

                } else if (column.equals(Issue.MODIFICATION_DATE)) {
                    columns.add(issue.getModificationDate());

                } else if (column.equals(Issue.DUE_DATE)) {
                    columns.add(issue.getDueDateShort());

                } else if (column.equals(Issue.TYPE)) {
                    columns.add(issue.getTypeName());

                } else if (column.equals(Issue.STATUS)) {
                    columns.add(issue.getStatusName());

                } else if (column.equals(Issue.PRIORITY)) {
                    columns.add(issue.getPriorityName());
                }
            }

            // Add custom field values
            addCustomFieldValues(adminService, attrs, columns, issue);

            reportWriter.addRow(columns);
        }
    }
}
