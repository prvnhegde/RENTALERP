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
package com.kwoksys.action.contracts;

import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;

/**
 * ActionForm class for editing contract.
 */
public class ContractForm extends BaseObjectForm {

    private Integer contractId;
    private String contractName;
    private String contractDescription;
    private Integer contractType;
    private Integer contractStage;
    private Integer contractOwner;
    private String contractEffectiveDateDate;
    private String contractEffectiveDateMonth;
    private String contractEffectiveDateYear;
    private String contractExpirationDateDate;
    private String contractExpirationDateMonth;
    private String contractExpirationDateYear;
    private Integer contractRenewalType;
    private String contractRenewalDateDate;
    private String contractRenewalDateMonth;
    private String contractRenewalDateYear;
    private Integer contractProviderId;
    private Integer contactId;
    private String formContactId;
    private String relationshipDescription;

    @Override
    public void setRequest(RequestContext requestContext) {
        contractId = requestContext.getParameterInteger("contractId");
        contractName = requestContext.getParameterString("contractName");
        contractDescription = requestContext.getParameterString("contractDescription");
        contractType = requestContext.getParameterInteger("contractType");
        contractStage = requestContext.getParameterInteger("contractStage");
        contractOwner = requestContext.getParameterInteger("contractOwner");
        contractEffectiveDateDate = requestContext.getParameterString("contractEffectiveDateDate");
        contractEffectiveDateMonth = requestContext.getParameterString("contractEffectiveDateMonth");
        contractEffectiveDateYear = requestContext.getParameterString("contractEffectiveDateYear");
        contractExpirationDateDate = requestContext.getParameterString("contractExpirationDateDate");
        contractExpirationDateMonth = requestContext.getParameterString("contractExpirationDateMonth");
        contractExpirationDateYear = requestContext.getParameterString("contractExpirationDateYear");
        contractRenewalType = requestContext.getParameterInteger("contractRenewalType");
        contractRenewalDateDate = requestContext.getParameterString("contractRenewalDateDate");
        contractRenewalDateMonth = requestContext.getParameterString("contractRenewalDateMonth");
        contractRenewalDateYear = requestContext.getParameterString("contractRenewalDateYear");
        contractProviderId = requestContext.getParameterInteger("contractProviderId");
        contactId = requestContext.getParameterInteger("contactId");
        formContactId = requestContext.getParameterString("formContactId");
        relationshipDescription = requestContext.getParameterString("relationshipDescription");
    }

    public void setContract(Contract contract) {
        contractName = contract.getName();
        contractDescription = contract.getDescription();
        contractType = contract.getType();
        contractStage = contract.getStage();
        contractOwner = contract.getOwnerId();
        contractRenewalType = contract.getRenewalType();
        contractProviderId = contract.getContractProviderId();

        contractEffectiveDateYear = DatetimeUtils.toYearString(contract.getEffectiveDate());
        contractEffectiveDateMonth = DatetimeUtils.toMonthString(contract.getEffectiveDate());
        contractEffectiveDateDate = DatetimeUtils.toDateString(contract.getEffectiveDate());

        contractExpirationDateYear = DatetimeUtils.toYearString(contract.getExpireDate());
        contractExpirationDateMonth = DatetimeUtils.toMonthString(contract.getExpireDate());
        contractExpirationDateDate = DatetimeUtils.toDateString(contract.getExpireDate());

        contractRenewalDateYear = DatetimeUtils.toYearString(contract.getRenewalDate());
        contractRenewalDateMonth = DatetimeUtils.toMonthString(contract.getRenewalDate());
        contractRenewalDateDate = DatetimeUtils.toDateString(contract.getRenewalDate());
    }

    public Integer getContractId() {
        return contractId;
    }
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }
    public String getContractName() {
        return contractName;
    }
    public String getContractDescription() {
        return contractDescription;
    }
    public Integer getContractType() {
        return contractType;
    }
    public String getContractEffectiveDateDate() {
        return contractEffectiveDateDate;
    }
    public Integer getContractRenewalType() {
        return contractRenewalType;
    }
    public String getContractEffectiveDateMonth() {
        return contractEffectiveDateMonth;
    }
    public String getContractEffectiveDateYear() {
        return contractEffectiveDateYear;
    }
    public String getContractExpirationDateDate() {
        return contractExpirationDateDate;
    }
    public String getContractExpirationDateMonth() {
        return contractExpirationDateMonth;
    }
    public String getContractExpirationDateYear() {
        return contractExpirationDateYear;
    }
    public String getContractRenewalDateDate() {
        return contractRenewalDateDate;
    }
    public String getContractRenewalDateMonth() {
        return contractRenewalDateMonth;
    }
    public String getContractRenewalDateYear() {
        return contractRenewalDateYear;
    }
    public Integer getContractProviderId() {
        return contractProviderId;
    }

    public Integer getContractOwner() {
        return contractOwner;
    }

    public Integer getContractStage() {
        return contractStage;
    }

    public String getRelationshipDescription() {
        return relationshipDescription;
    }

    public String getFormContactId() {
        return formContactId;
    }

    public Integer getContactId() {
        return contactId;
    }
}
