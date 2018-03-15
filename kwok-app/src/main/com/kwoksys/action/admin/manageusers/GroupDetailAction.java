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
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminTabs;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for showing group detail
 */
public class GroupDetailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        Integer groupId = requestContext.getParameter("groupId");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessGroup group = adminService.getGroup(groupId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.groupDetail.header");

        if (user.hasPermission(AppPaths.ADMIN_GROUP_EDIT)) {
            header.addHeaderCmds(new Link(requestContext).setTitleKey("admin.groupEdit.title")
                    .setAjaxPath(AppPaths.ADMIN_GROUP_EDIT + "?groupId=" + group.getId())
                    .setImgSrc(Image.getInstance().getGroupEditIcon()));
        }

        if (user.hasPermission(AppPaths.ADMIN_GROUP_DELETE)) {
            header.addHeaderCmds(new Link(requestContext).setTitleKey("admin.groupDelete.title")
                    .setAjaxPath(AppPaths.ADMIN_GROUP_DELETE + "?groupId=" + group.getId())
                    .setImgSrc(Image.getInstance().getGroupDeleteIcon()));
        }

        header.addNavLink(Links.getAdminHomeLink(requestContext));

        if (user.hasPermission(AppPaths.ADMIN_GROUP_LIST)) {
            header.addNavLink(new Link(requestContext).setTitleKey("admin.index.groupList")
                    .setAjaxPath(AppPaths.ADMIN_GROUP_LIST));
        }

        //
        // Template: GroupDetailTemplate
        //
        standardTemplate.addTemplate(new GroupDetailTemplate(group));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(AdminTabs.createGroupTabs(requestContext, groupId));
        tabs.setTabActive(AdminTabs.GROUP_MEMBERS_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(AccessGroup.getGroupMembersColumnHeader());
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setStyle(TableTemplate.STYLE_TAB);
        tableTemplate.setEmptyRowMsgKey("admin.groupMembers.emptyTableMessage");
        
        List<AccessUser> users = adminService.getGroupMembers(groupId);
        if (!users.isEmpty()) {
            for (AccessUser requestUser : users) {
                List<String> columns = new ArrayList<>();
                columns.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_DETAIL + "?userId="
                                        + requestUser.getId()).setTitle(AdminUtils.getSystemUsername(requestContext, requestUser)).getString());

                tableTemplate.addRow(columns);
            }
        }

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
}
