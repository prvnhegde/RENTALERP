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
package com.kwoksys.action.issueplugin;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.issues.IssueForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for reporting a new issue.
 */
public class IssueAdd2Action extends Action2 {

    public String execute() throws Exception {
        // Get request parameters
        IssueForm actionForm = saveActionForm(new IssueForm());
        Issue issue = new Issue();
        issue.setSubject(actionForm.getSubject());
        issue.setDescription(actionForm.getDescription());
        issue.setType(actionForm.getType());
        issue.setPriority(actionForm.getPriority());
        issue.setCreatorIP(request.getRemoteAddr());

        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        // Add issue
        ActionMessages errors = issueService.addIssueSimple(issue);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ISSUE_PLUGIN_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
            
        } else {
            if (ConfigManager.email.isIssueNotificationFromUiEnabled()) {
                issueService.sendSimpleIssueNotification(issue, null);
            }
            
            return ajaxUpdateView(AppPaths.ISSUE_PLUGIN_ADD_3 + "?issueId=" + issue.getId());
        }
    }
}
