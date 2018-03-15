<%-- * Copyright 2016 Kwoksys
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
 * limitations under the License.--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>
<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="postEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<html:hidden name="form" property="postId"/>
<table class="standardForm">
    <tr>
        <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.post_name"/>:</th>
        <td><input type="text" name="postTitle" value="<k:write value="${form.postTitle}"/>" onkeyup="App.refreshPostPreview(this, 'postTitleDiv');" size="60" autofocus></td>
    </tr>
    <tr>
        <th><k:message key="common.column.post_description"/>:</th>
        <td><html:textarea name="form" property="postBody" rows="20" cols="120" onkeyup="App.refreshPostPreview(this, 'postBodyDiv');"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.category_id"/>:</th>
        <td><html:select name="form" property="categoryId">
            <html:options collection="postCategoryIdOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.column.post_allow_comment"/>:</th>
        <td><html:select name="form" property="postAllowComment">
            <html:options collection="postAllowCommentOptions" property="value" labelProperty="label"/>
        </html:select></td>
    </tr>
    <tr>
        <td>&nbsp;</td>        <td><button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>        ${formCancelLink}
        </td>
    </tr>
</table>
</form>
