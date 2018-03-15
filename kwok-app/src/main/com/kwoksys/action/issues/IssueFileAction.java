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

import java.io.FileNotFoundException;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.files.FileAddTemplate;
import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for managing issue files.
 */
public class IssueFileAction extends Action2 {

    public String add() throws Exception {
        Integer issueId = requestContext.getParameter("issueId");

        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        Issue issue = issueService.getIssue(issueId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: IssueSpecTemplate
        //
        IssueSpecTemplate spec = standardTemplate.addTemplate(new IssueSpecTemplate(issue));
        spec.setHeaderText(issue.getSubject());

        //
        // Template: FileAddTemplate
        //
        FileAddTemplate fileAdd = standardTemplate.addTemplate(new FileAddTemplate(getBaseForm(FileUploadForm.class)));
        fileAdd.setFileName(requestContext.getParameterString("fileName0"));
        fileAdd.setFormAction(AppPaths.ISSUES_FILE_ADD_2 + "?issueId=" + issueId);
        fileAdd.setFormCancelAction(AppPaths.ISSUES_DETAIL + "?issueId=" + issueId);
        fileAdd.getErrorsTemplate().setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String add2() throws Exception {
        FileUploadForm actionForm = saveActionForm(new FileUploadForm());

        // Instantiate Issue class.
        Issue issue = new Issue();
        issue.setId(requestContext.getParameter("issueId"));

        // Add the file
        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        ActionMessages errors = issueService.addIssueFile(issue, actionForm);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ISSUES_FILE_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&issueId=" + issue.getId());

        } else {
            return redirect(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());
        }
    }
    
    public String download() throws Exception {
        ResponseContext responseContext = new ResponseContext(response);

        try {
            // Get request parameters
            Integer issueId = requestContext.getParameter("issueId");

            // Call the service
            IssueService issueService = ServiceProvider.getIssueService(requestContext);

            // Check whether the object exists
            issueService.getIssue(issueId);

            Integer fileId = requestContext.getParameter("fileId");
            File file = issueService.getIssueFile(issueId, fileId);

            FileService fileService = ServiceProvider.getFileService(requestContext);
            fileService.download(responseContext, file);

        } catch (ObjectNotFoundException e) {
            throw new FileNotFoundException();
        }
        return null;
    }
}

