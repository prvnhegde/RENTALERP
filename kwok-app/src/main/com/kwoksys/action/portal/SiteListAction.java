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

import java.util.ArrayList;
import java.util.List;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RecordsNavigationTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.dao.PortalQueries;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for getting site list.
 */
public class SiteListAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        String cmd = requestContext.getParameterString("cmd");
        String rowCmd = requestContext.getParameterString("rowCmd");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.SITES_ORDER_BY, Site.SITE_NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.SITES_ORDER, QueryCriteria.ASCENDING);

        int rowStart = 0;
        if (!cmd.isEmpty() || rowCmd.equals("showAll")) {
            request.getSession().setAttribute(SessionManager.SITES_ROW_START, rowStart);
        } else {
            rowStart = SessionManager.getOrSetAttribute(requestContext, "rowStart", SessionManager.SITES_ROW_START, rowStart);
        }

        int rowLimit = requestContext.getParameter("rowLimit", ConfigManager.app.getPortalNumberOfSitesToShow());
        if (rowCmd.equals("showAll")) {
            rowLimit = 0;
        }

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria();
        query.setLimit(rowLimit, rowStart);

        if (Site.isSortableColumn(orderBy)) {
            query.addSortColumn(PortalQueries.getOrderByColumn(orderBy), order);
        }

        // Get column headers
        List<String> columnHeaders = Site.getColumnHeader();

        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.portalSiteList.header");
        header.setTitleClassNoLine();

        // Link to add Portal Site
        if (Access.hasPermission(user, AppPaths.PORTAL_SITE_ADD)) {
            header.addHeaderCmds(new Link(requestContext).setAjaxPath(AppPaths.PORTAL_SITE_ADD)
                    .setTitleKey("admin.portalSiteAdd.title"));
        }

        //
        // Template: RecordsNavigationTemplate
        //
        int rowCount = portalService.getSiteCount(query);

        RecordsNavigationTemplate nav = standardTemplate.addTemplate(new RecordsNavigationTemplate());
        nav.setInfoText("");
        nav.setRowOffset(rowStart);
        nav.setRowLimit(rowLimit);
        nav.setRowCount(rowCount);
        nav.setRowCountMsgkey("core.template.recordsNav.rownum");
        nav.setShowAllRecordsText(Localizer.getText(requestContext, "admin.portalSiteList.rowCount", new Object[]{rowCount}));
        nav.setShowAllRecordsPath(AppPaths.PORTAL_SITE_LIST + "?rowCmd=showAll");
        nav.setPath(AppPaths.PORTAL_SITE_LIST + "?rowStart=");

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setSortableColumnHeaders(Site.getSortableColumnList());
        tableTemplate.setColumnPath(AppPaths.PORTAL_SITE_LIST);
        tableTemplate.setColumnTextKey("common.column.");
        tableTemplate.setRowCmd(rowCmd);
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("admin.portalSiteList.emptyTableMessage");

        // Loop through the Portal Site list.
        List<Site> sites = portalService.getSites(query);
        if (!sites.isEmpty()) {
            Counter counter = new Counter(rowStart);

            for (Site site : sites) {
                List<String> columns = new ArrayList<>();

                for (String column : columnHeaders) {
                    if (column.equals(Site.SITE_NAME)) {
                        Link link = new Link(requestContext)
                                .setAjaxPath(AppPaths.PORTAL_SITE_DETAIL + "?siteId=" + site.getId())
                                .setTitle(site.getName());
                        columns.add(link.getString());

                    } else if (column.equals(Site.PLACEMENT)) {
                        columns.add(Localizer.getText(requestContext,
                                Site.getSitePlacementMessageKey(site.getPlacement())));

                    } else if (column.equals(Site.SUPPORT_IFRAME)) {
                        columns.add(Localizer.getText(requestContext,
                                Site.getSupportIframe(site.getSupportIframe())));

                    } else if (column.equals(Site.ROWNUM)) {
                        columns.add(String.valueOf(counter.incr()));

                    } else if (column.equals(Site.SITE_PATH)) {
                        columns.add(HtmlUtils.encode(site.getPath()));
                    }
                }
                tableTemplate.addRow(columns);
            }
        }

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
}
