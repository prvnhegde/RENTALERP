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
package com.kwoksys.biz.contracts.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.SystemUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.Callback;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * ContractUtil
 */
public class ContractUtils {

    public static final String CONTACTS_TAB = "contactsTab";

    public static final String FILES_TAB = "filesTab";

    public static final String HARDWARE_TAB = "hardwareTab";

    public static final String[] CONTRACT_COLUMNS_DEFAULT = new String[] { Contract.ROWNUM, Contract.NAME,
            Contract.DESCRIPTION, Contract.TYPE, Contract.STAGE, Contract.OWNER, Contract.CONTRACT_EXPIRE_DATE,
            Contract.CONTRACT_EFFECT_DATE, Contract.RENEWAL_TYPE, Contract.CONTRACT_RENEWAL_DATE };

    /**
     * Speficify the sortable columns allowed.
     */
    public static List<String> getSortableColumns() {
        return Arrays.asList(Contract.NAME, Contract.ID, Contract.OWNER, Contract.CONTRACT_EXPIRE_DATE,
                Contract.CONTRACT_EFFECT_DATE);
    }

    /**
     * Check whether a column is sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableColumn(String columnName) {
        return getSortableColumns().contains(columnName);
    }

    /**
     * Specify the column header for the list page.
     */
    public static List<String> getColumnHeaderList() {
        return Arrays.asList(ConfigManager.app.getContractsColumns());
    }

    public static String formatExpirationDate(RequestContext requestContext, Date currentDate, Date expirationDate) {
        return SystemUtils.formatExpirationDateHtml(requestContext, currentDate, expirationDate, ConfigManager.app.getContractsExpireCountdown());
    }

    public static String formatExpirationDateText(RequestContext requestContext, Date currentDate, Date expirationDate) {
        return SystemUtils.formatExpirationDateText(requestContext, currentDate, expirationDate, ConfigManager.app.getContractsExpireCountdown());
    }

    /**
     * This is for generating contract tabs.
     *
     * @param request
     * @param contract
     * @return ..
     */
    public static List<Link> contractTabList(RequestContext requestContext, Contract contract)
            throws DatabaseException {

        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        List<Integer> linkedTypes = Arrays.asList(ObjectTypes.HARDWARE, ObjectTypes.SOFTWARE);
        int relationshipCount = systemService.getLinkedObjectMapCount(linkedTypes, contract.getId(), ObjectTypes.CONTRACT);

        AccessUser user = requestContext.getUser();

        List<Link> tabList = new ArrayList<>();

        // Link to contract attachments view.
        if (Access.hasPermission(user, AppPaths.CONTRACTS_DETAIL)) {
            tabList.add(new Link(requestContext).setName(FILES_TAB)
                    .setAjaxPath(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId())
                    .setTitleKey("itMgmt.tab.contractFile"));
        }

        // Link to contract hardware tab.
        if (Access.hasPermission(user, AppPaths.CONTRACTS_ITEMS)) {
            tabList.add(new Link(requestContext).setName(HARDWARE_TAB)
                    .setAppPath(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contract.getId())
                    .setTitle(Localizer.getText(requestContext, "common.tab.relationships", new Object[]{relationshipCount})));
        }

        if (Access.hasPermission(user, AppPaths.CONTRACTS_CONTACTS)) {
            linkedTypes = Arrays.asList(ObjectTypes.CONTACT);
            int contactsCount = systemService.getLinkedObjectMapCount(linkedTypes, contract.getId(), ObjectTypes.CONTRACT);

            tabList.add(new Link(requestContext).setName(CONTACTS_TAB)
                    .setAppPath(AppPaths.CONTRACTS_CONTACTS + "?contractId=" + contract.getId())
                    .setTitle(Localizer.getText(requestContext, "common.linking.tab.linkedContacts", new Object[]{contactsCount})));
        }
        return tabList;
    }

    public static List<DataRow> fetchContractList(RequestContext requestContext, QueryCriteria queryCriteria, Counter counter) throws Exception {
        AccessUser user = requestContext.getUser();
        boolean hasContractAccess = Access.hasPermission(user, AppPaths.CONTRACTS_DETAIL);
        boolean hasUserDetailAccess = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);

        // Sysdate timestamp
        Date unixTimestamp = requestContext.getSysdate();

        List<DataRow> list = new ArrayList<>();
        List<String> columnHeaders = ContractUtils.getColumnHeaderList();

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        
        queryCriteria.setCallback(new Callback() {
            @Override
            public void run(Object object) throws Exception {
                Contract contract = (Contract) object;
                List<String> columns = new ArrayList<>();

                for (String column : columnHeaders) {
                    if (column.equals(Contract.ROWNUM)) {
                        columns.add(counter.incr() + ".");

                    } else if (column.equals(Contract.NAME)) {
                        Link link = new Link(requestContext);
                        link.setTitle(contract.getName());

                        if (hasContractAccess) {
                            link.setAjaxPath(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
                        }
                        columns.add(link.getString());

                    } else if (column.equals(Contract.DESCRIPTION)) {
                        columns.add(HtmlUtils.formatMultiLineDisplay(contract.getDescription()));
                                
                    } else if (column.equals(Contract.TYPE)) {
                        columns.add(HtmlUtils.encode(contract.getTypeName()));

                    } else if (column.equals(Contract.STAGE)) {
                        columns.add(new AttributeManager(requestContext).getAttrFieldNameCache(Attributes.CONTRACT_STAGE, contract.getStage()));

                    } else if (column.equals(Contract.OWNER)) {
                        AccessUser contractOwner = new CacheManager(requestContext).getUserCache(contract.getOwnerId());
                        columns.add(Links.getUserIconLink(requestContext, contractOwner, hasUserDetailAccess, true).getString());

                    } else if (column.equals(Contract.RENEWAL_TYPE)) {
                        columns.add(HtmlUtils.encode(contract.getRenewalTypeName()));

                    } else if (column.equals(Contract.CONTRACT_EXPIRE_DATE)) {
                        columns.add(ContractUtils.formatExpirationDate(requestContext, unixTimestamp, contract.getExpireDate()));

                    } else if (column.equals(Contract.CONTRACT_EFFECT_DATE)) {
                        columns.add(DatetimeUtils.toShortDate(contract.getEffectiveDate()));

                    } else if (column.equals(Contract.CONTRACT_RENEWAL_DATE)) {
                        columns.add(DatetimeUtils.toShortDate(contract.getRenewalDate()));
                    }
                }
                DataRow dataRow = new DataRow();
                dataRow.setRowId(String.valueOf(contract.getId()));
                dataRow.setColumns(columns);
                list.add(dataRow);
            }
        });
        contractService.fetchContracts(queryCriteria);
        return list;
    }
}
