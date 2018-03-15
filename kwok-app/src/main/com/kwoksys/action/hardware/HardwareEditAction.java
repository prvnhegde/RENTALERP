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
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.NumberUtils;

/**
 * Action class for editing hardware.
 */
public class HardwareEditAction extends Action2 {

    public String edit() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Load attributes
        hardware.loadAttrs(requestContext);

        HardwareForm actionForm = getBaseForm(HardwareForm.class);

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setHardware(hardware);
        }

        LabelValueBean selectOneLabel = new SelectOneLabelValueBean(requestContext, "0");

        // Only shows users whose status is "Enable", plus hardware owner.
        List<LabelValueBean> hardwareOwnerOptions = new ArrayList<>();
        hardwareOwnerOptions.add(new SelectOneLabelValueBean(requestContext));
        hardwareOwnerOptions.addAll(AdminUtils.getUserOptions(requestContext, hardware.getOwnerId()));

        // Get company list
        List<LabelValueBean> hardwareVendors = new ArrayList<>();
        hardwareVendors.add(selectOneLabel);

        CompanySearch vendorSearch = new CompanySearch();
        vendorSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_HARDWARE_VENDOR);
        vendorSearch.put(CompanySearch.COMPANY_ID_EQUALS, hardware.getVendorId());

        QueryCriteria vendorQuery = new QueryCriteria(vendorSearch);
        vendorQuery.addSortColumn(Company.COMPANY_NAME);

        hardwareVendors.addAll(CompanyUtils.getCompanyOptions(requestContext, vendorQuery));

        List<LabelValueBean> hardwareManufacturers = new ArrayList<>();
        hardwareManufacturers.add(selectOneLabel);

        CompanySearch manufacturerSearch = new CompanySearch();
        manufacturerSearch.put(CompanySearch.COMPANY_TYPE_EQUALS, AttributeFieldIds.COMPANY_TYPE_HARDWARE_MANUFACTURER);
        manufacturerSearch.put(CompanySearch.COMPANY_ID_EQUALS, hardware.getManufacturerId());

        QueryCriteria manufacturerQuery = new QueryCriteria(manufacturerSearch);
        manufacturerQuery.addSortColumn(Company.COMPANY_NAME);

        hardwareManufacturers.addAll(CompanyUtils.getCompanyOptions(requestContext, manufacturerQuery));

        int purchaseYear = NumberUtils.replaceNull(hardware.getPurchaseYear(), 0);
        int warrantyYear = NumberUtils.replaceNull(hardware.getWarrantyYear(), 0);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("hardware", hardware);
        standardTemplate.setPathAttribute("formAction", AppPaths.HARDWARE_EDIT_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.HARDWARE_EDIT);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.getId()).getString());
        standardTemplate.setAttribute("currencySymbol", ConfigManager.system.getCurrencySymbol());
        standardTemplate.setAttribute("locationOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(hardware.getLocation()).getActiveAttrFieldOptionsCache(Attributes.HARDWARE_LOCATION));
        standardTemplate.setAttribute("hardwareTypeOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(hardware.getType()).getActiveAttrFieldOptionsCache(Attributes.HARDWARE_TYPE));
        standardTemplate.setAttribute("hardwareStatusOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(hardware.getStatus()).getActiveAttrFieldOptionsCache(Attributes.HARDWARE_STATUS));
        standardTemplate.setAttribute("purchaseYearOptions", CalendarUtils.getExtraPastYearOptions(requestContext, purchaseYear));
        standardTemplate.setAttribute("warrantyYearOptions", CalendarUtils.getExtraYearOptions(requestContext, warrantyYear));
        standardTemplate.setAttribute("monthOptions", CalendarUtils.getMonthOptions(requestContext));
        standardTemplate.setAttribute("dateOptions", CalendarUtils.getDateOptions(requestContext));
        standardTemplate.setAttribute("hardwareOwnerOptions", hardwareOwnerOptions);
        standardTemplate.setAttribute("manufacturersOptions", hardwareManufacturers);
        standardTemplate.setAttribute("vendorsOptions", hardwareVendors);
        standardTemplate.setAttribute("manufacturerHelpIcon", Links.getHelpIconLink(requestContext, "help.addHardwareManufacturer"));
        standardTemplate.setAttribute("vendorHelpIcon", Links.getHelpIconLink(requestContext, "help.addHardwareVendor"));

        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.cmd.hardwareEdit");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("itMgmt.hardwareEdit.sectionHeader");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE);
        customFieldsTemplate.setObjectId(hardwareId);
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getHardwareType());
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        HardwareForm actionForm = saveActionForm(new HardwareForm());
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());
        
        Integer currentHardwareOwnerId = hardware.getOwnerId(); 
                
        hardware.setForm(actionForm);

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.HARDWARE);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, hardware, customAttributes);

        // Update the hardware
        ActionMessages errors = hardwareService.updateHardware(hardware, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_EDIT + "?hardwareId=" + hardware.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            if (!currentHardwareOwnerId.equals(hardware.getOwnerId())) {
                if (currentHardwareOwnerId != 0) {
                    new CacheManager(requestContext).removeUserCache(currentHardwareOwnerId);
                }
                if (hardware.getOwnerId() != 0) {
                    new CacheManager(requestContext).removeUserCache(hardware.getOwnerId());
                }
            }
            
            return ajaxUpdateView(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.getId());
        }
    }
}
