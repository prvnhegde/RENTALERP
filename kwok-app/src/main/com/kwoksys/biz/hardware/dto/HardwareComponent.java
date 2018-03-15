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
package com.kwoksys.biz.hardware.dto;

import com.kwoksys.biz.base.BaseObject;

/**
 * HardwareComponent
 */
public class HardwareComponent extends BaseObject {

    public static final String COMP_NAME = "attribute_field_name";

    private Integer hardwareId;
    private Integer id;
    // This is a system field
    private Integer type;
    private String typeName;
    private String description;

    public Integer getHardwareId() {
        return hardwareId;
    }
    public void setHardwareId(Integer hardwareId) {
        this.hardwareId = hardwareId;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
}
