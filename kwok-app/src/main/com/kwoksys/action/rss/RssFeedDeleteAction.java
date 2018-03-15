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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.rss.RssService;
import com.kwoksys.biz.rss.dto.RssFeed;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting rss feed.
 */
public class RssFeedDeleteAction extends Action2 {

    public String delete() throws Exception {
        Integer feedId = requestContext.getParameter("feedId");

        RssService rssService = ServiceProvider.getRssService(requestContext);
        RssFeed rssFeed = rssService.getRssFeed(feedId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("portal.rssFeedDelete.title");
        header.setSectionText("<b>" + Localizer.getText(requestContext, "rss.colName.url") + ":</b> " +
                rssFeed.getUrl());

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = standardTemplate.addTemplate(new ObjectDeleteTemplate());
        delete.setFormAjaxAction(AppPaths.RSS_FEED_DELETE_2 + "?feedId=" + feedId);
        delete.setFormCancelAction(AppPaths.RSS_FEED_LIST);
        delete.setConfirmationMsgKey("portal.rssFeedDelete.confirm");
        delete.setSubmitButtonKey("portal.rssFeedDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String delete2() throws Exception {
        Integer feedId = requestContext.getParameter("feedId");

        RssService rssService = ServiceProvider.getRssService(requestContext);
        rssService.getRssFeed(feedId);

        ActionMessages errors = rssService.deleteRssFeed(feedId);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.RSS_FEED_DELETE + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&feedId=" + feedId);
        
        } else {
            return ajaxUpdateView(AppPaths.RSS_FEED_LIST + "?feedId=" + feedId);
        }
    }
}
