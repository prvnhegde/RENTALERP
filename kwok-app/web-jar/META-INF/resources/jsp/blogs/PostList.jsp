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

<p class="section"><k:message key="blogs.postList.rowCount" arg0="${recordsNavigationTemplate.rowCount}" arg1="${recordsNavigationTemplate.rowStart}" arg2="${recordsNavigationTemplate.rowEnd}"/>

<%-- Showing posts --%>
<k:notEmpty name="postList">
    <div align="right" class="blogPermalink solidLine">&nbsp;</div>
    <k:foreach id="post" name="postList">
        <h2 class="noLine">${post.titleLink}</h2>
        <p><div class="infoContent section"><k:write value="${post.postCreator}"/></div>
        <p class="section">${post.postBody}
        <div align="right" class="blogPermalink solidLine">&raquo;
            ${post.postCategoryPath} |
            ${post.postCommentPath} |
            ${post.permaLink}</div>
    </k:foreach>
</k:notEmpty>

<%-- Showing Blogs navigation. --%>
<div align="center">
<jsp:include page="/jsp/common/template/RecordsNavigationWidget.jsp"/>
</div>
