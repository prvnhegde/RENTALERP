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
package com.kwoksys.biz.issues.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.util.DatetimeUtils;

/**
 * Issue Object.
 */
public class Issue extends BaseObject {

    public static final String ID = "issue_id";
    public static final String TITLE = "issue_name";
    public static final String DESCRIPTION = "issue_description";
    public static final String TYPE = "issue_type";
    public static final String STATUS = "issue_status";
    public static final String PRIORITY = "issue_priority";
    public static final String ASSIGNEE = "issue_assignee";
    public static final String ASSIGNEE_NAME = "assignee_name";
    public static final String HISTORY_CREATION_DATE = "creation_date";
    public static final String DUE_DATE = "issue_due_date";

    private Integer id;        // ID of the Issue being modified.
    private String subject;     // Issue Subject, usually a one-line description of what this issue is about.
    private String description;     // Issue description. For creating a new issue.
    private Integer status;         // Issue Status custom attribute.
    private String statusName;
    private Integer type;           // Issue Type custom attribute.
    private String typeName;
    private Integer priority;       // Issue Priority custom attribute.
    private String priorityName;
    private Integer resolution;     // Issue Resolution custom attribute.
    private String resolutionName;
    private String url;         // Issue URL. The URI of where the Issue came from.
    private String followup;    // Issue Follow Up. For adding information to an Issue.
    private AccessUser assignee = new AccessUser(); // Issue Assigned to. This is the same as owner of the Issue.
    private boolean hasDueDate;
    private Date dueDate;
    private String dueDateDate = "";
    private String dueDateMonth = "";
    private String dueDateYear = "";
    private String fromEmail;
    private String creatorIP;
    private Integer fileId;
    private List<Integer> selectedSubscribers = new ArrayList<>();
    private Integer proxyUserId;

    public Issue() throws DatabaseException {
        super(ObjectTypes.ISSUE);

        // Issue Status custom attribute.
        status = new CacheManager().getSystemAttrCache(Attributes.ISSUE_STATUS).getDefaultAttrFieldId();

        // Issue Type custom attribute.
        type = new CacheManager().getSystemAttrCache(Attributes.ISSUE_TYPE).getDefaultAttrFieldId();

        // Issue Priority custom attribute.
        priority = new CacheManager().getSystemAttrCache(Attributes.ISSUE_PRIORITY).getDefaultAttrFieldId();

        // Issue Resolution custom attribute.
        resolution = new CacheManager().getSystemAttrCache(Attributes.ISSUE_RESOLUTION).getDefaultAttrFieldId();

        hasDueDate = false;
    }

    public boolean isAttrEmpty(String attrName) {
        if (attrName.equals(Issue.ASSIGNEE)) {
            return !hasAssignee();
        }
        return false;
    }

    public void setDueDate(String year, String month, String date) {
        dueDateYear = year;
        dueDateMonth = month;
        dueDateDate = date;
    }

    /**
     * Returns whether the issue has assignee.
     * @return
     */
    public boolean hasAssignee() {
        return assignee != null && assignee.getId() != 0;
    }

    public boolean isValidDueDate() {
        return DatetimeUtils.isValidDate(dueDateYear, dueDateMonth, dueDateDate);
    }

    /**
     * Speficifies the sortable columns allowed.
     */
    public static List<String> getSortableHistoryColumns() {
        return Arrays.asList("creation_date");
    }

    /**
     * Checks whether a column is sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableHistoryColumn(String columnName) {
        return getSortableHistoryColumns().contains(columnName);
    }

    /**
     * Returns required fields.
     */
    private static List<String> getIssueRequiredFields() {
        return Arrays.asList("issue_name", "issue_description");
    }

    /**
     * Returns whether the column is a required field.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isIssueRequiredField(String columnName) {
        return getIssueRequiredFields().contains(columnName);
    }

    //
    // Getters and setters
    //
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public Integer getResolution() {
        return resolution;
    }
    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getFollowup() {
        return followup;
    }
    public void setFollowup(String followup) {
        this.followup = followup;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCreatorIP() {
        return creatorIP;
    }
    public void setCreatorIP(String creatorIP) {
        this.creatorIP = creatorIP;
    }
    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
    public Integer getFileId() {
        return fileId;
    }
    public List<Integer> getSelectedSubscribers() {
        return selectedSubscribers;
    }

    public boolean isHasDueDate() {
        return hasDueDate;
    }
    public void setHasDueDate(boolean hasDueDate) {
        this.hasDueDate = hasDueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public Date getDueDate() {
        return dueDate;
    }

    public String getDueDateShort() {
        return DatetimeUtils.toShortDate(dueDate);
    }

    public String getDueDateDate() {
        return dueDateDate;
    }
    public String getDueDateMonth() {
        return dueDateMonth;
    }
    public String getDueDateYear() {
        return dueDateYear;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getPriorityName() {
        return priorityName;
    }
    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }
    public String getResolutionName() {
        return resolutionName;
    }
    public void setResolutionName(String resolutionName) {
        this.resolutionName = resolutionName;
    }
    public void setDueDateDate(String dueDateDate) {
        this.dueDateDate = dueDateDate;
    }
    public void setDueDateMonth(String dueDateMonth) {
        this.dueDateMonth = dueDateMonth;
    }
    public void setDueDateYear(String dueDateYear) {
        this.dueDateYear = dueDateYear;
    }
    public String getFromEmail() {
        return fromEmail;
    }
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public AccessUser getAssignee() {
        return assignee;
    }

    public void setAssignee(AccessUser assignee) {
        this.assignee = assignee;
    }

    public Integer getProxyUserId() {
        return proxyUserId;
    }

    public void setProxyUserId(Integer proxyUserId) {
        this.proxyUserId = proxyUserId;
    }
}
