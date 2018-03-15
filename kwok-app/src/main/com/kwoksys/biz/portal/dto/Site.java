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
package com.kwoksys.biz.portal.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.portal.dao.PortalQueries;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;

/**
 * Portal Site Object.
 */
public class Site extends BaseObject {

    public static final String SITE_NAME = "site_name";
    public static final String SITE_PATH = "site_path";
    public static final String PLACEMENT = "site_placement";
    public static final String SUPPORT_IFRAME = "site_support_iframe";
    public static final String CATEGORY_NAME = "category_name";

    public static final int PLACEMENT_HIDDEN = 0;
    public static final int PLACEMENT_LIST = 1;
    public static final int PLACEMENT_TAB = 2;
    public static final int PLACEMENT_LIST_AND_TAB = 3;

    private Integer id;
    private String name;
    private String path;
    private String description;
    private int placement = 1;
    private int isSupportIframe = 0;
    private Integer categoryId = 0;
    private String categoryName;

    /**
     * Return column headers for list view.
     * I really want to put this in database table as a custom attribute.
     *
     * @return ..
     */
    public static List<String> getColumnHeader() {
        return Arrays.asList(ConfigManager.app.getPortalColumns());
    }

    /**
     * Specifies the sortable Company columns allowed.
     */
    public static List<String> getSortableColumnList() {
        return Arrays.asList("site_name", "site_path", "site_placement", "site_support_iframe");

    }

    /**
     * Check whether a column is sortable.
     * Return true if the given column is sortable.
     * Return false if the given column is not sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableColumn(String columnName) {
        return getSortableColumnList().contains(columnName);
    }

    /**
     * Return the size of column header, useful for colSpan.
     *
     * @return ..
     */
    public static int getColumnHeaderCount() {
        return getColumnHeader().size();
    }

    /**
     * This returns message key of site visibility
     */
    public static String getSitePlacementMessageKey(int input) {
        switch (input) {
            case PLACEMENT_HIDDEN:
                return "portalSite.placementHidden";
            case PLACEMENT_LIST:
                return "portalSite.placementList";
            case PLACEMENT_TAB:
                return "portalSite.placementTab";
            case PLACEMENT_LIST_AND_TAB:
                return "portalSite.placementListAndTab";
        }
        return null;
    }

    /**
     * App Public in LabelValueBean.
     */
    public static List<LabelValueBean> getSitePlacementList(RequestContext requestContext) {
        return Arrays.asList(
                new LabelValueBean(Localizer.getText(requestContext, "portalSite.placementHidden"), "0"),
                new LabelValueBean(Localizer.getText(requestContext, "portalSite.placementList"), "1"),
                new LabelValueBean(Localizer.getText(requestContext, "portalSite.placementTab"), "2"),
                new LabelValueBean(Localizer.getText(requestContext, "portalSite.placementListAndTab"), "3"));
    }

    /**
     * Site category options.
     */
    public static List<LabelValueBean> getSiteCategories(RequestContext requestContext) throws DatabaseException {
        QueryCriteria query = new QueryCriteria();
        query.addSortColumn(PortalQueries.getOrderByColumn(Category.CATEGORY_NAME));

        List<LabelValueBean> categoryOptions = new ArrayList<>();

        PortalService portalService = ServiceProvider.getPortalService(requestContext);

        for (Category category : portalService.getCategories(query)) {
            categoryOptions.add(new LabelValueBean(category.getName(),
                    String.valueOf(category.getId())));
        }
        return categoryOptions;
    }

    /**
     * This returns message key of Iframe support
     */
    public static String getSupportIframe(int input) {
        if (input == 0) {
            return "common.boolean.yes_no.false";
        } else {
            return "common.boolean.yes_no.true";
        }
    }

    //
    // Getter and Setter
    //
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getPlacement() {
        return placement;
    }
    public void setPlacement(int placement) {
        this.placement = placement;
    }
    public int getSupportIframe() {
        return isSupportIframe;
    }
    public void setSupportIframe(int supportIframe) {
        this.isSupportIframe = supportIframe;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
