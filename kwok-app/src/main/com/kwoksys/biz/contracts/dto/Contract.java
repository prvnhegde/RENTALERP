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
package com.kwoksys.biz.contracts.dto;

import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.base.BaseObject;

import java.util.Date;

/**
 * Contract object
 */
public class Contract extends BaseObject {

    public static final String ID = "contract_id";
    public static final String NAME = "contract_name";
    public static final String DESCRIPTION = "contract_description";
    public static final String TYPE = "contract_type";
    public static final String STAGE = "contract_stage";
    public static final String CONTRACT_OWNER_ID = "contract_owner_id";
    public static final String OWNER = "contract_owner";
    public static final String CONTRACT_OWNER_USERNAME = "contract_owner_username";
    public static final String CONTRACT_OWNER_DISPLAY_NAME = "contract_owner_display_name";
    public static final String CONTRACT_PROVIDER_ID = "contract_provider_id";
    public static final String CONTRACT_PROVIDER_NAME = "contract_provider_name";
    public static final String CONTRACT_EXPIRE_DATE = "contract_expiration_date";
    public static final String CONTRACT_EFFECT_DATE = "contract_effective_date";
    public static final String RENEWAL_TYPE = "contract_renewal_type";
    public static final String CONTRACT_RENEWAL_DATE = "contract_renewal_date";

    private Integer id;
    private String name;
    private String description;
    private Integer type;
    private Integer stage;
    private String typeName;
    private Date effectiveDate;
    private String effectiveDateDate = "";
    private String effectiveDateMonth = "";
    private String effectiveDateYear = "";
    private Date expireDate;
    private String expireDateDate = "";
    private String expireDateMonth = "";
    private String expireDateYear = "";
    private Integer renewalType;
    private String renewalTypeName;
    private Date renewalDate;
    private String renewalDateDate = "";
    private String renewalDateMonth = "";
    private String renewalDateYear = "";
    private Integer contractProviderId;
    private String contractProviderName;
    private Integer ownerId;
    private Integer contractProviderContactId;
    private String contractProviderContactName;

    public Contract() {
        super(ObjectTypes.CONTRACT);
        type = 0;
        stage = 0;
        renewalType = 0;
        contractProviderId = 0;
    }

    public boolean hasContractEffectiveDate() {
        return (!effectiveDateYear.isEmpty() || !effectiveDateMonth.isEmpty() || !effectiveDateDate.isEmpty());
    }

    public boolean isValidContractEffectiveDate() {
        return DatetimeUtils.isValidDate(effectiveDateYear, effectiveDateMonth, effectiveDateDate);
    }

    public boolean hasContractExpirationDate() {
        return (!expireDateYear.isEmpty() || !expireDateMonth.isEmpty() || !expireDateDate.isEmpty());
    }

    public boolean isValidContractExpirationDate() {
        return DatetimeUtils.isValidDate(expireDateYear, expireDateMonth, expireDateDate);
    }

    public boolean hasContractRenewalDate() {
        return (!renewalDateYear.isEmpty() || !renewalDateMonth.isEmpty() || !renewalDateDate.isEmpty());
    }

    public boolean isValidContractRenewalDate() {
        return DatetimeUtils.isValidDate(renewalDateYear, renewalDateMonth, renewalDateDate);
    }

    public boolean isAttrEmpty(String attrName) {
        if (attrName.equals(Contract.TYPE)) {
            return type == 0;

        } else if (attrName.equals(Contract.RENEWAL_TYPE)) {
            return renewalType == 0;
        }
        return false;
    }

    //
    // Getters and Setters
    //
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    public Integer getRenewalType() {
        return renewalType;
    }
    public void setRenewalType(Integer renewalType) {
        this.renewalType = renewalType;
    }
    public Date getRenewalDate() {
        return renewalDate;
    }
    public void setRenewalDate(Date renewalDate) {
        this.renewalDate = renewalDate;
    }
    public String getEffectiveDateDate() {
        return effectiveDateDate;
    }
    public void setEffectiveDateDate(String effectiveDateDate) {
        this.effectiveDateDate = effectiveDateDate;
    }
    public String getEffectiveDateMonth() {
        return effectiveDateMonth;
    }
    public void setEffectiveDateMonth(String effectiveDateMonth) {
        this.effectiveDateMonth = effectiveDateMonth;
    }
    public String getEffectiveDateYear() {
        return effectiveDateYear;
    }
    public void setEffectiveDateYear(String effectiveDateYear) {
        this.effectiveDateYear = effectiveDateYear;
    }
    public String getExpireDateDate() {
        return expireDateDate;
    }
    public void setExpireDateDate(String expireDateDate) {
        this.expireDateDate = expireDateDate;
    }
    public String getExpireDateMonth() {
        return expireDateMonth;
    }
    public void setExpireDateMonth(String expireDateMonth) {
        this.expireDateMonth = expireDateMonth;
    }
    public String getExpireDateYear() {
        return expireDateYear;
    }
    public void setExpireDateYear(String expireDateYear) {
        this.expireDateYear = expireDateYear;
    }
    public String getRenewalDateDate() {
        return renewalDateDate;
    }
    public void setRenewalDateDate(String renewalDateDate) {
        this.renewalDateDate = renewalDateDate;
    }
    public String getRenewalDateMonth() {
        return renewalDateMonth;
    }
    public void setRenewalDateMonth(String renewalDateMonth) {
        this.renewalDateMonth = renewalDateMonth;
    }
    public String getRenewalDateYear() {
        return renewalDateYear;
    }
    public void setRenewalDateYear(String renewalDateYear) {
        this.renewalDateYear = renewalDateYear;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getRenewalTypeName() {
        return renewalTypeName;
    }
    public void setRenewalTypeName(String renewalTypeName) {
        this.renewalTypeName = renewalTypeName;
    }
    public Integer getContractProviderId() {
        return contractProviderId;
    }
    public void setContractProviderId(Integer contractProviderId) {
        this.contractProviderId = contractProviderId;
    }
    public String getContractProviderName() {
        return contractProviderName;
    }
    public void setContractProviderName(String contractProviderName) {
        this.contractProviderName = contractProviderName;
    }

    public Integer getContractProviderContactId() {
        return contractProviderContactId;
    }

    public void setContractProviderContactId(Integer contractProviderContactId) {
        this.contractProviderContactId = contractProviderContactId;
    }

    public String getContractProviderContactName() {
        return contractProviderContactName;
    }

    public void setContractProviderContactName(String contractProviderContactName) {
        this.contractProviderContactName = contractProviderContactName;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }
}
