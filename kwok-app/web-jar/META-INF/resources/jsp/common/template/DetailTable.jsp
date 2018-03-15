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

<k:define id="template" name="DetailTableTemplate${RootTemplate.prefix}" type="com.kwoksys.action.common.template.DetailTableTemplate"/>
<k:define id="table" name="template" property="table"/>

<table class="<k:write value="${template.style}"/>">

<k:foreach id="tr" name="table">
    <tr>
        <k:foreach id="td" name="tr">
            <th style="width:15%">
                <k:isPresent name="td" property="headerKey"><k:message key="${td.headerKey}"/>:</k:isPresent>
                <k:isPresent name="td" property="headerText"><k:write value="${td.headerText}"/>:</k:isPresent>
            </th>
            <td<k:notEmpty name="template" property="width"> style="width:${template.width}"</k:notEmpty>>${td.value}&nbsp;</td>
        </k:foreach>
    </tr>
</k:foreach>
</table>

<p>