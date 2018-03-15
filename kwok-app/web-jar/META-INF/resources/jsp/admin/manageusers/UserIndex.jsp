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

<div class="section">
<ul>
<k:foreach id="link" name="links">
    <li>${link}</li>
</k:foreach>
</ul>
</div>

<h3><k:message key="admin.userIndex.userSearchTitle"/></h3>
<form action="${formUserSearchAction}" method="post" id="userSearchForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<html:hidden name="UserSearchForm" property="cmd" value="search"/>
<input type="hidden" name="_resubmit" value="true">
<table class="standard section">
    <tr>
        <th><k:message key="common.column.username"/>:</th>
        <td><html:text name="UserSearchForm" property="username" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.user_first_name"/>:</th>
        <td><html:text name="UserSearchForm" property="firstName" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.user_last_name"/>:</th>
        <td><html:text name="UserSearchForm" property="lastName" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.user_display_name"/>:</th>
        <td><html:text name="UserSearchForm" property="displayName" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.user_email"/>:</th>
        <td><select name="emailCriteria"><option value="equals"><k:message key="core.search.criteria.exactMatch"/></option></select>
            <html:text name="UserSearchForm" property="email" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.user_status"/>:</th>
        <td><html:select name="UserSearchForm" property="status">
            <html:options collection="statusOptions" property="value" labelProperty="label"/>
        </html:select></td>
    </tr>
    <tr>
        <td colspan="2">
            <button type="submit" name="submitBtn"><k:message key="form.button.search"/></button>
        </td>
    </tr>    
</table>
</form>
