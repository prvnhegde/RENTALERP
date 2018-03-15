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

import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.software.SoftwareSearchForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.auth.core.ReportAccess;
import com.kwoksys.biz.software.SoftwareSearch;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for running a report.
 */
public class ReportPreviewSoftwareResultsAction extends Action2 {

    public String execute() throws Exception {
        SoftwareSearchForm actionForm = getSessionBaseForm(SoftwareSearchForm.class);

        AccessUser user = requestContext.getUser();
        String reportType = requestContext.getParameterString("reportType");

        // Checks report permission
        if (!ReportAccess.hasPermission(user, reportType)) {
            throw new AccessDeniedException();
        }

        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = requestContext.getParameterString("orderBy", Software.NAME);
        String order = requestContext.getParameterString("order");
        int rowStart = requestContext.getParameter("rowStart", 0);

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getSoftwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }
        SoftwareSearch softwareSearch = new SoftwareSearch(requestContext, SessionManager.SOFTWARE_REPORT_CRITERIA_MAP);
        softwareSearch.prepareMap(actionForm);

        QueryCriteria query = new QueryCriteria(softwareSearch);
        query.setLimit(rowLimit, rowStart);

        if (SoftwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(SoftwareQueries.getOrderByColumn(orderBy), order);
        }

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        int rowCount = softwareService.getSoftwareCount(query);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: ReportPreviewResultsTemplate
        //
        ReportPreviewResultsTemplate preview = standardTemplate.addTemplate(new ReportPreviewResultsTemplate());
        preview.setReportType(reportType);
        for (String column : ConfigManager.app.getSoftwareExportColumns()) {
            preview.addReportColumnOptions(new LabelValueBean(Localizer.getText(requestContext, "common.column." + column), column));
        }
        for (Attribute attr : new AttributeManager(requestContext).getCustomFieldList(ObjectTypes.SOFTWARE)) {
            preview.addReportColumnOptions(new LabelValueBean(attr.getName(), String.valueOf(attr.getId())));
        }

        for (String column : SoftwareUtils.getSortableColumns()) {
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
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "itMgmt.softwareList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.REPORTS_SOFTWARE_SEARCH + "?reportType=" + reportType + "&rowCmd=showAll");
        nav.setPath(AppPaths.REPORTS_SOFTWARE_SEARCH + "?reportType=" + reportType + "&rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(SoftwareUtils.getColumnHeaderList());
        tableTemplate.setColumnPath(AppPaths.SOFTWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setEmptyRowMsgKey("itMgmt.softwareList.emptyTableMessage");

        if (rowCount != 0) {
            List<Software> softwareList = softwareService.getSoftwareList(query);
            List<DataRow> dataList = SoftwareUtils.formatSoftwareList(requestContext, softwareList, new Counter(rowStart));
            tableTemplate.setDataList(dataList);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}