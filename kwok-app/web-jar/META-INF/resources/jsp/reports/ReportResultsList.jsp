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

<jsp:include page="/jsp/common/template/HeaderSimple.jsp"/>

<style type="text/css">
th {
    text-align: left; vertical-align: top; white-space: nowrap;
}
</style>

<k:notEmpty name="reportTitle">
    <h1 class="noline black" style="text-align:center"><k:write value="${reportTitle}"/></h1><hr>
</k:notEmpty>

<k:foreach id="row" name="rows" >
    <table>
    <k:foreach indexId="index" id="column" name="row">
        <tr>
            <th>${columnHeaders[index]}:</th>
            <td><k:isPresent name="column"><k:write value="${column}"/></k:isPresent>&nbsp;</td>
        </tr>
    </k:foreach>
    </table>
    <hr>
</k:foreach>
