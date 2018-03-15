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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.biz.system.dto.SystemInfo;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * SystemDao
 */
public class SystemDao extends BaseDao {

    private static final Logger LOGGER = Logger.getLogger(SystemDao.class.getName());

    public SystemDao(RequestContext requestContext) {
        super(requestContext);
    }

    public Map<String, String> getDatabaseInfo() throws DatabaseException {
        Connection conn = getConnection();

        Map<String, String> map = new HashMap<>();

        try {
            DatabaseMetaData meta = conn.getMetaData();
            map.put("DatabaseProductName", meta.getDatabaseProductName());
            map.put("DatabaseProductVersion", meta.getDatabaseProductVersion());

        } catch (Exception e) {
            // This doesn't go through QueryHelper. Don't know how to log errors.
            LOGGER.log(Level.SEVERE, LogConfigManager.DATABASE_ACCESS_PREFIX + " Exception: " + e.getMessage());

        } finally {
            closeConnection(conn);
        }
        return map;
    }

    public SystemInfo getSystemInfo() throws DatabaseException {
        SystemInfo systemInfo = new SystemInfo();

        QueryHelper queryHelper = new QueryHelper(SystemQueries.selectSystemValues()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                systemInfo.setCacheKey(StringUtils.replaceNull(rs.getString("cache_key")));
                systemInfo.setSysdate(DatetimeUtils.getDate(rs, "sysdate"));
            }
        };

        executeSingleRecordQuery(queryHelper);

        return systemInfo;
    }

    public String getTimezone() throws DatabaseException {
        List<String> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(SystemQueries.selectCurrentTimezone()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                list.add(rs.getString("timezone"));
            }
        };
        
        executeSingleRecordQuery(queryHelper);

        return list.get(0);
    }

    public ActionMessages updateTimezone() throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SystemQueries.updateCurrentTimezone());
        return executeUpdate(queryHelper);
    }

    /**
     * Gets a list of cache keys that need to be flushed
     * @return
     * @throws DatabaseException
     */
    public List<String> getFlushSystemCacheKeys(Long cacheTime) throws DatabaseException {
        List<String> cacheKeys = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(SystemQueries.selectFlushSystemCaches()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                cacheKeys.add(rs.getString("cache_key"));
            }
        };

        queryHelper.addInputLong(cacheTime);

        executeQuery(queryHelper);
        
        return cacheKeys;
    }

    public Map<String, String> getSystemConfig() throws DatabaseException {
        Map<String, String> map = new HashMap<>();

        QueryHelper queryHelper = new QueryHelper(SystemQueries.selectSystemConfig()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                // Add each column to the map
                map.put(rs.getString("config_key"), StringUtils.replaceNull(rs.getString("config_value")));
            }
        };

        executeQuery(queryHelper);
        
        return map;
    }

    public int getObjectMapCount(List<Integer> objectTypeIds, Integer linkedObjectId, List<Integer> linkedObjectTypeIds) throws DatabaseException {
        String objectTypeIdOption = StringUtils.joinIntegers(objectTypeIds, ",");
        String linkedTypeIdOption = StringUtils.joinIntegers(linkedObjectTypeIds, ",");
        String sqlQuery = SystemQueries.selectObjectMapCount(objectTypeIdOption, linkedTypeIdOption);

        List<Integer> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(sqlQuery) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                list.add(rs.getInt("obj_count"));
            }
        };
        
        queryHelper.addInputInt(linkedObjectId);
        
        executeSingleRecordQuery(queryHelper);
        
        return list.get(0);
    }

    public int getLinkedObjectMapCount(List<Integer> linkedObjectTypeIds, Integer objectId, Integer objectTypeId) throws DatabaseException {
        List<Integer> list = new ArrayList<>();

        String linkedTypeIdOption = StringUtils.joinIntegers(linkedObjectTypeIds, ",");

        QueryHelper queryHelper = new QueryHelper(SystemQueries.selectLinkedObjectMapCount(linkedTypeIdOption)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                list.add(rs.getInt("obj_count"));
            }
        };
        
        queryHelper.addInputInt(objectId);
        queryHelper.addInputInt(objectTypeId);

        executeSingleRecordQuery(queryHelper);

        return list.get(0);
    }

    public ActionMessages validateSystemCaches(Long cacheTime) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SystemQueries.validateSystemCaches());
        queryHelper.addInputLong(cacheTime);

        return executeProcedure(queryHelper);
    }

    public ActionMessages resetSystemCache(String cacheKey, Long cacheTime) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SystemQueries.updateSystemCache());
        queryHelper.addInputString(cacheKey);
        queryHelper.addInputLong(cacheTime);

        return executeProcedure(queryHelper);
    }

    /**
     * Add a object mapping
     */
    public ActionMessages addObjectMapping(ObjectLink objectMap) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(SystemQueries.insertObjectMapQuery());
        queryHelper.addInputInt(objectMap.getObjectTypeId());
        queryHelper.addInputInt(objectMap.getObjectId());
        queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
        queryHelper.addInputInt(objectMap.getLinkedObjectId());
        queryHelper.addInputInt(requestContext.getUser().getId());
        queryHelper.addInputStringConvertNull(objectMap.getRelDescription());

        return executeProcedure(queryHelper);
    }

    /**
     * Delete a object mapping.
     *
     * @return ..
     */
    public ActionMessages deleteObjectMapping(ObjectLink objectMap) throws DatabaseException {
        if (objectMap.getLinkedObjectId() == 0) {
            return errors;
        }
        
        QueryHelper queryHelper = new QueryHelper(SystemQueries.deleteObjectMapQuery());
        queryHelper.addInputInt(objectMap.getObjectTypeId());
        queryHelper.addInputInt(objectMap.getObjectId());
        queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
        queryHelper.addInputInt(objectMap.getLinkedObjectId());

        return executeProcedure(queryHelper);
    }
}
