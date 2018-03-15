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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Contact spec template.
 */
public class EmployeeContactSpecTemplate extends BaseTemplate {

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    private String contactDetailHeader;
    private Contact contact;

    public EmployeeContactSpecTemplate() {
        super(EmployeeContactSpecTemplate.class);
    }

    public void init() {
        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws Exception {
        detailTableTemplate.setNumColumns(2);

        DetailTableTemplate.Td td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_id");
        td.setValue(String.valueOf(contact.getId()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.company_name");
        td.setValue(Links.getCompanyDetailsLink(requestContext, contact.getCompanyName(), contact.getCompanyId()).getString());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_first_name");
        td.setValue(HtmlUtils.encode(contact.getFirstName()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_last_name");
        td.setValue(HtmlUtils.encode(contact.getLastName()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_title");
        td.setValue(HtmlUtils.encode(contact.getTitle()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_phone_work");
        td.setValue(HtmlUtils.encode(contact.getPhoneWork()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_phone_home");
        td.setValue(HtmlUtils.encode(contact.getPhoneHome()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_phone_mobile");
        td.setValue(HtmlUtils.encode(contact.getPhoneMobile()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_fax");
        td.setValue(HtmlUtils.encode(contact.getFax()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_email_primary");
        td.setValue(HtmlUtils.formatMailtoLink(contact.getEmailPrimary()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_email_secondary");
        td.setValue(HtmlUtils.formatMailtoLink(contact.getEmailSecondary()));

        td = detailTableTemplate.newTd();
        if (contact.getMessenger1TypeAttribute(requestContext).isEmpty()) {
            td.setHeaderKey("common.column.contact_im");
        } else {
            td.setHeaderText(Localizer.getText(requestContext, "common.column.contact_im_not_null",
                    new String[] {HtmlUtils.encode(contact.getMessenger1TypeAttribute(requestContext))}));
        }
        td.setValue(HtmlUtils.encode(contact.getMessenger1Id()));

        td = detailTableTemplate.newTd();
        if (contact.getMessenger2TypeAttribute(requestContext).isEmpty()) {
            td.setHeaderKey("common.column.contact_im");
        } else {
            td.setHeaderText(Localizer.getText(requestContext, "common.column.contact_im_not_null",
                    new String[] {HtmlUtils.encode(contact.getMessenger2TypeAttribute(requestContext))}));
        }
        td.setValue(HtmlUtils.encode(contact.getMessenger2Id()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_homepage_url");
        td.setValue(HtmlUtils.formatExternalLink(requestContext, contact.getHomepageUrl()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contact_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getDescription()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.creator");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, contact.getCreationDate(), contact.getCreator()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.modifier");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, contact.getModificationDate(), contact.getModifier()));

        request.setAttribute("TemplateContactSpec_contactDetailHeader", contactDetailHeader);
    }

    @Override
    public String getJspPath() {
        return "/jsp/contacts/ContactSpecTemplate.jsp";
    }

    public void setContactDetailHeader(String contactDetailHeader) {
        this.contactDetailHeader = contactDetailHeader;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
