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

<table align="center" cellpadding="2" width="80%">
<tr>
    <td>
        <h2><k:write value="${postTitle}"/></h2>
        <p><div class="infoContent"><k:write value="${postCreator}"/></div>
        <p>${postBody}
        <p>&nbsp;<a name="comment"></a><div class="blogPostCommentHeader"><b><k:write value="${postCommentCount}"/></b></div><p>

<%-- Comments for this post --%>
<k:notEmpty name="postCommentList">
    <k:foreach id="row" name="postCommentList">
        <div class="blogPostCommentBody">${row.commentCreator}</div>
        <br>${row.commentDescription}<br><br>
    </k:foreach>
</k:notEmpty>

<%-- Allow posting comment if the user has permission --%>
<k:notEmpty name="postCommentPath">
    <form action="${postCommentPath}" method="post" onsubmit="return App.disableButtonSubmit(this.submitBtn)">
    <html:hidden name="form" property="postId"/>
        <jsp:include page="/jsp/common/template/ActionError.jsp"/>

        <div class="blogPostCommentHeader"><b><k:message key="blogs.postDetail.postComment"/></b></div>
        <html:textarea name="form" property="postComment" rows="14" cols="68"/>
        <br>
        <button type="submit" name="submitBtn"><k:message key="blogs.postDetail.postCommentButton"/></button>
    </form>
</k:notEmpty>

<%-- Show this if the post does not allow comment. --%>
<k:equal name="postAllowComment" value="false">
    <div class="blogPostCommentHeader"><b><k:message key="blogs.postDetail.postNotAllowComment"/></b></div>
</k:equal>
</td></tr></table>
