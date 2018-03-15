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

import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;

/**
 * Asset Map Object.
 */
public class HardwareSoftwareMap {

    private Integer mapId;
    private Integer softwareId;
    private Integer hardwareId;
    private Integer licenseId;
    private int licenseEntitlement;

    private Software software;
    private SoftwareLicense license;

    public HardwareSoftwareMap() {
        licenseId = 0;
        licenseEntitlement = 1;
        software = new Software();
        license = new SoftwareLicense();
    }

    //
    // Getters and setters
    //
    public Integer getMapId() {
        return mapId;
    }
    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }
    public Integer getSoftwareId() {
        return softwareId;
    }
    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }
    public Integer getHardwareId() {
        return hardwareId;
    }
    public void setHardwareId(Integer hardwareId) {
        this.hardwareId = hardwareId;
    }
    public Integer getLicenseId() {
        return licenseId;
    }
    public void setLicenseId(Integer licenseId) {
        this.licenseId = licenseId;
    }
    public int getLicenseEntitlement() {
        return licenseEntitlement;
    }
    public void setLicenseEntitlement(int licenseEntitlement) {
        this.licenseEntitlement = licenseEntitlement;
    }
    public Software getSoftware() {
        return software;
    }
    public void setSoftware(Software software) {
        this.software = software;
    }
    public SoftwareLicense getLicense() {
        return license;
    }
    public void setLicense(SoftwareLicense license) {
        this.license = license;
    }
}
