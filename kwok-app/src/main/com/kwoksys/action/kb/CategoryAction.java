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
package com.kwoksys.action.kb;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dao.KbQueries;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.kb.dto.KBCategory;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;

/**
 * Action class for managing categories.
 */
public class CategoryAction extends Action2 {

    public String list() throws Exception {
        AccessUser user = requestContext.getUser();

        // Get column headers
        List<String> columnHeaders = KbUtils.getCategoryColumnHeaderList();

        // Ready to pass variables to query.
        QueryCriteria query = new QueryCriteria();
        if (KbUtils.isSortableCategoryColumn(Category.CATEGORY_NAME)) {
            query.addSortColumn(KbQueries.getOrderByColumn(Category.CATEGORY_NAME));
        }

        KbService kbService = ServiceProvider.getKbService(requestContext);

        List<Category> categoryDataset = kbService.getCategories(query);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("kb.categoryList.header");
        header.setTitleClassNoLine();
        
        // Link to add category.
        if (user.hasPermission(AppPaths.KB_CATEGORY_ADD)) {
            header.addHeaderCmds(new Link(requestContext).setTitleKey("kb.cmd.categoryAdd")
                    .setAjaxPath(AppPaths.KB_CATEGORY_ADD));
        }

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnPath(AppPaths.KB_CATEGORY_LIST);
        tableTemplate.setColumnTextKey("common.column.");

        if (!categoryDataset.isEmpty()) {
            boolean canEditCategory = user.hasPermission(AppPaths.KB_CATEGORY_EDIT);
            Counter counter = new Counter();

            for (Category category : categoryDataset) {
                List<String> columns = new ArrayList<>();

                for (String column : columnHeaders) {
                    if (column.equals(Category.CATEGORY_NAME)) {
                        columns.add(HtmlUtils.encode(category.getName()));

                    } else if (column.equals(Category.CATEGORY_DESCRIPTION)) {
                        columns.add(HtmlUtils.formatMultiLineDisplay(category.getDescription()));

                    } else if (column.equals(Article.ARTICLE_OBJECT_COUNT)) {
                        columns.add(String.valueOf(category.getCountObjects()));

                    } else if (column.equals(Category.CATEGORY_ACTIONS)) {
                        if (canEditCategory) {
                            Link link = new Link(requestContext)
                                    .setAjaxPath(AppPaths.KB_CATEGORY_EDIT + "?categoryId=" + category.getId())
                                    .setTitleKey("kb.cmd.categoryEdit");
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
        CategoryForm actionForm = getBaseForm(CategoryForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setCategory(category);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.KB_CATEGORY_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.KB_CATEGORY_LIST).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("kb.categoryAdd.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String add2() throws Exception {
        CategoryForm actionForm = saveActionForm(new CategoryForm());

        KBCategory category = new KBCategory();
        category.setName(actionForm.getCategoryName());

        KbService kbService = ServiceProvider.getKbService(requestContext);

        ActionMessages errors = kbService.addCategory(category);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.KB_CATEGORY_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.KB_CATEGORY_LIST);
        }
    }
    
    public String edit() throws Exception {
        CategoryForm actionForm = getBaseForm(CategoryForm.class);

        KbService kbService = ServiceProvider.getKbService(requestContext);
        Category category = kbService.getCategory(actionForm.getCategoryId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setCategory(category);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.KB_CATEGORY_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.KB_CATEGORY_LIST).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("kb.categoryEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        // Get request parameters
        CategoryForm actionForm = saveActionForm(new CategoryForm());
        Integer categoryId = actionForm.getCategoryId();

        KbService kbService = ServiceProvider.getKbService(requestContext);

        Category category = kbService.getCategory(categoryId);
        category.setName(actionForm.getCategoryName());

        ActionMessages errors = kbService.updateCategory(category);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.KB_CATEGORY_EDIT + "?categoryId=" + category.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.KB_CATEGORY_LIST);
        }
    }
}
