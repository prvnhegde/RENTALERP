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

import java.util.List;
import java.util.Arrays;

/**
 * Attribute Object.
 */
public class Attribute {

    public static final String ATTR_ID = "attribute_id";
    public static final String OBJECT_TYPE_ID = "object_type_id";
    public static final String ATTR_NAME = "attribute_name";
    public static final String IS_CUSTOM_ATTR = "is_custom_attr";
    public static final String ATTR_TYPE = "attribute_type";
    public static final String ATTR_OPTION = "attribute_option";
    public static final String ATTR_CONVERT_URL = "attribute_convert_url";
    public static final String ATTR_URL = "attribute_url";
    public static final String ATTR_IS_REQUIRED = "is_required";
    public static final String ATTR_TYPE_CURRENCY_SYMBOL = "type_currency_symbol";

    public static final Integer ATTR_TYPE_STRING = 1;
    public static final Integer ATTR_TYPE_MULTILINE = 2;
    public static final Integer ATTR_TYPE_SELECTBOX = 3;
    public static final Integer ATTR_TYPE_RADIO_BUTTON = 4;
    public static final Integer ATTR_TYPE_DATE = 5;
    public static final Integer ATTR_TYPE_MULTISELECT = 6;
    public static final Integer ATTR_TYPE_CURRENCY = 7;

    public static final Integer ATTR_IS_REQUIRED_NA = -1;
    public static final Integer ATTR_IS_REQUIRED_YES = 1;
    public static final Integer ATTR_IS_REQUIRED_NO = 0;

    public static final List<Integer> ATTR_TYPE_OPTION_LIST = Arrays.asList(ATTR_TYPE_STRING, ATTR_TYPE_MULTILINE, 
            ATTR_TYPE_SELECTBOX, ATTR_TYPE_RADIO_BUTTON, ATTR_TYPE_DATE, ATTR_TYPE_CURRENCY);

    private Integer id;
    private String name;
    private String description;
    private Integer type;
    private String attributeOption;
    private List<String> attributeOptions;
    private boolean convertUrl;
    private Integer objectTypeId;
    private String objectKey;
    private boolean isCustomAttr;
    private String url;    // This is for supporting dynamic url for custom fields
    private List<Integer> attrFieldIds;
    private boolean isAttrFieldsEditable;
    private boolean isRequired;
    private boolean isRequiredFieldEditable;
    private Integer defaultAttrFieldId;
    private boolean isDefaultAttrFieldEditable;
    private Integer attributeGroupId;
    private String inputMask;
    private String typeCurrencySymbol;

    public Attribute() {
        type = ATTR_TYPE_STRING;
        objectTypeId = 0;
        defaultAttrFieldId = 0;
        convertUrl = false;
    }

    public void setAttributeOptions(String attributeOption) {
        this.attributeOption = attributeOption;
        attributeOption = attributeOption.replace("\r\n", "\n");
        this.attributeOptions = Arrays.asList(attributeOption.split("\n"));
    }

    public static boolean supportOptions(Integer attrType) {
        if (attrType.equals(ATTR_TYPE_SELECTBOX) || attrType.equals(ATTR_TYPE_RADIO_BUTTON)) {
            return true;
        } else {
            return false;
        }
    }

    public void setConvertUrl(int convertUrl) {
        this.convertUrl = convertUrl == 1;
    }

    //
    // Getters and Setters
    //
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getObjectTypeId() {
        return objectTypeId;
    }
    public void setObjectTypeId(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }
    public String getObjectKey() {
        return objectKey;
    }
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }
    public Integer getDefaultAttrFieldId() {
        return defaultAttrFieldId;
    }
    public void setDefaultAttrFieldId(Integer defaultAttrFieldId) {
        this.defaultAttrFieldId = defaultAttrFieldId;
    }
    public boolean isRequiredFieldEditable() {
        return isRequiredFieldEditable;
    }
    public void setRequiredFieldEditable(boolean requiredFieldEditable) {
        isRequiredFieldEditable = requiredFieldEditable;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<String> getAttributeOptions() {
        return attributeOptions;
    }

    public String getAttributeOption() {
        return attributeOption;
    }

    public void setAttributeOption(String attributeOption) {
        this.attributeOption = attributeOption;
    }

    public boolean isConvertUrl() {
        return convertUrl;
    }

    public void setConvertUrl(boolean convertUrl) {
        this.convertUrl = convertUrl;
    }
    public boolean isCustomAttr() {
        return isCustomAttr;
    }

    public void setCustomAttr(boolean customAttr) {
        isCustomAttr = customAttr;
    }

    public List<Integer> getAttrFieldIds() {
        return attrFieldIds;
    }

    public void setAttrFieldIds(List<Integer> attrFieldIds) {
        this.attrFieldIds = attrFieldIds;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isAttrFieldsEditable() {
        return isAttrFieldsEditable;
    }

    public void setAttrFieldsEditable(boolean attrFieldsEditable) {
        isAttrFieldsEditable = attrFieldsEditable;
    }

    public Integer getAttributeGroupId() {
        return attributeGroupId;
    }

    public void setAttributeGroupId(Integer attributeGroupId) {
        this.attributeGroupId = attributeGroupId;
    }

    public boolean isDefaultAttrFieldEditable() {
        return isDefaultAttrFieldEditable;
    }

    public void setDefaultAttrFieldEditable(boolean defaultAttrFieldEditable) {
        isDefaultAttrFieldEditable = defaultAttrFieldEditable;
    }

    public String getInputMask() {
        return inputMask;
    }

    public void setInputMask(String inputMask) {
        this.inputMask = inputMask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeCurrencySymbol() {
        return typeCurrencySymbol;
    }

    public void setTypeCurrencySymbol(String typeCurrencySymbol) {
        this.typeCurrencySymbol = typeCurrencySymbol;
    }
}
