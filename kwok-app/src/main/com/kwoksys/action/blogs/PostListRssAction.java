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
import com.kwoksys.biz.blogs.core.BlogPostSearch;
import com.kwoksys.biz.blogs.dao.BlogQueries;
import com.kwoksys.biz.blogs.dto.BlogPost;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.parsers.rss.RssModel;
import com.kwoksys.framework.parsers.rss.RssModelHelper;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for blog list in rss format.
 */
public class PostListRssAction extends Action2 {

    public String execute() throws Exception {
        int rowStart = requestContext.getParameter("rowStart", 0);
        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getBlogsNumPosts());

        String appPath = ConfigManager.system.getAppUrl() + AppPaths.ROOT;

        RssModel rssModel = new RssModel();
        rssModel.setTitle(Localizer.getText(requestContext, "blogs.postList.header"));
        rssModel.setLink(appPath);
        rssModel.setDescription(Localizer.getText(requestContext, "rss.description"));
        rssModel.setLanguage("en-us");

        // Create search criteria map
        BlogPostSearch blogPostSearch = new BlogPostSearch();
        blogPostSearch.put("blogPostListPage", "");

        QueryCriteria queryCriteria = new QueryCriteria(blogPostSearch);
        queryCriteria.addSortColumn(BlogQueries.getOrderByColumn(BlogPost.CREATION_DATE), QueryCriteria.DESCENDING);

        // Set limit.
        queryCriteria.setLimit(rowLimit, rowStart);

        BlogService blogService = ServiceProvider.getBlogService(requestContext);

        for (BlogPost post : blogService.getPosts(queryCriteria)) {
            RssModel.Item item = rssModel.new Item();
            item.setTitle(post.getPostTitle());

            String postPermalinkpath = appPath + AppPaths.BLOG_POST_DETAIL + "?postId=" + post.getId();
            item.setLink(postPermalinkpath);
            rssModel.addItem(item);
        }

        response.setContentType("text/xml");

        RssModelHelper helper = new RssModelHelper();
        helper.modelToXml(rssModel);

        response.getWriter().print(helper.getXmlString());
        return null;
    }
}
