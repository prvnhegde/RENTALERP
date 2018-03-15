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

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.NumberUtils;

/**
 * Action class for editing issue.
 */
public class IssueEditAction extends Action2 {

    public String edit() throws Exception {
        IssueForm actionForm = getBaseForm(IssueForm.class);

        IssueService issueService = ServiceProvider.getIssueService(requestContext);
        Issue issue = issueService.getIssue(actionForm.getIssueId());

        // Load attributes
        issue.loadAttrs(requestContext);
        
        // Not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setIssue(issue);
        }

        // Only shows users whose status is "Enable", plus issue assignee.
        List<LabelValueBean> assigneeOptions = new ArrayList<>();
        assigneeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.colName.unassigned"), "0"));
        assigneeOptions.addAll(AdminUtils.getUserOptions(requestContext, issue.getAssignee().getId()));

        // Get a list of available subscribers.
        List<LabelValueBean> availableSubscribers = new ArrayList<>();
        for (AccessUser availableSubscriber : issueService.getAvailableSubscribers(issue.getId())) {
            availableSubscribers.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, availableSubscriber), String.valueOf(availableSubscriber.getId())));
        }

        // Get a list of selected subscribers.
        List<LabelValueBean> selectedSubscribers = new ArrayList<>();
        for (AccessUser selectedSubscriber : issueService.getSelectedSubscribers(issue.getId())) {
            selectedSubscribers.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, selectedSubscriber), String.valueOf(selectedSubscriber.getId())));
        }

        // Issue due date: year
        int dueDateYear = NumberUtils.replaceNull(actionForm.getDueDateYear(), 0);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("issue", issue);
        standardTemplate.setPathAttribute("formAction", AppPaths.ISSUES_EDIT_2);
        standardTemplate.setPathAttribute("formThisAction", AppPaths.ISSUES_EDIT);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId()).getString());
        request.setAttribute("emailNotification", ConfigManager.email.isIssueNotificationFromUiEnabled());

        standardTemplate.setAttribute("statusOptions", new AttributeManager(requestContext)
                .setSelectedAttrFieldId(issue.getStatus()).getActiveAttrFieldOptionsCache(Attributes.ISSUE_STATUS));

        standardTemplate.setAttribute("priorityOptions", new AttributeManager(requestContext)
                .setSelectedAttrFieldId(issue.getPriority()).getActiveAttrFieldOptionsCache(Attributes.ISSUE_PRIORITY));

        standardTemplate.setAttribute("typeOptions", new AttributeManager(requestContext)
                .setSelectedAttrFieldId(issue.getType()).getActiveAttrFieldOptionsCache(Attributes.ISSUE_TYPE));

        standardTemplate.setAttribute("resolutionOptions", new AttributeManager(requestContext).setOptional(true)
                .setSelectedAttrFieldId(issue.getResolution()).getActiveAttrFieldOptionsCache(Attributes.ISSUE_RESOLUTION));

        request.setAttribute("assignedToOptions", assigneeOptions);
        request.setAttribute("availableSubscribersOptions", availableSubscribers);
        request.setAttribute("selectedSubscribersOptions", selectedSubscribers);
        // If issue has due date, disabling of issue due date is set to false
        request.setAttribute("formDisableIssueDueDate", actionForm.getHasDueDate() != 1);
        request.setAttribute("dueDateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("dueMonthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("dueYearOptions", CalendarUtils.getExtraYearOptions(requestContext, dueDateYear));

        //
        // Template: IssueSpecTemplate
        //
        IssueSpecTemplate spec = standardTemplate.addTemplate(new IssueSpecTemplate(issue));
        spec.setHeaderText(Localizer.getText(requestContext, "issueMgmt.issueEdit.header"));
        
        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessageKey("issueMgmt.issueEdit.issueUpdateSectionTitle");

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.ISSUE);
        customFieldsTemplate.setObjectId(issue.getId());
        customFieldsTemplate.setObjectAttrTypeId(actionForm.getType());
        customFieldsTemplate.setForm(actionForm);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        IssueForm actionForm = saveActionForm(new IssueForm());
        Issue issue = issueService.getIssue(actionForm.getIssueId());
        
        // Need to get previous assignee because the user needs to be notified too.
        Integer prevAssigneeId = issue.getAssignee().getId();  
        issue.setSubject(actionForm.getSubject());
        issue.setFollowup(actionForm.getFollowup());
        issue.setStatus(actionForm.getStatus());
        issue.setType(actionForm.getType());
        issue.setPriority(actionForm.getPriority());
        issue.setResolution(actionForm.getResolution());
        issue.getAssignee().setId(actionForm.getAssignedTo());
        issue.getSelectedSubscribers().addAll(actionForm.getSelectedSubscribers());
        issue.setFromEmail("");

        issue.setHasDueDate(actionForm.getHasDueDate() == 1);
        if (actionForm.getHasDueDate() == 1) {
            issue.setDueDate(actionForm.getDueDateYear(), actionForm.getDueDateMonth(), actionForm.getDueDateDate());
        }

        // Get custom field values from request
        AttributeManager attributeManager = new AttributeManager(requestContext);
        Map<Integer, Attribute> customAttributes = attributeManager.getCustomFieldMap(ObjectTypes.ISSUE);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, issue, customAttributes);

        // Perform the update and see if there is any error.
        ActionMessages errors = issueService.updateIssue(issue, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ISSUES_EDIT + "?issueId=" + issue.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Send an email to assignee/subscribers.
            if (ConfigManager.email.isIssueNotificationFromUiEnabled() && actionForm.getSuppressNotification() != 1) {
                issueService.sendUpdateIssueNotification(issue, prevAssigneeId);
            }

            return ajaxUpdateView(AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());
        }
    }
}
