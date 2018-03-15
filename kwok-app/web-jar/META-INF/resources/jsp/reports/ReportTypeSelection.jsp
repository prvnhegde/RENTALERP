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

<form action="${formAction}" method="post" name="ReportForm" id="ReportForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="_resubmit" value="true">
<table class="standard">
    <tr>
        <td><k:message key="reports.workflow.typeSelection.description"/>
            &nbsp;<button type="submit" name="submitBtn"><k:message key="reports.button.next"/></button></td>
    </tr>
    <tr>
        <td>
            <html:select name="ReportForm" property="reportType" onchange="App.checkReportTypeEnabled(this);">
                <html:options collection="reportTypeOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
</table>
</form>
