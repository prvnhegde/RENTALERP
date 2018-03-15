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

import java.util.Map;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding company.
 */
public class CompanyAddAction extends Action2 {

    public String add() throws Exception {
        Company company = new Company();
        CompanyForm actionForm = getBaseForm(CompanyForm.class);

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            company.setTypeIds(CompanyUtils.DEFAULT_COMPANY_TYPES);
            actionForm.setCompany(company);
        }

        company.loadAttrs(requestContext);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTACTS_COMPANY_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTACTS_COMPANY_LIST).getString());
        standardTemplate.setAttribute("companyTypes", new AttributeManager(requestContext).getActiveAttrFieldOptionsCache(Attributes.COMPANY_TYPES));
        standardTemplate.setAttribute("company", company);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("contactMgmt.cmd.companyAdd");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("contactMgmt.companyAdd.sectionHeader");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.COMPANY);
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add2() throws Exception {
        CompanyForm actionForm = saveActionForm(new CompanyForm());

        Company company = new Company();
        company.setName(actionForm.getCompanyName());
        company.setDescription(actionForm.getCompanyDescription());
        company.setTags(actionForm.getCompanyTags());
        company.setTypeIds(actionForm.getCompanyTypes());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.COMPANY);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, company, customAttributes);

        // Call the service
        ContactService contactService = ServiceProvider.getContactService(requestContext);
        ActionMessages errors = contactService.addCompany(company, customAttributes);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + company.getId());
        }
    }
}
