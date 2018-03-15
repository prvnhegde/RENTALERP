/*
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
 */
package com.kwoksys.action.kb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kwoksys.action.common.template.HeaderSimpleTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.CkeditorHelper;

/**
 * Action class for showing Article detail.
 */
public class ArticleDetailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Get request parameters
        Integer articleId = requestContext.getParameter("articleId");
        String articleWikiNamespace = requestContext.getParameterString("title");

        KbService kbService = ServiceProvider.getKbService(requestContext);
        FileService fileService = ServiceProvider.getFileService(requestContext);
        
        Article article = (articleId != 0) ? kbService.getArticle(articleId) : kbService.getArticle(articleWikiNamespace);

        // Update view count
        kbService.updateArticleViewCount(article);
        Category category = kbService.getCategory(article.getCategoryId());

        // Article files
        QueryCriteria query = new QueryCriteria();
        query.addSortColumn(FileQueries.getOrderByColumn(File.NAME));

        List<File> files = kbService.getArticleFiles(query, article.getId());

        boolean ckeditorBrowse = !requestContext.getParameterString(CkeditorHelper.CKEDITOR).isEmpty();
        
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        boolean canDownloadFile = Access.hasPermission(user, AppPaths.KB_ARTICLE_FILE_DOWNLOAD);

        if (ckeditorBrowse) {
            //
            // Template: HeaderSimpleTemplate
            //
            HeaderSimpleTemplate header = standardTemplate.addTemplate(new HeaderSimpleTemplate());
            header.setTitleText(Localizer.getText(requestContext, "files.fileAttachmentTab"));
            header.setThemeTitleText(Localizer.getText(requestContext, "files.fileAttachmentTab"));

            String ckeditorFuncNum = requestContext.getParameterString(CkeditorHelper.CKEDITOR_FUNC_NUM);
            request.setAttribute("ckeditorFuncNum", ckeditorFuncNum);
            
            List<Map> formattedFiles = new ArrayList<>();
            for (File file : files) {
                Map<String, Object> map = new HashMap<>();

                map.put("file", file);
                map.put("fileName", Links.getFileIconLink(requestContext, canDownloadFile, file.getLogicalName(),
                        AppPaths.KB_ARTICLE_FILE_DOWNLOAD + "?articleId=" + articleId + "&fileId=" + file.getId()).getString());
                map.put("filesize", FileUtils.formatFileSize(requestContext, file.getSize()));
                map.put("filePath", AppPaths.KB_ARTICLE_FILE_DOWNLOAD_RELATIVE
                        + "?articleId=" + articleId + "&fileId=" + file.getId());
                formattedFiles.add(map);
            }
            request.setAttribute("files", formattedFiles);

            return standardTemplate.findTemplate("files");
        }
        
        boolean canDeleteFile = Access.hasPermission(user, AppPaths.KB_ARTICLE_FILE_DELETE);

        if (!files.isEmpty()) {
            List<Map> formattedFiles = new ArrayList<>();
            for (File file : files) {
                Map<String, Object> map = new HashMap<>();

                map.put("file", file);
                map.put("fileName", Links.getFileIconLink(requestContext, canDownloadFile, file.getLogicalName(),
                        AppPaths.KB_ARTICLE_FILE_DOWNLOAD + "?articleId=" + articleId + "&fileId=" + file.getId()).getString());
                map.put("filesize", FileUtils.formatFileSize(requestContext, file.getSize()));
                
                if (canDeleteFile) {
                    map.put("deleteFilePath", new Link(requestContext).setAjaxPath(AppPaths.KB_ARTICLE_FILE_DELETE
                            + "?articleId=" + articleId + "&fileId=" + file.getId()).setTitleKey("common.command.Delete"));
                }
                formattedFiles.add(map);
            }
            standardTemplate.setAttribute("files", formattedFiles);
        }

        standardTemplate.setAttribute("article", article);
        standardTemplate.setAttribute("articleText", KbUtils.formatContent(requestContext, article));
        standardTemplate.setAttribute("articleCreator", AdminUtils.getSystemUsername(requestContext, article.getCreator()));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        KbUtils.generatePath(header, requestContext, category);

        // Edit this Article
        if (Access.hasPermission(user, AppPaths.KB_ARTICLE_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.KB_ARTICLE_EDIT + "?articleId=" + article.getId());
            link.setTitleKey("kb.cmd.articleEdit");
            header.addHeaderCmds(link);
        }

        // Print this Article
        if (Access.hasPermission(user, AppPaths.KB_ARTICLE_PRINT)) {
            Link link = new Link(requestContext);
            link.setExportPath(AppPaths.KB_ARTICLE_PRINT + "?articleId=" + article.getId());
            link.setTitleKey("kb.cmd.articlePrint");
            link.setMaterialIcon(Image.getInstance().getPrintIcon());
            header.addHeaderCmds(link);
        }

        // Attach file
        if (Access.hasPermission(user, AppPaths.KB_ARTICLE_FILE_ADD)) {
            Link link = new Link(requestContext);
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getKbFileRepositoryLocation())) {
                link.setAjaxPath(AppPaths.KB_ARTICLE_FILE_ADD + "?articleId=" + articleId);
                link.setImgSrc(Image.getInstance().getFileAddIcon());
            } else {
                link.setImgAltKey("files.warning.invalidPath");
                link.setImgSrc(Image.getInstance().getSignWarning());
            }
            header.addHeaderCmds(link);
        }

        // Delete
        if (Access.hasPermission(user, AppPaths.KB_ARTICLE_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.KB_ARTICLE_DELETE + "?articleId=" + article.getId());
            link.setTitleKey("kb.cmd.articleDelete");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
