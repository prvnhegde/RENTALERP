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

<h3><k:message key="contracts.filter"/></h3>

<%-- Contract filter --%>
<ul class="section">
<k:foreach id="row" name="contractFilters">
    <li>${row}</li>
</k:foreach>
</ul>

<%-- Contracts summary --%>
<h3><k:message key="contracts.summary"/></h3>
<table class="standard section">
    <tr><td>
        <b><k:message key="contracts.summary.byExpiration"/></b>
        <p><table class="standard">
        <k:foreach id="row" name="contractsSummary">
            <tr><th><k:write value="${row.text}"/></th>
                <td>${row.path}</td></tr>
        </k:foreach>
        </table>
    </td></tr>
</table>

<%-- Contract search --%>
<h3><k:message key="contracts.search"/></h3>
<jsp:include page="/jsp/contracts/ContractSearchTemplate.jsp"/>
