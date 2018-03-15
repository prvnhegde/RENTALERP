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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

/**
 * Action class for adding user.
 */
public class UserAddAction extends Action2 {

    public String add() throws Exception {
        UserForm actionForm = getBaseForm(UserForm.class);
        AccessUser requestUser = new AccessUser();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        Integer contactId = requestContext.getParameter("contactId", 0);
        Contact contact = contactId == 0 ? new Contact() : contactService.getContact(contactId);

        actionForm.setContactId(contactId);
        actionForm.setPassword(null);
        actionForm.setConfirmPassword(null);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            // Default user info
            actionForm.setUsername(requestUser.getUsername());
            actionForm.setDisplayName(requestUser.getDisplayName());
            actionForm.setStatus(requestUser.getStatus());
            actionForm.setGroupId(requestUser.getGroupId());
            
            // Default contact info
            actionForm.setFirstName(contact.getFirstName());
            actionForm.setLastName(contact.getLastName());
            actionForm.setEmail(contact.getEmailPrimary());
            actionForm.setCompanyId(contact.getCompanyId());

            actionForm.setContact(contact);
        }

        List<LabelValueBean> companyIdOptions = new ArrayList<>();
        companyIdOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        companyIdOptions.addAll(CompanyUtils.getCompanyOptions(requestContext));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_USER_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_USER_LIST).getString());
        request.setAttribute("allowPasswordUpdate", adminService.allowPasswordUpdate());
        request.setAttribute("groupIdOptions", AdminUtils.getGroupOptions(requestContext));
        request.setAttribute("optionStatus", new AttributeManager(requestContext).getActiveAttrFieldOptionsCache(Attributes.USER_STATUS_TYPE));
        request.setAttribute("optionsCompanyId", companyIdOptions);
        request.setAttribute("messengerTypeOptions", new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.CONTACT_IM));
        request.setAttribute("allowBlankPassword", ConfigManager.admin.isAllowBlankUserPassword());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.userAdd.title");
        header.setOnloadJavascript("App.togglePasswordFields(" + actionForm.getStatus() + ", 'passwordSpan', 'confirmPasswordSpan');");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("admin.userAdd.sectionHeader");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.USER);
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add2() throws Exception {
        UserForm actionForm = saveActionForm(new UserForm());

        AccessUser requestUser = new AccessUser();
        requestUser.setUsername(actionForm.getUsername());
        requestUser.setFirstName(actionForm.getFirstName());
        requestUser.setLastName(actionForm.getLastName());
        requestUser.setEmail(actionForm.getEmail());
        requestUser.setDisplayName(actionForm.getDisplayName());
        requestUser.setStatus(actionForm.getStatus());
        requestUser.setPasswordNew(actionForm.getPassword());
        requestUser.setPasswordConfirm(actionForm.getConfirmPassword());

        AccessGroup group = new AccessGroup();
        group.setId(actionForm.getGroupId());

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        Contact contact = actionForm.getContactId() == 0 ? new Contact() : contactService.getContact(actionForm.getContactId());

        contact.setCompanyId(actionForm.getCompanyId());
        contact.setFirstName(actionForm.getFirstName());
        contact.setLastName(actionForm.getLastName());
        contact.setEmailPrimary(actionForm.getEmail());
        contact.setEmailSecondary("");
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
        contact.setDescription(actionForm.getContactDescription());
        contact.setAddressStreetPrimary(actionForm.getAddressStreet());
        contact.setAddressCityPrimary(actionForm.getAddressCity());
        contact.setAddressStatePrimary(actionForm.getAddressState());
        contact.setAddressZipcodePrimary(actionForm.getAddressZipcode());
        contact.setAddressCountryPrimary(actionForm.getAddressCountry());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.USER);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, requestUser, customAttributes);

        ActionMessages errors = adminService.addUser(requestUser, group, contact, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_USER_ADD + "?contactId=" + contact.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
            
        } else {
            return ajaxUpdateView(AppPaths.ADMIN_USER_DETAIL + "?userId=" + requestUser.getId());
        }
    }
}