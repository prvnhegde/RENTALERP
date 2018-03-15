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
package com.kwoksys.action.contacts;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.UrlUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contacts module index page
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        getSessionBaseForm(ContactSearchForm.class);

        AccessUser user = requestContext.getUser();

        HttpSession session = request.getSession();

        // Company search.
        List<String> links = new ArrayList<>();
        String companySearchAction = "";
        if (user.hasPermission(AppPaths.CONTACTS_COMPANY_LIST)) {
            if (session.getAttribute(SessionManager.COMPANY_SEARCH_CRITERIA_MAP) != null) {
                links.add(new Link(requestContext).setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST)
                        .setTitleKey("common.search.showLastSearch").getString());
            }

            links.add(new Link(requestContext).setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST + "?cmd=showAll")
                    .setTitleKey("contactMgmt.index.companyList").getString());

            companySearchAction = AppPaths.ROOT + AppPaths.CONTACTS_COMPANY_LIST;
        }

        // Ready to pass variables to query.
        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(ContactQueries.getOrderByColumn("creation_date"), QueryCriteria.DESCENDING);
        queryCriteria.setLimit(30, 0);

        // Contact search.
        String contactSearchAction = user.hasPermission(AppPaths.CONTACTS_CONTACT_LIST) ? AppPaths.ROOT + AppPaths.CONTACTS_CONTACT_LIST : "";

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        List<String> companyTagLinks = new ArrayList<>();
        for (Map<String, String> map : contactService.getExistingCompanyTags(queryCriteria)) {
            companyTagLinks.add(new Link(requestContext).setTitle(map.get("tag_name")).setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST
                    + "?cmd=search&companyTag=" + UrlUtils.encode(map.get("tag_name"))).getString());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("customFieldsOptions", new AttributeManager(requestContext).getCustomFieldOptions(ObjectTypes.COMPANY));
        standardTemplate.setAttribute("formCompanySearchAction", companySearchAction);
        standardTemplate.setAttribute("formContactSearchAction", contactSearchAction);
        standardTemplate.setAttribute("companyTagLinks", companyTagLinks);
        standardTemplate.setAttribute("linkList", links);
        standardTemplate.setAttribute("numCompanyRecords", contactService.getCompanyCount(new QueryCriteria()));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.5");
        header.setTitleClassNoLine();
        
        // Link to add company page
        if (user.hasPermission(AppPaths.CONTACTS_COMPANY_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_ADD);
            link.setTitleKey("contactMgmt.cmd.companyAdd");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
