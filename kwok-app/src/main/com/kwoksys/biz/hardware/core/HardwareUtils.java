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
package com.kwoksys.biz.hardware.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.SystemUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.CurrencyUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * HardwareUtil
 */
public class HardwareUtils {

    public static final String HARDWARE_LICENSE_TAB = "licenseTab";
    public static final String HARDWARE_COMP_TAB = "componentTab";
    public static final String HARDWARE_FILE_TAB = "fileTab";
    public static final String HARDWARE_ISSUE_TAB = "issueTab";
    public static final String HARDWARE_MEMBER_TAB = "memberTab";
    public static final String HARDWARE_CONTACT_TAB = "contactTab";

    public static final String[] HARDWARE_DEFAULT_COLUMNS = new String[] { Hardware.ROWNUM, Hardware.ID,
            Hardware.HARDWARE_NAME, Hardware.HARDWARE_DESCRIPTION, Hardware.TYPE, Hardware.STATUS, Hardware.MODEL_NAME,
            Hardware.MODEL_NUMBER, Hardware.SERIAL_NUMBER, Hardware.LOCATION, Hardware.PURCHASE_DATE,
            Hardware.PURCHASE_PRICE, Hardware.WARRANTY_EXPIRATION, Hardware.SERVICE_DATE, Hardware.OWNER_NAME };

    /**
     * Speficify the sortable columns allowed.
     */
    public static List<String> getSortableColumns() {
        return Arrays.asList(Hardware.ID, Hardware.HARDWARE_NAME, Hardware.MODEL_NAME, Hardware.MODEL_NUMBER,
                Hardware.SERIAL_NUMBER, Hardware.PURCHASE_PRICE, Hardware.PURCHASE_DATE, Hardware.WARRANTY_EXPIRATION, 
                Hardware.SERVICE_DATE, Hardware.OWNER_NAME, Hardware.LOCATION);
    }

    /**
     * Check whether a column is sortable.
     * Return true if the given column is sortable.
     * Return false if the given column is not sortable.
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
        return ConfigManager.app.getHardwareColumns();
    }

    public static void addHardwareHeaderCommands(RequestContext requestContext, HeaderTemplate headerTemplate, Integer hardwareId) throws DatabaseException {
        AccessUser accessUser = requestContext.getUser();

        // Edit Hardware link.
        if (Access.hasPermission(accessUser, AppPaths.HARDWARE_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_EDIT + "?hardwareId=" + hardwareId);
            link.setTitleKey("itMgmt.cmd.hardwareEdit");
            headerTemplate.addHeaderCmds(link);
        }

        // Copy hardware
        if (Access.hasPermission(accessUser, AppPaths.HARDWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_ADD + "?copyHardwareId=" + hardwareId);
            link.setTitleKey("itMgmt.cmd.hardwareCopy");
            headerTemplate.addHeaderCmds(link);
        }

        // Delete Hardware link.
        if (Access.hasPermission(accessUser, AppPaths.HARDWARE_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_DELETE + "?hardwareId=" + hardwareId);
            link.setTitleKey("itMgmt.cmd.hardwareDelete");
            headerTemplate.addHeaderCmds(link);
        }

        if (Access.hasPermission(accessUser, AppPaths.HARDWARE_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_LIST);
            link.setTitleKey("itMgmt.cmd.hardwareList");
            headerTemplate.addHeaderCmds(link);
        }
    }

    /**
     * This is for generating hardware tabs.
     *
     * @param request
     * @param hardware
     * @return ..
     */
    public static List<Link> hardwareTabList(Hardware hardware, RequestContext requestContext) throws DatabaseException {
        AccessUser user = requestContext.getUser();

        List<Link> tabList = new ArrayList<>();

        // Link to Hardware assigned software tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_DETAIL)) {
            tabList.add(new Link(requestContext).setName(HARDWARE_LICENSE_TAB)
                    .setAppPath(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.getId())
                    .setTitle(Localizer.getText(requestContext, "itMgmt.tab.hardwareAssignedSoftware",
                    new Object[]{hardware.getCountSoftware()})));
        }

