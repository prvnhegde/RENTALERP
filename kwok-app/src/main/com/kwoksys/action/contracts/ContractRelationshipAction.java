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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableEmptyTemplate;
import com.kwoksys.action.common.template.TableHeaderTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.action.hardware.HardwareListTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.software.SoftwareSearch;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dto.linking.ContractHardwareLink;
import com.kwoksys.biz.system.dto.linking.ContractSoftwareLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for managing contract relationships.
 */
public class ContractRelationshipAction extends Action2 {

    public String list() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        Integer contractId = requestContext.getParameter("contractId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        getBaseForm(ContractRelationshipForm.class);

        //
        // Linked hardware
        //
        String hwOrderBy = SessionManager.getOrSetAttribute(requestContext, "hwOrderBy", SessionManager.HARDWARE_ORDER_BY, Hardware.HARDWARE_NAME);
        String hwOrder = SessionManager.getOrSetAttribute(requestContext, "hwOrder", SessionManager.HARDWARE_ORDER, QueryCriteria.ASCENDING);

        boolean canRemoveHardware = Access.hasPermission(accessUser, AppPaths.CONTRACTS_HARDWARE_REMOVE_2);

        // Get column headers
        List<String> hwColumnHeaders = new ArrayList<>();
        if (canRemoveHardware) {
            // Add an extra blank column to the headers, that's for the radio button to remove hardware
            hwColumnHeaders.add("");
        }
        hwColumnHeaders.addAll(HardwareUtils.getColumnHeaderList());

        QueryCriteria queryCriteria = new QueryCriteria();

        if (HardwareUtils.isSortableColumn(hwOrderBy)) {
            queryCriteria.addSortColumn(HardwareQueries.getOrderByColumn(hwOrderBy), hwOrder);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("ajaxHardwareDetailPath", AppPaths.IT_MGMT_AJAX_GET_HARDWARE_DETAIL + "?hardwareId=");
        standardTemplate.setPathAttribute("formRemoveHardwareAction", AppPaths.CONTRACTS_HARDWARE_REMOVE_2);

        //
        // Template: HardwareListTemplate
        //
        HardwareListTemplate listTemplate = standardTemplate.addTemplate(new HardwareListTemplate("_hardware"));
        listTemplate.setHardwareList(contractService.getContractHardwareList(queryCriteria, contractId));
        listTemplate.setCanRemoveHardware(canRemoveHardware);
        listTemplate.setColspan(hwColumnHeaders.size());
        listTemplate.setCounter(new Counter());
        listTemplate.getFormHiddenVariableMap().put("contractId", String.valueOf(contract.getId()));

        //
        // Template: TableEmptyTemplate
        //
        TableEmptyTemplate empty = standardTemplate.addTemplate(new TableEmptyTemplate("_hardware"));
        empty.setColSpan(hwColumnHeaders.size());
        empty.setRowText(Localizer.getText(requestContext, "itMgmt.hardwareList.emptyTableMessage"));

        //
        // Template: TableHeaderTemplate
        //
        TableHeaderTemplate tableHeader = standardTemplate.addTemplate(new TableHeaderTemplate("_hardware"));
        tableHeader.setColumnList(hwColumnHeaders);
        tableHeader.setSortableColumnList(HardwareUtils.getSortableColumns());
        tableHeader.setColumnPath(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contractId);
        tableHeader.setColumnTextKey("common.column.");
        tableHeader.setOrderBy(hwOrderBy);
        tableHeader.setOrderByParamName("hwOrderBy");
        tableHeader.setOrderParamName("hwOrder");
        tableHeader.setOrder(hwOrder);

        //
        // Linked software
        //
        String swOrderBy = SessionManager.getOrSetAttribute(requestContext, "swOrderBy", SessionManager.SOFTWARE_ORDER_BY, Software.NAME);
        String swOrder = SessionManager.getOrSetAttribute(requestContext, "swOrder", SessionManager.SOFTWARE_ORDER, QueryCriteria.ASCENDING);

        // Do some sorting.
        QueryCriteria swQuery = new QueryCriteria();

        if (SoftwareUtils.isSortableColumn(swOrderBy)) {
            swQuery.addSortColumn(SoftwareQueries.getOrderByColumn(swOrderBy), swOrder);
        }

        List<Software> softwareList = contractService.getContractSoftwareList(swQuery, contractId);
        List<DataRow> formattedList = SoftwareUtils.formatSoftwareList(requestContext, softwareList, new Counter());

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate("_software"));
        tableTemplate.setDataList(formattedList);
        tableTemplate.setColumnHeaders(SoftwareUtils.getColumnHeaderList());
        tableTemplate.setColumnPath(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contractId);
        tableTemplate.setSortableColumnHeaders(SoftwareUtils.getSortableColumns());
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setEmptyRowMsgKey("itMgmt.softwareList.emptyTableMessage");
        tableTemplate.setOrderBy(swOrderBy);
        tableTemplate.setOrder(swOrder);
        tableTemplate.setOrderByParamName("swOrderBy");
        tableTemplate.setOrderParamName("swOrder");
        tableTemplate.setFormRemoveItemAction(AppPaths.CONTRACTS_SOFTWARE_REMOVE_2);
        tableTemplate.getFormHiddenVariableMap().put("contractId", String.valueOf(contract.getId()));
        tableTemplate.setFormRowIdName("formSoftwareId");

        //
        // Template: ContractSpecTemplate
        //
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        if (Access.hasPermission(accessUser, AppPaths.CONTRACTS_HARDWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_HARDWARE_ADD + "?contractId=" + contractId);
            link.setTitleKey("common.linking.linkHardware");
            header.addHeaderCmds(link);
        }

        if (Access.hasPermission(accessUser, AppPaths.CONTRACTS_SOFTWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_SOFTWARE_ADD + "?contractId=" + contractId);
            link.setTitleKey("common.linking.linkSoftware");
            header.addHeaderCmds(link);
        }

        Link link = new Link(requestContext);
        link.setAjaxPath(AppPaths.CONTRACTS_LIST);
        link.setTitleKey("itMgmt.cmd.contractList");
        header.addHeaderCmds(link);

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(ContractUtils.contractTabList(requestContext, contract));
        tabs.setTabActive(ContractUtils.HARDWARE_TAB);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String hardwareAdd() throws Exception {
        AccessUser user = requestContext.getUser();

        ContractRelationshipForm actionForm = getBaseForm(ContractRelationshipForm.class);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(actionForm.getContractId());

        List<Map> hardwareList = new ArrayList<>();

        if (!actionForm.getFormHardwareId().isEmpty()) {
            HardwareSearch hardwareSearch = new HardwareSearch();
            hardwareSearch.put(HardwareSearch.HARDWARE_ID_EQUALS, actionForm.getFormHardwareId());

            // Ready to pass variables to query.
            List<Hardware> hardwareDataset = hardwareService.getHardwareList(new QueryCriteria(hardwareSearch));
            if (!hardwareDataset.isEmpty()) {
                boolean hasHardwareAccess = Access.hasPermission(user, AppPaths.HARDWARE_DETAIL);

                for (Hardware hardware : hardwareDataset) {
                    Map<String, String> map = new HashMap<>();
                    map.put("hardwareId", String.valueOf(hardware.getId()));
                    actionForm.setHardwareId(hardware.getId());

                    Link hardwareNameLink = new Link(requestContext);
                    hardwareNameLink.setTitle(hardware.getName());

                    if (hasHardwareAccess) {
                        hardwareNameLink.setAjaxPath(AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.getId());
                    }
                    map.put("hardwareName", hardwareNameLink.getString());
                    hardwareList.add(map);
                }
                request.setAttribute("hardwareList", hardwareList);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("contractId", actionForm.getContractId());
        standardTemplate.setPathAttribute("formSearchAction", AppPaths.CONTRACTS_HARDWARE_ADD + "?contractId=" + actionForm.getContractId());
        standardTemplate.setPathAttribute("formSaveAction", AppPaths.CONTRACTS_HARDWARE_ADD_2 + "?contractId=" + actionForm.getContractId());

        if (actionForm.getFormHardwareId().isEmpty()) {
            standardTemplate.setAttribute("selectHardwareMessage", "form.noSearchInput");
        } else if (hardwareList.isEmpty()) {
            standardTemplate.setAttribute("selectHardwareMessage", "form.noSearchResult");
        }

        if (hardwareList.isEmpty()) {
            standardTemplate.setAttribute("disableSaveButton", "disabled");
        }
        
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTRACTS_ITEMS + "?contractId=" + actionForm.getContractId()).getString());

        //
        // Template: ContractSpecTemplate
        //
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String hardwareAdd2() throws Exception {
        ContractRelationshipForm actionForm = getBaseForm(ContractRelationshipForm.class);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(actionForm.getContractId());

        ContractHardwareLink contractHardware = new ContractHardwareLink();
        contractHardware.setContractId(actionForm.getContractId());
        contractHardware.setHardwareId(actionForm.getHardwareId());

        ActionMessages errors = contractService.addContractHardware(contractHardware);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTRACTS_HARDWARE_ADD + "?contractId=" + contract.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contract.getId());
        }
    }
    
