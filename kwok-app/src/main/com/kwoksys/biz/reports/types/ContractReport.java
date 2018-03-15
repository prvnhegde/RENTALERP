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
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractSearch;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dao.ContractQueries;
import com.kwoksys.biz.contracts.dto.Contract;
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
import com.kwoksys.framework.util.Callback;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * ContractReport
 */
public class ContractReport extends Report {

    private String reportCriteriaMapKey;

    public ContractReport(RequestContext requestContext, String reportCriteriaMapKey) {
        this.requestContext = requestContext;
        this.reportCriteriaMapKey = reportCriteriaMapKey;
    }

    public String getFilename() {
        String filename = ConfigManager.reports.getContractsReportFilename();
        return StringUtils.isEmpty(filename) ? Localizer.getText(requestContext, "reports.workflow.type.contract_report") : filename;
    }
    
    public String getReportFormName() {
        return "ContractSearchForm";
    }

    public String getReportPath() {
        return AppPaths.REPORTS_CONTRACT_SEARCH;
    }

    @Override
    public void populateData(ReportWriter reportWriter) throws Exception {
        // This is for column header.
        List<String> columnKeys = computeColumns(ConfigManager.app.getContractsExportColumns());

        for (String column : columnKeys) {
            getColumnHeaders().add(Localizer.getText(requestContext, "common.column." + column));
        }

        // Print custom field headers
        Collection<Attribute> attrs = computeCustFieldColumns(requestContext, ObjectTypes.CONTRACT);

        for (Attribute attr : attrs) {
            getColumnHeaders().add(attr.getName());
        }

        reportWriter.addHeaderRow(getColumnHeaders());

        // Getting search criteria map from session variable.
        ContractSearch contractSearch = new ContractSearch(requestContext, reportCriteriaMapKey);

        // Get order and orderBy
        String orderBy = getReportColumnOrderBy();
        if (orderBy == null) {
            orderBy = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.CONTRACTS_ORDER_BY, Contract.NAME);
        }

        String order = getReportColumnOrder();
        if (order == null) {
            order = SessionManager.getAttribute(requestContext.getRequest(), SessionManager.CONTRACTS_ORDER, QueryCriteria.ASCENDING);
        }

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AttributeManager attributeManager = new AttributeManager(requestContext);

        QueryCriteria queryCriteria = new QueryCriteria(contractSearch);

        if (ContractUtils.isSortableColumn(orderBy)) {
            queryCriteria.addSortColumn(ContractQueries.getOrderByColumn(orderBy), order);
        }

        queryCriteria.setCallback(new Callback() {
            @Override
            public void run(Object object) throws Exception {
                Contract contract = (Contract) object;

                List<String> columns = new ArrayList<>();
                for (String column : columnKeys) {
                    if (column.equals(Contract.NAME)) {
                        columns.add(contract.getName());
    
                    } else if (column.equals(Contract.TYPE)) {
                        columns.add(contract.getTypeName());
    
                    } else if (column.equals(Contract.STAGE)) {
                        columns.add(attributeManager.getAttrFieldNameCache(Attributes.CONTRACT_STAGE, contract.getStage()));
    
                    } else if (column.equals(Contract.CONTRACT_OWNER_USERNAME)) {
                        AccessUser contractOwner = new CacheManager(requestContext).getUserCache(contract.getOwnerId());
                        columns.add(AdminUtils.getSystemUsername(requestContext, contract.getOwnerId(), contractOwner.getUsername()));
    
                    } else if (column.equals(Contract.CONTRACT_OWNER_DISPLAY_NAME)) {
                        AccessUser contractOwner = new CacheManager(requestContext).getUserCache(contract.getOwnerId());
                        columns.add(AdminUtils.getSystemUsername(requestContext, contract.getOwnerId(), contractOwner.getDisplayName()));
    
                    } else if (column.equals(Contract.RENEWAL_TYPE)) {
                        columns.add(contract.getRenewalTypeName());
    
                    } else if (column.equals(Contract.CONTRACT_EXPIRE_DATE)) {
                        columns.add(DatetimeUtils.toShortDate(contract.getExpireDate()));
    
                    } else if (column.equals(Contract.CONTRACT_EFFECT_DATE)) {
                        columns.add(DatetimeUtils.toShortDate(contract.getEffectiveDate()));
    
                    } else if (column.equals(Contract.CONTRACT_RENEWAL_DATE)) {
                        columns.add(DatetimeUtils.toShortDate(contract.getRenewalDate()));
                    }
                }
    
                // Add custom field values
                addCustomFieldValues(adminService, attrs, columns, contract);
    
                reportWriter.addRow(columns);
            }
        });
        contractService.fetchContracts(queryCriteria);
    }
}
