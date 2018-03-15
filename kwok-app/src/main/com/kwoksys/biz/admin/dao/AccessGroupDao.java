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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.GroupPermissionMap;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * AccessGroupDao
 */
public class AccessGroupDao extends BaseDao {

    public AccessGroupDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Gets user groups.
     *
     * @return ..
     */
    public List<AccessGroup> getGroups(QueryCriteria query) throws DatabaseException {
        List<AccessGroup> groups = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectGroupListQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AccessGroup group = new AccessGroup();
                group.setId(rs.getInt("group_id"));
                group.setName(StringUtils.replaceNull(rs.getString("group_name")));
                group.setDescription(StringUtils.replaceNull(rs.getString("group_description")));
                groups.add(group);
            }
        };
        
        executeQuery(queryHelper);
        
        return groups;
    }

    /**
     * Get AccessGroup object
     *
     * @param groupId
     * @return ..
     */
    public AccessGroup getGroup(Integer groupId) throws DatabaseException, ObjectNotFoundException {
        AccessGroup group = new AccessGroup();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectGroupDetailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                group.setId(rs.getInt("group_id"));
                group.setName(rs.getString("group_name"));
                group.setDescription(rs.getString("group_description"));
                group.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                group.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                group.setCreator(new AccessUser());
                group.getCreator().setId(rs.getInt("creator"));
                group.getCreator().setUsername(rs.getString("creator_username"));
                group.getCreator().setDisplayName(rs.getString("creator_display_name"));

                group.setModifier(new AccessUser());
                group.getModifier().setId(rs.getInt("modifier"));
                group.getModifier().setUsername(rs.getString("modifier_username"));
                group.getModifier().setDisplayName(rs.getString("modifier_display_name"));
            }
        };
        
        queryHelper.addInputInt(groupId);
        
        executeSingleRecordQuery(queryHelper);

        if (group.getId() != null) {
            return group;
        } else {
            throw new ObjectNotFoundException("Group ID: " + groupId);
        }
    }

    /**
     * Add group.
     *
     * @return ..
     */
    public ActionMessages addGroup(AccessGroup group) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.addGroupQuery());
        queryHelper.addOutputParam(Types.INTEGER);

        queryHelper.addInputStringConvertNull(group.getName());
        queryHelper.addInputStringConvertNull(group.getDescription());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);
            // Put some values in the result
            group.setId((Integer)queryHelper.getSqlOutputs().get(0));

            // Update group members
            updateGroupMembers(conn, group);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Update group.
     *
     * @return ..
     */
    public ActionMessages editGroup(AccessGroup group) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateGroupQuery());
        queryHelper.addInputInt(group.getId());
        queryHelper.addInputStringConvertNull(group.getName());
        queryHelper.addInputStringConvertNull(group.getDescription());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update group members
            updateGroupMembers(conn, group);

        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages deleteGroup(AccessGroup group) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.deleteGroupQuery());
        queryHelper.addInputInt(group.getId());

        return executeProcedure(queryHelper);
    }

    public List<GroupPermissionMap> getGroupAccess(QueryCriteria query, Integer groupId) throws DatabaseException {
        List<GroupPermissionMap> access = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(AdminQueries.selectGroupAccessQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                GroupPermissionMap groupperm = new GroupPermissionMap();
                groupperm.setGroupId(groupId);
                groupperm.setPermId(rs.getInt("perm_id"));
                groupperm.setPermName(rs.getString("perm_name"));
                groupperm.setHasPermission(rs.getInt("has_permission")!=0);
                access.add(groupperm);
            }
        };

        queryHelper.addInputInt(groupId);

        executeQuery(queryHelper);

        return access;
    }

    public ActionMessages updateGroupAccess(GroupPermissionMap groupperm) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.updateGroupAccessQuery());
        queryHelper.addInputInt(groupperm.getGroupId());
        queryHelper.addInputInt(groupperm.getPermId());
        queryHelper.addInputInt(groupperm.getCmd());

        return executeProcedure(queryHelper);
    }

    public List<AccessUser> getAvailableMembers(Integer groupId) throws DatabaseException {
        // Get a list of available members.
        UserSearch userSearch = new UserSearch();
        userSearch.put("excludedDefaultAdmin", "");
        userSearch.put("notInGroup", groupId);

        QueryCriteria query = new QueryCriteria(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        return adminService.getUsers(query);
    }

    public List<AccessUser> getGroupMembers(Integer groupId) throws DatabaseException {
        // Get a list of selected members.
        UserSearch userSearch = new UserSearch();
        userSearch.put("excludedDefaultAdmin", "");
        userSearch.put("inGroupId", groupId);

        QueryCriteria query = new QueryCriteria(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        return adminService.getUsers(query);
    }

    public void updateGroupMembers(Connection conn, AccessGroup group) throws DatabaseException {
        List<Integer> selectedMemberList = group.getSelectedMembers() == null ? new ArrayList<>() :
            new ArrayList<>(group.getSelectedMembers());

        // Loop through the current member list
        // If the member is not the in list, run a procedure to remove the member
        for (AccessUser user: getGroupMembers(group.getId())) {
            if (selectedMemberList.contains(user.getId())) {
                selectedMemberList.remove(user.getId());
            } else {
                removeGroupMember(conn, group, user.getId());
            }
        }

        // The remaining list is the users we want to add
        for (Integer userId: selectedMemberList) {
            addGroupMember(conn, group, userId);
        }
    }

    public void addGroupMember(Connection conn, AccessGroup group, Integer userId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.addGroupMembersQuery());
        queryHelper.addInputInt(group.getId());
        queryHelper.addInputInt(userId);
        queryHelper.addInputInt(requestContext.getUser().getId());

        queryHelper.executeProcedure(conn);
    }

    public void removeGroupMember(Connection conn, AccessGroup group, Integer userId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(AdminQueries.removeGroupMembersQuery());
        queryHelper.addInputInt(group.getId());
        queryHelper.addInputInt(userId);

        queryHelper.executeProcedure(conn);
    }
}