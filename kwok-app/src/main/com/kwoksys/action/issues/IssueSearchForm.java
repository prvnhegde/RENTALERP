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

import com.kwoksys.action.reports.ReportForm;
import com.kwoksys.framework.http.RequestContext;

import java.util.List;

/**
 * Action class for issue search.
 */
public class IssueSearchForm extends ReportForm {

    private String cmd;
    private String subject;
    private String description;
    private String url;
    private List<Integer> status;
    private List<Integer> priority;
    private List<Integer> type;
    private String issueId;
    private String assignedTo;
    private String submitter;
    private String submissionDate;
    private String dueWithin;
    private String submittedAfterMonth;
    private String submittedAfterDate;
    private String submittedAfterYear;
    private String submittedBeforeMonth;
    private String submittedBeforeDate;
    private String submittedBeforeYear;
    private String attrId;
    private String attrValue;
    private List<Integer> issueIds;

    @Override
    public void setRequest(RequestContext requestContext) {
        cmd = requestContext.getParameterString("cmd");
        subject = requestContext.getParameterString("subject");
        description = requestContext.getParameterString("description");
        url = requestContext.getParameterString("url");
        status = requestContext.getParameters("status");
        priority = requestContext.getParameters("priority");
        type = requestContext.getParameters("type");
        issueId = requestContext.getParameterString("issueId");
        assignedTo = requestContext.getParameterString("assignedTo");
        submitter = requestContext.getParameterString("submitter");
        submissionDate = requestContext.getParameterString("submissionDate");
        dueWithin = requestContext.getParameterString("dueWithin");
        submittedAfterMonth = requestContext.getParameterString("submittedAfterMonth");
        submittedAfterDate = requestContext.getParameterString("submittedAfterDate");
        submittedAfterYear = requestContext.getParameterString("submittedAfterYear");
        submittedBeforeMonth = requestContext.getParameterString("submittedBeforeMonth");
        submittedBeforeDate = requestContext.getParameterString("submittedBeforeDate");
        submittedBeforeYear = requestContext.getParameterString("submittedBeforeYear");
        attrId = requestContext.getParameterString("attrId");
        attrValue = requestContext.getParameterString("attrValue");
        issueIds = requestContext.getParameters("issueIds");
    }

    public String getCmd() {
        return cmd;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public List<Integer> getPriority() {
        return priority;
    }

    public List<Integer> getType() {
        return type;
    }
    public void setType(List<Integer> type) {
        this.type = type;
    }
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    public String getAssignedTo() {
        return assignedTo;
    }

    public String getSubmitter() {
        return submitter;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public String getDueWithin() {
        return dueWithin;
    }

    public String getSubmittedAfterMonth() {
        return submittedAfterMonth;
    }

    public String getSubmittedAfterYear() {
        return submittedAfterYear;
    }

    public String getSubmittedAfterDate() {
        return submittedAfterDate;
    }

    public String getSubmittedBeforeMonth() {
        return submittedBeforeMonth;
    }

    public String getSubmittedBeforeDate() {
        return submittedBeforeDate;
    }

    public String getSubmittedBeforeYear() {
        return submittedBeforeYear;
    }

    public String getAttrId() {
        return attrId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public List<Integer> getIssueIds() {
        return issueIds;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}