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
package com.kwoksys.biz.contacts.dto;

import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.util.StringUtils;

/**
 * Contact Object.
 */
public class Contact extends BaseObject {

    public static final String ID = "contact_id";
    public static final String FIRST_NAME = "contact_first_name";
    public static final String LAST_NAME = "contact_last_name";
    public static final String TITLE = "contact_title";
    public static final String PRIMARY_EMAIL = "contact_email_primary";
    public static final String WORK_PHONE = "contact_phone_work";
    public static final String COMPANY_NAME = "company_name";

    private Integer id;
    private Integer userId;
    private Integer companyId;
    private Integer companyContactType;
    private String companyName;
    private String firstName;
    private String lastName;
    private String description;
    private String title;
    private String phoneHome;
    private String phoneMobile;
    private String phoneWork;
    private String fax;
    private String emailPrimary;
    private String emailSecondary;
    private String homepageUrl;
    private String addressStreetPrimary;
    private String addressCityPrimary;
    private String addressStatePrimary;
    private String addressZipcodePrimary;
    private String addressCountryPrimary;
    private Integer messenger1Type;
    private String messenger1Id;
    private Integer messenger2Type;
    private String messenger2Id;

    public Contact() {
        id = 0;
        userId = 0;
        messenger1Type = 0;
        messenger2Type = 0;
        companyId = 0;
    }

    public Contact(Integer companyId) {
        this();
        this.companyId = companyId;
    }

    public String getMessenger1TypeAttribute(RequestContext requestContext) throws DatabaseException {
        AttributeManager attributeManager = new AttributeManager(requestContext);
        return StringUtils.replaceNull(attributeManager.getAttrFieldNameCache(Attributes.CONTACT_IM, messenger1Type));
    }

    public String getMessenger2TypeAttribute(RequestContext requestContext) throws DatabaseException {
        AttributeManager attributeManager = new AttributeManager(requestContext);
        return StringUtils.replaceNull(attributeManager.getAttrFieldNameCache(Attributes.CONTACT_IM, messenger2Type));
    }

    //
    // Getter and Setter
    //
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setContactId(Integer contactId) {
        this.id = contactId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setCompanyContactType(Integer companyContactType) {
        this.companyContactType = companyContactType;
    }
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }
    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }
    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }
    public void setEmailPrimary(String emailPrimary) {
        this.emailPrimary = emailPrimary;
    }
    public void setEmailSecondary(String emailSecondary) {
        this.emailSecondary = emailSecondary;
    }
    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }
    public void setAddressStreetPrimary(String addressStreetPrimary) {
        this.addressStreetPrimary = addressStreetPrimary;
    }
    public void setAddressCityPrimary(String addressCityPrimary) {
        this.addressCityPrimary = addressCityPrimary;
    }
    public void setAddressStatePrimary(String addressStatePrimary) {
        this.addressStatePrimary = addressStatePrimary;
    }
    public void setAddressZipcodePrimary(String addressZipcodePrimary) {
        this.addressZipcodePrimary = addressZipcodePrimary;
    }
    public void setAddressCountryPrimary(String addressCountryPrimary) {
        this.addressCountryPrimary = addressCountryPrimary;
    }
    public void setMessenger1Type(Integer messenger1Type) {
        this.messenger1Type = messenger1Type;
    }
    public void setMessenger1Id(String messenger1Id) {
        this.messenger1Id = messenger1Id;
    }
    public void setMessenger2Type(Integer messenger2Type) {
        this.messenger2Type = messenger2Type;
    }
    public void setMessenger2Id(String messenger2Id) {
        this.messenger2Id = messenger2Id;
    }
    public Integer getUserId() {
        return userId;
    }
    public Integer getCompanyContactType() {
        return companyContactType;
    }
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
    public String getPhoneHome() {
        return phoneHome;
    }
    public String getPhoneMobile() {
        return phoneMobile;
    }
    public String getPhoneWork() {
        return phoneWork;
    }
    public String getFax() {
        return fax;
    }
    public String getEmailPrimary() {
        return emailPrimary;
    }
    public String getEmailSecondary() {
        return emailSecondary;
    }
    public String getHomepageUrl() {
        return homepageUrl;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getAddressStreetPrimary() {
        return addressStreetPrimary;
    }
    public String getAddressCityPrimary() {
        return addressCityPrimary;
    }
    public String getAddressStatePrimary() {
        return addressStatePrimary;
    }
    public String getAddressZipcodePrimary() {
        return addressZipcodePrimary;
    }
    public String getAddressCountryPrimary() {
        return addressCountryPrimary;
    }
    public Integer getMessenger1Type() {
        return messenger1Type;
    }
    public String getMessenger1Id() {
        return messenger1Id;
    }
    public Integer getMessenger2Type() {
        return messenger2Type;
    }
    public String getMessenger2Id() {
        return messenger2Id;
    }
}
