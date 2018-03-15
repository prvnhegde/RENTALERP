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
 * Action class for adding/editing Company Contact.
 */
public class CompanyContactForm extends BaseForm {

    private Integer companyId;
    private Integer contactId;
    private String contactTitle;
    private String contactPhoneWork;
    private String contactFax;
    private String contactEmailPrimary;
    private String contactHomepageUrl;
    private String contactDescription;
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String addressZipcode;
    private String addressCountry;

    @Override
    public void setRequest(RequestContext requestContext) {
        companyId = requestContext.getParameterInteger("companyId");
        contactId = requestContext.getParameterInteger("contactId");
        contactTitle = requestContext.getParameterString("contactTitle");
        contactPhoneWork = requestContext.getParameterString("contactPhoneWork");
        contactFax = requestContext.getParameterString("contactFax");
        contactEmailPrimary = requestContext.getParameterString("contactEmailPrimary");
        contactHomepageUrl = requestContext.getParameterString("contactHomepageUrl");
        contactDescription = requestContext.getParameterString("contactDescription");
        addressStreet = requestContext.getParameterString("addressStreet");
        addressCity = requestContext.getParameterString("addressCity");
        addressState = requestContext.getParameterString("addressState");
        addressZipcode = requestContext.getParameterString("addressZipcode");
        addressCountry = requestContext.getParameterString("addressCountry");
    }

    public void setContact(Contact contact) {
        contactTitle = contact.getTitle();
        contactPhoneWork = contact.getPhoneWork();
        contactFax = contact.getFax();
        contactEmailPrimary = contact.getEmailPrimary();
        contactHomepageUrl = contact.getHomepageUrl();
        contactDescription = contact.getDescription();
        addressStreet = contact.getAddressStreetPrimary();
        addressCity = contact.getAddressCityPrimary();
        addressState = contact.getAddressStatePrimary();
        addressZipcode = contact.getAddressZipcodePrimary();
        addressCountry = contact.getAddressCountryPrimary();
    }

    public Integer getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    public Integer getContactId() {
        return contactId;
    }
    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
    public String getContactTitle() {
        return contactTitle;
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