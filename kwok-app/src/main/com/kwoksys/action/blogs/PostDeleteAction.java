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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.BlogPostAccess;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting a post.
 */
public class PostDeleteAction extends Action2 {

    public String delete() throws Exception {
        BlogService blogService = ServiceProvider.getBlogService(requestContext);
        BlogPost blogPost = blogService.getPost(requestContext.getParameter("postId"));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, "delete");
        standardTemplate.setAttribute("postId", blogPost.getId());
        standardTemplate.setAttribute("postTitle", blogPost.getPostTitle());
        String[] args = {AdminUtils.getSystemUsername(requestContext, blogPost.getCreator()), blogPost.getCreationDate()};
        standardTemplate.setAttribute("postCreator", Localizer.getText(requestContext, "blogs.colName.creation_info", args));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("blogs.postDelete.header");

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = standardTemplate.addTemplate(new ObjectDeleteTemplate());
        delete.setFormAjaxAction(AppPaths.BLOG_POST_DELETE_2 + "?postId=" + blogPost.getId());
        delete.setFormCancelAction(AppPaths.BLOG_POST_DETAIL + "?postId=" + blogPost.getId());
        delete.setConfirmationMsgKey("blogs.postDelete.confirm");
        delete.setSubmitButtonKey("blogs.postDelete.buttonSubmit");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String delete2() throws Exception {
        AccessUser user = requestContext.getUser();

        PostForm actionForm = saveActionForm(new PostForm());

        BlogService blogService = ServiceProvider.getBlogService(requestContext);
        BlogPost post = blogService.getPost(actionForm.getPostId());

        // Advance access control
        if (!BlogPostAccess.hasDeletePermission(user, post)) {
            throw new AccessDeniedException();
        }

        ActionMessages errors = blogService.deletePost(post);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.BLOG_POST_DELETE + "?postId=" + post.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.BLOG_POST_LIST);
        }
    }
}
