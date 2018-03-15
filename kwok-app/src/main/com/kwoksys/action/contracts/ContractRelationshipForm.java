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

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.framework.http.RequestContext;

/**
 * ContractSoftwareForm
 */
public class ContractRelationshipForm extends BaseForm {

    private Integer contractId;
    private Integer softwareId;
    private String formSoftwareId;
    private Integer hardwareId;
    private String formHardwareId;

    @Override
    public void setRequest(RequestContext requestContext) {
        contractId = requestContext.getParameterInteger("contractId");
        softwareId = requestContext.getParameterInteger("softwareId");
        formSoftwareId = requestContext.getParameterString("formSoftwareId");
        hardwareId = requestContext.getParameterInteger("hardwareId");
        formHardwareId = requestContext.getParameterString("formHardwareId");
    }

    public Integer getContractId() {
        return contractId;
    }
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getFormHardwareId() {
        return formHardwareId;
    }
    public void setFormHardwareId(String formHardwareId) {
        this.formHardwareId = formHardwareId;
    }

    public String getFormSoftwareId() {
        return formSoftwareId;
    }
    public void setFormSoftwareId(String formSoftwareId) {
        this.formSoftwareId = formSoftwareId;
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

    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }
}