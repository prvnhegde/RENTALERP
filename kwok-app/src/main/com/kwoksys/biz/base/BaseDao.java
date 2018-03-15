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
package com.kwoksys.biz.base;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.connections.database.DatabaseManager;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;

/**
 * BaseDao
 */
public class BaseDao {

    private static final Logger LOGGER = Logger.getLogger(BaseDao.class.getName());

    protected RequestContext requestContext;

    private DatabaseManager databaseManager;

    protected final ActionMessages errors = new ActionMessages();

    public BaseDao(RequestContext requestContext) {
        this.requestContext = requestContext;
        databaseManager = DatabaseManager.getInstance();
    }

    public Connection getConnection() throws DatabaseException {
        return databaseManager.createConnection();
    }

    public void closeConnection(Connection conn) {
        databaseManager.closeConnection(conn, !errors.isEmpty());
    }

    protected ActionMessages executeProcedure(QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        try {
            queryHelper.executeProcedure(conn);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    protected ActionMessages executeUpdate(QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        try {
            queryHelper.executeUpdate(conn);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public void executeQuery(QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                queryHelper.callback(rs);
            }

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.close();
            closeConnection(conn);
        }
    }
    
    public void executeSingleRecordQuery(QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                queryHelper.callback(rs);
            }

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.close();
            closeConnection(conn);
        }
    }

    public List<Map<String, String>> executeQueryReturnList(QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        try {
            return queryHelper.executeQueryReturnList(conn);

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.close();
            closeConnection(conn);
        }
    }

    public int getRowCount(String sql) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(sql);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            rs.next();
            return rs.getInt("row_count");

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.close();
            closeConnection(conn);
        }
    }
    
    /**
     * Adds database error message to errors variable. This lets the UI returns an explanation instead of serving an 
     * HTTP 500 error. 
     */
    public void handleError(Exception e) {
        if (e instanceof DatabaseException && ((DatabaseException) e).getQueryHelper() != null) {
            // The error is from a subquery. Don't need to log it since it's been logged in the exception.
        } else {
            LOGGER.log(Level.SEVERE, LogConfigManager.DATABASE_ACCESS_PREFIX + " Problem executing SQL statement", e);
        }
        errors.add("database", new ActionMessage("common.error.database"));
    }
}
