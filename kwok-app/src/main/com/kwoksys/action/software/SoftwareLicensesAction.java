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

import java.util.Map;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for managing software licenses.
 */
public class SoftwareLicensesAction extends Action2 {

    public String add() throws Exception {
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        SoftwareLicenseForm actionForm = getBaseForm(SoftwareLicenseForm.class);
        Software software = softwareService.getSoftware(actionForm.getSoftwareId());

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setLicense(new SoftwareLicense());
            actionForm.setLicenseEntitlement("1");
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("software", software);
        standardTemplate.setPathAttribute("formAction", AppPaths.SOFTWARE_LICENSE_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.SOFTWARE_DETAIL + "?softwareId=" + software.getId()).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("software.license.addLicenseHeader");

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.SOFTWARE_LICENSE);
        customFieldsTemplate.setObjectId(actionForm.getSoftwareId());
        customFieldsTemplate.setForm(actionForm);
        customFieldsTemplate.setPartialTable(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String add2() throws Exception {
        SoftwareLicenseForm actionForm = saveActionForm(new SoftwareLicenseForm());

        SoftwareLicense license = new SoftwareLicense();
        license.setSoftwareId(actionForm.getSoftwareId());
        license.setKey(actionForm.getLicenseKey());
        license.setNote(actionForm.getLicenseNote());
        license.setEntitlement(actionForm.getLicenseEntitlement());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.SOFTWARE_LICENSE);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, license, customAttributes);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        ActionMessages errors = softwareService.addLicense(license, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);

            if (actionForm.hasCustomFields()) {
                return ajaxUpdateView(AppPaths.SOFTWARE_LICENSE_ADD + "?softwareId=" + license.getSoftwareId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
            } else {
                return ajaxUpdateView(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + license.getSoftwareId() + "&cmd=add&" + RequestContext.URL_PARAM_ERROR_TRUE);
            }
        } else {
            // Reset Software License count.
            softwareService.resetSoftwareLicenseCount(license);

            return ajaxUpdateView(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + license.getSoftwareId());
        }
    }
    
    public String delete2() throws Exception {
        SoftwareLicense license = new SoftwareLicense();
        license.setSoftwareId(requestContext.getParameter("softwareId"));
        license.setId(requestContext.getParameter("licenseId"));

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Delete software license
        softwareService.deleteLicense(license);

        // Reset Software License count
        softwareService.resetSoftwareLicenseCount(license);

        return ajaxUpdateView(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + license.getSoftwareId());
    }
    
    public String edit() throws Exception {
        SoftwareLicenseForm actionForm = getBaseForm(SoftwareLicenseForm.class);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(actionForm.getSoftwareId());
        SoftwareLicense softwareLicense = softwareService.getSoftwareLicense(actionForm.getSoftwareId(),
                actionForm.getLicenseId());

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setLicense(softwareLicense);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("software", software);
        request.setAttribute("softwareLicense", softwareLicense);
        standardTemplate.setPathAttribute("formAction", AppPaths.SOFTWARE_LICENSE_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.SOFTWARE_DETAIL + "?softwareId=" + software.getId()).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("software.license.updateLicenseHeader");

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.SOFTWARE_LICENSE);
        customFieldsTemplate.setObjectId(softwareLicense.getId());
        customFieldsTemplate.setForm(actionForm);
        customFieldsTemplate.setPartialTable(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        SoftwareLicenseForm actionForm = saveActionForm(new SoftwareLicenseForm());

        SoftwareLicense license = new SoftwareLicense();
        license.setSoftwareId(actionForm.getSoftwareId());
        license.setId(actionForm.getLicenseId());
        license.setKey(actionForm.getLicenseKey());
        license.setNote(actionForm.getLicenseNote());
        license.setEntitlement(actionForm.getLicenseEntitlement());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.SOFTWARE_LICENSE);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, license, customAttributes);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        ActionMessages errors = softwareService.updateLicense(license, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);

            if (actionForm.hasCustomFields()) {
                return ajaxUpdateView(AppPaths.SOFTWARE_LICENSE_EDIT + "?softwareId=" + license.getSoftwareId() + "&licenseId=" + license.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
            } else {
                return ajaxUpdateView(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + license.getSoftwareId() + "&licenseId=" + license.getId() + "&cmd=edit&" + RequestContext.URL_PARAM_ERROR_TRUE);
            }
        } else {
            return ajaxUpdateView(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + license.getSoftwareId());
        }
    }    
}
