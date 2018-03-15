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

<table class="listTable">
<tr class="themeHeader">
    <th class="rownum">#</th>
    <th><k:message key="${nameHeader}"/></th>
    <th><k:message key="import.heading.result"/></th>
    <th><k:message key="import.heading.messages"/></th>
</tr>
<k:foreach id="row" name="dataList">
    <k:define id="importItem" name="row" type="com.kwoksys.biz.admin.dto.ImportItem"/>

    <tr>
        <td>${importItem.rowNum}</td>
        <td>${importItem.title}</td>
        <td><k:message key="import.result.${importItem.action}"/></td>
        <td>
            <k:notEmpty name="importItem" property="errorMessages">
                <span class="error"><k:message key="import.validate.errors"/></span>:
                <ul>
                <k:foreach id="message" name="importItem" property="errorMessages">
                    <li><k:write value="${message}"/></li>
                </k:foreach>
                </ul>
            </k:notEmpty>
            <k:notEmpty name="importItem" property="warningMessages">
                <span class="warning"><k:message key="import.validate.warnings"/></span>:
                <ul>
                <k:foreach id="message" name="importItem" property="warningMessages">
                    <li><k:write value="${message}"/></li>
                </k:foreach>
                </ul>
            </k:notEmpty>
        </td>
    </tr>
</k:foreach>
</table>
<p>
${formBackLink}