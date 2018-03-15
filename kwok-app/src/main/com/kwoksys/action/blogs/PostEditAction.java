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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.BlogPostAccess;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.core.BlogUtils;
import com.kwoksys.biz.blogs.dao.BlogQueries;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing blog post.
 */
public class PostEditAction extends Action2 {

    public String edit() throws Exception {
        AccessUser user = requestContext.getUser();

        PostForm actionForm = getBaseForm(PostForm.class);

        BlogService blogService = ServiceProvider.getBlogService(requestContext);
        BlogPost post = blogService.getPost(actionForm.getPostId());

        // Advance access control
        if (!BlogPostAccess.hasEditPermission(user, post)) {
            throw new AccessDeniedException();
        }

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setPost(post);
        }

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(BlogQueries.getOrderByColumn(Category.CATEGORY_NAME));

        List<LabelValueBean> categoryIdOptions = new ArrayList<>();

        for (Category category : blogService.getCategories(queryCriteria)) {
            categoryIdOptions.add(new LabelValueBean(category.getName(),
                    String.valueOf(category.getId())));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.BLOG_POST_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.BLOG_POST_DETAIL + "?postId=" + post.getId()).getString());
        request.setAttribute("postAllowCommentOptions", BlogUtils.getCommentOptions(requestContext));
        request.setAttribute("postCategoryIdOptions", categoryIdOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("blogs.postEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        AccessUser user = requestContext.getUser();

        PostForm actionForm = saveActionForm(new PostForm());

        BlogService blogService = ServiceProvider.getBlogService(requestContext);
        BlogPost post = blogService.getPost(actionForm.getPostId());

        post.setId(actionForm.getPostId());
        post.setPostTitle(actionForm.getPostTitle());
        post.setPostBody(actionForm.getPostBody());
        post.setPostAllowComment(actionForm.getPostAllowComment());
        post.setCategoryId(actionForm.getCategoryId());

        // Advance access control
        if (!BlogPostAccess.hasEditPermission(user, post)) {
            throw new AccessDeniedException();
        }

        ActionMessages errors = blogService.editPost(post);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.BLOG_POST_EDIT + "?postId=" + post.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return ajaxUpdateView(AppPaths.BLOG_POST_DETAIL + "?postId=" + post.getId());
        }
    }
}
