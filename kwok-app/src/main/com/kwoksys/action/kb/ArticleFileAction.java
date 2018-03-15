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

import java.io.FileNotFoundException;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.files.FileAddTemplate;
import com.kwoksys.action.files.FileDeleteTemplate;
import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.kb.dto.ArticleFile;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.CkeditorHelper;

/**
 * Action class for managing article files.
 */
public class ArticleFileAction extends Action2 {

    public String add() throws Exception {
        Integer articleId = requestContext.getParameter("articleId");

        KbService kbService = ServiceProvider.getKbService(requestContext);
        Article article = kbService.getArticle(articleId);

        Category category = kbService.getCategory(article.getCategoryId());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        KbUtils.generatePath(header, requestContext, category);

        //
        // Template: ArticleSpecTemplate
        //
        standardTemplate.addTemplate(new ArticleSpecTemplate(article));
        
        //
        // Template: FileAddTemplate
        //
        FileUploadForm actionForm = getBaseForm(FileUploadForm.class);

        FileAddTemplate fileAdd = standardTemplate.addTemplate(new FileAddTemplate(actionForm));
        fileAdd.setFileName(requestContext.getParameterString("fileName0"));
        fileAdd.setFormAction(AppPaths.KB_ARTICLE_FILE_ADD_2 + "?articleId=" + articleId);
        fileAdd.setFormCancelAction(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + articleId);
        fileAdd.getErrorsTemplate().setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String add2() throws Exception {
        boolean ckeditorUpload = !requestContext.getParameterString(CkeditorHelper.CKEDITOR).isEmpty();
        
        FileUploadForm actionForm = new FileUploadForm();
        actionForm.setFormUploadFieldName(ckeditorUpload ? "upload" : "file0");
        actionForm = saveActionForm(actionForm);

        Integer articleId = requestContext.getParameter("articleId");

        KbService kbService = ServiceProvider.getKbService(requestContext);

        Article article = kbService.getArticle(articleId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        File file = new ArticleFile(articleId);

        ActionMessages errors = fileService.addFile(file, actionForm);
        
        if (ckeditorUpload) {
            requestContext.getRequest().setAttribute("fileDownloadPath", AppPaths.KB_ARTICLE_FILE_DOWNLOAD_RELATIVE 
                    + "?articleId=" + articleId + "&fileId=" + file.getId());
            return SUCCESS;
        } else {
            if (!errors.isEmpty()) {
                saveActionErrors(errors);
                return redirect(AppPaths.KB_ARTICLE_FILE_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&articleId=" + articleId);
            } else {
                return redirect(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + article.getId());
            }
        }
    }

    public String delete() throws Exception {
        Integer articleId = requestContext.getParameter("articleId");

        KbService kbService = ServiceProvider.getKbService(requestContext);
        Article article = kbService.getArticle(articleId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = kbService.getArticleFile(articleId, fileId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        Category category = kbService.getCategory(article.getCategoryId());
        KbUtils.generatePath(header, requestContext, category);

        //
        // Template: ArticleSpecTemplate
        //
        standardTemplate.addTemplate(new ArticleSpecTemplate(article));

        //
        // Template: FileDeleteTemplate
        //
        FileDeleteTemplate fileDelete = standardTemplate.addTemplate(new FileDeleteTemplate());
        fileDelete.setFile(file);
        fileDelete.setFormAction(AppPaths.KB_ARTICLE_FILE_DELETE_2 + "?articleId=" + articleId + "&fileId=" + file.getId());
        fileDelete.setFormCancelAction(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + articleId);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String delete2() throws Exception {
        Integer articleId = requestContext.getParameter("articleId");

        KbService kbService = ServiceProvider.getKbService(requestContext);
        kbService.getArticle(articleId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = kbService.getArticleFile(articleId, fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete the file
        fileService.deleteFile(file);

        return ajaxUpdateView(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + articleId);
    }
    
    public String download() throws Exception {
        ResponseContext responseContext = new ResponseContext(response);

        try {
            // Call the service
            KbService kbService = ServiceProvider.getKbService(requestContext);

            // Get request parameters
            Integer articleId = requestContext.getParameter("articleId");

            // Check whether the object exists
            kbService.getArticle(articleId);

            Integer fileId = requestContext.getParameter("fileId");
            File file = kbService.getArticleFile(articleId, fileId);

            FileService fileService = ServiceProvider.getFileService(requestContext);
            fileService.download(responseContext, file);

        } catch (ObjectNotFoundException e) {
            throw new FileNotFoundException();
        }
        return null;
    }
}