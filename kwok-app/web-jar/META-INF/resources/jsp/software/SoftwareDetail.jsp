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

<jsp:include page="/jsp/software/SoftwareSpecTemplate.jsp"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<%-- Some ajax script here. --%>
<div id="hardwareDetail">&nbsp;</div>
<script type="text/javascript">
hardwarePopupAjax.clearCache();
hardwarePopupAjax.divPos = 'top';
hardwarePopupAjax.popupDiv = 'hardwareDetail';
hardwarePopupAjax.url = '${ajaxHardwareDetailPath}';
</script>

<jsp:include page="/jsp/common/template/CustomFieldsTableToggle.jsp"/>

<jsp:include page="/jsp/common/template/Tabs.jsp"/>

<form action="${formAction}" method="post" id="licenseForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<table class="listTable stripedListTable">
    <tr class="themeHeader">
        <k:equal name="canDeleteLicense" value="true"><th class="rownum">&nbsp;</th></k:equal>
        <th width="35%"><k:message key="common.column.license_key"/></th>
        <th><k:message key="common.column.license_note"/></th>
        <th><k:message key="common.column.license_entitlement"/></th>
        <th><k:message key="common.column.command"/></th>
        <th><k:message key="common.column.installed_on_hardware"/></th>
    </tr>
    <%-- Show input fields for adding a new license. --%>
    <k:equal name="addLicense" value="true">
    <html:hidden name="form" property="softwareId"/>
    <html:hidden name="form" property="cmd"/>
    <tr class="row2">
        <k:equal name="canDeleteLicense" value="true"><td>&nbsp;</td></k:equal>
        <td><input type="text" name="licenseKey" value="<k:write value="${form.licenseKey}"/>" size="60" autofocus>&nbsp;<k:message key="common.requiredFieldIndicator.true"/></td>
        <td><html:textarea name="form" property="licenseNote" rows="2" cols="40"/>&nbsp;</td>
        <td><html:text name="form" property="licenseEntitlement" size="10"/>&nbsp;<k:message key="common.requiredFieldIndicator.true"/></td>
        <td nowrap>
            <button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
        <td>&nbsp;</td>
    </tr>
    </k:equal>
    <%-- The UI is confusing because we have add/edit/remove all on the same page.
    Show licenses associated with this Software. --%>
    <k:notEmpty name="softwareLicenseList">
        <html:hidden name="form" property="softwareId" value="${softwareId}"/>
        <html:hidden name="form" property="cmd" value="${cmd}"/>
        <k:foreach id="row" name="softwareLicenseList">
            <tr class="dataRow">
            <k:equal name="row" property="licenseId" value="${formLicenseId}">
                <html:hidden name="form" property="licenseId" value="${formLicenseId}"/>
                <k:equal name="canDeleteLicense" value="true">
                    <td rowspan="${row.rowSpan}">&nbsp;</td>
                </k:equal>
                <td rowspan="${row.rowSpan}"><html:text name="form" property="licenseKey" size="60"/>&nbsp;<k:message key="common.requiredFieldIndicator.true"/></td>
                <td rowspan="${row.rowSpan}"><html:textarea name="form" property="licenseNote" rows="2" cols="40"/></td>
                <td rowspan="${row.rowSpan}"><html:text name="form" property="licenseEntitlement" size="10"/>&nbsp;<k:message key="common.requiredFieldIndicator.true"/></td>
                <td rowspan="${row.rowSpan}">
                    <button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
                    ${formCancelLink}</td>
            </k:equal>
            <k:notEqual name="row" property="licenseId" value="${formLicenseId}">
                <k:isPresent name="row" property="showCol">
                    <k:equal name="canDeleteLicense" value="true">
                        <td rowspan="${row.rowSpan}">
                            <html:radio name="form" property="licenseId" value="${row.licenseId}"/>
                        </td>
                    </k:equal>
                    <td rowspan="${row.rowSpan}">${row.licenseKey}<div id="cf${row.licenseId}" class="customFieldEmbedded" style="display:none"></div></td>
                    <td rowspan="${row.rowSpan}">${row.licenseNote}</td>
                    <td rowspan="${row.rowSpan}"><k:write value="${row.licenseEntitlement}"/></td>
                    <%-- Show the edit link only if the user has permission. --%>
                    <td rowspan="${row.rowSpan}">
                    <k:isPresent name="row" property="editLink">
                        ${row.editLink}
                    </k:isPresent>&nbsp;
                    </td>
                </k:isPresent>
            </k:notEqual>
            <td>${row.hardwareName}&nbsp;</td>
            </tr>
        </k:foreach>
        <k:equal name="canDeleteLicense" value="true">
            <tr class="themeHeader">
                <td colspan="${colSpan}">
                    <input type="button" value="<k:message key="itMgmt.softwareDetail.deleteLicenseButton"/>" onclick="Js.Modal.newInstance().setBody('<k:message key="common.form.confirmDelete"/>').render(this, function(formElem) {App.submitFormUpdate(formElem);})">
                </td>
            </tr>
        </k:equal>
    </k:notEmpty>
    <%-- Show some message when there is no data --%>
    <k:isEmpty name="softwareLicenseList">
        <jsp:include page="/jsp/common/template/TableEmpty.jsp"/>
    </k:isEmpty>
</table>
</form>
<%-- Show computers that are using invalid software licenses --%>
<k:notEmpty name="softwareNeedLicenseList">
<br><br>
<table class="listTable">
    <tr class="themeHeader"><td colspan="2"><b><k:message key="itMgmt.softwareDetail.unknownLicenseTableHeader"/></b></td></tr>
    <k:foreach id="row" name="softwareNeedLicenseList">
        <tr class="row2">
            <td class="rownum"><k:write value="${row.rowNum}"/></td>
            <td width="100%">${row.hardwareName}</td>
        </tr>
    </k:foreach>
</table>
</k:notEmpty>
