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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.StringUtils;

/**
 * Action class for running a report.
 */
public class ReportTypeSelectionAction extends Action2 {

    public String execute() throws Exception {
        ReportForm reportForm = getSessionBaseForm(ReportForm.class);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.REPORTS_SEARCH_CRITERIA + "?cmd=clear");
        standardTemplate.setAttribute("reportTypeOptions", ReportUtils.getTypeOptions(requestContext));
        standardTemplate.setAttribute("reportType", reportForm.getReportType());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("reports.workflow.typeSelection.header");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        if (StringUtils.isEmpty(reportForm.getReportType())) {
            standardTemplate.getHeaderTemplate().setOnloadJavascript("document.ReportForm.submitBtn.disabled=true;");
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}