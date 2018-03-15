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
package com.kwoksys.biz.admin.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.UserPermissionMap;
import com.kwoksys.biz.auth.core.AuthUtils;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * AccessUserDao.
 */
public class AccessUserDao extends BaseDao {

    public AccessUserDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Gets users.
     *
     * @param query
     * @return ..
     */
    public List<AccessUser> getUsers(QueryCriteria query) throws DatabaseException {
        List<AccessUser> users = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserListQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AccessUser user = new AccessUser();
                user.setId(rs.getInt("user_id"));
                user.setUsername(StringUtils.replaceNull(rs.getString("username")));
                user.setDisplayName(StringUtils.replaceNull(rs.getString("display_name")));
                user.setFirstName(StringUtils.replaceNull(rs.getString("first_name")));
                user.setLastName(StringUtils.replaceNull(rs.getString("last_name")));
                user.setEmail(StringUtils.replaceNull(rs.getString("email")));
                user.setStatus(rs.getInt("status"));
                users.add(user);
            }
        };
        
        executeQuery(queryHelper);

        return users;
    }

    /**
     * Get users with details (i.e. for export).
     */
    public List<AccessUser> getExtendedUsers(QueryCriteria query) throws DatabaseException {
        List<AccessUser> users = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserExportListQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AccessUser user = new AccessUser();
                user.setId(rs.getInt("user_id"));
                user.setUsername(StringUtils.replaceNull(rs.getString("username")));
                user.setDisplayName(StringUtils.replaceNull(rs.getString("display_name")));
                user.setFirstName(StringUtils.replaceNull(rs.getString("first_name")));
                user.setLastName(StringUtils.replaceNull(rs.getString("last_name")));
                user.setEmail(StringUtils.replaceNull(rs.getString("email")));
                user.setStatus(rs.getInt("status"));
                user.setLastLogonTime(DatetimeUtils.getDate(rs, "last_logon"));
                user.setLastVisitTime(DatetimeUtils.getDate(rs, "last_visit"));
                user.setContactId(rs.getInt("contact_id"));
                user.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                user.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                user.setCreator(new AccessUser());
                user.getCreator().setId(rs.getInt("creator"));
                user.getCreator().setUsername(rs.getString("creator_username"));
                user.getCreator().setDisplayName(rs.getString("creator_display_name"));

                user.setModifier(new AccessUser());
                user.getModifier().setId(rs.getInt("modifier"));
                user.getModifier().setUsername(rs.getString("modifier_username"));
                user.getModifier().setDisplayName(rs.getString("modifier_display_name"));
                users.add(user);
            }
        };
        
        executeQuery(queryHelper);
        
        return users;
    }

    public int getUserCount(QueryCriteria query) throws DatabaseException {
        return getRowCount(AdminQueries.selectUserCountQuery(query));
    }

    /**
     * Gets user details.
     *
     * @param reqUserId
     * @return ..
     */
    public AccessUser getUser(Integer reqUserId) throws DatabaseException, ObjectNotFoundException {
        AccessUser user = new AccessUser();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserDetailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                user.setId(rs.getInt("user_id"));
                user.setUsername(StringUtils.replaceNull(rs.getString("username")));
                user.setDisplayName(StringUtils.replaceNull(rs.getString("display_name")));
                user.setFirstName(StringUtils.replaceNull(rs.getString("first_name")));
                user.setLastName(StringUtils.replaceNull(rs.getString("last_name")));
                user.setEmail(StringUtils.replaceNull(rs.getString("email")));
                user.setStatus(rs.getInt("status"));
                user.setHashedPassword(StringUtils.replaceNull(rs.getString("password")));
                user.setSessionToken(StringUtils.replaceNull(rs.getString("session_key")));
                user.setLastLogonTime(DatetimeUtils.getDate(rs, "last_logon"));
                user.setLastVisitTime(DatetimeUtils.getDate(rs, "last_visit"));
                user.setHardwareCount(rs.getInt("hardware_count"));
                user.setContactId(rs.getInt("contact_id"));
                user.setDefaultUser(rs.getBoolean("is_default_user"));
                user.setGroupId(rs.getInt("group_id"));
                user.setGroupName(StringUtils.replaceNull(rs.getString("group_name")));
                user.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                user.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                user.setCreator(new AccessUser());
                user.getCreator().setId(rs.getInt("creator"));
                user.getCreator().setUsername(rs.getString("creator_username"));
                user.getCreator().setDisplayName(rs.getString("creator_display_name"));

                user.setModifier(new AccessUser());
                user.getModifier().setId(rs.getInt("modifier"));
                user.getModifier().setUsername(rs.getString("modifier_username"));
                user.getModifier().setDisplayName(rs.getString("modifier_display_name"));
            }
        };
        
        queryHelper.addInputInt(reqUserId);
        
        executeSingleRecordQuery(queryHelper);
        
        if (user.getId() != null) {
            return user;

        } else {
            throw new ObjectNotFoundException("User ID: " + reqUserId);
        }
    }

    /**
     * Returns user id given a username.
     *
     * @return ..
     */
    public Integer getUserIdByUsername(String username) throws DatabaseException {
        List<Integer> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserIdByNameQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                list.add(rs.getInt("user_id"));
            }
        };
        
        queryHelper.addInputString(username);

        executeSingleRecordQuery(queryHelper);
        
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public Integer getUserIdByEmail(String email) throws DatabaseException {
        List<Integer> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserIdByEmailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                list.add(rs.getInt("user_id"));
            }
        };
        
        queryHelper.addInputString(email);
        
        executeSingleRecordQuery(queryHelper);
        
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<UserPermissionMap> getUserAccess(QueryCriteria query, Integer userId) throws DatabaseException {
        List<UserPermissionMap> access = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectUserAccessQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                UserPermissionMap userperm = new UserPermissionMap();
                userperm.setUserId(userId);
                userperm.setPermId(rs.getInt("perm_id"));
                userperm.setPermName(rs.getString("perm_name"));
                userperm.setHasPermission(rs.getInt("has_permission")!=0);
                access.add(userperm);
            }
        };
        
        queryHelper.addInputInt(userId);
        
        executeQuery(queryHelper);

        return access;
    }

    public ActionMessages addUser(AccessUser user, AccessGroup group, Contact contact) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.insertUserQuery());

        try {
            queryHelper.addOutputParam(Types.INTEGER);
            queryHelper.addInputStringConvertNull(user.getUsername());
            queryHelper.addInputStringConvertNull(user.getFirstName());
            queryHelper.addInputStringConvertNull(user.getLastName());
            queryHelper.addInputStringConvertNull(user.getDisplayName());
            queryHelper.addInputStringConvertNull(user.getEmail());
            queryHelper.addInputInt(user.getStatus());
            queryHelper.addInputStringConvertNull(AuthUtils.hashPassword(user.getPasswordNew()));
            queryHelper.addInputInt(requestContext.getUser().getId());

            queryHelper.executeProcedure(conn);
            // Put some values in the result
            user.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update contact
            updateUserContact(conn, user, contact);

            // Update group
            updateUserGroup(conn, group, user);

            // Update custom fields
            if (!user.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, user.getId(), user.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Updates user details.
     *
     * @return ..
     */
    public ActionMessages updateUser(AccessUser user, AccessGroup group, Contact contact) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateUserQuery());
        queryHelper.addInputInt(user.getId());
        queryHelper.addInputStringConvertNull(user.getUsername());
        queryHelper.addInputStringConvertNull(user.getFirstName());
        queryHelper.addInputStringConvertNull(user.getLastName());
        queryHelper.addInputStringConvertNull(user.getDisplayName());
        queryHelper.addInputStringConvertNull(user.getEmail());
        queryHelper.addInputInt(user.getStatus());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update contact
            updateUserContact(conn, user, contact);

            // Update group
            updateUserGroup(conn, group, user);

            // Update custom fields
            if (!user.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, user.getId(), user.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages updateUserContact(AccessUser user, Contact contact) throws DatabaseException {
        Connection conn = getConnection();

        try {
            updateUserContact(conn, user, contact);
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Updates user password.
     *
     * @return ..
     */
    public ActionMessages editUserPassword(AccessUser user) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateUserPasswordQuery());
        try {
            queryHelper.addInputStringConvertNull(AuthUtils.hashPassword(user.getPasswordNew()));
            queryHelper.addInputInt(user.getId());

        } catch (Exception e) {
            errors.add("application", new ActionMessage("common.error.application"));
            return errors;
        }

        return executeProcedure(queryHelper);
    }

    /**
     * Deletes a user
     * @param user
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteUser(AccessUser user) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.deleteUserQuery());
        queryHelper.addInputInt(ObjectTypes.USER);
        queryHelper.addInputInt(user.getId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    public ActionMessages updateUserAccess(UserPermissionMap userGroup) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateUserAccessQuery());
        queryHelper.addInputInt(userGroup.getUserId());
        queryHelper.addInputInt(userGroup.getPermId());
        queryHelper.addInputInt(userGroup.getCmd());

        return executeProcedure(queryHelper);
    }

    public void updateUserContact(Connection conn, AccessUser user, Contact contact) throws DatabaseException {
        if (contact == null) {
            return;
        }

        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateUserContact());
        queryHelper.addInputInt(user.getId());
        queryHelper.addInputInt(contact.getId());
        queryHelper.addInputStringConvertNull(contact.getFirstName());
        queryHelper.addInputStringConvertNull(contact.getLastName());
        queryHelper.addInputStringConvertNull(contact.getEmailPrimary());
        queryHelper.addInputStringConvertNull(contact.getTitle());
        queryHelper.addInputIntegerConvertNull(contact.getCompanyId());
        queryHelper.addInputInt(ObjectTypes.COMPANY_EMPLOYEE_CONTACT);
        queryHelper.addInputStringConvertNull(contact.getPhoneHome());
        queryHelper.addInputStringConvertNull(contact.getPhoneMobile());
        queryHelper.addInputStringConvertNull(contact.getPhoneWork());
        queryHelper.addInputStringConvertNull(contact.getFax());
        queryHelper.addInputStringConvertNull(contact.getEmailSecondary());
        queryHelper.addInputInt(contact.getMessenger1Type());
        queryHelper.addInputStringConvertNull(contact.getMessenger1Id());
        queryHelper.addInputInt(contact.getMessenger2Type());
        queryHelper.addInputStringConvertNull(contact.getMessenger2Id());
        queryHelper.addInputStringConvertNull(contact.getHomepageUrl());
        queryHelper.addInputStringConvertNull(contact.getDescription());
        queryHelper.addInputStringConvertNull(contact.getAddressStreetPrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressCityPrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressStatePrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressZipcodePrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressCountryPrimary());
        queryHelper.addInputInt(requestContext.getUser().getId());

        queryHelper.executeProcedure(conn);
    }

    private void updateUserGroup(Connection conn, AccessGroup group, AccessUser user) throws DatabaseException {
        // Only runs the add/remove when user changes group
        if (group != null && !group.getId().equals(user.getGroupId())) {
            AccessGroupDao groupDao = new AccessGroupDao(requestContext);
            groupDao.removeGroupMember(conn, new AccessGroup(user.getGroupId()), user.getId());

            if (group.getId() != 0) {
                groupDao.addGroupMember(conn, group, user.getId());
            }
        }
    }
}
