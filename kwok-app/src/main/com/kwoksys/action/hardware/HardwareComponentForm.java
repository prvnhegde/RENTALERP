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

import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.framework.http.RequestContext;

/**
 * ActionForm class for hardware components.
 */
public class HardwareComponentForm extends BaseObjectForm {

    private Integer hardwareId;
    private Integer compId;
    private String compDescription;
    // This is a system field
    private Integer hardwareComponentType;

    private boolean hasCustomFields = false;

    @Override
    public void setRequest(RequestContext requestContext) {
        hardwareId = requestContext.getParameter("hardwareId");
        compId = requestContext.getParameter("compId");
        hardwareComponentType = requestContext.getParameter("hardwareComponentType");
        compDescription = requestContext.getParameterString("compDescription");
    }

    public Integer getHardwareId() {
        return hardwareId;
    }
    public void setHardwareId(Integer hardwareId) {
        this.hardwareId = hardwareId;
    }
    public Integer getCompId() {
        return compId;
    }
    public void setCompId(Integer compId) {
        this.compId = compId;
    }
    public Integer getHardwareComponentType() {
        return hardwareComponentType;
    }
    public void setHardwareComponentType(Integer hardwareComponentType) {
        this.hardwareComponentType = hardwareComponentType;
    }
    public String getCompDescription() {
        return compDescription;
    }
    public void setCompDescription(String compDescription) {
        this.compDescription = compDescription;
    }
    public boolean hasCustomFields() {
        return hasCustomFields;
    }

    public void setHasCustomFields(boolean hasCustomFields) {
        this.hasCustomFields = hasCustomFields;
    }
}