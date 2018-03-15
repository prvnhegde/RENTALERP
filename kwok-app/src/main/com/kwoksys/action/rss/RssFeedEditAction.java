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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.rss.RssService;
import com.kwoksys.biz.rss.dto.RssFeed;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding RSS feed.
 */
public class RssFeedEditAction extends Action2 {

    public String edit() throws Exception {
        RssFeedForm actionForm = getBaseForm(RssFeedForm.class);

        RssService rssService = ServiceProvider.getRssService(requestContext);
        RssFeed rssFeed = rssService.getRssFeed(actionForm.getFeedId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setFeed(rssFeed);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.RSS_FEED_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.RSS_FEED_LIST).getString());
        request.setAttribute("rssFeedId", rssFeed.getId());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("portal.rssFeedEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        RssService rssService = ServiceProvider.getRssService(requestContext);

        RssFeedForm actionForm = saveActionForm(new RssFeedForm());

        RssFeed rssFeed = rssService.getRssFeed(actionForm.getFeedId());
        rssFeed.setUrl(actionForm.getFeedUrl());
        rssFeed.setName(actionForm.getFeedName());

        ActionMessages errors = rssService.updateRssFeed(rssFeed);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.RSS_FEED_EDIT + "?" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.RSS_FEED_LIST);
        }
    }
}
