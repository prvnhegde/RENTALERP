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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.kb.KbSearch;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dao.KbQueries;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing KB Articles.
 */
public class ArticleSearchAction extends Action2 {

    public String execute() throws Exception {
        ArticleSearchForm actionForm = getSessionBaseForm(ArticleSearchForm.class);

        int rowStart = requestContext.getParameter("rowStart", 0);
        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getKbArticleNumberOfSearchResults());
        
        KbService kbService = ServiceProvider.getKbService(requestContext);

        // Get search criteria map from session variable.
        KbSearch kbSearch = new KbSearch();

        // NOTE: for now, we expect categoryId
        kbSearch.prepareMap(actionForm);

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria(kbSearch);
        query.addSortColumn(KbQueries.getOrderByColumn(Article.ARTICLE_NAME));
        query.setLimit(rowLimit, rowStart);

        List<Map> articles = new ArrayList<>();

        int rowCount = kbService.getArticlesCount(query);

        if (rowCount != 0) {
            for (Article article : kbService.getArticles(query)) {
                Map<String, String> map = new HashMap<>();
                
                String articleText = KbUtils.truncateBodyText(HtmlUtils.removeHtmlTags(article.getContent())); 
                if (article.getSyntaxType().equals(AttributeFieldIds.KB_ARTICLE_SYNTAX_MEDIAWIKI)) {
                    articleText = HtmlUtils.encode(articleText);
                }
                map.put("articleText", articleText);
                
                map.put("articleCreator", AdminUtils.getSystemUsername(requestContext, article.getCreator()));
                map.put("articleCreationDate", article.getCreationDate());
                map.put("articleDetailsLink", new Link(requestContext).setAjaxPath(AppPaths.KB_ARTICLE_DETAIL
                        + "?articleId=" + article.getId()).setStyleClass("h2").setTitle(article.getName()).getString());
                articles.add(map);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("articles", articles);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("kb.articleSearchResults.header");
        header.setTitleClassNoLine();
        
        //
        // Template: RecordsNavigationTemplate
        //
        RecordsNavigationTemplate nav = standardTemplate.addTemplate(new RecordsNavigationTemplate());
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setPath(AppPaths.KB_ARTICLE_SEARCH + "?rowStart=");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}