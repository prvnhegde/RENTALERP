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

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.framework.http.RequestContext;

import java.util.List;

/**
 * Action form for reports.
 */
public class ReportForm extends BaseForm {

    private String reportType;

    private String outputType = ReportUtils.OUTPUT_FORMAT_CSV_TABLE;

    private String reportTitle;

    private List<String> reportColumns;

    private String reportSortColumns;

    private String reportSortOrder;

    public void setRequest(RequestContext requestContext) {
        reportType = requestContext.getParameterString("reportType", reportType);
        outputType = requestContext.getParameterString("outputType", outputType);
        reportTitle = requestContext.getParameterString("reportTitle", reportTitle);
        reportColumns = requestContext.getParameterStrings("reportColumns", reportColumns);
        reportSortColumns = requestContext.getParameterString("reportSortColumns", reportSortColumns);
        reportSortOrder = requestContext.getParameterString("reportSortOrder", reportSortOrder);
    }

    public String getReportType() {
        return reportType;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public List<String> getReportColumns() {
        return reportColumns;
    }

    public String getReportSortColumns() {
        return reportSortColumns;
    }

    public String getReportSortOrder() {
        return reportSortOrder;
    }
}