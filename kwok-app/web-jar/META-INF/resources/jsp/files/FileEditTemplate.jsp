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

<k:define id="fileEditTemplate" name="FileEditTemplate" type="com.kwoksys.action.files.FileEditTemplate"/>

<!-- FileEditTemplate -->
<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<%-- Show input fields for Editing File. --%>
<form action="${fileEditTemplate.formAction}" method="post" id="fileEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<table class="listTable">
    <tr class="themeHeader">
        <td colspan="2"><b><k:message key="files.fileEdit"/></b></td>
    </tr>
    <tr>
        <td style="width:10%"><k:message key="files.colName.file_name"/>:</td>
        <td><k:write value="${fileEditTemplate.fileName}"/></td>
    </tr>
    <tr>
        <td><k:message key="files.colName.file_byte_size"/>:</td>
        <td><k:write value="${fileEditTemplate.fileSize}"/></td>
    </tr>
    <tr>
        <td><k:message key="files.colName.file_friendly_name"/>:</td>
        <td><html:text name="form" property="fileName0" value="${fileEditTemplate.fileTitle}" size="40"/>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
            ${fileEditTemplate.formCancelLink}
    </tr>
</table>
</form>
