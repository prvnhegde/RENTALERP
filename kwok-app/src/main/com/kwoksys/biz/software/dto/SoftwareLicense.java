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
package com.kwoksys.biz.software.dto;

import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.biz.base.BaseObject;

/**
 * Software License Object.
 */
public class SoftwareLicense extends BaseObject {

    public static final String LICENSE_KEY = "license_key";
    public static final String LICENSE_NOTE = "license_note";
    public static final String LICENSE_COUNT = "license_count";
    public static final String LICENSE_PUCHASED = "license_purchased";
    public static final String LICENSE_INSTALLED = "license_installed";
    public static final String LICENSE_AVAILABLE = "license_available";

    private Integer id;
    /** Corresponds to asset_software_licenses.license_key column **/
    private String key;
    private String note;
    private int entitlement;
    private boolean correctLicenseEntitlementFormat;
    private Integer softwareId;

    public SoftwareLicense() {
        entitlement = 0;
        correctLicenseEntitlementFormat = false;
    }

    //
    // Getters and setters
    //
    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setEntitlement(String entitlement) {
        if (NumberUtils.isNonNegativeInteger(entitlement)) {
            this.entitlement = NumberUtils.replaceNull(entitlement);
            correctLicenseEntitlementFormat = true;
        }
    }

    public void setEntitlement(int entitlement) {
        this.entitlement = entitlement;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }
    public Integer getId() {
        return id;
    }
    public String getKey() {
        return key;
    }
    public String getNote() {
        return note;
    }
    public int getEntitlement() {
        return entitlement;
    }
    public boolean isCorrectLicenseEntitlementFormat() {
        return correctLicenseEntitlementFormat;
    }
}
