<%--
 * Copyright 2017 Kwoksys
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

<k:define id="headerTemplate" name="HeaderTemplate" type="com.kwoksys.action.common.template.HeaderTemplate"/>
<k:define id="rootTemplate" name="StandardTemplate" type="com.kwoksys.action.common.template.RootTemplate"/>

<k:equal name="rootTemplate" property="ajax" value="false">
<jsp:include page="/jsp/common/template/HeaderSimple.jsp"/>

<div id="container">
    <noscript>
        <div class="noscript"><k:message key="common.warning.noscript"/></div>
    </noscript>

    <div id="headerLogo">
        ${headerTemplate.appLogoPath}
    </div>
    
    <div id="headerLinks">
	    ${headerTemplate.userMessage}
    </div>

    <div style="clear:both;"><%-- Avoid overlapping of header and module tabs. --%></div>
    
    <div id="moduleTabs" class="themeBg themeBorder">
        <k:notEmpty name="headerTemplate" property="moduleTabs">
            <ul>
            <k:foreach id="tab" name="headerTemplate" property="moduleTabs">
                <li>${tab.modulePath}</li>
            </k:foreach>
            </ul>
            <br style="clear:left"/>
        </k:notEmpty>
    </div>
    <script type="text/javascript">
        var MODULE_IDS = [${headerTemplate.moduleIds}];
    </script>
<div id="content">
</k:equal>

<k:equal name="rootTemplate" property="ajax" value="true">
    <script type="text/javascript">
        document.title = '<k:write value="${headerTemplate.pageTitleText}" encodeJavascript="true"/>';
    </script>
</k:equal>

<k:notEmpty name="headerTemplate" property="headerCmds">
    <div class="subCmds row1">
        <k:message key="core.template.header.cmd"/>&nbsp;
        <k:foreach id="cmd" name="headerTemplate" property="headerCmds">
            <k:isEmpty name="cmd" property="path">
                <span class="inactive">${cmd.string}</span>
            </k:isEmpty>

            <k:notEmpty name="cmd" property="path">${cmd.string}</k:notEmpty>
        </k:foreach>
    </div>
</k:notEmpty>
	
<div class="notificationBarWrapper" id="notificationBar" style="display:none">
<jsp:include page="/jsp/common/template/NotificationBar.jsp"/>
</div>
	
<%-- This needs to be a table because there's left TD for content and right TD for ads. --%>	
<table style="width:100%"><tr><td id="contentContainer">

<k:notEmpty name="headerTemplate" property="navCmds">
    <div class="nav section">${headerTemplate.navCmds}</div>
</k:notEmpty>

<k:notEmpty name="headerTemplate" property="titleText">
    <h2 class="${headerTemplate.titleClass}"><k:write value="${headerTemplate.titleText}"/></h2>
</k:notEmpty>

<k:notEmpty name="headerTemplate" property="sectionText">
    <div class="section"><p>${headerTemplate.sectionText}</div>
</k:notEmpty>

<script type="text/javascript">
    <%-- Ajax calls need these scripts too. --%>
    $(document).ready(
        function() {
            // Highlight selected module tab
            App.highlightModuleTab(MODULE_IDS, ${headerTemplate.moduleId});
            
            // tooltipster
            $('.tooltip').tooltipster({
                theme: ['tooltipster-light'],
                side: 'right'
                });

            ${headerTemplate.onloadJavascript}
            
            <k:notEmpty name="_pageExecutionTime">
                Js.Element.setHtml('footer_pageExecutionTime', '${_pageExecutionTime}');
            </k:notEmpty>
            
            // Autofocus doesn't seem to work well when content is dynamically generated using ajax.
            // Use javascript focus() to do that.
            var elems = document.getElementsByTagName('input');
            for (var i = 0; i < elems.length; i++) {
                var input = elems[i];
                if (input.getAttribute('autofocus') != null) {
                    input.focus();
                }
            }
        }
    );
</script>
