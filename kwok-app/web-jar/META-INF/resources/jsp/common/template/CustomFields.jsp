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
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<k:define id="customFieldsTemplate" name="CustomFieldsTemplate" type="com.kwoksys.action.common.template.CustomFieldsTemplate"/>
<k:define id="customFields" name="customFieldsTemplate" property="customFields"/>

<k:foreach id="attrMap" name="customFields">
    <div class="customField">
    <k:notEmpty name="attrMap" property="key">
        <h3>${attrMap.key}</h3>
    </k:notEmpty>
    <table class="${StandardTemplate.detailsTableStyle}">
        <k:foreach id="field" name="attrMap" property="value">
            <k:define id="attr" name="field" property="attr" type="com.kwoksys.biz.admin.dto.Attribute"/>
            <tr>
                <th style="width:15%"><k:write value="${attr.name}"/>:</th>
                <td>${field.value}${field.attrLink}</td>
            </tr>
        </k:foreach>
    </table>
    </div>
</k:foreach>
