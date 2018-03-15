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

<form action="${formAction}" name="dataImportForm" method="post" enctype="multipart/form-data" onsubmit="return App.disableButtonSubmit(this.submitBtn)">
<input type="hidden" name="_resubmit" value="true">
<table class="standardForm">
<tr>
    <th><k:message key="import.selectTypes"/>:</th>
    <td>
        <html:select name="DataImportForm" property="importType" onchange="App.toggleImportType(this.value)">
            <html:options collection="importTypeOptions" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr id="allowDuplicate">
    <th><k:message key="import.selectDuplicateHandling"/>:</th>
    <td>
        <html:radio name="DataImportForm" property="allowDuplicate" value="false"/>
        <k:message key="import.selectDuplicateHandling.update"/>&nbsp;

        <k:equal name="isCheckUniqueHardwareName" value="false">
            <html:radio name="DataImportForm" property="allowDuplicate" value="true"/>
            <k:message key="import.selectDuplicateHandling.add"/>
        </k:equal>
    </td>
</tr>
<tr>
    <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="import.selectImportFile"/>:</th>
    <td>
        <input type="file" name="file0" size="60">&nbsp;
        <p>&nbsp;<k:foreach id="sampleFileLink" name="sampleFileLinks">
            ${sampleFileLink}&nbsp;
        </k:foreach>
    </td>
</tr>
<tr>
    <th>&nbsp;</th>
    <td>
        <button type="submit" name="submitBtn"><k:message key="form.button.next"/></button>
        ${formCancelLink}
    </td>
</tr>
</table>
</form>

<script type="text/javascript">
    App.toggleImportType(dataImportForm.importType.value);
</script>