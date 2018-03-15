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
package com.kwoksys.action.reports;

import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.software.SoftwareSearchForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.ReportAccess;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.reports.ReportService;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.biz.reports.dao.ReportQueries;
import com.kwoksys.biz.reports.dto.SoftwareUsage;
import com.kwoksys.biz.software.SoftwareSearch;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for running a report.
 */
public class ReportPreviewSoftwareUsageResultsAction extends Action2 {

    public String execute() throws Exception {
        SoftwareSearchForm actionForm = getSessionBaseForm(SoftwareSearchForm.class);
        AccessUser user = requestContext.getUser();
        String reportType = requestContext.getParameterString("reportType");

        // Checks report permission
        if (!ReportAccess.hasPermission(user, reportType)) {
            throw new AccessDeniedException();
        }

        String rowCmd = requestContext.getParameterString("rowCmd");
        String order = requestContext.getParameterString("order");
        int rowStart = requestContext.getParameter("rowStart", 0);

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getSoftwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }
        
        SoftwareSearch softwareSearch = new SoftwareSearch(requestContext, SessionManager.SOFTWARE_USAGE_REPORT_CRITERIA_MAP);
        softwareSearch.prepareMap(actionForm);

        QueryCriteria query = new QueryCriteria(softwareSearch);
        query.setLimit(rowLimit, rowStart);
        query.addSortColumn(ReportQueries.getSoftwareUsageOrderByColumn(Software.NAME), order);
        query.addSortColumn(ReportQueries.getSoftwareUsageOrderByColumn(Hardware.HARDWARE_NAME), order);

        ReportService reportService = ServiceProvider.getReportService(requestContext);
        int rowCount = reportService.getSoftwareUsageCount(query);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: ReportPreviewResultsTemplate
        //
        ReportPreviewResultsTemplate preview = standardTemplate.addTemplate(new ReportPreviewResultsTemplate());
        preview.setReportType(reportType);

        for (String column : ReportUtils.getSoftwareUsageExportColumns()) {
            preview.addReportColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = standardTemplate.addTemplate(new RecordsNavigationTemplate());
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "itMgmt.softwareList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.REPORTS_SOFTWARE_USAGE_SEARCH + "?reportType=" + reportType + "&rowCmd=showAll");
        nav.setPath(AppPaths.REPORTS_SOFTWARE_USAGE_SEARCH + "?reportType=" + reportType + "&rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(ReportUtils.getSoftwareUsageColumns());
        tableTemplate.setColumnPath(AppPaths.SOFTWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setEmptyRowMsgKey("itMgmt.softwareList.emptyTableMessage");

        if (rowCount != 0) {
            List<SoftwareUsage> softwareList = reportService.getSoftwareUsage(query);
            List<DataRow> dataList = formatDataList(requestContext, softwareList, new Counter(rowStart));
            tableTemplate.setDataList(dataList);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    private static List<DataRow> formatDataList(RequestContext requestContext, List<SoftwareUsage> softwareDataset, Counter counter) throws Exception {
        List<DataRow> softwareList = new ArrayList<>();
        if (softwareDataset == null) {
            return softwareList;
        }

        AccessUser user = requestContext.getUser();
        boolean hasSoftwareAccess = Access.hasPermission(user, AppPaths.SOFTWARE_DETAIL);
        boolean hasHardwareAccess = Access.hasPermission(user, AppPaths.HARDWARE_DETAIL);
        boolean hasUserAccess = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);

        for (SoftwareUsage software : softwareDataset) {
            List<String> columns = new ArrayList<>();

            // For Software detail
            for (String column : ReportUtils.getSoftwareUsageColumns()) {
                if (column.equals(Software.ROWNUM)) {
                    columns.add(counter.incr() + ".");

                } else if (column.equals(Software.NAME)) {
                    Link link = new Link(requestContext);
                    link.setTitle(software.getName());
                    if (hasSoftwareAccess) {
                        link.setAjaxPath(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + software.getId());
                    }
                    columns.add(link.getString());

                } else if  (column.equals(Software.TYPE)) {
                    columns.add(HtmlUtils.encode(software.getTypeName()));

                } else if (column.equals(Hardware.HARDWARE_NAME)) {
                    Link link = new Link(requestContext);
                    link.setTitle(software.getHardwareName());

                    if (hasHardwareAccess) {
                        link.setAjaxPath(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + software.getHardwareId());
                    }
                    columns.add(link.getString());

                } else if (column.equals(Hardware.OWNER_NAME)) {
                    Link link = new Link(requestContext);
                    link.setTitle(software.getHardwareOwnerName());

                    if (hasUserAccess) {
                        link.setAjaxPath(AppPaths.ADMIN_USER_DETAIL + "?userId=" + software.getHardwareOwnerId());
                    }
                    columns.add(link.getString());
                }
            }
            DataRow dataRow = new DataRow();
            dataRow.setColumns(columns);
            softwareList.add(dataRow);
        }
        return softwareList;
    }
}