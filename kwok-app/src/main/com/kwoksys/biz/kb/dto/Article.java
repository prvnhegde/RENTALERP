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
package com.kwoksys.biz.kb.dto;

import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.AttributeFieldIds;

/**
 * KB Article Object.
 */
public class Article extends BaseObject {

    public static final String ARTICLE_ID = "article_id";

    public static final String ARTICLE_NAME = "article_name";

    public static final String ARTICLE_SYNTAX = "article_syntax_type";

    public static final String ARTICLE_WIKI_NAMESPACE = "article_wiki_namespace";

    public static final String ARTICLE_OBJECT_COUNT = "article_object_count";

    public static final String VIEW_COUNT = "article_view_count";

    /** Corresponds to kb_article.article_id column. **/
    private Integer id;

    /** Corresponds to kb_article.article_name column. **/
    private String name;

    /** Corresponds to kb_article.article_text column. **/
    private String content;

    /** Corresponds to kb_article.category_id column. **/
    private Integer categoryId;

    /** Corresponds to kb_article.article_syntax_type column. **/
    private Integer syntaxType;

    /** Corresponds to kb_article.article_wiki_namespace column. **/
    private String wikiNamespace;

    /** Corresponds to kb_article.view_count column **/
    private long viewCount;

    private String categoryName;

    public Article() {
        syntaxType = AttributeFieldIds.KB_ARTICLE_SYNTAX_HTML;
        categoryId = 0;
    }

    //
    // Getter and Setter
    //
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public long getViewCount() {
        return viewCount;
    }
    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getSyntaxType() {
        return syntaxType;
    }

    public void setSyntaxType(Integer syntaxType) {
        this.syntaxType = syntaxType;
    }

    public String getWikiNamespace() {
        return wikiNamespace;
    }

    public void setWikiNamespace(String wikiNamespace) {
        this.wikiNamespace = wikiNamespace;
    }
}
