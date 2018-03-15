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

import java.util.Arrays;
import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.action.contacts.ContactAssociateForm;
import com.kwoksys.action.contacts.ContactAssociateTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.contacts.core.ContactUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.ContractContactLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for contract contacts.
 */
public class ContractContactsAction extends Action2 {

    public String list() throws Exception {
        getBaseForm(ContactAssociateForm.class);

        AccessUser accessUser = requestContext.getUser();

        Integer contractId = requestContext.getParameter("contractId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        List<String> columnHeaders = Arrays.asList(Contact.FIRST_NAME, Contact.LAST_NAME, Contact.PRIMARY_EMAIL, BaseObject.REL_DESCRIPTION);

        String orderBy = Contact.FIRST_NAME;
        String order = QueryCriteria.ASCENDING;

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(ContactQueries.getOrderByColumn(orderBy));

        List<Contact> contacts = contractService.getContractContacts(queryCriteria, contract.getId());
        List<DataRow> dataList = ContactUtils.formatContacts(requestContext, contacts, columnHeaders, new Counter());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        // Add contract contact.
        if (Access.hasPermission(accessUser, AppPaths.CONTRACTS_CONTACT_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_CONTACT_ADD + "?contractId=" + contract.getId());
            link.setTitleKey("common.linking.linkContacts");
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
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(ContractUtils.contractTabList(requestContext, contract));
        tabs.setTabActive(ContractUtils.CONTACTS_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setFormRemoveItemAction(AppPaths.CONTRACTS_CONTACT_REMOVE_2);
        tableTemplate.getFormHiddenVariableMap().put("contractId", String.valueOf(contract.getId()));
        tableTemplate.setFormRowIdName("contactId");
        tableTemplate.setEmptyRowMsgKey("contactMgmt.contactList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add() throws Exception {
        ContractForm actionForm = getBaseForm(ContractForm.class);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(actionForm.getContractId());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("contractId", contract.getId());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        //
        // Template: ContractSpecTemplate
        //
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));

        //
        // Template: ContactAssociateTemplate
        //
        ContactAssociateTemplate associateTemplate = standardTemplate.addTemplate(new ContactAssociateTemplate());
        associateTemplate.setFormContactId(actionForm.getFormContactId());
        associateTemplate.setFormSearchAction(AppPaths.CONTRACTS_CONTACT_ADD + "?contractId=" + contract.getId());
        associateTemplate.setFormSaveAction(AppPaths.CONTRACTS_CONTACT_ADD_2 + "?contractId=" + contract.getId());
        associateTemplate.setFormCancelAction(AppPaths.CONTRACTS_CONTACTS + "?contractId=" + contract.getId());

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add2() throws Exception {
        ContractForm actionForm = saveActionForm(new ContractForm());

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        // Check to make sure the software exists
        Contract contract = contractService.getContract(actionForm.getContractId());

        ContractContactLink link = new ContractContactLink();
        link.setContractId(contract.getId());
        link.setContactId(actionForm.getContactId());
        link.setRelDescription(actionForm.getRelationshipDescription());

        ActionMessages errors = contractService.addContractContact(link);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTRACTS_CONTACT_ADD + "?contractId=" + contract.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.CONTRACTS_CONTACTS + "?contractId=" + contract.getId());
        }
    }
    
    public String remove2() throws Exception {
        ContractForm actionForm = saveActionForm(new ContractForm());

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        // Verify contract exists
        Contract contract = contractService.getContract(actionForm.getContractId());

        ContractContactLink link = new ContractContactLink();
        link.setContractId(contract.getId());
        link.setContactId(actionForm.getContactId());

        // Delete contract contact mapping.
        ActionMessages errors = contractService.deleteContractContact(link);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTRACTS_CONTACTS + "?contractId=" + actionForm.getContractId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return ajaxUpdateView(AppPaths.CONTRACTS_CONTACTS + "?contractId=" + actionForm.getContractId());
        }
    }
}