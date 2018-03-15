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
import com.kwoksys.action.common.template.ThisTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminTabs;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.GroupPermissionMap;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.StringUtils;

/**
 * Action class for editing group access
 */
public class GroupAccessEditAction extends Action2 {

    public String edit() throws Exception {
        GroupAccessForm actionForm = getBaseForm(GroupAccessForm.class);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessGroup group = adminService.getGroup(actionForm.getGroupId());

        // Do some sorting.
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(AccessGroup.ORDER_NUM);

        List<Map> accessList = new ArrayList<>();

        // For javascript select all
        List<Integer> permissions = new ArrayList<>();

        for (GroupPermissionMap groupPerm : adminService.getGroupAccess(queryCriteria, actionForm.getGroupId())) {
            Map<String, Object> accessMap = new HashMap<>();
            accessMap.put("accessText", AdminUtils.getPermissionLabel(requestContext, groupPerm.getPermName()));
            String accessName = "formAccess_" + groupPerm.getPermId();
            // This would determine which button is checked.
            accessMap.put(accessName, groupPerm.isHasPermission() ? 1 : 0);
            // Name of the radio button.
            accessMap.put("accessName", accessName);
            accessList.add(accessMap);

            permissions.add(groupPerm.getPermId());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("accessList", accessList);
        standardTemplate.setAttribute("permissionList", StringUtils.joinIntegers(permissions, ","));
        standardTemplate.setAttribute("accessOptions", AdminUtils.getUserAccessOptionList());
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_GROUP_ACCESS_EDIT_2 + "?groupId=" + actionForm.getGroupId());
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_GROUP_ACCESS + "?groupId=" + actionForm.getGroupId()).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.groupDetail.header");

        //
        // Template: GroupDetailTemplate
        //
        standardTemplate.addTemplate(new GroupDetailTemplate(group));

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(AdminTabs.createGroupTabs(requestContext, actionForm.getGroupId()));
        tabs.setTabActive(AdminTabs.GROUP_ACCESS_TAB);

        //
        // Template: ThisTemplate
        //
        standardTemplate.addTemplate(new ThisTemplate());

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String edit2() throws Exception {
        GroupAccessForm actionForm = saveActionForm(new GroupAccessForm());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessGroup group = adminService.getGroup(actionForm.getGroupId());

        ActionMessages errors = new ActionMessages();

        for (GroupPermissionMap groupperm : adminService.getGroupAccess(new QueryCriteria(), group.getId())) {
            groupperm.setCmd(requestContext.getParameter("formAccess_" + groupperm.getPermId()));
            errors = adminService.updateGroupAccess(groupperm);
        }

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_GROUP_ACCESS_EDIT + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&groupId=" + actionForm.getGroupId());

        } else {
            new CacheManager(requestContext).removeGroupPermissionsCache(actionForm.getGroupId());

            return ajaxUpdateView(AppPaths.ADMIN_GROUP_ACCESS + "?groupId=" + actionForm.getGroupId());
        }
    }
}
