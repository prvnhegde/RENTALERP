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

<!-- /commmon/template/Table.jsp -->

<k:define id="tableTemplate" name="TableTemplate${RootTemplate.prefix}" type="com.kwoksys.action.common.template.TableTemplate"/>

<k:equal name="tableTemplate" property="canRemoveItem" value="true">
    <k:define id="formRowIdName" name="tableTemplate" property="formRowIdName"/>

    <form action="${path.root}${tableTemplate.formRemoveItemAction}" id="tableForm${RootTemplate.prefix}" method="post">
        <k:foreach id="var" name="tableTemplate" property="formHiddenVariableMap">
            <html:hidden property="${var.key}" value="${var.value}"/>
        </k:foreach>
</k:equal>

<table class="${tableTemplate.style}">
<%-- Column headers --%>
<jsp:include page="/jsp/common/template/TableHeader.jsp"/>

<k:notEmpty name="tableTemplate" property="dataList">
    <k:foreach id="row" name="tableTemplate" property="dataList">
        <tr class="dataRow">
            <k:equal name="tableTemplate" property="canRemoveItem" value="true">
                <td>
                    <k:equal name="tableTemplate" property="formSelectMultipleRows" value="true">
                        <html:checkbox name="${tableTemplate.formName}" property="${formRowIdName}" value="${row.rowId}"/>
                    </k:equal>
                    <k:equal name="tableTemplate" property="formSelectMultipleRows" value="false">
                        <html:radio name="${tableTemplate.formName}" property="${formRowIdName}" value="${row.rowId}"/>
                    </k:equal>
                </td>
            </k:equal>
                
            <k:foreach id="column" name="row" property="columns">
                <td>${column}</td>
            </k:foreach>
        </tr>
    </k:foreach>

    <k:equal name="tableTemplate" property="canRemoveItem" value="true">
        <tr class="themeHeader">
            <td colspan="${tableTemplate.colSpan}">
                <k:foreach id="button" name="tableTemplate" property="formButtons">
                    <input type="button" value="<k:message key="${button.key}"/>" onclick="Js.Modal.newInstance().setBody('<k:message key="${button.value}"/>').render(this, function(formElem){App.submitFormUpdate(formElem);})">
                </k:foreach>
            </td>
        </tr>
    </k:equal>
</k:notEmpty>

<%-- Show some message when there is no data --%>
<k:isEmpty name="tableTemplate" property="dataList">
    <jsp:include page="/jsp/common/template/TableEmpty.jsp"/>
</k:isEmpty>
</table>

<k:equal name="tableTemplate" property="canRemoveItem" value="true">
    </form>
</k:equal>
