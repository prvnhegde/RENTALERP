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

import java.util.List;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.kb.dao.KbDao;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.dao.CategoryDao;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.validations.ColumnField;
import com.kwoksys.framework.validations.InputValidator;

/**
 * KbService
 */
public class KbService {

    private RequestContext requestContext;

    public KbService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    //
    // KB Categories
    //
    /**
     * Gets a list of KB categories
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<Category> getCategories(QueryCriteria query) throws DatabaseException {
        CategoryDao categoryDao = new CategoryDao(requestContext);
        return categoryDao.getCategoryList(query, ObjectTypes.KB_ARTICLE);
    }

    public Category getCategory(Integer categoryId) throws DatabaseException, ObjectNotFoundException {
        CategoryDao categoryDao = new CategoryDao(requestContext);
        return categoryDao.getCategory(categoryId, ObjectTypes.KB_ARTICLE);
    }

    /**
     * Adds a KB category.
     * @param category
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addCategory(Category category) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (category.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("kb.categoryAdd.error.emptyName"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        CategoryDao categoryDao = new CategoryDao(requestContext);
        return categoryDao.addCategory(category);
    }

    /**
     * Updates a KB category.
     * @param category
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateCategory(Category category) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (category.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("kb.categoryAdd.error.emptyName"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        CategoryDao categoryDao = new CategoryDao(requestContext);
        return categoryDao.editCategory(category);
    }

    /**
     * Gets a list of Articles.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<Article> getArticles(QueryCriteria query) throws DatabaseException {
        KbDao kbDao = new KbDao(requestContext);
        return kbDao.getArticles(query);
    }

    /**
     * Gets an Articles count.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public int getArticlesCount(QueryCriteria query) throws DatabaseException {
        return new KbDao(requestContext).getArticlesCount(query);
    }

    /**
     * Given an article id, returns an article object.
     * @param articleId
     * @return
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     */
    public Article getArticle(Integer articleId) throws DatabaseException, ObjectNotFoundException {
        KbSearch kbSearch = new KbSearch();
        kbSearch.put(KbSearch.ARTICLE_ID_EQUALS, articleId);
        QueryCriteria query = new QueryCriteria(kbSearch);

        KbDao kbDao = new KbDao(requestContext);
        return kbDao.getArticle(query);
    }

    public Article getArticle(String articleWikiNamespace) throws DatabaseException, ObjectNotFoundException {
        KbSearch kbSearch = new KbSearch();
        kbSearch.put(KbSearch.ARTICLE_WIKI_NAMESPACE, articleWikiNamespace);
        QueryCriteria queryCriteria = new QueryCriteria(kbSearch);

        KbDao kbDao = new KbDao(requestContext);
        return kbDao.getArticle(queryCriteria);
    }

    /**
     * Gets KB article files.
     * @param query
     * @param companyId
     * @return
     * @throws DatabaseException
     */
    public List<File> getArticleFiles(QueryCriteria queryCriteria, Integer articleId) throws DatabaseException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        return fileService.getFiles(queryCriteria, ObjectTypes.KB_ARTICLE, articleId);
    }

    public File getArticleFile(Integer articleId, Integer fileId) throws DatabaseException, ObjectNotFoundException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        File file = fileService.getFile(ObjectTypes.KB_ARTICLE, articleId, fileId);
        file.setConfigRepositoryPath(ConfigManager.file.getKbFileRepositoryLocation());
        file.setConfigUploadedFilePrefix(ConfigManager.file.getKbUploadedFilePrefix());
        return file;
    }

    /**
     * Checks whether a namespace is being used by another article.
     * @param article
     * @return
     * @throws DatabaseException
     */
    public boolean isWikiNamespaceInUse(Article article) throws DatabaseException {
    	if (article.getWikiNamespace() == null) {
    		return false;
    	}
        KbSearch kbSearch = new KbSearch();
        kbSearch.put(KbSearch.ARTICLE_WIKI_NAMESPACE, article.getWikiNamespace());
        if (article.getId() != null && article.getId() != 0) {
            kbSearch.put(KbSearch.ARTICLE_ID_NOT_EQUALS, article.getId());
        }
        QueryCriteria queryCriteria = new QueryCriteria(kbSearch);
        return (getArticlesCount(queryCriteria) > 0);
    }

    /**
     * Adds a KB Article.
     * @param article
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addArticle(Article article) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("articleTitle").setTitleKey("common.column.article_name")
                .setNullable(false).calculateLength(article.getName()).setColumnName(Schema.KB_ARTICLE_NAME));

        // The way we count characters is a little special because the textarea includes some line breaks
        // on top of the number of characters in the textarea (which the user can see).
        // So, we take it out before doing the count. This is ok, because the char limit is a soft limit anyway.
        if (article.getContent().replace("\n","").replace("\r","").length() > ConfigManager.app.getKbArticleCharLimit()) {
            String fieldName = Localizer.getText(requestContext, "common.column.article_text");
            errors.add("articleText", new ActionMessage("common.form.fieldExceededMaxLen",
                    new Object[]{fieldName, ConfigManager.app.getKbArticleCharLimit()}));
        }

        // Checks for unique namespace
        if (isWikiNamespaceInUse(article)) {
            errors.add("namespaceInUse", new ActionMessage("kb.articleAdd.error.namespaceInUse", new Object[]{article.getWikiNamespace()}));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        KbDao kbDao = new KbDao(requestContext);
        return kbDao.addArticle(article);
    }

    /**
     * Updates a KB Aricle.
     * @param article
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateArticle(Article article) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("articleTitle").setTitleKey("common.column.article_name")
                .setNullable(false).calculateLength(article.getName()).setColumnName(Schema.KB_ARTICLE_NAME));

        // The way we count characters is a little special because the textarea includes some line breaks
        // on top of the number of characters in the textarea (which the user can see).
        // So, we take it out before doing the count. This is ok, because the char limit is a soft limit anyway.
        if (article.getContent().replace("\n","").replace("\r","").length() > ConfigManager.app.getKbArticleCharLimit()) {
            String fieldName = Localizer.getText(requestContext, "common.column.article_text");
            errors.add("articleText", new ActionMessage("common.form.fieldExceededMaxLen",
                    new Object[]{fieldName, ConfigManager.app.getKbArticleCharLimit()}));
        }

        // Checks for unique namespace
        if (isWikiNamespaceInUse(article)) {
            errors.add("namespaceInUse", new ActionMessage("kb.articleAdd.error.namespaceInUse", new Object[]{article.getWikiNamespace()}));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new KbDao(requestContext).updateArticle(article);
    }

    /**
     * Deletes a KB Aricle.
     * @param article
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteArticle(Article article) throws DatabaseException {
        /**
         * Here is what i can do. First, collect a list of file names to be deleted (probably in a dataset).
         * Then, delete the Article, which would also delete the File records. Next, delete the actual files.
         */
        List<File> deleteFileList = getArticleFiles(new QueryCriteria(), article.getId());

        KbDao kbDao = new KbDao(requestContext);
        ActionMessages errors = kbDao.deleteArticle(article);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete actual files
        if (errors.isEmpty()) {
            fileService.bulkDelete(ConfigManager.file.getKbFileRepositoryLocation(), deleteFileList);
        }
        return errors;
    }

    /**
     * Updates Article view count.
     * @param article
     * @throws DatabaseException
     */
    public void updateArticleViewCount(Article article) throws DatabaseException {
        KbDao kbDao = new KbDao(requestContext);
        kbDao.updateArticleViewCount(article);
    }
}
