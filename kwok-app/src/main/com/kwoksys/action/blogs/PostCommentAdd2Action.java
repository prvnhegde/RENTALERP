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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.dto.BlogPostComment;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding comment to blog post.
 */
public class PostCommentAdd2Action extends Action2 {

    public String execute() throws Exception {
        PostCommentAddForm actionForm = saveActionForm(new PostCommentAddForm());
        BlogPostComment postComment = new BlogPostComment();

        postComment.setPostId(actionForm.getPostId());
        postComment.setCommentDescription(actionForm.getPostComment());

        BlogService blogService = ServiceProvider.getBlogService(requestContext);

        ActionMessages errors = blogService.addPostComment(postComment);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.BLOG_POST_DETAIL + "?postId=" + postComment.getPostId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return redirect(AppPaths.BLOG_POST_DETAIL + "?postId=" + postComment.getPostId());
        }
    }
}
