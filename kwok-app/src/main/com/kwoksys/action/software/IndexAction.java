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
package com.kwoksys.action.software;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Action class for software index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();
        HttpSession session = request.getSession();

        if (requestContext.getParameterString("cmd").equals("clear")) {
            session.setAttribute(SessionManager.SOFTWARE_SEARCH_CRITERIA_MAP, null);
            clearSessionBaseForm(SoftwareSearchForm.class);
        }

        getSessionBaseForm(SoftwareSearchForm.class);

        // Link to software list.
        List<Link> links = new ArrayList<>();

        if (user.hasPermission(AppPaths.SOFTWARE_LIST)) {
            if (session.getAttribute(SessionManager.SOFTWARE_SEARCH_CRITERIA_MAP) != null) {
                links.add(new Link(requestContext).setTitleKey("common.search.showLastSearch")
                        .setAjaxPath(AppPaths.SOFTWARE_LIST));
            }

            links.add(new Link(requestContext).setAjaxPath(AppPaths.SOFTWARE_LIST + "?cmd=showAll")
                    .setTitleKey("itMgmt.index.showAllSoftware"));
        }

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Get the number of records.
        int numSoftwareRecords = softwareService.getSoftwareCount(new QueryCriteria());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("linkList", links);
        standardTemplate.setPathAttribute("lookupAction", AppPaths.SOFTWARE_DETAIL + "?softwareId=");

        //
        // Template: SoftwareSearchTemplate
        //
        SoftwareSearchTemplate searchTemplate = standardTemplate.addTemplate(new SoftwareSearchTemplate());
        searchTemplate.setFormAction(AppPaths.SOFTWARE_LIST);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.2");
        header.setTitleClassNoLine();
        header.setSectionKey("itMgmt.softwareIndex.numRecords", new Object[] {numSoftwareRecords});

        if (user.hasPermission(AppPaths.SOFTWARE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_ADD);
            link.setTitleKey("itMgmt.cmd.softwareAdd");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
