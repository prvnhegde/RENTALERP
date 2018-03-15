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
package com.kwoksys.action.portal;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.SiteSearch;
import com.kwoksys.biz.portal.dao.PortalQueries;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.parsers.rss.RssModel;
import com.kwoksys.framework.parsers.rss.RssModelHelper;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for showing sites.
 */
public class SiteListRssAction extends Action2 {

    public String execute() throws Exception {

        RssModel rssModel = new RssModel();
        rssModel.setTitle(Localizer.getText(requestContext, "portal.siteList.header"));
        rssModel.setLink(ConfigManager.system.getAppUrl());
        rssModel.setDescription(Localizer.getText(requestContext, "rss.description"));
        rssModel.setLanguage("en-us");

        // Create search criteria map
        SiteSearch siteSearch = new SiteSearch();
        siteSearch.put(SiteSearch.SHOW_ON_LIST, "");

        QueryCriteria query = new QueryCriteria(siteSearch);
        query.addSortColumn(PortalQueries.getOrderByColumn(Site.SITE_NAME));

        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        for (Site site : portalService.getSites(query)) {
            RssModel.Item item = rssModel.new Item();
            item.setTitle(site.getName());
            item.setLink(HtmlUtils.encode(site.getPath()));
            rssModel.addItem(item);
        }

        response.setContentType("text/xml");

        RssModelHelper helper = new RssModelHelper();
        helper.modelToXml(rssModel);

        response.getWriter().print(helper.getXmlString());
        return null;
    }
}
