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
package com.kwoksys.biz.system.dao;

import com.kwoksys.framework.connections.database.QueryCriteria;

/**
 * CategoryQueries.
 */
public class CategoryQueries {
    /**
     * Return categories.
     */
    public static String selectCategoryListQuery(QueryCriteria query) {
        return "select c.category_id, c.category_name, c.category_description, c.object_count, c.object_type_id," +
                " c.creator, c.creator_username, c.creator_display_name, c.creation_date " +
                "from category_view c " +
                "where c.object_type_id = ? "
                + query.createAndClause();
    }

    /**
     * Return categories.
     */
    public static String selectCategoryDetailQuery() {
        return "select c.category_id, c.category_name, c.category_description, c.object_type_id " +
                "from category_view c " +
                "where c.category_id = ? " +
                "and c.object_type_id = ?";
    }

    /**
     * Add Category.
     */
    public static String insertCategoryQuery() {
        return "{call sp_category_add(?,?,?,?,?)}";
    }

    /**
     * Update Category.
     */
    public static String updateCategoryQuery() {
        return "{call sp_category_update(?,?,?,?,?)}";
    }
}