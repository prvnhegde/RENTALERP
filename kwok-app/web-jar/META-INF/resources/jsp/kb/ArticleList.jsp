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

<%-- Show articles --%>
<k:notEmpty name="articles">
    <ol>
    <k:foreach id="article" name="articles">
        <li>${article.articleName}
            <k:notEmpty name="article" property="viewCount">
                <span class="infoContent"> (<i><k:message key="kb.colName.view_count.description" arg0="${article.viewCount}"/></i>)</span>
            </k:notEmpty>
    </k:foreach>
    </ol>
</k:notEmpty>
<k:isEmpty name="articles">
    <p class="section"><k:message key="kb.articleList.noArticles"/>
</k:isEmpty>
