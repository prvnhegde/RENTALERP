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
 * Queries for Bookmark package.
 * Format: select_, insert_, update_, delete_, _Query
 */
public class BookmarkQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals("bookmark_name")) {
            return "lower(b.bookmark_name)";
        } else {
            return column;
        }
    }

    /**
     * Return all Bookmarks associated with a specific objects.
     */
    public static String selectBookmarkListQuery(QueryCriteria query) {
        return "select b.bookmark_id, b.bookmark_name, b.bookmark_description, b.bookmark_path " +
                "from bookmark_view b " +
                "where b.object_type_id = ? " +
                "and b.object_id = ? " + query.createAndClause();
    }

    /**
     * Return a specific Bookmark associated with a specific objects.
     */
    public static String selectBookmarkDetailQuery() {
        return "select bookmark_id, bookmark_name, bookmark_description, bookmark_path " +
                "from bookmark_view " +
                "where object_type_id = ? " +
                "and object_id = ? " +
                "and bookmark_id = ?";
    }


    /**
     * Add a Bookmark.
     *
     * @return ..
     */
    public static String insertBookmarkQuery() {
        return "{call sp_bookmark_add(?,?,?,?,?,?,?)}";
    }

    /**
     * Update a Bookmark.
     *
     * @return ..
     */
    public static String updateBookmarkQuery() {
        return "{call sp_bookmark_update(?,?,?,?,?,?,?)}";
    }

    /**
     * Delete a Bookmark.
     *
     * @return ..
     */
    public static String deleteBookmarkQuery() {
        return "{call sp_bookmark_delete(?,?,?)}";
    }
}
