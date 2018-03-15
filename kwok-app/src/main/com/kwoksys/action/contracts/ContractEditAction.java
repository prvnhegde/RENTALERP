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
package com.kwoksys.action.contracts;

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
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.NumberUtils;

/**
 * Action class for editing contract.
 */
public class ContractEditAction extends Action2 {

    public String edit() throws Exception {
        ContractForm actionForm = getBaseForm(ContractForm.class);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(actionForm.getContractId());

        // Load attributes
        contract.loadAttrs(requestContext);
        
        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setContract(contract);
        }

        // Get years
        int effectiveDateYear = NumberUtils.replaceNull(actionForm.getContractEffectiveDateYear(), 0);
        int expirationDateYear = NumberUtils.replaceNull(actionForm.getContractExpirationDateYear(), 0);
        int renewalDateYear = NumberUtils.replaceNull(actionForm.getContractRenewalDateYear(), 0);

        // Get company list
        List<LabelValueBean> companyOptions = new ArrayList<>();
        companyOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        companyOptions.addAll(CompanyUtils.getCompanyOptions(requestContext));

        // Only shows users whose status is "Enable", plus contract owner.
        List<LabelValueBean> contractOwnerOptions = new ArrayList<>();
        contractOwnerOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        contractOwnerOptions.addAll(AdminUtils.getUserOptions(requestContext, contract.getOwnerId()));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("contract", contract);
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTRACTS_EDIT_2 + "?contractId=" + actionForm.getContractId());
        standardTemplate.setPathAttribute("formThisAction", AppPaths.CONTRACTS_EDIT);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTRACTS_DETAIL + "?contractId=" + actionForm.getContractId()).getString());

        standardTemplate.setAttribute("contractTypeOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(contract.getType()).getActiveAttrFieldOptionsCache(Attributes.CONTRACT_TYPE));

        standardTemplate.setAttribute("contractStageOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(contract.getStage()).getActiveAttrFieldOptionsCache(Attributes.CONTRACT_STAGE));

        standardTemplate.setAttribute("contractRenewalTypeOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(contract.getRenewalType()).getActiveAttrFieldOptionsCache(Attributes.CONTRACT_RENEWAL_TYPE));

        request.setAttribute("contractOwnerOptions", contractOwnerOptions);
        request.setAttribute("dateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("monthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("effectiveYearOptions", CalendarUtils.getExtraYearOptions(requestContext, effectiveDateYear));
        request.setAttribute("expirationYearOptions", CalendarUtils.getExtraYearOptions(requestContext, expirationDateYear));
        request.setAttribute("renewalYearOptions", CalendarUtils.getExtraYearOptions(requestContext, renewalDateYear));
        request.setAttribute("contractProviderOptions", companyOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.cmd.contractEdit");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.CONTRACT);
        customFieldsTemplate.setObjectId(actionForm.getContractId());
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getContractType());
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        ContractForm actionForm = saveActionForm(new ContractForm());
        Contract contract = new Contract();
        contract.setId(actionForm.getContractId());
        contract.setName(actionForm.getContractName());
        contract.setDescription(actionForm.getContractDescription());
        contract.setOwnerId(actionForm.getContractOwner());
        contract.setType(actionForm.getContractType());
        contract.setStage(actionForm.getContractStage());
        contract.setContractProviderId(actionForm.getContractProviderId());
        contract.setEffectiveDateYear(actionForm.getContractEffectiveDateYear());
        contract.setEffectiveDateMonth(actionForm.getContractEffectiveDateMonth());
        contract.setEffectiveDateDate(actionForm.getContractEffectiveDateDate());
        contract.setExpireDateYear(actionForm.getContractExpirationDateYear());
        contract.setExpireDateMonth(actionForm.getContractExpirationDateMonth());
        contract.setExpireDateDate(actionForm.getContractExpirationDateDate());
        contract.setRenewalDateYear(actionForm.getContractRenewalDateYear());
        contract.setRenewalDateMonth(actionForm.getContractRenewalDateMonth());
        contract.setRenewalDateDate(actionForm.getContractRenewalDateDate());
        contract.setRenewalType(actionForm.getContractRenewalType());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.CONTRACT);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, contract, customAttributes);

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        ActionMessages errors = contractService.updateContract(contract, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTRACTS_EDIT + "?contractId=" + contract.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
            
        } else {
            return ajaxUpdateView(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
        }
    }
}
