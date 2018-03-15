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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * ContractSpecTemplate
 */
public class ContractSpecTemplate extends BaseTemplate {

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    private Contract contract;

    private String headerText;

    private int columns = 2;

    public ContractSpecTemplate(Contract contract) {
        super(ContractSpecTemplate.class);
        this.contract = contract;
    }

    public void init() {
        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws Exception {
        AccessUser user = requestContext.getUser();
        AttributeManager attributeManager = new AttributeManager(requestContext);

        detailTableTemplate.setNumColumns(columns);

        DetailTableTemplate.Td td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_id");
        td.setValue(String.valueOf(contract.getId()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_name");
        td.setValue(HtmlUtils.encode(contract.getName()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contract.getDescription()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_type");
        td.setValue(HtmlUtils.encode(attributeManager.getAttrFieldNameCache(Attributes.CONTRACT_TYPE, contract.getType())));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_stage");
        td.setValue(attributeManager.getAttrFieldNameCache(Attributes.CONTRACT_STAGE, contract.getStage()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_owner");
        boolean canViewUserPage = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);
        
        AccessUser contractOwner = new CacheManager(requestContext).getUserCache(contract.getOwnerId());
        td.setValue(Links.getUserIconLink(requestContext, contractOwner, canViewUserPage, true).getString());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_provider_name");
        td.setValue(Links.getCompanyDetailsLink(requestContext, contract.getContractProviderName(),
                contract.getContractProviderId()).getString());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_effective_date");
        td.setValue(DatetimeUtils.toShortDate(contract.getEffectiveDate()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_expiration_date");
        td.setValue(ContractUtils.formatExpirationDate(requestContext, requestContext.getSysdate(),
                contract.getExpireDate()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_renewal_date");
        td.setValue(DatetimeUtils.toShortDate(contract.getRenewalDate()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.contract_renewal_type");
        td.setValue(HtmlUtils.encode(attributeManager.getAttrFieldNameCache(Attributes.CONTRACT_RENEWAL_TYPE, contract.getRenewalType())));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.creator");
        AccessUser creatorUser = new CacheManager(requestContext).getUserCache(contract.getCreatorId());
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, contract.getCreationDate(), creatorUser));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.modifier");
        AccessUser modifierUser = new CacheManager(requestContext).getUserCache(contract.getModifierId());
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, contract.getModificationDate(), modifierUser));

        headerText = Localizer.getText(requestContext, "itMgmt.contractDetail.header", new String[]{contract.getName()});
    }

    @Override
    public String getJspPath() {
        return "/jsp/contracts/ContractSpecTemplate.jsp";
    }

    public String getHeaderText() {
        return headerText;
    }
}