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

<h2><k:message key="admin.customAttrAdd.header"/> - <k:message key="common.objectType.${objectTypeId}"/></h2>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="customAttributeAddForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<html:hidden name="form" property="objectTypeId"/>
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="admin.attribute.attribute_name"/>:</th>
        <td><input type="text" name="attrName" value="<k:write value="${form.attrName}"/>" size="48" maxlength="50" autofocus></td>
    </tr>
    <tr>
        <th><k:message key="admin.attribute.attribute_description"/>:</th>
        <td><html:text name="form" property="description" size="48" maxlength="255"/>
            <br><span class="formFieldDesc"><k:message key="admin.attribute.attribute_description.desc"/></span></td>
    </tr>
    <k:notEmpty name="attrGroupOptions">
        <tr>
            <th><k:message key="admin.attribute.attribute_group"/>:</th>
            <td><html:select name="form" property="attrGroupId">
                    <html:options collection="attrGroupOptions" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
    </k:notEmpty>
    <tr>
        <th><k:message key="admin.attribute.attribute_type"/>:</th>
        <td><html:select name="form" property="attrType" onchange="App.updateAttrOptions(this.value)">
                <html:options collection="attrTypeOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr id="attrOptions">
        <th><k:message key="admin.attribute.attribute_option"/>:</th>
        <td><html:textarea name="form" property="attrOption" cols="50" rows="6"/>
            <br><span class="formFieldDesc"><k:message key="admin.attribute.attribute_option.desc"/></span></td>
    </tr>
    <tr id="attrCurrencySymbol">
        <th><k:message key="admin.attribute.currency_symbol"/>:</th>
        <td><html:text name="form" property="currencySymbol" size="5"/></td>
    </tr>
    <k:notEmpty name="systemFields">
    <tr>
        <th><k:message key="admin.attributeAdd.applyToSystemFields"/>:</th>
        <td style="vertical-align:top">
            <table class="nested">
            <k:foreach id="systemField" name="systemFields">
                <tr>
                    <td><k:write value="${systemField.fieldName}"/></td>
                    <td>
                        <input type="checkbox" name="systemFields" value="${systemField.fieldId}" ${systemField.checked}>
                    </td>
                </tr>
            </k:foreach>
            </table>
        </td>
    </tr>
    </k:notEmpty>
    <tr>
        <td colspan="2"><h3><k:message key="common.form.advancedOptions"/></h3></td>
    </tr>
    <tr>
        <th><k:message key="admin.attribute.attribute_input_mask"/>:</th>
        <td><html:text name="form" property="inputMask" size="48" maxlength="50"/>
            <br><span class="formFieldDesc"><k:message key="admin.attribute.attribute_input_mask.desc" arg0="${path.siteDocInputMask}"/></span></td>
    </tr>
    <tr id="convertUrl">
        <th><k:message key="admin.attribute.attribute_convert_url"/>:</th>
        <td><k:foreach id="option" name="attrConvertUrlOptions">
                <html:radio name="form" property="attrConvertUrl" value="${option.value}"/>
                <k:write value="${option.label}"/>
            </k:foreach>
        </td>
    </tr>
    <tr id="createUrl">
        <th><k:message key="admin.attribute.attribute_url"/>:</th>
        <td><html:text name="form" property="attrUrl" size="80"/>
            <br><span class="formFieldDesc"><k:message key="common.example" arg0="${attrUrlExample}"/></span></td>
    </tr>
    <tr><td style="width:30%">&nbsp;</td>
        <td>
            <button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
            ${formCancelLink}
    </td></tr>
</table>
</form>
<p>
<script type="text/javascript">
    attrOptions(${attrTypeId});
</script>
