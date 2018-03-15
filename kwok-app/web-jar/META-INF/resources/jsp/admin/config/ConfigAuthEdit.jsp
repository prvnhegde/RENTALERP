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

<form action="${formAction}" method="post" id="configAuthEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="cmd" value="${cmd}"/>
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th><k:message key="admin.config.auth.type"/>:</th>
        <td><html:select name="form" property="authType">
            <html:options collection="authTypeOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.auth.authenticationMethod"/>:</th>
        <td><html:select name="form" property="authMethod">
            <html:options collection="authMethodOptions" property="value" labelProperty="label"/>
            </html:select>
            <br><span class="formFieldDesc"><k:message key="admin.config.auth.authenticationMethodDesc"/></span>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.auth.ldapUrl"/>:</th>
        <td><html:select name="form" property="ldapUrlScheme">
                <html:options collection="ldapUrlSchemeOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:text name="form" property="ldapUrl" size="60"/>
            <br><span class="formFieldDesc"><k:message key="admin.configDesc.auth.ldapUrl"/></span>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.auth.ldapSecurityPrincipal"/>:</th>
            <td><html:text name="form" property="ldapSecurityPrincipal" size="60"/>
            <br><span class="formFieldDesc"><k:message key="admin.config.auth.ldapSecurityPrincipal.desc"/></span>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.auth.domain"/>:</th>
        <td><html:text name="form" property="domain" size="60"/>
            <br><span class="formFieldDesc"><k:message key="admin.configDesc.auth.domain"/></span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.auth.sessionTimeoutSeconds"/>:</th>
        <td><html:select name="form" property="authTimeout">
            <html:options collection="sessionTimeoutOptions" property="value" labelProperty="label"/>
            </html:select>&nbsp;<span class="formFieldDesc"><k:message key="common.calendar.time.hours"/></span>
        </td>
    </tr>
    <tr><td colspan="2">
        <h3><k:message key="admin.config.security.passwordPolicy"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.security.allowBlankUserPassword"/>:</th>
        <td><html:select name="form" property="allowBlankUserPassword">
            <html:options collection="allowBlankUserPasswordOptions" property="value" labelProperty="label"/>
            </html:select>
            <br><span class="formFieldDesc"><k:message key="admin.config.security.allowBlankUserPassword.desc"/></span>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.security.minimumPasswordLength"/>:</th>
        <td><html:select name="form" property="minimumPasswordLength">
            <html:options collection="passwordLenOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.security.passwordComplexity"/>:</th>
        <td><html:select name="form" property="passwordComplexityEnabled">
                <html:options collection="passwordComplexityOptions" labelProperty="label" property="value"/>
            </html:select>
            <br><span class="formFieldDesc"><k:message key="admin.config.security.passwordComplexity.desc"/></span>
        </td>
    </tr>    
    <tr><td colspan="2">
        <h3><k:message key="admin.config.security.accountLockoutPolicy"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.security.accountLockoutThreshold"/>:</th>
        <td><html:text name="form" property="accountLockoutThreshold" size="2" maxlength="2"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.security.accountLockoutDuration"/>:</th>
        <td><html:text name="form" property="accountLockoutDurationMinutes" size="2" maxlength="2"/></td>
    </tr>
    <tr>
        <td style="width:30%">&nbsp;</td>
        <td><button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>        
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
