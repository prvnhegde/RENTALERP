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
package com.kwoksys.biz.blogs.core;

import com.kwoksys.action.blogs.PostSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.SqlUtils;
import com.kwoksys.framework.http.RequestContext;

/**
 * This is for building search queries.
 */
public class BlogPostSearch extends BaseSearch {

    public BlogPostSearch(RequestContext requestContext, String sessionKey) {
        super(requestContext, sessionKey);
    }

    public BlogPostSearch() {}

    /**
     * Prepare searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(PostSearchForm postSearchForm) {
        String cmd = requestContext.getParameterString("cmd");

        if (!cmd.isEmpty()) {
            reset();
            postSearchForm.setRequest(requestContext);

            if (cmd.equals("showAll")) {
                // No criteria set, search all posts.

            } else if (cmd.equals("search")) {
                // Search by post title contains.
                String postTitle = postSearchForm.getPostTitle();
                if (!postTitle.isEmpty()) {
                    searchCriteriaMap.put("postTitleContains", postTitle);
                }
                // Search by post description contains.
                String postDescription = postSearchForm.getPostDescription();
                if (!postDescription.isEmpty()) {
                    searchCriteriaMap.put("postDescriptionContains", postDescription);
                }
                // Search by whether Blog post allow comment.
                int postAllowComment = postSearchForm.getPostAllowComment();
                if (postAllowComment != BlogPost.POST_ALLOW_COMMENT_SELECT_ONE) {
                    searchCriteriaMap.put("postAllowComment", postAllowComment);
                }
                // Search by post category id.
                Integer categoryId = postSearchForm.getCategoryId();
                if (categoryId != 0) {
                    searchCriteriaMap.put("categoryId", categoryId);
                }
            }
        }
    }

    /**
     * This would take searchCriteriaMap and compose the sql queries.
     */
    public void applyMap(QueryCriteria query) {
        if (searchCriteriaMap.isEmpty()) {
            return;
        }
        // For Blog type equals.
        if (searchCriteriaMap.containsKey("postTypeEquals")) {
            query.appendWhereClause("p.post_type = " + SqlUtils.encodeInteger(searchCriteriaMap.get("postTypeEquals")));
        }
        // For post title contain.
        if (searchCriteriaMap.containsKey("postTitleContains")) {
            query.appendWhereClause("lower(p.post_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("postTitleContains")) + "%')");
        }
        // For post description contain.
        if (searchCriteriaMap.containsKey("postDescriptionContains")) {
            query.appendWhereClause("lower(p.post_description) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("postDescriptionContains")) + "%')");
        }
        // For whether Blog post allow comment.
        if (searchCriteriaMap.containsKey("postAllowComment")) {
            query.appendWhereClause("p.post_allow_comment = " + SqlUtils.encodeInteger(searchCriteriaMap.get("postAllowComment")));
        }
        // For whether Blog post allow comment.
        if (searchCriteriaMap.containsKey("categoryId")) {
            query.appendWhereClause("p.category_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get("categoryId")));
        }
    }
}
