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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.dao.KbQueries;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

/**
 * Action class for KB index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        getSessionBaseForm(ArticleSearchForm.class);

        AccessUser user = requestContext.getUser();

        // Show categories.
        QueryCriteria query = new QueryCriteria();
        query.addSortColumn(KbQueries.getOrderByColumn(Category.CATEGORY_NAME));

        // This is for putting categories in selectbox
        List<LabelValueBean> categoryOptions = new ArrayList<>();
        categoryOptions.add(new SelectOneLabelValueBean(requestContext));

        KbService kbService = ServiceProvider.getKbService(requestContext);

        List<String> categories = new ArrayList<>();
        for (Category category : kbService.getCategories(query)) {
            if (category.getCountObjects() > 0) {
                categories.add(new Link(requestContext).setAjaxPath(AppPaths.KB_ARTICLE_LIST + "?cmd=search&categoryId="
                        + category.getId()).setTitle(category.getName() + " (" + category.getCountObjects() + ")").getString());
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("articleSearchPath", AppPaths.ROOT + AppPaths.KB_ARTICLE_SEARCH);
        standardTemplate.setAttribute("categories", categories);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("kb.index.header");
        header.setTitleClassNoLine();
        
        // Manage categories
        if (Access.hasPermission(user, AppPaths.KB_CATEGORY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.KB_CATEGORY_LIST);
            link.setTitleKey("kb.categoryList.header");
            header.addHeaderCmds(link);
        }

        // Add Article
        if (Access.hasPermission(user, AppPaths.KB_ARTICLE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.KB_ARTICLE_ADD);
            link.setTitleKey("kb.cmd.articleAdd");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
