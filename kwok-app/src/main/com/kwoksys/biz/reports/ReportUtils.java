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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.ReportAccess;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

/**
 * ReportUtil
 */
public class ReportUtils {

    public static final String ISSUE_REPORT_TYPE = "issue_report";

    public static final String HARDWARE_REPORT_TYPE = "hardware_report";

    public static final String HARDWARE_MEMBER_REPORT_TYPE = "hardware_member_report";

    public static final String HARDWARE_LICENSE_REPORT_TYPE = "hardware_license_report";

    public static final String SOFTWARE_REPORT_TYPE = "software_report";

    public static final String SOFTWARE_USAGE_REPORT_TYPE = "software_usage_report";

    public static final String CONTACT_REPORT_TYPE = "contact_report";

    public static final String CONTRACT_REPORT_TYPE = "contract_report";

    public static final String OUTPUT_FORMAT_CSV_TABLE = "csv_table";

    public static final String OUTPUT_FORMAT_HTML_TABLE = "html_table";

    public static final String OUTPUT_FORMAT_HTML_LIST = "html_list";

    public static final String OUTPUT_FORMAT_PDF_LIST = "pdf_list";

    public static List<LabelValueBean> getTypeOptions(RequestContext requestContext) throws DatabaseException {
        AccessUser user = requestContext.getUser();
        String[] reportTypes = {HARDWARE_REPORT_TYPE, HARDWARE_LICENSE_REPORT_TYPE, HARDWARE_MEMBER_REPORT_TYPE,
                SOFTWARE_REPORT_TYPE, SOFTWARE_USAGE_REPORT_TYPE, ISSUE_REPORT_TYPE, CONTRACT_REPORT_TYPE};

        List<LabelValueBean> options = new ArrayList<>();
        options.add(new SelectOneLabelValueBean(requestContext));
        for (String reportType : reportTypes) {
            if (ReportAccess.hasPermission(user, reportType)) {
                options.add(new LabelValueBean(Localizer.getText(requestContext, "reports.workflow.type." + reportType), reportType));
            }
        }
        return options;
    }

    public static List<String> getOutputOptions() {
        return Arrays.asList(OUTPUT_FORMAT_CSV_TABLE, OUTPUT_FORMAT_HTML_TABLE, OUTPUT_FORMAT_HTML_LIST, OUTPUT_FORMAT_PDF_LIST);
    }

    public static List<LabelValueBean> getReportSortOrderOptions(RequestContext requestContext) {
        List<LabelValueBean> options = new ArrayList<>();
        options.add(new LabelValueBean(Localizer.getText(requestContext, "core.sort.asc"), "asc"));
        options.add(new LabelValueBean(Localizer.getText(requestContext, "core.sort.desc"), "desc"));
        return options;
    }

    public static List<String> getSoftwareUsageColumns() {
        return Arrays.asList(BaseObject.ROWNUM, Software.NAME, Software.TYPE, Hardware.HARDWARE_NAME, Hardware.OWNER_NAME);
    }

    public static List<String> getSoftwareUsageSortableColumns() {
        return Arrays.asList(Software.NAME, Hardware.HARDWARE_NAME, Hardware.OWNER_NAME);
    }

    public static boolean isSoftwareUsageSortableColumn(String columnName) {
        return getSoftwareUsageSortableColumns().contains(columnName);
    }

    public static List<String> getSoftwareUsageExportColumns() {
        return Arrays.asList(Software.NAME, Software.DESCRIPTION, Software.TYPE,
                Hardware.HARDWARE_NAME, Hardware.OWNER_NAME);
    }

    public static List<String> getHardwareMembersExportColumns() {
        return Arrays.asList(Hardware.HARDWARE_NAME, "hardware_member_name");
    }

    public static List<String> getHardwareLicenseColumns() {
        return Arrays.asList(Hardware.HARDWARE_NAME, Software.NAME, SoftwareLicense.LICENSE_KEY);
    }
}
