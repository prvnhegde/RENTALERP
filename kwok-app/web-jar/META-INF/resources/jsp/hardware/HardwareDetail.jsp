<%--
 * Copyright 2017 Kwoksys
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

<jsp:include page="/jsp/hardware/HardwareSpecTemplate.jsp"/>

<k:isEmpty name="form" property="cmd">
<p>
<jsp:include page="/jsp/common/template/CustomFieldsTableToggle.jsp"/>
</k:isEmpty>

<%-- The user is trying to add a Software to this Hardware --%>
<k:equal name="form" property="cmd" value="add">
<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAddLicAction}" method="post" id="hardwareForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.saveBtn})">
<html:hidden name="form" property="hardwareId"/>
<table class="listTable">
<tr class="themeHeader"><td colspan="2"><b><k:message key="itMgmt.hardwareDetail.addLicenseHeader"/> </b></td></tr>
<tr>
    <th class="row2" style="width:200px"><k:message key="itMgmt.hardwareDetail.addLicenseStep1"/></th>
    <td class="row2">
        <k:notEmpty name="softwareOptions">
            <html:select name="form" property="softwareId" onchange="App.updateView('softwareLicensesDiv', '${formGetSoftwareLicenseAction}'+this.value);" >
                <html:options collection="softwareOptions" property="value" labelProperty="label"/>
            </html:select>
        </k:notEmpty>
        <k:isEmpty name="softwareOptions" >
            <k:message key="itMgmt.hardwareDetail.noSoftware"/>
        </k:isEmpty>
    </td>
</tr>
<tr>
    <th class="row2"><k:message key="itMgmt.hardwareDetail.addLicenseStep2"/></th>
    <%-- This area is left empty for AJAX script to get data. --%>
    <td class="row2" id="softwareLicensesDiv">&nbsp;</td>
</tr>
<tr>
    <th class="row2">
        <k:message key="common.column.license_entitlement"/>
    </th>
    <td class="row2">
        ${form.licenseEntitlement}
        <html:hidden name="form" property="licenseEntitlement"/>
    </td>
</tr>
<tr>
<th class="row2"><k:message key="itMgmt.hardwareDetail.addLicenseStep3"/></th>
<td class="row2">
    <k:notEmpty name="softwareOptions">
        <button type="submit" name="saveBtn"><k:message key="form.button.save"/></button>
        ${formCancelLink}
    </k:notEmpty>
</td>
</tr>
</table>
</form>
<p>
</k:equal>

<jsp:include page="/jsp/common/template/Tabs.jsp"/>

<form action="${formRemoveLicenseAction}" method="post" id="hardwareLicenseForm">
<html:hidden name="form" property="hardwareId"/>
<table class="listTable stripedListTable">
    <tr class="themeHeader">
        <k:equal name="canRemoveLicense" value="true">
            <td>&nbsp;</td>
        </k:equal>
        <th style="text-align:left; width:34%"><k:message key="common.column.software_name"/></th>
        <th style="text-align:left; width:33%"><k:message key="common.column.license_key"/></th>
        <th style="text-align:left; width:33%"><k:message key="software.license.details"/></th>
    </tr>
    <%-- Show the data --%>
    <k:notEmpty name="installedLicenses">
        <k:foreach id="row" name="installedLicenses">
            <tr class="dataRow">
                <k:equal name="canRemoveLicense" value="true">
                    <td><html:radio name="form" property="mapId" value="${row.mapId}"/></td>
                </k:equal>
                <td>${row.softwareName}</td>
                <td>${row.licenseKey}&nbsp;${row.licenseInfoLink}</td>
                <td>
                    <span id="note${row.licenseId}">${row.licenseNote}</span>
                    <span id="cf${row.licenseId}" class="customFieldEmbedded" style="display:none"></span>
                </td>
            </tr>
        </k:foreach>
        <k:equal name="canRemoveLicense" value="true">
            <tr class="themeHeader"><td colspan="${colSpan}"><input type="button" value="<k:message key="itMgmt.hardwareDetail.removeLicenseButton"/>" onclick="Js.Modal.newInstance().setBody('<k:message key="common.form.confirmRemove"/>').render(this, function(formElem){App.submitFormUpdate(formElem);})"></td></tr>
        </k:equal>
    </k:notEmpty>
    <%-- Show some message when there is no data --%>
    <k:isEmpty name="installedLicenses">
        <jsp:include page="/jsp/common/template/TableEmpty.jsp"/>
    </k:isEmpty>
</table>
</form>
