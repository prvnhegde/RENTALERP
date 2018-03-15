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
package com.kwoksys.biz.admin.dto;

/**
 * AttributeValue
 */
public class AttributeValue {

    public static final String ATTR_KEY = "attribute_name";

    private Integer attributeId;
    private Integer attributeFieldId;
    private String attributeName;
    private String attributeValue;
    // Raw value from http request.
    private String rawValue;
    private Integer objectTypeId;
    private Integer objectId;

    public AttributeValue() {
        attributeId = 0;
        objectTypeId = 0;
        objectId = 0;
    }

    public Integer getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }
    public String getAttributeName() {
        return attributeName;
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    public String getAttributeValue() {
        return attributeValue;
    }
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
        this.rawValue = attributeValue;
    }
    public Integer getObjectTypeId() {
        return objectTypeId;
    }
    public void setObjectTypeId(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }
    public Integer getObjectId() {
        return objectId;
    }
    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getAttributeFieldId() {
        return attributeFieldId;
    }

    public void setAttributeFieldId(Integer attributeFieldId) {
        this.attributeFieldId = attributeFieldId;
    }

    public String getRawValue() {
        return rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }
}
