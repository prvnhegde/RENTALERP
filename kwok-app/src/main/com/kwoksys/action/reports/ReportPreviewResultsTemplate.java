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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.biz.system.core.AppPaths;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

public class ReportPreviewResultsTemplate extends BaseTemplate {

    private String reportType;

    private List<LabelValueBean> reportColumnOptions = new ArrayList<>();

    private List<LabelValueBean> sortColumnOptions = new ArrayList<>();

    public ReportPreviewResultsTemplate() {
        super(ReportPreviewResultsTemplate.class);
    }

    public void init() {}

    public void applyTemplate() throws Exception {
        request.setAttribute("formAction", AppPaths.ROOT + AppPaths.REPORTS_RESULTS_EXPORT);
        request.setAttribute("backAction", AppPaths.ROOT + AppPaths.REPORTS_SEARCH_CRITERIA + "?reportType=" + reportType);
        request.setAttribute("reportType", reportType);
        request.setAttribute("outputOptions", ReportUtils.getOutputOptions());
        request.setAttribute("reportColumnOptions", reportColumnOptions);
        request.setAttribute("reportSortColumnOptions", sortColumnOptions);
        request.setAttribute("reportSortOrderOptions", ReportUtils.getReportSortOrderOptions(requestContext));
    }

    public void addReportColumnOptions(LabelValueBean labelValueBean) {
        this.reportColumnOptions.add(labelValueBean);
    }

    public void addSortColumnOptions(LabelValueBean labelValueBean) {
        this.sortColumnOptions.add(labelValueBean);
    }

    public List getReportColumnOptions() {
        return reportColumnOptions;
    }

    public List getSortColumnOptions() {
        return sortColumnOptions;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
}
