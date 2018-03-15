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
package com.kwoksys.action.issues;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.IssueAccess;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueSearch;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IssueAssociateTemplate
 */
public class IssueAssociateTemplate extends BaseTemplate {

    private String issueId;

    private Integer linkedObjectTypeId;

    private Integer linkedObjectId;

    private String formSearchAction;

    private String formSaveAction;

    private String formCancelAction;

    public IssueAssociateTemplate() {
        super(IssueAssociateTemplate.class);
    }

    public void init() {
        addTemplate(new ActionErrorsTemplate());
    }

    public void applyTemplate() throws DatabaseException {
        AccessUser user = requestContext.getUser();
        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        List<Map> issueList = new ArrayList<>();
        
        if (issueId.isEmpty()) {
            request.setAttribute("selectIssueMessage", "form.noSearchInput");
        } else if (issueList.isEmpty()) {
            request.setAttribute("selectIssueMessage", "form.noSearchResult");
        }

        if (Access.hasPermission(user, AppPaths.ISSUES_ADD)) {
            request.setAttribute("issueAddLink", new Link(requestContext).setAjaxPath(AppPaths.ISSUES_ADD + "?linkedObjectTypeId="
                    + linkedObjectTypeId + "&linkedObjectId=" + linkedObjectId).setTitleKey("system.createLinkIssues").getString());
        }

        if (!issueId.isEmpty()) {
            IssueSearch issueSearch = new IssueSearch();
            issueSearch.put(IssueSearch.ISSUE_ID_EQUALS, issueId);

            List<Issue> issues = issueService.getIssues(new QueryCriteria(issueSearch));
            if (!issues.isEmpty()) {
                boolean hasIssueDetailAccess = Access.hasPermission(user, AppPaths.ISSUES_DETAIL);

                IssueAccess access = new IssueAccess(user);

                // For access control
                if (hasIssueDetailAccess && !access.hasReadAllPermission()) {
                    issueSearch.put(IssueSearch.ISSUE_PERMITTED_USER_ID, user.getId());
                    access.setAllowedIssues(issueService.getIssueIds(new QueryCriteria(issueSearch)));
                }

                for (Issue issue : issues) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("issueId", issue.getId());

                    Link link = new Link(requestContext);
                    link.setTitle(issue.getSubject());
                    if (hasIssueDetailAccess && access.hasPermission(issue.getId())) {
                        link.setAjaxPath(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());
                    }
                    map.put("issueTitle", link.getString());
                    issueList.add(map);
                }
                request.setAttribute("issueList", issueList);
            }
        }

        if (issueList.isEmpty()) {
            request.setAttribute("disableSaveButton", "disabled");
        }

        request.setAttribute("formSearchAction", AppPaths.ROOT + formSearchAction);
        request.setAttribute("formSaveAction", AppPaths.ROOT + formSaveAction);
        request.setAttribute("formCancelLink", Links.getCancelLink(requestContext, formCancelAction).getString());
    }

    @Override
    public String getJspPath() {
        return "/jsp/issues/IssueAssociateTemplate.jsp";
    }

    public void setFormSaveAction(String formSaveAction) {
        this.formSaveAction = formSaveAction;
    }

    public Integer getLinkedObjectTypeId() {
        return linkedObjectTypeId;
    }

    public void setLinkedObjectTypeId(Integer linkedObjectTypeId) {
        this.linkedObjectTypeId = linkedObjectTypeId;
    }

    public Integer getLinkedObjectId() {
        return linkedObjectId;
    }

    public void setLinkedObjectId(Integer linkedObjectId) {
        this.linkedObjectId = linkedObjectId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getFormSearchAction() {
        return formSearchAction;
    }

    public void setFormSearchAction(String formSearchAction) {
        this.formSearchAction = formSearchAction;
    }

    public String getFormCancelAction() {
        return formCancelAction;
    }

    public void setFormCancelAction(String formCancelAction) {
        this.formCancelAction = formCancelAction;
    }

    public String getFormSaveAction() {
        return formSaveAction;
    }
}
