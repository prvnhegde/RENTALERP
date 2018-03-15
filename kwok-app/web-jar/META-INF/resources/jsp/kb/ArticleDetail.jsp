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

<k:define id="article" name="article" type="com.kwoksys.biz.kb.dto.Article"/>

<table class="standard">
<tr>
    <td style="width:80%">
        <h2><k:write value="${article.name}"/></h2>
        <div class="section">${articleText}</div>
        <p><br><h3><k:message key="files.fileAttachmentTab"/></h3>

        <div class="section">
        <k:isEmpty name="files">
            <k:message key="files.noAttachments"/>
        </k:isEmpty>
        <k:notEmpty name="files">
            <k:foreach id="file" name="files">
                <k:define id="fileObj" name="file" property="file" type="com.kwoksys.biz.files.dto.File"/>
                <p>${file.fileName}&nbsp;
                    <k:write value="${fileObj.title}"/>
                    (<k:write value="${file.filesize}"/>, <k:write value="${fileObj.creationDate}"/>)
                    <k:isPresent name="file" property="deleteFilePath">
                        [${file.deleteFilePath}]
                    </k:isPresent>
            </k:foreach>
        </k:notEmpty>
        </div>
    </td>
    <td style="width:20%">
        <div class="kbArticleInfo">
            <p><h3><k:message key="kb.articleDetail.subHeader"/></h3>
            <k:message key="common.column.article_id"/>: <k:write value="${article.id}"/>
            <br><k:message key="common.column.article_creator"/>: <k:write value="${articleCreator}"/>
            <br><k:message key="common.column.article_creation_date"/>: <k:write value="${article.creationDate}"/>
            <p><i>(<k:message key="kb.colName.view_count.description" arg0="${article.viewCount}"/>)</i>
        </div>
    </td>
</tr>
</table>
