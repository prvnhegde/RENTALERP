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
package com.kwoksys.biz.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;

/**
 * BaseSearch
 */
public abstract class BaseSearch {

    protected Map<String, Object> searchCriteriaMap;
    protected RequestContext requestContext;
    private HttpSession session;
    private String sessionKey;

    public BaseSearch(RequestContext requestContext, String sessionKey) {
        this.requestContext = requestContext;
        this.session =  requestContext.getSession();
        this.sessionKey = sessionKey;

        searchCriteriaMap = (Map) session.getAttribute(sessionKey);
        if (searchCriteriaMap == null) {
            searchCriteriaMap = new HashMap<>();
            session.setAttribute(sessionKey, searchCriteriaMap);
        }
    }

    public BaseSearch() {
        searchCriteriaMap = new HashMap<>();
    }

    public void reset() {
        searchCriteriaMap = new HashMap<>();
        session.setAttribute(sessionKey, searchCriteriaMap);
    }

    public abstract void applyMap(QueryCriteria query);

    public void put(String key, Object value) {
        searchCriteriaMap.put(key, value);
    }

    public Map getSearchCriteriaMap() {
        return searchCriteriaMap;
    }
    public void setSearchCriteriaMap(Map<String, Object> searchCriteriaMap) {
        this.searchCriteriaMap = searchCriteriaMap;
    }
}
