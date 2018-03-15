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

<k:define id="contactAssociateTemplate" name="ContactAssociateTemplate" type="com.kwoksys.action.contacts.ContactAssociateTemplate"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${contactAssociateTemplate.formSearchAction}" method="post" id="contactAssociateForm" onsubmit="return App.submitFormUpdate(this, {'disable': this.searchBtn})">
<input type="hidden" name="_resubmit" value="true">
<table class="listTable">
    <tr class="themeHeader">
        <td colspan="2"><b><k:message key="common.linking.linkContacts"/></b></td>
    </tr>
    <tr>
        <th><k:message key="contactMgmt.index.contactSearch"/>:</th>
        <td><k:message key="common.column.contact_id"/>&nbsp;<input type="text" name="formContactId" value="<k:write value="${form.formContactId}"/>" size="40" autofocus>
            <button type="submit" name="searchBtn"><k:message key="form.button.search"/></button>
        </td>
    </tr>
    <tr>
        <th><k:message key="common.linking.selectContact"/>:</th>
        <td>
            <k:isEmpty name="contactAssociateTemplate" property="contactList">
                <k:message key="${selectContactMessage}"/>
            </k:isEmpty>
            <k:notEmpty name="contactAssociateTemplate" property="contactList">
                <table class="noBorder">
                    <k:foreach id="contact" name="contactAssociateTemplate" property="contactList">
                        <tr>
                            <td><html:checkbox name="contactAssociateTemplate" property="contactId" value="${contact.contactId}"/>
                                <k:write value="${contact.contactName}"/></td>
                            <td>
                        </tr>
                        <tr>
                            <td>
                                <k:message key="common.column.relationship_description"/>:
                                <html:text name="form" property="relationshipDescription" size="50" maxlength="50"/>
                            </td>
                        </tr>
                    </k:foreach>
                </table>
            </k:notEmpty>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <button type="button" onclick="App.submitFormUpdate(this.form, {'url': '${contactAssociateTemplate.formSaveAction}', 'disable': this})" ${contactAssociateTemplate.disableSaveButton}><k:message key="form.button.save"/></button>
            ${contactAssociateTemplate.formCancelLink}
        </td>
    </tr>
</table>
</form>
