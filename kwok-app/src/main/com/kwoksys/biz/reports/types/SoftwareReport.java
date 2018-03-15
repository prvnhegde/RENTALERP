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
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.reports.Report;
import com.kwoksys.biz.reports.writers.ReportWriter;
import com.kwoksys.biz.software.SoftwareSearch;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * SoftwareReport
 */
public class SoftwareReport extends Report {

    private String reportCriteriaMapAttr;

    public SoftwareReport(RequestContext requestContext, String reportCriteriaMapAttr) {
        this.requestContext = requestContext;
        this.reportCriteriaMapAttr = reportCriteriaMapAttr;
    }

    public String getFilename() {
        String filename = ConfigManager.reports.getSoftwareReportFilename();
        return StringUtils.isEmpty(filename) ? Localizer.getText(requestContext, "reports.workflow.type.software_report") : filename;
    }


    public String getReportFormName() {
        return "SoftwareSearchForm";
    }

    public String getReportPath() {
        return AppPaths.REPORTS_SOFTWARE_SEARCH;
    }

    @Override
    public void populateData(ReportWriter reportWriter) throws Exception {
        // This is for column header.
        List<String> columnKeys = computeColumns(ConfigManager.app.getSoftwareExportColumns());

        for (String column : columnKeys) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column." + column));
        }

        // Print custom field headers
        Collection<Attribute> attrs = computeCustFieldColumns(requestContext, ObjectTypes.SOFTWARE);

        for (Attribute attr : attrs) {
            getColumnHeaders().add(attr.getName());
        }

        reportWriter.addHeaderRow(getColumnHeaders());

        // Getting search criteria map from session variable.
        SoftwareSearch softwareSearch = new SoftwareSearch(requestContext, reportCriteriaMapAttr);

        // Get order and orderBy
        String orderBy = getReportColumnOrderBy();
        if (orderBy == null) {
            orderBy = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.SOFTWARE_ORDER_BY, Software.NAME);
        }

        String order = getReportColumnOrder();
        if (order == null) {
            order = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.SOFTWARE_ORDER, QueryCriteria.ASCENDING);
        }

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria(softwareSearch);

        if (SoftwareUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(SoftwareQueries.getOrderByColumn(orderBy), order);
        }

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        AttributeManager attributeManager = new AttributeManager(requestContext);

        // Loop through the Hardware list.
        for (Software software : softwareService.getSoftwareList(query)) {
            List<String> columns = new ArrayList<>();
            for (String column : columnKeys) {
                if (column.equals(Software.NAME)) {
                    columns.add(software.getName());

                } else if (column.equals(Software.VERSION)) {
                    columns.add(software.getVersion());

                } else if (column.equals(Software.EXPIRE_DATE)) {
                    columns.add(DatetimeUtils.toShortDate(software.getExpireDate()));

                } else if (column.equals(Software.DESCRIPTION)) {
                    columns.add(software.getDescription());

                } else if (column.equals(Software.OWNER_USERNAME)) {
                    columns.add(AdminUtils.getSystemUsername(requestContext, software.getOwner().getId(), software.getOwner().getUsername()));

                } else if (column.equals(Software.OWNER_DISPLAY_NAME)) {
                    columns.add(AdminUtils.getSystemUsername(requestContext, software.getOwner().getId(), software.getOwner().getDisplayName()));

                } else if (column.equals(Software.MANUFACTURER)) {
                    columns.add(software.getManufacturerName());

                } else if (column.equals(Software.VENDOR)) {
                    columns.add(software.getVendorName());

                } else if (column.equals(Software.QUOTED_OEM_PRICE)) {
                    columns.add(software.getQuotedOemPrice());

                } else if (column.equals(Software.QUOTED_RETAIL_PRICE)) {
                    columns.add(software.getQuotedRetailPrice());

                } else if (column.equals(Software.LICENSE_PUCHASED)) {
                    columns.add(String.valueOf(software.getLicensePurchased()));

                } else if (column.equals(Software.LICENSE_INSTALLED)) {
                    columns.add(String.valueOf(software.getLicenseInstalled()));

                } else if (column.equals(Software.LICENSE_AVAILABLE)) {
                    columns.add(String.valueOf(software.getLicenseAvailable()));

                } else if (column.equals(Software.TYPE)) {
                    columns.add(attributeManager.getAttrFieldNameCache(Attributes.SOFTWARE_TYPE, software.getType()));

                } else if (column.equals(Software.OS)) {
                    columns.add(attributeManager.getAttrFieldNameCache(Attributes.SOFTWARE_OS, software.getOs()));
                }
            }

            // Add custom field values
            addCustomFieldValues(adminService, attrs, columns, software);

            reportWriter.addRow(columns);
        }
    }
}