<%--
 * Copyright 2015 Kwoksys
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

<k:define id="fileDeleteTemplate" name="FileDeleteTemplate" type="com.kwoksys.action.files.FileDeleteTemplate"/>

<!-- FileDeleteTemplate -->
<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${fileDeleteTemplate.formAction}" method="post" id="fileDeleteForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<div class="tabBody">
    <div class="themeHeader">
        <b><k:message key="files.fileDelete"/></b>
    </div>
    
    <p><img src="${image.deleteIcon}" class="standard" alt=""><b><k:message key="files.fileDelete.warning"/></b>
    <p>
    <table class="${StandardTemplate.detailsTableStyle}">
        <tr>
            <th style="width:10%"><k:message key="files.colName.file_name"/>:</th>
            <td><k:write value="${fileDeleteTemplate.fileName}"/></td>
        </tr>
        <tr>
            <th><k:message key="files.colName.file_friendly_name"/>:</th>
            <td><k:write value="${fileDeleteTemplate.fileTitle}"/></td>
        </tr>
        <tr>
            <th><k:message key="files.colName.file_byte_size"/>:</th>
            <td><k:write value="${fileDeleteTemplate.fileSize}"/></td>
        </tr>
        <tr>
            <td colspan="2">
               <button type="submit" name="submitBtn"><k:message key="form.button.delete"/></button>
               ${fileDeleteTemplate.formCancelLink}
            </td>
        </tr>
    </table>
</div>
</form>
