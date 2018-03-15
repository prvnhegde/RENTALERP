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

<k:define id="thisTemplate" name="HardwareListTemplate${RootTemplate.prefix}" type="com.kwoksys.action.hardware.HardwareListTemplate"/>

<k:equal name="thisTemplate" property="canRemoveHardware" value="true">
    <form action="${formRemoveHardwareAction}" method="post" id="hardwareListForm">
        <k:foreach id="var" name="thisTemplate" property="formHiddenVariableMap">
            <html:hidden property="${var.key}" value="${var.value}"/>
        </k:foreach>
</k:equal>

<table class="listTable stripedListTable">
    <k:notEmpty name="thisTemplate" property="listHeader">
        <tr class="header3">
            <td colspan="<k:write value="${thisTemplate.colspan}"/>">
                <b><k:message key="${thisTemplate.listHeader}"/></b></td>
        </tr>
    </k:notEmpty>
        
    <jsp:include page="/jsp/common/template/TableHeader.jsp"/>
    <k:notEmpty name="thisTemplate" property="formattedList">
        <k:foreach id="row" name="thisTemplate" property="formattedList">
            <tr class="dataRow">
            <k:equal name="thisTemplate" property="canRemoveHardware" value="true">
                <td><html:radio name="form" property="formHardwareId" value="${row.rowId}"/></td>
            </k:equal>
            <k:foreach id="column" property="columns" name="row">
                <td>${column}</td>
            </k:foreach>
            </tr>
        </k:foreach>
        <k:equal name="thisTemplate" property="canRemoveHardware" value="true">
        <tr class="themeHeader">
            <td colspan="<k:write value="${thisTemplate.colspan}"/>">
                <input type="button" value="<k:message key="form.button.remove"/>" onclick="Js.Modal.newInstance().setBody('<k:message key="common.form.confirmRemove"/>').render(this, function(formElem){App.submitFormUpdate(formElem);})">
            </td>
        </tr>
        </k:equal>
    </k:notEmpty>
    
    <%-- Show some message when there is no data --%>
    <k:isEmpty name="thisTemplate" property="formattedList">
        <jsp:include page="/jsp/common/template/TableEmpty.jsp"/>
    </k:isEmpty>
</table>

<k:equal name="thisTemplate" property="canRemoveHardware" value="true">
    </form>
</k:equal>
