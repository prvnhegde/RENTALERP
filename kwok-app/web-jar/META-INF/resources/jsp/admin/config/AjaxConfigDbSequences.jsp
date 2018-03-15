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

<table class="listTable">
    <tr>
        <th><k:message key="admin.config.system.sequences.sequenceName"/></th>
        <th><k:message key="admin.config.system.sequences.sequenceLastValue"/></th>
        <th><k:message key="admin.config.system.sequences.columName"/></th>
        <th><k:message key="admin.config.system.sequences.columnMaxValue"/></th>
        <th class="rownum"><k:message key="admin.config.system.sequences.status"/></th>
    </tr>
<k:foreach id="sequence" name="sequences" type="com.kwoksys.biz.admin.dto.DbSequence">
<tr>
    <td>${sequence.name}</td>
    <td>${sequence.lastValue}</td>
    <td>${sequence.tableColumnName}</td>
    <td>${sequence.tableMaxValue}</td>
    <td>${sequence.statusIcon}</td>
</tr>
</k:foreach>
</table>
