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
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.core.BlogUtils;
import com.kwoksys.biz.blogs.dao.BlogQueries;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for adding blog post.
 */
public class PostAddAction extends Action2 {

    public String add() throws Exception {
        AccessUser user = requestContext.getUser();

        BlogPost post = new BlogPost();
        PostForm actionForm = getBaseForm(PostForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setPost(post);
        }

        String postCreatorText = Localizer.getText(requestContext,
                "blogs.colName.creation_preview_info", new Object[]{user.getDisplayName()});

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(BlogQueries.getOrderByColumn(Category.CATEGORY_NAME));

        List<LabelValueBean> categoryIdOptions = new ArrayList<>();

        BlogService blogService = ServiceProvider.getBlogService(requestContext);

        for (Category category : blogService.getCategories(queryCriteria)) {
            categoryIdOptions.add(new LabelValueBean(category.getName(), String.valueOf(category.getId())));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.BLOG_POST_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.BLOG_POST_LIST).getString());
        standardTemplate.setAttribute("previewLink", new Link(requestContext).setJavascript("Js.Display.toggle('previewPostDiv')")
                .setTitleKey("blogs.postAdd.toggleDiv"));
        request.setAttribute("postAllowCommentOptions", BlogUtils.getCommentOptions(requestContext));
        request.setAttribute("postCategoryIdOptions", categoryIdOptions);
        request.setAttribute("postCreatorText", postCreatorText);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("portal.cmd.blogPostAdd");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("blogs.postAdd.sectionHeader");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String add2() throws Exception {
        BlogPost post = new BlogPost();
        PostForm actionForm = saveActionForm(new PostForm());
        post.setPostTitle(actionForm.getPostTitle());
        post.setPostBody(actionForm.getPostBody());
        post.setPostAllowComment(actionForm.getPostAllowComment());
        post.setCategoryId(actionForm.getCategoryId());

        BlogService blogService = ServiceProvider.getBlogService(requestContext);

        ActionMessages errors = blogService.addPost(post);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.BLOG_POST_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
            
        } else {
            return ajaxUpdateView(AppPaths.BLOG_POST_DETAIL + "?postId=" + post.getId());
        }
    }
}
