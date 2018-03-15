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
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.kb.KbSearch;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dao.KbQueries;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.*;

/**
 * Action class for showing KB Articles.
 */
public class ArticleListAction extends Action2 {

    public String execute() throws Exception {
        ArticleSearchForm actionForm = getBaseForm(ArticleSearchForm.class);

        AccessUser user = requestContext.getUser();

        KbService kbService = ServiceProvider.getKbService(requestContext);
        Category category = kbService.getCategory(actionForm.getCategoryId());

        // Get search criteria map from session variable.
        KbSearch kbSearch = new KbSearch();
        kbSearch.prepareMap(actionForm);

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria(kbSearch);
        query.addSortColumn(KbQueries.getOrderByColumn(Article.ARTICLE_NAME));

        boolean hasArticleAccess = Access.hasPermission(user, AppPaths.KB_ARTICLE_DETAIL);
        List<Map> articles = new ArrayList<>();
        List<String> includeColumns = Arrays.asList(ConfigManager.app.getKbArticleColumns());

        for (Article article : kbService.getArticles(query)) {
            Map<String, String> map = new HashMap<>();

            Link link = new Link(requestContext);
            link.setTitle(article.getName());

            if (hasArticleAccess) {
                // Convert into a link if user has permission
                link.setAjaxPath(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + article.getId());
            }
            map.put("articleName", link.getString());
            if (includeColumns.contains(Article.VIEW_COUNT)) {
                map.put("viewCount", String.valueOf(article.getViewCount()));
            }
            articles.add(map);
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
        header.setTitleKey("kb.articleList.header");
        KbUtils.generatePath(header, requestContext, category);

        // Add Article
        if (Access.hasPermission(user, AppPaths.KB_ARTICLE_ADD)) {
            header.addHeaderCmds(new Link(requestContext).setAjaxPath(AppPaths.KB_ARTICLE_ADD + "?categoryId="
                    + actionForm.getCategoryId()).setTitleKey("kb.cmd.articleAdd"));
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
