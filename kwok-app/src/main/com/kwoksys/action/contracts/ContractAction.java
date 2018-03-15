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

import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

/**
 * Action class for contract detail.
 */
public class ContractAction extends Action2 {

    public String detail() throws Exception {
        AccessUser user = requestContext.getUser();
        Integer contractId = requestContext.getParameter("contractId");
        
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.FILES_ORDER_BY, File.NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.FILES_ORDER, QueryCriteria.ASCENDING);

        FileService fileService = ServiceProvider.getFileService(requestContext);
        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        String fileDownloadPath = AppPaths.CONTRACTS_FILE_DOWNLOAD + "?contractId=" + contractId + "&fileId=";
        String fileDeletePath = AppPaths.CONTRACTS_FILE_DELETE + "?contractId=" + contractId + "&fileId=";
        String fileEditPath = AppPaths.CONTRACTS_FILE_EDIT + "?contractId=" + contractId + "&fileId=";

        boolean canDeleteFile = user.hasPermission(AppPaths.CONTRACTS_FILE_DELETE);
        boolean canDownloadFile = user.hasPermission(AppPaths.CONTRACTS_FILE_DOWNLOAD);

        QueryCriteria queryCriteria = new QueryCriteria();
        if (FileUtils.isSortableColumn(orderBy)) {
            queryCriteria.addSortColumn(FileQueries.getOrderByColumn(orderBy), order);
        }

        List<File> fileList = contractService.getContractFiles(queryCriteria, contractId);

        List<String> columnHeaders = new ArrayList<>(FileUtils.getFileColumnHeaders());
        if (canDeleteFile) {
            columnHeaders.add("command");
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new String[] {contract.getName()});

        // Edit contract link.
        if (user.hasPermission(AppPaths.CONTRACTS_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_EDIT + "?contractId=" + contractId);
            link.setTitleKey("itMgmt.cmd.contractEdit");
            header.addHeaderCmds(link);
        }

        // Delete contract link.
        if (user.hasPermission(AppPaths.CONTRACTS_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_DELETE + "?contractId=" + contractId);
            link.setTitleKey("itMgmt.cmd.contractDelete");
            header.addHeaderCmds(link);
        }

