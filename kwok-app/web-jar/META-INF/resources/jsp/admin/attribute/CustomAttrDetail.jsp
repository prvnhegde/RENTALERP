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
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<k:define id="attr" name="attr" type="com.kwoksys.biz.admin.dto.Attribute"/>

<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th style="width:30%"><k:message key="admin.attribute.attribute_name"/>:</th>
        <td><k:write value="${attr.name}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.attribute.attribute_description"/>:</th>
        <td><k:write value="${attr.description}"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.attribute_is_required"/>?</th>
        <td><k:message key="${attributeIsRequired}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.attribute.attribute_group"/>:</th>
        <td>
            <k:write value="${attrGroup.name}"/>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.attribute.attribute_type"/>:</th>
        <td><k:message key="admin.attribute.attribute_type.${attr.type}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.attribute.attribute_option"/>:</th>
        <td><k:write value="${attrOption}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.attribute.currency_symbol"/>:</th>
        <td><k:write value="${attr.typeCurrencySymbol}"/></td>
    </tr>
    <k:isPresent name="systemFields">
        <tr>
            <th><k:message key="admin.attributeAdd.applyToSystemFields"/>:</th>
            <td>
                <ul>
                <k:foreach id="attrLink" name="systemFields">
                    <li>${attrLink}
                </k:foreach>
                </ul>
            </td>
        </tr>
    </k:isPresent>
    <tr>
        <td colspan="2"><h3><k:message key="common.form.advancedOptions"/></h3></td>
    </tr>
    <tr>
        <th><k:message key="admin.attribute.attribute_input_mask"/>:</th>
        <td><k:write value="${attr.inputMask}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.attribute.attribute_convert_url"/>:</th>
        <td><k:message key="${attrConvertUrl}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.attribute.attribute_url"/>:</th>
        <td><k:write value="${attr.url}"/></td>
    </tr>
</table>
