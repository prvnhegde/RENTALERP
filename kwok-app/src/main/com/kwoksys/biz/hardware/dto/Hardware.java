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

import com.kwoksys.action.hardware.HardwareForm;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.util.CurrencyUtils;
import com.kwoksys.framework.util.DatetimeUtils;

import java.util.Date;

/**
 * Hardware Object.
 */
public class Hardware extends BaseObject {

    public static final String ID = "hardware_id";
    public static final String HARDWARE_NAME = "hardware_name";
    public static final String HARDWARE_DESCRIPTION = "hardware_description";
    public static final String MODEL_NAME = "hardware_model_name";
    public static final String MODEL_NUMBER = "hardware_model_number";
    public static final String SERIAL_NUMBER = "hardware_serial_number";
    public static final String SERVICE_DATE = "hardware_last_service_date";
    public static final String TYPE = "hardware_type";
    public static final String STATUS = "hardware_status";
    public static final String LOCATION = "hardware_location";
    public static final String PURCHASE_PRICE = "hardware_purchase_price";
    public static final String PURCHASE_DATE = "hardware_purchase_date";
    public static final String WARRANTY_EXPIRATION = "hardware_warranty_expire_date";
    public static final String MANUFACTURER_NAME = "hardware_manufacturer_name";
    public static final String VENDOR_NAME = "hardware_vendor_name";
    public static final String OWNER_NAME = "hardware_owner_name";
    public static final String OWNER_USERNAME = "hardware_owner_username";
    public static final String OWNER_DISPLAY_NAME = "hardware_owner_display_name";
    public static final String OWNER_ID = "hardware_owner_id";

    private String name;
    private String serialNumber;
    private String modelName;
    private String modelNumber;
    private String description;
    private Integer ownerId;
    private Integer manufacturerId;
    private String manufacturerName;
    private Integer vendorId;
    private String vendorName;
    private Date hardwarePurchaseDate;
    private String purchaseDate = "";
    private String purchaseMonth = "";
    private String purchaseYear = "";
    private Date warrantyExpireDate;
    private String warrantyDate = "";
    private String warrantyMonth = "";
    private String warrantyYear = "";
    private Date lastServicedOn;
    private int resetLastServiceDate;
    private String purchasePrice;
    private double purchasePriceRaw;
    private Integer location;
    private Integer type;
    private Integer status;
    private int countSoftware;
    private int countComponent;
    private int countFile;
    private Integer fileId;
    private boolean validHardwareCost = true;

    public Hardware() throws DatabaseException {
        super(ObjectTypes.HARDWARE);
        name = "";
        modelName = "";
        modelNumber = "";
        serialNumber = "";
        manufacturerId = 0;
        vendorId = 0;
        resetLastServiceDate = 0;
        location = 0;
        type = new CacheManager().getSystemAttrCache(Attributes.HARDWARE_TYPE).getDefaultAttrFieldId();
        status = 0;
        countSoftware = 0;
        countComponent = 0;
        countFile = 0;
        fileId = 0;
    }

    public void setForm(HardwareForm actionForm) {
        setName(actionForm.getHardwareName());
        setDescription(actionForm.getHardwareDescription());
        setOwnerId(actionForm.getHardwareOwner());
        setManufacturerId(actionForm.getManufacturerId());
        setVendorId(actionForm.getVendorId());
        setType(actionForm.getHardwareType());
        setStatus(actionForm.getHardwareStatus());
        setModelName(actionForm.getHardwareModelName());
        setModelNumber(actionForm.getHardwareModelNumber());
        setSerialNumber(actionForm.getSerialNumber());
        setPurchasePrice(actionForm.getHardwareCost());
        setResetLastServiceDate(actionForm.getLastServicedOn());
        setHardwarePurchaseDate(actionForm.getPurchaseYear(), actionForm.getPurchaseMonth(), actionForm.getPurchaseDate());
        setHardwareWarrantyExpireDate(actionForm.getWarrantyYear(), actionForm.getWarrantyMonth(), actionForm.getWarrantyDate());
        setLocation(actionForm.getHardwareLocation());        
    }

    public void setHardwarePurchaseDate(String fullDate) {
        if (!fullDate.isEmpty()) {
            String[] dateArray = fullDate.split("-");

            purchaseYear = dateArray[0];
            purchaseMonth = dateArray[1];
            purchaseDate = dateArray[2];
        } else {
            purchaseYear = "";
            purchaseMonth = "";
            purchaseDate = "";
        }
    }

    public void setHardwarePurchaseDate(String year, String month, String date) {
        purchaseYear = year;
        purchaseMonth = month;
        purchaseDate = date;
    }

