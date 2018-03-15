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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.auth.core.Permissions;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Keywords;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.NumberUtils;

/**
 * Action class for adding issue.
 */
public class IssueAddAction extends Action2 {

    private static final Logger LOGGER = Logger.getLogger(IssueAddAction.class.getName());
    
    public String add() throws Exception {
        AttributeManager attributeManager = new AttributeManager(requestContext);

        AccessUser accessUser = requestContext.getUser();

        String linkedObjectTypeId = requestContext.getParameterString("linkedObjectTypeId");
        request.setAttribute("linkedObjectTypeId", linkedObjectTypeId);

        String linkedObjectId = requestContext.getParameterString("linkedObjectId");
        request.setAttribute("linkedObjectId", linkedObjectId);

        // Instantiate Issue class.
        IssueForm actionForm = getBaseForm(IssueForm.class);
        Issue issue = new Issue();

        // Load attributes
        issue.loadAttrs(requestContext);
        
        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            IssueUtils.setDefaultDueDate(requestContext, issue);
            actionForm.setIssue(issue);
            actionForm.setCreator(0);
        }

        if (accessUser.hasPermission(Permissions.ISSUE_PROXY_SUBMIT)) {
            if (actionForm.getCreator() != 0) {
                request.setAttribute("hideIssueCreatedBy", "display:none");
            } else {
                request.setAttribute("hideIssueCreatedBySelect", "display:none");
            }

            request.setAttribute("changeSubmitterLink", new Link(requestContext).setTitleKey("issues.changeSubmitter")
                    .setJavascript("Js.Display.hide('issueCreatedBy');Js.Display.show('issueCreatedBySelect');").getString());

            request.setAttribute("changeSubmitterSelfLink", new Link(requestContext).setTitleKey("issues.changeSubmitter.self")
                    .setJavascript("Js.Display.hide('issueCreatedBySelect');Js.Display.show('issueCreatedBy');")
                    .getString());
        }
        request.setAttribute("createdBy", AdminUtils.getSystemUsername(requestContext, accessUser));

        // Get a list of Users whose status is "Enable" for assignee and subscribers
        UserSearch userSearch = new UserSearch();
        userSearch.put(UserSearch.USER_STATUS, AttributeFieldIds.USER_STATUS_ENABLED);

