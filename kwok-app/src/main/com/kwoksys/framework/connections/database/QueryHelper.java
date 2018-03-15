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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;

/**
 * QueryManager
 */
public class QueryHelper {

    private static final Logger LOGGER = Logger.getLogger(QueryHelper.class.getName());

    private static final String IN_PARAM = "in";
    private static final String OUT_PARAM = "out";

    private List<Object[]> sqlParams = new ArrayList<>();
    private List<Object> sqlOutputs = new ArrayList<>();

    private String sqlStatement;
    private QueryCriteria clauses;
    private ResultSet rs;
    private PreparedStatement pstmt;
    private CallableStatement cstmt;

    public QueryHelper() {}
    
    public QueryHelper(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public void addInputStringConvertNull(String value) {
        if (value == null || value.isEmpty()) {
            sqlParams.add(new Object[]{IN_PARAM, Types.NULL, Types.VARCHAR});
        } else {
            addInputString(value);
        }
    }

    public void addInputIntegerConvertNull(Integer value) {
        if (value == null || value.equals(0)) {
            sqlParams.add(new Object[]{IN_PARAM, Types.NULL, Types.INTEGER});
        } else {
            addInputInt(value);
        }
    }

    public void addInputDoubleConvertNull(Double value) {
        if (value == null || value.equals(0.0)) {
            sqlParams.add(new Object[]{IN_PARAM, Types.NULL, Types.DOUBLE});
        } else {
            addInputDouble(value);
        }
    }

    public void addInputString(String value) {
        sqlParams.add(new Object[]{IN_PARAM, Types.VARCHAR, value});
    }

    public void addInputInt(Integer value) {
        sqlParams.add(new Object[]{IN_PARAM, Types.INTEGER, value});
    }

    public void addInputLong(Long value) {
        sqlParams.add(new Object[]{IN_PARAM, Types.BIGINT, value});
    }

    public void addInputDouble(Double value) {
        sqlParams.add(new Object[]{IN_PARAM, Types.DOUBLE, value});
    }

    public void addOutputParam(int keyPos) {
        //cstmt.registerOutParameter(1, Types.INTEGER);
        // we only need Types.INTEGER, order is populated automatically
        sqlParams.add(new Object[]{OUT_PARAM, keyPos, keyPos});
    }

    private void setCsInputParameters(CallableStatement cstmt, List<Object[]> sqlParams) throws SQLException {
        int i = 1;
        for (Object[] param : sqlParams) {
            Object paramType = param[0];
            int dataType = (Integer)param[1];
            Object dataValue = param[2];

            if (paramType.equals(IN_PARAM)) {
                if (dataType == Types.NULL) {
                    cstmt.setNull(i++, (Integer)dataValue);

                } else if (dataType == Types.INTEGER) {
                    cstmt.setInt(i++, (Integer) dataValue);

                } else if (dataType == Types.BIGINT) {
                    cstmt.setLong(i++, (Long) dataValue);

                } else if (dataType == Types.VARCHAR) {
                    cstmt.setString(i++, (String) dataValue);

                } else if (dataType == Types.DOUBLE) {
                    cstmt.setDouble(i++, (Double) dataValue);

                } else if (dataType == Types.TIMESTAMP) {
                    cstmt.setTimestamp(i++, (Timestamp) dataValue);
                }
            } else if (paramType.equals(OUT_PARAM)) {
                cstmt.registerOutParameter(i++, dataType);
            }
        }
    }
    
    public String concatSqlParams() {
        StringBuilder output = new StringBuilder();
        for (Object[] param : sqlParams) {
            output.append("[").append(param[2]).append("] ");
        }
        return output.toString();
    }

    /**
     * To be deprecated
     * @param conn
     * @param sqlStatement
     * @return
     * @throws SQLException
     */
    public List<Map<String, String>> executeQueryReturnList(Connection conn) throws SQLException {
        List<Map<String, String>> result = new ArrayList<>();

        rs = executeQuery(conn);

        ResultSetMetaData rsm = rs.getMetaData();
        while (rs.next()) {
            // Add the current column to the temp list
            Map<String, String> map = new HashMap<>();
            for (int counter = 1; counter <= rsm.getColumnCount(); counter++) {
                // Add each column to the map
                map.put(rsm.getColumnName(counter), rs.getString(counter));
            }
            result.add(map);
        }
        return result;
    }

    private void initPreparedStatement(Connection conn) throws SQLException {
        // Log this query.
        LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.DATABASE_ACCESS_PREFIX), LogConfigManager.DATABASE_ACCESS_PREFIX + " Executing statement: " + sqlStatement + "; params: " + concatSqlParams());

