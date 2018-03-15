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

<%-- KB article search --%>
<div class="section">
<form action="${articleSearchPath}" method="post" id="articleSearchForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
    <html:hidden name="ArticleSearchForm" property="cmd" value="search"/>
    <input type="hidden" name="_resubmit" value="true">
        <k:message key="common.column.article_text"/>:
        <html:text name="ArticleSearchForm" property="articleText" size="40"/>
        <button type="submit" name="submitBtn"><k:message key="form.button.search"/></button>
</form>
</div>

<%-- KB categories --%>
<h3><k:message key="kb.index.categoryList"/></h3>
<ul class="section">
<k:foreach id="category" name="categories">
    <li>${category}</li>
</k:foreach>
</ul>
