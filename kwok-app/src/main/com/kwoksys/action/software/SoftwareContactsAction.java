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
package com.kwoksys.action.software;

import java.util.Arrays;
import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.action.contacts.ContactAssociateTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.contacts.core.ContactUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.SoftwareContactLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for managing software contacts.
 */
public class SoftwareContactsAction extends Action2 {

    public String list() throws Exception {
        Integer softwareId = requestContext.getParameterInteger("softwareId");

        AccessUser accessUser = requestContext.getUser();

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        getBaseForm(SoftwareContactForm.class);

        Software software = softwareService.getSoftware(softwareId);

        List<String> columnHeaders = Arrays.asList(Contact.FIRST_NAME, Contact.LAST_NAME, Contact.PRIMARY_EMAIL, BaseObject.REL_DESCRIPTION);

        String orderBy = Contact.FIRST_NAME;
        String order = QueryCriteria.ASCENDING;

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(ContactQueries.getOrderByColumn(orderBy));
        
        List<Contact> contacts = softwareService.getSoftwareContacts(queryCriteria, software.getId());
        List<DataRow> dataList = ContactUtils.formatContacts(requestContext, contacts, columnHeaders, new Counter());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        SoftwareUtils.addSoftwareHeaderCommands(requestContext, header, software.getId());
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        // Add Software contact
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_CONTACT_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_CONTACT_ADD + "?softwareId=" + software.getId());
            link.setTitleKey("common.linking.linkContacts");
            header.addHeaderCmds(link);
        }

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabsTemplate = standardTemplate.addTemplate(new TabsTemplate());
        tabsTemplate.setTabList(SoftwareUtils.getSoftwareTabs(requestContext, software));
        tabsTemplate.setTabActive(SoftwareUtils.CONTACTS_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setDataList(dataList);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnPath(AppPaths.SOFTWARE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setFormRemoveItemAction(AppPaths.SOFTWARE_CONTACT_REMOVE_2);
        tableTemplate.getFormHiddenVariableMap().put("softwareId", String.valueOf(software.getId()));
        tableTemplate.setFormRowIdName("contactId");
        tableTemplate.setEmptyRowMsgKey("contactMgmt.contactList.emptyTableMessage");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add() throws Exception {
        Integer softwareId = requestContext.getParameterInteger("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        SoftwareContactForm actionForm = getBaseForm(SoftwareContactForm.class);
        actionForm.setContactId(requestContext.getParameterInteger("formContactId"));

        Software software = softwareService.getSoftware(softwareId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("softwareId", software.getId());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        //
        // Template: ContactAssociateTemplate
        //
        ContactAssociateTemplate associateTemplate = standardTemplate.addTemplate(new ContactAssociateTemplate());
        associateTemplate.setFormContactId(actionForm.getFormContactId());
        associateTemplate.setFormSearchAction(AppPaths.SOFTWARE_CONTACT_ADD + "?softwareId=" + software.getId());
        associateTemplate.setFormSaveAction(AppPaths.SOFTWARE_CONTACT_ADD_2 + "?softwareId=" + software.getId());
        associateTemplate.setFormCancelAction(AppPaths.SOFTWARE_CONTACTS + "?softwareId=" + software.getId());

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add2() throws Exception {
        SoftwareContactForm contactForm = saveActionForm(new SoftwareContactForm());
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Check to make sure the software exists
        Software software = softwareService.getSoftware(contactForm.getSoftwareId());

        SoftwareContactLink softwareContactLink = new SoftwareContactLink();
        softwareContactLink.setSoftwareId(software.getId());
        softwareContactLink.setContactId(contactForm.getContactId());
        softwareContactLink.setRelDescription(contactForm.getRelationshipDescription());

        ActionMessages errors = softwareService.addSoftwareContact(softwareContactLink);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.SOFTWARE_CONTACT_ADD + "?softwareId=" + software.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.SOFTWARE_CONTACTS + "?softwareId=" + software.getId());
        }
    }

    public String remove2() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");
        Integer contactId = requestContext.getParameter("contactId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Verify the Software exists
        softwareService.getSoftware(softwareId);

        SoftwareContactLink softwareContactLink = new SoftwareContactLink();
        softwareContactLink.setSoftwareId(softwareId);
        softwareContactLink.setContactId(contactId);

        // Delete contract software mapping.
        ActionMessages errors = softwareService.deleteSoftwareContact(softwareContactLink);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.SOFTWARE_CONTACTS + "?softwareId=" + softwareId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.SOFTWARE_CONTACTS + "?softwareId=" + softwareId);
        }
    }
}
