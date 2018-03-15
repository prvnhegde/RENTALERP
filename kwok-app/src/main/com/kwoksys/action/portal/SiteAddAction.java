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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.WidgetUtils;

/**
 * Action class for adding site.
 */
public class SiteAddAction extends Action2 {

    public String add() throws Exception {
        Site site = new Site();

        // If not a resubmit, set some defaults
        SiteForm actionForm = getBaseForm(SiteForm.class);
        if (!actionForm.isResubmit()) {
            actionForm.setSite(site);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.PORTAL_SITE_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.PORTAL_SITE_LIST).getString());
        standardTemplate.setAttribute("optionSitePlacement", Site.getSitePlacementList(requestContext));
        standardTemplate.setAttribute("optionSiteSupportIframe", WidgetUtils.getYesNoOptions(requestContext));
        standardTemplate.setAttribute("optionCategories", Site.getSiteCategories(requestContext));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.portalSiteAdd.title");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("admin.portalSiteAdd.sectionHeader");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add2() throws Exception {
        SiteForm actionForm = saveActionForm(new SiteForm());

        Site site = new Site();
        site.setName(actionForm.getSiteName());
        site.setPath(actionForm.getSitePath());
        site.setDescription(actionForm.getSiteDescription());
        site.setPlacement(actionForm.getSitePlacement());
        site.setSupportIframe(actionForm.getSiteSupportIframe());
        site.setCategoryId(actionForm.getCategoryId());

        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        // Add the site
        ActionMessages errors = portalService.addSite(site);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.PORTAL_SITE_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
            
        } else {
            return ajaxUpdateView(AppPaths.PORTAL_SITE_DETAIL + "?siteId=" + site.getId());
        }
    }
}
