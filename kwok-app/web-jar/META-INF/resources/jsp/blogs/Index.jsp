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

<ul class="section">
<k:foreach id="link" name="linkList">
    <li>${link}</li>
</k:foreach>
</ul>
<p>

<%-- Blog post search --%>
<h3><k:message key="portal.index.blogPostSearch"/></h3>

<form action="${postListPath}" method="post" id="blogSearchForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<html:hidden name="PostSearchForm" property="cmd" value="search"/>
<table class="standard section">
    <tr>
        <th><k:message key="common.column.post_name"/>:</th>
        <td width="100%"><html:text name="PostSearchForm" property="postTitle" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.post_description"/>:</th>
        <td width="100%"><html:text name="PostSearchForm" property="postDescription" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.category_id"/>: </th>
        <td><html:select name="PostSearchForm" property="categoryId">
            <html:options collection="postCategoryIdLabel" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.column.post_allow_comment"/>:</th>
        <td><html:select name="PostSearchForm" property="postAllowComment">
            <html:options collection="postAllowCommentOptions" property="value" labelProperty="label"/>
        </html:select></td>
    </tr>
    <tr>
        <td colspan="2">
            <button type="submit" name="submitBtn"><k:message key="form.button.search"/></button>
        </td>
    </tr>
</table>
</form>

<%-- Post categories --%>
<p>
<h3><k:message key="portal.index.blogPostCategory"/></h3>
<div class="section">
<k:foreach id="category" name="postCategoryLinks">
    ${category.link} (<k:write value="${category.postCount}"/>)&nbsp;
</k:foreach>
</div>
