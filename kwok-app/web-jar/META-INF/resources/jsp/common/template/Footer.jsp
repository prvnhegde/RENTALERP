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

<k:define id="footerTemplate" name="FooterTemplate" type="com.kwoksys.action.common.template.FooterTemplate"/>
<k:define id="standardTemplate" name="StandardTemplate" type="com.kwoksys.action.common.template.StandardTemplate"/>

</td><%-- End contentContainer td --%>

<k:equal name="standardTemplate" property="adEnabled" value="true">
<td id="sponsorAd">
    <jsp:include page="/jsp/common/template/SponsorAd.jsp"/>
</td>
</k:equal>

</tr></table>

<k:equal name="standardTemplate" property="ajax" value="false">
</div><%-- End content div --%>

<div id="footer" class="themeBg">
    <div class="standardPadding">
	    <k:notEmpty name="_pageExecutionTime">
	        <k:message key="admin.config.timezone"/>: <k:write value="${footerTemplate.timezone}"/> |
	        <span id="footer_pageExecutionTime">&nbsp;</span><br>
	    </k:notEmpty>
	
	    ${footerTemplate.siteLink},
	
	    <k:message key="${footerTemplate.edition}"/>
	    <k:notEmpty name="footerTemplate" property="reportIssuePath">
	        | ${footerTemplate.reportIssuePath}
	    </k:notEmpty>
	    <br>${footerTemplate.copyrightNotice}
    </div>
</div>

</div><%-- End container div --%>
<%--<table>--%>
<%--<%--%>
    <%--Enumeration<String> attrEnum = request.getAttributeNames();--%>
    <%--while (attrEnum.hasMoreElements()) {--%>
        <%--String attrName = attrEnum.nextElement();--%>
        <%--out.println("<tr><td>" + attrName + "</td><td>" + request.getAttribute(attrName) + "</td></tr>");--%>
    <%--}--%>
<%--%>--%>
<%--</table>--%>

<%--<table>--%>
<%--<%--%>
    <%--Enumeration<String> attrEnum = request.getSession().getAttributeNames();--%>
    <%--while (attrEnum.hasMoreElements()) {--%>
        <%--String attrName = attrEnum.nextElement();--%>
        <%--out.println("<tr><td>" + attrName + "</td><td>" + request.getSession().getAttribute(attrName) + "</td></tr>");--%>
    <%--}--%>
<%--%>--%>
<%--</table>--%>

<%-- Modal --%>
<div id="modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <span id="modal-close" class="modal-close">&times;</span>
        </div>
        <div id="modal-body" class="modal-body"></div>
        <div class="modal-footer">
            <button type="button" id="modal-btn-submit" class="modal-btn"><k:message key="common.boolean.yes_no.true"/></button>
            <button type="button" id="modal-btn-close" class="modal-btn modal-btn-close"><k:message key="form.button.cancel"/></button>
        </div>
    </div>
</div>

</body>
</html>
</k:equal>