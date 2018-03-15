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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.CompanyTag;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.StringUtils;

import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;

/**
 * Action class for editing company detail.
 */
public class CompanyEditAction extends Action2 {

    public String edit() throws Exception {
        CompanyForm actionForm = getBaseForm(CompanyForm.class);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Integer companyId = actionForm.getCompanyId();

        Company company = contactService.getCompany(companyId);
        company.loadAttrs(requestContext);

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            company.setTypeIds(contactService.getCompanyTypes(company.getId()));
            
            // Get a list of tags.
            QueryCriteria queryCriteria = new QueryCriteria();
            queryCriteria.addSortColumn(ContactQueries.getOrderByColumn(CompanyTag.TAG_NAME));

            company.setTags(StringUtils.join(contactService.getCompanyTags(queryCriteria, companyId), "tag_name", ", "));
            
            actionForm.setCompany(company);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTACTS_COMPANY_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + companyId).getString());

        List<Integer> companyTypeIds = ServiceProvider.getContactService(requestContext)
                .getCompanyTypes(company.getId());

        standardTemplate.setAttribute("companyTypes", new AttributeManager(requestContext)
                .setSelectedAttrFieldIds(companyTypeIds).getActiveAttrFieldOptionsCache(Attributes.COMPANY_TYPES));
        standardTemplate.setAttribute("company", company);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("contactMgmt.companyEdit.title");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("contactMgmt.companyEdit.sectionHeader");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.COMPANY);
        customFieldsTemplate.setObjectId(companyId);
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        CompanyForm actionForm = saveActionForm(new CompanyForm());

        Company company = new Company();
        company.setId(actionForm.getCompanyId());
        company.setName(actionForm.getCompanyName());
        company.setDescription(actionForm.getCompanyDescription());
        company.setTags(actionForm.getCompanyTags());
        company.setTypeIds(actionForm.getCompanyTypes());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.COMPANY);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, company, customAttributes);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        ActionMessages errors = contactService.updateCompany(company, customAttributes);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_EDIT + "?companyId=" + company.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + company.getId());
        }
    }
}
