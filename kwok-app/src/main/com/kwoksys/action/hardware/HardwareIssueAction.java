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
package com.kwoksys.action.hardware;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.action.issues.IssueAssociateTemplate;
import com.kwoksys.action.issues.IssueListTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dto.linking.HardwareIssueLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Action class for managing hardware issues.
 */
public class HardwareIssueAction extends Action2 {

    public String list() throws Exception {
        HardwareForm actionForm = getBaseForm(HardwareForm.class);

        AccessUser user = requestContext.getUser();

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(HardwareQueries.getOrderByColumn(Issue.CREATION_DATE));

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});
        HardwareUtils.addHardwareHeaderCommands(requestContext, headerTemplate, hardware.getId());

        // Add hardware components.
        if (Access.hasPermission(user, AppPaths.HARDWARE_ISSUE_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_ISSUE_ADD + "?hardwareId=" + hardware.getId());
            link.setTitleKey("common.linking.linkIssue");
            headerTemplate.addHeaderCmds(link);
        }

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_ISSUE_TAB);

        //
        // Template: IssueListTemplate
        //
        IssueListTemplate listTemplate = standardTemplate.addTemplate(new IssueListTemplate());
        listTemplate.setAccessUser(user);
        listTemplate.setQueryCriteria(queryCriteria);
        listTemplate.setObjectMap(new HardwareIssueLink(hardware.getId()).createObjectMap());
        listTemplate.setFormRemoveItemAction(AppPaths.HARDWARE_ISSUE_REMOVE_2);
        listTemplate.setColumnHeaders(IssueUtils.getIssueColumnHeaders());
        listTemplate.setEmptyTableRowKey("issueMgmt.issueList.emptyTableMessage");
        listTemplate.getFormHiddenVariableMap().put("hardwareId", String.valueOf(hardware.getId()));
        listTemplate.setFormRowIdName("issueId");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add() throws Exception {
        HardwareForm actionForm = getBaseForm(HardwareForm.class);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("hardwareId", hardware.getId());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: IssueAssociateTemplate
        //
        IssueAssociateTemplate issueAdd = standardTemplate.addTemplate(new IssueAssociateTemplate());
        issueAdd.setIssueId(actionForm.getIssueId());
        issueAdd.setLinkedObjectTypeId(ObjectTypes.HARDWARE);
        issueAdd.setLinkedObjectId(hardware.getId());
        issueAdd.setFormSearchAction(AppPaths.HARDWARE_ISSUE_ADD);
        issueAdd.setFormSaveAction(AppPaths.HARDWARE_ISSUE_ADD_2);
        issueAdd.setFormCancelAction(AppPaths.HARDWARE_ISSUE + "?hardwareId=" + hardware.getId());

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add2() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");
        Integer issueId = requestContext.getParameter("issueId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        hardwareService.getHardware(hardwareId);

        HardwareIssueLink issueMap = new HardwareIssueLink();
        issueMap.setHardwareId(hardwareId);
        issueMap.setIssueId(issueId);

        ActionMessages errors = hardwareService.addHardwareIssue(issueMap);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_ISSUE_ADD + "?hardwareId=" + hardwareId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.HARDWARE_ISSUE + "?hardwareId=" + hardwareId);
        }
    }
    
    public String remove2() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");
        Integer issueId = requestContext.getParameter("issueId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        hardwareService.getHardware(hardwareId);

        HardwareIssueLink issueMap = new HardwareIssueLink();
        issueMap.setHardwareId(hardwareId);
        issueMap.setIssueId(issueId);

        // Delete contract hardware mapping.
        ActionMessages errors = hardwareService.deleteHardwareIssue(issueMap);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_ISSUE + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&hardwareId=" + hardwareId);

        } else {
            return ajaxUpdateView(AppPaths.HARDWARE_ISSUE + "?hardwareId=" + hardwareId);
        }
    }    
}
