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

<h2><k:message key="admin.attributeEdit.header"/>: 
    <k:message key="common.objectType.${attribute.objectTypeId}"/> &raquo; <k:message key="common.column.${attribute.name}"/></h2>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="attFieldEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
    <html:hidden name="form" property="attributeId"/>
    <html:hidden name="form" property="attrFieldId"/>
    <table class="standardForm">
        <tr>
            <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="admin.attributeAdd.name"/>:</th>
            <td><input type="text" name="attributeFieldName" value="<k:write value="${form.attributeFieldName}"/>" size="40" autofocus></td>
        </tr>
        <tr>
            <th><k:message key="admin.attributeAdd.description"/>:</th>
            <td><html:textarea name="form" property="attributeFieldDescription" cols="40" rows="6"/></td>
        </tr>
        <tr>
            <th><k:message key="admin.attributeAdd.status"/>:</th>
            <td>             
                <html:select name="form" property="disabled">
                    <html:options collection="statusList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <k:notEmpty name="customAttrs">
        <tr>
            <th><k:message key="admin.customAttrList"/>:</th>
            <td>
                <table border="0">
                <k:foreach id="customAttrMap" name="customAttrs">
                    <k:define id="customAttr" name="customAttrMap" property="attr" type="com.kwoksys.biz.admin.dto.Attribute"/>
                    <tr>
                        <td><k:write value="${customAttr.name}"/></td>
                        <td>
                            <input type="checkbox" name="customAttrs" value="${customAttr.id}" ${customAttrMap.checked}>
                        </td>
                    </tr>
                </k:foreach>
                </table>
            </td>
        </tr>
        </k:notEmpty>
        <k:notEmpty name="iconList">
            <tr>
                <th><k:message key="admin.attributeAdd.icon"/>:</th>
                <td>
                    <html:radio name="form" property="iconId" value="0"/><k:message key="admin.attributeAdd.noIcon"/>
                    <k:foreach id="icon" name="iconList" type="com.kwoksys.biz.admin.dto.Icon">
                        <html:radio name="form" property="iconId" value="${icon.id}"/><img src="${icon.path}" width="16" height="16" alt="">&nbsp;&nbsp;
                    </k:foreach>
                </td>
            </tr>
        </k:notEmpty>
        <tr>
            <td>&nbsp;</td>
            <td>
                <button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
                ${formCancelLink}
            </td>
        </tr>
    </table>
</form>
