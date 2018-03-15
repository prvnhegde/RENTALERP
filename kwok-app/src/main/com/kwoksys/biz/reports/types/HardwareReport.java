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
package com.kwoksys.biz.reports.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.reports.Report;
import com.kwoksys.biz.reports.writers.ReportWriter;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.util.CurrencyUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * HardwareReport
 */
public class HardwareReport extends Report {

    private String reportCriteriaMapKey;

    public HardwareReport(RequestContext requestContext, String reportCriteriaMapKey) {
        this.requestContext = requestContext;
        this.reportCriteriaMapKey = reportCriteriaMapKey;
    }

    public String getFilename() {
        String filename = ConfigManager.reports.getHardwareReportFilename();
        return StringUtils.isEmpty(filename) ? Localizer.getText(requestContext, "reports.workflow.type.hardware_report") : filename;
    }

    public String getReportFormName() {
        return "HardwareSearchForm";
    }

    public String getReportPath() {
        return AppPaths.REPORTS_HARDWARE_SEARCH;
    }

    @Override
    public void populateData(ReportWriter reportWriter) throws Exception {
        // This is for column header.
        List<String> columnKeys = computeColumns(ConfigManager.app.getHardwareExportColumns());

        for (String column : columnKeys) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column." + column));
        }

        // Print custom field headers
        Collection<Attribute> attrs = computeCustFieldColumns(requestContext, ObjectTypes.HARDWARE);

        for (Attribute attr : attrs) {
            getColumnHeaders().add(attr.getName());
        }

        reportWriter.addHeaderRow(getColumnHeaders());

        // Getting search criteria map from session variable.
        HardwareSearch hardwareSearch = new HardwareSearch(requestContext, reportCriteriaMapKey);

        // Get order and orderBy
        String orderBy = getReportColumnOrderBy();
        if (orderBy == null) {
            orderBy = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        }

        String order = getReportColumnOrder();
        if (order == null) {
            order = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.HARDWARE_ORDER, QueryCriteria.ASCENDING);
        }

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria(hardwareSearch);

        if (HardwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(orderBy), order);
        }

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Loop through the Hardware list.
        for (Hardware hardware : hardwareService.getHardwareList(query)) {
            List<String> columns = new ArrayList<>();
            for (String column : columnKeys) {
                if (column.equals(Hardware.ID)) {
                    columns.add(String.valueOf(hardware.getId()));

                } else if (column.equals(Hardware.HARDWARE_NAME)) {
                     columns.add(hardware.getName());

                } else if (column.equals(Hardware.HARDWARE_DESCRIPTION)) {
                     columns.add(hardware.getDescription());

                } else if (column.equals(Hardware.MANUFACTURER_NAME)) {
                     columns.add(hardware.getManufacturerName());

                } else if (column.equals(Hardware.VENDOR_NAME)) {
                     columns.add(hardware.getVendorName());

                } else if (column.equals(Hardware.TYPE)) {
                    columns.add(attributeManager.getAttrFieldNameCache(Attributes.HARDWARE_TYPE, hardware.getType()));

                } else if (column.equals(Hardware.STATUS)) {
                    columns.add(attributeManager.getAttrFieldNameCache(Attributes.HARDWARE_STATUS, hardware.getStatus()));

                } else if (column.equals(Hardware.MODEL_NAME)) {
                     columns.add(hardware.getModelName());

                } else if (column.equals(Hardware.MODEL_NUMBER)) {
                     columns.add(hardware.getModelNumber());

                } else if (column.equals(Hardware.SERIAL_NUMBER)) {
                     columns.add(hardware.getSerialNumber());

                } else if (column.equals(Hardware.PURCHASE_PRICE)) {
                     columns.add(CurrencyUtils.formatCurrency(hardware.getPurchasePriceRaw(), ConfigManager.system.getCurrencySymbol()));

                } else if (column.equals(Hardware.PURCHASE_DATE)) {
                    columns.add(DatetimeUtils.toShortDate(hardware.getHardwarePurchaseDate()));

                } else if (column.equals(Hardware.WARRANTY_EXPIRATION)) {
                    columns.add(DatetimeUtils.toShortDate(hardware.getWarrantyExpireDate()));

                } else if (column.equals(Hardware.SERVICE_DATE)) {
                     columns.add(hardware.getLastServicedOn());

                } else if (column.equals(Hardware.LOCATION)) {
                    columns.add(attributeManager.getAttrFieldNameCache(Attributes.HARDWARE_LOCATION, hardware.getLocation()));

                } else if (column.equals(Hardware.OWNER_NAME)) {
                    AccessUser hardwareOwner = new CacheManager(requestContext).getUserCache(hardware.getOwnerId());
                    columns.add(AdminUtils.getSystemUsername(requestContext, hardwareOwner));

                } else if (column.equals(Hardware.CREATOR_NAME)) {
                    columns.add(AdminUtils.getSystemUsername(requestContext, hardware.getCreator()));

                } else if (column.equals(Hardware.CREATION_DATE)) {
                    columns.add(hardware.getCreationDate());

                } else if (column.equals(Hardware.MODIFIER_NAME)) {
                    columns.add(AdminUtils.getSystemUsername(requestContext, hardware.getModifier()));

                } else if (column.equals(Hardware.MODIFICATION_DATE)) {
                    columns.add(hardware.getModificationDate());
                }
            }

            // Add custom field values
            addCustomFieldValues(adminService, attrs, columns, hardware);

            reportWriter.addRow(columns);
        }
    }
}
