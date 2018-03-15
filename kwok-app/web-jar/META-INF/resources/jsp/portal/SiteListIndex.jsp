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

<h2 class="noLine">
    <k:message key="portal.siteList.header"/>
    &nbsp;<span class="command">[${toggleFrameLink}]</span>
</h2>
<p>
<div id="div1" class="displayBlock">
<%-- We're doing a few loops here, from a list of categories to a list of sites. --%>
<k:notEmpty name="dataList">
<k:foreach id="categories" name="dataList">
    <k:foreach id="category" name="categories" property="key">
        <h3><k:write value="${category.value}"/></h3>
            &nbsp;&nbsp;
            <k:foreach id="site" name="categories" property="value">
                ${site}&nbsp;&nbsp;
            </k:foreach><p>
    </k:foreach>
</k:foreach>
</k:notEmpty>
<%-- Show some message when there is no data --%>
<k:isEmpty name="dataList">
    <table class="standard">
        <jsp:include page="/jsp/common/template/TableEmpty.jsp"/>
    </table>
</k:isEmpty>
</div>

<div id="iframeDiv" style="display:none">
    <div class="solidLine">&nbsp;</div><iframe src="" height="800" width="100%" id="iframe" frameborder="0" class="portalIframe"></iframe>
</div>
