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

import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting issue.
 */
public class IssueDeleteAction extends Action2 {

    public String delete() throws Exception {
        Integer issueId = requestContext.getParameter("issueId");

        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        Issue issue = issueService.getIssue(issueId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("issues.issueDelete.title");

        //
        // Template: IssueSpecTemplate
        //
        IssueSpecTemplate spec = standardTemplate.addTemplate(new IssueSpecTemplate(issue));
        spec.setHeaderText(issue.getSubject());

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = standardTemplate.addTemplate(new ObjectDeleteTemplate());
        delete.setFormAjaxAction(AppPaths.ISSUES_DELETE_2 + "?issueId=" + issueId);
        delete.setFormCancelAction(AppPaths.ISSUES_DETAIL + "?issueId=" + issueId);
        delete.setConfirmationMsgKey("issues.issueDelete.confirm");
        delete.setSubmitButtonKey("issues.issueDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String delete2() throws Exception {
        Integer issueId = requestContext.getParameter("issueId");
        List<Integer> issueIds = requestContext.getParameters("issueIds");

        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        if (!issueIds.isEmpty()) {
            for (Integer deleteIssueId : issueIds) {
                // Check to make sure the issue exists
                issueService.getIssue(deleteIssueId);
                ActionMessages errors = issueService.deleteIssue(deleteIssueId);

                if (!errors.isEmpty()) {
                    saveActionErrors(errors);
                    break;
                }
            }
        } else {
            // Check to make sure the issue exists
            issueService.getIssue(issueId);
            ActionMessages errors = issueService.deleteIssue(issueId);

            if (!errors.isEmpty()) {
                saveActionErrors(errors);
                return ajaxUpdateView(AppPaths.ISSUES_DELETE + "?issueId=" + issueId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
            }
        }
        return ajaxUpdateView(AppPaths.ISSUES_LIST);
    }    
}