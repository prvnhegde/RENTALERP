<%--
 * Copyright 2015 Kwoksys
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

<k:notEmpty name="configList">
<div class="section" style="float:left; vertical-align:top">
    <k:foreach id="link" name="configList">
        <p>${link}
    </k:foreach>
</div>
</k:notEmpty>

<k:notEmpty name="configListB">
<div class="section" style="float:left; width:50%; vertical-align:top">
    <k:foreach id="link" name="configListB">
        <p>${link}
    </k:foreach>
</div>
</k:notEmpty>
