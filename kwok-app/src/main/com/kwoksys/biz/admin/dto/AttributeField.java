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

/**
 * AttributeField class.
 */
public class AttributeField {

    public static final String NAME = "attribute_field_name";
    public static final String IS_DISABLED = "is_disabled";

    private Integer id;
    private String name;
    private String description;
    private Integer attributeId;
    private Integer iconId;
    private String iconPath;
    private List<Integer> linkedAttrIds;

    // We want to be able to tell whether an icon is provided by default or added by users
    private boolean isSystemIcon;
    private boolean isDisabled;

    public AttributeField() {
        id = 0;
        attributeId = 0;
        iconId = 0;
        isSystemIcon = false;
        isDisabled = false;
    }

    public void setDisabled(int disabled) {
        isDisabled = disabled == 1;
    }

    //
    // Getter and Setter
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }
    public Integer getIconId() {
        return iconId;
    }
    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }
    public String getIconPath() {
        return iconPath;
    }
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
    public boolean isSystemIcon() {
        return isSystemIcon;
    }
    public void setSystemIcon(boolean systemIcon) {
        isSystemIcon = systemIcon;
    }
    public boolean isDisabled() {
        return isDisabled;
    }
    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public List<Integer> getLinkedAttrIds() {
        return linkedAttrIds;
    }

    public void setLinkedAttrIds(List<Integer> linkedAttrIds) {
        this.linkedAttrIds = linkedAttrIds;
    }
}
