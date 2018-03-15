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

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareComponent;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.struts2.Action2;

public class AjaxGetComponentCustomFieldsAction extends Action2 {

    public String execute() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");
        Integer compId = requestContext.getParameter("compId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Hardware component
        HardwareComponent component = hardwareService.getHardwareComponent(hardware.getId(), compId);

        //
        // Template: AjaxTemplate
        //
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = ajaxTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE_COMPONENT);
        customFieldsTemplate.setObjectId(component.getId());
        customFieldsTemplate.setAllowToggle(false);

        return ajaxTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
}
