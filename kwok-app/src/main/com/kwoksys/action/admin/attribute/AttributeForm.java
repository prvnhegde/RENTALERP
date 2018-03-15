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

import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.framework.http.RequestContext;

import java.util.List;

/**
 * Action form for adding/editing attribute.
 */
public class AttributeForm extends BaseForm {

    private Integer attributeId;
    private Integer attrFieldId;
    private String attributeFieldName;
    private String attributeFieldDescription;
    private List<Integer> customAttrs;
    private Integer iconId;
    private int disabled;
    private int required;
    private int defaultAttrField;

    @Override
    public void setRequest(RequestContext requestContext) {
        attributeId = requestContext.getParameterInteger("attributeId");
        attrFieldId = requestContext.getParameterInteger("attrFieldId");
        attributeFieldName = requestContext.getParameterString("attributeFieldName");
        attributeFieldDescription = requestContext.getParameterString("attributeFieldDescription");
        customAttrs = requestContext.getParameters("customAttrs");
        iconId = requestContext.getParameterInteger("iconId");
        disabled = requestContext.getParameter("disabled");
        required = requestContext.getParameter("required");
        defaultAttrField = requestContext.getParameter("defaultAttrField");
    }

    public void setAttributeField(AttributeField attrField) {
        attributeFieldName = attrField.getName();
        attributeFieldDescription = attrField.getDescription();
        disabled = attrField.isDisabled() ? 1 : 0;
        iconId = attrField.getIconId();
        customAttrs = attrField.getLinkedAttrIds();
    }

    public Integer getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }
    public Integer getAttrFieldId() {
        return attrFieldId;
    }

    public String getAttributeFieldName() {
        return attributeFieldName;
    }

    public String getAttributeFieldDescription() {
        return attributeFieldDescription;
    }

    public Integer getIconId() {
        return iconId;
    }

    public int getDisabled() {
        return disabled;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public List<Integer> getCustomAttrs() {
        return customAttrs;
    }

    public void setCustomAttrs(List<Integer> customAttrs) {
        this.customAttrs = customAttrs;
    }

    public int getDefaultAttrField() {
        return defaultAttrField;
    }

    public void setDefaultAttrField(int defaultAttrField) {
        this.defaultAttrField = defaultAttrField;
    }
}