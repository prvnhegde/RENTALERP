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

<k:define id="softwareSpecTemplate" name="SoftwareSpecTemplate" type="com.kwoksys.action.software.SoftwareSpecTemplate"/>

<h2><k:write value="${softwareSpecTemplate.headerText}"/></h2>

<jsp:include page="/jsp/common/template/DetailTable.jsp"/>

<k:notEmpty name="softwareSpecTemplate" property="linkedContracts">
    <h3><k:message key="hardware.detail.colName.linkedContracts"/></h3>
        <p class="section"><k:foreach id="contractName" name="softwareSpecTemplate" property="linkedContracts">
            ${contractName}&nbsp;&nbsp;
        </k:foreach>
</k:notEmpty>
<p>