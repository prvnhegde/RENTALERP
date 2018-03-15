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
package com.kwoksys.biz.rss.dao;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.rss.dto.RssFeed;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * RssDao
 */
public class RssDao extends BaseDao {

    public RssDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Gets a list of RSS Feeds.
     *
     * @param query
     * @return ..
     */
    public List<RssFeed> getRssFeedList(QueryCriteria query) throws DatabaseException {
        List<RssFeed> rssFeeds = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(RssQueries.selectRssFeedListQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                RssFeed rssFeed = new RssFeed();
                rssFeed.setId(rs.getInt("feed_id"));
                rssFeed.setName(StringUtils.replaceNull(rs.getString("feed_name")));
                rssFeed.setUrl(StringUtils.replaceNull(rs.getString("feed_url")));
                rssFeed.setCache(StringUtils.replaceNull(rs.getString("feed_cache")));
                rssFeed.setItemCount(rs.getInt("feed_item_count"));

                rssFeeds.add(rssFeed);
            }
        };

        executeQuery(queryHelper);
        
        return rssFeeds;
    }

    /**
     * Gets a RSS Feed.
     * @param feedId
     * @return
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     */
    public RssFeed getRssFeed(Integer feedId) throws DatabaseException, ObjectNotFoundException {
        List<RssFeed> rssFeeds = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(RssQueries.selectRssFeedDetailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                RssFeed rssFeed = new RssFeed();
                rssFeed.setId(rs.getInt("feed_id"));
                rssFeed.setName(StringUtils.replaceNull(rs.getString("feed_name")));
                rssFeed.setUrl(StringUtils.replaceNull(rs.getString("feed_url")));
                rssFeed.setCache(StringUtils.replaceNull(rs.getString("feed_cache")));
                rssFeed.setCacheDate(DatetimeUtils.getDate(rs, "feed_cache_date"));
                rssFeed.setItemCount(rs.getInt("feed_item_count"));
                
                rssFeeds.add(rssFeed);
            }
        };
        
        queryHelper.addInputInt(feedId);
        
        executeQuery(queryHelper);
        
        if (!rssFeeds.isEmpty()) {
            return rssFeeds.get(0);
            
        } else {
            throw new ObjectNotFoundException("RSS Feed ID: " + feedId);
        }
    }

    /**
     * Adds RSS Feed.
     *
     * @return ..
     */
    public ActionMessages add(RssFeed rssFeed) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(RssQueries.insertRssFeedQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(rssFeed.getUrl());
        queryHelper.addInputStringConvertNull(rssFeed.getName());
        queryHelper.addInputInt(rssFeed.getItemCount());
        queryHelper.addInputStringConvertNull(rssFeed.getCache());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Put some values in the result
        if (errors.isEmpty()) {
            rssFeed.setId((Integer) queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    /**
     * Updates RSS Feed.
     *
     * @return ..
     */
    public ActionMessages update(RssFeed rssFeed) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(RssQueries.updateRssFeedQuery());
        queryHelper.addInputInt(rssFeed.getId());
        queryHelper.addInputStringConvertNull(rssFeed.getUrl());
        queryHelper.addInputStringConvertNull(rssFeed.getName());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    /**
     * Updates RSS Feed.
     *
     * @return ..
     */
    public ActionMessages updateContent(RssFeed rssFeed) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(RssQueries.updateRssFeedCacheQuery());
        queryHelper.addInputInt(rssFeed.getId());
        queryHelper.addInputInt(rssFeed.getItemCount());
        queryHelper.addInputStringConvertNull(rssFeed.getCache());

        return executeProcedure(queryHelper);
    }

    /**
     * Deletes RSS Feed.
     *
     * @return ..
     */
    public ActionMessages delete(Integer feedId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(RssQueries.deleteRssFeedQuery());
        queryHelper.addInputInt(feedId);

        return executeProcedure(queryHelper);
    }
}