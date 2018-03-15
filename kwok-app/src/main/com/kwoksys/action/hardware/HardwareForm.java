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
package com.kwoksys.action.hardware;

import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.framework.http.RequestContext;

/**
 * ActionForm class adding/editing hardware.
 */
public class HardwareForm extends BaseObjectForm {

    private Integer hardwareId;
    private Integer copyHardwareId;
    private String hardwareName;
    private String hardwareModelName;
    private String hardwareModelNumber;
    private String serialNumber;
    private String hardwareCost;
    private int lastServicedOn;
    private String purchaseDate;
    private String purchaseMonth;
    private String purchaseYear;
    private String warrantyDate;
    private String warrantyMonth;
    private String warrantyYear;
    private String warrantyDuration;
    private Integer hardwareLocation;
    private String hardwareDescription;
    private Integer hardwareType;
    private Integer hardwareStatus;
    private Integer hardwareOwner;
    private Integer manufacturerId;
    private Integer vendorId;
    private String issueId;
    private String contactId;
    private String formContactId;
    private String relationshipDescription;

    @Override
    public void setRequest(RequestContext requestContext) {
        hardwareId = requestContext.getParameterInteger("hardwareId");
        copyHardwareId = requestContext.getParameterInteger("copyHardwareId");
        hardwareName = requestContext.getParameterString("hardwareName");
        hardwareModelName = requestContext.getParameterString("hardwareModelName");
        hardwareModelNumber = requestContext.getParameterString("hardwareModelNumber");
        serialNumber = requestContext.getParameterString("serialNumber");
        hardwareCost = requestContext.getParameterString("hardwareCost");
        lastServicedOn = requestContext.getParameter("lastServicedOn");
        purchaseDate = requestContext.getParameterString("purchaseDate");
        purchaseMonth = requestContext.getParameterString("purchaseMonth");
        purchaseYear = requestContext.getParameterString("purchaseYear");
        warrantyDate = requestContext.getParameterString("warrantyDate");
        warrantyMonth = requestContext.getParameterString("warrantyMonth");
        warrantyYear = requestContext.getParameterString("warrantyYear");
        warrantyDuration = requestContext.getParameterString("warrantyDuration");
        hardwareLocation = requestContext.getParameterInteger("hardwareLocation");
        hardwareDescription = requestContext.getParameterString("hardwareDescription");
        hardwareType = requestContext.getParameterInteger("hardwareType");
        hardwareStatus = requestContext.getParameterInteger("hardwareStatus");
        hardwareOwner = requestContext.getParameterInteger("hardwareOwner");
        manufacturerId = requestContext.getParameterInteger("manufacturerId");
        vendorId = requestContext.getParameterInteger("vendorId");
        issueId = requestContext.getParameterString("issueId");
        contactId = requestContext.getParameterString("contactId");
        formContactId = requestContext.getParameterString("formContactId");
        relationshipDescription = requestContext.getParameterString("relationshipDescription");
    }

    public void setHardware(Hardware hardware) {
        hardwareName = hardware.getName();
        hardwareDescription = hardware.getDescription();
        hardwareOwner = hardware.getOwnerId();
        hardwareType = hardware.getType();
        hardwareStatus = hardware.getStatus();
        hardwareModelName = hardware.getModelName();
        hardwareModelNumber = hardware.getModelNumber();
        serialNumber = hardware.getSerialNumber();
        hardwareCost = hardware.getPurchasePrice();
        lastServicedOn = hardware.getResetLastServiceDate();
        hardwareLocation = hardware.getLocation();
        manufacturerId = hardware.getManufacturerId();
        vendorId = hardware.getVendorId();
        purchaseYear = hardware.getPurchaseYear();
        purchaseMonth = hardware.getPurchaseMonth();
        purchaseDate = hardware.getPurchaseDate();
        warrantyYear = hardware.getWarrantyYear();
        warrantyMonth = hardware.getWarrantyMonth();
        warrantyDate = hardware.getWarrantyDate();
        warrantyDuration = "";
    }

    public Integer getHardwareId() {
        return hardwareId;
    }
    public void setHardwareId(Integer hardwareId) {
        this.hardwareId = hardwareId;
    }
    public String getHardwareName() {
        return hardwareName;
    }

    public String getHardwareModelName() {
        return hardwareModelName;
    }

    public String getHardwareModelNumber() {
        return hardwareModelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getHardwareCost() {
        return hardwareCost;
    }

    public int getLastServicedOn() {
        return lastServicedOn;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getPurchaseMonth() {
        return purchaseMonth;
    }

    public String getPurchaseYear() {
        return purchaseYear;
    }

    public String getWarrantyDate() {
        return warrantyDate;
    }

    public String getWarrantyYear() {
        return warrantyYear;
    }

    public String getWarrantyMonth() {
        return warrantyMonth;
    }

    public Integer getHardwareLocation() {
        return hardwareLocation;
    }

    public String getHardwareDescription() {
        return hardwareDescription;
    }

    public Integer getHardwareType() {
        return hardwareType;
    }

    public Integer getHardwareStatus() {
        return hardwareStatus;
    }

    public Integer getHardwareOwner() {
        return hardwareOwner;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public Integer getCopyHardwareId() {
        return copyHardwareId;
    }

    public String getWarrantyDuration() {
        return warrantyDuration;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getContactId() {
        return contactId;
    }

    public String getRelationshipDescription() {
        return relationshipDescription;
    }

    public String getFormContactId() {
        return formContactId;
    }
}