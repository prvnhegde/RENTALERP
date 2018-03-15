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
package com.kwoksys.biz.kb;

import com.kwoksys.action.kb.ArticleSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.SqlUtils;

/**
 * This is for building search queries.
 */
public class KbSearch extends BaseSearch {

    public static final String CATEGORY_ID = "categoryId";

    public static final String ARTICLE_ID = "articleId";

    public static final String ARTICLE_ID_EQUALS = "articleIdEquals";

    public static final String ARTICLE_ID_NOT_EQUALS = "articleIdNotEquals";

    public static final String ARTICLE_TEXT = "articleText";

    public static final String ARTICLE_WIKI_NAMESPACE = "articleWikiNamespace";

    /**
     * Generates searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(ArticleSearchForm articleSearchForm) {
        // Search by KB category id.
        Integer categoryId = articleSearchForm.getCategoryId();
        // Search by article title/content
        String articleText = articleSearchForm.getArticleText();

        String articleWikiNamespace = articleSearchForm.getNamespace();

        if (categoryId != null) {
            searchCriteriaMap.put(CATEGORY_ID, categoryId);
        }
        if (!articleText.isEmpty()) {
            searchCriteriaMap.put(ARTICLE_TEXT, articleText);
        }
        if (!articleWikiNamespace.isEmpty()) {
            searchCriteriaMap.put(ARTICLE_WIKI_NAMESPACE, articleWikiNamespace);
        }
    }

    /**
     * Composes a sql queries from searchCriteriaMap.
     */
    public void applyMap(QueryCriteria queryCriteria) {
        if (searchCriteriaMap == null) {
            return;
        }
        // Search by KB category id.
        if (searchCriteriaMap.containsKey(CATEGORY_ID)) {
            queryCriteria.appendWhereClause("a.category_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(CATEGORY_ID)));
        }
        if (searchCriteriaMap.containsKey(ARTICLE_ID_NOT_EQUALS)) {
            queryCriteria.appendWhereClause("a.article_id != " + SqlUtils.encodeInteger(searchCriteriaMap.get(ARTICLE_ID_NOT_EQUALS)));
        }
        if (searchCriteriaMap.containsKey(ARTICLE_ID_EQUALS)) {
            queryCriteria.appendWhereClause("a.article_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(ARTICLE_ID_EQUALS)));
        }
        // Search by article title/content
        if (searchCriteriaMap.containsKey(ARTICLE_TEXT)) {
            queryCriteria.appendWhereClause("lower(a.article_text) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get(ARTICLE_TEXT)) + "%') " +
                    "or lower(a.article_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get(ARTICLE_TEXT)) + "%')");
        }
        // Search by article wiki namespace.
        if (searchCriteriaMap.containsKey(ARTICLE_WIKI_NAMESPACE)) {
            queryCriteria.appendWhereClause("lower(a.article_wiki_namespace) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(ARTICLE_WIKI_NAMESPACE)) + "')");
        }
    }
}
