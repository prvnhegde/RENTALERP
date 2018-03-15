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
package com.kwoksys.action.software;

import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;

/**
 * Action class for adding/editing Software.
 */
public class SoftwareForm extends BaseObjectForm {

    private Integer softwareId;
    private String softwareName;
    private String softwareDescription;
    private Integer softwareOwner;
    private Integer softwareType;
    private Integer softwareOS;
    private String retailPrice;
    private String oemPrice;
    private Integer manufacturerId;
    private Integer vendorId;
    private String version;
    private String expireDateY;
    private String expireDateM;
    private String expireDateD;    

    @Override
    public void setRequest(RequestContext requestContext) {
        softwareId = requestContext.getParameterInteger("softwareId");
        softwareName = requestContext.getParameterString("softwareName");
        softwareDescription = requestContext.getParameterString("softwareDescription");
        softwareOwner = requestContext.getParameterInteger("softwareOwner");
        softwareType = requestContext.getParameterInteger("softwareType");
        softwareOS = requestContext.getParameterInteger("softwareOS");
        retailPrice = requestContext.getParameterString("retailPrice");
        oemPrice = requestContext.getParameterString("oemPrice");
        manufacturerId = requestContext.getParameterInteger("manufacturerId");
        vendorId = requestContext.getParameterInteger("vendorId");
        version = requestContext.getParameterString("version");
        expireDateY = requestContext.getParameterString("expireDateY");
        expireDateM = requestContext.getParameterString("expireDateM");
        expireDateD = requestContext.getParameterString("expireDateD");
    }

    public void setSoftware(Software software) {
        softwareName = software.getName();
        softwareDescription = software.getDescription();
        softwareOwner = software.getOwner() != null ? software.getOwner().getId() : 0;
        softwareType = software.getType();
        softwareOS = software.getOs();
        version = software.getVersion();
        retailPrice = software.getQuotedRetailPrice();
        oemPrice = software.getQuotedOemPrice();
        manufacturerId = software.getManufacturerId();
        vendorId = software.getVendorId();

        expireDateY = DatetimeUtils.toYearString(software.getExpireDate());
        expireDateM = DatetimeUtils.toMonthString(software.getExpireDate());
        expireDateD = DatetimeUtils.toDateString(software.getExpireDate());
    }

    public Integer getSoftwareId() {
        return softwareId;
    }
    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }
    public String getSoftwareName() {
        return softwareName;
    }

    public String getSoftwareDescription() {
        return softwareDescription;
    }

    public Integer getSoftwareType() {
        return softwareType;
    }

    public Integer getSoftwareOS() {
        return softwareOS;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public String getOemPrice() {
        return oemPrice;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }
    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }
    public Integer getVendorId() {
        return vendorId;
    }

    public Integer getSoftwareOwner() {
        return softwareOwner;
    }

    public String getExpireDateY() {
        return expireDateY;
    }

    public String getExpireDateM() {
        return expireDateM;
    }

    public String getExpireDateD() {
        return expireDateD;
    }

    public String getVersion() {
        return version;
    }
}