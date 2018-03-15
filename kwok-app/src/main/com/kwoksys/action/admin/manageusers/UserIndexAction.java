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
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for user index page.
 */
public class UserIndexAction extends Action2 {

    public String execute() throws Exception {
        getSessionBaseForm(UserSearchForm.class);

        AccessUser user = requestContext.getUser();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        List<String> links = new ArrayList<>();
        links.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_LIST + "?cmd=showAll").setTitleKey("admin.config.users.all").getString());
        links.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_LIST + "?cmd=showEnabled").setTitleKey("admin.config.users.enabled").getString());
        links.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_LIST + "?cmd=showDisabled").setTitleKey("admin.config.users.disabled").getString());
        links.add(new Link(requestContext).setAjaxPath(AppPaths.ADMIN_USER_LIST + "?cmd=showLoggedIn").setTitleKey("admin.config.users.loggedIn").getString());

        // User status selectbox
        List<SelectOneLabelValueBean> statusOptions = new ArrayList<>();
        statusOptions.add(new SelectOneLabelValueBean(requestContext));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formUserSearchAction", AppPaths.ADMIN_USER_LIST);
        standardTemplate.setAttribute("statusOptions", new AttributeManager(requestContext).setOptional(true)
                .getAttrFieldOptionsCache(Attributes.USER_STATUS_TYPE));
        standardTemplate.setAttribute("links", links);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.users");
        header.setSectionKey("admin.userIndex.numRecords", new Object[]{adminService.getUserCount(new QueryCriteria())});
        header.setTitleClassNoLine();
        
        // Add User
        if (user.hasPermission(AppPaths.ADMIN_USER_ADD)) {
            header.addHeaderCmds(new Link(requestContext).setTitleKey("admin.userAdd.title")
                    .setAjaxPath(AppPaths.ADMIN_USER_ADD).setImgSrc(Image.getInstance().getUserAddIcon()));
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.userIndex.userSearchTitle"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}