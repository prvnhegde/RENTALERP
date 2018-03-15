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

<div id="tabs">
<k:foreach id="link" name="TabsTemplate" property="tabList" type="com.kwoksys.framework.ui.Link">
    <k:isEmpty name="link" property="path">
        <div class="active">${link.string}</div>
    </k:isEmpty>
    <k:notEmpty name="link" property="path">
        ${link.string}
    </k:notEmpty>
</k:foreach>
</div>
