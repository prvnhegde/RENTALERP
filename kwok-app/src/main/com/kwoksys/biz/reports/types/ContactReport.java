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
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.core.ContactSearch;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Contact;
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
 * ContactReport
 */
public class ContactReport extends Report {

    private String reportCriteriaMapAttr;

    public ContactReport(RequestContext requestContext, String reportCriteriaMapAttr) {
        this.requestContext = requestContext;
        this.reportCriteriaMapAttr = reportCriteriaMapAttr;
    }

    public String getFilename() {
        String filename = ConfigManager.reports.getContactsReportFilename();
        return StringUtils.isEmpty(filename) ? Localizer.getText(requestContext, "reports.workflow.type.contact_report") : filename;
    }

    public String getReportFormName() {
        return "ContactSearchForm";
    }

    public String getReportPath() {
        return AppPaths.REPORTS_CONTRACT_SEARCH;
    }

    @Override
    public void populateData(ReportWriter reportWriter) throws Exception {
        HttpSession session = requestContext.getSession();

        // This is for column header.
        List<String> columnKeys = computeColumns(ConfigManager.app.getContactsExportColumns());

        for (String column : columnKeys) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column." + column));
        }

        // Print custom field headers
        Collection<Attribute> attrs = computeCustFieldColumns(requestContext, ObjectTypes.CONTACT);

        for (Attribute attr : attrs) {
            getColumnHeaders().add(attr.getName());
        }

        reportWriter.addHeaderRow(getColumnHeaders());

        // Getting search criteria map from session variable.
        ContactSearch contactSearch = new ContactSearch();
        if (session.getAttribute(reportCriteriaMapAttr) != null) {
            contactSearch.setSearchCriteriaMap((Map) session.getAttribute(reportCriteriaMapAttr));
        }

        // Get order and orderBy
        String orderBy = getReportColumnOrderBy();
        if (orderBy == null) {
            orderBy = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.CONTACTS_ORDER_BY, Contact.COMPANY_NAME);
        }

        String order = getReportColumnOrder();
        if (order == null) {
            order = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.CONTACTS_ORDER, QueryCriteria.ASCENDING);
        }

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria(contactSearch);

        if (CompanyUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        List<Contact> contacts = contactService.getCompanyContacts(query, null, ObjectTypes.COMPANY_EMPLOYEE_CONTACT);

        for (Contact contact : contacts) {
            List<String> columns = new ArrayList<>();
            for (String column : columnKeys) {
                if (column.equals(Contact.FIRST_NAME)) {
                    columns.add(contact.getFirstName());

                } else if (column.equals(Contact.LAST_NAME)) {
                    columns.add(contact.getLastName());

                } else if (column.equals(Contact.TITLE)) {
                    columns.add(contact.getTitle());

                } else if (column.equals(Contact.PRIMARY_EMAIL)) {
                    columns.add(contact.getEmailPrimary());

                } else if (column.equals(Contact.WORK_PHONE)) {
                    columns.add(contact.getPhoneWork());

                } else if (column.equals(Contact.COMPANY_NAME)) {
                    columns.add(contact.getCompanyName());
                }
            }

            reportWriter.addRow(columns);
        }
    }
}