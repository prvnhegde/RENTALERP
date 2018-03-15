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

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.framework.http.RequestContext;

/**
 * Action form for Contact search
 */
public class ContactSearchForm extends BaseForm {

    private String cmd;
    private String companyName;
    private String description;
    private String companyTag;
    private String contactFirstName;
    private String contactLastName;
    private String contactTitle;
    private String contactEmail;
    private String attrId;
    private String attrValue;

    public void setRequest(RequestContext requestContext) {
        cmd = requestContext.getParameterString("cmd");
        companyName = requestContext.getParameterString("companyName");
        description = requestContext.getParameterString("description");
        companyTag = requestContext.getParameterString("companyTag");
        contactFirstName = requestContext.getParameterString("contactFirstName");
        contactLastName = requestContext.getParameterString("contactLastName");
        contactTitle = requestContext.getParameterString("contactTitle");
        contactEmail = requestContext.getParameterString("contactEmail");
        attrId = requestContext.getParameterString("attrId");
        attrValue = requestContext.getParameterString("attrValue");
    }

    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getCompanyName() {
        return companyName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCompanyTag() {
        return companyTag;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getAttrId() {
        return attrId;
    }

    public String getAttrValue() {
        return attrValue;
    }
}
