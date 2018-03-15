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

import com.kwoksys.biz.system.core.AppPaths;

/**
 * Icon
 */
public class Icon {

    private Integer id;
    private String path;
    private boolean isSystemIcon;
    private Integer attributeId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }
    public boolean isSystemIcon() {
        return isSystemIcon;
    }
    public void setSystemIcon(boolean systemIcon) {
        isSystemIcon = systemIcon;
    }
    public String getPath() {
        return path;
    }
    public void setAppPath(String path) {
        this.path = AppPaths.ROOT + path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}
