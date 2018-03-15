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

<k:define id="softwareSearchTemplate" name="SoftwareSearchTemplate" type="com.kwoksys.action.software.SoftwareSearchTemplate"/>

<form action="${softwareSearchTemplate.formAction}" name="SoftwareSearchForm" id="SoftwareSearchForm" method="post" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<html:hidden name="SoftwareSearchForm" property="cmd" value="search"/>
<html:hidden name="SoftwareSearchForm" property="reportType" value="${reportType}"/>
<table class="standard">
    <tr>
        <th><k:message key="common.column.software_manufacturer"/>:</th>
        <td><html:select name="SoftwareSearchForm" property="manufacturerId" onchange="App.updateView('softwareDiv', '${formGetSoftwareAction}'+this.value);">
        <html:options collection="manufacturerOptions" property="value" labelProperty="label"/>
        </html:select>
        <%-- This area is left empty for AJAX script to get data. --%>
        <span id="softwareDiv"></span>
        </td>
    </tr>

    <tr>
        <th><k:message key="common.column.software_type"/>:</th>
        <td>
            <html:select name="SoftwareSearchForm" property="softwareTypes" multiple="true" size="5">
                <html:options collection="softwareTypeOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <k:notEmpty name="customFieldsOptions">
        <tr>
            <th><k:message key="common.template.customFields"/>:</th>
            <td><html:select name="SoftwareSearchForm" property="attrId">
                <html:options collection="customFieldsOptions" property="value" labelProperty="label"/>
            </html:select> <html:text name="SoftwareSearchForm" property="attrValue" size="40"/>
            </td>
        </tr>
    </k:notEmpty>
    <tr>
        <td colspan="2">
            <k:notEqual name="softwareSearchTemplate" property="hideSearchButton" value="true">
                <button type="submit" name="submitBtn"><k:message key="form.button.search"/></button>
                &nbsp;&nbsp;${softwareSearchTemplate.clearSearchLink}
            </k:notEqual>
        </td>
    </tr>
</table>
</form>
