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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.contacts.ContactForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for updating password.
 */
public class ContactEditAction extends Action2 {

    public String edit() throws Exception {
        ContactForm actionForm = getBaseForm(ContactForm.class);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getOptionalContact(requestContext.getUser().getContactId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setContact(contact);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.USER_PREF_CONTACT_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.USER_PREF_INDEX).getString());
        standardTemplate.setAttribute("messenger1TypeOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(contact.getMessenger1Type()).getActiveAttrFieldOptionsCache(Attributes.CONTACT_IM));
        standardTemplate.setAttribute("messenger2TypeOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(contact.getMessenger2Type()).getActiveAttrFieldOptionsCache(Attributes.CONTACT_IM));
        standardTemplate.setAttribute("contact", contact);
        standardTemplate.setAttribute("contactId", contact.getId().equals(0) ? Localizer.getText(requestContext, "form.autoId") : contact.getId());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("userPref.contactEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("contactMgmt.contactEdit.sectionHeader");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        ContactForm actionForm = saveActionForm(new ContactForm());

        Contact contact = contactService.getOptionalContact(accessUser.getContactId());

        contact.setTitle(actionForm.getContactTitle());
        contact.setPhoneHome(actionForm.getContactPhoneHome());
        contact.setPhoneMobile(actionForm.getContactPhoneMobile());
        contact.setPhoneWork(actionForm.getContactPhoneWork());
        contact.setFax(actionForm.getContactFax());
        contact.setEmailSecondary(actionForm.getContactEmailSecondary());
        contact.setMessenger1Id(actionForm.getMessenger1Id());
        contact.setMessenger1Type(actionForm.getMessenger1Type());
        contact.setMessenger2Id(actionForm.getMessenger2Id());
        contact.setMessenger2Type(actionForm.getMessenger2Type());
        contact.setHomepageUrl(actionForm.getContactHomepageUrl());
        contact.setAddressStreetPrimary(actionForm.getAddressStreet());
        contact.setAddressCityPrimary(actionForm.getAddressCity());
        contact.setAddressStatePrimary(actionForm.getAddressState());
        contact.setAddressZipcodePrimary(actionForm.getAddressZipcode());
        contact.setAddressCountryPrimary(actionForm.getAddressCountry());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        ActionMessages errors = adminService.updateUserContact(accessUser, contact);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.USER_PREF_CONTACT_EDIT + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return ajaxUpdateView(AppPaths.USER_PREF_INDEX);
        }
    }
}

