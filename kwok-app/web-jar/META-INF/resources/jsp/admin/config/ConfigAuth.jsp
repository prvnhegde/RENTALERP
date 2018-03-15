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
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th style="width:30%"><k:message key="admin.config.auth.type"/>:</th>
        <td><k:write value="${authType}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.auth.authenticationMethod"/>:</th>
        <td><k:write value="${authenticationMethod}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.auth.ldapUrl"/>:</th>
        <td><k:write value="${authLdapUrl}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.auth.ldapSecurityPrincipal"/>:</th>
        <td><k:write value="${authLdapSecurityPrincipal}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.auth.domain"/>:</th>
        <td><k:write value="${authDomain}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.auth.sessionTimeoutSeconds"/>:</th>
        <td><k:write value="${authSessionTimeout}"/></td>
    </tr>
    <tr>
        <td colspan="2"><h3><k:message key="admin.config.security.passwordPolicy"/></h3></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.security.allowBlankUserPassword"/>:</th>
        <td><k:write value="${allowBlankUserPassword}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.security.minimumPasswordLength"/>:</th>
        <td><k:message key="admin.config.security.minimumPasswordLength.value" arg0="${minimumPasswordLength}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.security.passwordComplexity"/>:</th>
        <td><k:write value="${passwordComplexity}"/></td>
    </tr>
    <tr>
        <td colspan="2"><h3><k:message key="admin.config.security.accountLockoutPolicy"/></h3></td></tr>
    <tr>
        <th><k:message key="admin.config.security.accountLockoutThreshold"/>:</th>
        <td><k:write value="${accountLockoutThreshold}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.security.accountLockoutDuration"/>:</th>
        <td><k:message key="admin.config.security.accountLockoutDuration.value" arg0="${accountLockoutDuration}"/>
            <br><span class="formFieldDesc"><k:write value="${accountLockoutDescription}"/></span></td>
    </tr>
</table>
