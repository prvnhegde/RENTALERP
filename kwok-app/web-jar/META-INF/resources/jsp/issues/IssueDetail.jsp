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

<k:define id="customFieldsTemplate" name="CustomFieldsTemplate" type="com.kwoksys.action.common.template.CustomFieldsTemplate"/>
<k:define id="customFields" name="customFieldsTemplate" property="customFields"/>

<jsp:include page="/jsp/issues/IssueSpecTemplate.jsp"/>

<jsp:include page="/jsp/common/template/CustomFieldsTableToggle.jsp"/>

<jsp:include page="/jsp/common/template/Tabs.jsp"/>

<table class="listTable stripedListTable">
    <tr class="themeHeader">
        <th class="rownum">#</th><th><k:message key="issueMgmt.issueDetail.historyModifierHeader"/></th>
        <th>${issueChangeCreationDateHeader}</th><th><k:message key="issueMgmt.issueDetail.historyCommentHeader"/></th>
    </tr>

    <k:notEmpty name="issueHistoryList">
        <k:foreach id="row" name="issueHistoryList" indexId="index">
            <tr class="dataRow">
                <td><k:write value="${row.rownum}"/></td>
                <td width="20%" colspan="2">
                    ${row.changeCreator}
                    <k:isPresent name="row" property="changeCreatorEmail">
                        <br>(<k:write value="${row.changeCreatorEmail}"/>)
                    </k:isPresent>
                    <br><k:write value="${row.changeCreationDate}"/>
                </td>
                <td width="80%">
                <k:isPresent name="row" property="changeComment">
                    ${row.changeComment}<p>
                </k:isPresent>
                <k:isPresent name="row" property="changeFile">
                    ${row.changeFile}<br>
                </k:isPresent>
                <k:isPresent name="row" property="changeSubject">
                    ${row.changeSubject}<br>
                </k:isPresent>
                <k:isPresent name="row" property="changeType">
                    ${row.changeType}<br>
                </k:isPresent>
                <k:isPresent name="row" property="changeStatus">
                    ${row.changeStatus}<br>
                </k:isPresent>
                <k:isPresent name="row" property="changePriority">
                    ${row.changePriority}<br>
                </k:isPresent>
                <k:isPresent name="row" property="changeResolution">
                    ${row.changeResolution}<br>
                </k:isPresent>
                <k:isPresent name="row" property="changeAssignee">
                    ${row.changeAssignee}<br>
                </k:isPresent>
            </td></tr>
        </k:foreach>
    </k:notEmpty>
    <%-- Show some message when there is no data --%>
    <k:isEmpty name="issueHistoryList">
        <jsp:include page="/jsp/common/template/TableEmpty.jsp"/>
    </k:isEmpty>
</table>
