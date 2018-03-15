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

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.util.DatetimeUtils;

import java.util.Date;

/**
 * Software Object.
 */
public class Software extends BaseObject {

    public static final String NAME = "software_name";
    public static final String DESCRIPTION = "software_description";
    public static final String OWNER_USERNAME = "software_owner_username";
    public static final String OWNER_DISPLAY_NAME = "software_owner_display_name";
    public static final String VERSION = "software_version";
    public static final String CONTRACT_PROVIDER_ID = "contract_provider_id";
    public static final String MANUFACTURER = "software_manufacturer";
    public static final String VENDOR = "software_vendor";
    public static final String TYPE = "software_type";
    public static final String OS = "software_platform";
    public static final String QUOTED_RETAIL_PRICE = "software_quoted_retail_price";
    public static final String QUOTED_OEM_PRICE = "software_quoted_oem_price";
    public static final String EXPIRE_DATE = "software_expire_date";
    public static final String LICENSE_PUCHASED = "license_purchased";
    public static final String LICENSE_INSTALLED = "license_installed";
    public static final String LICENSE_AVAILABLE = "license_available";

    private String name;
    private String description;
    private AccessUser owner;
    private Integer ownerId;
    private Integer type;
    private String typeName;
    private String version;
    private Integer os;
    private String osName;
    private String quotedRetailPrice;
    private String quotedOemPrice;
    private Integer manufacturerId;
    private String manufacturerName;
    private Integer vendorId;
    private String vendorName;
    private Date expireDate;
    private String expireDateY = "";
    private String expireDateM = "";
    private String expireDateD = "";
    private int countLicense;
    private int countFile;
    private int countBookmark;
    private int licensePurchased;
    private int licenseInstalled;
    private int licenseAvailable;
    private Integer fileId;

    public Software() {
        super(ObjectTypes.SOFTWARE);
        type = 0;
        os = 0;
        manufacturerId = 0;
        vendorId = 0;
        licensePurchased = 0;
        licenseInstalled = 0;
        licenseAvailable = 0;
        fileId = 0;
    }

    /**
     * This method checks whether we should show a warning for the given
     * license.
     */
    public static boolean isEnoughLicenses(Object numLicenses) {
        return ((Integer.parseInt(numLicenses.toString())) >= 0);
    }

    public boolean isAttrEmpty(String attrName) {
        if (attrName.equals(Software.OS)) {
            return os == 0;
        } else if (attrName.equals(Software.TYPE)) {
            return type == 0;
        }
        return false;
    }

    public boolean isValidExpireDate() {
        return DatetimeUtils.isValidDate(expireDateY, expireDateM, expireDateD);
    }

    public boolean hasExpireDate() {
        return (!expireDateY.isEmpty() || !expireDateM.isEmpty() || !expireDateD.isEmpty());
    }

    //
    // Getter and Setter
    //
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public void setOs(Integer os) {
        this.os = os;
    }
    public void setQuotedRetailPrice(String quotedRetailPrice) {
        this.quotedRetailPrice = quotedRetailPrice;
    }
    public void setQuotedOemPrice(String quotedOemPrice) {
        this.quotedOemPrice = quotedOemPrice;
    }
    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }
    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Integer getType() {
        return type;
    }
    public Integer getOs() {
        return os;
    }
    public String getQuotedRetailPrice() {
        return quotedRetailPrice;
    }
    public String getQuotedOemPrice() {
        return quotedOemPrice;
    }
    public Integer getManufacturerId() {
        return manufacturerId;
    }
    public String getManufacturerName() {
        return manufacturerName;
    }
    public int getLicensePurchased() {
        return licensePurchased;
    }
    public int getLicenseInstalled() {
        return licenseInstalled;
    }
    public int getLicenseAvailable() {
        return licenseAvailable;
    }
    public Integer getVendorId() {
        return vendorId;
    }
    public String getVendorName() {
        return vendorName;
    }
    public int getCountLicense() {
        return countLicense;
    }
    public int getCountFile() {
        return countFile;
    }
    public int getCountBookmark() {
        return countBookmark;
    }
    public Integer getFileId() {
        return fileId;
    }
    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public void setCountLicense(int countLicense) {
        this.countLicense = countLicense;
    }
    public void setCountFile(int countFile) {
        this.countFile = countFile;
    }
    public void setCountBookmark(int countBookmark) {
        this.countBookmark = countBookmark;
    }
    public void setLicensePurchased(int licensePurchased) {
        this.licensePurchased = licensePurchased;
    }
    public void setLicenseInstalled(int licenseInstalled) {
        this.licenseInstalled = licenseInstalled;
    }
    public void setLicenseAvailable(int licenseAvailable) {
        this.licenseAvailable = licenseAvailable;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getOsName() {
        return osName;
    }
    public void setOsName(String osName) {
        this.osName = osName;
    }

    public AccessUser getOwner() {
        return owner;
    }

    public void setOwner(AccessUser owner) {
        this.owner = owner;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getExpireDateY() {
        return expireDateY;
    }

    public void setExpireDateY(String expireDateY) {
        this.expireDateY = expireDateY;
    }

    public String getExpireDateM() {
        return expireDateM;
    }

    public void setExpireDateM(String expireDateM) {
        this.expireDateM = expireDateM;
    }

    public String getExpireDateD() {
        return expireDateD;
    }

    public void setExpireDateD(String expireDateD) {
        this.expireDateD = expireDateD;
    }
}
