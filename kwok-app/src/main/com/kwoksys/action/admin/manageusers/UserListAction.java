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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing user list.
 */
public class UserListAction extends Action2 {

    public String execute() throws Exception {
        UserSearchForm actionForm = getSessionBaseForm(UserSearchForm.class);
        AccessUser user = requestContext.getUser();

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.USERS_ORDER_BY, AccessUser.USERNAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.USERS_ORDER, QueryCriteria.ASCENDING);

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll")) {
            request.getSession().setAttribute(SessionManager.USERS_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.USERS_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getUserRows());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        // Get session variables
        HttpSession session = request.getSession();

        // Getting search criteria map from session variable.
        UserSearch userSearch = new UserSearch();

        if (!cmd.isEmpty()) {
            if (cmd.equals("search")) {
                // We are expecting user to enter some search criteria.
                userSearch.prepareMap(actionForm, requestContext);

            } else if (cmd.equals("showEnabled")) {
                userSearch.put(UserSearch.USER_STATUS, AttributeFieldIds.USER_STATUS_ENABLED);

            } else if (cmd.equals("showDisabled")) {
                userSearch.put(UserSearch.USER_STATUS, AttributeFieldIds.USER_STATUS_DISABLED);

            } else if (cmd.equals("showLoggedIn")) {
                userSearch.put(UserSearch.LOGGED_IN_USERS, "");
            }

            // Store search criteria in session.
            userSearch.put("cmd", cmd);
            session.setAttribute(SessionManager.USER_SEARCH_CRITERIA_MAP, userSearch.getSearchCriteriaMap());

        } else if (session.getAttribute(SessionManager.USER_SEARCH_CRITERIA_MAP) != null) {
            userSearch.setSearchCriteriaMap((Map) session.getAttribute(SessionManager.USER_SEARCH_CRITERIA_MAP));
        }

        // Pass variables to query.
        QueryCriteria queryCriteria = new QueryCriteria(userSearch);
        queryCriteria.setLimit(rowLimit, rowStart);

        if (AdminUtils.isSortableUserColumn(orderBy)) {
            queryCriteria.addSortColumn(AdminQueries.getOrderByColumn(orderBy), order);
        }

        // Get column headers
        List<String> columnHeaders = AdminUtils.getUserColumnHeaders();

        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Selectbox for filtering users.
        List<LabelValueBean> filterOptions = new ArrayList<>();
        filterOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.users.all"), "showAll"));
        filterOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.users.enabled"), "showEnabled"));
        filterOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.users.disabled"), "showDisabled"));
        filterOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.users.loggedIn"), "showLoggedIn"));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_USER_LIST);
        standardTemplate.setAttribute("filterOptions", filterOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.userList.title");
        header.setTitleClassNoLine();

        // Add User
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_ADD);
            link.setTitleKey("admin.userAdd.title");
            link.setImgSrc(Image.getInstance().getUserAddIcon());
            header.addHeaderCmds(link);
        }

        // Link to User export
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_LIST_EXPORT)) {
            Link link = new Link(requestContext);
            link.setExportPath(AppPaths.ADMIN_USER_LIST_EXPORT + "?rowCmd=" + rowCmd +
                    "&rowStart=" + rowStart + "&rowLimit" + rowLimit);
            link.setTitleKey("admin.cmd.userListExport");
            link.setImgSrc(Image.getInstance().getCsvFileIcon());
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        if (Access.hasPermission(user, AppPaths.ADMIN_USER_INDEX)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_INDEX);
            link.setTitleKey("admin.userIndex.userSearchTitle");
            header.addNavLink(link);
        }

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(AdminUtils.getSortableUserColumns());
        tableTemplate.setColumnPath(AppPaths.ADMIN_USER_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("admin.userList.emptyTableMessage");
        
        // Get a list of users
        List<AccessUser> userDataset = adminService.getUsers(queryCriteria);

        if (!userDataset.isEmpty()) {
            Counter counter = new Counter(rowStart);

            for (AccessUser appUser : userDataset) {
                List<String> columns = new ArrayList<>();

                for (String column : columnHeaders) {
                    if (column.equals(AccessUser.ROWNUM)) {
                        columns.add(counter.incr() + ".");

                    } else if (column.equals(AccessUser.USERNAME)) {
                        columns.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_DETAIL
                                + "?userId=" + appUser.getId()).setTitle(appUser.getUsername()).getString());

                    } else if (column.equals(AccessUser.STATUS)) {
                        columns.add(attributeManager.getAttrFieldNameCache(Attributes.USER_STATUS_TYPE, appUser.getStatus()));

                    } else if (column.equals(AccessUser.FIRST_NAME)) {
                        columns.add(HtmlUtils.encode(appUser.getFirstName()));

                    } else if (column.equals(AccessUser.LAST_NAME)) {
                        columns.add(HtmlUtils.encode(appUser.getLastName()));

                    } else if (column.equals(AccessUser.DISPLAY_NAME)) {
                        columns.add(HtmlUtils.encode(appUser.getDisplayName()));

                    } else if (column.equals(AccessUser.EMAIL)) {
                        columns.add(HtmlUtils.encode(appUser.getEmail()));
                    }
                }
                tableTemplate.addRow(columns);
            }
        }

        //
        // Template: RecordsNavigationTemplate
        //
        int rowCount = adminService.getUserCount(queryCriteria);
        Object[] args = {rowCount};

        RecordsNavigationTemplate nav = standardTemplate.addTemplate(new RecordsNavigationTemplate());
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "admin.userList.rowCount", args));
        nav.setShowAllRecordsPath(AppPaths.ADMIN_USER_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.ADMIN_USER_LIST + "?rowStart=");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}