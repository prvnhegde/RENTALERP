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

import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.contracts.dao.ContractQueries;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.Callback;
import com.kwoksys.framework.util.CurrencyUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * HardwareSpecTemplate
 */
public class HardwareSpecTemplate extends BaseTemplate {

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    private Hardware hardware;
    private boolean populateLinkedContract;
    private int columns = 2;
    private boolean disableHeader;
    private String headerText;
    private StringBuilder contracts;

    public HardwareSpecTemplate(Hardware hardware) {
        super(HardwareSpecTemplate.class);
        this.hardware = hardware;
    }

    public void init() {
        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws Exception {
        AccessUser accessUser = requestContext.getUser();
        AttributeManager attributeManager = new AttributeManager(requestContext);

        detailTableTemplate.setNumColumns(columns);

        DetailTableTemplate.Td td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_id");
        td.setValue(String.valueOf(hardware.getId()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_name");
        td.setValue(HtmlUtils.encode(hardware.getName()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(hardware.getDescription()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_manufacturer_name");
        td.setValue(Links.getCompanyDetailsLink(requestContext, hardware.getManufacturerName(),
                hardware.getManufacturerId()).getString());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_vendor_name");
        td.setValue(Links.getCompanyDetailsLink(requestContext, hardware.getVendorName(), hardware.getVendorId()).getString());

        AttributeField attrField = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_TYPE).get(hardware.getType());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_type");
        td.setValue(Links.getAttrFieldIcon(requestContext, attrField).getString());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_status");
        td.setValue(HtmlUtils.encode(attributeManager.getAttrFieldNameCache(Attributes.HARDWARE_STATUS, hardware.getStatus())));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_model_name");
        td.setValue(HtmlUtils.encode(hardware.getModelName()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_model_number");
        td.setValue(HtmlUtils.encode(hardware.getModelNumber()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_serial_number");
        td.setValue(HtmlUtils.encode(hardware.getSerialNumber()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_purchase_price");
        td.setValue(CurrencyUtils.formatCurrency(hardware.getPurchasePriceRaw(), ConfigManager.system.getCurrencySymbol()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_purchase_date");
        td.setValue(DatetimeUtils.toShortDate(hardware.getHardwarePurchaseDate()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_warranty_expire_date");
        td.setValue(HardwareUtils.formatWarrantyExpirationDate(requestContext, requestContext.getSysdate(),
                hardware.getWarrantyExpireDate()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_last_service_date");
        td.setValue(hardware.getLastServicedOn());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_location");
        td.setValue(HtmlUtils.encode(attributeManager.getAttrFieldNameCache(Attributes.HARDWARE_LOCATION, hardware.getLocation())));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.hardware_owner_name");
        boolean canViewUserPage = Access.hasPermission(accessUser, AppPaths.ADMIN_USER_DETAIL);
        AccessUser hardwareOwner = new CacheManager(requestContext).getUserCache(hardware.getOwnerId());
        td.setValue(Links.getUserIconLink(requestContext, hardwareOwner, canViewUserPage, true).getString());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.creator");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, hardware.getCreationDate(), hardware.getCreator()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.modifier");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, hardware.getModificationDate(), hardware.getModifier()));

        if (populateLinkedContract) {
            boolean canViewContract = Access.hasPermission(accessUser, AppPaths.CONTRACTS_DETAIL);

            // Get linked contracts
            HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

            contracts = new StringBuilder();

            QueryCriteria queryCriteria = new QueryCriteria();
            queryCriteria.addSortColumn(ContractQueries.getOrderByColumn(Contract.NAME));

            queryCriteria.setCallback(new Callback() {
                @Override
                public void run(Object object) throws Exception {
                    Contract contract = (Contract) object;

                    Link link = new Link(requestContext);
                    link.setTitle(contract.getName());

                    if (canViewContract) {
                        link.setAjaxPath(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
                    }
                    if (contracts.length() != 0) {
                        contracts.append(", ");
                    }
                    contracts.append(link.getString());
                }
            });
            hardwareService.fetchLinkedContracts(queryCriteria, hardware.getId());
        }

        headerText = Localizer.getText(requestContext, "itMgmt.hardwareDetail.header", new String[]{hardware.getName()});
    }

    @Override
    public String getJspPath() {
        return "/jsp/hardware/HardwareSpecTemplate.jsp";
    }

    public void setPopulateLinkedContract(boolean populateLinkedContract) {
        this.populateLinkedContract = populateLinkedContract;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setDisableHeader(boolean disableHeader) {
        this.disableHeader = disableHeader;
    }

    public boolean isDisableHeader() {
        return disableHeader;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getContracts() {
        return contracts.toString();
    }
}
