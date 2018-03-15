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
import java.util.List;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dao.KbQueries;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.CkeditorHelper;
import com.kwoksys.framework.util.StringUtils;

/**
 * Action class for editing KB Article.
 */
public class ArticleEditAction extends Action2 {

    public String edit() throws Exception {
        AccessUser user = requestContext.getUser();
        ArticleForm actionForm = getBaseForm(ArticleForm.class);

        FileService fileService = ServiceProvider.getFileService(requestContext);
        
        KbService kbService = ServiceProvider.getKbService(requestContext);
        Article article = kbService.getArticle(actionForm.getArticleId());

        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setArticle(article);
        }

        QueryCriteria query = new QueryCriteria();
        query.addSortColumn(KbQueries.getOrderByColumn(Category.CATEGORY_NAME));

        List<LabelValueBean> categoryIdOptions = new ArrayList<>();
        for (Category category : kbService.getCategories(query)) {
            categoryIdOptions.add(new LabelValueBean(category.getName(),
                    String.valueOf(category.getId())));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.KB_ARTICLE_EDIT_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.KB_ARTICLE_EDIT + "?articleId=" + article.getId());
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + article.getId()).getString());
        request.setAttribute("categoryIdOptions", categoryIdOptions);

        boolean isWikiType = KbUtils.isWikiType(actionForm.getArticleSyntax());
        String articleText = actionForm.getArticleText();
        if (!isWikiType) {
            articleText = StringUtils.encodeCkeditorJs(articleText);
            
            request.setAttribute("language", CkeditorHelper.getLocaleKey(requestContext.getLocale()));
            
            // For CKEditor "Browse Server" and "Upload" buttons.
            if (fileService.isDirectoryExist(ConfigManager.file.getKbFileRepositoryLocation())) {
                standardTemplate.setPathAttribute("fileBrowseUrl", AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + article.getId() + "&CKEditor=1");
            
                if (Access.hasPermission(user, AppPaths.KB_ARTICLE_FILE_ADD)) {
                    standardTemplate.setPathAttribute("fileUploadUrl", AppPaths.KB_ARTICLE_FILE_ADD_2 + "?articleId=" + article.getId() + "&CKEditor=1");
                }
            }
        }
        request.setAttribute("articleText", articleText);
        request.setAttribute("isWikiSyntax", isWikiType);
        request.setAttribute("articleSyntaxOptions", KbUtils.getArticleSyntaxOptions(requestContext, article));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("kb.articleEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {

        ArticleForm actionForm = saveActionForm(new ArticleForm());

        KbService kbService = ServiceProvider.getKbService(requestContext);

        Article article = kbService.getArticle(actionForm.getArticleId());
        article.setName(actionForm.getArticleName());
        article.setContent(actionForm.getArticleText());
        article.setSyntaxType(actionForm.getArticleSyntax());
        article.setWikiNamespace(KbUtils.formatWikiNamespace(actionForm.getArticleSyntax(), actionForm.getArticleName()));
        article.setCategoryId(actionForm.getCategoryId());

        ActionMessages errors = kbService.updateArticle(article);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.KB_ARTICLE_EDIT + "?articleId=" + article.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + article.getId());
        }
    }
}
