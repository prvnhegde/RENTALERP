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

<jsp:include page="/jsp/common/template/ActionMessages.jsp"/>

<form action="${formAction}" method="post" id="configPopEmailForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="cmd" value="${cmd}"/>
<input type="hidden" name="test" value="0"/>
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th><k:message key="admin.config.email.pop.host"/>:</th>
        <td><html:text name="form" property="popHost" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.port"/>:</th>
        <td><html:text name="form" property="popPort" size="40"/>
            <br><span class="formFieldDesc"><k:message key="admin.config.email.pop.port.description"/></span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.username"/>:</th>
        <td><html:text name="form" property="popUsername" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.password"/>:</th>
        <td><input type="password" name="popPassword" size="40" autocomplete="off">
            <br><span class="formFieldDesc"><k:message key="admin.config.email.password.description"/></span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.pop.useSSL"/>:</th>
        <td><html:checkbox name="form" property="popUseSSL" value="true"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.pop.ignoreSender"/>:</th>
        <td><html:textarea name="form" property="popIgnoreSender" rows="10" cols="50"/>
        <br><span class="formFieldDesc"><k:message key="admin.config.email.pop.ignoreSender.desc"/></span></td>
    </tr>
    <tr>
        <td colspan="2"><h3><k:message key="common.form.advancedOptions"/></h3></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.pop.retrievalFrequency"/>:</th>
        <td><html:select name="form" property="popRetrievalFrequency">
            <html:options collection="popRetrievalFrequencyOptions" property="value" labelProperty="label"/>
            </html:select> &nbsp;<k:message key="common.calendar.time.minutes"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.email.pop.retrievalBatchSize"/>:</th>
        <td><html:select name="form" property="popMessageBatchSize">
            <html:options collection="popMessageBatchSizeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>    
    <tr>
        <td style="width:30%">&nbsp;</td>
        <td><button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
        &nbsp;<button type="button" name="testBtn" onclick="Js.Form.setValue(this.form.test,1); App.submitFormUpdate(this.form, {'disable' : this})"><k:message key="form.button.test"/></button>
        ${formCancelLink}
        </td>
    </tr>
</table>
</form>
