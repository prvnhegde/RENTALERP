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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.blogs.dao.BlogQueries;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

/**
 * Action class for portal index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        getSessionBaseForm(PostSearchForm.class);

        AccessUser user = requestContext.getUser();

        // Allow comment options.
        List<LabelValueBean> commentOptions = Arrays.asList(
                new SelectOneLabelValueBean(requestContext, String.valueOf(BlogPost.POST_ALLOW_COMMENT_SELECT_ONE)),
                new LabelValueBean(Localizer.getText(requestContext, "common.boolean.yes_no.true"),
                        String.valueOf(BlogPost.POST_ALLOW_COMMENT_YES)),
                new LabelValueBean(Localizer.getText(requestContext, "common.boolean.yes_no.false"),
                        String.valueOf(BlogPost.POST_ALLOW_COMMENT_NO)));

        // Show categories.
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(BlogQueries.getOrderByColumn(Category.CATEGORY_NAME));

        List<Map> postCategoryLinks = new ArrayList<>();
        List<LabelValueBean> postCategoryIdLabel = new ArrayList<>();
        postCategoryIdLabel.add(new SelectOneLabelValueBean(requestContext));

        BlogService blogService = ServiceProvider.getBlogService(requestContext);

        for (Category category : blogService.getCategories(queryCriteria)) {
            Map<String, String> map = new HashMap<>();
            map.put("link", new Link(requestContext)
                    .setAjaxPath(AppPaths.BLOG_POST_LIST + "?cmd=search&categoryId=" + category.getId())
                    .setTitle(category.getName()).getString());
            map.put("postCount", String.valueOf(category.getCountObjects()));
            postCategoryLinks.add(map);

            postCategoryIdLabel.add(new LabelValueBean(category.getName(), String.valueOf(category.getId())));
        }

        List<String> linkList = new ArrayList<>();

        // Link to hardware list.
        if (user.hasPermission(AppPaths.BLOG_POST_LIST)) {
            linkList.add(new Link(requestContext).setTitleKey("portal.index.showAllPosts")
                    .setAjaxPath(AppPaths.BLOG_POST_LIST + "?cmd=showAll").getString());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("postAllowCommentOptions", commentOptions);
        standardTemplate.setPathAttribute("postListPath", AppPaths.BLOG_POST_LIST);
        standardTemplate.setAttribute("postCategoryLinks", postCategoryLinks);
        standardTemplate.setAttribute("postCategoryIdLabel", postCategoryIdLabel);
        standardTemplate.setAttribute("linkList", linkList);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.6");
        header.setTitleClassNoLine();
        
        // Manage categories
        if (user.hasPermission(AppPaths.BLOG_CATEGORY_LIST)) {
            header.addHeaderCmds(new Link(requestContext).setTitleKey("blogs.categoryList.header")
                    .setAjaxPath(AppPaths.BLOG_CATEGORY_LIST));
        }

        // Add blog post
        if (user.hasPermission(AppPaths.BLOG_POST_ADD)) {
            header.addHeaderCmds(new Link(requestContext).setTitleKey("portal.cmd.blogPostAdd")
                    .setAjaxPath(AppPaths.BLOG_POST_ADD));
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
