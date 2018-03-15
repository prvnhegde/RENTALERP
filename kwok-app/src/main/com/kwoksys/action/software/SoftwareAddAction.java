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
package com.kwoksys.action.software;

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
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.core.CompanySearch;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

/**
 * Action class for adding software.
 */
public class SoftwareAddAction extends Action2 {

    public String add() throws Exception {
        Software software = new Software();

        // Load attributes
        software.loadAttrs(requestContext);
        
        SoftwareForm actionForm = getBaseForm(SoftwareForm.class);

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setSoftware(software);
        }

        LabelValueBean selectOneLabel = new SelectOneLabelValueBean(requestContext, "0");

        // Software owner options
        List<LabelValueBean> softwareOwnerOptions = new ArrayList<>();
        softwareOwnerOptions.add(new SelectOneLabelValueBean(requestContext));
        softwareOwnerOptions.addAll(AdminUtils.getUserOptions(requestContext));

        // Get software maker/vendor list.
        List<LabelValueBean> softwareMakers = new ArrayList<>();
        softwareMakers.add(selectOneLabel);

        CompanySearch makerSearch = new CompanySearch();
        makerSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_SOFTWARE_MAKER);

        QueryCriteria makerQuery = new QueryCriteria(makerSearch);
        makerQuery.addSortColumn(Company.COMPANY_NAME);

        softwareMakers.addAll(CompanyUtils.getCompanyOptions(requestContext, makerQuery));

        List<LabelValueBean> softwareVendors = new ArrayList<>();
        softwareVendors.add(selectOneLabel);

        CompanySearch vendorSearch = new CompanySearch();
        vendorSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_SOFTWARE_VENDOR);

        QueryCriteria vendorQuery = new QueryCriteria(vendorSearch);
        vendorQuery.addSortColumn(Company.COMPANY_NAME);

        softwareVendors.addAll(CompanyUtils.getCompanyOptions(requestContext, vendorQuery));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("software", software);
        standardTemplate.setPathAttribute("formAction", AppPaths.SOFTWARE_ADD_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.SOFTWARE_ADD);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.SOFTWARE_LIST).getString());
        standardTemplate.setAttribute("softwareOwnerOptions", softwareOwnerOptions);
        standardTemplate.setAttribute("softwareTypeOptions", new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.SOFTWARE_TYPE));
        standardTemplate.setAttribute("softwareOsOptions", new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.SOFTWARE_OS));
        standardTemplate.setAttribute("manufacturersOptions", softwareMakers);
        standardTemplate.setAttribute("vendorsOptions", softwareVendors);
        standardTemplate.setAttribute("yearOptions", CalendarUtils.getYearOptions(requestContext));
        standardTemplate.setAttribute("monthOptions", CalendarUtils.getMonthOptions(requestContext));
        standardTemplate.setAttribute("dateOptions", CalendarUtils.getDateOptions(requestContext));
        standardTemplate.setAttribute("manufacturerHelpIcon", Links.getHelpIconLink(requestContext, "help.addSoftwareMaker"));
        standardTemplate.setAttribute("vendorHelpIcon", Links.getHelpIconLink(requestContext, "help.addSoftwareVendor"));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.cmd.softwareAdd");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("itMgmt.softwareAdd.sectionHeader");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.SOFTWARE);
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getSoftwareType());
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add2() throws Exception {
        SoftwareForm actionForm = saveActionForm(new SoftwareForm());
        Software software = new Software();
        software.setName(actionForm.getSoftwareName());
        software.setDescription(actionForm.getSoftwareDescription());
        software.setOwnerId(actionForm.getSoftwareOwner());
        software.setType(actionForm.getSoftwareType());
        software.setOs(actionForm.getSoftwareOS());
        software.setQuotedRetailPrice(actionForm.getRetailPrice());
        software.setQuotedOemPrice(actionForm.getOemPrice());
        software.setManufacturerId(actionForm.getManufacturerId());
        software.setVendorId(actionForm.getVendorId());
        software.setVersion(actionForm.getVersion());
        software.setExpireDateY(actionForm.getExpireDateY());
        software.setExpireDateM(actionForm.getExpireDateM());
        software.setExpireDateD(actionForm.getExpireDateD());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.SOFTWARE);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, software, customAttributes);
        
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Add the software
        ActionMessages errors = softwareService.addSoftware(software, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.SOFTWARE_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + software.getId());
        }
    }
}