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

import com.kwoksys.framework.properties.PropertiesManager;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.biz.system.core.configs.SystemConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseManager class
 */
public abstract class DatabaseManager {

    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    protected static final String DB_SERVERHOST = PropertiesManager.get(PropertiesManager.DB_SERVERHOST_KEY);
    protected static final String DB_SERVERPORT = PropertiesManager.get(PropertiesManager.DB_SERVERPORT_KEY);
    protected static final String DB_NAME = PropertiesManager.get(PropertiesManager.DB_NAME_KEY);
    protected static final String DB_USERNAME = PropertiesManager.get(PropertiesManager.DB_USERNAME_KEY);
    protected static final String DB_PASSWORD = PropertiesManager.get(PropertiesManager.DB_PASSWORD_KEY);

    private Set<Connection> availablePool = new HashSet<>();
    private Set<Connection> checkedOutPool = new HashSet<>();

    private static DatabaseManager instance;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = DatabaseFactory.getDatabaseManager(PropertiesManager.get(PropertiesManager.DB_TYPE_KEY));
            String driverName = instance.getDriverName();

            try {
                LOGGER.log(Level.INFO, LogConfigManager.DATABASE_ACCESS_PREFIX + " Loading database driver: " + driverName);
                Class.forName(driverName);

            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Failed to load database driver: " + driverName, e);
            }
        }
        return instance;
    }

    protected abstract String getDriverName();

    protected abstract String getJdbcUrl();

    /**
     * Creates a connection
     * @return
     * @throws DatabaseException
     */
    public abstract Connection createConnection() throws DatabaseException;

    /**
     * Closes a connection.
     */
    public abstract void closeConnection(Connection conn, boolean hasError);

    protected abstract boolean isConnectionValid(Connection conn);

    protected synchronized void registerConn(Connection conn) {
        checkedOutPool.add(conn);
        LOGGER.log(Level.FINER, LogConfigManager.DATABASE_ACCESS_PREFIX + " Adding/moving to checkout pool... (" + checkedOutPool.size() + ", " + availablePool.size() + ")");
    }

    protected synchronized void checkInConnection(Connection conn) {
        checkedOutPool.remove(conn);
        availablePool.add(conn);
        LOGGER.log(Level.FINER, LogConfigManager.DATABASE_ACCESS_PREFIX + " Moving from checkout pool to available pool... (" + checkedOutPool.size() + ", " + availablePool.size() + ")");
    }

    protected synchronized Connection checkOutConnection() throws SQLException {
        Iterator<Connection> iter = availablePool.iterator();
        // Loop through connections to get the first valid connection
        while (iter.hasNext()) {
            Connection conn = iter.next();

            // Remove connection from pool
            iter.remove();

            // Return only if connection is valid
            if (isConnectionValid(conn)) {
                return conn;
            }
        }

        if (isDbMaxConnectionExceeded()) {
            throw new SQLException("Max " + ConfigManager.system.getDbMaxConnection() + " connection exceeded.");
        }
        return null;
    }

    private boolean isDbMaxConnectionExceeded() {
        if (ConfigManager.system.getDbMaxConnection() == SystemConfigManager.DB_MAX_CONNECTION_UNLIITED) {
            return false;
        } else if ((availablePool.size() + checkedOutPool.size() + 2) < ConfigManager.system.getDbMaxConnection()) {
            return false;
        } else {
            return true;
        }
    }
    
    public static void destroy() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                LOGGER.log(Level.INFO, LogConfigManager.SHUTDOWN_PREFIX + " Deregistering jdbc driver: " + driver);
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, LogConfigManager.SHUTDOWN_PREFIX + " Error deregistering driver: " + driver, e);
            }
        }
    }

    public int getAvailablePoolSize() {
        return availablePool.size();
    }

    public int getCheckedOutPoolSize() {
        return checkedOutPool.size();
    }
}
