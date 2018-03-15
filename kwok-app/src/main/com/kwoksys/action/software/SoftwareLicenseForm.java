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

import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.framework.http.RequestContext;

/**
 * ActionForm class for software detail.
 */
public class SoftwareLicenseForm extends BaseObjectForm {

    private Integer softwareId;
    private String cmd;
    private String licenseKey;
    private String licenseNote;
    private String licenseEntitlement = "1";
    private Integer licenseId;

    @Override
    public void setRequest(RequestContext requestContext) {
        softwareId = requestContext.getParameterInteger("softwareId");
        cmd = requestContext.getParameterString("cmd");
        licenseKey = requestContext.getParameterString("licenseKey");
        licenseNote = requestContext.getParameterString("licenseNote");
        licenseEntitlement = requestContext.getParameterString("licenseEntitlement");
        licenseId = requestContext.getParameterInteger("licenseId");
    }

    public void setLicense(SoftwareLicense softwareLicense) {
        licenseKey = softwareLicense.getKey();
        licenseEntitlement = String.valueOf(softwareLicense.getEntitlement());
        licenseNote = softwareLicense.getNote();
    }

    public Integer getSoftwareId() {
        return softwareId;
    }
    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }
    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getLicenseKey() {
        return licenseKey;
    }

    public String getLicenseNote() {
        return licenseNote;
    }

    public String getLicenseEntitlement() {
        return licenseEntitlement;
    }
    public void setLicenseEntitlement(String licenseEntitlement) {
        this.licenseEntitlement = licenseEntitlement;
    }
    public Integer getLicenseId() {
        return licenseId;
    }
}