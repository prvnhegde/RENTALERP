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

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * AjaxGetSoftwareDetailsAction
 */
public class AjaxGetLicenseDetailsAction extends Action2 {

    public String execute() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");
        Integer licenseId = requestContext.getParameter("licenseId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        SoftwareLicense softwareLicense = softwareService.getSoftwareLicense(software.getId(), licenseId);

        //
        // Template: AjaxTemplate
        //
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);
        ajaxTemplate.setAttribute("softwareLicense", softwareLicense);

        //
        // Template: DetailTableTemplate
        //
        DetailTableTemplate detailTableTemplate = ajaxTemplate.addTemplate(new DetailTableTemplate());
        detailTableTemplate.setStyle("standard");

        DetailTableTemplate.Td td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.license_entitlement");
        td.setValue(softwareLicense.getEntitlement());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.license_note");
        td.setValue(HtmlUtils.formatMultiLineDisplay(softwareLicense.getNote()));

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = ajaxTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.SOFTWARE_LICENSE);
        customFieldsTemplate.setObjectId(softwareLicense.getId());

        return ajaxTemplate.findTemplate(AJAX_TEMPLATE);
    }
}