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

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.framework.http.RequestContext;

/**
 * ActionForm for adding/editing contact.
 */
public class ContactForm extends BaseForm {

    private Integer contactId;
    private Integer companyId;
    private String contactFirstName;
    private String contactLastName;
    private String contactTitle;
    private String contactPhoneHome;
    private String contactPhoneMobile;
    private String contactPhoneWork;
    private String contactFax;
    private String contactEmailPrimary;
    private String contactEmailSecondary;
    private String messenger1Id;
    private Integer messenger1Type;
    private String messenger2Id;
    private Integer messenger2Type;
    private String contactHomepageUrl;
    private String contactDescription;
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String addressZipcode;
    private String addressCountry;

    @Override
    public void setRequest(RequestContext requestContext) {
        contactId = requestContext.getParameterInteger("contactId");
        companyId = requestContext.getParameterInteger("companyId");
        contactFirstName = requestContext.getParameterString("contactFirstName");
        contactLastName = requestContext.getParameterString("contactLastName");
        contactTitle = requestContext.getParameterString("contactTitle");
        contactPhoneHome = requestContext.getParameterString("contactPhoneHome");
        contactPhoneMobile = requestContext.getParameterString("contactPhoneMobile");
        contactPhoneWork = requestContext.getParameterString("contactPhoneWork");
        contactFax = requestContext.getParameterString("contactFax");
        contactEmailPrimary = requestContext.getParameterString("contactEmailPrimary");
        contactEmailSecondary = requestContext.getParameterString("contactEmailSecondary");
        messenger1Id = requestContext.getParameterString("messenger1Id");
        messenger1Type = requestContext.getParameterInteger("messenger1Type");
        messenger2Id = requestContext.getParameterString("messenger2Id");
        messenger2Type = requestContext.getParameterInteger("messenger2Type");
        contactHomepageUrl = requestContext.getParameterString("contactHomepageUrl");
        contactDescription = requestContext.getParameterString("contactDescription");
        addressStreet = requestContext.getParameterString("addressStreet");
        addressCity = requestContext.getParameterString("addressCity");
        addressState = requestContext.getParameterString("addressState");
        addressZipcode = requestContext.getParameterString("addressZipcode");
        addressCountry = requestContext.getParameterString("addressCountry");
    }

    public void setContact(Contact contact) {
        companyId = contact.getCompanyId();
        contactFirstName = contact.getFirstName();
        contactLastName = contact.getLastName();
        contactTitle = contact.getTitle();
        contactPhoneHome = contact.getPhoneHome();
        contactPhoneMobile = contact.getPhoneMobile();
        contactPhoneWork = contact.getPhoneWork();
        contactFax = contact.getFax();
        contactEmailPrimary = contact.getEmailPrimary();
        contactEmailSecondary = contact.getEmailSecondary();
        messenger1Id = contact.getMessenger1Id();
        messenger1Type = contact.getMessenger1Type();
        messenger2Id = contact.getMessenger2Id();
        messenger2Type = contact.getMessenger2Type();
        contactHomepageUrl = contact.getHomepageUrl();
        contactDescription = contact.getDescription();
        addressStreet = contact.getAddressStreetPrimary();
        addressCity = contact.getAddressCityPrimary();
        addressState = contact.getAddressStatePrimary();
        addressZipcode = contact.getAddressZipcodePrimary();
        addressCountry = contact.getAddressCountryPrimary();
    }

    public Integer getContactId() {
        return contactId;
    }
    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
    public Integer getCompanyId() {
        return companyId;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public String getContactPhoneHome() {
        return contactPhoneHome;
    }

    public String getContactPhoneMobile() {
        return contactPhoneMobile;
    }

    public String getContactPhoneWork() {
        return contactPhoneWork;
    }

    public String getContactFax() {
        return contactFax;
    }

    public String getContactEmailPrimary() {
        return contactEmailPrimary;
    }

    public String getContactEmailSecondary() {
        return contactEmailSecondary;
    }

    public String getMessenger1Id() {
        return messenger1Id;
    }

    public Integer getMessenger1Type() {
        return messenger1Type;
    }

    public String getMessenger2Id() {
        return messenger2Id;
    }

    public Integer getMessenger2Type() {
        return messenger2Type;
    }

    public String getContactHomepageUrl() {
        return contactHomepageUrl;
    }

    public String getContactDescription() {
        return contactDescription;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public String getAddressZipcode() {
        return addressZipcode;
    }

    public String getAddressCountry() {
        return addressCountry;
    }
}