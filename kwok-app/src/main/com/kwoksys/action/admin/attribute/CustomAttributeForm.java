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
package com.kwoksys.action.admin.attribute;

import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.framework.http.RequestContext;

import java.util.List;

/**
 * Action form for adding/editing custom attribute.
 */
public class CustomAttributeForm extends BaseForm {

    private Integer objectTypeId;
    private Integer attrId;
    private String attrName;
    private String description;
    private Integer attrGroupId;
    private String attrGroupName;
    private Integer attrType;
    private String currencySymbol;
    private String attrOption;
    private int attrConvertUrl;
    private String attrUrl;
    private String inputMask;
    private List<Integer> systemFieldIds;
    private int required;

    @Override
    public void setRequest(RequestContext requestContext) {
        objectTypeId = requestContext.getParameterInteger("objectTypeId");
        attrId = requestContext.getParameterInteger("attrId");
        attrName = requestContext.getParameterString("attrName");
        description = requestContext.getParameterString("description");
        attrGroupId = requestContext.getParameterInteger("attrGroupId");
        attrGroupName = requestContext.getParameterString("attrGroupName");
        attrType = requestContext.getParameterInteger("attrType");
        currencySymbol = requestContext.getParameterString("currencySymbol");
        attrOption = requestContext.getParameterString("attrOption");
        attrConvertUrl = requestContext.getParameter("attrConvertUrl");
        attrUrl = requestContext.getParameterString("attrUrl");
        inputMask = requestContext.getParameterString("inputMask");
        systemFieldIds = requestContext.getParameters("systemFields");
        required = requestContext.getParameter("required");
    }

    public void setAttribute(Attribute attr) {
        attrId = attr.getId();
        attrName = attr.getName();
        description = attr.getDescription();
        attrType = attr.getType();
        attrOption = attr.getAttributeOption();
        attrConvertUrl = attr.isConvertUrl() ? 1 : 0;
        attrUrl = attr.getUrl();
        attrGroupId = attr.getAttributeGroupId();
        inputMask = attr.getInputMask();
        currencySymbol = attr.getTypeCurrencySymbol();
        required = attr.isRequired() ? 1 : 0;
    }

    public Integer getObjectTypeId() {
        return objectTypeId;
    }
    public void setObjectTypeId(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }
    public Integer getAttrId() {
        return attrId;
    }

    public String getAttrName() {
        return attrName;
    }
    public String getAttrUrl() {
        return attrUrl;
    }

    public Integer getAttrType() {
        return attrType;
    }

    public String getAttrOption() {
        return attrOption;
    }

    public int getAttrConvertUrl() {
        return attrConvertUrl;
    }

    public String getAttrGroupName() {
        return attrGroupName;
    }

    public void setAttrGroupName(String attrGroupName) {
        this.attrGroupName = attrGroupName;
    }

    public Integer getAttrGroupId() {
        return attrGroupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputMask() {
        return inputMask;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public List<Integer> getSystemFieldIds() {
        return systemFieldIds;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }
}