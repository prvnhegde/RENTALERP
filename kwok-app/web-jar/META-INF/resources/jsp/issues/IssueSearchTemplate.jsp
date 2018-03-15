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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<k:define id="issueSearchTemplate" name="IssueSearchTemplate" type="com.kwoksys.action.issues.IssueSearchTemplate"/>

<%-- Issue search --%>
<form action="${issueSearchTemplate.formAction}" method="post" name="IssueSearchForm" id="IssueSearchForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<html:hidden name="IssueSearchForm" property="cmd" value="search"/>
<input type="hidden" name="_resubmit" value="true">
<html:hidden name="IssueSearchForm" property="reportType" value="${reportType}"/>
<table class="standard">
    <tr><th><k:message key="common.column.issue_id"/>:</th><td> <html:text name="IssueSearchForm" property="issueId" size="40"/></td></tr>
    <tr><th><k:message key="common.column.issue_name"/>:</th><td> <html:text name="IssueSearchForm" property="subject" size="40"/></td></tr>
    <tr><th><k:message key="common.column.issue_description"/>:</th><td> <html:text name="IssueSearchForm" property="description" size="40"/></td></tr>
    <tr><th><k:message key="issueMgmt.colName.issue_url"/>:</th><td> <html:text name="IssueSearchForm" property="url" size="40"/></td></tr>
    <tr><th><k:message key="common.column.assignee_name"/>:</th>
    <td><html:select name="IssueSearchForm" property="assignedTo">
        <html:options collection="IssueSearchTemplate_assigneeOptions" property="value" labelProperty="label"/>
    </html:select></td></tr>
    <tr><th><k:message key="issueMgmt.index.submitter"/>:</th>
    <td><html:select name="IssueSearchForm" property="submitter">
        <html:options collection="IssueSearchTemplate_assigneeOptions" property="value" labelProperty="label"/>
    </html:select></td></tr>
    <tr><th><k:message key="issueMgmt.index.submissionDate"/>:</th>
    <td><html:select name="IssueSearchForm" property="submissionDate">
        <html:options collection="IssueSearchTemplate_submittedWithinOptions" property="value" labelProperty="label"/>
    </html:select></td></tr>
    <tr><th><k:message key="issueMgmt.index.submittedBetween"/>:</th>
        <td>
        <html:select name="IssueSearchForm" property="submittedAfterMonth">
        <html:options collection="monthOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="IssueSearchForm" property="submittedAfterDate">
        <html:options collection="dateOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="IssueSearchForm" property="submittedAfterYear">
        <html:options collection="yearOptions" property="value" labelProperty="label"/>
        </html:select>
        -
        <html:select name="IssueSearchForm" property="submittedBeforeMonth">
        <html:options collection="monthOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="IssueSearchForm" property="submittedBeforeDate">
        <html:options collection="dateOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="IssueSearchForm" property="submittedBeforeYear">
        <html:options collection="yearOptions" property="value" labelProperty="label"/>
        </html:select>
        </td>
    </tr>
    <tr><th><k:message key="issueMgmt.index.dueDate"/>:</th>
        <td><html:select name="IssueSearchForm" property="dueWithin">
        <html:options collection="IssueSearchTemplate_dueWithinOptions" property="value" labelProperty="label"/>
        </html:select></td>
    </tr>
    <tr><td colspan="2">
        <table class="standard">
        <tr>
        <th><k:message key="common.column.issue_type"/>:
            <input type="button" onclick="Js.Form.selectAllOptions(this.form.type);" value="<k:message key="form.button.selectAll"/>">
            <br><html:select name="IssueSearchForm" property="type" multiple="true" size="5">
                <html:options collection="typeOptions" property="value" labelProperty="label"/>
            </html:select>

        </th>
        <th><k:message key="common.column.issue_status"/>:
            <input type="button" onclick="Js.Form.selectAllOptions(this.form.status);" value="<k:message key="form.button.selectAll"/>">
            <br><html:select name="IssueSearchForm" property="status" multiple="true" size="5">
                <html:options collection="statusOptions" property="value" labelProperty="label"/>
            </html:select>
        </th>
        <th><k:message key="common.column.issue_priority"/>:
            <input type="button" onclick="Js.Form.selectAllOptions(this.form.priority);" value="<k:message key="form.button.selectAll"/>">
            <br><html:select name="IssueSearchForm" property="priority" multiple="true" size="5">
                <html:options collection="priorityOptions" property="value" labelProperty="label"/>
            </html:select>
        </th>
        <td>&nbsp;</td>
        </tr>
        </table>
    </td></tr>
    <k:notEmpty name="customFieldsOptions">
        <tr><td colspan="3">
            <k:message key="common.template.customFields"/>:
            <html:select name="IssueSearchForm" property="attrId">
                <html:options collection="customFieldsOptions" property="value" labelProperty="label"/>
            </html:select> <html:text name="IssueSearchForm" property="attrValue" size="40"/>
        </td></tr>
    </k:notEmpty>
    <tr><td colspan=6>
        <k:notEqual name="issueSearchTemplate" property="hideSearchButton" value="true">
            <button type="submit" name="submitBtn"><k:message key="form.button.search"/></button>
            &nbsp;&nbsp;${issueSearchTemplate.clearSearchLink}
        </k:notEqual>
    </td></tr>
</table>
</form>
