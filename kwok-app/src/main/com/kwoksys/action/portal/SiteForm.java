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

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.framework.http.RequestContext;

/**
 * ActionForm for adding/editing site.
 */
public class SiteForm extends BaseForm {

    private Integer siteId;
    private String siteName;
    private String sitePath;
    private String siteDescription;
    private int sitePlacement;
    private int siteSupportIframe;
    private Integer categoryId;

    @Override
    public void setRequest(RequestContext requestContext) {
        siteId = requestContext.getParameterInteger("siteId");
        siteName = requestContext.getParameterString("siteName");
        sitePath = requestContext.getParameterString("sitePath");
        siteDescription = requestContext.getParameterString("siteDescription");
        sitePlacement = requestContext.getParameter("sitePlacement");
        siteSupportIframe = requestContext.getParameter("siteSupportIframe");
        categoryId = requestContext.getParameterInteger("categoryId");
    }

    public void setSite(Site site) {
        siteName = site.getName();
        sitePath = site.getPath();
        siteDescription = site.getDescription();
        categoryId = site.getCategoryId();
        sitePlacement = site.getPlacement();
        siteSupportIframe = site.getSupportIframe();
    }

    public Integer getSiteId() {
        return siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getSitePath() {
        return sitePath;
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public int getSitePlacement() {
        return sitePlacement;
    }

    public int getSiteSupportIframe() {
        return siteSupportIframe;
    }

    public Integer getCategoryId() {
        return categoryId;
    }
}