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
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareBookmark;
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
 * Action class for managing software bookmarks.
 */
public class SoftwareBookmarkAction extends Action2 {

    public String list() throws Exception {
        SoftwareBookmarkForm actionForm = getBaseForm(SoftwareBookmarkForm.class);

        AccessUser accessUser = requestContext.getUser();

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(actionForm.getSoftwareId());

        String editBookmarkText = Localizer.getText(requestContext, "form.button.edit");

        int bookmarkRowSpan = 2;

        // These are for Software Bookmarks.
        boolean canDeleteBookmark = Access.hasPermission(accessUser, AppPaths.SOFTWARE_BOOKMARK_DELETE_2);
        if (canDeleteBookmark) {
            bookmarkRowSpan++;
        }

        boolean canEditBookmark = Access.hasPermission(accessUser, AppPaths.SOFTWARE_BOOKMARK_EDIT);
        String editBookmarkPath = canEditBookmark ? AppPaths.SOFTWARE_BOOKMARK_EDIT : "";
        if (canEditBookmark) {
            bookmarkRowSpan++;
        }
        QueryCriteria query = new QueryCriteria();
        query.addSortColumn(BookmarkQueries.getOrderByColumn(Bookmark.NAME));

        List<Bookmark> bookmarks = softwareService.getSoftwareBookmarks(query, software.getId());
        List<Map> bookmarkList = new ArrayList<>();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("bookmarks", bookmarkList);
        standardTemplate.setAttribute("canDeleteBookmark", canDeleteBookmark);
        standardTemplate.setPathAttribute("deleteBookmarkPath", AppPaths.SOFTWARE_BOOKMARK_DELETE_2);
        standardTemplate.setAttribute("canEditBookmark", canEditBookmark);
        standardTemplate.setAttribute("bookmarkRowSpan", bookmarkRowSpan);

        if (!bookmarks.isEmpty()) {
            for (Bookmark bookmark : bookmarks) {
                Map<String, String> map = new HashMap<>();
                map.put("bookmarkPath", new Link(requestContext).setExternalPath(bookmark.getPath())
                        .setTitle(bookmark.getName()).setImgSrc(Image.getInstance().getExternalPopupIcon()).setImgAlignRight().getString());
                if (canEditBookmark) {
                    map.put("bookmarkEditPath", new Link(requestContext).setAjaxPath(editBookmarkPath + "?softwareId="
                            + software.getId() + "&bookmarkId=" + bookmark.getId()).setEscapeTitle(editBookmarkText).getString());
                }
                map.put("bookmarkId", String.valueOf(bookmark.getId()));
                bookmarkList.add(map);
            }
        } else {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = standardTemplate.addTemplate(new TableEmptyTemplate());
            empty.setColSpan(bookmarkRowSpan);
            empty.setRowText(Localizer.getText(requestContext, "itMgmt.softwareBookmark.emptyTableMessage"));
        }

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        SoftwareUtils.addSoftwareHeaderCommands(requestContext, header, software.getId());
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        // Add Software Bookmark.
        if (Access.hasPermission(accessUser, AppPaths.SOFTWARE_BOOKMARK_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.SOFTWARE_BOOKMARK_ADD + "?softwareId=" + software.getId());
            link.setTitleKey("bookmarkMgmt.bookmarkAdd");
            header.addHeaderCmds(link);
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(SoftwareUtils.getSoftwareTabs(requestContext, software));
        tabs.setTabActive(SoftwareUtils.BOOKMARKS_TAB);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String add() throws Exception {
        SoftwareBookmarkForm actionForm = getBaseForm(SoftwareBookmarkForm.class);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(actionForm.getSoftwareId());

        Bookmark bookmark = new Bookmark();

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setBookmark(bookmark);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.SOFTWARE_BOOKMARK_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.SOFTWARE_BOOKMARK + "?softwareId=" + software.getId()).getString());

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.softwareDetail.header", new String[] {software.getName()});

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String add2() throws Exception {
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        SoftwareBookmarkForm actionForm = saveActionForm(new SoftwareBookmarkForm());
        Software software = softwareService.getSoftware(actionForm.getSoftwareId());

        // Instantiate Software class.
        SoftwareBookmark bookmark = new SoftwareBookmark(software.getId());
        bookmark.setName(actionForm.getBookmarkName());
        bookmark.setPath(actionForm.getBookmarkPath());
        bookmark.setDescription("");

        ActionMessages errors = softwareService.addSoftwareBookmark(bookmark);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.SOFTWARE_BOOKMARK_ADD + "?softwareId=" + software.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset Software Bookmark count.
            softwareService.resetSoftwareBookmarkCount(software.getId());

            return ajaxUpdateView(AppPaths.SOFTWARE_BOOKMARK + "?softwareId=" + software.getId());
        }
    }

    public String delete2() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        softwareService.getSoftware(softwareId);

        Integer bookmarkId = requestContext.getParameter("bookmarkId");
        Bookmark bookmark = softwareService.getSoftwareBookmark(softwareId, bookmarkId);

        // Delete the bookmark
        softwareService.deleteSoftwareBookmark(bookmark);

        // Reset software bookmark count
        softwareService.resetSoftwareBookmarkCount(softwareId);

        return ajaxUpdateView(AppPaths.SOFTWARE_BOOKMARK + "?softwareId=" + softwareId);
    }
    
    public String edit() throws Exception {
        SoftwareBookmarkForm actionForm = getBaseForm(SoftwareBookmarkForm.class);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(actionForm.getSoftwareId());
        Bookmark bookmark = softwareService.getSoftwareBookmark(software.getId(), actionForm.getBookmarkId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setBookmark(bookmark);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.SOFTWARE_BOOKMARK_EDIT_2 + "?softwareId=" + software.getId() + "&bookmarkId=" + bookmark.getId());
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.SOFTWARE_BOOKMARK + "?softwareId=" + software.getId()).getString());

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        SoftwareBookmarkForm actionForm = saveActionForm(new SoftwareBookmarkForm());

        // Instantiate SoftwareBookmark class.
        Bookmark bookmark = softwareService.getSoftwareBookmark(actionForm.getSoftwareId(),
                actionForm.getBookmarkId());

        bookmark.setName(actionForm.getBookmarkName());
        bookmark.setPath(actionForm.getBookmarkPath());
        bookmark.setDescription("");

        ActionMessages errors = softwareService.updateSoftwareBookmark(bookmark);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.SOFTWARE_BOOKMARK_EDIT + "?softwareId=" + bookmark.getObjectId()
                    + "&bookmarkId=" + bookmark.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            return ajaxUpdateView(AppPaths.SOFTWARE_BOOKMARK + "?softwareId=" + bookmark.getObjectId());
        }
    }
}
