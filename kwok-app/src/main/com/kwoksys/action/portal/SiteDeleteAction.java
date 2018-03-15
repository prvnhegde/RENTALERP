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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting site.
 */
public class SiteDeleteAction extends Action2 {

    public String delete() throws Exception {
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
        header.setPageTitleKey("admin.portalSiteDelete.title");

        //
        // Template: SiteSpecTemplate
        //
        standardTemplate.addTemplate(new SiteSpecTemplate(site));

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = standardTemplate.addTemplate(new ObjectDeleteTemplate());
        delete.setFormAjaxAction(AppPaths.PORTAL_SITE_DELETE_2 + "?siteId=" + siteId);
        delete.setFormCancelAction(AppPaths.PORTAL_SITE_DETAIL + "?siteId=" + siteId);
        delete.setConfirmationMsgKey("admin.portalSiteDelete.confirm");
        delete.setSubmitButtonKey("admin.portalSiteDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String delete2() throws Exception {
        Integer siteId = requestContext.getParameter("siteId");

        PortalService portalService = ServiceProvider.getPortalService(requestContext);
        portalService.getSite(siteId);

        ActionMessages errors = portalService.deleteSite(siteId);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.PORTAL_SITE_DELETE + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&siteId=" + siteId);
            
        } else {
            return ajaxUpdateView(AppPaths.PORTAL_SITE_LIST);
        }
    }
}
