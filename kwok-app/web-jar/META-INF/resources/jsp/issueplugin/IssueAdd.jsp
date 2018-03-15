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

<form action="${formAction}" method="post" id="issueAddForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<table class="standardForm">
    <k:notEmpty name="createdBy">
        <tr>
            <th><k:message key="common.column.creator_name"/>:</th>
            <td><k:write value="${createdBy}"/></td>
        </tr>
    </k:notEmpty>
    <tr>
        <th><k:message key="common.column.issue_type"/>:</th>
        <td><html:select name="form" property="type">
            <html:options collection="typeOptions" property="value" labelProperty="label"/>
            </html:select>
            ${legendLink}
        </td>
    </tr>
    <tr>
        <th><k:message key="common.column.issue_priority"/>:</th>
        <td><html:select name="form" property="priority">
            <html:options collection="priorityOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>    
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.issue_name"/>:</th>
        <td><html:text name="form" property="subject" size="40"/>
            <div class="formFieldDesc"><k:message key="common.form.fieldMaxLen" arg0="${issueNameCharLimit}"/></div>
        </td>
    </tr>
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.issue_description"/>:</th>
        <td><html:textarea name="form" property="description" rows="16" cols="60"/>
            <div class="formFieldDesc"><k:message key="common.form.fieldMaxLen" arg0="${issueDescriptionCharLimit}"/></div>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <button type="submit" name="submitBtn"><k:message key="form.button.submit"/></button>        
        </td>
    </tr>
</table>
</form>
