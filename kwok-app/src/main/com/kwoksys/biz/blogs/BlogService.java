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
package com.kwoksys.biz.blogs;

import com.kwoksys.biz.blogs.dao.BlogDao;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.blogs.dto.BlogPostComment;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dao.CategoryDao;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.List;

/**
 * BlogService
 */
public class BlogService {

    private RequestContext requestContext;

    public BlogService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * Get post categories.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<Category> getCategories(QueryCriteria query) throws DatabaseException {
        return new CategoryDao(requestContext).getCategoryList(query, ObjectTypes.BLOG_POST);
    }

    public Category getCategory(Integer categoryId) throws DatabaseException, ObjectNotFoundException {
        return new CategoryDao(requestContext).getCategory(categoryId, ObjectTypes.BLOG_POST);
    }

    /**
     * Add blog category
     * @param category
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addCategory(Category category) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (category.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("blogs.categoryEdit.error.emptyName"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new CategoryDao(requestContext).addCategory(category);
    }

    public ActionMessages updateCategory(Category category) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (category.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("blogs.categoryEdit.error.emptyName"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new CategoryDao(requestContext).editCategory(category);
    }

    /**
     * Gets blog posts.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<BlogPost> getPosts(QueryCriteria query) throws DatabaseException {
        return new BlogDao(requestContext).getPostList(query);
    }

    public int getPostCount(QueryCriteria query) throws DatabaseException {
        return new BlogDao(requestContext).getPostCount(query);
    }

    public BlogPost getPost(Integer postId) throws DatabaseException, ObjectNotFoundException {
        return new BlogDao(requestContext).getPost(postId);
    }

    /**
     * Adds blog post.
     * @param post
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addPost(BlogPost post) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (post.getPostTitle().isEmpty()) {
            errors.add("emptyTitle", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.post_name")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new BlogDao(requestContext).addPost(post);
    }

    public ActionMessages editPost(BlogPost post) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (post.getPostTitle().isEmpty()) {
            errors.add("emptyTitle", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.post_name")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new BlogDao(requestContext).editPost(post);
    }

    public ActionMessages deletePost(BlogPost post) throws DatabaseException {
        return new BlogDao(requestContext).deletePost(post);
    }

    /**
     * Gets post comments.
     * @param query
     * @param postId
     * @return
     * @throws DatabaseException
     */
    public List<BlogPostComment> getPostComments(QueryCriteria query, Integer postId) throws DatabaseException {
        return new BlogDao(requestContext).getPostCommentList(query, postId);
    }

    public ActionMessages addPostComment(BlogPostComment comment) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (comment.getCommentDescription().isEmpty()) {
            errors.add("emptyDescription", new ActionMessage("blogs.postDetail.error.emptyDescription"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new BlogDao(requestContext).addPostComment(comment);
    }
}
