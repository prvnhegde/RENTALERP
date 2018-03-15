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
package com.kwoksys.action.reports;

import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.contracts.ContractSearchForm;
import com.kwoksys.action.contracts.ContractSearchTemplate;
import com.kwoksys.action.hardware.HardwareSearchForm;
import com.kwoksys.action.hardware.HardwareSearchTemplate;
import com.kwoksys.action.issues.IssueSearchForm;
import com.kwoksys.action.issues.IssueSearchTemplate;
import com.kwoksys.action.software.SoftwareSearchForm;
import com.kwoksys.action.software.SoftwareSearchTemplate;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.ReportAccess;
import com.kwoksys.biz.reports.Report;
import com.kwoksys.biz.reports.ReportFactory;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for running a report.
 */
public class ReportSearchCriteriaAction extends Action2 {

    public String execute() throws Exception {
        ReportForm reportForm = getSessionBaseForm(ReportForm.class);

        AccessUser user = requestContext.getUser();
        String reportType = reportForm.getReportType();
        String cmd = requestContext.getParameterString("cmd");

        if (cmd.equals("clear")) {
            SessionManager.clearReportFromSession(requestContext, reportType);
        }

        // Checks report permission
        if (!ReportAccess.hasPermission(user, reportType)) {
            throw new AccessDeniedException();
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        if (reportType.equals(ReportUtils.ISSUE_REPORT_TYPE)) {
            getSessionBaseForm(IssueSearchForm.class);

            //
            // Template: IssueSearchTemplate
            //
            IssueSearchTemplate searchTemplate = standardTemplate.addTemplate(new IssueSearchTemplate());
            searchTemplate.setFormAction(AppPaths.REPORTS_ISSUE_SEARCH);
            searchTemplate.setHideSearchButton(true);

        } else if (reportType.equals(ReportUtils.HARDWARE_REPORT_TYPE)
                || reportType.equals(ReportUtils.HARDWARE_MEMBER_REPORT_TYPE)
                || reportType.equals(ReportUtils.HARDWARE_LICENSE_REPORT_TYPE)) {

            getSessionBaseForm(HardwareSearchForm.class);

            //
            // Template: HardwareSearchTemplate
            //
            HardwareSearchTemplate searchTemplate = standardTemplate.addTemplate(new HardwareSearchTemplate());
            searchTemplate.setFormAction(AppPaths.REPORTS_HARDWARE_SEARCH);
            searchTemplate.setHideSearchButton(true);

        } else if (reportType.equals(ReportUtils.SOFTWARE_REPORT_TYPE)) {
            getSessionBaseForm(SoftwareSearchForm.class);

            //
            // Template: SoftwareSearchTemplate
            //
            SoftwareSearchTemplate searchTemplate = standardTemplate.addTemplate(new SoftwareSearchTemplate());
            searchTemplate.setFormAction(AppPaths.REPORTS_SOFTWARE_SEARCH);
            searchTemplate.setHideSearchButton(true);

        } else if (reportType.equals(ReportUtils.SOFTWARE_USAGE_REPORT_TYPE)) {
            getSessionBaseForm(SoftwareSearchForm.class);

            //
            // Template: SoftwareSearchTemplate
            //
            SoftwareSearchTemplate searchTemplate = standardTemplate.addTemplate(new SoftwareSearchTemplate());
            searchTemplate.setFormAction(AppPaths.REPORTS_SOFTWARE_USAGE_SEARCH);
            searchTemplate.setHideSearchButton(true);

        } else if (reportType.equals(ReportUtils.CONTRACT_REPORT_TYPE)) {
            getSessionBaseForm(ContractSearchForm.class);

            //
            // Template: ContractSearchTemplate
            //
            ContractSearchTemplate searchTemplate = standardTemplate.addTemplate(new ContractSearchTemplate());
            searchTemplate.setFormAction(AppPaths.REPORTS_CONTRACT_SEARCH);
            searchTemplate.setHideSearchButton(true);
        }

        Report report = ReportFactory.getReport(requestContext, reportType);
        standardTemplate.setAttribute("formName", report.getReportFormName());
        standardTemplate.setAttribute("reportType", reportType);
        standardTemplate.setPathAttribute("reportBackAction", AppPaths.REPORTS_TYPE_SELECT);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}