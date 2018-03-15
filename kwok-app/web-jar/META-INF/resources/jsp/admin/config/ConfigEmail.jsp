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

<h3><k:message key="admin.config.email.outgoingServer.header"/>
    <k:equal name="canEditSmtpSettings" value="true">
        <span class="command"> [${editSmtpSettingsLink}]</span>
    </k:equal>
</h3>
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th style="width:30%"><k:message key="admin.config.email.notification.issues.fromUi"/>:</th>
        <td><k:write value="${issueNotificationFromUi}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.notification.issues.fromEmail"/>:</th>
        <td><k:write value="${issueNotificationFromEmail}"/>
            <br>
            <span class="formFieldDesc"><k:message key="admin.config.email.notification.issues.fromUi.description"/></span>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.notification.contractExpiration"/>:</th>
        <td><k:write value="${contractExpireNotification}"/>
            <br>
            <span class="formFieldDesc">${contractExpireNotificationDesc}</span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.smtp.host"/>:</th>
        <td><k:write value="${smtpHost}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.port"/>:</th>
        <td><k:write value="${smtpPort}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.username"/>:</th>
        <td><k:write value="${smtpUsername}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.password"/>:</th>
        <td><k:notEmpty name="smtpPassword">
                ******
                <br><span class="formFieldDesc"><k:message key="admin.configDesc.smtp.password"/></span>
            </k:notEmpty>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.smtp.from"/>:</th>
        <td><k:write value="${emailFrom}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.smtp.to"/>:</th>
        <td><k:write value="${emailTo}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.smtp.starttls"/>:</th>
        <td><k:write value="${starttls}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.allowedDomains"/>:</th>
        <td><k:write value="${emailAllowedDomains}"/>
            <br><span class="formFieldDesc"><k:message key="admin.configDesc.email.allowedDomains"/></span>
        </td>
    </tr>    
    <tr>
        <th><k:message key="admin.config.email.issueReportTemplate"/>:</th>
        <td>${issueReportEmailTemplate}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.issueAddTemplate"/>:</th>
        <td>${issueAddEmailTemplate}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.issueUpdateTemplate"/>:</th>
        <td>${issueUpdateEmailTemplate}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.contractNotificationTemplate"/>:</th>
        <td>${contractExpireNotifyEmailTemplate}</td>
    </tr>
</table>

<%-- Incoming Email Settings --%>
<h3><k:message key="admin.config.email.incomingServer.header"/>
    <k:equal name="canEditPopSettings" value="true">
       <span class="command"> [${editPopSettingsLink}]</span>
    </k:equal>
</h3>
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th style="width:30%"><k:message key="admin.config.email.pop.host"/>:</th>
        <td><k:write value="${popHost}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.port"/>:</th>
        <td><k:write value="${popPort}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.username"/>:</th>
        <td><k:write value="${popUsername}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.password"/>:</th>
        <td><k:notEmpty name="popPassword">
                ******
                <br><span class="formFieldDesc"><k:message key="admin.configDesc.smtp.password"/></span>
            </k:notEmpty>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.pop.useSSL"/>:</th>
        <td><k:message key="common.boolean.true_false.${popUseSSL}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.pop.ignoreSender"/>:</th>
        <td>${popIgnoreSender}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.pop.retrievalFrequency"/>:</th>
        <td>${popRetrievalFrequency}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.pop.retrievalBatchSize"/>:</th>
        <td>${popRetrievalBatchSize}</td>
    </tr>
</table>
