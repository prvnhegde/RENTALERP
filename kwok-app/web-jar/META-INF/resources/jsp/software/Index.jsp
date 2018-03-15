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

<div class="section">
<ul>
<k:foreach id="link" name="linkList">
<li>${link}</li>
</k:foreach></ul>
</div>
<p>

<h3><k:message key="software.lookup"/></h3>
<div class="section">
<form action="${lookupAction}">
<k:message key="common.column.software_id"/>
&nbsp;<html:text name="SoftwareSearchForm" property="softwareId" size="10"/>
    <button type="submit" name="submitBtn" onclick="App.disableButtonSubmit(this)"><k:message key="form.button.go"/></button>
</form>
</div>
<p>

<%-- Software Search --%>
<h3><k:message key="itMgmt.index.filterSoftware"/></h3>
<jsp:include page="/jsp/software/SoftwareSearchTemplate.jsp"/>
