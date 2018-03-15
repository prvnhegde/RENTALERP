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
package com.kwoksys.action.rss;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.rss.RssService;
import com.kwoksys.biz.rss.dto.RssFeed;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for showing rss feeds.
 */
public class RssFeedListTitlesAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        QueryCriteria query = new QueryCriteria();
        query.addSortColumn("feed_name");

        List<String> rssFeeds = new ArrayList<>();

        RssService rssService = ServiceProvider.getRssService(requestContext);

        for (RssFeed rssFeed : rssService.getRssFeeds(query)) {
            Link link = new Link(requestContext);
            link.setJavascript("parent.frameItems.location.href='" + AppPaths.ROOT + AppPaths.RSS_FEED_LIST_ITEMS + "?feedId=" + rssFeed.getId() + "'");
            link.setTitle(rssFeed.getName() + " (" + rssFeed.getItemCount() + ")");
            rssFeeds.add(link.getString());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("rssFeeds", rssFeeds);
        String sessionTheme = SessionManager.getAppSessionTheme(request.getSession());
        standardTemplate.setAttribute("themeStylePath", AppPaths.getInstance().getThemeCss(sessionTheme));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();

        // Add RSS feed.
        if (Access.hasPermission(user, AppPaths.RSS_FEED_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.RSS_FEED_ADD);
            link.setTitleKey("portal.rssFeedAdd.title");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(SUCCESS);
    }
}
