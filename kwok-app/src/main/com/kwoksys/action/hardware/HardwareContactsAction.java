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
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.ContactUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.HardwareContactLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for managing hardware contacts.
 */
public class HardwareContactsAction extends Action2 {

    public String list() throws Exception {
        getBaseForm(ContactAssociateForm.class);
        AccessUser user = requestContext.getUser();

        // Get request parameters
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Hardware members
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.HARDWARE_CONTACTS_ORDER_BY, Contact.FIRST_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.HARDWARE_CONTACTS_ORDER, QueryCriteria.ASCENDING);

        List<String> columnHeaders = Arrays.asList(Contact.FIRST_NAME, Contact.LAST_NAME, Contact.PRIMARY_EMAIL, BaseObject.REL_DESCRIPTION);

        QueryCriteria queryCriteria = new QueryCriteria();

        if (ContactUtils.isSortableColumn(orderBy)) {
            queryCriteria.addSortColumn(ContactQueries.getOrderByColumn(orderBy), order);
        }

        HardwareContactLink hardwareContact = new HardwareContactLink();
        hardwareContact.setHardwareId(hardwareId);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        List<Contact> contacts = contactService.getLinkedContacts(queryCriteria, hardwareContact.createObjectMap());
        List<DataRow> dataList = ContactUtils.formatContacts(requestContext, contacts, columnHeaders, new Counter());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});
        HardwareUtils.addHardwareHeaderCommands(requestContext, header, hardwareId);

        // Add hardware contact
        if (Access.hasPermission(user, AppPaths.HARDWARE_CONTACT_ADD)) {
            Link link = new Link(requestContext)
                    .setAjaxPath(AppPaths.HARDWARE_CONTACT_ADD + "?hardwareId=" + hardwareId)
                    .setTitleKey("common.linking.linkContacts");
            header.addHeaderCmds(link);
        }

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_CONTACT_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(ContactUtils.getSortableContactColumnList());
        tableTemplate.setColumnPath(AppPaths.HARDWARE_CONTACTS + "?hardwareId=" + hardwareId);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(null);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("contactMgmt.contactList.emptyTableMessage");
        tableTemplate.getFormHiddenVariableMap().put("hardwareId", String.valueOf(hardware.getId()));
        tableTemplate.setFormRowIdName("contactId");
        tableTemplate.setFormRemoveItemAction(AppPaths.HARDWARE_CONTACT_REMOVE);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add() throws Exception {
        HardwareForm actionForm = getBaseForm(HardwareForm.class);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        //
        // Template: ContactAssociateTemplate
        //
        ContactAssociateTemplate associateTemplate = standardTemplate.addTemplate(new ContactAssociateTemplate());
        associateTemplate.setFormContactId(actionForm.getFormContactId());
        associateTemplate.setFormSearchAction(AppPaths.HARDWARE_CONTACT_ADD + "?hardwareId=" + hardware.getId());
        associateTemplate.setFormSaveAction(AppPaths.HARDWARE_CONTACT_ADD_2 + "?hardwareId=" + hardware.getId());
        associateTemplate.setFormCancelAction(AppPaths.HARDWARE_CONTACTS + "?hardwareId=" + hardware.getId());

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add2() throws Exception {
        HardwareForm actionForm = saveActionForm(new HardwareForm());

        // Check to make sure the hardware exists
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        HardwareContactLink hardwareContactLink = new HardwareContactLink();
        hardwareContactLink.setHardwareId(hardware.getId());
        hardwareContactLink.setContactId(Integer.valueOf(actionForm.getContactId()));
        hardwareContactLink.setRelDescription(actionForm.getRelationshipDescription());

        SystemService systemService = ServiceProvider.getSystemService(requestContext);

        ActionMessages errors = systemService.addObjectMapping(hardwareContactLink.createObjectMap());
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_CONTACT_ADD + "?hardwareId=" + hardware.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.HARDWARE_CONTACTS + "?hardwareId=" + hardware.getId());
        }
    }
    
    public String remove() throws Exception {
        HardwareForm actionForm = getBaseForm(HardwareForm.class);

        // Check to make sure the hardware exists
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        HardwareContactLink hardwareContactLink = new HardwareContactLink();
        hardwareContactLink.setHardwareId(hardware.getId());
        hardwareContactLink.setContactId(Integer.valueOf(actionForm.getContactId()));

        // Delete hardware contact mapping
        SystemService systemService = ServiceProvider.getSystemService(requestContext);

        ActionMessages errors = systemService.deleteObjectMapping(hardwareContactLink.createObjectMap());

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_CONTACTS + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&hardwareId=" + hardware.getId());

        } else {
            return ajaxUpdateView(AppPaths.HARDWARE_CONTACTS + "?hardwareId=" + hardware.getId());
        }
    }    
}