        pstmt = conn.prepareStatement(sqlStatement);

        // Set parameters
        int i = 1;
        for (Object[] param : sqlParams) {
            int dataType = (Integer)param[1];
            Object dataValue = param[2];

            if (dataType == Types.INTEGER) {
                pstmt.setInt(i++, (Integer) dataValue);

            } else if (dataType == Types.BIGINT) {
                pstmt.setLong(i++, (Long) dataValue);

            } else if (dataType == Types.VARCHAR) {
                pstmt.setString(i++, (String) dataValue);
            }
        }
    }
    
    /**
     * Returns a resultSet.
     * @param conn
     * @param sqlStatement
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet executeQuery(Connection conn) throws SQLException {
        initPreparedStatement(conn);
        rs = pstmt.executeQuery();
        return rs;
    }

    public int executeUpdate(Connection conn) throws SQLException {
        initPreparedStatement(conn);
        
        try {
            return pstmt.executeUpdate();
            
        } finally {
            close();
        }
    }
    
    /**
     * Returns a CallableStmt.
     * @param conn
     * @param sqlStatement
     * @throws java.sql.SQLException
     */
    public void executeProcedure(Connection conn) throws DatabaseException {
        // Log this query.
        LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.DATABASE_ACCESS_PREFIX), LogConfigManager.DATABASE_ACCESS_PREFIX + " Executing procedure: " + sqlStatement + "; params: " + concatSqlParams());

        try {
            cstmt = conn.prepareCall(sqlStatement);

            setCsInputParameters(cstmt, sqlParams);
            
            // Execute callable statement
            cstmt.execute();

            int i = 1;
            for (Object[] param : sqlParams) {
                Object paramType = param[0];
                int dataType = (Integer)param[1];

                if (paramType.equals(OUT_PARAM)) {
                    switch (dataType) {
                        case Types.INTEGER:
                            sqlOutputs.add(cstmt.getInt(i++));
                            break;
                    }
                }
                i++;
            }
        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, this);
            
        } finally {
            close();
        }
    }

    public void prepareBatch(Connection conn) throws DatabaseException {
        // Log this query.
        LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.DATABASE_ACCESS_PREFIX), LogConfigManager.DATABASE_ACCESS_PREFIX + " Preparing batch query: " + sqlStatement + "; params: " + concatSqlParams());

        try {
            cstmt = conn.prepareCall(sqlStatement);
        } catch (Exception e) {
            close();

            // Database problem
            throw new DatabaseException(e, this);
        }
    }
    
    public void addBatch() throws DatabaseException {
        // Log this query.
        LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.DATABASE_ACCESS_PREFIX), LogConfigManager.DATABASE_ACCESS_PREFIX + " Adding batch query: " + sqlStatement + "; params: " + concatSqlParams());

        try {
            setCsInputParameters(cstmt, sqlParams);
            
            cstmt.addBatch();

            sqlParams = new ArrayList<>();

        } catch (Exception e) {
            close();

            // Database problem
            throw new DatabaseException(e, this);
        }
    }

    public void executeBatch() throws DatabaseException {
        // Log this query.
        LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.DATABASE_ACCESS_PREFIX), LogConfigManager.DATABASE_ACCESS_PREFIX + " Executing batch query: " + sqlStatement + "; params: " + concatSqlParams());
        
        try {
            cstmt.executeBatch();

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, this);
            
        } finally {
            close();
        }
    }
    
    /**
     * Closes all database resources created by the query execution.
     */
    public void close() {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) { /* ignored */ }
            rs = null;
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (Exception e) { /* ignored */ }
            pstmt = null;
        }
        if (cstmt != null) {
            try {
                cstmt.close();
            } catch (Exception e) { /* ignored */ }
            cstmt = null;
        }
    }

    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public String getSqlStatement() {
        return sqlStatement;
    }

    public List<Object> getSqlOutputs() {
        return sqlOutputs;
    }
 
    public QueryCriteria getClauses() {
        return clauses;
    }

    public void setClauses(QueryCriteria clauses) {
        this.clauses = clauses;
    }

    /**
     * Resultset callback code.
     * @param rs
     * @throws Exception
     */
    public void callback(ResultSet rs) throws Exception {}
}
