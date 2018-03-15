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

import com.kwoksys.action.reports.ReportForm;
import com.kwoksys.framework.http.RequestContext;

import java.util.List;

/**
 * ActionForm for software index page.
 */
public class SoftwareSearchForm extends ReportForm {

    private String cmd;
    private String softwareId;
    private String manufacturerId;
    private String manufacturerIdFilter;
    private List<Integer> softwareTypes;
    private String attrId;
    private String attrValue;

    @Override
    public void setRequest(RequestContext requestContext) {
        cmd = requestContext.getParameterString("cmd");
        softwareId = requestContext.getParameterString("softwareId");
        manufacturerId = requestContext.getParameterString("manufacturerId");
        manufacturerIdFilter = requestContext.getParameterString("manufacturerIdFilter");
        softwareTypes = requestContext.getParameters("softwareTypes");
        attrId = requestContext.getParameterString("attrId");
        attrValue = requestContext.getParameterString("attrValue");
    }

    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getSoftwareId() {
        return softwareId;
    }
    public void setSoftwareId(String softwareId) {
        this.softwareId = softwareId;
    }
    public List<Integer> getSoftwareTypes() {
        return softwareTypes;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public String getAttrId() {
        return attrId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public String getManufacturerIdFilter() {
        return manufacturerIdFilter;
    }

    public void setManufacturerIdFilter(String manufacturerIdFilter) {
        this.manufacturerIdFilter = manufacturerIdFilter;
    }
}
