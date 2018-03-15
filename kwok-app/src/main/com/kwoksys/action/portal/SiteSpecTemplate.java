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

import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * SiteSpecTemplate
 */
public class SiteSpecTemplate extends BaseTemplate {

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    private Site site;

    public SiteSpecTemplate(Site site) {
        super(SiteSpecTemplate.class);
        this.site = site;
    }

    public void init() {
        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws DatabaseException {
        DetailTableTemplate.Td td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.site_id");
        td.setValue(site.getId());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.site_name");
        td.setValue(HtmlUtils.encode(site.getName()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.site_path");
        // Use the path as title
        td.setValue(Links.getPortalSiteLink(requestContext, site).setTitle(site.getPath()).getString());

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.site_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(site.getDescription()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.site_category");
        td.setValue(HtmlUtils.encode(site.getCategoryName()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.site_placement");
        td.setValue(Localizer.getText(requestContext, Site.getSitePlacementMessageKey(site.getPlacement())));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.site_support_iframe");
        td.setValue(Localizer.getText(requestContext, Site.getSupportIframe(site.getSupportIframe())));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.creator");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, site.getCreationDate(), site.getCreator()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.modifier");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, site.getModificationDate(), site.getModifier()));
    }

    @Override
    public String getJspPath() {
        return "/jsp/portal/SiteSpecTemplate.jsp";
    }
}