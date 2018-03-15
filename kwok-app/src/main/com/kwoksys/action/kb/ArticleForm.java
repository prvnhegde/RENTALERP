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

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.framework.http.RequestContext;

/**
 * Action class for adding Article.
 */
public class ArticleForm extends BaseForm {

    private Integer articleId;
    private String articleName;
    private String articleText;
    private Integer articleSyntax;
    private Integer categoryId;

    @Override
    public void setRequest(RequestContext requestContext) {
        articleId = requestContext.getParameterInteger("articleId");
        articleName = requestContext.getParameterString("articleName");
        articleText = requestContext.getParameterString("articleText");
        articleSyntax = requestContext.getParameterInteger("articleSyntax");
        categoryId = requestContext.getParameterInteger("categoryId");
    }

    public void setArticle(Article article) {
        articleName = article.getName();
        articleText = article.getContent();
        articleSyntax = article.getSyntaxType();
        categoryId = article.getCategoryId();
    }

    //
    // Getters and setters
    //
    public Integer getArticleId() {
        return articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public String getArticleText() {
        return articleText;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public Integer getArticleSyntax() {
        return articleSyntax;
    }
}
