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
package com.kwoksys.action.hardware;

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
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

/**
 * Action class for adding hardware.
 */
public class HardwareAddAction extends Action2 {

    public String add() throws Exception {
        Integer copyHardwareId = requestContext.getParameter("copyHardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware;

        // If copyHardwareId is provided, we get the hardware spec of that id
        if (copyHardwareId == 0) {
            hardware = new Hardware();
        } else {
            hardware = hardwareService.getHardware(copyHardwareId);
        }

        // Load attributes
        hardware.loadAttrs(requestContext);
        
        HardwareForm actionForm = getBaseForm(HardwareForm.class);
        
        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setHardware(hardware);
        }

        LabelValueBean selectOneLabel = new SelectOneLabelValueBean(requestContext, "0");

        // Hardware owner options
        List<LabelValueBean> hardwareOwnerOptions = new ArrayList<>();
        hardwareOwnerOptions.add(new SelectOneLabelValueBean(requestContext));
        hardwareOwnerOptions.addAll(AdminUtils.getUserOptions(requestContext));

        // Get company list
        List<LabelValueBean> hardwareVendors = new ArrayList<>();
        hardwareVendors.add(selectOneLabel);

        CompanySearch vendorSearch = new CompanySearch();
        vendorSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_HARDWARE_VENDOR);

        QueryCriteria vendorQuery = new QueryCriteria(vendorSearch);
        vendorQuery.addSortColumn(Company.COMPANY_NAME);
        hardwareVendors.addAll(CompanyUtils.getCompanyOptions(requestContext, vendorQuery));

        List<LabelValueBean> hardwareManufacturers = new ArrayList<>();
        hardwareManufacturers.add(selectOneLabel);

        CompanySearch manufacturerSearch = new CompanySearch();
        manufacturerSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_HARDWARE_MANUFACTURER);

        QueryCriteria manufacturerQuery = new QueryCriteria(manufacturerSearch);
        manufacturerQuery.addSortColumn(Company.COMPANY_NAME);
        hardwareManufacturers.addAll(CompanyUtils.getCompanyOptions(requestContext, manufacturerQuery));

        List<LabelValueBean> warrantyOptions = new ArrayList<>();
        warrantyOptions.add(new LabelValueBean(Localizer.getText(requestContext, "hardware.selectWarrantyPeriod"), "0"));
        for (int i=1; i<=3; i++) {
            String numYear = String.valueOf(i);
            warrantyOptions.add(new LabelValueBean(Localizer.getText(requestContext, "hardware.predefinedWarranty",
                    new String[]{numYear}), numYear));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("hardware", hardware);
        standardTemplate.setPathAttribute("formAction", AppPaths.HARDWARE_ADD_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.HARDWARE_ADD);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_LIST).getString());
        standardTemplate.setAttribute("manufacturerHelpIcon", Links.getHelpIconLink(requestContext, "help.addHardwareManufacturer"));
        standardTemplate.setAttribute("vendorHelpIcon", Links.getHelpIconLink(requestContext, "help.addHardwareVendor"));
        request.setAttribute("currencySymbol", ConfigManager.system.getCurrencySymbol());
        request.setAttribute("purchaseYearOptions", CalendarUtils.getPastYearOptions(requestContext));
        request.setAttribute("purchaseMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("purchaseDateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("warrantyYearOptions", CalendarUtils.getYearOptions(requestContext));
        request.setAttribute("warrantyMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("warrantyDateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("hardwareOwnerOptions", hardwareOwnerOptions);
        request.setAttribute("manufacturersOptions", hardwareManufacturers);
        request.setAttribute("vendorsOptions", hardwareVendors);
        request.setAttribute("warrantyPeriodOptions", warrantyOptions);

        request.setAttribute("hardwareStatusOptions", new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.HARDWARE_STATUS));

        request.setAttribute("hardwareTypeOptions", new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.HARDWARE_TYPE));

        request.setAttribute("locationOptions", new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.HARDWARE_LOCATION));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.cmd.hardwareAdd");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE);
        if (copyHardwareId != 0) {
            customFieldsTemplate.setObjectId(copyHardwareId);
        }
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getHardwareType());
        customFieldsTemplate.setForm(actionForm);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("itMgmt.hardwareAdd.sectionHeader");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add2() throws Exception {
        HardwareForm actionForm = saveActionForm(new HardwareForm());

        Hardware hardware = new Hardware();
        hardware.setForm(actionForm);

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.HARDWARE);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, hardware, customAttributes);
        
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        // Add the hardware
        ActionMessages errors = hardwareService.addHardware(hardware, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
            
        } else {
            if (hardware.getOwnerId() != 0) {
                new CacheManager(requestContext).removeUserCache(hardware.getOwnerId());
            }

            return ajaxUpdateView(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.getId());
        }
    }
}
