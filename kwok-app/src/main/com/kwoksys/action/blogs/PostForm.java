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
package com.kwoksys.action.blogs;

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.framework.http.RequestContext;

/**
 * Action class for adding blog post.
 */
public class PostForm extends BaseForm {

    private Integer postId;
    private String postTitle;
    private String postBody;
    private int postAllowComment;
    private Integer categoryId;

    @Override
    public void setRequest(RequestContext requestContext) {
        postId = requestContext.getParameterInteger("postId");
        postTitle = requestContext.getParameterString("postTitle");
        postBody = requestContext.getParameterString("postBody");
        postAllowComment = requestContext.getParameter("postAllowComment");
        categoryId = requestContext.getParameterInteger("categoryId");
    }

    public void setPost(BlogPost post) {
        postTitle = post.getPostTitle();
        postBody = post.getPostBody();
        postAllowComment = post.getPostAllowComment();
        if (categoryId == null) {
            categoryId = post.getCategoryId();
        }
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public int getPostAllowComment() {
        return postAllowComment;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public Integer getPostId() {
        return postId;
    }
}
