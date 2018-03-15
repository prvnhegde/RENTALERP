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
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<k:define id="attribute" name="attribute" type="com.kwoksys.biz.admin.dto.Attribute"/>

<h2><k:message key="admin.attributeDetail.header"/>:
    <k:message key="common.objectType.${attribute.objectTypeId}"/> &raquo; <k:message key="common.column.${attribute.name}"/></h2>

<table class="standard">
    <k:equal name="attribute" property="requiredFieldEditable" value="true">
    <tr>
        <th><k:message key="common.column.attribute_is_required"/>?</th>
        <td><k:message key="${attributeIsRequired}"/></td>
    </tr>
    </k:equal>
    <k:equal name="attribute" property="defaultAttrFieldEditable" value="true">
    <tr>
        <th><k:message key="common.column.attribute_default_field"/>:</th>
        <td>${defaultAttrField.name}</td>
    </tr>
    </k:equal>
    <k:equal name="canAddAttrField" value="true">
    <tr>
        <th><k:message key="common.column.attribute_drop_down_list"/>:</th>
        <td>${attributeFieldAddPath}

        <table class="standard">
        <k:foreach id="row" name="attrFieldList">
        <tr>
            <th width="30%">${row.attrFieldName}</th>
            <th class="infoContent">${row.attrFieldDescription}&nbsp;</th>
            <td style="white-space:nowrap">
                ${row.attributeEditPath}
                
                <k:equal name="row" property="attrFieldDefault" value="true">
                    &nbsp;<k:message key="admin.attributeDetail.defaultField"/>
                </k:equal>
            </td>
        </tr>
        </k:foreach>
        <k:foreach id="row" name="attrFieldDisabledList">
            <tr>
                <th class="inactive" width="30%">${row.attrFieldName}</th>
                <th class="infoContent">${row.attrFieldDescription}&nbsp;</th>
                <td>
                    ${row.attributeEditPath}
                    
                    <k:equal name="row" property="attrFieldDefault" value="true">
                        &nbsp;<k:message key="admin.attributeDetail.defaultField"/>
                    </k:equal>
                </td>
            </tr>
        </k:foreach>
        </table>
    </td></tr>
    </k:equal>
</table>
