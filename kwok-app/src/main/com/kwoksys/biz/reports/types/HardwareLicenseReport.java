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
import java.util.List;
import java.util.Map;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.reports.Report;
import com.kwoksys.biz.reports.ReportService;
import com.kwoksys.biz.reports.dao.ReportQueries;
import com.kwoksys.biz.reports.writers.ReportWriter;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.util.StringUtils;

/**
 * HardwareLicenseReport
 */
public class HardwareLicenseReport extends Report {

    private String reportCriteriaMapKey;

    public HardwareLicenseReport(RequestContext requestContext, String reportCriteriaMapKey) {
        this.requestContext = requestContext;
        this.reportCriteriaMapKey = reportCriteriaMapKey;
    }

    public String getFilename() {
        String filename = ConfigManager.reports.getHardwareLicensesReportFilename();
        return StringUtils.isEmpty(filename) ? Localizer.getText(requestContext, "reports.workflow.type.hardware_license_report") : filename;
    }

    public String getReportFormName() {
        return "HardwareSearchForm";
    }

    public String getReportPath() {
        return AppPaths.REPORTS_HARDWARE_SEARCH;
    }

    @Override
    public void populateData(ReportWriter reportWriter) throws Exception {
        // This is for column header.
        List<String> columnKeys = computeColumns(ConfigManager.app.getHardwareLicenseExportColumns());

        if (columnKeys.contains(Hardware.ID)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.hardware_id"));
        }
        if (columnKeys.contains(Hardware.HARDWARE_NAME)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.hardware_name"));
        }
        if (columnKeys.contains(Software.NAME)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.software_name"));
        }
        if (columnKeys.contains(SoftwareLicense.LICENSE_KEY)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.license_key"));
        }
        if (columnKeys.contains(SoftwareLicense.LICENSE_NOTE)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.license_note"));
        }

        reportWriter.addHeaderRow(getColumnHeaders());

        // Getting search criteria map from session variable.
        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, reportCriteriaMapKey);

        String order = getReportColumnOrder();
        if (order == null) {
            order = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.HARDWARE_ORDER, QueryCriteria.ASCENDING);
        }

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria(hardwareSearch);

        // Get order and orderBy
        query.addSortColumn(ReportQueries.getHardwareLicenseOrderByColumn(Hardware.HARDWARE_NAME), QueryCriteria.ASCENDING);
        query.addSortColumn(ReportQueries.getHardwareLicenseOrderByColumn(Software.NAME), QueryCriteria.ASCENDING);
        query.addSortColumn(ReportQueries.getHardwareLicenseOrderByColumn(SoftwareLicense.LICENSE_KEY), QueryCriteria.ASCENDING);

        ReportService reportService = ServiceProvider.getReportService(requestContext);

        // Loop through the Hardware list.
        for (Map<String, String> record : reportService.getHardwareLicenses(query)) {
            List<String> columns = new ArrayList<>();

            if (columnKeys.contains(Hardware.ID)) {
                columns.add(record.get("hardware_id"));
            }
            if (columnKeys.contains(Hardware.HARDWARE_NAME)) {
                columns.add(record.get("hardware_name"));
            }
            if (columnKeys.contains(Software.NAME)) {
                columns.add(record.get("software_name"));
            }
            if (columnKeys.contains(SoftwareLicense.LICENSE_KEY)) {
                columns.add(record.get("license_key"));
            }
            if (columnKeys.contains(SoftwareLicense.LICENSE_NOTE)) {
                columns.add(record.get("license_note"));
            }

            reportWriter.addRow(columns);
        }
    }
}