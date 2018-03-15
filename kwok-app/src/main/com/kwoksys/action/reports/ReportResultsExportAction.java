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

import com.kwoksys.action.common.template.HeaderSimpleTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.ReportAccess;
import com.kwoksys.biz.reports.Report;
import com.kwoksys.biz.reports.ReportFactory;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.biz.reports.writers.CsvReportWriter;
import com.kwoksys.biz.reports.writers.HtmlReportWriter;
import com.kwoksys.biz.reports.writers.PdfReportWriter;
import com.kwoksys.biz.reports.writers.ReportWriter;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for running a report.
 */
public class ReportResultsExportAction extends Action2 {

    public String execute() throws Exception {
        ResponseContext responseContext = new ResponseContext(response);

        AccessUser user = requestContext.getUser();
        ReportForm reportForm = getSessionBaseForm(ReportForm.class);

        // Checks report permission
        if (!ReportAccess.hasPermission(user, reportForm.getReportType())) {
            throw new AccessDeniedException();
        }

        Report report = ReportFactory.getReport(requestContext, reportForm);
        report.setTitle(reportForm.getReportTitle());
        report.setReportColumns(reportForm.getReportColumns());
        report.setReportColumnOrder(reportForm.getReportSortOrder());
        report.setReportColumnOrderBy(reportForm.getReportSortColumns());

        if (reportForm.getOutputType().equals(ReportUtils.OUTPUT_FORMAT_PDF_LIST)) {
            ReportWriter reportWriter = new PdfReportWriter();

            responseContext.setAttachementName(report.getPdfFilename());

            reportWriter.init(responseContext, report);
            report.populateData(reportWriter);
            return reportWriter.close();

        } else if (reportForm.getOutputType().equals(ReportUtils.OUTPUT_FORMAT_CSV_TABLE)) {
            ReportWriter reportWriter = new CsvReportWriter();

            responseContext.setAttachementName(report.getCsvFilename());

            reportWriter.init(responseContext, report);
            report.populateData(reportWriter);
            return reportWriter.close();

        } else {
            //
            // Template: StandardTemplate
            //
            StandardTemplate standardTemplate = new StandardTemplate(requestContext);

            //
            // Template: HeaderSimpleTemplate
            //
            HeaderSimpleTemplate headerSimpleTemplate = standardTemplate.addTemplate(new HeaderSimpleTemplate());
            headerSimpleTemplate.setTitleText(Localizer.getText(requestContext, "reports.workflow.type." + reportForm.getReportType()));

            HtmlReportWriter reportWriter = new HtmlReportWriter();
            report.populateData(reportWriter);

            request.setAttribute("columnHeaders", reportWriter.getColumnHeaders());
            request.setAttribute("rows", reportWriter.getRows());
            request.setAttribute("reportTitle", report.getTitle());

            return standardTemplate.findTemplate(reportForm.getOutputType());
        }
    }
}
