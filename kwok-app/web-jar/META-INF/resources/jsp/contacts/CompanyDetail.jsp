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

<jsp:include page="/jsp/contacts/CompanySpecTemplate.jsp"/>

<jsp:include page="/jsp/common/template/CustomFieldsTableToggle.jsp"/>

<jsp:include page="/jsp/common/template/Tabs.jsp"/>

<table class="tabBody">
<tr class="themeHeader emptyHeader"><td>&nbsp;</td></tr>
<k:notEmpty name="mainContactList">
    <k:foreach id="row" name="mainContactList">
        <tr class="solidLine"><td>
        <k:define id="contact" name="row" property="contact" type="com.kwoksys.biz.contacts.dto.Contact"/>

    <table width="100%" cellspacing=5>
        <tr>
            <td colspan="2" class="lightGrayBorder" style="border-width:1px 0 0 0; border-style:solid;"><b><k:write value="${contact.title}"/></b> ${row.contactCommands}</td>
        </tr>
        <k:notEmpty name="row" property="contactNote"><tr><td>${row.contactNote}</td></tr></k:notEmpty>
        <tr>
        <td style="width:50%">
	        <k:notEmpty name="contact" property="addressStreetPrimary"><k:write value="${contact.addressStreetPrimary}"/><br></k:notEmpty>
	        <k:notEmpty name="contact" property="addressCityPrimary"><k:write value="${contact.addressCityPrimary}"/>, </k:notEmpty>
	        <k:write value="${contact.addressStatePrimary}"/>&nbsp;<k:write value="${contact.addressZipcodePrimary}"/><br>
	        <k:notEmpty name="contact" property="addressCountryPrimary"><k:write value="${contact.addressCountryPrimary}"/><br></k:notEmpty>
	        <k:notEmpty name="row" property="contactPhoneWork">
	            <k:message key="contactMgmt.companyContact.contactPhoneWork" arg0="${row.contactPhoneWork}"/><br>
	        </k:notEmpty>
	        <k:notEmpty name="row" property="contactFax">
	            <k:message key="contactMgmt.companyContact.contactFax" arg0="${row.contactFax}"/>
	        </k:notEmpty>
        </td>
        <td style="width:50%">
	        <k:notEmpty name="row" property="contactEmailPrimary">
	            <k:message key="contactMgmt.companyContact.contactEmailPrimary" arg0="${row.contactEmailPrimary}"/><br>            
	        </k:notEmpty>
	        <k:notEmpty name="row" property="contactHomepageUrl">
	            <k:message key="contactMgmt.companyContact.contactHomepageURL" arg0="${row.contactHomepageUrl}"/>
	        </k:notEmpty>&nbsp;
        </td>
        </tr>
    </table>

    </td></tr></k:foreach>
</k:notEmpty>
<%-- Show some message when there is no data --%>
<k:isEmpty name="mainContactList">
    <jsp:include page="/jsp/common/template/TableEmpty.jsp"/>
</k:isEmpty>
</table>
