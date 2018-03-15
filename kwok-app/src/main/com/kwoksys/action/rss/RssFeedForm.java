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

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.rss.dto.RssFeed;
import com.kwoksys.framework.http.RequestContext;

/**
 * Action form for editing RSS feed.
 */
public class RssFeedForm extends BaseForm {

    private Integer feedId;
    private String feedUrl;
    private String feedName;

    @Override
    public void setRequest(RequestContext requestContext) {
        feedId = requestContext.getParameterInteger("feedId");
        feedUrl = requestContext.getParameterString("feedUrl");
        feedName = requestContext.getParameterString("feedName");
    }

    public void setFeed(RssFeed rssFeed) {
        feedUrl = rssFeed.getUrl();
        feedName = rssFeed.getName();
    }

    public Integer getFeedId() {
        return feedId;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public String getFeedName() {
        return feedName;
    }
}
