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

<jsp:include page="/jsp/hardware/HardwareSpecTemplate.jsp"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<%-- Show input fields for adding a new hardware component. --%>
<form action="${formAction}" method="post" id="hardwareCompAddForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="hardwareId" value="${hardwareId}">
<div class="tabBody">
    <div class="themeHeader">
        <b><k:message key="itMgmt.cmd.hardwareComponentAdd"/></b>
    </div>
        <table class="standardForm">
            <tr>
                <th nowrap="nowrap"><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.hardware_component_type"/>:</th>
                <td><html:select name="form" property="hardwareComponentType">
                    <html:options collection="compOptions" property="value" labelProperty="label"/>
                    </html:select></td>
            </tr>
            <tr>
                <th><k:message key="common.column.hardware_component_description"/>:</th>
                <td><html:text name="form" property="compDescription" size="60"/>
            </tr>
            <tr>
                <td colspan="2"><jsp:include page="/jsp/common/template/CustomFieldsEdit.jsp"/></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
                    ${formCancelLink}
                </td>
            </tr>
        </table>
</div>
</form>
