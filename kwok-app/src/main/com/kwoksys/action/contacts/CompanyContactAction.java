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
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyTabs;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.core.ContactUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for showing employee contacts.
 */
public class CompanyContactAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        Integer companyId = requestContext.getParameter("companyId");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.COMPANY_CONTACTS_ORDER_BY, Contact.FIRST_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.COMPANY_CONTACTS_ORDER, QueryCriteria.ASCENDING);

        // Call the service
        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        QueryCriteria queryCriteria = new QueryCriteria();

        if (ContactUtils.isSortableColumn(orderBy)) {
            queryCriteria.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        // Get column heading
        List<String> columnHeaders = ContactUtils.getEmployeeContactColumnHeader();

        List<Contact> contacts = contactService.getCompanyContacts(queryCriteria, company.getId(), ObjectTypes.COMPANY_EMPLOYEE_CONTACT);
        List<DataRow> dataList = ContactUtils.formatContacts(requestContext, contacts, columnHeaders, new Counter());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", companyId);

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        // Add Contact
        if (Access.hasPermission(user, AppPaths.CONTACTS_CONTACT_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_CONTACT_ADD + "?companyId=" + companyId);
            link.setTitleKey("contactMgmt.cmd.employeeContactAdd");
            header.addHeaderCmds(link);
        }

        // Export Contact
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_CONTACT_EXPORT)) {
            Link link = new Link(requestContext);
            link.setExportPath(AppPaths.CONTACTS_COMPANY_CONTACT_EXPORT + "?companyId=" + companyId);
            link.setTitleKey("contactMgmt.cmd.companyOtherContactsExport");
            link.setImgSrc(Image.getInstance().getCsvFileIcon());
            header.addHeaderCmds(link);
        }

        // Company list
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST);
            link.setTitleKey("contactMgmt.cmd.companyList");
            header.addHeaderCmds(link);
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(CompanyUtils.getCompanyTabs(requestContext, company));
        tabs.setTabActive(CompanyTabs.OTHER_CONTACT_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableHeader = standardTemplate.addTemplate(new TableTemplate());
        tableHeader.setDataList(dataList);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setSortableColumnHeaders(ContactUtils.getSortableContactColumnList());
        tableHeader.setColumnPath(AppPaths.CONTACTS_COMPANY_CONTACT + "?companyId=" + companyId);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setEmptyRowMsgKey("contactMgmt.companyContact.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
}
