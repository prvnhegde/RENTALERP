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
import com.kwoksys.biz.contacts.core.CompanySearch;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.reports.writers.CsvReportWriter;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for exporting the company list.
 */
public class CompanyListExportAction extends Action2 {

    public String execute() throws Exception {
        String orderBy = SessionManager.getAttribute(request, SessionManager.COMPANIES_ORDER_BY, Company.COMPANY_NAME);
        String order = SessionManager.getAttribute(request, SessionManager.COMPANIES_ORDER, QueryCriteria.ASCENDING);

        CompanySearch companySearch = new CompanySearch(requestContext, SessionManager.COMPANY_SEARCH_CRITERIA_MAP);

        // Ready to pass variables to query.
        QueryCriteria queryCriteria = new QueryCriteria(companySearch);

        if (Company.isSortableCompanyColumn(orderBy)) {
            queryCriteria.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        // Get column headers
        List<String> columnKeys = ConfigManager.app.getContactsCompanyExportColumns();

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        ResponseContext responseContext = new ResponseContext(response);
        responseContext.setAttachementName(Localizer.getText(requestContext, "contacts.companyListExport.filename"));

        CsvReportWriter csvReportWriter = new CsvReportWriter();
        csvReportWriter.init(responseContext);

        // This is for column header.
        List<String> columnHeaders = new ArrayList<>();
        for (String column : columnKeys) {
            columnHeaders.add(Localizer.getText(requestContext, "common.column." + column));
        }

        csvReportWriter.addHeaderRow(columnHeaders);

        List<Company> companyDataset = contactService.getCompanies(queryCriteria);

        for (Company company : companyDataset) {
            List<String> columns = new ArrayList<>();
            for (String column : columnKeys) {
                if (column.equals(Company.COMPANY_NAME)) {
                    columns.add(company.getName());

                } else if (column.equals(Company.DESCRIPTION)) {
                    columns.add(company.getDescription());
                }
            }
            csvReportWriter.addRow(columns);
        }

        return csvReportWriter.close();
    }
}