        if (user.hasPermission(AppPaths.CONTRACTS_FILE_ADD)) {
            Link link = new Link(requestContext);
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getContractFileRepositoryLocation())) {
                link.setAjaxPath(AppPaths.CONTRACTS_FILE_ADD + "?contractId=" + contractId);
                link.setImgSrc(Image.getInstance().getFileAddIcon());
            } else {
                link.setImgAltKey("files.warning.invalidPath");
                link.setImgSrc(Image.getInstance().getSignWarning());
            }
            header.addHeaderCmds(link);
        }

        Link link = new Link(requestContext);
        link.setAjaxPath(AppPaths.CONTRACTS_LIST);
        link.setTitleKey("itMgmt.cmd.contractList");
        header.addHeaderCmds(link);

        //
        // Template: ContractSpecTemplate
        //
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.CONTRACT);
        customFieldsTemplate.setObjectId(contractId);
        customFieldsTemplate.setObjectAttrTypeId(contract.getType());
        customFieldsTemplate.setShowDefaultHeader(false);

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(ContractUtils.contractTabList(requestContext, contract));
        tabs.setTabActive(ContractUtils.FILES_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableHeader = standardTemplate.addTemplate(new TableTemplate());
        tableHeader.setColumnPath(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contractId);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setColumnTextKey("files.colName.");
        tableHeader.setSortableColumnHeaders(FileUtils.getSortableColumns());
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setEmptyRowMsgKey("files.noAttachments");

        if (!fileList.isEmpty()) {
            Counter counter = new Counter();

            for (File file : fileList) {
                List<String> columns = new ArrayList<>();
                columns.add(counter.incr() + ".");
                // Show a download link
                columns.add(Links.getFileIconLink(requestContext, canDownloadFile, file.getLogicalName(), fileDownloadPath + file.getId()).getString());
                columns.add(HtmlUtils.encode(file.getTitle()));
                columns.add(file.getCreationDate());
                columns.add(FileUtils.formatFileSize(requestContext, file.getSize()));

                if (canDeleteFile) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(new Link(requestContext).setAjaxPath(fileEditPath + file.getId()).setTitleKey("form.button.edit").getString());
                    sb.append(" | ");
                    sb.append(new Link(requestContext).setAjaxPath(fileDeletePath + file.getId()).setTitleKey("form.button.delete").getString());
                    
                    columns.add(sb.toString());
                }
                tableHeader.addRow(columns);
            }
        }

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String add() throws Exception {
        ContractForm actionForm = getBaseForm(ContractForm.class);

        Contract contract = new Contract();

        // Load attributes
        contract.loadAttrs(requestContext);

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setContract(contract);
        }

        List<LabelValueBean> typeOptions = new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.CONTRACT_TYPE);

        List<LabelValueBean> contractStageOptions = new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.CONTRACT_STAGE);

        List<LabelValueBean> renewalOptions = new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.CONTRACT_RENEWAL_TYPE);

        // Contract owner options.
        List<LabelValueBean> contractOwnerOptions = new ArrayList<>();
        contractOwnerOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        contractOwnerOptions.addAll(AdminUtils.getUserOptions(requestContext));

        // Get company list
        List<LabelValueBean> companyOptions = new ArrayList<>();
        companyOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        companyOptions.addAll(CompanyUtils.getCompanyOptions(requestContext));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("contract", contract);
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTRACTS_ADD_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.CONTRACTS_ADD);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTRACTS_LIST).getString());
        standardTemplate.setAttribute("contractTypeOptions", typeOptions);
        standardTemplate.setAttribute("contractStageOptions", contractStageOptions);
        standardTemplate.setAttribute("contractOwnerOptions", contractOwnerOptions);
        standardTemplate.setAttribute("contractRenewalTypeOptions", renewalOptions);
        standardTemplate.setAttribute("dateOptions", CalendarUtils.getDateOptions(requestContext));
        standardTemplate.setAttribute("monthOptions", CalendarUtils.getMonthOptions(requestContext));
        standardTemplate.setAttribute("yearOptions", CalendarUtils.getYearOptions(requestContext));
        standardTemplate.setAttribute("contractProviderOptions", companyOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("itMgmt.contractAdd.title");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.CONTRACT);
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getContractType());
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add2() throws Exception {
        ContractForm actionForm = saveActionForm(new ContractForm());
        Contract contract = new Contract();
        contract.setName(actionForm.getContractName());
        contract.setDescription(actionForm.getContractDescription());
        contract.setOwnerId(actionForm.getContractOwner());
        contract.setType(actionForm.getContractType());
        contract.setStage(actionForm.getContractStage());
        contract.setContractProviderId(actionForm.getContractProviderId());
        contract.setEffectiveDateYear(actionForm.getContractEffectiveDateYear());
        contract.setEffectiveDateMonth(actionForm.getContractEffectiveDateMonth());
        contract.setEffectiveDateDate(actionForm.getContractEffectiveDateDate());
        contract.setExpireDateYear(actionForm.getContractExpirationDateYear());
        contract.setExpireDateMonth(actionForm.getContractExpirationDateMonth());
        contract.setExpireDateDate(actionForm.getContractExpirationDateDate());
        contract.setRenewalDateYear(actionForm.getContractRenewalDateYear());
        contract.setRenewalDateMonth(actionForm.getContractRenewalDateMonth());
        contract.setRenewalDateDate(actionForm.getContractRenewalDateDate());
        contract.setRenewalType(actionForm.getContractRenewalType());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.CONTRACT);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, contract, customAttributes);

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        ActionMessages errors = contractService.addContract(contract, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTRACTS_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
            
        } else {
            return ajaxUpdateView(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
        }
    }
}
