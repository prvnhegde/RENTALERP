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

import java.util.Arrays;
import java.util.List;

import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * PortalUtil
 */
public class PortalUtils {

    /**
     * Specify the column header for the list page.
     */
    public static List<String> getCategoryColumnHeaderList() {
        return Arrays.asList(BaseObject.ROWNUM, Category.CATEGORY_NAME,
                Category.CATEGORY_DESCRIPTION, Category.CATEGORY_ACTIONS);
    }

    /**
     * Speficify the sortable columns allowed.
     */
    public static List<String> getCategorySortableColumnList() {
        return Arrays.asList(Category.CATEGORY_NAME);
    }

    /**
     * Check whether a column is sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableCategoryColumn(String columnName) {
        return getCategorySortableColumnList().contains(columnName);
    }

    /**
     * This is specifically for Blog Post body.
     *
     * @param input
     * @return ..
     */
    public static String truncateBodyTextOnList(String input) {
        int limit = ConfigManager.app.getBlogsNumberOfPostCharacters();

        if (limit != 0 && input.length() > limit) {
            input = HtmlUtils.formatMultiLineDisplay(input.substring(0, limit)) + "...";
        } else {
            input = HtmlUtils.formatMultiLineDisplay(input);
        }
        return input;
    }

    /**
     * Some formatting on RSS item.
     * @param input
     * @return
     */
    public static String formatRssItem(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        input = input.replaceAll("\\<object.*?object\\>","");
        return input.replaceAll("\\<a href", "<a target=\"_blank\" href");
    }
}
