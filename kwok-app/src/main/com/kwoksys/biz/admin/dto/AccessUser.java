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
package com.kwoksys.biz.admin.dto;

import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;

import java.util.Date;

/**
 * User Object.
 */
public class AccessUser extends BaseObject {

    public static final String USERNAME = "username";
    public static final String FIRST_NAME = "user_first_name";
    public static final String LAST_NAME = "user_last_name";
    public static final String DISPLAY_NAME = "user_display_name";
    public static final String EMAIL = "user_email";
    public static final String STATUS = "user_status";
    public static final String LAST_LOGON = "user_last_logon";
    public static final String LAST_VISIT = "user_last_visit";
    public static final String IS_DEFAULT_USER = "is_default_user";

    /*
     * There are several use cases for passwordNew.
     * One is for adminsitrator to reset passwordNew.
     * That case, we need new passwordNew and confirm new passwordNew.
     * <p>
     * Another case is for user to change passwordNew.
     * In this case, we need to queries user's existing passwordNew, and user entered old passwordNew,
     * new passwordNew, and confirm new passwordNew.
     */
    private Integer id;             // User Id. The ID of User being modified.
    private String displayName;     // This is the way users would like their name displayed.
    private String username;        // Username of the User being added/modified.
    private String firstName;       // The First Name of the User being added/modified.
    private String lastName;        // The Last Name of the User being added/modified.
    private String email;           // The Last Name of the User being added/modified.
    private int status = AttributeFieldIds.USER_STATUS_DISABLED;          // The Status of the User being added/modified.
    private String requestedPassword;   // Password given from a form, e.g. login form
    private String passwordNew;         // New password.
    private String passwordConfirm;     // Confirm old password
    private String hashedPassword;      // Existing hashed password
    private Date lastLogonTime;
    private Date lastVisitTime;
    private int hardwareCount = 0;
    private Integer contactId = 0;
    private Integer groupId = 0;
    private String groupName;
    private String sessionToken;
    private boolean isDefaultUser;
    private int invalidLogonCount = 0;
    private Date invalidLogonDate;
    /**
     * Indicates this user doesn't exist in the system anymore.
     */
    private boolean removed;

    public AccessUser() {}

    public AccessUser(Integer id) {
        this.id = id;
    }

    /**
     * Check whether userId same as Guess user's userId
     * @return
     */
    public boolean isGuessUser() {
        return (id.equals(Access.GUEST_USER_ID));
    }

    public boolean isLoggedOn() {
        return (!id.equals(Access.GUEST_USER_ID));
    }

    public boolean isLoggedOn(RequestContext requestContext ) {
        return !sessionToken.isEmpty()
                && !isGuessUser()
                && (requestContext.getSysdate().getTime() - lastVisitTime.getTime())/1000 < ConfigManager.auth.getSessionTimeoutSeconds();
    }

    public boolean isAccountDisabled() {
        return (status == AttributeFieldIds.USER_STATUS_DISABLED);
    }

    public boolean isAccountEnabled() {
        return (status == AttributeFieldIds.USER_STATUS_ENABLED);
    }

    public boolean hasPermission(Integer permissionId) throws DatabaseException {
        return Access.hasPermission(this, permissionId);
    }

    public boolean hasPermission(String pageName) throws Exception {
        return Access.hasPermission(this, pageName);
    }

    //
    // Getter and Setter
    //
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getRequestedPassword() {
        return requestedPassword;
    }
    public void setRequestedPassword(String requestedPassword) {
        this.requestedPassword = requestedPassword;
    }
    public String getPasswordNew() {
        return passwordNew;
    }
    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }
    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    public String getLastLogonTime() {
        return DatetimeUtils.toLocalDatetime(lastLogonTime);
    }
    public void setLastLogonTime(Date lastLogonTime) {
        this.lastLogonTime = lastLogonTime;
    }
    public String getFormattedLastVisitTime() {
        return DatetimeUtils.toLocalDatetime(lastVisitTime);
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }
    public Integer getContactId() {
        return contactId;
    }
    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
    public String getSessionToken() {
        return sessionToken;
    }
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public int getHardwareCount() {
        return hardwareCount;
    }
    public void setHardwareCount(int hardwareCount) {
        this.hardwareCount = hardwareCount;
    }
    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public boolean isDefaultUser() {
        return isDefaultUser;
    }

    public void setDefaultUser(boolean defaultUser) {
        isDefaultUser = defaultUser;
    }

    public int getInvalidLogonCount() {
        return invalidLogonCount;
    }

    public void setInvalidLogonCount(int invalidLogonCount) {
        this.invalidLogonCount = invalidLogonCount;
    }

    public Date getInvalidLogonDate() {
        return invalidLogonDate;
    }

    public void setInvalidLogonDate(Date invalidLogonDate) {
        this.invalidLogonDate = invalidLogonDate;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}