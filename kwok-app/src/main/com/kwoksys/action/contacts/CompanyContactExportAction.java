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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.reports.writers.CsvReportWriter;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for exporting company contacts.
 */
public class CompanyContactExportAction extends Action2 {

    public String execute() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        // Get cookie variables
        String orderBy = SessionManager.getAttribute(request, SessionManager.COMPANY_CONTACTS_ORDER_BY, Contact.FIRST_NAME);
        String order = SessionManager.getAttribute(request, SessionManager.COMPANY_CONTACTS_ORDER, QueryCriteria.ASCENDING);

        // Call the service
        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        String[] columnKeys = {Contact.FIRST_NAME, Contact.LAST_NAME, Contact.TITLE, Contact.PRIMARY_EMAIL,
                Contact.WORK_PHONE, Contact.COMPANY_NAME};

        ResponseContext responseContext = new ResponseContext(response);
        responseContext.setAttachementName(Localizer.getText(requestContext, "contactMgmt.companyContactExport.filename", new Object[]{company.getName()}));

        CsvReportWriter csvReportWriter = new CsvReportWriter();
        csvReportWriter.init(responseContext);

        // This is for column header.
        List<String> columnHeaders = new ArrayList<>();
        for (String column : columnKeys) {
            columnHeaders.add(Localizer.getText(requestContext, "common.column." + column));
        }

        csvReportWriter.addHeaderRow(columnHeaders);

        // Ready to pass variables to query.
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);

        List<Contact> contacts = contactService.getCompanyContacts(queryCriteria, company.getId(),
                ObjectTypes.COMPANY_EMPLOYEE_CONTACT);

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
            csvReportWriter.addRow(columns);
        }

        return csvReportWriter.close();
    }
}

