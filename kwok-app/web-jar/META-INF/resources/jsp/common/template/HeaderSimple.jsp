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

<k:notEmpty name="HeaderTemplate">
    <k:define id="headerTemplate" name="HeaderTemplate" type="com.kwoksys.action.common.template.HeaderTemplate"/>
</k:notEmpty>

<k:notEmpty name="HeaderSimpleTemplate">
    <k:define id="headerTemplate" name="HeaderSimpleTemplate" type="com.kwoksys.action.common.template.HeaderSimpleTemplate"/>
</k:notEmpty>

<!DOCTYPE html>
<html>
<head>
    <meta HTTP-EQUIV="Content-Type" content="text/html; charset=UTF-8">
    <title><k:write value="${headerTemplate.pageTitleText}"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="${image.favicon}">
    <link rel="stylesheet" href="${path.defaultStyle}" type="text/css">
    <link rel="stylesheet" href="${headerTemplate.themeStylePath}" type="text/css">
    <link rel="stylesheet" href="${path.materialDesignCss}" type="text/css">

    <style type="text/css">
    body {
        font-size:${headerTemplate.fontSize}px;
    }
    <%-- User-defined stylesheet --%>
    ${headerTemplate.customStylesheet}
    </style>

    <script type="text/javascript">
        window.focus();
        var APP_PATH = '${path.cookieRoot}';
        var SERVER_CONN_ERROR_HEADER = '<k:message key="common.errorPages.serverConnection.header"/>';
        var SERVER_CONN_ERROR_BODY = '<k:message key="common.errorPages.serverConnection.body"/>';
    </script>

    <script type="text/javascript" src="${path.kwokJs}"></script>
    <script type="text/javascript" src="${path.appJs}"></script>
    <script type="text/javascript" src="${path.ckeditor}"></script>
    <script type="text/javascript" src="${path.chartJs}"></script>

    <%-- jQuery --%>
    <%-- Used by: calendar widget, fadeout, tooltip --%>
    <script type="text/javascript" src="${path.jqueryUiBase}/external/jquery/jquery.js"></script>

    <%-- jQuery UI --%>
    <link rel="stylesheet" href="${path.jqueryUiBase}/jquery-ui.min.css">
    <script type="text/javascript" src="${path.jqueryUiBase}/jquery-ui.min.js"></script>
    <script type="text/javascript">
        var DATE_PICKER_OPTIONS = {
            showOn: "button",
            buttonImage: "${image.calendar}",
            buttonImageOnly: true,
            dateFormat: "${headerTemplate.jqueryDateFormat}"
        };
    </script>

    <%-- tooltipster --%> 
    <link rel="stylesheet" type="text/css" href="${path.tooltipsterBase}/css/tooltipster.bundle.min.css"/>
    <link rel="stylesheet" type="text/css" href="${path.tooltipsterBase}/css/plugins/tooltipster/sideTip/themes/tooltipster-sideTip-light.min.css"/>
    <script type="text/javascript" src="${path.tooltipsterBase}/js/tooltipster.bundle.min.js"></script>    
</head>

<body>

<k:notEmpty name="headerTemplate" property="themeTitleText">
    <div id="genericHeaderTitle" class="themeBg standardHeader">
        ${headerTemplate.themeTitleText}
    </div>
</k:notEmpty>
