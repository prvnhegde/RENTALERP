<%--
 * Copyright 2015 Kwoksys
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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<k:define id="issue" name="issue" type="com.kwoksys.biz.issues.dto.Issue"/>
<k:define id="issueSpecTemplate" name="IssueSpecTemplate" type="com.kwoksys.action.issues.IssueSpecTemplate"/>

<h2><k:write value="${issueSpecTemplate.headerText}"/></h2>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="issueEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="_resubmit" value="true">
<html:hidden name="form" property="issueId"/>
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th><k:message key="common.column.issue_id"/>:</th>
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
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.issue_name"/>:</th>
        <td><html:text name="form" property="subject" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="issueMgmt.colName.issue_history_description"/>:</th>
        <td><html:textarea name="form" property="followup" rows="10" cols="50"/></td>
    </tr>
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.issue_type"/>:</th>
        <td><html:select name="form" property="type" onchange="changeAction(this, '${formThisAction}');">
            <html:options collection="typeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.issue_status"/>:</th>
        <td><html:select name="form" property="status">
            <html:options collection="statusOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.issue_priority"/>:</th>
        <td><html:select name="form" property="priority">
            <html:options collection="priorityOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.column.issue_resolution"/>:</th>
        <td><html:select name="form" property="resolution">
            <html:options collection="resolutionOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th>${issue.getAttrRequiredText('issue_assignee')}<k:message key="issueMgmt.colName.assignee_name"/>:</th>
        <td><html:select name="form" property="assignedTo">
            <html:options collection="assignedToOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <th><k:message key="common.column.issue_due_date"/>:</th>
        <td>
            <html:radio name="form" property="hasDueDate" value="0" onclick="App.toggleIssueDueDate(this)"/>
            <k:message key="issueMgmt.issueEdit.issueHasNoDueDate"/>
            <br>
            <html:radio name="form" property="hasDueDate" value="1" onclick="App.toggleIssueDueDate(this)"/>
            <k:message key="issueMgmt.issueEdit.issueHasDueDate"/>&nbsp;
            <html:select name="form" property="dueDateMonth" disabled="${formDisableIssueDueDate}">
            <html:options collection="dueMonthOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="dueDateDate" disabled="${formDisableIssueDueDate}">
            <html:options collection="dueDateOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="dueDateYear" disabled="${formDisableIssueDueDate}">
            <html:options collection="dueYearOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <th><k:message key="issueMgmt.colName.issue_subscriber"/>:</th>
        <td>
            <table class="nested"><tr>
                <td>
                <span class="formSubscriberHeader"><k:message key="issueMgmt.issueAdd.availableSubscribers"/></span><Br>
                <html:select name="form" property="availableSubscribers" multiple="true" size="5" styleClass="formSubscriberSelectbox">
                <html:options collection="availableSubscribersOptions" property="value" labelProperty="label"/>
                </html:select>
                </td>
                <td valign="middle"><p>
                <button type="button" onclick="Js.Form.moveOptions(this.form.availableSubscribers, this.form.selectedSubscribers)" style="padding:6px 20px">&gt;</button><p>
                <button type="button" onclick="Js.Form.moveOptions(this.form.selectedSubscribers, this.form.availableSubscribers)" style="padding:6px 20px">&lt;</button>
                </td>
                <td>
                <span class="formSubscriberHeader"><k:message key="issueMgmt.issueAdd.selectedSubscribers"/></span><Br>
                <html:select name="form" property="selectedSubscribers" multiple="true" size="5" styleClass="formSubscriberSelectbox">
                <html:options collection="selectedSubscribersOptions" property="value" labelProperty="label"/>
                </html:select>
                </td>
            </tr></table>
        </td>
    </tr>
    <tr>
        <th><k:message key="issueMgmt.issueEdit.emailNotification"/>:</th>
        <td>
            <k:equal name="emailNotification" value="true">
                <k:message key="issueMgmt.issueEdit.emailNotificationOn"/>
                <br><html:checkbox name="form" property="suppressNotification" value="1"/><k:message key="issueMgmt.issueEdit.suppressNotification"/>
            </k:equal>
            <k:equal name="emailNotification" value="false">
                <k:message key="issueMgmt.issueEdit.emailNotificationOff"/>
            </k:equal>
        </td>
    </tr>
    <tr>
        <th><k:message key="common.column.creator"/>:</th>
        <td>${TemplateIssueSpec_issueCreatorInfo}</td>
    </tr>
    <tr>
        <th><k:message key="common.column.modifier"/>:</th>
        <td>${TemplateIssueSpec_issueModifierInfo}</td>
    </tr>
    <tr>
        <td colspan="2">
        <table class="nested" style="width:100%"><tr><td>
        <jsp:include page="/jsp/common/template/CustomFieldsEdit.jsp"/>
        </td></tr></table>
        </td>
    </tr>
    <tr>
        <td style="width:20%">&nbsp;</td>
        <td><button type="submit" name="submitBtn" onclick="Js.Form.selectAllOptions(this.form.selectedSubscribers)"><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
