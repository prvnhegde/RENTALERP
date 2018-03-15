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

<k:define id="siteSpecTemplate" name="SiteSpecTemplate" type="com.kwoksys.action.portal.SiteSpecTemplate"/>

<h2><k:message key="admin.portalSiteDetail.header"/></h2>

<jsp:include page="/jsp/common/template/DetailTable.jsp"/>

<%-- Iframe section --%>
<div id="iframeDiv" style="display:none; width:100%">
    <iframe src="" height="800" width="100%" id="iframe" frameborder="0" class="portalIframe"></iframe>
</div>