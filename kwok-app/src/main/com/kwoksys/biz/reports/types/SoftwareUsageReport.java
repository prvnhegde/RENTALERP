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
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.reports.Report;
import com.kwoksys.biz.reports.ReportService;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.biz.reports.dao.ReportQueries;
import com.kwoksys.biz.reports.dto.SoftwareUsage;
import com.kwoksys.biz.reports.writers.ReportWriter;
import com.kwoksys.biz.software.SoftwareSearch;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.StringUtils;

/**
 * SoftwareUsageReport
 */
public class SoftwareUsageReport extends Report {

    private String reportCriteriaMapAttr;

    public SoftwareUsageReport(RequestContext requestContext, String reportCriteriaMapAttr) {
        this.requestContext = requestContext;
        this.reportCriteriaMapAttr = reportCriteriaMapAttr;
    }

    public String getFilename() {
        String filename = ConfigManager.reports.getSoftwareUsageReportFilename();
        return StringUtils.isEmpty(filename) ? Localizer.getText(requestContext, "reports.workflow.type.software_usage_report") : filename;
    }

    public String getReportFormName() {
        return "SoftwareSearchForm";
    }

    public String getReportPath() {
        return AppPaths.REPORTS_SOFTWARE_USAGE_SEARCH;
    }

    @Override
    public void populateData(ReportWriter reportWriter) throws Exception {
        HttpSession session = requestContext.getSession();

        // This is for column header.
        List<String> columnKeys = computeColumns(ReportUtils.getSoftwareUsageExportColumns());

        for (String column : columnKeys) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column." + column));
        }

        // Print custom field headers
        Collection<Attribute> attrs = computeCustFieldColumns(requestContext, ObjectTypes.SOFTWARE);

        for (Attribute attr : attrs) {
            getColumnHeaders().add(attr.getName());
        }

        reportWriter.addHeaderRow(getColumnHeaders());

        // Getting search criteria map from session variable.
        SoftwareSearch softwareSearch = new SoftwareSearch();
        if (session.getAttribute(reportCriteriaMapAttr) != null) {
            softwareSearch.setSearchCriteriaMap((Map) session.getAttribute(reportCriteriaMapAttr));
        }

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria(softwareSearch);

        // Will use default ordering here
        query.addSortColumn(ReportQueries.getSoftwareUsageOrderByColumn(Software.NAME));
        query.addSortColumn(ReportQueries.getSoftwareUsageOrderByColumn(Hardware.HARDWARE_NAME));

        ReportService reportService = ServiceProvider.getReportService(requestContext);
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Loop through the Software Usage list.
        for (SoftwareUsage software : reportService.getSoftwareUsage(query)) {
            List<String> columns = new ArrayList<>();

            for (String column : columnKeys) {
                if (column.equals(Software.NAME)) {
                    columns.add(software.getName());

                } else if (column.equals(Software.DESCRIPTION)) {
                        columns.add(software.getDescription());

                } else if (column.equals(Software.TYPE)) {
                    columns.add(software.getTypeName());

                } else if (column.equals(Hardware.HARDWARE_NAME)) {
                    columns.add(software.getHardwareName());

                } else if (column.equals(Hardware.OWNER_NAME)) {
                    columns.add(software.getHardwareOwnerName());
                }
            }

            // Add custom field values
            addCustomFieldValues(adminService, attrs, columns, software);

            reportWriter.addRow(columns);
        }
    }
}