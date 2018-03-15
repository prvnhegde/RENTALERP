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

<k:define id="hardware" name="hardware" type="com.kwoksys.biz.hardware.dto.Hardware"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="hardwareAddForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="_resubmit" value="true">
<table class="standardForm">
    <tr>
        <th><k:message key="common.column.hardware_id"/>:</th>
        <td><k:message key="form.autoId"/></td>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.hardware_name"/>:</th>
        <td><input type="text" name="hardwareName" value="<k:write value="${form.hardwareName}"/>" size="40" autofocus></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_manufacturer_name"/>:</th>
        <td><html:select name="form" property="manufacturerId">
            <html:options collection="manufacturersOptions" property="value" labelProperty="label"/>
            </html:select> ${manufacturerHelpIcon}</td>
        <th><k:message key="common.column.hardware_vendor_name"/>:</th>
        <td><html:select name="form" property="vendorId">
            <html:options collection="vendorsOptions" property="value" labelProperty="label"/>
            </html:select> ${vendorHelpIcon}</td>
    </tr>
    <tr>
        <th>${hardware.getAttrRequiredText('hardware_type')}<k:message key="common.column.hardware_type"/>:</th>
        <td><html:select name="form" property="hardwareType" onchange="App.submitFormUpdate(this.form, {'url': '${formThisAction}'})">
            <html:options collection="hardwareTypeOptions" property="value" labelProperty="label"/>
            </html:select></td>
        <th>${hardware.getAttrRequiredText('hardware_status')}<k:message key="common.column.hardware_status"/>:</th>
        <td><html:select name="form" property="hardwareStatus">
            <html:options collection="hardwareStatusOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_model_name"/>:</th>
        <td><input type="text" name="hardwareModelName" value="<k:write value="${form.hardwareModelName}"/>" size="40"></td>
        <th><k:message key="common.column.hardware_model_number"/>:</th>
        <td><html:text name="form" property="hardwareModelNumber" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_serial_number"/>:</th>
        <td><html:text name="form" property="serialNumber" size="40"/></td>
        <th><k:message key="common.column.hardware_purchase_price"/>:</th>
        <td><k:write value="${currencySymbol}"/><html:text name="form" property="hardwareCost" size="20"/>
            <span class="formFieldDesc"><k:message key="itMgmt.colDesc.hardware_cost" arg0="${currencySymbol}"/></span>
        </td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_purchase_date"/>:</th>
        <td><html:select name="form" property="purchaseMonth" onchange="App.setWarrantyExpireYear(this.form.name, this.form.warrantyDuration.value)">
                <html:options collection="purchaseMonthOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="purchaseDate" onchange="App.setWarrantyExpireYear(this.form.name, this.form.warrantyDuration.value)">
                <html:options collection="purchaseDateOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="purchaseYear" onchange="App.setWarrantyExpireYear(this.form.name, this.form.warrantyDuration.value)">
                <html:options collection="purchaseYearOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
        <th><k:message key="common.column.hardware_warranty_expire_date"/>:</th>
        <td><html:select name="form" property="warrantyMonth">
                <html:options collection="warrantyMonthOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="warrantyDate">
                <html:options collection="warrantyDateOptions" property="value" labelProperty="label"/>
            </html:select>
            <html:select name="form" property="warrantyYear">
                <html:options collection="warrantyYearOptions" property="value" labelProperty="label"/>
            </html:select>
            &nbsp;<k:message key="common.condition.or"/>&nbsp;
            <html:select name="form" property="warrantyDuration" onchange="App.setWarrantyExpireYear(this.form.name, this.form.warrantyDuration.value)">
                <html:options collection="warrantyPeriodOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_last_service_date"/>:</th>
        <td><html:checkbox name="form" property="lastServicedOn" value="1"/> <i><k:message key="itMgmt.colDesc.hardware_last_service_date"/></i></td>
        <th>${hardware.getAttrRequiredText('hardware_location')}<k:message key="common.column.hardware_location"/>:</th>
        <td><html:select name="form" property="hardwareLocation">
            <html:options collection="locationOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_description"/>:</th>
        <td><html:textarea name="form" property="hardwareDescription" rows="10" cols="50"/></td>
        <th><k:message key="common.column.hardware_owner_name"/>:</th>
        <td><html:select name="form" property="hardwareOwner">
                <html:options collection="hardwareOwnerOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td colspan="4"><jsp:include page="/jsp/common/template/CustomFieldsEdit.jsp"/></td>
    </tr>
    <tr>
        <td colspan="4" style="text-align:center">
            <button type="submit" name="submitBtn"><k:message key="form.button.add"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
