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

<k:define id="hardwareSearchTemplate" name="HardwareSearchTemplate" type="com.kwoksys.action.hardware.HardwareSearchTemplate"/>

<%-- This is for showing hardware search --%>
<form action="${hardwareSearchTemplate.formAction}" method="post" name="HardwareSearchForm" id="HardwareSearchForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="_resubmit" value="true">
<html:hidden name="HardwareSearchForm" property="cmd" value="search"/>
<table class="standard section">
    <tr>
        <th><k:message key="common.column.hardware_id"/>:</th>
        <td><html:text name="HardwareSearchForm" property="hardwareId" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_name"/>:</th>
        <td><html:select name="HardwareSearchForm" property="hardwareNameCriteria">
            <html:options collection="hardwareNameCriteriaOptions" property="value" labelProperty="label"/>
            </html:select> <html:text name="HardwareSearchForm" property="hardwareName" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_description"/>:</th>
        <td><html:text name="HardwareSearchForm" property="description" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_model_name"/>:</th>
        <td><html:text name="HardwareSearchForm" property="hardwareModelName" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_model_number"/>:</th>
        <td><html:text name="HardwareSearchForm" property="hardwareModelNumber" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_serial_number"/>:</th>
        <td><html:text name="HardwareSearchForm" property="serialNumber" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_owner_name"/>:</th>
        <td><html:text name="HardwareSearchForm" property="hardwareOwner" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_manufacturer_name"/>:</th>
        <td><html:select name="HardwareSearchForm" property="manufacturerId">
            <html:options collection="manufacturersOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.column.hardware_vendor_name"/>:</th>
        <td><html:select name="HardwareSearchForm" property="vendorId">
            <html:options collection="vendorsOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr><th><k:message key="hardware.index.search.purchase_date"/>:</th>
        <td>
        <html:select name="HardwareSearchForm" property="purchasedAfterMonth">
        <html:options collection="monthOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="HardwareSearchForm" property="purchasedAfterDate">
        <html:options collection="dateOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="HardwareSearchForm" property="purchasedAfterYear">
        <html:options collection="yearOptions" property="value" labelProperty="label"/>
        </html:select>
        -
        <html:select name="HardwareSearchForm" property="purchasedBeforeMonth">
        <html:options collection="monthOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="HardwareSearchForm" property="purchasedBeforeDate">
        <html:options collection="dateOptions" property="value" labelProperty="label"/>
        </html:select>
        <html:select name="HardwareSearchForm" property="purchasedBeforeYear">
        <html:options collection="yearOptions" property="value" labelProperty="label"/>
        </html:select>
        </td>
    </tr>

    <tr><th><k:message key="common.column.hardware_warranty_expire_date"/>:</th>
        <td>
            <k:foreach id="warranty" collection="${warrantyOptions}">
                <input type="checkbox" name="${warranty.key}" value="true" ${warranty.checked}>
                <k:message key="hardware.$${warranty.key}"/>
            </k:foreach>
        </td>
    </tr>

    <tr><td colspan="2">
        <table><tr>
        <th><k:message key="common.column.hardware_type"/>:</th>
        <th>
            <html:select name="HardwareSearchForm" property="hardwareTypes" multiple="true" size="5">
                <html:options collection="hardwareTypeOptions" property="value" labelProperty="label"/>
            </html:select>
        </th>
        <th><k:message key="common.column.hardware_status"/>:</th>
        <th>
            <html:select name="HardwareSearchForm" property="hardwareStatus" multiple="true" size="5">
                <html:options collection="hardwareStatusOptions" property="value" labelProperty="label"/>
            </html:select>
        </th>
        <th><k:message key="common.column.hardware_location"/>:</th>
        <th>
            <html:select name="HardwareSearchForm" property="hardwareLocation" multiple="true" size="5">
                <html:options collection="hardwareLocationOptions" property="value" labelProperty="label"/>
            </html:select>
        </th>
        </tr></table>
        </td>
    </tr>
    <%-- Search by custom fields --%>
    <k:notEmpty name="customFieldsOptions">
        <tr><th><k:message key="common.template.customFields"/>:</th>
            <td><html:select name="HardwareSearchForm" property="attrId">
                <html:options collection="customFieldsOptions" property="value" labelProperty="label"/>
            </html:select> <html:text name="HardwareSearchForm" property="attrValue" size="40"/>
        </td></tr>
    </k:notEmpty>
    <%-- Search by components --%>
    <k:notEmpty name="componentTypeOptions">
        <tr><th><k:message key="common.column.hardware_component_type"/>:</th>
            <td><html:select name="HardwareSearchForm" property="compTypeId">
                <html:options collection="componentTypeOptions" property="value" labelProperty="label"/>
            </html:select> <html:text name="HardwareSearchForm" property="compValue" size="40"/>
        </td></tr>
    </k:notEmpty>
    <tr><td colspan="2">
        <k:notEqual name="hardwareSearchTemplate" property="hideSearchButton" value="true">
            <button type="submit" name="submitBtn"><k:message key="form.button.search"/></button>
            &nbsp;&nbsp;${hardwareSearchTemplate.clearSearchLink}
        </k:notEqual>
    </td></tr>
</table>
</form>
