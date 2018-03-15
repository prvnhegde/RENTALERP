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
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing group list.
 */
public class GroupListAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(AccessGroup.GROUP_NAME);

        // Get a list of users
        List<AccessGroup> groupDataset = adminService.getGroups(queryCriteria);

        List<Map> groups = new ArrayList<>();

        for (AccessGroup group : groupDataset) {
            Map<String, String> map = new HashMap<>();
            map.put("groupName", new Link(requestContext).setAjaxPath(AppPaths.ADMIN_GROUP_DETAIL
                    + "?groupId=" + group.getId()).setTitle(group.getName()).getString());
            groups.add(map);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        request.setAttribute("groups", groups);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.groupList.title");
        
        // Link to User export
        if (Access.hasPermission(user, AppPaths.ADMIN_GROUP_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_GROUP_ADD);
            link.setTitleKey("admin.groupAdd.title");
            link.setImgSrc(Image.getInstance().getGroupAddIcon());
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.index.groupList"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
