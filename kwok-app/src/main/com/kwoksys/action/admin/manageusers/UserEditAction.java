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
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

/**
 * Action class for updating a user.
 */
public class UserEditAction extends Action2 {

    public String edit() throws Exception {
        UserForm actionForm = getBaseForm(UserForm.class);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AccessUser requestUser = adminService.getUser(actionForm.getUserId());

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getOptionalContact(requestUser.getContactId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            // Default user info
            actionForm.setUsername(requestUser.getUsername());
            actionForm.setDisplayName(requestUser.getDisplayName());
            actionForm.setStatus(requestUser.getStatus());
            actionForm.setGroupId(requestUser.getGroupId());
            actionForm.setFirstName(requestUser.getFirstName());
            actionForm.setLastName(requestUser.getLastName());
            actionForm.setEmail(requestUser.getEmail());

            // Default contact info
            actionForm.setCompanyId(contact.getCompanyId());

            actionForm.setContact(contact);
        }

        List<LabelValueBean> companyIdOptions = new ArrayList<>();
        companyIdOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        companyIdOptions.addAll(CompanyUtils.getCompanyOptions(requestContext));

        AttributeManager attributeManager = new AttributeManager(requestContext);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("userId", actionForm.getUserId());
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_USER_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_USER_DETAIL + "?userId=" + actionForm.getUserId()).getString());
        request.setAttribute("groupIdOptions", AdminUtils.getGroupOptions(requestContext));
        request.setAttribute("optionStatus", attributeManager.getActiveAttrFieldOptionsCache(Attributes.USER_STATUS_TYPE));
        request.setAttribute("optionsCompanyId", companyIdOptions);
        standardTemplate.setAttribute("messengerType1Options", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(contact.getMessenger1Type()).getActiveAttrFieldOptionsCache(Attributes.CONTACT_IM));
        standardTemplate.setAttribute("messengerType2Options", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(contact.getMessenger2Type()).getActiveAttrFieldOptionsCache(Attributes.CONTACT_IM));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.cmd.userEdit");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.USER);
        customFieldsTemplate.setObjectId(requestUser.getId());
        customFieldsTemplate.setForm(actionForm);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("admin.userEdit.sectionHeader");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        UserForm actionForm = saveActionForm(new UserForm());

        Integer reqUserId = requestContext.getParameter("userId");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        AccessUser requestUser = adminService.getUser(reqUserId);
        requestUser.setUsername(actionForm.getUsername());
        requestUser.setFirstName(actionForm.getFirstName());
        requestUser.setLastName(actionForm.getLastName());
        requestUser.setEmail(actionForm.getEmail());
        requestUser.setDisplayName(actionForm.getDisplayName());
        requestUser.setStatus(actionForm.getStatus());

        AccessGroup group = new AccessGroup();
        group.setId(actionForm.getGroupId());

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Contact contact = contactService.getOptionalContact(requestUser.getContactId());
        contact.setCompanyId(actionForm.getCompanyId());
        contact.setEmailPrimary(actionForm.getEmail());
        contact.setFirstName(actionForm.getFirstName());
        contact.setLastName(actionForm.getLastName());
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

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.USER);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, requestUser, customAttributes);

        ActionMessages errors = adminService.updateUser(requestUser, group, contact, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_USER_EDIT + "?userId=" + requestUser.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
            
        } else {
            // Remove user from cache
            new CacheManager(requestContext).removeUserCache(requestUser.getId());
            
            return ajaxUpdateView(AppPaths.ADMIN_USER_DETAIL + "?userId=" + requestUser.getId());
        }
    }
}
