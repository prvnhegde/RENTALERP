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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;

import org.apache.struts.util.LabelValueBean;

import java.util.*;

/**
 * Utility class for Company module.
 */
public class CompanyUtils {

    public static boolean isSortableColumn(String columnName) {
        return false;
    }

    public static List<DataRow> formatCompanyList(RequestContext requestContext, List<Company> companyDataset, Counter counter)
            throws DatabaseException {

        List<DataRow> list = new ArrayList<>();
        if (companyDataset == null) {
            return list;
        }

        boolean hasCompanyDetailsAccess = Access.hasPermission(requestContext.getUser(), AppPaths.CONTACTS_COMPANY_DETAIL);

        List<String> columnHeaders = ConfigManager.app.getContactsCompanyColumnList();

        for (Company company : companyDataset) {
            List<String> columns = new ArrayList<>();

            for (String column : columnHeaders) {
                if (column.equals(Company.COMPANY_ID)) {
                    columns.add(String.valueOf(company.getId()));

                } else if (column.equals(Company.COMPANY_NAME)) {
                    Link link = new Link(requestContext);
                    link.setTitle(company.getName());

                    if (hasCompanyDetailsAccess) {
                        link.setAjaxPath(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + company.getId());
                    }

                    columns.add(link.getString());

                } else if (column.equals(Company.ROWNUM)) {
                    columns.add(counter.incr() + ".");
                }
            }

            DataRow dataRow = new DataRow();
            dataRow.setColumns(columns);
            list.add(dataRow);
        }
        return list;
    }

    /**
     * Get company tabs.
     *
     * @return ..
     */
    public static List<Link> getCompanyTabs(RequestContext requestContext, Company company) throws DatabaseException {
        AccessUser user = requestContext.getUser();
        List<Link> tabList = new ArrayList<>();

        // Link to Company main contacts tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_DETAIL)) {
            tabList.add(new Link(requestContext).setName(CompanyTabs.MAIN_CONTACT_TAB)
                    .setAjaxPath(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + company.getId())
                    .setTitle(Localizer.getText(requestContext, "contactMgmt.tab.companyMainContacts",
                            new Object[] {company.getCountMainContact()})));
        }

        // Link to Company employee contacts tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_CONTACT)) {
            tabList.add(new Link(requestContext).setName(CompanyTabs.OTHER_CONTACT_TAB)
                    .setAjaxPath(AppPaths.CONTACTS_COMPANY_CONTACT + "?companyId=" + company.getId())
                    .setTitle(Localizer.getText(requestContext, "contactMgmt.tab.companyOtherContacts",
                            new Object[] {company.getCountEmployeeContact()})));
        }

        // Link to Company notes tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_NOTE)) {
            tabList.add(new Link(requestContext).setName(CompanyTabs.NOTES_TAB)
                    .setAjaxPath(AppPaths.CONTACTS_COMPANY_NOTE + "?companyId=" + company.getId())
                    .setTitle(Localizer.getText(requestContext, "contactMgmt.tab.companyNotes",
                            new Object[] {company.getCountNote()})));
        }

        // Link to Company files tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_FILES)) {
            tabList.add(new Link(requestContext).setName(CompanyTabs.FILES_TAB)
                    .setAjaxPath(AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + company.getId())
                    .setTitle(Localizer.getText(requestContext, "contactMgmt.tab.companyFiles",
                            new Object[] {company.getCountFile()})));
        }

        // Link to Company Bookmarks tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_BOOKMARK)) {
            tabList.add(new Link(requestContext).setName(CompanyTabs.BOOKMAKRS_TAB)
                    .setAjaxPath(AppPaths.CONTACTS_COMPANY_BOOKMARK + "?companyId=" + company.getId())
                    .setTitle(Localizer.getText(requestContext, "contactMgmt.tab.companyBookmarks",
                            new Object[] {company.getCountBookmark()})));
        }

        // Link to Company Issues tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_ISSUES)) {
            tabList.add(new Link(requestContext).setName(CompanyTabs.ISSUES_TAB)
                    .setAjaxPath(AppPaths.CONTACTS_COMPANY_ISSUES + "?companyId=" + company.getId())
                    .setTitleKey("contactMgmt.tab.companyIssues"));
        }

         // Link to Company Contracts tab.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_CONTRACTS)) {
            tabList.add(new Link(requestContext).setName(CompanyTabs.CONTRACTS_TAB)
                    .setAjaxPath(AppPaths.CONTACTS_COMPANY_CONTRACTS + "?companyId=" + company.getId())
                    .setTitleKey("contracts"));
        }
        
        return tabList;
    }

    /**
     * Gets a list of company objects in LabelValueBean format.
     *
     * @return ..
     */
    public static List<LabelValueBean> getCompanyOptions(RequestContext requestContext) throws DatabaseException {
        QueryCriteria query = new QueryCriteria();
        query.addSortColumn(Company.COMPANY_NAME);
        return getCompanyOptions(requestContext, query);
    }
    
    /**
     * Gets a list of company objects in LabelValueBean format.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public static List<LabelValueBean> getCompanyOptions(RequestContext requestContext, QueryCriteria query) throws DatabaseException {
        List<LabelValueBean> companyIdOptions = new ArrayList<>();

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        for (Company company : contactService.getCompanies(query)) {
            companyIdOptions.add(new LabelValueBean(company.getName(), String.valueOf(company.getId())));
        }
        return companyIdOptions;
    }

    public static final List<Integer> DEFAULT_COMPANY_TYPES = Arrays.asList(
            AttributeFieldIds.COMPANY_TYPE_HARDWARE_MANUFACTURER,
            AttributeFieldIds.COMPANY_TYPE_HARDWARE_VENDOR,
            AttributeFieldIds.COMPANY_TYPE_SOFTWARE_MAKER,
            AttributeFieldIds.COMPANY_TYPE_SOFTWARE_VENDOR);
}