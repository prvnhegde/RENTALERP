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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding Contact.
 */
public class ContactAdd2Action extends Action2 {

    public String execute() throws Exception {
        // Get request parameters
        Contact contact = new Contact();

        ContactForm actionForm = saveActionForm(new ContactForm());
        contact.setCompanyId(actionForm.getCompanyId());
        contact.setFirstName(actionForm.getContactFirstName());
        contact.setLastName(actionForm.getContactLastName());
        contact.setTitle(actionForm.getContactTitle());
        contact.setPhoneHome(actionForm.getContactPhoneHome());
        contact.setPhoneMobile(actionForm.getContactPhoneMobile());
        contact.setPhoneWork(actionForm.getContactPhoneWork());
        contact.setFax(actionForm.getContactFax());
        contact.setEmailPrimary(actionForm.getContactEmailPrimary());
        contact.setEmailSecondary(actionForm.getContactEmailSecondary());
        contact.setMessenger1Id(actionForm.getMessenger1Id());
        contact.setMessenger1Type(actionForm.getMessenger1Type());
        contact.setMessenger2Id(actionForm.getMessenger2Id());
        contact.setMessenger2Type(actionForm.getMessenger2Type());
        contact.setHomepageUrl(actionForm.getContactHomepageUrl());
        contact.setDescription(actionForm.getContactDescription());

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        ActionMessages errors = contactService.addEmployeeContact(contact);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTACTS_CONTACT_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return ajaxUpdateView(AppPaths.CONTACTS_CONTACT_DETAIL + "?contactId=" + contact.getId());
        }
    }
}
