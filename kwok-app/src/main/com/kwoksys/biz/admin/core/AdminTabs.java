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
package com.kwoksys.biz.admin.core;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * AdminTabs
 */
public class AdminTabs {

    public static final String GROUP_ACCESS_TAB = "accessTab";

    public static final String GROUP_MEMBERS_TAB = "membersTab";

    public static final String USER_ACCESS_TAB = "accessTab";

    public static final String USER_CONACT_TAB = "contactTab";

    public static final String USER_HARDWARE_TAB = "hardwareTab";

    public static List<Link> createGroupTabs(RequestContext requestContext, Integer groupId) throws DatabaseException {
        AccessUser user = requestContext.getUser();

        List<Link> links = new ArrayList<>();

        // Link to User Members view.
        if (Access.hasPermission(user, AppPaths.ADMIN_GROUP_DETAIL)) {
            links.add(new Link(requestContext).setName(GROUP_MEMBERS_TAB)
                    .setAjaxPath(AppPaths.ADMIN_GROUP_DETAIL + "?groupId=" + groupId)
                    .setTitleKey("admin.groupDetail.membersTab"));
        }

        // Link to Group Access view.
        if (Access.hasPermission(user, AppPaths.ADMIN_GROUP_ACCESS)) {
            links.add(new Link(requestContext).setName(GROUP_ACCESS_TAB)
                    .setAjaxPath(AppPaths.ADMIN_GROUP_ACCESS + "?groupId=" + groupId)
                    .setTitleKey("admin.groupDetail.accessTab"));
        }
        return links;
    }

    public static List<Link> createUserTabs(RequestContext requestContext, AccessUser appUser) throws DatabaseException {
        AccessUser user = requestContext.getUser();

        List<Link> links = new ArrayList<>();

        // Link to User Contact view.
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL)) {
            links.add(new Link(requestContext).setName(USER_CONACT_TAB)
                    .setAjaxPath(AppPaths.ADMIN_USER_DETAIL + "?userId=" + appUser.getId())
                    .setTitleKey("admin.user.tab.contact"));
        }

        // Link to User Access view.
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_ACCESS)) {
            links.add(new Link(requestContext).setName(USER_ACCESS_TAB)
                    .setAjaxPath(AppPaths.ADMIN_USER_ACCESS + "?userId=" + appUser.getId())
                    .setTitleKey("admin.cmd.userAccess"));
        }

        // Link to Assigned Hardware tab.
        if (Access.hasPermission(user, AppPaths.ADMIN_USER_HARDWARE)) {
            links.add(new Link(requestContext).setName(USER_HARDWARE_TAB)
                    .setAjaxPath(AppPaths.ADMIN_USER_HARDWARE + "?userId=" + appUser.getId())
                    .setTitle(Localizer.getText(requestContext, "admin.user.tab.hardware", new Object[]{appUser.getHardwareCount()})));
        }
        return links;
    }
}
