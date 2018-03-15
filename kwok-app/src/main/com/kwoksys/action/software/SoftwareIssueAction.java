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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.action.issues.IssueListTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.linking.SoftwareIssueLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Software Issues.
 */
public class SoftwareIssueAction extends Action2 {

    public String list() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        getBaseForm(SoftwareIssueForm.class);

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(SoftwareQueries.getOrderByColumn(Issue.CREATION_DATE));

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

        // Add Software Issue.
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_ISSUE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_ISSUE_ADD + "?softwareId=" + softwareId);
            link.setTitleKey("common.linking.linkIssue");
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
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(SoftwareUtils.getSoftwareTabs(requestContext, software));
        tabs.setTabActive(SoftwareUtils.ISSUES_TAB);

        //
        // Template: IssueListTemplate
        //
        IssueListTemplate listTemplate = standardTemplate.addTemplate(new IssueListTemplate());
        listTemplate.setAccessUser(accessUser);
        listTemplate.setQueryCriteria(queryCriteria);
        listTemplate.setObjectMap(new SoftwareIssueLink(softwareId).createObjectMap());
        listTemplate.setFormRemoveItemAction(AppPaths.SOFTWARE_ISSUE_REMOVE_2);
        listTemplate.setColumnHeaders(IssueUtils.getIssueColumnHeaders());
        listTemplate.setEmptyTableRowKey("issueMgmt.issueList.emptyTableMessage");
        listTemplate.getFormHiddenVariableMap().put("softwareId", String.valueOf(software.getId()));
        listTemplate.setFormRowIdName("issueId");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String remove2() throws Exception {
        Integer softwareId= requestContext.getParameter("softwareId");
        Integer issueId = requestContext.getParameter("issueId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Verify the Software exists
        softwareService.getSoftware(softwareId);

        SoftwareIssueLink issueMap = new SoftwareIssueLink();
        issueMap.setSoftwareId(softwareId);
        issueMap.setIssueId(issueId);

        // Delete contract software mapping.
        ActionMessages errors = softwareService.deleteSoftwareIssue(issueMap);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.SOFTWARE_ISSUE + "?softwareId=" + softwareId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.SOFTWARE_ISSUE + "?softwareId=" + softwareId);
        }
    }    
}
