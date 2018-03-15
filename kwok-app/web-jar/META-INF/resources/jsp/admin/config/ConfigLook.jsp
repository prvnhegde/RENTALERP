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
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th style="width:20%"><k:message key="admin.config.theme"/>:</th>
        <td><k:write value="${theme}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.stylesheet"/>:</th>
        <td><pre><k:write value="${stylesheet}"/></pre></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.homeCustomDescription"/>:</th>
        <td>${homeCustomDescription}</td>
    </tr>
</table>
