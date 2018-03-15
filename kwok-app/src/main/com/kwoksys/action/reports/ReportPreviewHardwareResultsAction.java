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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.hardware.HardwareSearchForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.ReportAccess;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.reports.ReportService;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.biz.reports.dao.ReportQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
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

/**
 * Action class for running a report.
 */
public class ReportPreviewHardwareResultsAction extends Action2 {

    private HardwareSearchForm hardwareSearchForm ;

    public String execute() throws Exception {
        ReportForm reportForm = getSessionBaseForm(ReportForm.class);
        hardwareSearchForm = getSessionBaseForm(HardwareSearchForm.class);
        AccessUser user = requestContext.getUser();

        // Checks report permission
        if (!ReportAccess.hasPermission(user, reportForm.getReportType())) {
            throw new AccessDeniedException();
        }

        if (reportForm.getReportType().equals(ReportUtils.HARDWARE_MEMBER_REPORT_TYPE)) {
            return hardwareMembers(request, response);
        } else if (reportForm.getReportType().equals(ReportUtils.HARDWARE_LICENSE_REPORT_TYPE)) {
            return hardwareLicenses(request, response);
        }

        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = requestContext.getParameterString("orderBy", Hardware.HARDWARE_NAME);
        String order = requestContext.getParameterString("order");
        int rowStart = requestContext.getParameter("rowStart", 0);

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getHardwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, SessionManager.HARDWARE_REPORT_CRITERIA_MAP);
        hardwareSearch.prepareMap(hardwareSearchForm);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        QueryCriteria query = new QueryCriteria(hardwareSearch);
        query.setLimit(rowLimit, rowStart);

