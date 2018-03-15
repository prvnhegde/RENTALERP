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
package com.kwoksys.action.contacts;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.ContactSearch;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ContactAssociateTemplate
 */
public class ContactAssociateTemplate extends BaseTemplate {

    private Integer contactId;

    private String formContactId;

    private String formSearchAction;

    private String formSaveAction;

    private String formCancelAction;

    private List<Map> contactList = new ArrayList<>();

    public ContactAssociateTemplate() {
        super(ContactAssociateTemplate.class);
    }

    public void init() {
        addTemplate(new ActionErrorsTemplate());
    }

    public void applyTemplate() throws DatabaseException {
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        if (!formContactId.isEmpty()) {
            ContactSearch contactSearch = new ContactSearch();
            contactSearch.put(ContactSearch.CONTACT_ID_EQUALS, formContactId);
            contactSearch.put(ContactSearch.CONTACT_TYPE, ObjectTypes.COMPANY_EMPLOYEE_CONTACT);

            QueryCriteria queryCriteria = new QueryCriteria(contactSearch);
            List<Contact> contacts = contactService.getContacts(queryCriteria);

            if (!contacts.isEmpty()) {
                Contact contact = contacts.iterator().next();

                Map<String, String> map = new HashMap<>();
                map.put("contactId", String.valueOf(contact.getId()));
                contactId = contact.getId();

                StringBuilder contactDisplay = new StringBuilder();
                contactDisplay.append(contact.getLastName()).append(", ").append(contact.getFirstName());
                if (!contact.getEmailPrimary().isEmpty()) {
                    contactDisplay.append(" (").append(contact.getEmailPrimary()).append(")");
                }
                map.put("contactName", contactDisplay.toString());
                contactList.add(map);
            }
        }

        if (formContactId.isEmpty()) {
            request.setAttribute("selectContactMessage", "form.noSearchInput");
        } else if (contactList.isEmpty()) {
            request.setAttribute("selectContactMessage", "form.noSearchResult");
        }
    }

    @Override
    public String getJspPath() {
        return "/jsp/contacts/ContactAssociateTemplate.jsp";
    }

    public void setFormSaveAction(String formSaveAction) {
        this.formSaveAction = AppPaths.ROOT + formSaveAction;
    }

    public String getFormContactId() {
        return formContactId;
    }

    public void setFormContactId(String formContactId) {
        this.formContactId = formContactId;
    }

    public void setFormSearchAction(String formSearchAction) {
        this.formSearchAction = AppPaths.ROOT + formSearchAction;
    }

    public void setFormCancelAction(String formCancelAction) {
        this.formCancelAction = formCancelAction;
    }

    public String getFormSearchAction() {
        return formSearchAction;
    }

    public String getFormSaveAction() {
        return formSaveAction;
    }

    public String getFormCancelLink() {
        return Links.getCancelLink(requestContext, formCancelAction).getString();
    }

    public Integer getContactId() {
        return contactId;
    }

    public String getDisableSaveButton() {
        return contactList.isEmpty() ? "disabled" : "";
    }

    public List<Map> getContactList() {
        return contactList;
    }
}