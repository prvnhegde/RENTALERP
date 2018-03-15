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

import com.kwoksys.action.reports.ReportForm;
import com.kwoksys.framework.http.RequestContext;

/**
 * Action form for Index page.
 */
public class ContractSearchForm extends ReportForm {

    private String cmd;
    private String contractName;
    private String description;
    private int stage = 0;
    private String stageFilter;
    private Integer contractTypeId = 0;
    private Integer contractProviderId = 0;
    private String attrId;
    private String attrValue;

    @Override
    public void setRequest(RequestContext requestContext) {
        cmd = requestContext.getParameterString("cmd");
        stage = requestContext.getParameter("stage");
        stageFilter = requestContext.getParameterString("stageFilter");
        contractName = requestContext.getParameterString("contractName");
        description = requestContext.getParameterString("description");
        contractTypeId = requestContext.getParameter("contractTypeId");
        contractProviderId = requestContext.getParameter("contractProviderId");
        attrId = requestContext.getParameterString("attrId");
        attrValue = requestContext.getParameterString("attrValue");
    }

    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getContractName() {
        return contractName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAttrId() {
        return attrId;
    }
    public String getAttrValue() {
        return attrValue;
    }

    public Integer getContractTypeId() {
        return contractTypeId;
    }
    public Integer getContractProviderId() {
        return contractProviderId;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String getStageFilter() {
        return stageFilter;
    }

    public void setStageFilter(String stageFilter) {
        this.stageFilter = stageFilter;
    }
}