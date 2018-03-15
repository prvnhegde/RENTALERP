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
package com.kwoksys.action.userpreference;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for user preference index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getOptionalContact(user.getContactId());

        List<Link> linkList = new ArrayList<>();

        /**
         *  Link to Change password page.
         */
        if (user.hasPermission(AppPaths.USER_PREF_PASSWORD_EDIT) && adminService.allowPasswordUpdate()) {
            linkList.add(new Link(requestContext).setAjaxPath(AppPaths.USER_PREF_PASSWORD_EDIT)
                    .setTitleKey("userPref.passwordEdit.title"));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("linkList", linkList);
        if (user.hasPermission(AppPaths.USER_PREF_CONTACT_EDIT)) {
            standardTemplate.setAttribute("prefEditPath", new Link(requestContext)
                    .setAjaxPath(AppPaths.USER_PREF_CONTACT_EDIT).setTitleKey("common.command.Edit").getString());
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("userPref.index.header");

        //
        // Template: DetailTableTemplate
        //
        standardTemplate.addTemplate(AdminUtils.formatUserContact(contact, requestContext, false));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
