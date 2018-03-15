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
package com.kwoksys.biz.rss;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.rss.dao.RssDao;
import com.kwoksys.biz.rss.dto.RssFeed;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.parsers.rss.RssModelHelper;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.HttpUtils;

/**
 * RssService
 */
public class RssService {

    private static final Logger LOGGER = Logger.getLogger(RssService.class.getName());

    private RequestContext requestContext;

    public RssService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public List<RssFeed> getRssFeeds(QueryCriteria query) throws DatabaseException {
        RssDao rssDao = new RssDao(requestContext);
        return rssDao.getRssFeedList(query);
    }

    public RssFeed getRssFeed(Integer feedId) throws DatabaseException, ObjectNotFoundException {
        RssDao rssDao = new RssDao(requestContext);
        return rssDao.getRssFeed(feedId);
    }

    public ActionMessages addRssFeed(RssFeed rssFeed) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (rssFeed.getUrl().isEmpty()) {
            errors.add("emptyUrl", new ActionMessage("common.form.fieldRequired",
                                Localizer.getText(requestContext, "rss.colName.url")));
        } else {
            try {
                // Get the current rss and update database
                RssModelHelper helper = new RssModelHelper();
                helper.xmlToModel(HttpUtils.getContent(rssFeed.getUrl()));

                rssFeed.setName(helper.getRssModel().getTitle());
                rssFeed.setItemCount(helper.getRssModel().getCount());
                rssFeed.setCache(helper.getXmlString());

            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Problem adding RSS feed.", e);
                errors.add("invalidUrl", new ActionMessage("rss.feedAdd.error.invalidUrl",
                        new Object[]{e.getLocalizedMessage()}));
            }
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        RssDao rssDao = new RssDao(requestContext);
        return rssDao.add(rssFeed);
    }

    public ActionMessages updateRssFeed(RssFeed rssFeed) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (rssFeed.getUrl().isEmpty()) {
            errors.add("emptyUrl", new ActionMessage("common.form.fieldRequired",
                                Localizer.getText(requestContext, "rss.colName.url")));
        }
        if (rssFeed.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                                Localizer.getText(requestContext, "rss.colName.name")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        RssDao rssDao = new RssDao(requestContext);
        return rssDao.update(rssFeed);
    }

    public ActionMessages updateRssFeedContent(RssFeed rssFeed) throws DatabaseException {
        RssDao rssDao = new RssDao(requestContext);
        return rssDao.updateContent(rssFeed);
    }

    public ActionMessages deleteRssFeed(Integer rssFeedId) throws DatabaseException {
        RssDao rssDao = new RssDao(requestContext);
        return rssDao.delete(rssFeedId);
    }
}
