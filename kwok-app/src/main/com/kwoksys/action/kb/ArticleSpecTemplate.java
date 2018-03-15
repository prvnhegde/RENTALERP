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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.kb.dto.Article;

/**
 * Template class for Article spec.
 */
public class ArticleSpecTemplate extends BaseTemplate {

    private Article article;
    private String articleCreator;

    public ArticleSpecTemplate(Article article) {
        super(ArticleSpecTemplate.class);
        this.article = article;
    }

    public void init() {}

    public void applyTemplate() throws Exception {
        articleCreator = AdminUtils.getSystemUsername(requestContext, article.getCreator());
    }

    @Override
    public String getJspPath() {
        return "/jsp/kb/ArticleSpecTemplate.jsp";
    }

    public Article getArticle() {
        return article;
    }

    public String getArticleCreator() {
        return articleCreator;
    }
}