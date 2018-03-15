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
package com.kwoksys.action.contacts;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for contact detail page.
 */
public class ContactDetailAction extends Action2 {

    public String execute() throws Exception {
        Integer contactId = requestContext.getParameter("contactId");

        AccessUser user = requestContext.getUser();

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getContact(contactId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("contactId", contactId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.contactDetail.title");

        if (contact.getUserId() != 0) {
            // This is a System User contact, link to User detail page.
            if (Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL)) {
                Link link = new Link(requestContext);
                link.setAjaxPath(AppPaths.ADMIN_USER_DETAIL + "?userId=" + contact.getUserId());
                link.setTitleKey("contactMgmt.cmd.contactViewUser");
                header.addHeaderCmds(link);

            } else if (Access.hasPermission(user, AppPaths.CONTACTS_CONTACT_DELETE)) {
                // Since this is a system user contact, we won't show contact link, but we
                // need to provide an explanation.
                Link link = new Link(requestContext);
                link.setTitleKey("contactMgmt.cmd.contactDeleteNoPermission");
                header.addHeaderCmds(link);
            }
        } else {
            // Contact edit.
            if (Access.hasPermission(user, AppPaths.CONTACTS_CONTACT_EDIT)) {
                Link link = new Link(requestContext);
                link.setAjaxPath(AppPaths.CONTACTS_CONTACT_EDIT + "?contactId=" + contactId);
                link.setTitleKey("contactMgmt.contactEdit.title");
                header.addHeaderCmds(link);
            }

            // Contact delete.
            if (Access.hasPermission(user, AppPaths.CONTACTS_CONTACT_DELETE)) {
                Link link = new Link(requestContext);
                link.setAjaxPath(AppPaths.CONTACTS_CONTACT_DELETE + "?contactId=" + contactId);
                link.setTitleKey("contactMgmt.contactDelete.title");
                header.addHeaderCmds(link);
            }

            // Create System User.
            if (Access.hasPermission(user, AppPaths.ADMIN_USER_ADD)) {
                Link link = new Link(requestContext);
                link.setAjaxPath(AppPaths.ADMIN_USER_ADD + "?contactId=" + contact.getId());
                link.setTitleKey("contactMgmt.cmd.contactAddUser");
                header.addHeaderCmds(link);
            }
        }

        // Link to export vCard
        if (Access.hasPermission(user, AppPaths.CONTACTS_CONTACT_EXPORT_VCARD)) {
            Link link = new Link(requestContext);
            link.setExportPath(AppPaths.CONTACTS_CONTACT_EXPORT_VCARD + "?contactId=" + contactId);
            link.setImgSrc(Image.getInstance().getVCardIcon());
            link.setTitleKey("contactMgmt.cmd.contactExport");
            header.addHeaderCmds(link);
        }

        // Link to contact detail page
        if (contact.getCompanyId()!=0 && Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_CONTACT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_CONTACT + "?companyId=" + contact.getCompanyId());
            link.setTitleKey("contactMgmt.cmd.companyDetail");
            header.addHeaderCmds(link);
        }

        //
        // Template: EmployeeContactSpecTemplate
        //
        EmployeeContactSpecTemplate tmpl = standardTemplate.addTemplate(new EmployeeContactSpecTemplate());
        tmpl.setContactDetailHeader(Localizer.getText(requestContext, "contactMgmt.contactDetail.title"));
        tmpl.setContact(contact);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
}

