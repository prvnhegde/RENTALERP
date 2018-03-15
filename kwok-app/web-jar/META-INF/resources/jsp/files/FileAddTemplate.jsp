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

<k:define id="fileAddTemplate" name="FileAddTemplate" type="com.kwoksys.action.files.FileAddTemplate"/>

<!-- FileAddTemplate -->
<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<%-- Show input fields for adding a new File. --%>
<form action="${fileAddTemplate.formAction}" method="post" enctype="multipart/form-data" onsubmit="return App.disableButtonSubmit(this.submitBtn)">
<table class="listTable">
    <tr class="themeHeader">
        <td colspan="2"><b><k:message key="files.fileAdd"/></b></td>
    </tr>
    <tr>
        <td><k:message key="common.requiredFieldIndicator.true"/><k:message key="files.filePath"/>:</td>
        <td><input type="file" name="file0" size="60"></td>
    </tr>
    <tr>
        <td><k:message key="files.colName.file_friendly_name"/>:</td>
        <td><html:text name="form" property="fileName0" value="${fileAddTemplate.fileName}" size="40"/>
            <br><span class="formFieldDesc"><k:message key="files.colDesc.file_name"/></span>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <button type="submit" name="submitBtn"><k:message key="form.button.upload"/></button>
            ${fileAddTemplate.formCancelLink}
    </tr>
</table>
</form>
