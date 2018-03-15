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

import com.kwoksys.biz.reports.types.IssueReport;
import com.kwoksys.biz.reports.writers.CsvReportWriter;
import com.kwoksys.biz.reports.writers.ReportWriter;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for issue list export.
 */
public class IssueListExportAction extends Action2 {

    public String execute() throws Exception {
        IssueReport report = new IssueReport(requestContext, SessionManager.ISSUE_SEARCH_CRITERIA_MAP);

        ResponseContext responseContext = new ResponseContext(response);
        responseContext.setAttachementName(ConfigManager.app.getIssuesExportFilename());

        ReportWriter reportWriter = new CsvReportWriter();
        reportWriter.init(responseContext, report);
        report.populateData(reportWriter);
        return reportWriter.close();
    }
}