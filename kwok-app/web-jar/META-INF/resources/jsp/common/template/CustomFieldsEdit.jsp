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

<k:define id="customFieldsTemplate" name="CustomFieldsTemplate" type="com.kwoksys.action.common.template.CustomFieldsTemplate"/>

<k:foreach id="attrMap" name="CustomFieldsTemplate" property="customFields">

<k:notEqual name="customFieldsTemplate" property="partialTable" value="true">
    <table class="standardForm2">
</k:notEqual>

    <tr><td colspan="2"><h3>${attrMap.key}</h3></td></tr>    
<k:foreach id="field" name="attrMap" property="value">
    <k:define id="attr" name="field" property="attr" type="com.kwoksys.biz.admin.dto.Attribute"/>
    <tr>
    <th>
        <k:equal name="attr" property="required" value="true">
            <k:message key="common.requiredFieldIndicator.true"/>
        </k:equal>
        <k:write value="${attr.name}"/>:</th>
    <td>
    <%-- Here is the logic to display different input depending on attribute type --%>
    <%-- Attribute Type: String --%>
    <k:equal name="attr" property="type" value="1">
        <html:text name="field" property="attrId${attr.id}" value="${field.value}" size="40"/>
    </k:equal>
    <%-- Attribute Type: Multi-line --%>
    <k:equal name="attr" property="type" value="2">
        <html:textarea name="field" property="attrId${attr.id}" value="${field.value}" cols="40" rows="8"/>
    </k:equal>
    <%-- Attribute Type: Selectbox --%>
    <k:equal name="attr" property="type" value="3">
        <html:select name="field" property="attrId${attr.id}">
            <k:foreach id="option" name="field" property="attrOptions">
                <html:option value="${option.value}">${option.label}</html:option>
            </k:foreach>
        </html:select>
    </k:equal>
    <%-- Attribute Type: Radio Button --%>
    <k:equal name="attr" property="type" value="4">
        <k:foreach id="option" name="field" property="attrOptions">
            <html:radio name="field" property="attrId${attr.id}" idName="option" value="value"/>
            ${option.label}&nbsp;
        </k:foreach>
    </k:equal>
    <%-- Attribute Type: Date --%>
    <k:equal name="attr" property="type" value="5">
        <input type="text" name="attrId${attr.id}" id="attrId${attr.id}" value="${field.value}" size="20"/>
            <span class="formFieldDesc"><k:message key="common.example" arg0="${customFieldsTemplate.datePattern}"/></span>
        <script type="text/javascript">
            App.generateDatepicker($('#attrId${attr.id}'), DATE_PICKER_OPTIONS);
        </script>
    </k:equal>
    <%-- Attribute Type: Currency --%>
    <k:equal name="attr" property="type" value="7">
        <k:write value="${attr.typeCurrencySymbol}"/><html:text name="field" property="attrId${attr.id}" value="${field.value}" size="10"/>
    </k:equal>
    <br><span class="formFieldDesc"><k:write value="${attr.description}"/></span>
    </td>
    </tr>
</k:foreach>
<k:notEqual name="customFieldsTemplate" property="partialTable" value="true">
    </table>
</k:notEqual>

</k:foreach>