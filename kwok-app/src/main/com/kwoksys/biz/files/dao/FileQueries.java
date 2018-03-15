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
package com.kwoksys.biz.files.dao;

import com.kwoksys.biz.files.dto.File;
import com.kwoksys.framework.connections.database.QueryCriteria;

/**
 * For File object.
 * Format: select_, insert_, update_, delete_, _Query
 */
public class FileQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals(File.NAME)) {
            return "lower(file_name)";
        } else if (column.equals(File.LABEL)) {
                return "lower(file_friendly_name)";
        } else {
            return column;
        }
    }

    /**
     * Return all Files associated with a specific objects.
     */
    public static String selectFileListQuery(QueryCriteria query) {
        return "select f.file_id, f.file_name, f.file_friendly_name, f.file_description, f.file_mime_type, " +
                "f.file_byte_size, f.file_physical_name, f.creator, f.creation_date " +
                "from (select * from file_view where object_type_id = ? and object_id = ?) f "
                + query.createWhereClause();
    }

    /**
     * Return details of a File for a particular object.
     */
    public static String selectFileDetailQuery() {
        return "select f.file_id, f.file_name, f.file_friendly_name, f.file_description, f.file_mime_type, " +
                "f.file_byte_size, f.file_physical_name " +
                "from (select * from file_view where object_type_id = ? and object_id = ? and file_id = ?) f ";
    }

    /**
     * Add a new File record.
     *
     * @return ..
     */
    public static String insertNewFileQuery() {
        return "{call sp_file_add(?,?,?)}";
    }

    /**
     * This is for filling up File detail after a file is uploaded.
     *
     * @return ..
     */
    public static String updateNewFileDetailQuery() {
        return "{call sp_file_add_update(?,?,?,?,?,?,?,?,?,?)}";
    }

    public static String updateFileDetailsQuery() {
        return "update file set file_friendly_name = ?, modifier = ?, modification_date = now()"
                + " where file_id = ?";
    }

    /**
     * Delete a dummy File, probably created during failed file upload.
     *
     * @return ..
     */
    public static String deleteNewfileQuery() {
        return "{call sp_file_delete_dummy(?)}";
    }

    /**
     * Delete a File.
     *
     * @return ..
     */
    public static String deleteFileQuery() {
        return "{call sp_file_delete(?,?,?)}";
    }
}
