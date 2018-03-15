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

<form action="${formAction}" method="post" id="attrGroupAddForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
    <html:hidden name="form" property="objectTypeId"/>
    <table class="standardForm">
        <tr>
            <th><k:message key="common.requiredFieldIndicator.true"/><k:message key="admin.attribute.attribute_group_name"/>:</th>
            <td><input type="text" name="attrGroupName" value="<k:write value="${form.attrGroupName}"/>" size="40" autofocus></td>
        </tr>
        <tr><td>&nbsp;</td>
            <td>
                <button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
                ${formCancelLink}
            </td>
        </tr>
    </table>
</form>