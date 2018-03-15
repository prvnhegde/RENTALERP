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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.issues.IssueForm;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for reporting a new issue.
 */
public class IssueAddAction extends Action2 {

    public String execute() throws Exception {
        AccessUser accessUser = requestContext.getUser();
        AttributeManager attributeManager = new AttributeManager(requestContext);

        IssueForm actionForm = getBaseForm(IssueForm.class);
        Issue issue = new Issue();

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setSubject("");
            actionForm.setDescription("");
            actionForm.setType(issue.getType());
            actionForm.setPriority(issue.getPriority());
        }

        QueryCriteria query = new QueryCriteria();
        query.addSortColumn(AttributeField.NAME);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ISSUE_PLUGIN_ADD_2);

        request.setAttribute("legendLink", new Link(requestContext)
                .setImgSrc(Image.getInstance().getSignHelp())
                .setJavascript("window.open('" + AppPaths.ROOT + AppPaths.ISSUE_PLUGIN_LEGEND_DETAIL + "?attributeId="
                        + Attributes.ISSUE_TYPE + "', 'legend', 'width=480, height=420, left=50, top=50, scrollbars=yes, resizable=yes')"));

        request.setAttribute("issueNameCharLimit", Schema.getColumnLength(Schema.ISSUE_NAME));
        request.setAttribute("issueDescriptionCharLimit", Schema.getColumnLength(Schema.ISSUE_DESCRIPTION));
        request.setAttribute("typeOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_TYPE));
        request.setAttribute("priorityOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_PRIORITY));

        if (accessUser.isLoggedOn()) {
            request.setAttribute("createdBy", AdminUtils.getSystemUsername(requestContext, accessUser));
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("issuePlugin.issueAdd.title");
        header.setTitleKey("issuePlugin.issueAdd.title");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("issuePlugin.issueAdd.confirm");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
