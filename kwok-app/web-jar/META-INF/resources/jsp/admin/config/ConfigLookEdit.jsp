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

<form action="${formAction}" method="post" onsubmit="return App.disableButtonSubmit(this.submitBtn)">
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="cmd" value="${cmd}"/>
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th><k:message key="admin.config.theme"/>:</th>
        <td><html:select name="form" property="theme">
            <html:options collection="themeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.stylesheet"/>:</th>
        <td>&lt;style type=&quot;text/css&quot;&gt;
            <br><html:textarea name="form" property="stylesheet" cols="80" rows="14"/>
            <br>&lt;/style&gt;</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.homeCustomDescription"/>:</th>
        <td>
            <textarea name="homeCustomDescription" id="homeCustomDescription" cols="80" rows="14">${homepageContent}</textarea>
            <script type="text/javascript">
                CKEDITOR.replace('homeCustomDescription', {
                    language: '${language}',
                    filebrowserLinkBrowseUrl: '',
                    filebrowserImageBrowseUrl: '',
                    filebrowserFlashBrowseUrl: ''
                });
            </script>
        </td>
    </tr>
    <tr>
        <td style="width:20%">&nbsp;</td>
        <td><button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
        ${formCancelLink}
        </td>
    </tr>
</table>
</form>
