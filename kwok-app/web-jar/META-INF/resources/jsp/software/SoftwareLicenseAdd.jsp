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

<jsp:include page="/jsp/software/SoftwareSpecTemplate.jsp"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="licenseAddForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="hasCustomFields" value="true">
<input type="hidden" name="softwareId" value="${form.softwareId}">
<div class="tabBody">
    <div class="themeHeader">
        <b><k:message key="software.license.addLicenseHeader"/></b>
    </div>
    <table class="standardForm">
            <tr>
                <th nowrap="nowrap"><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.license_key"/>:</th>
                <td><input type="text" name="licenseKey" value="<k:write value="${form.licenseKey}"/>" size="40" autofocus></td>
            </tr>
            <tr>
                <th><k:message key="common.column.license_note"/>:</th>
                <td><html:textarea name="form" property="licenseNote" rows="2" cols="40"/>&nbsp;</td>
            </tr>
            <tr>
                <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.license_entitlement"/>:</th>
                <td><html:text name="form" property="licenseEntitlement" size="10"/>&nbsp;</td>
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
</div>
</form>
