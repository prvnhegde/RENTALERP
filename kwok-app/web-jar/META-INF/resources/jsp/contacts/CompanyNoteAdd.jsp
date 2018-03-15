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

<jsp:include page="/jsp/contacts/CompanySpecTemplate.jsp"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<k:define id="companyNote" name="form" property="note"/>

<form action="${formAction}" method="post" id="companyNoteAddForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<html:hidden name="form" property="companyId"/>
<table class="listTable">
    <tr class="themeHeader">
        <td colspan="2"><b><k:message key="contactMgmt.companyNoteAdd_sectionHeader"/></b></td>
    </tr>
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.company_note_name"/>:</th>
        <td><input type="text" name="noteName" value="<k:write value="${form.noteName}"/>" size="40" autofocus></td>
    </tr>
    <tr>
        <th><k:message key="common.column.company_note_description"/>:</th>
        <td><html:textarea name="form" property="noteDescription" cols="60" rows="5"/></td>
    </tr>
    <tr>
        <th>${companyNote.getAttrRequiredText('company_note_type')}<k:message key="common.column.company_note_type"/>:</th>
        <td><html:select name="form" property="noteType">
            <html:options collection="noteTypeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
