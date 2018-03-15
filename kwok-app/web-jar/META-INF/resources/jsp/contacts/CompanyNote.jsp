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

<jsp:include page="/jsp/contacts/CompanySpecTemplate.jsp"/>

<%-- Company Notes --%>
<jsp:include page="/jsp/common/template/Tabs.jsp"/>
<table class="listTable">
    <tr class="themeHeader">
        <th width="20%"><k:message key="common.column.company_note_creation_date"/></th>
        <th width="70%"><k:message key="common.column.company_note_description"/></th>
        <th><k:message key="common.column.company_note_type"/></th>
    </tr>
    <k:notEmpty name="notes">
        <k:foreach id="note" name="notes">
            <tr>
                <td><k:write value="${note.creationDate}"/>&nbsp;</td>
                <td><k:write value="${note.subject}"/>
                    <div>${note.description}</div>
                </td>
                <td><k:write value="${note.type}"/>&nbsp;</td>
            </tr>
        </k:foreach>
    </k:notEmpty>
    <%-- Show some message when there is no data --%>
    <k:isEmpty name="notes">
        <jsp:include page="/jsp/common/template/TableEmpty.jsp"/>
    </k:isEmpty>
</table>
