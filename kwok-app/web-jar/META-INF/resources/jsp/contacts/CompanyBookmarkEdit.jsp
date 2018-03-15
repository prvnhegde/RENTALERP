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

<jsp:include page="/jsp/contacts/CompanySpecTemplate.jsp"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="companyBookmarkEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<html:hidden name="form" property="companyId"/>
<html:hidden name="form" property="bookmarkId"/>
<table class="listTable">
    <tr class="themeHeader">
        <td colspan="2"><b><k:message key="bookmarkMgmt.bookmarkEdit"/></b></td>
    </tr>
    <tr>
        <td><k:message key="common.column.bookmark_name"/>:</td>
        <td><input type="text" name="bookmarkName" value="<k:write value="${form.bookmarkName}"/>" size="40" autofocus>&nbsp;<k:message key="common.requiredFieldIndicator.true"/></td>
    </tr>
    <tr>
        <td><k:message key="common.column.bookmark_path"/>:</td>
        <td><html:text name="form" property="bookmarkPath" size="60"/>&nbsp;<k:message key="common.requiredFieldIndicator.true"/>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td><button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
