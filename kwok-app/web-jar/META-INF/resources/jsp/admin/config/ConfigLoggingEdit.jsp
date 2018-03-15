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

<form action="${formAction}" method="post" id="logEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<table class="${StandardTemplate.detailsTableStyle}">
    <input type="hidden" name="cmd" value="${cmd}"/>
    <tr>
        <th><k:message key="admin.config.logging.database"/>:</th>
        <td><html:select name="form" property="databaseAccessLogLevel">
            <html:options collection="logLevelOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.logging.ldap"/>:</th>
        <td><html:select name="form" property="ldapLogLevel">
            <html:options collection="logLevelOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.logging.scheduler"/>:</th>
        <td><html:select name="form" property="schedulerLogLevel">
            <html:options collection="logLevelOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.logging.template"/>:</th>
        <td><html:select name="form" property="templateLogLevel">
            <html:options collection="logLevelOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <td style="width:30%">&nbsp;</td>
        <td>
            <button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
