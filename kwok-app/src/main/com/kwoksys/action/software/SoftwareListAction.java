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
package com.kwoksys.action.software;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.software.SoftwareSearch;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action class for displaying software list.
 */
public class SoftwareListAction extends Action2 {

    public String execute() throws Exception {
        SoftwareSearchForm actionForm = getSessionBaseForm(SoftwareSearchForm.class);

        AccessUser user = requestContext.getUser();

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.SOFTWARE_ORDER_BY, Software.NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.SOFTWARE_ORDER, QueryCriteria.ASCENDING);

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll")) {
            request.getSession().setAttribute(SessionManager.SOFTWARE_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.SOFTWARE_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getSoftwareRowsToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        // Getting search criteria map from session variable.
        SoftwareSearch softwareSearch = new SoftwareSearch(requestContext, SessionManager.SOFTWARE_SEARCH_CRITERIA_MAP);
        softwareSearch.prepareMap(actionForm);

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria(softwareSearch);
        query.setLimit(rowLimit, rowStart);

        if (SoftwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(SoftwareQueries.getOrderByColumn(orderBy), order);
        }

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        int rowCount = softwareService.getSoftwareCount(query);

        List<LabelValueBean> manufacturerOptions = new ArrayList<>();
        manufacturerOptions.add(new SelectOneLabelValueBean(requestContext));

        QueryCriteria filterQuery = new QueryCriteria();
        filterQuery.addSortColumn(SoftwareQueries.getOrderByColumn(Software.MANUFACTURER));

        for (Map software : softwareService.getSoftwareCountGroupByCompany(filterQuery)) {
            // Make this a link.
            Object softwareCount = software.get("software_count");
            Object manufacturerId = software.get("manufacturer_company_id");
            Object manufacturerName = software.get("software_manufacturer");

            if (manufacturerName != null) {
                manufacturerOptions.add(new LabelValueBean(manufacturerName + " (" + softwareCount + ")", manufacturerId.toString()));
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("manufacturerIdOptions", manufacturerOptions);
        standardTemplate.setPathAttribute("formAction", AppPaths.SOFTWARE_LIST);
        if (!softwareSearch.getSearchCriteriaMap().isEmpty()) {
            request.setAttribute("searchResultText", Localizer.getText(requestContext, "itMgmt.softwareList.searchResult"));
        }

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(SoftwareUtils.getColumnHeaderList());
        tableTemplate.setSortableColumnHeaders(SoftwareUtils.getSortableColumns());
        tableTemplate.setColumnPath(AppPaths.SOFTWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("itMgmt.softwareList.emptyTableMessage");

        if (rowCount != 0) {
            List<Software> softwareList = softwareService.getSoftwareList(query);
            List<DataRow> dataList = SoftwareUtils.formatSoftwareList(requestContext, softwareList, new Counter(rowStart));
            tableTemplate.setDataList(dataList);
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
        nav.setShowAllRecordsPath(AppPaths.SOFTWARE_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.SOFTWARE_LIST + "?rowStart=");

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.softwareList.title");
        header.setTitleClassNoLine();

        if (user.hasPermission(AppPaths.SOFTWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_ADD);
            link.setTitleKey("itMgmt.cmd.softwareAdd");
            header.addHeaderCmds(link);
        }

        // Link to Software export.
        if (user.hasPermission(AppPaths.SOFTWARE_LIST_EXPORT)) {
            Link link = new Link(requestContext);
            link.setExportPath(AppPaths.SOFTWARE_LIST_EXPORT);
            link.setTitleKey("itMgmt.cmd.softwareListExport");
            link.setImgSrc(Image.getInstance().getCsvFileIcon());
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
