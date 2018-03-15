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
package com.kwoksys.action.issues;

import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;

import java.util.List;

/**
 * ActionForm class for issue object.
 */
public class IssueForm extends BaseObjectForm {

    private Integer issueId;
    private String subject;
    private String description;
    private String followup;
    private Integer status;
    private Integer type;
    private Integer priority;
    private Integer resolution;
    private Integer assignedTo;
    private List<Integer> availableSubscribers;
    private List<Integer> selectedSubscribers;
    private int suppressNotification;
    private int hasDueDate;
    private String dueDateDate;
    private String dueDateMonth;
    private String dueDateYear;
    private String linkedObjectTypeId;
    private String linkedObjectId;
    private Integer creator;
    private Integer proxyCreator;

    @Override
    public void setRequest(RequestContext requestContext) {
        issueId = requestContext.getParameterInteger("issueId");
        subject = requestContext.getParameterString("subject");
        description = requestContext.getParameterString("description");
        followup = requestContext.getParameterString("followup");
        status = requestContext.getParameterInteger("status");
        type = requestContext.getParameterInteger("type");
        priority = requestContext.getParameterInteger("priority");
        resolution = requestContext.getParameterInteger("resolution");
        assignedTo = requestContext.getParameterInteger("assignedTo");
        availableSubscribers = requestContext.getParameters("availableSubscribers");
        selectedSubscribers = requestContext.getParameters("selectedSubscribers");
        suppressNotification = requestContext.getParameter("suppressNotification");
        hasDueDate = requestContext.getParameter("hasDueDate");
        dueDateDate = requestContext.getParameterString("dueDateDate");
        dueDateMonth = requestContext.getParameterString("dueDateMonth");
        dueDateYear = requestContext.getParameterString("dueDateYear");
        linkedObjectTypeId = requestContext.getParameterString("linkedObjectTypeId");
        linkedObjectId = requestContext.getParameterString("linkedObjectId");
        creator = requestContext.getParameterInteger("creator");
        proxyCreator = requestContext.getParameterInteger("proxyCreator");
    }

    public void setIssue(Issue issue) {
        subject = issue.getSubject();
        description= issue.getDescription();
        followup = issue.getFollowup();
        status = issue.getStatus();
        type = issue.getType();
        priority = issue.getPriority();
        resolution = issue.getResolution();
        suppressNotification = 0;
        assignedTo = issue.getAssignee().getId() != null ? issue.getAssignee().getId() : 0;
        hasDueDate = issue.isHasDueDate() ? 1 : 0;

        dueDateYear = DatetimeUtils.toYearString(issue.getDueDate());
        dueDateMonth = DatetimeUtils.toMonthString(issue.getDueDate());
        dueDateDate = DatetimeUtils.toDateString(issue.getDueDate());
    }

    public Integer getIssueId() {
        return issueId;
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

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public List<Integer> getAvailableSubscribers() {
        return availableSubscribers;
    }

    public List<Integer> getSelectedSubscribers() {
        return selectedSubscribers;
    }

    public int getSuppressNotification() {
        return suppressNotification;
    }

    public int getHasDueDate() {
        return hasDueDate;
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

    public String getLinkedObjectTypeId() {
        return linkedObjectTypeId;
    }

    public void setLinkedObjectTypeId(String linkedObjectTypeId) {
        this.linkedObjectTypeId = linkedObjectTypeId;
    }

    public String getLinkedObjectId() {
        return linkedObjectId;
    }

    public void setLinkedObjectId(String linkedObjectId) {
        this.linkedObjectId = linkedObjectId;
    }

    public Integer getProxyCreator() {
        return proxyCreator;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }
}
