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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
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
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.CompanyBookmark;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dao.BookmarkQueries;
import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for showing company bookmark.
 */
public class CompanyBookmarkAction extends Action2 {

    public String index() throws Exception {
        AccessUser user = requestContext.getUser();

        // Bookmarks
        int bookmarkRowSpan = 2;

        CompanyBookmarkForm actionForm = getBaseForm(CompanyBookmarkForm.class);

        // Call the service
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        Company company = contactService.getCompany(actionForm.getCompanyId());

        // These are for Company Bookmarks.
        boolean canDeleteBookmark = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_BOOKMARK_DELETE_2);
        String deleteBookmarkPath = AppPaths.CONTACTS_COMPANY_BOOKMARK_DELETE_2;
        if (canDeleteBookmark) {
            bookmarkRowSpan++;
        }

        boolean canEditBookmark = Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_BOOKMARK_EDIT);
        String editBookmarkPath = AppPaths.CONTACTS_COMPANY_BOOKMARK_EDIT;
        if (canEditBookmark) {
            bookmarkRowSpan++;
        }

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(BookmarkQueries.getOrderByColumn(Bookmark.NAME));

        List<Bookmark> bookmarks = contactService.getCompanyBookmarks(queryCriteria, company.getId());
        List<Map> bookmarkList = new ArrayList<>();

        if (!bookmarks.isEmpty()) {
            for (Bookmark bookmark : bookmarks) {
                Map<String, String> map = new HashMap<>();
                map.put("bookmarkPath", new Link(requestContext).setExternalPath(bookmark.getPath()).setTitle(bookmark.getName()).setImgSrc(Image.getInstance().getExternalPopupIcon()).setImgAlignRight().getString());
                if (canEditBookmark) {
                    map.put("bookmarkEditPath", new Link(requestContext).setAjaxPath(editBookmarkPath + "?companyId=" + company.getId() + "&bookmarkId=" + bookmark.getId()).setTitleKey("form.button.edit").getString());
                }
                map.put("bookmarkId", String.valueOf(bookmark.getId()));
                bookmarkList.add(map);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", company.getId());
        standardTemplate.setAttribute("canDeleteBookmark", canDeleteBookmark);
        standardTemplate.setPathAttribute("deleteBookmarkPath", deleteBookmarkPath);
        standardTemplate.setAttribute("canEditBookmark", canEditBookmark);
        standardTemplate.setAttribute("bookmarkRowSpan", bookmarkRowSpan);
        standardTemplate.setAttribute("bookmarkList", bookmarkList);

        if (bookmarks.isEmpty()) {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = standardTemplate.addTemplate(new TableEmptyTemplate());
            empty.setColSpan(bookmarkRowSpan);
            empty.setRowText(Localizer.getText(requestContext, "contactMgmt.companyBookmark_emptyTableMessage"));
        }

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        // Link to add company bookmarks page
        if (Access.hasPermission(user, AppPaths.CONTACTS_COMPANY_BOOKMARK_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_BOOKMARK_ADD + "?companyId=" + company.getId());
            link.setTitleKey("bookmarkMgmt.bookmarkAdd");
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
        //  Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(CompanyUtils.getCompanyTabs(requestContext, company));
        tabs.setTabActive(CompanyTabs.BOOKMAKRS_TAB);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add() throws Exception {
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        CompanyBookmarkForm actionForm = getBaseForm(CompanyBookmarkForm.class);

        // Check whether the object exists
        Company company = contactService.getCompany(actionForm.getCompanyId());

        Bookmark bookmark = new Bookmark();

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setBookmark(bookmark);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", company.getId());
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTACTS_COMPANY_BOOKMARK_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTACTS_COMPANY_BOOKMARK + "?companyId=" + company.getId()).getString());

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add2() throws Exception {
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        CompanyBookmarkForm actionForm = saveActionForm(new CompanyBookmarkForm());

        Integer companyId = actionForm.getCompanyId();

        // Check whether the object exists
        contactService.getCompany(companyId);

        // Instantiate CompanyBookmarkDAO class
        CompanyBookmark bookmark = new CompanyBookmark(companyId);
        bookmark.setName(actionForm.getBookmarkName());
        bookmark.setPath(actionForm.getBookmarkPath());
        bookmark.setDescription("");

        ActionMessages errors = contactService.addCompanyBookmark(bookmark);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_BOOKMARK_ADD + "?companyId=" + companyId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset company bookmark count.
            contactService.resetCompanyBookmarkCount(companyId);

            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_BOOKMARK + "?companyId=" + companyId);
        }
    }

    public String delete2() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");
        Integer bookmarkId = requestContext.getParameter("bookmarkId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        contactService.getCompany(companyId);

        Bookmark bookmark = contactService.getCompanyBookmark(companyId, bookmarkId);

        // Delete the bookmark
        contactService.deleteCompanyBookmark(bookmark);

        // Reset company bookmark count
        contactService.resetCompanyBookmarkCount(companyId);

        return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_BOOKMARK + "?companyId=" + companyId);
    }
    
    public String edit() throws Exception {
        CompanyBookmarkForm actionForm = getBaseForm(CompanyBookmarkForm.class);

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        Integer companyId = actionForm.getCompanyId();
        Company company = contactService.getCompany(companyId);

        Integer bookmarkId = actionForm.getBookmarkId();
        Bookmark bookmark = contactService.getCompanyBookmark(companyId, bookmarkId);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setBookmark(bookmark);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", companyId);
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTACTS_COMPANY_BOOKMARK_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTACTS_COMPANY_BOOKMARK + "?companyId=" + companyId).getString());
        standardTemplate.setAttribute("bookmarkId", bookmarkId);

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        CompanyBookmarkForm actionForm = saveActionForm(new CompanyBookmarkForm());
        Integer companyId = actionForm.getCompanyId();

        // Check whether the object exists
        contactService.getCompany(companyId);

        // Instantiate Bookmark class.
        Bookmark bookmark = contactService.getCompanyBookmark(companyId, actionForm.getBookmarkId());
        bookmark.setName(actionForm.getBookmarkName());
        bookmark.setPath(actionForm.getBookmarkPath());
        bookmark.setDescription("");

        // Update the bookmark
        ActionMessages errors = contactService.updateCompanyBookmark(bookmark);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_BOOKMARK_EDIT + "?companyId=" + companyId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_BOOKMARK + "?companyId=" + companyId);
        }
    }    
}
