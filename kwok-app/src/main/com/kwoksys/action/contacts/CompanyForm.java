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
package com.kwoksys.action.contacts;

import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.framework.http.RequestContext;

import java.util.List;

/**
 * ActionClass for editing company.
 */
public class CompanyForm extends BaseObjectForm {

    private Integer companyId;
    private String companyName;
    private String companyDescription;
    private String companyTags;
    private List<Integer> companyTypes;
    private String issueId;

    @Override
    public void setRequest(RequestContext requestContext) {
        companyId = requestContext.getParameterInteger("companyId");
        companyName = requestContext.getParameterString("companyName");
        companyDescription = requestContext.getParameterString("companyDescription");
        companyTags = requestContext.getParameterString("companyTags");
        companyTypes = requestContext.getParameters("companyTypes");
        issueId = requestContext.getParameterString("issueId");
    }

    public void setCompany(Company company) {
        companyName = company.getName();
        companyDescription = company.getDescription();
        companyTags = company.getTags();
        companyTypes = company.getTypeIds();
    }

    public Integer getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public String getCompanyTags() {
        return companyTags;
    }

    public List<Integer> getCompanyTypes() {
        return companyTypes;
    }

    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
}
