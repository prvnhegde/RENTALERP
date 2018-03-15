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
package com.kwoksys.biz.contacts.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * ContactUtils
 */
public class ContactUtils {

    /**
     * Returns the sortable Contact columns allowed.
     */
    public static List<String> getSortableContactColumnList() {
        return Arrays.asList("contact_first_name", "contact_last_name", "contact_title", "company_name");
    }

    /**
     * Checks whether a column is sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableColumn(String columnName) {
        return getSortableContactColumnList().contains(columnName);
    }

    /**
     * Returns the column headers for the list page.
     */
    public static List<String> getContactColumnHeader() {
        return Arrays.asList(ConfigManager.app.getContactColumns());
    }

    /**
     * Returns employee contact column headers.
     */
    public static List<String> getEmployeeContactColumnHeader() {
        return Arrays.asList("rownum", "contact_first_name", "contact_last_name", "contact_title", "contact_phone_work");
    }

    public static List<DataRow> formatContacts(RequestContext requestContext, List<Contact> contacts, List<String> columnHeaders,
            Counter counter) throws Exception {

        List<DataRow> dataList = new ArrayList<>();

        if (contacts.isEmpty()) {
            return dataList;
        }

        AccessUser user = requestContext.getUser();
        boolean canViewCompanyDetail = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_DETAIL);
        boolean canViewContactDetail = Access.hasPermission(user, AppPaths.CONTACTS_CONTACT_DETAIL);

        for (Contact contact : contacts) {
            List<String> columns = new ArrayList<>();

            for (String column : columnHeaders) {
                if (column.equals(Contact.FIRST_NAME)) {
                    Link link = new Link(requestContext);
                    link.setTitle(contact.getFirstName());

                    if (canViewContactDetail) {
                        link.setAjaxPath(AppPaths.CONTACTS_CONTACT_DETAIL + "?contactId=" + contact.getId());
                    }
                    columns.add(link.getString());

                } else if (column.equals(Contact.COMPANY_NAME)) {
                    Link link = new Link(requestContext);
                    link.setTitle(contact.getCompanyName());

                    if (canViewCompanyDetail) {
                        link.setAjaxPath(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + contact.getCompanyId());
                    }
                    columns.add(link.getString());

                } else if (column.equals(Contact.LAST_NAME)) {
                    columns.add(HtmlUtils.encode(contact.getLastName()));

                } else if (column.equals(Contact.TITLE)) {
                    columns.add(HtmlUtils.encode(contact.getTitle()));

                } else if (column.equals(Contact.WORK_PHONE)) {
                    columns.add(HtmlUtils.encode(contact.getPhoneWork()));

                } else if (column.equals(Contact.PRIMARY_EMAIL)) {
                    columns.add(HtmlUtils.encode(contact.getEmailPrimary()));

                } else if (column.equals(BaseObject.ROWNUM)) {
                    columns.add(counter.incr() + ".");

                } else if (column.equals(BaseObject.REL_DESCRIPTION)) {
                    columns.add(HtmlUtils.encode(contact.getRelDescription()));
                }
            }
            
            DataRow dataRow = new DataRow();
            dataRow.setRowId(String.valueOf(contact.getId()));
            dataRow.setColumns(columns);
            dataList.add(dataRow);
        }
        return dataList;
    }
}
