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

<h2><k:message key="admin.attributeEdit.header"/> :
    <k:message key="common.objectType.${attribute.objectTypeId}"/> &raquo; <k:message key="common.column.${attribute.name}"/></h2>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="attributeEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<html:hidden name="form" property="attributeId"/>
<table class="standardForm">
    <tr><td>
        <table>
        <k:equal name="attribute" property="requiredFieldEditable" value="true">
        <tr>
            <th><k:message key="common.column.attribute_is_required"/>?</th>
            <td>             
                <html:select name="form" property="required">
                    <html:options collection="isRequiredOptions" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        </k:equal>
        <k:equal name="attribute" property="defaultAttrFieldEditable" value="true">
        <tr>
            <th><k:message key="common.column.attribute_default_field"/>:</th>
            <td>
                <html:select name="form" property="defaultAttrField">
                    <html:options collection="defaultAttrFieldOptions" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        </k:equal>
        <tr>
            <td>&nbsp;</td>
            <td>
                <button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
                ${formCancelLink}
            </td>
        </tr>
        </table>
    </td></tr>
</table>
</form>
