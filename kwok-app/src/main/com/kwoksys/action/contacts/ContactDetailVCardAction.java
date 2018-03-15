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
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HttpUtils;
import com.kwoksys.framework.util.StringUtils;

import java.io.PrintWriter;

/**
 * Action class for contact-mgmt/contact-export-vcard.jsp.
 * For details about how vCard works, check http://www.imc.org/ website.
 */
public class ContactDetailVCardAction extends Action2 {

    public String execute() throws Exception {
        Integer contactId = requestContext.getParameter("contactId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getContact(contactId);

        response.setContentType("text");
        HttpUtils.setDownloadResponseHeaders(response, contact.getFirstName() + " " + contact.getLastName() + ".vcf");

        PrintWriter out = response.getWriter();
        // For debugging.
        //out.print("<pre>");

        Object[] nameArgs = {StringUtils.encodeVCard(contact.getFirstName()), StringUtils.encodeVCard(contact.getLastName())};

        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.begin"));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.version"));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.name", nameArgs));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.formatted_name", nameArgs));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.company_name") + StringUtils.encodeVCard(contact.getCompanyName()));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.contact_title") + StringUtils.encodeVCard(contact.getTitle()));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.contact_description") + StringUtils.encodeVCard(contact.getDescription()));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.contact_phone_home") + StringUtils.encodeVCard(contact.getPhoneHome()));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.contact_phone_work") + StringUtils.encodeVCard(contact.getPhoneWork()));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.contact_phone_mobile") + StringUtils.encodeVCard(contact.getPhoneMobile()));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.contact_fax") + StringUtils.encodeVCard(contact.getFax()));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.contact_email_primary") + StringUtils.encodeVCard(contact.getEmailPrimary()));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.contact_homepage_url") + StringUtils.encodeVCard(contact.getHomepageUrl()));
        out.println(Localizer.getText(requestContext, "contactMgmt.contactVCard.end"));

        // For debugging.
        //out.print("</pre>");
        return null;
    }
}

