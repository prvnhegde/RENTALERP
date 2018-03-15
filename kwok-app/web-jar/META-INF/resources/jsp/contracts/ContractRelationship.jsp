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

<jsp:include page="/jsp/contracts/ContractSpecTemplate.jsp"/>

<%-- Ajax script --%>
<div id="hardwareDetail">&nbsp;</div>
<script type="text/javascript">
hardwarePopupAjax.clearCache();
hardwarePopupAjax.popupDiv = 'hardwareDetail';
hardwarePopupAjax.url = '${ajaxHardwareDetailPath}';
</script>

<jsp:include page="/jsp/common/template/Tabs.jsp"/>

<%-- Contract hardware --%>
<jsp:setProperty name="RootTemplate" property="prefix" value="_hardware"/>
<jsp:include page="/jsp/hardware/HardwareListTemplate.jsp"/>

<%-- Contract software --%>
<p>
<jsp:setProperty name="RootTemplate" property="prefix" value="_software"/>
<jsp:include page="/jsp/common/template/Table.jsp"/>
