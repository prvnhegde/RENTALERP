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
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<jsp:include page="/jsp/hardware/HardwareSpecTemplate.jsp"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<jsp:include page="/jsp/common/template/Tabs.jsp"/>

<%-- Hardware Components --%>
<form action="${deletePath}" method="post" id="hardwareComponentsForm">
<input name="hardwareId" type="hidden" value="${hardwareId}">
<table class="listTable stripedListTable">
    <tr class="themeHeader">
        <k:equal name="canDeleteComponent" value="true">
            <th class="rownum">&nbsp;</th>
        </k:equal>
        <th align="left" width="30%"><k:message key="common.column.hardware_component_type"/></th>
        <th align="left" width="55%"><k:message key="common.column.hardware_component_description"/></th>
        <k:equal name="canEditComponent" value="true">
            <th align="left" width="15%"><k:message key="common.column.command"/></th>
        </k:equal>
    </tr>
    <%-- Show the data --%>
    <k:notEmpty name="components">
        <k:foreach id="row" name="components">
            <k:define id="component" name="row" property="component" type="com.kwoksys.biz.hardware.dto.HardwareComponent"/>
            <tr class="dataRow">
                <k:equal name="canDeleteComponent" value="true">
                    <td><input type="radio" name="compId" value="${component.id}"></td>
                </k:equal>
                <td>
                    <k:write value="${component.typeName}"/>
                    ${row.compPath}<div id="cf${component.id}" class="customFieldEmbedded" style="display:none;"></div>
                </td>
                <td><k:write value="${component.description}"/></td>
                <k:equal name="canEditComponent" value="true">
                    <td>${row.editLink}&nbsp;</td>
                </k:equal>
            </tr>
        </k:foreach>
        <k:equal name="canDeleteComponent" value="true">
            <tr class="themeHeader"><td colspan="${colspan}">
                <input type="button" value="<k:message key="itMgmt.hardwareComp.deleteButton"/>" onclick="Js.Modal.newInstance().setBody('<k:message key="common.form.confirmDelete"/>').render(this, function(formElem) {App.submitFormUpdate(formElem);})">
            </td></tr>
        </k:equal>
    </k:notEmpty>
    <%-- Show some message when there is no data --%>
    <k:isEmpty name="components">
        <jsp:include page="/jsp/common/template/TableEmpty.jsp"/>
    </k:isEmpty>
</table>
</form>