        // Link to Hardware components tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_COMP)) {
            tabList.add(new Link(requestContext).setName(HARDWARE_COMP_TAB)
                    .setAppPath(AppPaths.HARDWARE_COMP + "?hardwareId=" + hardware.getId())
                    .setTitle(Localizer.getText(requestContext, "itMgmt.tab.hardwareComponents",
                    new Object[] {hardware.getCountComponent()})));
        }

        // Link to Hardware attachments tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_FILE)) {
            tabList.add(new Link(requestContext).setName(HARDWARE_FILE_TAB)
                    .setAjaxPath(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardware.getId())
                    .setTitle(Localizer.getText(requestContext, "itMgmt.tab.hardwareFile", new Object[] {hardware.getCountFile()})));
        }

        // Link to Hardware association tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_MEMBER)) {
            tabList.add(new Link(requestContext).setName(HARDWARE_MEMBER_TAB)
                    .setAppPath(AppPaths.HARDWARE_MEMBER + "?hardwareId=" + hardware.getId())
                    .setTitleKey("hardware.tab.hardwareMembers"));
        }

        // Link to Hardware Issues tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_ISSUE)) {
            tabList.add(new Link(requestContext).setName(HARDWARE_ISSUE_TAB)
                    .setAppPath(AppPaths.HARDWARE_ISSUE + "?hardwareId=" + hardware.getId())
                    .setTitleKey("itMgmt.tab.hardwareIssues"));
        }

        // Link to Hardware Contacts tab.
        if (Access.hasPermission(user, AppPaths.HARDWARE_CONTACTS)) {
            SystemService systemService = ServiceProvider.getSystemService(requestContext);
            List<Integer> linkedTypes = Arrays.asList(ObjectTypes.CONTACT);
            int relationshipCount = systemService.getLinkedObjectMapCount(linkedTypes, hardware.getId(), ObjectTypes.HARDWARE);

            tabList.add(new Link(requestContext).setName(HARDWARE_CONTACT_TAB)
                    .setAppPath(AppPaths.HARDWARE_CONTACTS + "?hardwareId=" + hardware.getId())
                    .setTitle(Localizer.getText(requestContext, "common.linking.tab.linkedContacts",
                    new Object[] {relationshipCount})));
        }
        return tabList;
    }

    public static List<DataRow> formatHardwareList(RequestContext requestContext, List<Hardware> hardwareDataset, Counter counter,
                                          String hardwarePath) throws Exception {
        return formatHardwareList(requestContext, hardwareDataset, HardwareUtils.getColumnHeaderList(), counter, hardwarePath);
    }

    public static List<DataRow> formatHardwareList(RequestContext requestContext, List<Hardware> hardwareDataset, List<String> columnHeaders,
                                      Counter counter, String hardwarePath) throws Exception {
        List<DataRow> list = new ArrayList<>();

        if (hardwareDataset.isEmpty()) {
            return list;
        }

        AccessUser user = requestContext.getUser();

        // Sysdate timestamp
        Date unixTimestamp = requestContext.getSysdate();

        boolean hasHardwareAccess = Access.hasPermission(user, hardwarePath);
        boolean hasUserDetailAccess = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);
        boolean hasHwAjaxAccess = Access.hasPermission(user, AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL);

        AttributeManager attributeManager = new AttributeManager(requestContext);

        for (Hardware hardware : hardwareDataset) {
            List<String> columns = new ArrayList<>();

            for (String column : columnHeaders) {
                if (column.equals(Hardware.ROWNUM)) {
                    columns.add(counter.incr() + ".");

                } else if (column.equals(Hardware.ID)) {
                    columns.add(String.valueOf(hardware.getId()));

                } else if (column.equals(Hardware.HARDWARE_NAME)) {
                    if (hasHardwareAccess) {
                        Link link = new Link(requestContext);
                        link.setTitle(hardware.getName());
                        link.setAjaxPath(hardwarePath + "?hardwareId=" + hardware.getId());
                        String tempHardwareName = link.getString();

                        if (hasHwAjaxAccess) {
                            link = new Link(requestContext);
                            link.setImgSrc(Image.getInstance().getMagGlassIcon());
                            link.setImgOnclick("hardwarePopupAjax.show(this," + hardware.getId() + ")");
                            tempHardwareName += "&nbsp;" + link.getString();
                        }
                        columns.add(tempHardwareName);
                    } else {
                        columns.add(HtmlUtils.encode(hardware.getName()));
                    }
                    
                } else if (column.equals(Hardware.HARDWARE_DESCRIPTION)) {
                    columns.add(HtmlUtils.formatMultiLineDisplay(hardware.getDescription()));
                    
                } else if (column.equals(Hardware.OWNER_NAME)) {
                    AccessUser hardwareOwner = new CacheManager(requestContext).getUserCache(hardware.getOwnerId());
                    columns.add(Links.getUserIconLink(requestContext, hardwareOwner, hasUserDetailAccess, true).getString());

                } else if (column.equals(Hardware.MODEL_NAME)) {
                    columns.add(HtmlUtils.encode(hardware.getModelName()));

                } else if (column.equals(Hardware.MODEL_NUMBER)) {
                    columns.add(HtmlUtils.encode(hardware.getModelNumber()));

                } else if (column.equals(Hardware.SERIAL_NUMBER)) {
                    columns.add(HtmlUtils.encode(hardware.getSerialNumber()));

                } else if (column.equals(Hardware.PURCHASE_PRICE)) {
                	columns.add(CurrencyUtils.formatCurrency(hardware.getPurchasePriceRaw(), ConfigManager.system.getCurrencySymbol()));
                	
                } else if (column.equals(Hardware.PURCHASE_DATE)) {
                	columns.add(DatetimeUtils.toShortDate(hardware.getHardwarePurchaseDate()));
                	
                } else if (column.equals(Hardware.WARRANTY_EXPIRATION)) {
                    columns.add(HardwareUtils.formatWarrantyExpirationDate(requestContext, unixTimestamp,
                            hardware.getWarrantyExpireDate()));

                } else if (column.equals(Hardware.SERVICE_DATE)) {
                    columns.add(hardware.getLastServicedOn());

                } else if (column.equals(Hardware.STATUS)) {
                    AttributeField attrField = attributeManager.getAttrFieldMapCache(
                            Attributes.HARDWARE_STATUS).get(hardware.getStatus());
                    columns.add(Links.getAttrFieldIcon(requestContext, attrField).getString());

                } else if (column.equals(Hardware.TYPE)) {
                    AttributeField attrField = attributeManager.getAttrFieldMapCache(
                            Attributes.HARDWARE_TYPE).get(hardware.getType());
                    columns.add(Links.getAttrFieldIcon(requestContext, attrField).getString());

                } else if (column.equals(Hardware.LOCATION)) {
                    columns.add(HtmlUtils.encode(attributeManager.getAttrFieldNameCache(Attributes.HARDWARE_LOCATION,
                            hardware.getLocation())));
                }
            }

            DataRow dataRow = new DataRow();
            dataRow.setRowId(String.valueOf(hardware.getId()));
            dataRow.setColumns(columns);
            list.add(dataRow);
        }
        return list;
    }

    public static String formatWarrantyExpirationDate(RequestContext requestContext, Date currentDate, Date expirationDate) {
        return SystemUtils.formatExpirationDateHtml(requestContext, currentDate, expirationDate, ConfigManager.app.getHardwareWarrantyExpireCountdown());
    }
}
