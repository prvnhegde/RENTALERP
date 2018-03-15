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

<k:define id="company" name="company" type="com.kwoksys.biz.contacts.dto.Company"/>
<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="companyAddForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<table class="standardForm">
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.company_name"/>:</th>
        <td><input type="text" name="companyName" value="<k:write value="${form.companyName}"/>" size="40" autofocus></td>
    </tr>
    <tr>
        <th><k:message key="common.column.company_description"/>:</th>
        <td><html:textarea name="form" property="companyDescription" rows="10" cols="50"/></td>
    </tr>
    <tr>
        <th>${company.getAttrRequiredText('company_types')}<k:message key="common.column.company_types"/>:</th>
        <td>
            <k:foreach id="type" name="companyTypes">
                <html:multibox name="form" property="companyTypes" value="${type.value}"/>${type.label}<br/>
            </k:foreach>
        </td>
    </tr>
    <tr>
        <th><k:message key="common.column.company_tags"/>:</th>
        <td><html:text name="form" property="companyTags" size="60"/>
            <br><span class="formFieldDesc"><k:message key="contactMgmt.colDesc.company_tags"/></span>
        </td>
    </tr>
    <tr>
        <td colspan="2"><jsp:include page="/jsp/common/template/CustomFieldsEdit.jsp"/></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <button type="submit" name="submitBtn"><k:message key="form.button.add"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
