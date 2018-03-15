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
import com.kwoksys.action.common.template.TableEmptyTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyTabs;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.CompanyNote;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing company notes.
 */
public class CompanyNoteAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        AttributeManager attributeManager = new AttributeManager(requestContext);

        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(ContactQueries.getOrderByColumn(CompanyNote.CREATION_DATE), QueryCriteria.DESCENDING);
        
        List<CompanyNote> notesDataset = contactService.getCompanyNotes(queryCriteria, company.getId());
        List<Map> notes = new ArrayList<>();

        if (!notesDataset.isEmpty()) {
            for (CompanyNote companyNote : notesDataset) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", companyNote.getNoteId());
                map.put("subject", companyNote.getNoteName());
                map.put("description", HtmlUtils.formatMultiLineDisplay(companyNote.getNoteDescription()));
                map.put("type", attributeManager.getAttrFieldNameCache(Attributes.COMPANY_NOTE_TYPE, companyNote.getNoteTypeId()));
                map.put("creationDate", companyNote.getCreationDate());
                notes.add(map);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", company.getId());
        standardTemplate.setAttribute("notes", notes);
        standardTemplate.setAttribute("companyNotePath", AppPaths.CONTACTS_COMPANY_NOTE);

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        // Link to add company note page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_NOTE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_NOTE_ADD + "?companyId=" + company.getId());
            link.setTitleKey("contactMgmt.cmd.contactAddNote");
            link.setImgSrc(Image.getInstance().getNoteAddIcon());
            header.addHeaderCmds(link);
        }

        // Link to company list page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST);
            link.setTitleKey("contactMgmt.cmd.companyList");
            header.addHeaderCmds(link);
        }

        //
        // Template: TableEmptyTemplate
        //
        if (notesDataset.isEmpty()) {
            TableEmptyTemplate empty = standardTemplate.addTemplate(new TableEmptyTemplate());
            empty.setColSpan(3);
            empty.setRowText(Localizer.getText(requestContext, "contactMgmt.companyNote_emptyTableMessage"));
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(CompanyUtils.getCompanyTabs(requestContext, company));
        tabs.setTabActive(CompanyTabs.NOTES_TAB);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}