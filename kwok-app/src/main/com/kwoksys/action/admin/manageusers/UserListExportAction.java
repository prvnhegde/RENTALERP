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
package com.kwoksys.action.admin.manageusers;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.reports.writers.CsvReportWriter;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action class for exporting user list.
 */
public class UserListExportAction extends Action2 {

    public String execute() throws Exception {
        String rowCmd = requestContext.getParameterString("rowCmd");
        int rowStart = requestContext.getParameter("rowStart", 0);
        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getUserRows());

        // Get session variables
        HttpSession session = request.getSession();
        
        ResponseContext responseContext = new ResponseContext(response);
        responseContext.setAttachementName(Localizer.getText(requestContext, "admin.userListExport.filename"));

        CsvReportWriter csvReportWriter = new CsvReportWriter();
        csvReportWriter.init(responseContext);

        String[] columnsKeys = {AccessUser.USERNAME, AccessUser.FIRST_NAME, AccessUser.LAST_NAME, AccessUser.DISPLAY_NAME, AccessUser.EMAIL,
                AccessUser.STATUS, AccessUser.LAST_LOGON, AccessUser.LAST_VISIT};

        // This is for column header.
        List<String> columnHeaders = new ArrayList<>();
        for (String column : columnsKeys) {
            columnHeaders.add(Localizer.getText(requestContext, "common.column." + column));
        }

        csvReportWriter.addHeaderRow(columnHeaders);

        // Getting search criteria map from session variable.
        UserSearch userSearch = new UserSearch();
        if (session.getAttribute(SessionManager.USER_SEARCH_CRITERIA_MAP) != null) {
            userSearch.setSearchCriteriaMap((Map) session.getAttribute(SessionManager.USER_SEARCH_CRITERIA_MAP));
        }

        // Get session variables
        String orderBy = SessionManager.getAttribute(request, SessionManager.USERS_ORDER_BY, AccessUser.USERNAME);
        String order = SessionManager.getAttribute(request, SessionManager.USERS_ORDER, QueryCriteria.ASCENDING);

        // Ready to pass variables to query.
        QueryCriteria queryCriteria = new QueryCriteria(userSearch);

        if (AdminUtils.isSortableUserColumn(orderBy)) {
            queryCriteria.addSortColumn(AdminQueries.getOrderByColumn(orderBy), order);
        }
        if (!rowCmd.equals("showAll")) {
            queryCriteria.setLimit(rowLimit, rowStart);
        }

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Loop through the user list
        for (AccessUser user : adminService.getExtendedUsers(queryCriteria)) {
            List<String> columns = new ArrayList<>();
            for (String column : columnsKeys) {
                if (column.equals(AccessUser.USERNAME)) {
                    columns.add(user.getUsername());

                } else if (column.equals(AccessUser.FIRST_NAME)) {
                    columns.add(user.getFirstName());

                } else if (column.equals(AccessUser.LAST_NAME)) {
                    columns.add(user.getLastName());

                } else if (column.equals(AccessUser.DISPLAY_NAME)) {
                    columns.add(user.getDisplayName());

                } else if (column.equals(AccessUser.EMAIL)) {
                    columns.add(user.getEmail());

                } else if (column.equals(AccessUser.STATUS)) {
                    columns.add(attributeManager.getAttrFieldNameCache(Attributes.USER_STATUS_TYPE, user.getStatus()));

                } else if (column.equals(AccessUser.LAST_LOGON)) {
                    columns.add(user.getLastLogonTime());

                } else if (column.equals(AccessUser.LAST_VISIT)) {
                    columns.add(user.getFormattedLastVisitTime());
                }
            }
            csvReportWriter.addRow(columns);
        }

        return csvReportWriter.close();
    }
}
