<%--
 * Copyright 2017 Kwoksys
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

<k:define id="objectDeleteTemplate" name="ObjectDeleteTemplate" type="com.kwoksys.action.common.template.ObjectDeleteTemplate"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<div class="tabBody">
    <k:isEmpty name="objectDeleteTemplate" property="titleText">
        <div class="themeHeader emptyHeader">&nbsp;</div>
    </k:isEmpty>
    <k:notEmpty name="objectDeleteTemplate" property="titleText">
        <div class="themeHeader">
            <b>${objectDeleteTemplate.titleText}</b>
        </div>
    </k:notEmpty>
    <form action="${objectDeleteTemplate.formAction}" method="post" id="objectDeleteForm" onsubmit="${objectDeleteTemplate.onsubmit}">
        <k:foreach id="var" name="objectDeleteTemplate" property="formHiddenVariableMap">
            <html:hidden property="${var.key}" value="${var.value}"/>
        </k:foreach>
        <table>
            <tr>
                <td><img src="${image.deleteIcon}" class="standard" alt=""> <k:message key="${objectDeleteTemplate.confirmationMsgKey}"/><p>
                    <button type="submit" name="deleteBtn"><k:message key="${objectDeleteTemplate.submitButtonKey}"/></button>
                    ${objectDeleteTemplate.formCancelLink}
                </td>
            </tr>
        </table>
    </form>
</div>
<p>