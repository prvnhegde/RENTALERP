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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for editing company contact.
 */
public class CompanyContactEditAction extends Action2 {

    public String execute() throws Exception {
        CompanyContactForm actionForm = getBaseForm(CompanyContactForm.class);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getContact(actionForm.getContactId());
        actionForm.setCompanyId(contact.getCompanyId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setContact(contact);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("companyName", contact.getCompanyName());
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTACTS_COMPANY_CONTACT_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + contact.getCompanyId()).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("contactMgmt.companyContactEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("contactMgmt.companyContactEdit.sectionHeader");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
