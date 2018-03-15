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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for resetting user password.
 */
public class UserPasswordResetAction extends Action2 {

    public String reset() throws Exception {
        UserPasswordForm actionForm = getBaseForm(UserPasswordForm.class);
        actionForm.setPassword(null);
        actionForm.setPasswordConfirm(null);

        AccessUser requestUser = new CacheManager(requestContext).getUserCacheValidate(actionForm.getUserId());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_USER_PASSWORD_RESET_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_USER_DETAIL + "?userId=" + actionForm.getUserId()).getString());
        request.setAttribute("allowBlankPassword", ConfigManager.admin.isAllowBlankUserPassword());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.userPasswordReset.title");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("admin.userPasswordReset.sectionHeader", new String[]{HtmlUtils.encode(requestUser.getDisplayName())});

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String reset2() throws Exception {
        UserPasswordForm actionForm = saveActionForm(new UserPasswordForm());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessUser user = adminService.getUser(actionForm.getUserId());

        user.setPasswordNew(actionForm.getPassword());
        user.setPasswordConfirm(actionForm.getPasswordConfirm());

        ActionMessages errors = adminService.resetUserPassword(user);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_USER_PASSWORD_RESET + "?userId=" + actionForm.getUserId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
            
        } else {
            return ajaxUpdateView(AppPaths.ADMIN_USER_DETAIL + "?userId=" + actionForm.getUserId());
        }
    }
}
