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

import com.kwoksys.action.reports.ReportForm;
import com.kwoksys.framework.http.RequestContext;

import java.util.List;

/**
 * ActionForm for Hardware index page.
 */
public class HardwareSearchForm extends ReportForm {

    private String cmd;
    private String hardwareId;
    private String hardwareName;
    private String description;
    private String hardwareModelName;
    private String hardwareModelNumber;
    private String serialNumber;
    private String hardwareOwner;
    private List<Integer> hardwareTypes;
    private Integer hardwareType;
    private String hardwareTypeFilter;
    private List<Integer> hardwareStatus;
    private List<Integer> hardwareLocation;
    private String hardwareNameCriteria;
    private Integer manufacturerId;
    private Integer vendorId;
    private String attrId;
    private String attrValue;
    private String compTypeId;
    private String compValue;
    private String purchasedAfterMonth;
    private String purchasedAfterDate;
    private String purchasedAfterYear;
    private String purchasedBeforeMonth;
    private String purchasedBeforeDate;
    private String purchasedBeforeYear;
    private List<Integer> hardwareIds;
    private String customerName;
    private String rentalstartdateDate;
    private String rentalstartdateMonth;
    private String rentalstartdateYear;
    private String rentalenddateDate;
    private String rentalenddateMonth;
    private String rentalenddateYear;
    private String returnDate;
    private String returnMonth;
    private String returnYear;
    
    @Override
    public void setRequest(RequestContext requestContext) {
        cmd = requestContext.getParameterString("cmd");
        hardwareId = requestContext.getParameterString("hardwareId");
        hardwareName = requestContext.getParameterString("hardwareName");
        description = requestContext.getParameterString("description");
        hardwareModelName = requestContext.getParameterString("hardwareModelName");
        hardwareModelNumber = requestContext.getParameterString("hardwareModelNumber");
        serialNumber = requestContext.getParameterString("serialNumber");
        hardwareOwner = requestContext.getParameterString("hardwareOwner");
        hardwareTypes = requestContext.getParameters("hardwareTypes");
        hardwareType = requestContext.getParameterInteger("hardwareType");
        hardwareTypeFilter = requestContext.getParameterString("hardwareTypeFilter");
        hardwareStatus = requestContext.getParameters("hardwareStatus");
        hardwareLocation = requestContext.getParameters("hardwareLocation");
        hardwareNameCriteria = requestContext.getParameterString("hardwareNameCriteria");
        manufacturerId = requestContext.getParameterInteger("manufacturerId");
        vendorId = requestContext.getParameterInteger("vendorId");
        attrId = requestContext.getParameterString("attrId");
        attrValue = requestContext.getParameterString("attrValue");
        compTypeId = requestContext.getParameterString("compTypeId");
        compValue = requestContext.getParameterString("compValue");
        purchasedAfterMonth = requestContext.getParameterString("purchasedAfterMonth");
        purchasedAfterDate = requestContext.getParameterString("purchasedAfterDate");
        purchasedAfterYear = requestContext.getParameterString("purchasedAfterYear");
        purchasedBeforeMonth = requestContext.getParameterString("purchasedBeforeMonth");
        purchasedBeforeDate = requestContext.getParameterString("purchasedBeforeDate");
        purchasedBeforeYear = requestContext.getParameterString("purchasedBeforeYear");
        hardwareIds = requestContext.getParameters("hardwareIds");
        customerName = requestContext.getParameterString("customerName");
        rentalstartdateDate = requestContext.getParameterString("rentalstartdateDate");
        rentalstartdateMonth = requestContext.getParameterString("rentalstartdateMonth");
        rentalstartdateYear = requestContext.getParameterString("rentalstartdateYear");
        rentalenddateDate  = requestContext.getParameterString("rentalenddateDate");
        rentalenddateMonth = requestContext.getParameterString("rentalenddateMonth");
        rentalenddateYear = requestContext.getParameterString("rentalenddateYear");
        returnDate = requestContext.getParameterString("returnDate");
        returnMonth = requestContext.getParameterString("returnMonth");
        returnYear = requestContext.getParameterString("returnYear");
         
    }

    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getHardwareId() {
        return hardwareId;
    }
    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }
    public String getHardwareName() {
        return hardwareName;
    }

    public String getDescription() {
        return description;
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

    public String getHardwareOwner() {
        return hardwareOwner;
    }

    public List<Integer> getHardwareTypes() {
        return hardwareTypes;
    }

    public void setHardwareTypes(List<Integer> hardwareTypes) {
        this.hardwareTypes = hardwareTypes;
    }

    public List<Integer> getHardwareStatus() {
        return hardwareStatus;
    }

    public List<Integer> getHardwareLocation() {
        return hardwareLocation;
    }

    public String getHardwareNameCriteria() {
        return hardwareNameCriteria;
    }

    public String getAttrId() {
        return attrId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public Integer getHardwareType() {
        return hardwareType;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public String getCompTypeId() {
        return compTypeId;
    }

    public String getCompValue() {
        return compValue;
    }

    public String getPurchasedAfterMonth() {
        return purchasedAfterMonth;
    }

    public String getPurchasedAfterDate() {
        return purchasedAfterDate;
    }

    public String getPurchasedAfterYear() {
        return purchasedAfterYear;
    }

    public String getPurchasedBeforeMonth() {
        return purchasedBeforeMonth;
    }

    public String getPurchasedBeforeDate() {
        return purchasedBeforeDate;
    }

    public String getPurchasedBeforeYear() {
        return purchasedBeforeYear;
    }

    public void setHardwareType(Integer hardwareType) {
        this.hardwareType = hardwareType;
    }

    public void setHardwareStatus(List<Integer> hardwareStatus) {
        this.hardwareStatus = hardwareStatus;
    }

    public void setHardwareLocation(List<Integer> hardwareLocation) {
        this.hardwareLocation = hardwareLocation;
    }

    public String getHardwareTypeFilter() {
        return hardwareTypeFilter;
    }

    public void setHardwareTypeFilter(String hardwareTypeFilter) {
        this.hardwareTypeFilter = hardwareTypeFilter;
    }

    public List<Integer> getHardwareIds() {
        return hardwareIds;
    }
    
    public String getCustomerName() {
        return customerName;
    }

	public String getRentalstartdateDate() {
		return rentalstartdateDate;
	}

	public String getRentalstartdateMonth() {
		return rentalstartdateMonth;
	}

	public String getRentalstartdateYear() {
		return rentalstartdateYear;
	}

	public String getRentalenddateDate() {
		return rentalenddateDate;
	}

	public String getRentalenddateMonth() {
		return rentalenddateMonth;
	}

	public String getRentalenddateYear() {
		return rentalenddateYear;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public String getReturnMonth() {
		return returnMonth;
	}

	public String getReturnYear() {
		return returnYear;
	}


}
