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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.issues.IssueAssociateTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dto.linking.SoftwareIssueLink;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding Software Issue.
 */
public class SoftwareIssueAddAction extends Action2 {

    public String add() throws Exception {
        SoftwareIssueForm actionForm = getBaseForm(SoftwareIssueForm.class);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(actionForm.getSoftwareId());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

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
        // Template: IssueAssociateTemplate
        //
        IssueAssociateTemplate issueAdd = standardTemplate.addTemplate(new IssueAssociateTemplate());
        issueAdd.setIssueId(actionForm.getIssueId());
        issueAdd.setLinkedObjectTypeId(ObjectTypes.SOFTWARE);
        issueAdd.setLinkedObjectId(actionForm.getSoftwareId());
        issueAdd.setFormSearchAction(AppPaths.SOFTWARE_ISSUE_ADD + "?softwareId=" + actionForm.getSoftwareId());
        issueAdd.setFormSaveAction(AppPaths.SOFTWARE_ISSUE_ADD_2 + "?softwareId=" + actionForm.getSoftwareId());
        issueAdd.setFormCancelAction(AppPaths.SOFTWARE_ISSUE + "?softwareId=" + actionForm.getSoftwareId());

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String add2() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");
        Integer issueId = requestContext.getParameter("issueId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        softwareService.getSoftware(softwareId);

        SoftwareIssueLink issueMap = new SoftwareIssueLink();
        issueMap.setSoftwareId(softwareId);
        issueMap.setIssueId(issueId);

        ActionMessages errors = softwareService.addSoftwareIssue(issueMap);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.SOFTWARE_ISSUE_ADD + "?softwareId=" + softwareId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.SOFTWARE_ISSUE + "?softwareId=" + softwareId);
        }
    }
}
