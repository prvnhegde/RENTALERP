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

<form action="${formAction}?cmd=filter" method="get">
<table class="standard">
<tr>
    <th>${searchResultText}&nbsp;</th>
    
    <td align="right">
    <k:message key="itMgmt.hardwareList.hardwareTypeFilter"/>:
        <html:select name="HardwareSearchForm" property="hardwareTypeFilter" onchange="App.changeSelectedOption(this);">
            <html:options collection="hardwareTypeOptions" property="value" labelProperty="label"/>
        </html:select>
    </td>
    <td style="width:20%; text-align:right; vertical-align:middle" nowrap>
        <jsp:include page="/jsp/common/template/RecordsNavigationWidget.jsp"/>
    </td>
</tr>
</table>
</form>

<%-- Some ajax script here. --%>
<div id="hardwareDetail">&nbsp;</div>
<script type="text/javascript">
hardwarePopupAjax.clearCache();
hardwarePopupAjax.popupDiv = 'hardwareDetail';
hardwarePopupAjax.url = '${ajaxHardwareDetailPath}';
</script>

<jsp:include page="/jsp/common/template/Table.jsp"/>
