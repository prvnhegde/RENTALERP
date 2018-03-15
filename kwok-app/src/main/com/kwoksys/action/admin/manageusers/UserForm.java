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
package com.kwoksys.action.admin.manageusers;

import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.framework.http.RequestContext;

/**
 * ActionForm for adding/editing user.
 */
public class UserForm extends BaseObjectForm {

    private String password;
    private String confirmPassword;
    private Integer userId;

    // User detail.
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private int status;
    private String displayName;
    private Integer groupId;

    // User contact.
    private Integer contactId;
    private String contactTitle;
    private Integer companyId;
    private String contactPhoneHome;
    private String contactPhoneMobile;
    private String contactPhoneWork;
    private String contactFax;
    private String contactEmailSecondary;
    private String contactHomepageUrl;
    private String contactDescription;
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String addressZipcode;
    private String addressCountry;
    private String messenger1Id;
    private Integer messenger1Type;
    private String messenger2Id;
    private Integer messenger2Type;

    @Override
    public void setRequest(RequestContext requestContext) {
        password = requestContext.getParameterString("password");
        confirmPassword = requestContext.getParameterString("confirmPassword");
        userId = requestContext.getParameterInteger("userId");
        username = requestContext.getParameterString("username");
        firstName = requestContext.getParameterString("firstName");
        lastName = requestContext.getParameterString("lastName");
        email = requestContext.getParameterString("email");
        status = requestContext.getParameter("status");
        displayName = requestContext.getParameterString("displayName");
        groupId = requestContext.getParameter("groupId");

        // User contact.
        contactId = requestContext.getParameterInteger("contactId");
        contactTitle = requestContext.getParameterString("contactTitle");
        companyId = requestContext.getParameterInteger("companyId");
        contactPhoneHome = requestContext.getParameterString("contactPhoneHome");
        contactPhoneMobile = requestContext.getParameterString("contactPhoneMobile");
        contactPhoneWork = requestContext.getParameterString("contactPhoneWork");
        contactFax = requestContext.getParameterString("contactFax");
        contactEmailSecondary = requestContext.getParameterString("contactEmailSecondary");
        contactHomepageUrl = requestContext.getParameterString("contactHomepageUrl");
        contactDescription = requestContext.getParameterString("contactDescription");
        addressStreet = requestContext.getParameterString("addressStreet");
        addressCity = requestContext.getParameterString("addressCity");
        addressState = requestContext.getParameterString("addressState");
        addressZipcode = requestContext.getParameterString("addressZipcode");
        addressCountry = requestContext.getParameterString("addressCountry");
        messenger1Id = requestContext.getParameterString("messenger1Id");
        messenger1Type = requestContext.getParameterInteger("messenger1Type");
        messenger2Id = requestContext.getParameterString("messenger2Id");
        messenger2Type = requestContext.getParameterInteger("messenger2Type");
    }

    public void setContact(Contact contact) {
        contactTitle = contact.getTitle();
        contactPhoneHome = contact.getPhoneHome();
        contactPhoneMobile = contact.getPhoneMobile();
        contactPhoneWork = contact.getPhoneWork();
        contactFax = contact.getFax();
        contactEmailSecondary = contact.getEmailSecondary();
        addressStreet = contact.getAddressStreetPrimary();
        addressCity = contact.getAddressCityPrimary();
        addressState = contact.getAddressStatePrimary();
        addressZipcode = contact.getAddressZipcodePrimary();
        addressCountry = contact.getAddressCountryPrimary();
        messenger1Id = contact.getMessenger1Id();
        messenger1Type = contact.getMessenger1Type();
        messenger2Id = contact.getMessenger2Id();
        messenger2Type = contact.getMessenger2Type();
        contactHomepageUrl = contact.getHomepageUrl();
        contactDescription = contact.getDescription();
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getContactTitle() {
        return contactTitle;
    }

    public Integer getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public String getContactEmailSecondary() {
        return contactEmailSecondary;
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

    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    public Integer getContactId() {
        return contactId;
    }
    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
}
