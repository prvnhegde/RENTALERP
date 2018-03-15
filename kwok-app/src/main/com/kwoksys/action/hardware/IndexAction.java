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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.ChartJsColors;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * Action class for hardware index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        HttpSession session = request.getSession();

        if (requestContext.getParameterString("cmd").equals("clear")) {
            session.setAttribute(SessionManager.HARDWARE_SEARCH_CRITERIA_MAP, null);
            clearSessionBaseForm(HardwareSearchForm.class);
        }

        getSessionBaseForm(HardwareSearchForm.class);

        // Link to hardware list.
        List<Link> links = new ArrayList<>();
        if (user.hasPermission(AppPaths.HARDWARE_LIST)) {
            if (session.getAttribute(SessionManager.HARDWARE_SEARCH_CRITERIA_MAP) != null) {
                links.add(new Link(requestContext).setTitleKey("common.search.showLastSearch")
                        .setAjaxPath(AppPaths.HARDWARE_LIST));
            }

            links.add(new Link(requestContext).setTitleKey("itMgmt.index.showAllHardware")
                    .setAjaxPath(AppPaths.HARDWARE_LIST + "?cmd=showAll"));
        }

        // Get the number of records.
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        int numHardwareRecords = hardwareService.getHardwareCount(new QueryCriteria());

        // Warranty expiration links
        List<String> warrantyExpirationLinks = new ArrayList<>();

        for (Map.Entry<String, String> entry : hardwareService.getWarrantyExpirationCounts().entrySet()) {
            if (NumberUtils.replaceNull(entry.getValue()) > 0) {
                warrantyExpirationLinks.add(new Link(requestContext)
                        .setAjaxPath(AppPaths.HARDWARE_LIST + "?cmd=groupBy&warrantyExpire="+entry.getKey())
                        .setTitle(Localizer.getText(requestContext, "contracts.search.expire_"+entry.getKey()) + " (" + entry.getValue() + ")").getString());
            }
        }

        AttributeManager attributeManager = new AttributeManager(requestContext);

        /*
         * Count Hardware section
         */
        // We'll use the same queryCriteria for a few different queries below
        QueryCriteria query = new QueryCriteria();
        query.addSortColumn(HardwareQueries.getOrderByColumn(AttributeField.NAME));

        Map attrFieldTypeMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_TYPE);
        Map attrFieldStatusMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_STATUS);
        Map attrFieldLocMap = attributeManager.getAttrFieldMapCache(Attributes.HARDWARE_LOCATION);

        // Group by Hardware type.
        List<Map> hardwareTypeCounts = new ArrayList<>();
        List<AttributeFieldCount> hardwareTypeData = hardwareService.getHardwareTypeCount(query);

        ChartJsColors chartJsColors = new ChartJsColors();

        for (AttributeFieldCount hardware : hardwareTypeData) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(hardware.getAttrFieldId());
            String chartHardwareLabel = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");
            String hardwareTypeName = attrField != null ? Links.getAttrFieldIcon(requestContext, attrField).getString() : chartHardwareLabel;

            Map<String, String> map = new HashMap<>();
            map.put("countKey", hardwareTypeName);
            map.put("countValue", String.valueOf(hardware.getObjectCount()));
            map.put("path", AppPaths.ROOT + AppPaths.HARDWARE_LIST + "?cmd=groupBy&hardwareType=" + hardware.getAttrFieldId());
            map.put("chartLabel", StringUtils.encodeJavascript(chartHardwareLabel));
            map.put("color", chartJsColors.getColor());
            chartJsColors.incrCount();
            hardwareTypeCounts.add(map);
        }

        // Group by Hardware status.
        List<Map> hardwareStatusCounts = new ArrayList<>();
        List<AttributeFieldCount> hardwareStatusData = hardwareService.getHardwareStatusCount(query);

        for (AttributeFieldCount hardware : hardwareStatusData) {
            AttributeField attrField = (AttributeField) attrFieldStatusMap.get(hardware.getAttrFieldId());
            String hardwareStatusName = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");

            Map<String, String> map = new HashMap<>();
            map.put("countKey", hardwareStatusName);
            map.put("countValue", String.valueOf(hardware.getObjectCount()));
            map.put("path", AppPaths.ROOT + AppPaths.HARDWARE_LIST + "?cmd=groupBy&hardwareStatus=" + hardware.getAttrFieldId());
            map.put("chartLabel", StringUtils.encodeJavascript(hardwareStatusName));
            map.put("color", chartJsColors.getColor());
            chartJsColors.incrCount();
            hardwareStatusCounts.add(map);
        }

        // Group by Hardware location.
        List<Map> locationCounts = new ArrayList<>();
        List<AttributeFieldCount> hardwareLocationData = hardwareService.getHardwareLocationCount(query);

        for (AttributeFieldCount hardware : hardwareLocationData) {
            AttributeField attrField = (AttributeField) attrFieldLocMap.get(hardware.getAttrFieldId());
            String hardwareLocName = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");

            Map<String, String> map = new HashMap<>();
            map.put("countKey", hardwareLocName);
            map.put("countValue", String.valueOf(hardware.getObjectCount()));
            map.put("path", AppPaths.ROOT + AppPaths.HARDWARE_LIST + "?cmd=groupBy&hardwareLocation=" + hardware.getAttrFieldId());
            map.put("chartLabel", StringUtils.encodeJavascript(hardwareLocName));
            map.put("color", chartJsColors.getColor());
            chartJsColors.incrCount();
            locationCounts.add(map);
        }

        
        // Group by Hardware Rentout type.
        List<Map> hardwareRentoutTypeCounts = new ArrayList<>();
        List<AttributeFieldCount> hardwareRentoutTypeData = hardwareService.getHardwareRentoutTypeCount(query);

        for (AttributeFieldCount hardware : hardwareRentoutTypeData) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(hardware.getAttrFieldId());
            String chartHardwareLabel = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");
            String hardwareRentoutTypeName = attrField != null ? Links.getAttrFieldIcon(requestContext, attrField).getString() : chartHardwareLabel;

            Map<String, String> map = new HashMap<>();
            map.put("countKey", hardwareRentoutTypeName);
            map.put("countValue", String.valueOf(hardware.getObjectCount()));
            map.put("path", AppPaths.ROOT + AppPaths.HARDWARE_LIST + "?cmd=groupBy&hardwareType=" + hardware.getAttrFieldId());
            map.put("chartLabel", StringUtils.encodeJavascript(chartHardwareLabel));
            map.put("color", chartJsColors.getColor());
            chartJsColors.incrCount();
            hardwareRentoutTypeCounts.add(map);
        }
        
        // Group by Hardware Rentout by Customer.
        List<Map> hardwareRentoutByCustomerCounts = new ArrayList<>();
        List<AttributeFieldCount> hardwareRentoutByCustomerData = hardwareService.getHardwareRentoutByCustomerCount(query);

        for (AttributeFieldCount hardware : hardwareRentoutByCustomerData) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(hardware.getAttrFieldId());
            String chartHardwareLabel = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");
            String hardwareRentoutByCustomerName = attrField != null ? Links.getAttrFieldIcon(requestContext, attrField).getString() : chartHardwareLabel;

            Map<String, String> map = new HashMap<>();
            map.put("countKey", hardwareRentoutByCustomerName);
            map.put("countValue", String.valueOf(hardware.getObjectCount()));
            map.put("path", AppPaths.ROOT + AppPaths.HARDWARE_LIST + "?cmd=groupBy&hardwareType=" + hardware.getAttrFieldId());
            map.put("chartLabel", StringUtils.encodeJavascript(chartHardwareLabel));
            map.put("color", chartJsColors.getColor());
            chartJsColors.incrCount();
            hardwareRentoutByCustomerCounts.add(map);
        }
        
        // Group by Hardware Rentout by Duration.
        List<Map> hardwareRentoutByDurationCounts = new ArrayList<>();
        List<AttributeFieldCount> hardwareRentoutByDurationData = hardwareService.getHardwareRentoutByDurationCount(query);

        for (AttributeFieldCount hardware : hardwareRentoutByDurationData) {
            AttributeField attrField = (AttributeField) attrFieldTypeMap.get(hardware.getAttrFieldId());
            String chartHardwareLabel = attrField != null ? attrField.getName() : Localizer.getText(requestContext, "itMgmt.index.na");
            String hardwareRentoutByDuration = attrField != null ? Links.getAttrFieldIcon(requestContext, attrField).getString() : chartHardwareLabel;

            Map<String, String> map = new HashMap<>();
            map.put("countKey", hardwareRentoutByDuration);
            map.put("countValue", String.valueOf(hardware.getObjectCount()));
            map.put("path", AppPaths.ROOT + AppPaths.HARDWARE_LIST + "?cmd=groupBy&hardwareType=" + hardware.getAttrFieldId());
            map.put("chartLabel", StringUtils.encodeJavascript(chartHardwareLabel));
            map.put("color", chartJsColors.getColor());
            chartJsColors.incrCount();
            hardwareRentoutByDurationCounts.add(map);
        }        
        
        
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("linkList", links);
        standardTemplate.setAttribute("hardwareTypeCountList", hardwareTypeCounts);
        standardTemplate.setAttribute("hardwareStatusCounts", hardwareStatusCounts);
        standardTemplate.setAttribute("hardwareLocationCountList", locationCounts);
        standardTemplate.setAttribute("hardwareRentoutTypeCountList", hardwareRentoutTypeCounts);
        standardTemplate.setAttribute("hardwareRentoutByCustomerCountList", hardwareRentoutByCustomerCounts);
        standardTemplate.setAttribute("hardwareRentoutByDurationCountList", hardwareRentoutByDurationCounts);
        standardTemplate.setAttribute("warrantyExpirationLinks", warrantyExpirationLinks);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.1");
        header.setTitleClassNoLine();
        header.setSectionKey("itMgmt.hardwareIndex.numRecords", new Integer[]{numHardwareRecords});
        
        // Link to add hardware.
        if (user.hasPermission(AppPaths.HARDWARE_ADD)) {
            header.addHeaderCmds(new Link(requestContext).setTitleKey("itMgmt.cmd.hardwareAdd")
                    .setAjaxPath(AppPaths.HARDWARE_ADD));
        }
        
        
        // Link to Rental.
        if (user.hasPermission(AppPaths.HARDWARE_RENTAL_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_RENTAL_LIST + "?cmd=showAll");
            link.setTitleKey("itMgmt.cmd.rental");
            header.addHeaderCmds(link);
        }
        
        if (user.hasPermission(AppPaths.HARDWARE_RENTAL_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_RENTAL_LIST + "?cmd=showAll");
            link.setTitleKey("itMgmt.cmd.RefurbishedSale");
            header.addHeaderCmds(link);
        }
        if (user.hasPermission(AppPaths.HARDWARE_RENTAL_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_RENTAL_LIST + "?cmd=showAll");
            link.setTitleKey("itMgmt.cmd.amcsale");
            header.addHeaderCmds(link);
        }
        if (user.hasPermission(AppPaths.HARDWARE_RENTAL_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_RENTAL_LIST + "?cmd=showAll");
            link.setTitleKey("itMgmt.cmd.newsale");
            header.addHeaderCmds(link);
        }
        
        //
        // Template: HardwareSearchTemplate
        //
        HardwareSearchTemplate searchTemplate = standardTemplate.addTemplate(new HardwareSearchTemplate());
        searchTemplate.setFormAction(AppPaths.HARDWARE_LIST);
        searchTemplate.setHardwareTypeData(hardwareTypeData);
        searchTemplate.setHardwareStatusData(hardwareStatusData);
        searchTemplate.setHardwareLocationData(hardwareLocationData);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
