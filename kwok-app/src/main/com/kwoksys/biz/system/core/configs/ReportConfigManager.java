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
package com.kwoksys.biz.system.core.configs;

/**
 * ReportConfigManager
 */
public class ReportConfigManager extends BaseConfigManager {

    private String contactsReportFilename;

    private String contractsReportFilename;

    private String issuesReportFilename;

    private String hardwareReportFilename;

    private String hardwareMembersReportFilename;
    
    private String hardwareLicensesReportFilename;

    private String softwareReportFilename;

    private String softwareUsageReportFilename;

    private static ReportConfigManager instance = new ReportConfigManager();

    private ReportConfigManager() {}

    static ReportConfigManager getInstance() {
        return instance;
    }

    /**
     * Initializes configurations.
     * @param configMap
     * @return
     */
    void init(ConfigManager configManager) {
        contactsReportFilename = configManager.getString(ConfigKeys.CONTACT_REPORT);

        contractsReportFilename = configManager.getString(ConfigKeys.CONTRACT_REPORT);
        
        issuesReportFilename = configManager.getString(ConfigKeys.ISSUE_REPORT);

        hardwareReportFilename = configManager.getString(ConfigKeys.HARDWARE_REPORT);

        hardwareMembersReportFilename = configManager.getString(ConfigKeys.HARDWARE_MEMBER_REPORT);
        
        hardwareLicensesReportFilename = configManager.getString(ConfigKeys.HARDWARE_LICENSE_REPORT);

        softwareReportFilename = configManager.getString(ConfigKeys.SOFTWARE_REPORT);

        softwareUsageReportFilename = configManager.getString(ConfigKeys.SOFTWARE_USAGE_REPORT);
    }

    public String getContactsReportFilename() {
        return contactsReportFilename;
    }

    public String getContractsReportFilename() {
        return contractsReportFilename;
    }

    public String getIssuesReportFilename() {
        return issuesReportFilename;
    }

    public String getHardwareReportFilename() {
        return hardwareReportFilename;
    }

    public String getSoftwareReportFilename() {
        return softwareReportFilename;
    }

    public String getSoftwareUsageReportFilename() {
        return softwareUsageReportFilename;
    }

    public String getHardwareMembersReportFilename() {
        return hardwareMembersReportFilename;
    }

    public String getHardwareLicensesReportFilename() {
        return hardwareLicensesReportFilename;
    }
}
