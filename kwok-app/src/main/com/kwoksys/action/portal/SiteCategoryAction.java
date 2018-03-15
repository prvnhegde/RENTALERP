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

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.PortalUtils;
import com.kwoksys.biz.portal.dao.PortalQueries;
import com.kwoksys.biz.portal.dto.SiteCategory;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for managing category list.
 */
public class SiteCategoryAction extends Action2 {

    public String list() throws Exception {
        AccessUser user = requestContext.getUser();

        // Initialize rownum.
        Counter counter = new Counter();

        // Get column headers
        List<String> columnHeaders = PortalUtils.getCategoryColumnHeaderList();

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria();
        if (PortalUtils.isSortableCategoryColumn(Category.CATEGORY_NAME)) {
            query.addSortColumn(PortalQueries.getOrderByColumn(Category.CATEGORY_NAME));
        }

        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        List<Category> categories = portalService.getCategories(query);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("portal.siteCategoryList.header");
        header.setTitleClassNoLine();

        // Link to add category.
        if (user.hasPermission(AppPaths.PORTAL_CATEGORY_ADD)) {
            header.addHeaderCmds(new Link(requestContext).setAjaxPath(AppPaths.PORTAL_CATEGORY_ADD)
                    .setTitleKey("portal.siteCategoryAdd.header"));
        }

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnPath(AppPaths.PORTAL_CATEGORY_LIST);
        tableTemplate.setColumnTextKey("common.column.");

        if (!categories.isEmpty()) {
            boolean canEditCategory = user.hasPermission(AppPaths.PORTAL_CATEGORY_EDIT);

            for (Category category : categories) {
                List<String> columns = new ArrayList<>();

                for (String column : columnHeaders) {
                    if (column.equals(Category.CATEGORY_NAME)) {
                        columns.add(HtmlUtils.encode(category.getName()));

                    } else if (column.equals(Category.CATEGORY_DESCRIPTION)) {
                        columns.add(HtmlUtils.formatMultiLineDisplay(category.getDescription()));

                    } else if (column.equals(Category.CATEGORY_ACTIONS)) {
                        if (canEditCategory) {
                            Link link = new Link(requestContext)
                                    .setTitleKey("portal.cmd.categoryEdit")
                                    .setAjaxPath(AppPaths.PORTAL_CATEGORY_EDIT + "?categoryId=" + category.getId());
                            columns.add(link.getString());
                        } else {
                            columns.add("");
                        }
                    } else if (column.equals(BaseObject.ROWNUM)) {
                        columns.add(counter.incr() + ".");
                    }
                }
                tableTemplate.addRow(columns);
            }
        }

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add() throws Exception {
        Category category = new Category();

        // If not a resubmit, set some defaults
        SiteCategoryForm actionForm = getBaseForm(SiteCategoryForm.class);
        if (!actionForm.isResubmit()) {
            actionForm.setCategoryName(category.getName());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.PORTAL_CATEGORY_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.PORTAL_CATEGORY_LIST).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("portal.siteCategoryAdd.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String add2() throws Exception {
        SiteCategoryForm actionForm = saveActionForm(new SiteCategoryForm());

        SiteCategory category = new SiteCategory();
        category.setName(actionForm.getCategoryName());

        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        ActionMessages errors = portalService.addCategory(category);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.PORTAL_CATEGORY_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.PORTAL_CATEGORY_LIST);
        }
    }

    public String edit() throws Exception {
        SiteCategoryForm actionForm = getBaseForm(SiteCategoryForm.class);
        Integer categoryId = actionForm.getCategoryId();

        PortalService portalService = ServiceProvider.getPortalService(requestContext);
        Category category = portalService.getCategory(categoryId);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setCategoryName(category.getName());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("categoryId", categoryId);
        standardTemplate.setPathAttribute("formAction", AppPaths.PORTAL_CATEGORY_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.PORTAL_CATEGORY_LIST).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("portal.siteCategoryEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        SiteCategoryForm actionForm = saveActionForm(new SiteCategoryForm());
        Integer categoryId = actionForm.getCategoryId();

        Category category = portalService.getCategory(categoryId);
        category.setName(actionForm.getCategoryName());

        ActionMessages errors = portalService.editCategory(category);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.PORTAL_CATEGORY_EDIT + "?categoryId=" + categoryId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.PORTAL_CATEGORY_LIST);
        }
    }
}
