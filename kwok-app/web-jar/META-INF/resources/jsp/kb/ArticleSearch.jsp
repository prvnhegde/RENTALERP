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

<%-- Show articles --%>
<k:notEmpty name="articles">
    <div align="right" class="blogPermalink solidLine">&nbsp;</div>
    <k:foreach id="article" name="articles">
        <h2 class="noLine">${article.articleDetailsLink}</h2>
        <div class="infoContent section">
            <p><k:message key="common.column.article_creator"/>: <k:write value="${article.articleCreator}"/>
            <br><k:message key="common.column.article_creation_date"/>: <k:write value="${article.articleCreationDate}"/>
        </div>
        <p class="section"><k:message key="common.column.article_text"/>: ${article.articleText}

        <div align="right" class="blogPermalink solidLine">&nbsp;</div>
    </k:foreach>
</k:notEmpty>
<k:isEmpty name="articles">
    <k:message key="kb.articleSearchResults.noArticles"/>
</k:isEmpty>

<%-- Showing navigation. --%>
<div align="center">
<jsp:include page="/jsp/common/template/RecordsNavigationWidget.jsp"/>
</div>
