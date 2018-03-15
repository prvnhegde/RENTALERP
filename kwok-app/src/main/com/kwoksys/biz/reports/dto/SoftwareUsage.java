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
package com.kwoksys.biz.reports.dto;

import com.kwoksys.biz.software.dto.Software;

/**
 * SoftwareUsage
 */
public class SoftwareUsage extends Software {

    private Integer hardwareId;

    private String hardwareName;

    private Integer hardwareOwnerId;

    private String hardwareOwnerName;

    public Integer getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(Integer hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getHardwareName() {
        return hardwareName;
    }

    public void setHardwareName(String hardwareName) {
        this.hardwareName = hardwareName;
    }

    public Integer getHardwareOwnerId() {
        return hardwareOwnerId;
    }

    public void setHardwareOwnerId(Integer hardwareOwnerId) {
        this.hardwareOwnerId = hardwareOwnerId;
    }

    public String getHardwareOwnerName() {
        return hardwareOwnerName;
    }

    public void setHardwareOwnerName(String hardwareOwnerName) {
        this.hardwareOwnerName = hardwareOwnerName;
    }
}
