/*
 * Copyright 2015 Kwoksys
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.BlogPostAccess;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.blogs.dto.BlogPostComment;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for showing post detail.
 */
public class PostDetailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        getBaseForm(PostCommentAddForm.class);

        BlogService blogService = ServiceProvider.getBlogService(requestContext);
        Integer postId = requestContext.getParameter("postId");
        BlogPost blogPost = blogService.getPost(postId);

        Object[] args = {AdminUtils.getSystemUsername(requestContext, blogPost.getCreator()), blogPost.getCreationDate()};
        String postCommentCount = Localizer.getText(requestContext, "blogs.postDetail.commentHeader",
                new Object[]{blogPost.getPostCommentCount()});

        boolean postAllowComment = blogPost.isPostAllowComment();

        // Get post comments.
        List<Map<String, String>> postCommentList = new ArrayList<>();
        for (BlogPostComment comment : blogService.getPostComments(new QueryCriteria(), blogPost.getId())) {
            Map<String, String> map = new HashMap<>();
            map.put("commentDescription", HtmlUtils.formatMultiLineDisplay(comment.getCommentDescription()));

            Link link = new Link(requestContext);
            link.setTitle(Localizer.getText(requestContext, "blogs.colData.comment_creator",
                                new String[]{AdminUtils.getSystemUsername(requestContext, comment.getCreator()), comment.getCreationDate()}));
            map.put("commentCreator", Image.getInstance().getBlogComment() + link.getString());
            postCommentList.add(map);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("postTitle", blogPost.getPostTitle());
        standardTemplate.setAttribute("postBody", HtmlUtils.formatMultiLineDisplay(blogPost.getPostBody()));
        standardTemplate.setAttribute("postCreator", Localizer.getText(requestContext, "blogs.colName.creation_info", args));
        standardTemplate.setAttribute("postCommentCount", postCommentCount);
        standardTemplate.setAttribute("postCommentList", postCommentList);
        if (postAllowComment && Access.hasPermission(user, AppPaths.BLOG_POST_COMMENT_ADD_2)) {
            standardTemplate.setPathAttribute("postCommentPath", AppPaths.BLOG_POST_COMMENT_ADD_2 + "?postId=" + blogPost.getId());
        }
        standardTemplate.setAttribute("postAllowComment", postAllowComment);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();

        // Back to Blog list.
        if (Access.hasPermission(user, AppPaths.BLOG_POST_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.BLOG_POST_LIST);
            link.setTitleKey("portal.cmd.blogPostList");
            header.addHeaderCmds(link);
        }

        if (user.hasPermission(AppPaths.BLOG_POST_EDIT)) {
            Link link = new Link(requestContext);

            if (BlogPostAccess.hasEditPermission(user, blogPost)) {
                link.setAjaxPath(AppPaths.BLOG_POST_EDIT + "?postId=" + blogPost.getId());
            } else {
                link.setImgAltKey("blogs.postEdit.noPermission");
                link.setImgSrc(Image.getInstance().getSignInfo());
            }
            link.setTitleKey("blogs.postEdit.header");
            header.addHeaderCmds(link);
        }

        if (user.hasPermission(AppPaths.BLOG_POST_DELETE)) {
            Link link = new Link(requestContext);

            if (BlogPostAccess.hasDeletePermission(user, blogPost)) {
                link.setAjaxPath(AppPaths.BLOG_POST_DELETE + "?postId=" + blogPost.getId());
            } else {
                link.setImgAltKey("blogs.postDelete.noPermission");
                link.setImgSrc(Image.getInstance().getSignInfo());
            }
            link.setTitleKey("blogs.postDelete.header");
            header.addHeaderCmds(link);
        }

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