        QueryCriteria query = new QueryCriteria(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        List<AccessUser> users = adminService.getUsers(query);

        List<LabelValueBean> assigneeOptions = new ArrayList<>();
        assigneeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.colName.unassigned"), "0"));
        for (AccessUser user : users) {
            assigneeOptions.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, user), String.valueOf(user.getId())));
        }

        List<LabelValueBean> subscribersOptions = new ArrayList<>();
        for (AccessUser user : users) {
            subscribersOptions.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, user), String.valueOf(user.getId())));
        }

        List<LabelValueBean> creatorOptions = new ArrayList<>();
        creatorOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        for (AccessUser user : users) {
            creatorOptions.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, user), String.valueOf(user.getId())));
        }

        request.setAttribute("creatorOptions", creatorOptions);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("issue", issue);
        standardTemplate.setPathAttribute("formAction", AppPaths.ISSUES_ADD_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.ISSUES_ADD);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ISSUES_LIST).getString());
        request.setAttribute("emailNotification", ConfigManager.email.isIssueNotificationFromUiEnabled());
        request.setAttribute("statusOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_STATUS));
        request.setAttribute("priorityOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_PRIORITY));
        request.setAttribute("typeOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.ISSUE_TYPE));
        request.setAttribute("resolutionOptions", new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.ISSUE_RESOLUTION));
        request.setAttribute("assignedToOptions", assigneeOptions);
        request.setAttribute("availableSubscribersOptions", subscribersOptions);
        // New issue doesn't have any selected subscribers
        request.setAttribute("selectedSubscribersOptions", new ArrayList<>());
        // If issue has due date, disabling of issue due date is set to false
        request.setAttribute("formDisableIssueDueDate", actionForm.getHasDueDate() != 1);
        request.setAttribute("dueDateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("dueMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("dueYearOptions", CalendarUtils.getYearOptions(requestContext));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("issueMgmt.cmd.issueAdd");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.ISSUE);
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getType());
        customFieldsTemplate.setForm(actionForm);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("issueMgmt.issueAdd.sectionHeader");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add2() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        IssueForm actionForm = saveActionForm(new IssueForm());
        Issue issue = new Issue();
        issue.setSubject(actionForm.getSubject());
        issue.setDescription(actionForm.getDescription());
        issue.setStatus(actionForm.getStatus());
        issue.setType(actionForm.getType());
        issue.setPriority(actionForm.getPriority());
        issue.setResolution(actionForm.getResolution());
        issue.getAssignee().setId(actionForm.getAssignedTo());
        issue.getSelectedSubscribers().addAll(actionForm.getSelectedSubscribers());

        if (accessUser.hasPermission(Permissions.ISSUE_PROXY_SUBMIT)) {
            issue.setProxyUserId(actionForm.getCreator());
        }
        issue.setCreatorIP(request.getRemoteAddr());

        issue.setHasDueDate(actionForm.getHasDueDate() == 1);
        if (actionForm.getHasDueDate() == 1) {
            issue.setDueDate(actionForm.getDueDateYear(), actionForm.getDueDateMonth(), actionForm.getDueDateDate());
        }

        // Get custom field values from request
        AttributeManager attributeManager = new AttributeManager(requestContext);
        Map<Integer, Attribute> customAttributes = attributeManager.getCustomFieldMap(ObjectTypes.ISSUE);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, issue, customAttributes);
        
        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        // Add the issue
        ActionMessages errors = issueService.addIssue(issue, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ISSUES_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            if (ConfigManager.email.isIssueNotificationFromUiEnabled() && actionForm.getSuppressNotification() != 1) {
                try {
                    // Send an email to assignee.
                    // Here, we need to get the issue again because otherwise, we don't have creator name and creation date.
                    Issue updatedIssue = issueService.getPublicIssue(issue.getId());

                    String emailBody = ConfigManager.email.getIssueAddEmailTemplate().isEmpty() ?
                                            Localizer.getText(requestContext, "issues.issueAdd2.emailBody") : ConfigManager.email.getIssueAddEmailTemplate();

                    emailBody = emailBody.replace(Keywords.ISSUE_ASSIGNEE_VAR, AdminUtils.getSystemUsername(requestContext, updatedIssue.getAssignee()))
                            .replace(Keywords.ISSUE_ID_VAR, String.valueOf(updatedIssue.getId()))
                            .replace(Keywords.ISSUE_REPORTED_BY_VAR, AdminUtils.getSystemUsername(requestContext, updatedIssue.getCreator()))
                            .replace(Keywords.ISSUE_REPORTED_DATE_VAR, updatedIssue.getCreationDate())
                            .replace(Keywords.ISSUE_STATUS_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_STATUS, updatedIssue.getStatus()))
                            .replace(Keywords.ISSUE_PRIORITY_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_PRIORITY, updatedIssue.getPriority()))
                            .replace(Keywords.ISSUE_TYPE_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_TYPE, updatedIssue.getType()))
                            .replace(Keywords.ISSUE_DESCRIPTION_VAR, updatedIssue.getDescription())
                            .replace(Keywords.ISSUE_URL_VAR, ConfigManager.system.getAppUrl() + AppPaths.ROOT + AppPaths.ISSUES_DETAIL + "?issueId=" + updatedIssue.getId());

                    issueService.sendMail(updatedIssue, null, emailBody);
                    
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Problem sending issue notification", e);
                }
            }
            
            Integer objectTypeId = NumberUtils.replaceNull(actionForm.getLinkedObjectTypeId());
            Integer objectId = NumberUtils.replaceNull(actionForm.getLinkedObjectId());

            if (objectTypeId != 0 && objectId != 0) {
                if (objectTypeId.equals(ObjectTypes.HARDWARE)) {
                    return ajaxUpdateView(AppPaths.HARDWARE_ISSUE_ADD_2 + "?issueId=" + issue.getId() + "&hardwareId=" + actionForm.getLinkedObjectId());

                } else if (objectTypeId.equals(ObjectTypes.SOFTWARE)) {
                    return ajaxUpdateView(AppPaths.SOFTWARE_ISSUE_ADD_2 + "?issueId=" + issue.getId() + "&softwareId=" + actionForm.getLinkedObjectId());

                } else if (objectTypeId.equals(ObjectTypes.COMPANY)) {
                    return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_ISSUE_ADD_2 + "?issueId=" + issue.getId() + "&companyId=" + actionForm.getLinkedObjectId());
                }
                return null;
                
            } else {
                return ajaxUpdateView(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());
            }
        }
    }
}
