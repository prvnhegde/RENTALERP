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

<k:define id="template" name="TabBodyTemplate" type="com.kwoksys.action.common.template.TabBodyTemplate"/>
<k:define id="table" name="template" property="table"/>

<table class="tabBody">
<tr class="themeHeader emptyHeader"><td colspan="2">&nbsp;</td></tr>

<k:foreach id="tr" name="table" property="tr">
    <tr>
        <k:foreach id="td" name="tr" property="td">
            <td<k:notEmpty name="td" property="style"> style="${td.style}"</k:notEmpty>>
                ${td.value}&nbsp;
            </td>
        </k:foreach>
    </tr>
</k:foreach>
</table>
