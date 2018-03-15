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

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class serving page for Ajax call.
 */
public class AjaxGetLicenseBySoftwareAction extends Action2 {

    public String execute() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");

        List<LabelValueBean> licenseOptions = new ArrayList<>();

        if (softwareId != 0) {
            QueryCriteria query = new QueryCriteria();
            query.addSortColumn(HardwareQueries.getOrderByColumn(SoftwareLicense.LICENSE_KEY));

            HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

            for (SoftwareLicense license : hardwareService.getAvailableLicenses(query, softwareId)) {
                licenseOptions.add(new LabelValueBean(HtmlUtils.encode(license.getKey())
                        + (license.getNote().isEmpty() ? "" : " (" + SoftwareUtils.formatLicenseKey(license.getNote()) + ")")
                        , String.valueOf(license.getId())));
            }
        }

        //
        // Template: AjaxTemplate
        //
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);
        ajaxTemplate.setAttribute("licenseOptions", licenseOptions);

        return ajaxTemplate.findTemplate(AJAX_TEMPLATE);
    }
}
