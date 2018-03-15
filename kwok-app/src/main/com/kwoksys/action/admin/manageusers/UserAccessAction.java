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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminTabs;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.UserPermissionMap;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.StringUtils;

/**
 * Action class for getting user access info.
 */
public class UserAccessAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        Integer reqUserId = requestContext.getParameter("userId");
        String cmd = requestContext.getParameterString("cmd");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessUser requestUser = new CacheManager(requestContext).getUserCacheValidate(reqUserId);

        if (!cmd.equals("edit")) {
            cmd = "detail";
        }

        // Do some sorting.
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(AdminQueries.getOrderByColumn("order_num"));

        List<Map> accessList = new ArrayList<>();

        // For javascript select all
        List<Integer> permissions = new ArrayList<>();

        for (UserPermissionMap userperm : adminService.getUserAccess(queryCriteria, reqUserId)) {
            Map<String, Object> accessMap = new HashMap<>();
            accessMap.put("accessText", AdminUtils.getPermissionLabel(requestContext, userperm.getPermName()));

            if (cmd.equals("edit")) {
                String accessName = "formAccess_" + userperm.getPermId();
                // This would determine which button is checked.
                accessMap.put(accessName, userperm.isHasPermission() ? 1 : 0);
                // Name of the radio button.
                accessMap.put("accessName", accessName);
                accessMap.put("accessOptions", AdminUtils.getUserAccessOptionList());

                permissions.add(userperm.getPermId());

            } else {
                accessMap.put("accessValue", AdminUtils.getUserAccessIcon(userperm.isHasPermission()));
            }
            accessList.add(accessMap);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("cmd", cmd);
        standardTemplate.setAttribute("accessList", accessList);
        standardTemplate.setAttribute("permissionList", StringUtils.joinIntegers(permissions, ","));
        standardTemplate.setAttribute("accessOptions", AdminUtils.getUserAccessOptionList());
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_USER_ACCESS_EDIT_2 + "?userId=" + reqUserId);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_USER_ACCESS + "?userId=" + reqUserId).getString());
        
        boolean formDisabled = AdminUtils.disableAccessEdit(requestUser);
        standardTemplate.setAttribute("formDisabled", formDisabled);
        standardTemplate.setAttribute("formButtonDisabled", formDisabled ? "disabled" : "");

        //
        // Template: UserSpecTemplate
        //
        standardTemplate.addTemplate(new UserSpecTemplate(requestUser));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(AdminTabs.createUserTabs(requestContext, requestUser));
        tabs.setTabActive(AdminTabs.USER_ACCESS_TAB);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();

        if (Access.hasPermission(user, AppPaths.ADMIN_USER_ACCESS_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_ACCESS + "?userId=" + reqUserId + "&cmd=edit");
            link.setTitleKey("admin.cmd.userAccessEdit");
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

        if (Access.hasPermission(user, AppPaths.ADMIN_USER_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_USER_LIST);
            link.setTitleKey("admin.userList.title");
            header.addNavLink(link);
        }

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        Integer reqUserId = requestContext.getParameter("userId");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessUser requestUser = adminService.getUser(reqUserId);

        ActionMessages errors = new ActionMessages();

        for (UserPermissionMap userperm : adminService.getUserAccess(new QueryCriteria(), requestUser.getId())) {
            userperm.setCmd(requestContext.getParameter("formAccess_" + userperm.getPermId()));
            errors = adminService.updateUserAccess(userperm);
        }

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_USER_ACCESS + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&userId=" + reqUserId + "&cmd=edit");
            
        } else {
            new CacheManager(requestContext).removeUserPermissionsCache(reqUserId);

            return ajaxUpdateView(AppPaths.ADMIN_USER_ACCESS + "?userId=" + reqUserId);
        }
    }
}
