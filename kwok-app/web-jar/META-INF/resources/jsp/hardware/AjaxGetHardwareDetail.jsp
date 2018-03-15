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

<k:define id="hardwareSpecTemplate" name="HardwareSpecTemplate" type="com.kwoksys.action.hardware.HardwareSpecTemplate"/>

<table class="standard">
    <tr><td style="width:100%"><h2><k:write value="${hardwareSpecTemplate.headerText}"/>
    <span style="float:right">${closePopupLink}</span></h2>
    </td></tr>
</table>
<div id="hardwareDetailActive">
    <jsp:include page="/jsp/hardware/HardwareSpecTemplate.jsp"/>
</div>