<%--
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
--%>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<k:define id="issueSpecTemplate" name="IssueSpecTemplate" type="com.kwoksys.action.issues.IssueSpecTemplate"/>
<k:define id="issue" name="issueSpecTemplate" property="issue" type="com.kwoksys.biz.issues.dto.Issue"/>

<h2><k:write value="${issueSpecTemplate.headerText}"/></h2>

<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th style="width:20%"><k:message key="common.column.issue_id"/>:</th>
        <td><k:write value="${issue.id}"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.issue_description"/>:</th>
        <td><div id="issueText">
                <k:equal name="issueSpecTemplate" property="hasHtmlContent" value="true">
                    <a href="javascript:App.issueDisplayHtml();"><span class="tooltip tooltipstered" title="<k:message key="issues.issueDetails.showHtml.info"/>">[<k:message key="issues.issueDetails.showHtml"/>]</span></a>
                </k:equal>
                <div id="issueTextContent">
                    ${issueSpecTemplate.formattedDescription}
                </div>
            </div>
            <k:equal name="issueSpecTemplate" property="hasHtmlContent" value="true">
                <div id="issueHtml" style="display:none">
                    [${issueSpecTemplate.showPlainTextLink}]
                    <div id="issueHtmlContent"></div>
                </div>
            </k:equal>
        </td>
    </tr>
    <tr>
        <th><k:message key="issueMgmt.colName.issue_url"/>:</th>
        <td><k:write value="${TemplateIssueSpec_issue.url}"/></td>
    </tr>

    <tr>
        <th><k:message key="common.column.issue_type"/>:</th>
        <td><k:write value="${TemplateIssueSpec_issueTypeName}"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.issue_status"/>:</th>
        <td><k:write value="${TemplateIssueSpec_issueStatusName}"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.issue_priority"/>:</th>
        <td><k:write value="${TemplateIssueSpec_issuePriorityName}"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.issue_resolution"/>:</th>
        <td><k:write value="${TemplateIssueSpec_issueResolutionName}"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.assignee_name"/>:</th>
        <td>${TemplateIssueSpec_issueAssignee}</td>
    </tr>
    <tr>
        <th><k:message key="common.column.issue_due_date"/>:</th>
        <td><k:write value="${TemplateIssueSpec_issue.dueDateShort}"/></td>
    </tr>
    <k:notEmpty name="TemplateIssueSpec_issue" property="fromEmail">
        <tr>
            <th><k:message key="issueMgmt.colName.issue_created_from_email"/>:</th>
            <td><k:write value="${TemplateIssueSpec_issue.fromEmail}"/></td>
        </tr>
    </k:notEmpty>
    <tr>
        <th><k:message key="common.column.creator"/>:</th>
        <td>${TemplateIssueSpec_issueCreatorInfo}</td>
    </tr>
    <tr>
        <th><k:message key="issueMgmt.colName.creator_ip"/>:</th>
        <td><k:write value="${TemplateIssueSpec_issue.creatorIP}"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.modifier"/>:</th>
        <td>${TemplateIssueSpec_issueModifierInfo}</td>
    </tr>
    <tr>
        <th><k:message key="issueMgmt.colName.issue_subscriber"/>:</th>
        <td><k:write value="${TemplateIssueSpec_issueSubscribers}"/></td>
    </tr>
</table>
<p>