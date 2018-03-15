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

<form action="${formAction}" method="post" id="hardwareSearchForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">

<table class="standard">
<tr>
    <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="itMgmt.index.rental.assign.customer.name"/>:</th>
   
    <td><html:text name="HardwareSearchForm" property="customerName" size="40"/></td>
</tr>
<tr>
        <th><k:message key="common.column.hardware.rental.start.date"/>:</th>
        <td>
            <html:select name="HardwareSearchForm" property="rentalstartdateDate" value="${rentalstartdateDate}">
                <html:options collection="rentalstartdateDateOptions" property="value" labelProperty="label"/>
            </html:select>
        	<html:select name="HardwareSearchForm" property="rentalstartdateMonth" value="${rentalstartdateMonth}">
                <html:options collection="rentalstartdateMonthOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="HardwareSearchForm" property="rentalstartdateYear" value="${rentalstartdateYear}">
                <html:options collection="rentalstartdateYearOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
</tr>
<tr>        
        <th><k:message key="common.column.hardware.rental.end.date"/>:</th>
        <td>
        	<html:select name="HardwareSearchForm" property="rentalenddateDate" value="${rentalenddateDate}">
                <html:options collection="rentalenddateDateOptions" property="value" labelProperty="label"/>
            </html:select>
        	<html:select name="HardwareSearchForm" property="rentalenddateMonth" value="${rentalenddateMonth}">
                <html:options collection="rentalenddateMonthOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="HardwareSearchForm" property="rentalenddateYear" value="${rentalenddateYear}">
                <html:options collection="rentalenddateYearOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
</tr>       
<tr>
        <td colspan="4" style="text-align:left">
            <button type="submit" name="submitBtn"><k:message key="form.button.rentout"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
<%-- Hardware search --%>
<table class="standard">
<tr>
    <th>${searchResultText}&nbsp;</th>
    
    <td align="right">
    <k:message key="itMgmt.hardwareList.hardwareTypeFilter"/>:
        <html:select name="HardwareSearchForm" property="hardwareTypeFilter" onchange="App.changeSelectedOption(this);">
            <html:options collection="hardwareTypeOptions" property="value" labelProperty="label"/>
        </html:select>
    </td>

    <td style="width:20%; text-align:right; vertical-align:middle" nowrap>
        <jsp:include page="/jsp/common/template/RecordsNavigationWidget.jsp"/>
    </td>
</tr>
</table>
<%-- Some ajax script here. --%>
<div id="hardwareDetail">&nbsp;</div>
<script type="text/javascript">
hardwarePopupAjax.clearCache();
hardwarePopupAjax.popupDiv = 'hardwareDetail';
hardwarePopupAjax.url = '${ajaxHardwareDetailPath}';
</script>
<jsp:include page="/jsp/common/template/Table.jsp"/>
</form>