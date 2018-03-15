/*
 * Copyright 2015 Kwoksys
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

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.framework.http.RequestContext;

/**
 * ActionForm class for hardware license.
 */
public class HardwareLicenseForm extends BaseForm {

    private String cmd;
    private Integer hardwareId;
    private Integer softwareId;
    private Integer licenseId;
    private Integer mapId;
    private int licenseEntitlement;

    @Override
    public void setRequest(RequestContext requestContext) {
        cmd = requestContext.getParameterString("cmd");
        hardwareId = requestContext.getParameterInteger("hardwareId");
        softwareId = requestContext.getParameterInteger("softwareId");
        licenseId = requestContext.getParameter("licenseId");
        mapId = requestContext.getParameterInteger("mapId");
        licenseEntitlement = requestContext.getParameter("licenseEntitlement", 1);
    }

    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public Integer getHardwareId() {
        return hardwareId;
    }
    public void setHardwareId(Integer hardwareId) {
        this.hardwareId = hardwareId;
    }
    public Integer getSoftwareId() {
        return softwareId;
    }

    public Integer getLicenseId() {
        return licenseId;
    }

    public Integer getMapId() {
        return mapId;
    }

    public int getLicenseEntitlement() {
        return licenseEntitlement;
    }
}