    public String hardwareRemove2() throws Exception {
        Integer contractId = requestContext.getParameter("contractId");
        Integer hardwareId = requestContext.getParameter("formHardwareId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        // Verify contract exists
        Contract contract = contractService.getContract(contractId);

        ContractHardwareLink contractHardware = new ContractHardwareLink();
        contractHardware.setContractId(contract.getId());
        contractHardware.setHardwareId(hardwareId);

        // Delete contract hardware mapping.
        contractService.deleteContractHardware(contractHardware);

        return ajaxUpdateView(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contractId);
    }
    
    public String softwareAdd() throws Exception {
        AccessUser user = requestContext.getUser();

        ContractRelationshipForm actionForm = getBaseForm(ContractRelationshipForm.class);

        SoftwareService service = ServiceProvider.getSoftwareService(requestContext);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(actionForm.getContractId());

        List<Map> formattedList = new ArrayList<>();

        if (!actionForm.getFormSoftwareId().isEmpty()) {
            SoftwareSearch softwareSearch = new SoftwareSearch();
            softwareSearch.put("softwareIdEquals", actionForm.getFormSoftwareId());

            // Ready to pass variables to query.
            List<Software> softwareList = service.getSoftwareList(new QueryCriteria(softwareSearch));
            if (!softwareList.isEmpty()) {
                boolean viewSoftwareDetail = Access.hasPermission(user, AppPaths.SOFTWARE_DETAIL);

                for (Software software : softwareList) {
                    Map<String, String> map = new HashMap<>();
                    map.put("softwareId", String.valueOf(software.getId()));
                    actionForm.setSoftwareId(software.getId());

                    Link link = new Link(requestContext);
                    link.setTitle(software.getName());
                    if (viewSoftwareDetail) {
                        link.setAjaxPath(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + software.getId());
                    }
                    map.put("softwareName", link.getString());
                    formattedList.add(map);
                }
                request.setAttribute("softwareList", formattedList);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("contractId", actionForm.getContractId());
        standardTemplate.setPathAttribute("formSearchAction", AppPaths.CONTRACTS_SOFTWARE_ADD + "?contractId=" + actionForm.getContractId());
        standardTemplate.setPathAttribute("formSaveAction", AppPaths.CONTRACTS_SOFTWARE_ADD_2 + "?contractId=" + actionForm.getContractId());

        if (actionForm.getFormSoftwareId().isEmpty()) {
            standardTemplate.setAttribute("selectSoftwareMessage", "form.noSearchInput");
        } else if (formattedList.isEmpty()) {
            standardTemplate.setAttribute("selectSoftwareMessage", "form.noSearchResult");
        }

        if (formattedList.isEmpty()) {
            standardTemplate.setAttribute("disableSaveButton", "disabled");
        }
        
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTRACTS_ITEMS + "?contractId=" + actionForm.getContractId()).getString());

        //
        // Template: ContractSpecTemplate
        //
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String softwareAdd2() throws Exception {
        ContractRelationshipForm actionForm = getBaseForm(ContractRelationshipForm.class);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(actionForm.getContractId());

        ContractSoftwareLink contractSoftware = new ContractSoftwareLink();
        contractSoftware.setContractId(actionForm.getContractId());
        contractSoftware.setSoftwareId(actionForm.getSoftwareId());

        ActionMessages errors = contractService.addContractSoftware(contractSoftware);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTRACTS_SOFTWARE_ADD + "?contractId=" + contract.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contract.getId());
        }
    }
    
    public String softwareRemove2() throws Exception {
        Integer contractId = requestContext.getParameter("contractId");
        Integer softwareId= requestContext.getParameter("formSoftwareId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        // Verify contract exists
        Contract contract = contractService.getContract(contractId);

        ContractSoftwareLink map = new ContractSoftwareLink();
        map.setContractId(contract.getId());
        map.setSoftwareId(softwareId);

        // Delete contract hardware mapping.
        contractService.deleteContractSoftware(map);

        return ajaxUpdateView(AppPaths.CONTRACTS_ITEMS + "?contractId=" + contractId);
    }    
}