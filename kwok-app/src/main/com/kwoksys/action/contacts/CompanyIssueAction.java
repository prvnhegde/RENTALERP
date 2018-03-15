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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.action.issues.IssueListTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyTabs;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.CompanyIssueLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for showing Company Issues.
 */
public class CompanyIssueAction extends Action2 {

    public String list() throws Exception {
        AccessUser user = requestContext.getUser();

        CompanyForm actionForm = getBaseForm(CompanyForm.class);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(actionForm.getCompanyId());

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(ContactQueries.getOrderByColumn(Issue.CREATION_DATE));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        // Add hardware components.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_ISSUE_ADD)) {
            header.addHeaderCmds(new Link(requestContext).setAjaxPath(AppPaths.CONTACTS_COMPANY_ISSUE_ADD
                    + "?companyId=" + actionForm.getCompanyId()).setTitleKey("contactMgmt.cmd.companyIssueAdd"));
        }

        // Back to hardware list.
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST)) {
            header.addHeaderCmds(new Link(requestContext).setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST)
                    .setTitleKey("contactMgmt.cmd.companyList"));
        }

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(CompanyUtils.getCompanyTabs(requestContext, company));
        tabs.setTabActive(CompanyTabs.ISSUES_TAB);

        //
        // Template: IssueListTemplate
        //
        IssueListTemplate listTemplate = standardTemplate.addTemplate(new IssueListTemplate());
        listTemplate.setAccessUser(user);
        listTemplate.setQueryCriteria(queryCriteria);
        listTemplate.setObjectMap(new CompanyIssueLink(actionForm.getCompanyId()).createObjectMap());
        listTemplate.setFormRemoveItemAction(AppPaths.CONTACTS_COMPANY_ISSUE_REMOVE_2);
        listTemplate.setColumnHeaders(IssueUtils.getIssueColumnHeaders());
        listTemplate.setEmptyTableRowKey("issueMgmt.issueList.emptyTableMessage");
        listTemplate.getFormHiddenVariableMap().put("companyId", String.valueOf(company.getId()));

        listTemplate.setFormRowIdName("issueId");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String remove2() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");
        Integer issueId = requestContext.getParameter("issueId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        contactService.getCompany(companyId);

        CompanyIssueLink issueMap = new CompanyIssueLink();
        issueMap.setCompanyId(companyId);
        issueMap.setIssueId(issueId);

        // Delete Company Issue mapping.
        ActionMessages errors = contactService.deleteCompanyIssue(issueMap);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_ISSUES + "?companyId=" + companyId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_ISSUES + "?companyId=" + companyId);
        }
    }
}
