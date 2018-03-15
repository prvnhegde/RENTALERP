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

import java.util.ArrayList;
import java.util.List;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * Template class for Issue spec.
 * detailList
 * columnText
 * columnValue
 * rowClass
 */
public class IssueSpecTemplate extends BaseTemplate {

    private String headerText;
    private Issue issue;
    private String subscribers;
    private boolean hasHtmlContent;
    private String showPlainTextLink;
    private String formattedDescription;

    public IssueSpecTemplate(Issue issue) {
        super(IssueSpecTemplate.class);
        this.issue = issue;
    }

    public void init() {}

    @Override
    public String getJspPath() {
        return "/jsp/issues/IssueSpecTemplate.jsp";
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public void applyTemplate() throws DatabaseException {
        AccessUser user = requestContext.getUser();

        AttributeManager attributeManager = new AttributeManager(requestContext);

        hasHtmlContent = IssueUtils.isHtmlEmail(issue.getDescription());
        formattedDescription = HtmlUtils.formatMultiLineDisplay(issue.getDescription());

        showPlainTextLink = new Link(requestContext).setJavascript("App.issueDisplayText()")
                .setTitleKey("issues.issueDetails.showPlainText").getString();

        boolean hasPermission = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);
        String assigneeName = IssueUtils.getAssigneeIconLink(requestContext, hasPermission, issue.getAssignee());

        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        List<AccessUser> subscribeUsers = issueService.getSelectedSubscribers(issue.getId());
        List<String> list = new ArrayList<>();
        for (AccessUser subscribeUser : subscribeUsers) {
            list.add(AdminUtils.getSystemUsername(requestContext, subscribeUser));
        }
        subscribers = StringUtils.join(list, ", ");

        request.setAttribute("TemplateIssueSpec_issue", issue);
        request.setAttribute("TemplateIssueSpec_issueAssignee", assigneeName);
        request.setAttribute("TemplateIssueSpec_issueCreatorInfo", WidgetUtils.formatCreatorInfo(requestContext, issue.getCreationDate(), issue.getCreator()));
        request.setAttribute("TemplateIssueSpec_issueModifierInfo", WidgetUtils.formatCreatorInfo(requestContext, issue.getModificationDate(), issue.getModifier()));
        request.setAttribute("TemplateIssueSpec_issueTypeName", attributeManager.getAttrFieldNameCache(Attributes.ISSUE_TYPE, issue.getType()));
        request.setAttribute("TemplateIssueSpec_issueStatusName", attributeManager.getAttrFieldNameCache(Attributes.ISSUE_STATUS, issue.getStatus()));
        request.setAttribute("TemplateIssueSpec_issuePriorityName", attributeManager.getAttrFieldNameCache(Attributes.ISSUE_PRIORITY, issue.getPriority()));
        request.setAttribute("TemplateIssueSpec_issueResolutionName", attributeManager.getAttrFieldNameCache(Attributes.ISSUE_RESOLUTION, issue.getResolution()));
        request.setAttribute("TemplateIssueSpec_issueSubscribers", subscribers);
    }

    public Issue getIssue() {
        return issue;
    }

    public String getHeaderText() {
        return headerText;
    }

    public boolean isHasHtmlContent() {
        return hasHtmlContent;
    }

    public String getShowPlainTextLink() {
        return showPlainTextLink;
    }

    public String getFormattedDescription() {
        return formattedDescription;
    }
}