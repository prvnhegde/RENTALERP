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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * IssueSearchTemplate
 */
public class IssueSearchTemplate extends BaseTemplate {

    private String formAction;
    private boolean hideSearchButton;
    private String clearSearchLink;

    public IssueSearchTemplate() {
        super(IssueSearchTemplate.class);
    }

    public void init() {}

    public void applyTemplate() throws DatabaseException {
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        QueryCriteria userQuery = new QueryCriteria();
        userQuery.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        LabelValueBean selectOneLabel = new SelectOneLabelValueBean(requestContext);

        List<LabelValueBean> typeOptions = new ArrayList<>();
        typeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.selectType"), ""));
        typeOptions.addAll(new AttributeManager(requestContext).getAttrFieldOptionsCache(Attributes.ISSUE_TYPE));

        List<LabelValueBean> statusOptions = new ArrayList<>();
        statusOptions.add(new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.selectStatus"), ""));
        statusOptions.addAll(new AttributeManager(requestContext).getAttrFieldOptionsCache(Attributes.ISSUE_STATUS));

        List<LabelValueBean> priorityOptions = new ArrayList<>();
        priorityOptions.add(new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.selectPriority"), ""));
        priorityOptions.addAll(new AttributeManager(requestContext).getAttrFieldOptionsCache(Attributes.ISSUE_PRIORITY));

        // Create assignee options
        List<LabelValueBean> assigneeOptions = new ArrayList<>();
        assigneeOptions.add(selectOneLabel);

        for (AccessUser assignee : adminService.getUsers(userQuery)) {
            assigneeOptions.add(new LabelValueBean(AdminUtils.getSystemUsername(requestContext, assignee), String.valueOf(assignee.getId())));
        }

        // Create submission date range
        List<LabelValueBean> submissionOptions = Arrays.asList(selectOneLabel,
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.submittedWithin.last1days"), "last1days"),
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.submittedWithin.last7days"), "last7days"),
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.submittedWithin.last14days"), "last14days"),
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.submittedWithin.last30days"), "last30days"));

        // Create due within options
        List<LabelValueBean> dueWithinOptions = Arrays.asList(selectOneLabel,
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.dueIn.1day"), "1day"),
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.dueIn.1week"), "7days"),
                new LabelValueBean(Localizer.getText(requestContext, "issueMgmt.index.dueIn.1month"), "30days"));

        request.setAttribute("typeOptions", typeOptions);
        request.setAttribute("statusOptions", statusOptions);
        request.setAttribute("priorityOptions", priorityOptions);
        request.setAttribute("IssueSearchTemplate_assigneeOptions", assigneeOptions);
        request.setAttribute("IssueSearchTemplate_submittedWithinOptions", submissionOptions);
        request.setAttribute("IssueSearchTemplate_dueWithinOptions", dueWithinOptions);
        request.setAttribute("monthOptions", CalendarUtils.getMonthOptions(requestContext));
        request.setAttribute("dateOptions", CalendarUtils.getDateOptions(requestContext));
        request.setAttribute("yearOptions", CalendarUtils.getPastYearOptions(requestContext));
        request.setAttribute("customFieldsOptions", new AttributeManager(requestContext).getCustomFieldOptions(ObjectTypes.ISSUE));

        if (!hideSearchButton) {
            clearSearchLink = new Link(requestContext).setAjaxPath(AppPaths.ISSUES_INDEX + "?cmd=clear").setTitleKey("common.form.clearCriteria").getString();
        }
    }

    public String getFormAction() {
        return formAction;
    }
    public void setFormAction(String formAction) {
        this.formAction = AppPaths.ROOT + formAction;
    }

    public boolean getHideSearchButton() {
        return hideSearchButton;
    }

    public void setHideSearchButton(boolean hideSearchButton) {
        this.hideSearchButton = hideSearchButton;
    }

    public String getClearSearchLink() {
        return clearSearchLink;
    }
}
