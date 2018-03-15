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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<jsp:include page="/jsp/software/SoftwareSpecTemplate.jsp"/>

<%-- Software Bookmarks --%>
<jsp:include page="/jsp/common/template/Tabs.jsp"/>
<table class="listTable stripedListTable">
    <tr class="themeHeader">
        <k:equal name="canDeleteBookmark" value="true">
            <th class="rownum">&nbsp;</th>
        </k:equal>
        <th width="70%"><k:message key="common.column.bookmark_name"/></th>
        <k:equal name="canEditBookmark" value="true">
            <th width="30%"><k:message key="common.column.command"/></th>
        </k:equal>
    </tr>
    <k:notEmpty name="bookmarks">
    <form action="${deleteBookmarkPath}" method="post" id="softwareBookmarkForm">
        <html:hidden name="form" property="softwareId"/>
        <k:foreach id="row" name="bookmarks">
            <tr class="dataRow">
            <k:equal name="canDeleteBookmark" value="true">
                 <td>
                    <html:radio name="form" property="bookmarkId" value="${row.bookmarkId}"/>
                 </td>
            </k:equal>
            <td width="100%">${row.bookmarkPath}</td>
            <k:equal name="canEditBookmark" value="true">
                <td width="100%">${row.bookmarkEditPath}</td>
            </k:equal>
            </tr>
        </k:foreach>
        <k:equal name="canDeleteBookmark" value="true">
            <tr class="themeHeader">
                <td colspan="${bookmarkRowSpan}">
                    <input type="button" value="<k:message key="bookmarks.deleteBookmarkButton"/>" onclick="Js.Modal.newInstance().setBody('<k:message key="common.form.confirmDelete"/>').render(this, function(formElem) {App.submitFormUpdate(formElem);})">
                </td>
            </tr>
        </k:equal>
    </form>
    </k:notEmpty>
    <%-- Show some message when there is no data --%>
    <k:isEmpty name="bookmarks">
        <jsp:include page="/jsp/common/template/TableEmpty.jsp"/>
    </k:isEmpty>
</table>