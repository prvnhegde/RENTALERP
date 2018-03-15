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
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.reports.Report;
import com.kwoksys.biz.reports.ReportService;
import com.kwoksys.biz.reports.dao.ReportQueries;
import com.kwoksys.biz.reports.writers.ReportWriter;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.util.StringUtils;

/**
 * HardwareMembersReport
 */
public class HardwareMembersReport extends Report {

    private String reportCriteriaMapKey;

    public HardwareMembersReport(RequestContext requestContext, String reportCriteriaMapKey) {
        this.requestContext = requestContext;
        this.reportCriteriaMapKey = reportCriteriaMapKey;
    }

    public String getFilename() {
        String filename = ConfigManager.reports.getHardwareMembersReportFilename();
        return StringUtils.isEmpty(filename) ? Localizer.getText(requestContext, "reports.workflow.type.hardware_member_report") : filename;
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
        List<String> columnKeys = computeColumns(ConfigManager.app.getHardwareMembersExportColumns());

        if (columnKeys.contains(Hardware.ID)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.hardware_id"));
        }
        if (columnKeys.contains(Hardware.HARDWARE_NAME)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.hardware_name"));
        }
        if (columnKeys.contains(Hardware.ID)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.hardware_member_id"));
        }
        if (columnKeys.contains(Hardware.HARDWARE_NAME)) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column.hardware_member_name"));
        }
        reportWriter.addHeaderRow(getColumnHeaders());

        // Getting search criteria map from session variable.
        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, reportCriteriaMapKey);

        // Get order and orderBy
        String orderBy = getReportColumnOrderBy();
        if (orderBy == null) {
            orderBy = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        }

        String order = getReportColumnOrder();
        if (order == null) {
            order = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.HARDWARE_ORDER, QueryCriteria.ASCENDING);
        }

        // Ready to pass variables to query.
        QueryCriteria searchQuery = new QueryCriteria(hardwareSearch);

        QueryCriteria query = new QueryCriteria();
        if (HardwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(ReportQueries.getHardwareMemberOrderByColumn(orderBy), order);
        }

        ReportService reportService = ServiceProvider.getReportService(requestContext);

        // Loop through the Hardware list.
        for (Map<String, String> record : reportService.getHardwareMembers(searchQuery, query)) {
            List<String> columns = new ArrayList<>();

            if (columnKeys.contains(Hardware.ID)) {
                columns.add(record.get("hardware_id"));
            }
            if (columnKeys.contains(Hardware.HARDWARE_NAME)) {
                columns.add(record.get("hardware_name"));
            }
            if (columnKeys.contains(Hardware.ID)) {
                columns.add(record.get("hardware_member_id"));
            }
            if (columnKeys.contains(Hardware.HARDWARE_NAME)) {
                columns.add(record.get("hardware_member_name"));
            }

            reportWriter.addRow(columns);
        }
    }
}