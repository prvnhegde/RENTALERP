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
package com.kwoksys.biz.rss.dto;

import com.kwoksys.framework.parsers.rss.RssModel;
import com.kwoksys.biz.base.BaseObject;

import java.util.Date;

/**
 * RssFeed
 */
public class RssFeed extends BaseObject {

    private Integer id;
    private String name;
    private String url;
    private String cache;
    private Date cacheDate;
    private int itemCount;
    private RssModel model;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCache() {
        return cache;
    }
    public void setCache(String cache) {
        this.cache = cache;
    }
    public int getItemCount() {
        return itemCount;
    }
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    public Date getCacheDate() {
        return cacheDate;
    }
    public void setCacheDate(Date cacheDate) {
        this.cacheDate = cacheDate;
    }
    public RssModel getModel() {
        return model;
    }
    public void setModel(RssModel model) {
        this.model = model;
    }
}
