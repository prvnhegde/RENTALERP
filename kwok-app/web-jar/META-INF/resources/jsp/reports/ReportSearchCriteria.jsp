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

<h2><k:message key="reports.workflow.reportCriteria.header"/></h2>

<div class="section">
<k:message key="reports.workflow.reportCriteria.description"/>
&nbsp;<button type="button" onclick="App.disableButton(this);App.updateViewHistory('${reportBackAction}')"><k:message key="reports.button.back"/></button>
&nbsp;<button type="button" onclick="App.submitFormUpdate(document.${formName}, {'disable': this})"><k:message key="reports.button.next"/></button>
<p>
 
<k:equal name="reportType" value="issue_report">
    <h4><k:message key="reports.workflow.type.issue_report"/></h4>
    <jsp:include page="/jsp/issues/IssueSearchTemplate.jsp"/>
</k:equal>
<k:equal name="reportType" value="hardware_report">
    <h4><k:message key="reports.workflow.type.hardware_report"/></h4>
    <jsp:include page="/jsp/hardware/HardwareSearchTemplate.jsp"/>
</k:equal>
<k:equal name="reportType" value="hardware_member_report">
    <h4><k:message key="reports.workflow.type.hardware_member_report"/></h4>
    <jsp:include page="/jsp/hardware/HardwareSearchTemplate.jsp"/>
</k:equal>
<k:equal name="reportType" value="hardware_license_report">
    <h4><k:message key="reports.workflow.type.hardware_license_report"/></h4>
    <jsp:include page="/jsp/hardware/HardwareSearchTemplate.jsp"/>
</k:equal>
<k:equal name="reportType" value="software_report">
    <h4><k:message key="reports.workflow.type.software_report"/></h4>
    <jsp:include page="/jsp/software/SoftwareSearchTemplate.jsp"/>
</k:equal>
<k:equal name="reportType" value="software_usage_report">
    <h4><k:message key="reports.workflow.type.software_usage_report"/></h4>
    <jsp:include page="/jsp/software/SoftwareSearchTemplate.jsp"/>
</k:equal>
<k:equal name="reportType" value="contract_report">
    <h4><k:message key="reports.workflow.type.contract_report"/></h4>
    <jsp:include page="/jsp/contracts/ContractSearchTemplate.jsp"/>
</k:equal>
</div>