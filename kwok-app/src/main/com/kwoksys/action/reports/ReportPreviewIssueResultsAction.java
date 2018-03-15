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
package com.kwoksys.action.reports;

import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.issues.IssueSearchForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.IssueAccess;
import com.kwoksys.biz.auth.core.Permissions;
import com.kwoksys.biz.auth.core.ReportAccess;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueSearch;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dao.IssueQueries;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for running a report.
 */
public class ReportPreviewIssueResultsAction extends Action2 {

    public String execute() throws Exception {
        IssueSearchForm actionForm = getSessionBaseForm(IssueSearchForm.class);

        AccessUser accessUser = requestContext.getUser();
        String reportType = requestContext.getParameterString("reportType");

        // Checks report permission
        if (!ReportAccess.hasPermission(accessUser, reportType)) {
            throw new AccessDeniedException();
        }

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        int rowStart = requestContext.getParameter("rowStart", 0);

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getIssueRows());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        IssueSearch issueSearch = new IssueSearch(requestContext, SessionManager.ISSUE_REPORT_CRITERIA_MAP);
        if (!cmd.isEmpty()) {
            if (cmd.equals("search")) {
                issueSearch.reset();
                issueSearch.prepareMap(actionForm);
            }
        }

        // For advanced access control
        if (!accessUser.hasPermission(Permissions.ISSUE_READ_PERMISSION)) {
            issueSearch.put(IssueSearch.ISSUE_PERMITTED_USER_ID, accessUser.getId());
        }

        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        QueryCriteria query = new QueryCriteria(issueSearch);
        query.setLimit(rowLimit, rowStart);
        query.addSortColumn(IssueQueries.getOrderByColumn(Issue.CREATION_DATE), QueryCriteria.DESCENDING);

        int rowCount = issueService.getCount(query);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: ReportPreviewResultsTemplate
        //
        ReportPreviewResultsTemplate preview = standardTemplate.addTemplate(new ReportPreviewResultsTemplate());
        preview.setReportType(reportType);

        for (String column : ConfigManager.app.getIssueExportColumns()) {
            preview.addReportColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }
        for (Attribute attr : new AttributeManager(requestContext).getCustomFieldList(ObjectTypes.ISSUE)) {
            preview.addReportColumnOptions(new LabelValueBean(attr.getName(), String.valueOf(attr.getId())));
        }

        for (String column : IssueUtils.getSortableColumns()) {
            preview.addSortColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = standardTemplate.addTemplate(new RecordsNavigationTemplate());
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "issueMgmt.issueList.rowCount",
                new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.REPORTS_ISSUE_SEARCH + "?reportType=" + reportType + "&rowCmd=showAll");
        nav.setPath(AppPaths.REPORTS_ISSUE_SEARCH + "?reportType=" + reportType + "&rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(IssueUtils.getIssueColumnHeaders());
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setEmptyRowMsgKey("issueMgmt.issueList.emptyTableMessage");

        if (rowCount != 0) {
            List<Issue> issues = issueService.getIssues(query);
            boolean hasIssueDetailAccess = Access.hasPermission(accessUser, AppPaths.ISSUES_DETAIL);

            IssueAccess access = new IssueAccess(accessUser);

            if (hasIssueDetailAccess) {
                access.setAllowedIssues(issues);
            }
            List<DataRow> dataList = IssueUtils.formatIssues(requestContext, issues, access, new Counter(rowStart));
            tableTemplate.setDataList(dataList);
        }
        
        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
