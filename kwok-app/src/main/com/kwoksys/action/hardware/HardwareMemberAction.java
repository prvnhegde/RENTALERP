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

import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dto.linking.HardwareMemberLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;

/**
 * Action class for showing hardware association.
 */
public class HardwareMemberAction extends Action2 {

    public String list() throws Exception {
        getBaseForm(HardwareMemberForm.class);

        AccessUser user = requestContext.getUser();

        Integer hardwareId = requestContext.getParameter("hardwareId");
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Hardware members
        String moOrderBy = SessionManager.getOrSetAttribute(requestContext, "moOrderBy", SessionManager.HARDWARE_MEMBER_OF_ORDER_BY, Hardware.HARDWARE_NAME);
        String moOrder = SessionManager.getOrSetAttribute(requestContext, "moOrder", SessionManager.HARDWARE_MEMBER_OF_ORDER, QueryCriteria.ASCENDING);

        String mOrderBy = SessionManager.getOrSetAttribute(requestContext, "mOrderBy", SessionManager.HARDWARE_MEMBER_ORDER_BY, Hardware.HARDWARE_NAME);
        String mOrder = SessionManager.getOrSetAttribute(requestContext, "mOrder", SessionManager.HARDWARE_MEMBER_ORDER, QueryCriteria.ASCENDING);

        boolean canRemoveHardware = Access.hasPermission(user, AppPaths.HARDWARE_MEMBER_REMOVE_2);

        // Get column headers
        List<String> hwColumnHeaders = new ArrayList<>();
        if (canRemoveHardware) {
            // Add an extra blank column to the headers, that's for the radio button to remove hardware
            hwColumnHeaders.add("");
        }
        hwColumnHeaders.addAll(HardwareUtils.getColumnHeaderList());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formRemoveHardwareAction", AppPaths.HARDWARE_MEMBER_REMOVE_2);
        standardTemplate.setPathAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});
        HardwareUtils.addHardwareHeaderCommands(requestContext, headerTemplate, hardwareId);

        // Add hardware member
        if (Access.hasPermission(user, AppPaths.HARDWARE_MEMBER_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_MEMBER_ADD + "?hardwareId=" + hardwareId);
            link.setTitleKey("hardware.cmd.hardwareMemberAdd");
            headerTemplate.addHeaderCmds(link);
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_MEMBER_TAB);

        //
        // Member of
        //
        
        //
        // Template: TableEmptyTemplate
        //
        TableEmptyTemplate empty = standardTemplate.addTemplate(new TableEmptyTemplate("_memberOf"));
        empty.setColSpan(HardwareUtils.getColumnHeaderList().size());
        empty.setRowText(Localizer.getText(requestContext, "hardware.members.emptyMemberOfMessage"));

        //
        // Template: TableHeaderTemplate
        //
        TableHeaderTemplate tableHeader = standardTemplate.addTemplate(new TableHeaderTemplate("_memberOf"));
        tableHeader.setColumnList(HardwareUtils.getColumnHeaderList());
        tableHeader.setSortableColumnList(HardwareUtils.getSortableColumns());
        tableHeader.setColumnPath(AppPaths.HARDWARE_MEMBER + "?hardwareId=" + hardwareId);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setOrderBy(moOrderBy);
        tableHeader.setOrderByParamName("moOrderBy");
        tableHeader.setOrder(moOrder);
        tableHeader.setOrderParamName("moOrder");

        // Do some sorting.
        QueryCriteria query = new QueryCriteria();

        if (HardwareUtils.isSortableColumn(moOrderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(moOrderBy), moOrder);
        }

        //
        // Template: HardwareListTemplate
        //
        HardwareListTemplate listTemplate = standardTemplate.addTemplate(new HardwareListTemplate("_memberOf"));
        listTemplate.setHardwareList(hardwareService.getHardwareParents(query, hardwareId));
        listTemplate.setHardwarePath(AppPaths.HARDWARE_MEMBER);
        listTemplate.setColspan(HardwareUtils.getColumnHeaderList().size());
        listTemplate.setListHeader("hardware.members.header.memberOf");
        listTemplate.getFormHiddenVariableMap().put("hardwareId", String.valueOf(hardware.getId()));

        //
        // Hardware Members
        //

        //
        // Template: TableEmptyTemplate
        //
        empty = standardTemplate.addTemplate(new TableEmptyTemplate("_members"));
        empty.setColSpan(hwColumnHeaders.size());
        empty.setRowText(Localizer.getText(requestContext, "hardware.members.emptyMemberMessage"));

        //
        // Template: TableHeaderTemplate
        //
        tableHeader = standardTemplate.addTemplate(new TableHeaderTemplate("_members"));
        tableHeader.setColumnList(hwColumnHeaders);
        tableHeader.setSortableColumnList(HardwareUtils.getSortableColumns());
        tableHeader.setColumnPath(AppPaths.HARDWARE_MEMBER + "?hardwareId=" + hardwareId);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setOrderBy(mOrderBy);
        tableHeader.setOrderByParamName("mOrderBy");
        tableHeader.setOrder(mOrder);
        tableHeader.setOrderParamName("mOrder");

        // Do some sorting.
        query = new QueryCriteria();

        if (HardwareUtils.isSortableColumn(mOrderBy)) {
            query.addSortColumn(HardwareQueries.getOrderByColumn(mOrderBy), mOrder);
        }

        //
        // Template: HardwareListTemplate
        //
        listTemplate = standardTemplate.addTemplate(new HardwareListTemplate("_members"));
        listTemplate.setHardwareList(hardwareService.getHardwareMembers(query, hardwareId));
        listTemplate.setHardwarePath(AppPaths.HARDWARE_MEMBER);
        listTemplate.setCanRemoveHardware(canRemoveHardware);        
        listTemplate.setColspan(hwColumnHeaders.size());
        listTemplate.getFormHiddenVariableMap().put("hardwareId", String.valueOf(hardware.getId()));

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String add() throws Exception {
        HardwareMemberForm actionForm = getBaseForm(HardwareMemberForm.class);

        AccessUser user = requestContext.getUser();

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        List<Map> hardwareList = new ArrayList<>();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, "add");
        standardTemplate.setAttribute("hardwareId", hardware.getId());
        standardTemplate.setPathAttribute("formSearchAction", AppPaths.HARDWARE_MEMBER_ADD + "?hardwareId=" + hardware.getId());
        standardTemplate.setPathAttribute("formSaveAction", AppPaths.HARDWARE_MEMBER_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_MEMBER + "?hardwareId=" + hardware.getId()).getString());

        if (!actionForm.getFormHardwareId().isEmpty()) {
            HardwareSearch hardwareSearch = new HardwareSearch();
            hardwareSearch.put(HardwareSearch.HARDWARE_ID_EQUALS, actionForm.getFormHardwareId());

            // Ready to pass variables to query.
            List<Hardware> hardwareDataset = hardwareService.getHardwareList(new QueryCriteria(hardwareSearch));
            if (!hardwareDataset.isEmpty()) {
                boolean hasHardwareAccess = Access.hasPermission(user, AppPaths.HARDWARE_DETAIL);

                for (Hardware memberHardware : hardwareDataset) {
                    Map<String, String> map = new HashMap<>();
                    map.put("hardwareId", String.valueOf(memberHardware.getId()));

                    Link hardwareLink = new Link(requestContext);
                    hardwareLink.setTitle(memberHardware.getName());
                    if (hasHardwareAccess) {
                        hardwareLink.setAjaxPath(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + memberHardware.getId());
                    }
                    map.put("hardwareName", hardwareLink.getString());
                    hardwareList.add(map);
                }
                standardTemplate.setAttribute("hardwareList", hardwareList);
            }
        }

        if (actionForm.getFormHardwareId().isEmpty()) {
            standardTemplate.setAttribute("selectHardwareMessage", "form.noSearchInput");
        } else if (hardwareList.isEmpty()) {
            standardTemplate.setAttribute("selectHardwareMessage", "form.noSearchResult");
        }

        if (hardwareList.isEmpty()) {
            standardTemplate.setAttribute("disableSaveButton", "disabled");
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String add2() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");
        Integer formHardwareId = requestContext.getParameter("formHardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        // Check to make sure the hardware exists
        hardwareService.getHardware(hardwareId);

        HardwareMemberLink memberMap = new HardwareMemberLink();
        memberMap.setHardwareId(hardwareId);
        memberMap.setMemberHardwareId(formHardwareId);

        ActionMessages errors = hardwareService.addHardwareMember(memberMap);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_MEMBER_ADD + "?hardwareId=" + hardwareId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.HARDWARE_MEMBER + "?hardwareId=" + hardwareId);
        }
    }
    
    public String remove() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");
        Integer formHardwareId = requestContext.getParameter("formHardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        // Verify the Hardware exists
        hardwareService.getHardware(hardwareId);

        HardwareMemberLink memberMap = new HardwareMemberLink();
        memberMap.setHardwareId(hardwareId);
        memberMap.setMemberHardwareId(formHardwareId);

        // Delete contract hardware mapping.
        ActionMessages errors = hardwareService.removeHardwareMember(memberMap);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_MEMBER + "?" + RequestContext.URL_PARAM_ERROR_TRUE+ "&hardwareId=" + hardwareId);

        } else {
            return ajaxUpdateView(AppPaths.HARDWARE_MEMBER + "?hardwareId=" + hardwareId);
        }
    }    
}