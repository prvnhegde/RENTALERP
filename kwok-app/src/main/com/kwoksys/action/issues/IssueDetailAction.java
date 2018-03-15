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
package com.kwoksys.action.issues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableEmptyTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dao.IssueQueries;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SortByIconLink;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * Action class for issue detail.
 */
public class IssueDetailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        Integer issueId = requestContext.getParameter("issueId");

        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.ISSUE_HISTORY_ORDER_BY, Issue.HISTORY_CREATION_DATE);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.ISSUE_HISTORY_ORDER, QueryCriteria.DESCENDING);

        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        Issue issue = issueService.getIssue(issueId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        QueryCriteria query = new QueryCriteria();
        if (Issue.isSortableHistoryColumn(orderBy)) {
            query.addSortColumn(IssueQueries.getOrderByColumn(orderBy), order);
        }

        SortByIconLink sortByUi = new SortByIconLink(order);
        String urlSortBy = sortByUi.getUrl();

        // Get issue history
        List<Map<String, String>> changeList = issueService.getHistory(query, issueId);

        List<Map> issueHistoryList = new ArrayList<>();
        String userDetailPath = AppPaths.ADMIN_USER_DETAIL;
        boolean hasUserDetailAccess = Access.hasPermission(user, userDetailPath);
        boolean canDownloadFile = Access.hasPermission(user, AppPaths.ISSUES_FILE_DOWNLOAD);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        Link dateHeaderLink = new Link(requestContext);
        dateHeaderLink.setTitleKey("issueMgmt.issueDetail.historyDateHeader");

        if (!changeList.isEmpty()) {
            if (Issue.isSortableHistoryColumn(orderBy)) {
                dateHeaderLink.setAjaxPath(AppPaths.ISSUES_DETAIL + "?issueId=" + issueId + "&orderBy=" + Issue.HISTORY_CREATION_DATE + urlSortBy);
                dateHeaderLink.setImgSrc(sortByUi.getImg(orderBy));
            }
            String creationDate = "";
            Counter counter = new Counter();
            Map<String, String> map = new HashMap<>();
            map.put("rownum", counter.incr() + ".");

            for (Map<String, String> change : changeList) {
                if (creationDate.isEmpty()) {
                    creationDate = (String)change.get("creation_date");
                }

                // If there is a change in creation_date, that means it's a different update
                if (!creationDate.equals(change.get("creation_date"))) {
                    creationDate = (String)change.get("creation_date");
                    issueHistoryList.add(map);
                    map = new HashMap<>();
                    map.put("rownum", counter.incr() + ".");
                }

                String changeField = String.valueOf(change.get("issue_change_field"));

                /** Creator **/
                AccessUser creatorUser = new AccessUser();
                creatorUser.setId(NumberUtils.replaceNull(change.get("creator")));
                creatorUser.setUsername((String)change.get("creator_username"));
                creatorUser.setDisplayName((String)change.get("creator_display_name"));

                map.put("changeCreator", Links.getUserIconLink(requestContext, creatorUser, hasUserDetailAccess, false).getString());
                map.put("changeCreatorEmail", change.get("issue_created_from_email"));

                /** Creation date **/
                String thisCreationDate = "";
                try {
                    thisCreationDate = DatetimeUtils.toLocalDatetime(DatetimeUtils.parseDate(String.valueOf(change.get("creation_date")), ConfigManager.system.getDatetimeBase()));
                } catch (Exception e) {/* ignore */}
                map.put("changeCreationDate", thisCreationDate);

                /** Change comment **/
                if (changeField.equals("comment")) {
                    String comment = change.get("issue_comment_description") == null ? "" : change.get("issue_comment_description").toString();
                    map.put("changeComment", HtmlUtils.formatMultiLineDisplay(comment));
                }
                /** Issue subject **/
                if (changeField.equals("subject")) {
                    Object[] subjectArgs = {HtmlUtils.encode((String)change.get("issue_change_varchar_old")), HtmlUtils.encode((String)change.get("issue_change_varchar_new"))};
                    map.put("changeSubject", Localizer.getText(requestContext, "issueMgmt.issueDetail.historySubject", subjectArgs));
                }
                /** Issue type **/
                if (changeField.equals("type")) {
                    Object[] typeArgs = {HtmlUtils.encode((String)change.get("issue_change_int_old")), HtmlUtils.encode((String)change.get("issue_change_int_new"))};
                    map.put("changeType", Localizer.getText(requestContext, "issueMgmt.issueDetail.historyType", typeArgs));
                }
                /** Issue status **/
                if (changeField.equals("status")) {
                    Object[] staArgs = {HtmlUtils.encode((String)change.get("issue_change_int_old")), HtmlUtils.encode((String)change.get("issue_change_int_new"))};
                    map.put("changeStatus", Localizer.getText(requestContext, "issueMgmt.issueDetail.historyStatus", staArgs));
                }
                /** Issue priority **/
                if (changeField.equals("priority")) {
                    Object[] priArgs = {HtmlUtils.encode((String)change.get("issue_change_int_old")), HtmlUtils.encode((String)change.get("issue_change_int_new"))};
                    map.put("changePriority", Localizer.getText(requestContext, "issueMgmt.issueDetail.historyPriority", priArgs));
                }
                /** Issue resolution **/
                if (changeField.equals("resolution")) {
                    Object[] resArgs = {HtmlUtils.encode((String)change.get("issue_change_int_old")), HtmlUtils.encode((String)change.get("issue_change_int_new"))};
                    map.put("changeResolution", Localizer.getText(requestContext, "issueMgmt.issueDetail.historyResolution", resArgs));
                }
                /** Issue assignee **/
                if (changeField.equals("assignee")) {
                    AccessUser removedAssignee = new AccessUser();
                    removedAssignee.setId(NumberUtils.replaceNull(change.get("issue_change_int_old")));
                    removedAssignee.setDisplayName(StringUtils.replaceNull(change.get("issue_change_varchar_old")));
                    removedAssignee.setUsername(removedAssignee.getDisplayName());

                    String removedAssigneeName = IssueUtils.getAssigneeIconLink(requestContext, hasUserDetailAccess, removedAssignee);

                    AccessUser addedAssignee = new AccessUser();
                    addedAssignee.setId(NumberUtils.replaceNull(change.get("issue_change_int_new")));
                    addedAssignee.setDisplayName(StringUtils.replaceNull(change.get("issue_change_varchar_new")));
                    addedAssignee.setUsername(addedAssignee.getDisplayName());

                    String addedAssigneeName = IssueUtils.getAssigneeIconLink(requestContext, hasUserDetailAccess, addedAssignee);

                    Object[] assignArgs = new Object[]{removedAssigneeName, addedAssigneeName};
                    map.put("changeAssignee", Localizer.getText(requestContext, "issueMgmt.issueDetail.historyAssignee", assignArgs));
                }

                /** Attached files **/
                if (changeField.equals("file")) {
                    String path = AppPaths.ISSUES_FILE_DOWNLOAD + "?issueId=" + issueId + "&fileId=" + change.get("file_id");
                    String fileIconLink = Links.getFileIconLink(requestContext, canDownloadFile, change.get("file_name").toString(), path).getString();

                    String[] params = {StringUtils.replaceNull(change.get("file_friendly_name")), fileIconLink, FileUtils.formatFileSize(requestContext, NumberUtils.replaceNull(change.get("file_byte_size")))};
                    map.put("changeFile", Localizer.getText(requestContext, "issueMgmt.issueDetail.historyDownloadFile", params));
                }
            }
            if (!map.isEmpty()) {
                // If there is only one update, there won't be any creation different, so, check map here
                issueHistoryList.add(map);
            }
        } else {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = standardTemplate.addTemplate(new TableEmptyTemplate());
            empty.setColSpan(4);
            empty.setRowText(Localizer.getText(requestContext, "issueMgmt.issueDetail.emptyTableMessage"));
        }

        request.setAttribute("issueHistoryList", issueHistoryList);
        request.setAttribute("issueChangeCreationDateHeader", dateHeaderLink.getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("issueMgmt.issueDetail.title", new Object[] {issue.getSubject()});

        // Edit Issue.
        if (Access.hasPermission(user, AppPaths.ISSUES_EDIT)) {
            header.addHeaderCmds(new Link(requestContext).setAjaxPath(AppPaths.ISSUES_EDIT + "?issueId=" + issueId)
                    .setTitleKey("issueMgmt.cmd.issueEdit"));
        }

        // Add Issue File.
        if (Access.hasPermission(user, AppPaths.ISSUES_FILE_ADD)) {
            Link link = new Link(requestContext);
            link.setTitleKey("issueMgmt.cmd.issueFileAdd");
            if (fileService.isDirectoryExist(ConfigManager.file.getIssueFileRepositoryLocation())) {
                link.setAjaxPath(AppPaths.ISSUES_FILE_ADD + "?issueId=" + issueId);
                link.setImgSrc(Image.getInstance().getFileAddIcon());
            } else {
                link.setImgAltKey("files.warning.invalidPath");
                link.setImgSrc(Image.getInstance().getSignWarning());
            }
            header.addHeaderCmds(link);
        }

        // Delete Issue.
        if (Access.hasPermission(user, AppPaths.ISSUES_DELETE)) {
            header.addHeaderCmds(new Link(requestContext).setAjaxPath(AppPaths.ISSUES_DELETE + "?issueId=" + issueId)
                    .setTitleKey("issueMgmt.cmd.issueDelete"));
        }

        // Back to Issue list.
        if (Access.hasPermission(user, AppPaths.ISSUES_LIST)) {
            header.addHeaderCmds(new Link(requestContext).setAjaxPath(AppPaths.ISSUES_LIST).setTitleKey("issueMgmt.cmd.issueList"));
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(IssueUtils.getIssueTabs(requestContext, issue, null));
        tabs.setTabActive(IssueUtils.ISSUE_TAB_HISTORY);

        //
        // Template: IssueSpecTemplate
        //
        IssueSpecTemplate spec = standardTemplate.addTemplate(new IssueSpecTemplate(issue));
        spec.setHeaderText(issue.getSubject());

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.ISSUE);
        customFieldsTemplate.setObjectId(issueId);
        customFieldsTemplate.setObjectAttrTypeId(issue.getType());
        customFieldsTemplate.setShowDefaultHeader(false);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
