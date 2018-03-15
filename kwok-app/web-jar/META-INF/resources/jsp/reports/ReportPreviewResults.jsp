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

<h2><k:message key="reports.workflow.previewResults.header"/></h2>

<k:equal name="reportType" value="hardware_report">
    <%-- Some ajax script here. --%>
    <div id="hardwareDetail">&nbsp;</div>
    <script type="text/javascript">
	    hardwarePopupAjax.clearCache();
	    hardwarePopupAjax.popupDiv = 'hardwareDetail';
	    hardwarePopupAjax.url = '${ajaxHardwareDetailPath}';
    </script>
</k:equal>
<k:equal name="reportType" value="hardware_member_report">
    <%-- Some ajax script here. --%>
    <div id="hardwareDetail">&nbsp;</div>
    <script type="text/javascript">
	    hardwarePopupAjax.clearCache();
	    hardwarePopupAjax.popupDiv = 'hardwareDetail';
	    hardwarePopupAjax.url = '${ajaxHardwareDetailPath}';
    </script>
</k:equal>

<form action="${formAction}" method="post" target="_blank">
<input type="hidden" name="_resubmit" value="true">
<table class="standard">
    <tr>
        <td colspan="2"><k:message key="reports.workflow.previewResults.description"/>
            &nbsp;<input type="button" onclick="App.disableButton(this);App.updateViewHistory('${backAction}')" value="<k:message key="reports.button.back"/>">
            &nbsp;<html:submit><k:message key="reports.button.run"/></html:submit><p>
        </td>
    </tr>
    <tr>
        <th width="20%"><k:message key="reports.field.report_title"/>:</th>
        <td width="80%"><html:text name="ReportForm" property="reportTitle" size="60"/>
            &nbsp;<span class="formFieldDesc"><k:message key="reports.workflow.outputSelection.report_title.desc"/></span></td>
    </tr>
    <tr>
        <th><k:message key="reports.field.output_format"/>:</th>
        <td><k:foreach id="output" name="outputOptions">
                <html:radio name="ReportForm" property="outputType" value="${output}"/>
                <k:message key="reports.workflow.output.${output}"/>&nbsp;
            </k:foreach>
        </td>
    </tr>
    <tr>
        <th><k:message key="reports.workflow.previewResults.columns"/>:</th>
        <td>
            <input type="checkbox" onclick="Js.Form.checkAll(this, this.form.reportColumns);"><k:message key="common.form.checkAll"/><br>
            <k:foreach id="column" name="reportColumnOptions">
                <html:multibox name="ReportForm" property="reportColumns" value="${column.value}"/>${column.label}&nbsp;
            </k:foreach>
        </td>
    </tr>
    <k:notEmpty name="reportSortColumnOptions">
    <tr>
        <th><k:message key="reports.workflow.previewResults.sortColumns"/>:</th>
        <td>
            <html:select name="ReportForm" property="reportSortColumns">
                <html:options collection="reportSortColumnOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="ReportForm" property="reportSortOrder">
                <html:options collection="reportSortOrderOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    </k:notEmpty>
</table>
<form>

<div style="float:right; padding:4px">
    <jsp:include page="/jsp/common/template/RecordsNavigationWidget.jsp"/>
</div>

<jsp:include page="/jsp/common/template/Table.jsp"/>