    public boolean hasHardwarePurchaseDate() {
        return (!purchaseYear.isEmpty() || !purchaseMonth.isEmpty() || !purchaseDate.isEmpty());
    }

    /**
     * Return whether purchase date is in valid format.
     *
     * @return ..
     */
    public boolean isValidPurchaseDate() {
        return DatetimeUtils.isValidDate(purchaseYear, purchaseMonth, purchaseDate);
    }

    public void setHardwareWarrantyExpireDate(String fullDate) {
        if (!fullDate.isEmpty()) {
            String[] dateArray = fullDate.split("-");

            warrantyYear = dateArray[0];
            warrantyMonth = dateArray[1];
            warrantyDate = dateArray[2];
        } else {
            warrantyYear = "";
            warrantyMonth = "";
            warrantyDate = "";
        }
    }
    public void setHardwareWarrantyExpireDate(String year, String month, String date) {
        warrantyYear = year;
        warrantyMonth = month;
        warrantyDate = date;
    }

    public boolean hasHardwareWarrantyExpireDate() {
        return (!warrantyYear.isEmpty() || !warrantyMonth.isEmpty() || !warrantyDate.isEmpty());
    }

    /**
     * Return whether warranty expiration date is in valid format.
     *
     * @return ..
     */
    public boolean isValidWarrantyExpireDate() {
        return DatetimeUtils.isValidDate(warrantyYear, warrantyMonth, warrantyDate);
    }

    public boolean isAttrEmpty(String attrName) {
        if (attrName.equals(Hardware.STATUS)) {
            return status == 0;

        } else if (attrName.equals(Hardware.LOCATION)) {
            return location == 0;

        } else if (attrName.equals(Hardware.TYPE)) {
            return type == 0;
        }
        return false;
    }

    public String getWarrantyExpireDateString() {
        return DatetimeUtils.createDatetimeString(warrantyYear, warrantyMonth, warrantyDate);
    }

    public String getHardwarePurchaseDateString() {
        return DatetimeUtils.createDatetimeString(purchaseYear, purchaseMonth, purchaseDate);
    }

    /**
     * Checks whether hardwareCost is in valid format.
     *
     * @return ..
     */
    public boolean isValidHardwareCost() {
        return validHardwareCost;
    }

    public void setPurchasePrice(String purchasePrice) {
        if (!purchasePrice.isEmpty()) {
            purchasePrice = purchasePrice.replace(",", "");
            if (CurrencyUtils.isValidFormat(purchasePrice)) {
                this.purchasePriceRaw = Double.parseDouble(purchasePrice);
            } else {
                validHardwareCost = false;
            }
            this.purchasePrice = purchasePrice;
        } else {
            this.purchasePriceRaw = 0;
        }
    }

    //
    // Getter and Setter
    //
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getModelNumber() {
        return modelNumber;
    }
    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLastServicedOn() {
        return DatetimeUtils.toLocalDate(lastServicedOn);
    }
    public void setLastServicedOn(Date lastServicedOn) {
        this.lastServicedOn = lastServicedOn;
    }

    public Date getHardwarePurchaseDate() {
        return hardwarePurchaseDate;
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
    public double getPurchasePriceRaw() {
        return purchasePriceRaw;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public Integer getLocation() {
        return location;
    }
    public void setLocation(Integer location) {
        this.location = location;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getManufacturerId() {
        return manufacturerId;
    }
    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }
    public String getManufacturerName() {
        return manufacturerName;
    }
    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public Integer getVendorId() {
        return vendorId;
    }
    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }
    public String getVendorName() {
        return vendorName;
    }
    public int getResetLastServiceDate() {
        return resetLastServiceDate;
    }
    public void setResetLastServiceDate(int resetLastServiceDate) {
        this.resetLastServiceDate = resetLastServiceDate;
    }
    public int getCountSoftware() {
        return countSoftware;
    }
    public int getCountFile() {
        return countFile;
    }
    public Integer getFileId() {
        return fileId;
    }
    public void setHardwarePurchaseDate(Date hardwarePurchaseDate) {
        this.hardwarePurchaseDate = hardwarePurchaseDate;
    }
    public void setCountSoftware(int countSoftware) {
        this.countSoftware = countSoftware;
    }
    public void setCountFile(int countFile) {
        this.countFile = countFile;
    }
    public int getCountComponent() {
        return countComponent;
    }
    public void setCountComponent(int countComponent) {
        this.countComponent = countComponent;
    }

    public Date getWarrantyExpireDate() {
        return warrantyExpireDate;
    }

    public void setWarrantyExpireDate(Date warrantyExpireDate) {
        this.warrantyExpireDate = warrantyExpireDate;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}
