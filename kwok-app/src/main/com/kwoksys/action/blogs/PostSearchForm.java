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
 * Action class for searching blog post.
 */
public class PostSearchForm extends BaseForm {

    private String postTitle;
    private String postDescription;
    private int postAllowComment = BlogPost.POST_ALLOW_COMMENT_SELECT_ONE;
    private String[] creator;
    private Integer categoryId;

    @Override
    public void setRequest(RequestContext requestContext) {
        postTitle = requestContext.getParameterString("postTitle");
        postDescription = requestContext.getParameterString("postDescription");
        categoryId = requestContext.getParameterInteger("categoryId");
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public String[] getCreator() {
        return creator;
    }

    public int getPostAllowComment() {
        return postAllowComment;
    }

    public Integer getCategoryId() {
        return categoryId;
    }
}
