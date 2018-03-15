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

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="configCompanyEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="cmd" value="${cmd}"/>
<table class="standardForm">
    <tr>
        <th width="30%"><k:message key="admin.config.companyName"/>:</th>
        <td><html:text name="form" property="companyName" size="60"/>
            <br><span class="formFieldDesc"><k:message key="admin.configDesc.companyName"/></span></td>
    </tr>
    <tr>
        <th width="30%"><k:message key="admin.config.companyPath"/>:</th>
        <td><html:text name="form" property="companyPath" size="60"/>
            <br><span class="formFieldDesc"><k:message key="admin.configDesc.companyPath"/></span></td>
    </tr>
    <tr>
        <th width="30%"><k:message key="admin.config.companyLogoPath"/>:</th>
        <td><html:text name="form" property="companyLogoPath" size="60"/>
            <br><span class="formFieldDesc"><k:message key="admin.config.companyLogoPath.desc"/></span></td>
    </tr>
    <tr>
        <th width="30%"><k:message key="admin.config.companyFooterNotes"/>:</th>
        <td><html:textarea name="form" property="companyFooterNotes" rows="6" cols="50"/></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td><button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
