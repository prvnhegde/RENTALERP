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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for showing portal site detail.
 */
public class SiteDetailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        Integer siteId = requestContext.getParameter("siteId");
        Site site = portalService.getSite(siteId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("admin.portalSiteDetail.header");

        // Link to company edit page
        if (Access.hasPermission(user, AppPaths.PORTAL_SITE_EDIT)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.PORTAL_SITE_EDIT + "?siteId=" + site.getId());
            link.setTitleKey("admin.portalSiteEdit.title");
            header.addHeaderCmds(link);
        }

        // Link to delete company page
        if (Access.hasPermission(user, AppPaths.PORTAL_SITE_DELETE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.PORTAL_SITE_DELETE + "?siteId=" + site.getId());
            link.setTitleKey("admin.portalSiteDelete.title");
            header.addHeaderCmds(link);
        }

        // Link to company list page
        if (Access.hasPermission(user, AppPaths.PORTAL_SITE_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.PORTAL_SITE_LIST);
            link.setTitleKey("admin.cmd.portalSiteList");
            header.addHeaderCmds(link);
        }

        //
        // Template: SiteSpecTemplate
        //
        standardTemplate.addTemplate(new SiteSpecTemplate(site));

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
}
