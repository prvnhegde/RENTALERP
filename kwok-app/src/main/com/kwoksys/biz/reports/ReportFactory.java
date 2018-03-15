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
package com.kwoksys.biz.reports;

import com.kwoksys.action.reports.ReportForm;
import com.kwoksys.biz.reports.types.*;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.http.RequestContext;

/**
 * ReportFactory
 */
public class ReportFactory {

    public static Report getReport(RequestContext requestContext, ReportForm reportForm) throws Exception {
        return getReport(requestContext, reportForm.getReportType());
    }

    public static Report getReport(RequestContext requestContext, String reportType) throws Exception {
        if (reportType.equals(ReportUtils.ISSUE_REPORT_TYPE)) {
            return new IssueReport(requestContext, SessionManager.ISSUE_REPORT_CRITERIA_MAP);

        } else if (reportType.equals(ReportUtils.HARDWARE_REPORT_TYPE)) {
            return new HardwareReport(requestContext, SessionManager.HARDWARE_REPORT_CRITERIA_MAP);

        } else if (reportType.equals(ReportUtils.HARDWARE_MEMBER_REPORT_TYPE)) {
            return new HardwareMembersReport(requestContext, SessionManager.HARDWARE_REPORT_CRITERIA_MAP);

        } else if (reportType.equals(ReportUtils.HARDWARE_LICENSE_REPORT_TYPE)) {
            return new HardwareLicenseReport(requestContext, SessionManager.HARDWARE_REPORT_CRITERIA_MAP);

        } else if (reportType.equals(ReportUtils.SOFTWARE_REPORT_TYPE)) {
            return new SoftwareReport(requestContext, SessionManager.SOFTWARE_REPORT_CRITERIA_MAP);

        } else if (reportType.equals(ReportUtils.SOFTWARE_USAGE_REPORT_TYPE)) {
            return new SoftwareUsageReport(requestContext, SessionManager.SOFTWARE_USAGE_REPORT_CRITERIA_MAP);

        } else if (reportType.equals(ReportUtils.CONTACT_REPORT_TYPE)) {
            return new ContactReport(requestContext, SessionManager.COMPANY_SEARCH_CRITERIA_MAP);

        } else if (reportType.equals(ReportUtils.CONTRACT_REPORT_TYPE)) {
            return new ContractReport(requestContext, SessionManager.CONTRACT_REPORT_CRITERIA_MAP);

        } else {
            throw new Exception("Unsupported report type");
        }
    }
}
