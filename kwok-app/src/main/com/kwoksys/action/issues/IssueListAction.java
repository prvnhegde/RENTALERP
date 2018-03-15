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

import java.util.Arrays;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.IssueAccess;
import com.kwoksys.biz.auth.core.Permissions;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueSearch;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dao.IssueQueries;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.FeatureManager;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

/**
 * Action class for issue list.
 * Expecting:
 * cmd:            search|showNonClosed|showAll|showMyIssues|showMyReportedIssues
 * order:   
 * orderBy
 * rowStart
 * rowOffset
 * rowCmd:         showAll
 */
public class IssueListAction extends Action2 {

    public String execute() throws Exception {
        IssueSearchForm actionForm = getSessionBaseForm(IssueSearchForm.class);

        AccessUser accessUser = requestContext.getUser();

        String rowCmd = requestContext.getParameterString("rowCmd");
        String cmd = requestContext.getParameterString("cmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.ISSUES_ORDER_BY, Issue.CREATION_DATE);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.ISSUES_ORDER, QueryCriteria.DESCENDING);

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll")) {
            request.getSession().setAttribute(SessionManager.ISSUES_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.ISSUES_ROW_START, rowStart);
        }
        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getIssueRows());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        // Getting search criteria map from session variable.
        IssueSearch issueSearch = new IssueSearch(requestContext, SessionManager.ISSUE_SEARCH_CRITERIA_MAP);
        issueSearch.prepareMap(actionForm);

        // For advanced access control
        if (!accessUser.hasPermission(Permissions.ISSUE_READ_PERMISSION)) {
            issueSearch.put(IssueSearch.ISSUE_PERMITTED_USER_ID, accessUser.getId());
        }
        
        // Pass variables to query.
        QueryCriteria query = new QueryCriteria(issueSearch);
        query.setLimit(rowLimit, rowStart);

        if (IssueUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(IssueQueries.getOrderByColumn(orderBy), order);
        }

        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        // Get column headers
        List<String> columnHeaders = IssueUtils.getIssueColumnHeaders();

        int rowCount = issueService.getCount(query);

        List<LabelValueBean> filterOptions = Arrays.asList(
                new SelectOneLabelValueBean(requestContext, "showAll"),
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.showNonClosed"), "showNonClosed"),
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.showOpenUnassigned"), "showOpenUnassigned"),
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.showMyIssues"), "showMyIssues"),
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.showMyReportedIssues"), "showMyReportedIssues"));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ISSUES_LIST);
        standardTemplate.setAttribute("filterOptions", filterOptions);

        if (!issueSearch.getSearchCriteriaMap().isEmpty()) {
            standardTemplate.setAttribute("searchResultText", Localizer.getText(requestContext, "issueMgmt.issueList.searchResult"));
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = standardTemplate.addTemplate(new RecordsNavigationTemplate());
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "issueMgmt.issueList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.ISSUES_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.ISSUES_LIST + "?rowStart=");

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("issueMgmt.issueList.title");
        header.setTitleClassNoLine();
        
        // Add Issue.
        if (Access.hasPermission(accessUser, AppPaths.ISSUES_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ISSUES_ADD);
            link.setTitleKey("issueMgmt.cmd.issueAdd");
            header.addHeaderCmds(link);
        }

        if (FeatureManager.isIssueGuestSubmitModuleEnabled(accessUser)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ISSUE_PLUGIN_ADD);
            link.setTitleKey("issuePlugin.issueAdd.title");
            header.addHeaderCmds(link);
        }

        // Export Issues
        if (Access.hasPermission(accessUser, AppPaths.ISSUES_LIST_EXPORT)) {
            Link link = new Link(requestContext);
            link.setExportPath(AppPaths.ISSUES_LIST_EXPORT + "?rowCmd=" + rowCmd + "&rowStart=" + rowStart);
            link.setTitleKey("issues.cmd.issueExport");
            link.setImgSrc(Image.getInstance().getCsvFileIcon());
            header.addHeaderCmds(link);
        }

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(IssueUtils.getSortableColumns());
        tableTemplate.setColumnPath(AppPaths.ISSUES_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
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
        
        if (ConfigManager.app.isIssuesMultipleDeleteEnabled()
                && Access.hasPermission(accessUser, AppPaths.ISSUES_DELETE_2)) {
            tableTemplate.setFormName(IssueSearchForm.class.getSimpleName());
            tableTemplate.setFormRemoveItemAction(AppPaths.ISSUES_DELETE_2);
            tableTemplate.setFormRowIdName("issueIds");
            tableTemplate.setFormSelectMultipleRows(true);
            tableTemplate.getFormButtons().put("form.button.delete", "common.form.confirmDelete");
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
