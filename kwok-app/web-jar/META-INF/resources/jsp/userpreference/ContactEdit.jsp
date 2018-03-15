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

<form action="${formAction}" method="post" id="contactEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<table class="standardForm">
    <tr>
        <th width="15%"><k:message key="common.column.contact_id"/>:</th>
        <td width="35%">${contactId}</td>
        <th><k:message key="common.column.company_name"/>:</th>
        <td><k:write value="${contact.companyName}"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_title"/>:</th>
        <td><html:text name="form" property="contactTitle" size="40"/></td>
        <th><k:message key="common.column.contact_phone_work"/>:</th>
        <td><html:text name="form" property="contactPhoneWork" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_phone_home"/>:</th>
        <td><html:text name="form" property="contactPhoneHome" size="40"/></td>
        <th><k:message key="common.column.contact_phone_mobile"/>:</th>
        <td><html:text name="form" property="contactPhoneMobile" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_fax"/>:</th>
        <td><html:text name="form" property="contactFax" size="40"/></td>
        <th><k:message key="common.column.contact_address_street_primary"/>:</th>
        <td><html:text name="form" property="addressStreet" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_address_city_primary"/>:</th>
        <td><html:text name="form" property="addressCity" size="40"/></td>
        <th><k:message key="common.column.contact_address_state_primary"/>:</th>
        <td><html:text name="form" property="addressState" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_address_zipcode_primary"/>:</th>
        <td><html:text name="form" property="addressZipcode" size="40"/></td>
        <th><k:message key="common.column.contact_address_country_primary"/>:</th>
        <td><html:text name="form" property="addressCountry" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_email_secondary"/>:</th>
        <td><html:text name="form" property="contactEmailSecondary" size="40"/></td>
        <th><k:message key="common.column.contact_homepage_url"/>:</th>
        <td><html:text name="form" property="contactHomepageUrl" size="40"/>
            <br><span class="formFieldDesc"><k:message key="contactMgmt.colExample.website"/></span></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contact_im"/>:</th>
        <td><html:select name="form" property="messenger1Type">
            <html:options collection="messenger1TypeOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:text name="form" property="messenger1Id" size="24"/>
        </td>
        <th><k:message key="common.column.contact_im"/>:</th>
        <td>
            <html:select name="form" property="messenger2Type">
            <html:options collection="messenger2TypeOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:text name="form" property="messenger2Id" size="24"/>
        </td>
    </tr>
    <tr>
        <td colspan="4" align="center">
            <button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
    </tr>    
</table>
</form>
