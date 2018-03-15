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
<%-- TODO: Need better layout for small screen --%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<jsp:include page="/jsp/common/template/ActionMessages.jsp"/>

<form action="${formAction}" method="post" id="configEmailForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="cmd" value="${cmd}"/>
<input type="hidden" name="test" value="0"/>
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th><k:message key="admin.config.email.notification.issues.fromUi"/>:</th>
        <td><html:select name="form" property="issueNotificationFromUiOn">
            <html:options collection="onOffOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.notification.issues.fromEmail"/>:</th>
        <td><html:select name="form" property="issueNotificationFromEmailOn">
            <html:options collection="onOffOptions" property="value" labelProperty="label"/>
            </html:select>
            <br>
            <span class="formFieldDesc"><k:message key="admin.config.email.notification.issues.fromUi.description"/></span>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.notification.contractExpiration"/>:</th>
        <td><html:select name="form" property="contractExpireNotificationOn">
            <html:options collection="onOffOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.smtp.host"/>:</th>
        <td><html:text name="form" property="smtpHost" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.port"/>:</th>
        <td><html:text name="form" property="smtpPort" size="40"/>
            <br><span class="formFieldDesc"><k:message key="admin.configDesc.smtp.port"/></span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.username"/>:</th>
        <td><html:text name="form" property="smtpUsername" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.password"/>:</th>
        <td><input type="password" name="smtpPassword" size="40" autocomplete="off">
            <br><span class="formFieldDesc"><k:message key="admin.config.email.password.description"/></span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.smtp.from"/>:</th>
        <td><html:text name="form" property="smtpFrom" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.smtp.to"/>:</th>
        <td><html:text name="form" property="smtpTo" size="40"/>
            <br><span class="formFieldDesc"><k:message key="admin.config.email.smtp.to.description"/></span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.smtp.starttls"/>:</th>
        <td><html:select name="form" property="smtpStarttls">
            <html:options collection="onOffOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.allowedDomains"/>:</th>
        <td><html:text name="form" property="allowedDomains" size="40"/>
            <br><span class="formFieldDesc"><k:message key="admin.configDesc.email.allowedDomains"/></span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.issueReportTemplate"/>:</th>
        <td><div style="float:left">
                <html:textarea name="form" property="reportIssueEmailTemplate" cols="60" rows="10"/>
                <br><span class="formFieldDesc"><k:message key="admin.configDesc.email.issueTemplate"/></span>
            </div>
            <div style="float:left; padding-left:10px"><pre class="formFieldDesc"><k:message key="issuePlugin.issueAdd2.emailBody"/></pre></div>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.issueAddTemplate"/>:</th>
        <td><div style="float:left">
                <html:textarea name="form" property="issueAddEmailTemplate" cols="60" rows="10"/>
                <br><span class="formFieldDesc"><k:message key="admin.configDesc.email.issueTemplate"/></span>
            </div>
            <div style="float:left; padding-left:10px"><pre class="formFieldDesc"><k:message key="issues.issueAdd2.emailBody"/></pre></div>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.issueUpdateTemplate"/>:</th>
        <td><div style="float:left">
                <html:textarea name="form" property="issueUpdateEmailTemplate" cols="60" rows="10"/>
                <br><span class="formFieldDesc"><k:message key="admin.configDesc.email.issueTemplate"/></span>
            </div>
            <div style="float:left; padding-left:10px"><pre class="formFieldDesc"><k:message key="issues.issueEdit2.emailBody"/></pre></div>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.contractNotificationTemplate"/>:</th>
        <td><div style="float:left">
                <html:textarea name="form" property="contractExpireNotifyEmailTemplate" cols="60" rows="10"/>
                <br><span class="formFieldDesc"><k:message key="admin.configDesc.email.issueTemplate"/></span>
            </div>
            <div style="float:left; padding-left:10px"><pre class="formFieldDesc"><k:message key="contracts.expirationNotification.body"/></pre></div>
        </td>
    </tr>
    <tr>
        <td style="width:30%">&nbsp;</td>
        <td><button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
        &nbsp;<button type="button" name="testBtn" onclick="Js.Form.setValue(this.form.test,1); App.submitFormUpdate(this.form, {'disable' : this})"><k:message key="form.button.test"/></button>
        ${formCancelLink}</td>
    </tr>
</table>
</form>
