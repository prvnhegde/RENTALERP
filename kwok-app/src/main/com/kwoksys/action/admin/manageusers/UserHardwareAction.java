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

import java.util.ArrayList;
import java.util.List;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminTabs;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for displaying user hardware.
 */
public class UserHardwareAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        Integer reqUserId = requestContext.getParameter("userId");

        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.HARDWARE_ORDER, QueryCriteria.ASCENDING);
        
        AccessUser requestUser = new CacheManager(requestContext).getUserCacheValidate(reqUserId);

        HardwareSearch hardwareSearch = new HardwareSearch();
        hardwareSearch.put("hardwareOwnerId", requestUser.getId());

        // Do some sorting.
        QueryCriteria queryCriteria = new QueryCriteria(hardwareSearch);

        if (HardwareUtils.isSortableColumn(orderBy)) {
            queryCriteria.addSortColumn(HardwareQueries.getOrderByColumn(orderBy), order);
        }
        
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        List<Hardware> hardwareList = hardwareService.getHardwareList(queryCriteria);

        List<String> columnHeaders = new ArrayList<>(HardwareUtils.getColumnHeaderList());
        columnHeaders.remove(Hardware.OWNER_NAME);
        List<DataRow> dataList = HardwareUtils.formatHardwareList(requestContext, hardwareList, columnHeaders, new Counter(), AppPaths.HARDWARE_DETAIL);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");

        //
        // Template: UserSpecTemplate
        //
        standardTemplate.addTemplate(new UserSpecTemplate(requestUser));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(AdminTabs.createUserTabs(requestContext, requestUser));
        tabs.setTabActive(AdminTabs.USER_HARDWARE_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(HardwareUtils.getSortableColumns());
        tableTemplate.setColumnPath(AppPaths.ADMIN_USER_HARDWARE + "?userId=" + reqUserId);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("itMgmt.hardwareList.emptyTableMessage");

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        if (Access.hasPermission(user, AppPaths.ADMIN_USER_INDEX)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_INDEX);
            link.setTitleKey("admin.userIndex.userSearchTitle");
            header.addNavLink(link);
        }

        if (Access.hasPermission(user, AppPaths.ADMIN_USER_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_LIST);
            link.setTitleKey("admin.userList.title");
            header.addNavLink(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}