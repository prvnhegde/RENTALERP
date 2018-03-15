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
package com.kwoksys.framework.connections.database;

import com.kwoksys.biz.system.core.configs.LogConfigManager;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * DatabaseFactory class
 */
abstract class DatabaseFactory {

    private static final Logger LOGGER = Logger.getLogger(DatabaseFactory.class.getName());

    private static PostgresDatabase postgresDbInstance;

    public static DatabaseManager getDatabaseManager(String dbType) {
        if (dbType.equals("postgres")) {
            if (postgresDbInstance == null) {
                LOGGER.log(Level.INFO, LogConfigManager.DATABASE_ACCESS_PREFIX + " Creating a new PostgresDatabase instance.");
                postgresDbInstance = new PostgresDatabase();
            }
            return postgresDbInstance;
        }
        return null;
    }
}
