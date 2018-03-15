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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting group.
 */
public class GroupDeleteAction extends Action2 {

    public String delete() throws Exception {
        Integer groupId = requestContext.getParameter("groupId");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessGroup accessGroup = adminService.getGroup(groupId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.groupDetail.header");

        //
        // Template: GroupDetailTemplate
        //
        standardTemplate.addTemplate(new GroupDetailTemplate(accessGroup));

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate deleteTemplate = standardTemplate.addTemplate(new ObjectDeleteTemplate());
        deleteTemplate.setFormAjaxAction(AppPaths.ADMIN_GROUP_DELETE_2 + "?groupId=" + accessGroup.getId());
        deleteTemplate.setFormCancelAction(AppPaths.ADMIN_GROUP_DETAIL + "?groupId=" + accessGroup.getId());
        deleteTemplate.setConfirmationMsgKey("admin.groupDelete.confirm");
        deleteTemplate.setSubmitButtonKey("admin.groupDelete.buttonSubmit");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String delete2() throws Exception {
        Integer groupId = requestContext.getParameter("groupId");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Check to make sure the object exists
        AccessGroup accessGroup = adminService.getGroup(groupId);

        ActionMessages errors = adminService.deleteGroup(accessGroup);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_GROUP_DELETE + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&groupId=" + groupId);

        } else {
            return ajaxUpdateView(AppPaths.ADMIN_GROUP_LIST);
        }
    }
}
