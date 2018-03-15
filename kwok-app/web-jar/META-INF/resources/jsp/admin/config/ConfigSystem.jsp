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
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<h3><k:message key="admin.config.clientBrowser"/></h3>

<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th width="30%"><k:message key="admin.config.clientBrowser.userAgent"/>:</th>
        <td width="70%">${browserUserAgent}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.clientBrowser.windowSize"/>:</th>
        <td><div id="browserSize"></div></td>
    </tr>
    <%--<tr>
        <th>Cookies:</th>
        <td><span id="cookies"></span>
            <script type="text/javascript">document.getElementById('cookies').innerHTML = document.cookie;</script>
        </td>
    </tr>--%>
    </tr>
</table>

<h3><k:message key="admin.config.os"/></h3>
<table class="${StandardTemplate.detailsTableStyle}">        
    <tr>
        <th width="30%"><k:message key="admin.config.os.name"/>:</th>
        <td width="70%"><k:write value="${os}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.os.arch"/>:</th>
        <td><k:write value="${osArch}"/></td>
    </tr>
</table>

<h3><k:message key="admin.config.jvm"/></h3>    
<table class="${StandardTemplate.detailsTableStyle}">    
    <tr>
        <th width="30%"><k:message key="admin.config.jvm.vendor"/>:</th>
        <td width="70%"><k:write value="${jvmVendor}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.jvm.version"/>:</th>
        <td><k:write value="${jvmVersion}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.jvm.home"/>:</th>
        <td><k:write value="${jvmHome}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.system.user.home"/>:</th>
        <td><k:write value="${userHome}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.jvm.freeMemory"/>:</th>
        <td><k:write value="${jvmFreeMemory}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.jvm.totalMemory"/>:</th>
        <td><k:write value="${jvmTotalMemory}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.jvm.maxMemory"/>:</th>
        <td><k:write value="${jvmMaxMemory}"/></td>
    </tr>
</table>
    
<h3><k:message key="admin.config.db.server"/></h3>
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th width="30%"><k:message key="admin.config.db.serverName"/>:</th>
        <td width="70%"><k:write value="${dbProductName}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.db.serverVersion"/>:</th>
        <td><k:write value="${dbProductVersion}"/></td>
    </tr>
    <k:notEmpty name="databases">
    <tr>
        <th><k:message key="admin.config.db.databases"/>:</th>
        <td><k:write value="${databases}"/></td>
    </tr>
    </k:notEmpty>
</table>
    
<h3><k:message key="admin.configHeader.db_config"/> <span class="command"> [${backupLink}]</span></h3>
<table class="${StandardTemplate.detailsTableStyle}">    
    <tr>
        <th width="30%"><k:message key="admin.config.db.host"/>:</th>
        <td width="70%"><k:write value="${dbHost}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.db.port"/>:</th>
        <td><k:write value="${dbPort}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.db.schemaName"/>:</th>
        <td><k:write value="${dbName}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.db.maxPoolSize"/>:</th>
        <td><k:write value="${dbMaxPoolSize}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.db.poolSizeCurrent"/>:</th>
        <td><k:write value="${dbPoolSizeCurrent}"/></td>
    </tr>
</table>

<h3><k:message key="admin.configHeader.dbSequences"/>
    <span class="command"> [${loadDbSequencesLink}]</span></h3>
<table class="${StandardTemplate.detailsTableStyle}">    
    <tr>
        <td colspan="2"><div id="dbSequences" style="display:none"></div></td>
    </tr>
</table>    

<h3><k:message key="admin.config.logging"/>
<k:notEmpty name="loggingLink">
    <span class="command"> [${loggingLink}]</span>
</k:notEmpty></h3>
    
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th width="30%"><k:message key="admin.config.logging.database"/>:</th>
        <td width="70%">${databaseAccessLogLevel}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.logging.ldap"/>:</th>
        <td>${ldapLogLevel}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.logging.scheduler"/>:</th>
        <td>${schedulerLogLevel}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.logging.template"/>:</th>
        <td>${templateLogLevel}</td>
    </tr>
</table>

<script type="text/javascript">
document.body.onresize = App.browserSizeRefresh;
App.browserSizeRefresh();
</script>
