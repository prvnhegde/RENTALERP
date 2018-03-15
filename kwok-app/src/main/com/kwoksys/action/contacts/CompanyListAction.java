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
package com.kwoksys.action.contacts;

import java.util.List;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanySearch;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for displaying company list.
 */
public class CompanyListAction extends Action2 {

    public String execute() throws Exception {
        ContactSearchForm actionForm = getSessionBaseForm(ContactSearchForm.class);

        AccessUser user = requestContext.getUser();

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.COMPANIES_ORDER_BY, Company.COMPANY_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.COMPANIES_ORDER, QueryCriteria.ASCENDING);

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll")) {
            request.getSession().setAttribute(SessionManager.COMPANIES_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.COMPANIES_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getCompanyRows());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        CompanySearch companySearch = new CompanySearch(requestContext, SessionManager.COMPANY_SEARCH_CRITERIA_MAP);

        // Getting search criteria map from session variable.
        if (!cmd.isEmpty()) {
            companySearch.reset();
            actionForm.setRequest(requestContext);

            if (cmd.equals("search")) {
                // We are expecting user to enter some search criteria.
                companySearch.prepareMap(actionForm);

            } else if (cmd.equals("showAll")) {
                // We're expecting to reset the search criteria.
            }

            companySearch.put("cmd", cmd);
        }

        // Ready to pass variables to query.
        QueryCriteria queryCriteria = new QueryCriteria(companySearch);
        queryCriteria.setLimit(rowLimit, rowStart);

        if (Company.isSortableCompanyColumn(orderBy)) {
            queryCriteria.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        // Get column headers
        List<String> columnHeaders = ConfigManager.app.getContactsCompanyColumnList();

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        int rowCount = contactService.getCompanyCount(queryCriteria);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("contactMgmt.companyList.title");
        header.setTitleClassNoLine();
        
        // Link to add company page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_ADD)) {
            Link link = new Link(requestContext)
                    .setAjaxPath(AppPaths.CONTACTS_COMPANY_ADD)
                    .setTitleKey("contactMgmt.cmd.companyAdd");
            header.addHeaderCmds(link);
        }

        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST_EXPORT)) {
            Link link = new Link(requestContext)
                    .setExportPath(AppPaths.CONTACTS_COMPANY_LIST_EXPORT)
                    .setTitleKey("contacts.cmd.companyExport")
                    .setImgSrc(Image.getInstance().getCsvFileIcon());
            header.addHeaderCmds(link);
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = standardTemplate.addTemplate(new RecordsNavigationTemplate());
        if (companySearch.getSearchCriteriaMap().containsKey("cmd")
                && companySearch.getSearchCriteriaMap().get("cmd").equals("search")) {        
            nav.setInfoText(Localizer.getText(requestContext, "contactMgmt.companyList.searchResult"));
        }
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "contactMgmt.companyList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.CONTACTS_COMPANY_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.CONTACTS_COMPANY_LIST + "?rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(Company.getSortableCompanyColumnList());
        tableTemplate.setColumnPath(AppPaths.CONTACTS_COMPANY_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("contactMgmt.companyList.emptyTableMessage");

        if (rowCount != 0) {
            List<Company> companyDataset = contactService.getCompanies(queryCriteria);
            Counter counter = new Counter(rowStart);
            List<DataRow> dataList = CompanyUtils.formatCompanyList(requestContext, companyDataset, counter);
            tableTemplate.setDataList(dataList);
        }

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
}
