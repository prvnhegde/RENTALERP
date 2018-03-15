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
package com.kwoksys.biz.portal;

import com.kwoksys.biz.portal.dao.*;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.biz.system.dao.CategoryDao;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.CacheManager;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.List;

/**
 * PortalService
 */
public class PortalService {

    private RequestContext requestContext;

    public PortalService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public List<Site> getSites(QueryCriteria query) throws DatabaseException {
        return new SiteDao(requestContext).getSiteList(query);
    }

    public int getSiteCount(QueryCriteria query) throws DatabaseException {
        return new SiteDao(requestContext).getCount(query);
    }

    public Site getSite(Integer siteId) throws DatabaseException, ObjectNotFoundException {
        return new SiteDao(requestContext).getSite(siteId);
    }

    public ActionMessages addSite(Site site) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (site.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.site_name")));
        }
        if (site.getPath().isEmpty()) {
            errors.add("emptyPath", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.site_path")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        errors = new SiteDao(requestContext).add(site);
        if (errors.isEmpty()) {
            new CacheManager(requestContext).removeModuleTabsCache();
        }
        return errors;
    }

    public ActionMessages updateSite(Site site) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (site.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.site_name")));
        }
        if (site.getPath().isEmpty()) {
            errors.add("emptyPath", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.site_path")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        errors = new SiteDao(requestContext).update(site);

        if (errors.isEmpty()) {
            new CacheManager(requestContext).removeModuleTabsCache();
        }
        return errors;
    }

    public ActionMessages deleteSite(Integer siteId) throws DatabaseException {
        ActionMessages errors = new SiteDao(requestContext).delete(siteId);
        if (errors.isEmpty()) {
            new CacheManager(requestContext).removeModuleTabsCache();
        }
        return errors;
    }

    /**
     * Gets Site categories.
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<Category> getCategories(QueryCriteria query) throws DatabaseException {
        return new CategoryDao(requestContext).getCategoryList(query, ObjectTypes.PORTAL_SITE);
    }

    public Category getCategory(Integer categoryId) throws DatabaseException, ObjectNotFoundException {
        return new CategoryDao(requestContext).getCategory(categoryId, ObjectTypes.PORTAL_SITE);
    }

    /**
     * Adds Site category
     * @param category
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addCategory(Category category) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (category.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("blogs.categoryEdit.error.emptyName"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new CategoryDao(requestContext).addCategory(category);
    }

    /**
     * Edits Site category
     * @param category
     * @return
     * @throws DatabaseException
     */
    public ActionMessages editCategory(Category category) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (category.getName().isEmpty()) {
            errors.add("emptyName", new ActionMessage("blogs.categoryEdit.error.emptyName"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new CategoryDao(requestContext).editCategory(category);
    }
}
