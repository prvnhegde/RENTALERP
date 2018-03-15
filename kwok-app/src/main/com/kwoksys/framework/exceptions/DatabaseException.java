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
package com.kwoksys.framework.exceptions;

import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.connections.database.QueryHelper;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database access related exception.
 */
public class DatabaseException extends Exception {

    private static final Logger LOGGER = Logger.getLogger(DatabaseException.class.getName());

    private QueryHelper queryHelper;

    /**
     * JDBC connection exception
     * @param e
     */
    public DatabaseException(Exception e) {
        super(e);
    }

    /**
     * Exception when executing a query
     * @param e
     * @param sqlQuery
     */
    public DatabaseException(Exception e, QueryHelper queryHelper) {
        super(e);
        this.queryHelper = queryHelper;
        LOGGER.log(Level.SEVERE, LogConfigManager.DATABASE_ACCESS_PREFIX + " Problem with this query: "
                + queryHelper.getSqlStatement() + "; params: " + queryHelper.concatSqlParams(), e);
    }

    public QueryHelper getQueryHelper() {
        return queryHelper;
    }
}
