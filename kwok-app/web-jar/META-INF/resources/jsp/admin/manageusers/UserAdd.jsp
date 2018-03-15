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

<form action="${formAction}" method="post" id="userAddForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<html:hidden name="form" property="contactId"/>
<table class="standardForm">
    <tr>
        <th><k:message key="common.column.user_id"/>:</th>
        <td><k:message key="form.autoId"/></td>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.username"/>:</th>
        <td><input type="text" name="username" value="<k:write value="${form.username}"/>" size="40" autofocus></td>
    </tr>
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.user_first_name"/>:</th>
        <td><html:text name="form" property="firstName" size="40" onkeyup="App.refreshDisplayName(this, this.form.lastName, this.form.displayName)"/></td>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.user_last_name"/>:</th>
        <td><html:text name="form" property="lastName" size="40" onkeyup="App.refreshDisplayName(this.form.firstName, this, this.form.displayName)"/></td>
    </tr>
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.user_display_name"/>:</th>
        <td><html:text name="form" property="displayName" size="40"/></td>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.user_email"/>:</th>
        <td><html:text name="form" property="email" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.user_status"/>:</th>
        <td><html:select name="form" property="status" onchange="App.togglePasswordFields(this.value, 'passwordSpan', 'confirmPasswordSpan')">
            <html:options collection="optionStatus" property="value" labelProperty="label"/>
        </html:select></td>
        <th><k:message key="common.column.user_group"/>:</th>
        <td><html:select name="form" property="groupId">
            <html:options collection="groupIdOptions" property="value" labelProperty="label"/>
        </html:select></td>
    </tr>
    <k:equal name="allowPasswordUpdate" value="true">
    <tr>
        <th>
            <k:equal name="allowBlankPassword" value="false">
                <span id="passwordSpan"><k:message key="common.requiredFieldIndicator.true"/></span>
            </k:equal>
            <k:message key="common.column.user_password"/>:</th>
        <td><input type="password" name="password" size="40" autocomplete="off"></td>
        <th>
            <k:equal name="allowBlankPassword" value="false">
                <span id="confirmPasswordSpan"><k:message key="common.requiredFieldIndicator.true"/></span>
            </k:equal>
            <k:message key="common.column.user_password_confirm"/>:</th>
        <td><input type="password" name="confirmPassword" size="40" autocomplete="off"></td>
    </tr>
    </k:equal>

    <tr>
        <td colspan="4"><jsp:include page="/jsp/common/template/CustomFieldsEdit.jsp"/></td>
    </tr>
    
    <tr><td colspan="4"><h3><k:message key="admin.user.tab.contact"/></h3></td></tr>
    <tr>
        <th><k:message key="common.column.contact_title"/>:</th>
        <td><html:text name="form" property="contactTitle" size="40"/></td>
        <th><k:message key="common.column.company_name"/>:</th>
        <td><html:select name="form" property="companyId">
            <html:options collection="optionsCompanyId" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_phone_work"/>:</th>
        <td><html:text name="form" property="contactPhoneWork" size="40"/></td>
        <th><k:message key="common.column.contact_phone_mobile"/>:</th>
        <td><html:text name="form" property="contactPhoneMobile" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_phone_home"/>:</th>
        <td><html:text name="form" property="contactPhoneHome" size="40"/></td>
        <th><k:message key="common.column.contact_fax"/>:</th>
        <td><html:text name="form" property="contactFax" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_address_street_primary"/>:</th>
        <td><html:text name="form" property="addressStreet" size="40"/></td>
        <th><k:message key="common.column.contact_address_city_primary"/>:</th>
        <td><html:text name="form" property="addressCity" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_address_state_primary"/>:</th>
        <td><html:text name="form" property="addressState" size="40"/></td>
        <th><k:message key="common.column.contact_address_zipcode_primary"/>:</th>
        <td><html:text name="form" property="addressZipcode" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_address_country_primary"/>:</th>
        <td><html:text name="form" property="addressCountry" size="40"/></td>
        <th><k:message key="common.column.contact_email_secondary"/>:</th>
        <td><html:text name="form" property="contactEmailSecondary" size="40"/></td>
    <tr>
        <th><k:message key="common.column.contact_im"/>:</th>
        <td><html:select name="form" property="messenger1Type">
            <html:options collection="messengerTypeOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:text name="form" property="messenger1Id" size="24"/>
        </td>
        <th><k:message key="common.column.contact_im"/>:</th>
        <td>
            <html:select name="form" property="messenger2Type">
            <html:options collection="messengerTypeOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:text name="form" property="messenger2Id" size="24"/>
        </td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_homepage_url"/>:</th>
        <td><html:text name="form" property="contactHomepageUrl" size="40"/></td>
        <th><k:message key="common.column.contact_description"/>:</th>
        <td><html:textarea name="form" property="contactDescription" rows="8" cols="30"/></td>
    </tr>
    <tr>
        <td colspan="4" align="center">
            <button type="submit" name="submitBtn"><k:message key="form.button.add"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
