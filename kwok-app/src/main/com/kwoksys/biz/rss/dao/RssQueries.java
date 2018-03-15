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

/**
 * RssQueries
 */
public class RssQueries {

    /**
     * Gets RSS Feeds.
     *
     * @return ..
     */
    public static String selectRssFeedListQuery() {
        return "select f.feed_id, f.feed_name, f.feed_url, f.feed_cache, f.feed_item_count from rss_feed f";
    }

    /**
     * Gets a RSS Feed.
     *
     * @return ..
     */
    public static String selectRssFeedDetailQuery() {
        return "select f.feed_id, f.feed_name, f.feed_url, f.feed_item_count, f.feed_cache, f.feed_cache_date" +
                " from rss_feed f where f.feed_id=?";
    }

    /**
     * Adds RSS feed.
     */
    public static String insertRssFeedQuery() {
        return "{call sp_rss_feed_add(?,?,?,?,?,?)}";
    }

    /**
     * Updates RSS feed.
     */
    public static String updateRssFeedQuery() {
        return "{call sp_rss_feed_update(?,?,?,?)}";
    }

    /**
     * Updates RSS feed content/cache.
     */
    public static String updateRssFeedCacheQuery() {
        return "{call sp_rss_feed_cache_update(?,?,?)}";
    }

    /**
     * Deletes RSS feed.
     */
    public static String deleteRssFeedQuery() {
        return "{call sp_rss_feed_delete(?)}";
    }
}
