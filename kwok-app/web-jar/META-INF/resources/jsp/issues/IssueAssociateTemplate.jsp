<%--
 * Copyright 2017 Kwoksys
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

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<%-- Show Issue search form. --%>
<form action="${formSearchAction}" method="post" id="issueAssociateForm" onsubmit="return App.submitFormUpdate(this, {'disable': this.searchBtn})">
<k:notEmpty name="hardwareId"><html:hidden name="form" property="hardwareId"/></k:notEmpty>
<k:notEmpty name="softwareId"><html:hidden name="form" property="softwareId"/></k:notEmpty>
<k:notEmpty name="companyId"><html:hidden name="form" property="companyId"/></k:notEmpty>

<table class="listTable">
    <tr class="themeHeader">
        <td colspan="2"><b><k:message key="common.linking.linkIssue"/></b></td>
    </tr>
    <tr>
        <td><k:message key="issueMgmt.index.searchHeader"/>:</td>
        <td><k:message key="common.column.issue_id"/>&nbsp;<input type="text" name="issueId" value="<k:write value="${form.issueId}"/>" size="40" autofocus>
            <button type="submit" name="searchBtn"><k:message key="form.button.search"/></button>
            <k:notEmpty name="issueAddLink">
                &nbsp;<k:message key="common.condition.or"/>&nbsp;${issueAddLink}
            </k:notEmpty>
        </td>
    </tr>
    <tr>
        <td><k:message key="system.issueSelect"/>:</td>
        <td>
            <k:notPresent name="issueList">
                <k:message key="${selectIssueMessage}"/>
            </k:notPresent>
            <k:isPresent name="issueList">
                <table class="noBorder">
                    <k:foreach id="issue" name="issueList">
                        <tr><td><html:checkbox name="form" property="issueId" value="${issue.issueId}"/>
                            ${issue.issueTitle}</td></tr>
                    </k:foreach>
                </table>
            </k:isPresent>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <button type="button" onclick="App.submitFormUpdate(this.form, {'url': '${formSaveAction}', 'disable': this})" ${disableSaveButton}><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
