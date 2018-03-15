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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;

/**
 * Database APIs.
 */
class PostgresDatabase extends DatabaseManager {

    private static final Logger LOGGER = Logger.getLogger(PostgresDatabase.class.getName());
    
    private Properties prop = new Properties();

    public PostgresDatabase() {
        // Put all params in a property objects, making it easier to read.
        prop.put("user", DB_USERNAME);
        prop.put("password", DB_PASSWORD);
        prop.put("timezone", "GMT");
    }

    protected String getDriverName() {
        return "org.postgresql.Driver";
    }

    protected String getJdbcUrl() {
        return "jdbc:postgresql://" + DB_SERVERHOST + ":" + DB_SERVERPORT + "/" + DB_NAME;
    }

    protected boolean isConnectionValid(Connection conn) {
        boolean isValid = true;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        if (conn.isClosed()) {
                // Return false, don't have much to do
	            return false;
	        }

	        ps = conn.prepareStatement("select 1");
	        rs = ps.executeQuery();

        } catch (Exception e) {
	        isValid = false;

        } finally {
	        if (rs != null) {
	            try {
	                rs.close();
                } catch (SQLException ignore) {}
	        }
	        if (ps != null) {
	            try {
	                ps.close();
	            } catch (SQLException ignore) {}
	        }
            if (!isValid) {
                // When we get to this point because of a bad connection, the bad
                // connection may still need to be closed.
                try {
                    conn.close();
                } catch (SQLException ignore) {}
            }
	    }
        return isValid;
    }

    /**
     * Creates a new connection with the default database name.
     */
    public Connection createConnection() throws DatabaseException {
        try {
            // Try to get connection from connection pool
            Connection conn = checkOutConnection();
            if (conn == null) {
                LOGGER.log(Level.INFO, LogConfigManager.DATABASE_ACCESS_PREFIX + " Creating connection to " + getJdbcUrl());
                conn = DriverManager.getConnection(getJdbcUrl(), prop);
                conn.setAutoCommit(false);
            }
            registerConn(conn);
            return conn;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, LogConfigManager.DATABASE_ACCESS_PREFIX + " Problem connecting to " + getJdbcUrl(), e);
            throw new DatabaseException(e);
        }
    }

    /**
     * Closes database connection
     */
    public void closeConnection(Connection conn, boolean hasError) {
        if (hasError) {
            try {
                LOGGER.log(Level.FINER, LogConfigManager.DATABASE_ACCESS_PREFIX + " Rolling back transaction.");
                conn.rollback();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, LogConfigManager.DATABASE_ACCESS_PREFIX + " Problem rolling back a transaction.", e);
            }
        } else {
            try {
                LOGGER.log(Level.FINER, LogConfigManager.DATABASE_ACCESS_PREFIX + " Committing transaction.");
                conn.commit();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, LogConfigManager.DATABASE_ACCESS_PREFIX + " Problem committing a transaction.", e);
            }
        }
        // Check connection back in to pool but don't close it
        checkInConnection(conn);
    }
}