        if (HardwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(orderBy), order);
        }

        int rowCount = hardwareService.getHardwareCount(query);
        List<DataRow> dataList = null;

        if (rowCount != 0) {
            List<Hardware> hardwareList = hardwareService.getHardwareList(query);
            dataList = HardwareUtils.formatHardwareList(requestContext, hardwareList, new Counter(rowStart), AppPaths.HARDWARE_DETAIL);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");

        //
        // Template: ReportPreviewResultsTemplate
        //
        ReportPreviewResultsTemplate preview = standardTemplate.addTemplate(new ReportPreviewResultsTemplate());
        preview.setReportType(reportForm.getReportType());

        for (String column : ConfigManager.app.getHardwareExportColumns()) {
            preview.addReportColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }
        for (Attribute attr : new AttributeManager(requestContext).getCustomFieldList(ObjectTypes.HARDWARE)) {
            preview.addReportColumnOptions(new LabelValueBean(attr.getName(), String.valueOf(attr.getId())));
        }

        for (String column : HardwareUtils.getSortableColumns()) {
            preview.addSortColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }

        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = standardTemplate.addTemplate(new RecordsNavigationTemplate());
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "itMgmt.hardwareList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportForm.getReportType() + "&rowCmd=showAll");
        nav.setPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportForm.getReportType() + "&rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(HardwareUtils.getColumnHeaderList());
        tableTemplate.setColumnPath(AppPaths.HARDWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setEmptyRowMsgKey("itMgmt.hardwareList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String hardwareMembers(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String reportType = requestContext.getParameterString("reportType");

        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = requestContext.getParameterString("orderBy", Hardware.HARDWARE_NAME);
        String order = requestContext.getParameterString("order");
        int rowStart = requestContext.getParameter("rowStart", 0);

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getHardwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, SessionManager.HARDWARE_REPORT_CRITERIA_MAP);
        hardwareSearch.prepareMap(hardwareSearchForm);

        QueryCriteria searchQuery = new QueryCriteria(hardwareSearch);

        QueryCriteria query = new QueryCriteria();
        query.setLimit(rowLimit, rowStart);

        if (HardwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(ReportQueries.getHardwareMemberOrderByColumn(orderBy), order);
        }

        ReportService reportService = ServiceProvider.getReportService(requestContext);

        int rowCount = reportService.getHardwareMembersCount(searchQuery);
        List<String> columnList = ReportUtils.getHardwareMembersExportColumns();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: ReportPreviewResultsTemplate
        //
        ReportPreviewResultsTemplate preview = standardTemplate.addTemplate(new ReportPreviewResultsTemplate());
        preview.setReportType(reportType);

        for (String column : new String[] {Hardware.ID, Hardware.HARDWARE_NAME}) {
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
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "reports.list.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportType + "&rowCmd=showAll");
        nav.setPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportType + "&rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(columnList);
        tableTemplate.setColumnPath(AppPaths.HARDWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setEmptyRowMsgKey("itMgmt.hardwareList.emptyTableMessage");

        if (rowCount != 0) {
            Counter counter = new Counter(rowStart);
            List<Map<String, String>> hardwareList = reportService.getHardwareMembers(searchQuery, query);
            List<DataRow> dataList = formatDataList(requestContext, hardwareList, columnList, counter, AppPaths.HARDWARE_MEMBER);

            tableTemplate.setDataList(dataList);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String hardwareLicenses(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String reportType = requestContext.getParameterString("reportType");

        String rowCmd = requestContext.getParameterString("rowCmd");
        //String orderBy = requestContext.getParameterString("orderBy", Hardware.HARDWARE_NAME);
        //String order = requestContext.getParameterString("order");
        int rowStart = requestContext.getParameter("rowStart", 0);

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getHardwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, SessionManager.HARDWARE_REPORT_CRITERIA_MAP);
        hardwareSearch.prepareMap(hardwareSearchForm);

        QueryCriteria query = new QueryCriteria(hardwareSearch);
        query.setLimit(rowLimit, rowStart);
        query.addSortColumn(ReportQueries.getHardwareLicenseOrderByColumn(Hardware.HARDWARE_NAME), QueryCriteria.ASCENDING);
        query.addSortColumn(ReportQueries.getHardwareLicenseOrderByColumn(Software.NAME), QueryCriteria.ASCENDING);
        query.addSortColumn(ReportQueries.getHardwareLicenseOrderByColumn(SoftwareLicense.LICENSE_KEY), QueryCriteria.ASCENDING);

        ReportService reportService = ServiceProvider.getReportService(requestContext);

        int rowCount = reportService.getHardwareLicenseCount(query);
        List<String> columnList = ReportUtils.getHardwareLicenseColumns();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: ReportPreviewResultsTemplate
        //
        ReportPreviewResultsTemplate preview = standardTemplate.addTemplate(new ReportPreviewResultsTemplate());
        preview.setReportType(reportType);

        for (String column : new String[] {Hardware.ID, Hardware.HARDWARE_NAME, Software.NAME, SoftwareLicense.LICENSE_KEY}) {
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
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "reports.list.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportType + "&rowCmd=showAll");
        nav.setPath(AppPaths.REPORTS_HARDWARE_SEARCH + "?reportType=" + reportType + "&rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(columnList);
        tableTemplate.setColumnPath(AppPaths.HARDWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setEmptyRowMsgKey("itMgmt.hardwareList.emptyTableMessage");

        if (rowCount != 0) {
            List<Map<String, String>> hardwareList = reportService.getHardwareLicenses(query);
            List<DataRow> dataList = formatDataList(requestContext, hardwareList, columnList, new Counter(rowStart), AppPaths.HARDWARE_DETAIL);
            tableTemplate.setDataList(dataList);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    private List<DataRow> formatDataList(RequestContext requestContext, List<Map<String, String>> dataset, List<String> columnList, Counter counter, String hardwarePath) throws Exception {
        List<DataRow> formattedDatalist = new ArrayList<>();
        if (dataset == null) {
            return formattedDatalist;
        }

        AccessUser user = requestContext.getUser();
        boolean hasHardwareAccess = Access.hasPermission(user, hardwarePath);

        for (Map<String, String> record : dataset) {
            List<String> columns = new ArrayList<>();

            for (String column : columnList) {
                if (column.equals(Software.ROWNUM)) {
                    columns.add(counter.incr() + ".");

                } else if (column.equals(Hardware.HARDWARE_NAME)) {
                    Link link = new Link(requestContext);
                    link.setTitle(record.get("hardware_name"));
                    if (hasHardwareAccess) {
                        link.setAjaxPath(hardwarePath + "?hardwareId=" + record.get("hardware_id"));
                    }
                    columns.add(link.getString());

                } else if (column.equals("hardware_member_name")) {
                    Link link = new Link(requestContext);
                    link.setTitle(record.get("hardware_member_name"));
                    if (hasHardwareAccess) {
                        link.setAjaxPath(hardwarePath + "?hardwareId=" + record.get("hardware_member_id"));
                    }
                    columns.add(link.getString());

                } else if (column.equals(Software.NAME)) {
                    String value = record.get("software_name");
                    columns.add(value == null ? "" : HtmlUtils.encode(value));

                } else if (column.equals(SoftwareLicense.LICENSE_KEY)) {
                    String value = record.get("license_key");
                    columns.add(value == null ? "" : HtmlUtils.encode(value));
                }
            }
            DataRow dataRow = new DataRow();
            dataRow.setColumns(columns);
            formattedDatalist.add(dataRow);
        }
        return formattedDatalist;
    }
}