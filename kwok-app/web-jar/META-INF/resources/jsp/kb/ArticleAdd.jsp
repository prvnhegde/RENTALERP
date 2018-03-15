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

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="articleAddForm" onsubmit="return App.disableButtonSubmit(this.submitBtn)">
<input type="hidden" name="_resubmit" value="true">
<table class="standard section">
    <tr>
        <th><b><k:message key="common.requiredFieldIndicator.true"/><k:message key="common.column.article_name"/>:</b></th>
        <td><input type="text" name="articleName" value="<k:write value="${form.articleName}"/>" size="60" autofocus></td>
    </tr>
    <tr>
        <th><b><k:message key="common.column.category_id"/>:</b></th>
        <td><html:select name="form" property="categoryId">
            <html:options collection="categoryIdOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <k:notEmpty name="articleSyntaxOptions">
    <tr>
        <th><b><k:message key="common.column.article_syntax_type"/>:</b></th>
        <td><html:select name="form" property="articleSyntax" onchange="changeAction(this, '${formThisAction}');">
            <html:options collection="articleSyntaxOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    </k:notEmpty>
    <tr>
        <td colspan="2">
            <html:textarea name="form" property="articleText" cols="120" rows="25"/>
            <k:notEqual name="isWikiSyntax" value="true">
                <script type="text/javascript">
                    CKEDITOR.replace( 'articleText', {
                    	language: '${language}',
                        filebrowserLinkBrowseUrl: '',
                    	filebrowserImageBrowseUrl: '',
                        filebrowserFlashBrowseUrl: ''
                    });
                </script>
            </k:notEqual>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <button type="submit" name="submitBtn"><k:message key="form.button.add"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
