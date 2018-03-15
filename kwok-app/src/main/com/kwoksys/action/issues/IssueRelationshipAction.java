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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableEmptyTemplate;
import com.kwoksys.action.common.template.TableHeaderTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.action.hardware.HardwareListTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for issue relationship.
 */
public class IssueRelationshipAction extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        Integer issueId = requestContext.getParameter("issueId");

        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        Issue issue = issueService.getIssue(issueId);

        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        List<Integer> types = Arrays.asList(ObjectTypes.COMPANY, ObjectTypes.HARDWARE, ObjectTypes.SOFTWARE);
        List<Integer> linkedTypes = Arrays.asList(ObjectTypes.ISSUE);
        int relationshipCount = systemService.getObjectMapCount(types, issueId, linkedTypes);

        String hwOrderBy = SessionManager.getOrSetAttribute(requestContext, "hwOrderBy", SessionManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        String hwOrder = SessionManager.getOrSetAttribute(requestContext, "hwOrder", SessionManager.HARDWARE_ORDER, QueryCriteria.ASCENDING);

        // Get column headers
        List<String> hwColumnHeaders = HardwareUtils.getColumnHeaderList();

        // Do some sorting.
        QueryCriteria query = new QueryCriteria();

        if (HardwareUtils.isSortableColumn(hwOrderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(hwOrderBy), hwOrder);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");

        //
        // Template: HardwareListTemplate
        //
        HardwareListTemplate listTemplate = standardTemplate.addTemplate(new HardwareListTemplate("_hardware"));
        listTemplate.setHardwareList(issueService.getIssueHardwareList(new QueryCriteria(), issue.getId()));
        listTemplate.setColspan(hwColumnHeaders.size());
        listTemplate.setCounter(new Counter());

        //
        // Template: TableEmptyTemplate
        //
        TableEmptyTemplate empty = standardTemplate.addTemplate(new TableEmptyTemplate("_hardware"));
        empty.setColSpan(hwColumnHeaders.size());
        empty.setRowText(Localizer.getText(requestContext, "itMgmt.hardwareList.emptyTableMessage"));

        //
        // Template: TableHeaderTemplate
        //
        TableHeaderTemplate tableHeader = standardTemplate.addTemplate(new TableHeaderTemplate("_hardware"));
        tableHeader.setColumnList(hwColumnHeaders);
        tableHeader.setSortableColumnList(HardwareUtils.getSortableColumns());
        tableHeader.setColumnPath(AppPaths.ISSUES_RELATIONSHIP + "?issueId=" + issueId);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setOrderBy(hwOrderBy);
        tableHeader.setOrderByParamName("hwOrderBy");
        tableHeader.setOrderParamName("hwOrder");
        tableHeader.setOrder(hwOrder);

        //
        // Linked software
        //
        String swOrderBy = SessionManager.getOrSetAttribute(requestContext, "swOrderBy", SessionManager.SOFTWARE_ORDER_BY, Software.NAME);
        String swOrder = SessionManager.getOrSetAttribute(requestContext, "swOrder", SessionManager.SOFTWARE_ORDER, QueryCriteria.ASCENDING);

        // Do some sorting.
        QueryCriteria swQuery = new QueryCriteria();

        if (SoftwareUtils.isSortableColumn(swOrderBy)) {
            swQuery.addSortColumn(SoftwareQueries.getOrderByColumn(swOrderBy), swOrder);
        }

        List<Software> softwareList = issueService.getIssueSoftwareList(swQuery, issue.getId());
        List<DataRow> formattedList = SoftwareUtils.formatSoftwareList(requestContext, softwareList, new Counter());

        //
        // Template: TableTemplate
        //
        TableTemplate swTableTemplate = standardTemplate.addTemplate(new TableTemplate("_software"));
        swTableTemplate.setDataList(formattedList);
        swTableTemplate.setColumnHeaders(SoftwareUtils.getColumnHeaderList());
        swTableTemplate.setColumnPath(AppPaths.ISSUES_RELATIONSHIP + "?issueId=" + issueId);
        swTableTemplate.setSortableColumnHeaders(SoftwareUtils.getSortableColumns());
        swTableTemplate.setColumnTextKey("common.column.");
        swTableTemplate.setEmptyRowMsgKey("itMgmt.softwareList.emptyTableMessage");
        swTableTemplate.setOrderBy(swOrderBy);
        swTableTemplate.setOrder(swOrder);
        swTableTemplate.setOrderByParamName("swOrderBy");
        swTableTemplate.setOrderParamName("swOrder");

        //
        // Template: TableTemplate
        //
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "cOrderBy", SessionManager.COMPANIES_ORDER_BY, Company.COMPANY_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "cOrder", SessionManager.COMPANIES_ORDER, QueryCriteria.ASCENDING);

        query = new QueryCriteria();
        if (Company.isSortableCompanyColumn(orderBy)) {
            query.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        List<Company> companyDataset = issueService.getIssueCompanyList(query, issue.getId());
        List<DataRow> dataList = CompanyUtils.formatCompanyList(requestContext, companyDataset, new Counter());

        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate("_companies"));
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(ConfigManager.app.getContactsCompanyColumnList());
        tableTemplate.setSortableColumnHeaders(Company.getSortableCompanyColumnList());
        tableTemplate.setColumnPath(AppPaths.ISSUES_RELATIONSHIP + "?issueId=" + issueId);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setOrderByParamName("cOrderBy");
        tableTemplate.setOrderParamName("cOrder");
        tableTemplate.setEmptyRowMsgKey("contactMgmt.companyList.emptyTableMessage");

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("issueMgmt.issueDetail.title", new Object[] {issue.getSubject()});

        // Back to Issue list.
        if (Access.hasPermission(accessUser, AppPaths.ISSUES_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ISSUES_LIST);
            link.setTitleKey("issueMgmt.cmd.issueList");
            header.addHeaderCmds(link);
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(IssueUtils.getIssueTabs(requestContext, issue, relationshipCount));
        tabs.setTabActive(IssueUtils.ISSUE_TAB_RELATIONSHIP);

        //
        // Template: IssueSpecTemplate
        //
        IssueSpecTemplate spec = standardTemplate.addTemplate(new IssueSpecTemplate(issue));
        spec.setHeaderText(issue.getSubject());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}