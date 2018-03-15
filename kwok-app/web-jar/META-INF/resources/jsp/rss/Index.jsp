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

<div>
    <h2><k:message key="portal.rssFeedList.header"/></h2>
    <iframe src="${listTitlesPath}" height="100%" width="28%" id="frameTitles" name="frameTitles" frameborder="0" style="min-height:400px"></iframe>
    <iframe src="" height="88%" width="70%" id="frameItems" name="frameItems" frameborder="0" style="min-height:400px; vertical-align:top"></iframe>
</div>
