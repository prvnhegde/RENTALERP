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
package com.kwoksys.biz.kb.dao;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.EventTypes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * KbDao.
 */
public class KbDao extends BaseDao {

    public KbDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * This is to return a list of Articles.
     *
     * @param query
     * @return ..
     */
    public List<Article> getArticles(QueryCriteria query) throws DatabaseException {
        List<Article> articles = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(KbQueries.selectArticleListQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Article article = new Article();
                article.setId(rs.getInt(Article.ARTICLE_ID));
                article.setName(rs.getString(Article.ARTICLE_NAME));
                article.setContent(StringUtils.replaceNull(rs.getString("article_text")));
                article.setCategoryId(rs.getInt("category_id"));
                article.setCategoryName(StringUtils.replaceNull(rs.getString("category_name")));
                article.setSyntaxType(rs.getInt(Article.ARTICLE_SYNTAX));
                article.setViewCount(rs.getLong("view_count"));
                article.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                article.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                article.setCreator(new AccessUser());
                article.getCreator().setId(rs.getInt("creator"));
                article.getCreator().setUsername(rs.getString("creator_username"));
                article.getCreator().setDisplayName(rs.getString("creator_display_name"));

                article.setModifier(new AccessUser());
                article.getModifier().setId(rs.getInt("modifier"));
                article.getModifier().setUsername(rs.getString("modifier_username"));
                article.getModifier().setDisplayName(rs.getString("modifier_display_name"));

                articles.add(article);
            }
        };

        executeQuery(queryHelper);
        
        return articles;
    }

    /**
     * This returns Articles count.
     *
     * @param query
     * @return ..
     */
    public int getArticlesCount(QueryCriteria query) throws DatabaseException {
        return getRowCount(KbQueries.getArticlesCountQuery(query));
    }

    /**
     * This is for getting Article detail.
     *
     * @param articleId
     * @return ..
     */
    public Article getArticle(QueryCriteria query) throws DatabaseException, ObjectNotFoundException {
        List<Article> articles = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(KbQueries.selectArticleDetailQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Article article = new Article();
                article.setId(rs.getInt(Article.ARTICLE_ID));
                article.setName(rs.getString(Article.ARTICLE_NAME));
                article.setContent(StringUtils.replaceNull(rs.getString("article_text")));
                article.setCategoryId(rs.getInt("category_id"));
                article.setCategoryName(StringUtils.replaceNull(rs.getString("category_name")));
                article.setSyntaxType(rs.getInt(Article.ARTICLE_SYNTAX));
                article.setViewCount(rs.getLong("view_count"));
                article.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                article.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                article.setCreator(new AccessUser());
                article.getCreator().setId(rs.getInt("creator"));
                article.getCreator().setUsername(rs.getString("creator_username"));
                article.getCreator().setDisplayName(rs.getString("creator_display_name"));

                article.setModifier(new AccessUser());
                article.getModifier().setId(rs.getInt("modifier"));
                article.getModifier().setUsername(rs.getString("modifier_username"));
                article.getModifier().setDisplayName(rs.getString("modifier_display_name"));
                articles.add(article);
            }
        };
        
        executeSingleRecordQuery(queryHelper);
        
        if (!articles.isEmpty()) {
            return articles.get(0);
        } else {
            throw new ObjectNotFoundException();
        }
    }

    /**
     * Add Article.
     *
     * @return ..
     */
    public ActionMessages addArticle(Article article) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(KbQueries.addArticleQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(article.getName());
        queryHelper.addInputStringConvertNull(article.getContent());
        queryHelper.addInputInt(article.getSyntaxType());
        queryHelper.addInputStringConvertNull(article.getWikiNamespace());
        queryHelper.addInputInt(article.getCategoryId());
        queryHelper.addInputInt(EventTypes.KB_ARTICLE_ADD);
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Put some values in the result.
        if (errors.isEmpty()) {
            article.setId((Integer) queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    /**
     * Given an Article object, update Article.
     *
     * @return ..
     */
    public ActionMessages updateArticle(Article article) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(KbQueries.updateArticleQuery());
        queryHelper.addInputInt(article.getId());
        queryHelper.addInputStringConvertNull(article.getName());
        queryHelper.addInputStringConvertNull(article.getContent());
        queryHelper.addInputInt(article.getSyntaxType());
        queryHelper.addInputStringConvertNull(article.getWikiNamespace());
        queryHelper.addInputInt(article.getCategoryId());
        queryHelper.addInputInt(EventTypes.KB_ARTICLE_UPDATE);
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    /**
     * Deletes article.
     *
     * @return ..
     */
    public ActionMessages deleteArticle(Article article) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(KbQueries.deleteArticleQuery());
        queryHelper.addInputInt(ObjectTypes.KB_ARTICLE);
        queryHelper.addInputInt(article.getId());
        queryHelper.addInputInt(EventTypes.KB_ARTICLE_DELETE);
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    /**
     * Updates Article view count
     * @param article
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateArticleViewCount(Article article) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(KbQueries.updateArticleViewCountQuery());
        queryHelper.addInputInt(article.getId());

        return executeProcedure(queryHelper);
    }
}
