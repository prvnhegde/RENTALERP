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
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for contact edit.
 */
public class ContactEditAction extends Action2 {

    public String execute() throws Exception {
        ContactForm actionForm = getBaseForm(ContactForm.class);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getContact(actionForm.getContactId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setContact(contact);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTACTS_CONTACT_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTACTS_CONTACT_DETAIL + "?contactId=" + contact.getId()).getString());
        standardTemplate.setAttribute("companyIdOptions", CompanyUtils.getCompanyOptions(requestContext));
        standardTemplate.setAttribute("messenger1TypeOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(contact.getMessenger1Type()).getActiveAttrFieldOptionsCache(Attributes.CONTACT_IM));
        standardTemplate.setAttribute("messenger2TypeOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(contact.getMessenger2Type()).getActiveAttrFieldOptionsCache(Attributes.CONTACT_IM));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("contactMgmt.contactEdit.title");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("contactMgmt.contactEdit.sectionHeader");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
