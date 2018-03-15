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
package com.kwoksys.biz.blogs.dao;

import com.kwoksys.framework.connections.database.QueryCriteria;

/**
 * Format: select_, insert_, update_, delete_, _Query
 */
public class BlogQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals("category_name")) {
            return "lower(category_name)";

        } else {
            return column;
        }
    }

    /**
     * Return a list of Portal Blogs.
     *
     * @return ..
     */
    public static String selectBlogPostListQuery(QueryCriteria query) {
        return "select p.post_id, p.post_name, p.post_description, p.post_type, p.post_ip, p.comment_count, " +
                "p.category_id, p.category_name, " +
                "p.creator, p.creator_username, p.creator_display_name, p.creation_date, " +
                "p.modifier, p.modifier_username, p.modifier_display_name, p.modification_date " +
                "from blog_post_view p "
                + query.createWhereClause();
    }

    /**
     * Return the number of Portal Blogs.
     *
     * @return ..
     */
    public static String getBlogPostCountQuery(QueryCriteria query) {
        return "select count(p.post_id) as row_count from blog_post p " + query.createWhereCountClause();
    }

    /**
     * Return Portal Blog detail.
     *
     * @return ..
     */
    public static String selectBlogPostDetailQuery() {
        return "select p.post_id, p.post_name, p.post_description, p.post_type, p.post_ip, p.post_allow_comment, p.comment_count, " +
                "p.category_id, p.category_name, " +
                "p.creator, p.creator_username, p.creator_display_name, p.creation_date, " +
                "p.modifier, p.modifier_username, p.modifier_display_name, p.modification_date " +
                "from blog_post_view p " +
                "where p.post_id=?";
    }

    /**
     * Return post comments for a specific post.
     */
    public static String selectBlogPostCommentListQuery(QueryCriteria query) {
        return "select pc.comment_id, pc.comment_description, pc.comment_ip, pc.creator, pc.creator_username, pc.creator_display_name, pc.creation_date " +
                "from blog_post_comment_view pc " +
                "where pc.post_id=? "
                + query.createAndClause();
    }

    /**
     * Add Blog Post.
     */
    public static String insertBlogPostQuery() {
        return "{call sp_blog_post_add(?,?,?,?,?,?,?,?)}";
    }

    public static String updateBlogPostQuery() {
        return "{call sp_blog_post_update(?,?,?,?,?,?,?)}";
    }

    /**
     * Deletes blog post
     * @return
     */
    public static String deleteBlogPostQuery() {
        return "{call sp_blog_post_delete(?,?)}";
    }

    /**
     * Add Blog Post Comment.
     */
    public static String insertBlogPostCommentQuery() {
        return "{call sp_blog_post_comment_add(?,?,?,?,?)}";
    }
}