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
package com.kwoksys.biz.portal;

import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.framework.connections.database.QueryCriteria;

/**
 * This is for building search queries.
 */
public class SiteSearch extends BaseSearch {

    public static final String SHOW_ON_LIST = "shownOnList";
    public static final String SHOW_ON_TAB = "shownOnTab";

    public void applyMap(QueryCriteria query) {
        if (searchCriteriaMap == null) {
            return;
        }
        // For showing sites that are public/visible
        if (searchCriteriaMap.containsKey(SHOW_ON_LIST)) {
            query.appendWhereClause("s.site_placement in (" + Site.PLACEMENT_LIST + "," + Site.PLACEMENT_LIST_AND_TAB +
                    ")");
        }
        // For showing sites that are shown as modele tabs
        if (searchCriteriaMap.containsKey(SHOW_ON_TAB)) {
            query.appendWhereClause("s.site_placement in (" + Site.PLACEMENT_TAB + "," + Site.PLACEMENT_LIST_AND_TAB +
                    ")");
        }
    }
}
