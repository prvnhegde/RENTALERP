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
package com.kwoksys.biz.auth.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.dto.AccessPage;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.UsernameNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * AuthDao.
 */
public class AuthDao extends BaseDao {

    public AuthDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Puts pages in permissions map
     *
     * @return ..
     */
    public Map<Integer, Set<Integer>> getAccessPermPages() throws DatabaseException {
        Map<Integer, Set<Integer>> permMap = new HashMap<>();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.selectPermPages()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Integer permId = rs.getInt("perm_id");
                Integer pageId = rs.getInt("page_id");

                Set<Integer> pageSet = permMap.get(permId);
                if (pageSet == null) {
                    pageSet = new HashSet<>();
                    permMap.put(permId, pageSet);
                }
                pageSet.add(pageId);
            }
        };

        executeQuery(queryHelper);
        
        return permMap;
    }

    public Set<Integer> getAccessGroupPerms(Integer groupId) throws DatabaseException {
        Set<Integer> permSet = new HashSet<>();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.selectGroupPerms()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                permSet.add(rs.getInt("perm_id"));
            }
        };
        
        queryHelper.addInputInt(groupId);

        executeQuery(queryHelper);
        
        return permSet;
    }

    public Set<Integer> getAccessUserPerms(Integer userId) throws DatabaseException {
        Set<Integer> permSet = new HashSet<>();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.selectUserPerms()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                permSet.add(rs.getInt("perm_id"));
            }
        };
        
        queryHelper.addInputInt(userId);

        executeQuery(queryHelper);
        
        return permSet;
    }

    /**
     * Put all page names and ids in HashMap.
     *
     * @return ..
     */
    public Map<String, AccessPage> getAccessPages() throws DatabaseException {
        Map<String, AccessPage> map = new HashMap<>();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.selectAccessPages()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                // Add each column to the map
                AccessPage page = new AccessPage();
                page.setPageId(rs.getInt("page_id"));
                page.setModuleId(rs.getInt("module_id"));
                page.setPath(rs.getString("page_name") + ConfigManager.system.getExtension());
                map.put(page.getPath(), page);
            }
        };
        
        executeQuery(queryHelper);

        return map;
    }

    /**
     * Update User session when the user successfully logged on to the system.
     *
     * @param userId
     * @param sessionToken
     * @return ..
     */
    public ActionMessages updateUserLogonSession(AccessUser accessUser, String sessionToken) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.updateUserLogonSessionQuery());
        queryHelper.addInputStringConvertNull(sessionToken);
        queryHelper.addInputInt(accessUser.getId());

        try {
            queryHelper.executeProcedure(conn);

            accessUser.setSessionToken(sessionToken);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages updateUserInvalidLogon(AccessUser accessUser, int invalidLogonCount) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AuthQueries.updateUserInvalidLogonQuery());
        queryHelper.addInputInt(accessUser.getId());
        queryHelper.addInputInt(invalidLogonCount);

        return executeProcedure(queryHelper);
    }

    /**
     * Update User session when the user successfully logged out of the system.
     *
     * @param userId
     * @return ..
     */
    public ActionMessages updateUserLogoutSession(Integer userId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AuthQueries.updateUserLogoutSessionQuery());
        queryHelper.addInputInt(userId);

        return executeProcedure(queryHelper);
    }

    /**
     * @param userId
     * @param sessionTokenCookie
     * @return the number of users match, or 0 if no record is found.
     */
    public boolean isValidUserSession(Integer userId, String sessionTokenCookie) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.validateUserSessionQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(userId);
        queryHelper.addInputString(sessionTokenCookie);
        queryHelper.addInputInt(ConfigManager.auth.getSessionTimeoutSeconds());

        try {
            queryHelper.executeProcedure(conn);
            return !queryHelper.getSqlOutputs().get(0).equals(0);

        } finally {
            closeConnection(conn);
        }
    }

    public ActionMessages isValidUsername(AccessUser user) throws DatabaseException, UsernameNotFoundException {
        List<AccessUser> users = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(AuthQueries.selectValidUsernameQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                user.setId(rs.getInt("user_id"));
                user.setHashedPassword(StringUtils.replaceNull(rs.getString("password")));
                user.setStatus(rs.getInt("status"));
                user.setInvalidLogonCount(rs.getInt("invalid_logon_count"));
                user.setInvalidLogonDate(DatetimeUtils.getDate(rs, "invalid_logon_date"));
                
                users.add(user);
            }
        };
        
        queryHelper.addInputString(user.getUsername());

        try {
            executeSingleRecordQuery(queryHelper);

        } catch (Exception e) {
            handleError(e);
        }
        
        if (errors.isEmpty()) {
            if (users.isEmpty()) {
                throw new UsernameNotFoundException();
            }
        }

        return errors;
    }
}