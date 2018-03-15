/*
 * Copyright 2017 Kwoksys
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
package com.kwoksys.action.admin.config;

import java.util.List;

import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.framework.http.RequestContext;

/**
 * Action form for editing application configurations.
 */
public class ConfigForm extends BaseForm {

    private String cmd;
    private String applicationUrl;
    private String theme;
    private String stylesheet;
    private String homeCustomDescription;
    private String locale;
    private String timeFormat;
    private String shortDateFormat;
    private String currency;
    private String timezone;
    private String numberOfPastYears;
    private String numberOfFutureYears;
    private int companiesNumRows;
    private int contactsNumRows;
    private List<String> contractColumns;
    private int contractsNumRows;
    private int contractsExpireCountdown;
    private List<String> hardwareColumns;
    private int hardwareExpireCountdown;
    private int hardwareNumRows;
    private boolean checkUniqueHardwareName;
    private boolean checkUniqueSerialNumber;
    private boolean bulkHardwareDeleteEnabled;
    private List<String> softwareColumns;
    private int softwareNumRows;
    private String softwareLicneseNotesNumChars;
    private List<String> issuesColumns;
    private int issuesNumRows;
    private String issuesGuestSubmitModuleEnabled;
    private String issuesGuestSubmitEnabled;
    private String issuesMultipleDeleteEnabled;
    private String issuesDefaultDueDateDiff;
    private int usersNumRows;
    private String userNameDisplay;
    private int blogPostsListNumRows;
    private int blogPostCharactersList;
    private int kilobyteUnits;
    private String fileRepositoryCompany;
    private String fileRepositoryIssue;
    private String fileRepositoryContract;
    private String fileRepositoryHardware;
    private String fileRepositorySoftware;
    private String fileRepositoryKb;
    private List<String> kbColumns;
    private boolean kbMediaWikiSyntaxEnabled;
    private String authMethod;
    private String authType;
    private int authTimeout;
    private String ldapUrlScheme;
    private String ldapUrl;
    private String ldapUsername;
    private String ldapPassword;
    private String ldapSecurityPrincipal;
    private String domain;
    private int allowBlankUserPassword;
    private int minimumPasswordLength;
    private boolean passwordComplexityEnabled;
    private String companyName;
    private String companyPath;
    private String companyLogoPath;
    private String companyFooterNotes;
    private boolean issueNotificationFromUiOn;
    private boolean issueNotificationFromEmailOn;
    private boolean contractExpireNotificationOn;
    private String allowedDomains;
    private String smtpHost;
    private String smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private String smtpFrom;
    private String smtpTo;
    private boolean smtpStarttls;
    private String reportIssueEmailTemplate;
    private String issueAddEmailTemplate;
    private String issueUpdateEmailTemplate;
    private String contractExpireNotifyEmailTemplate;
    private String licenseKey;
    private String popHost;
    private String popPort;
    private String popUsername;
    private String popPassword;
    private boolean popUseSSL;
    private String popIgnoreSender;
    private int popRetrievalFrequency;
    private int popMessageBatchSize;
    private boolean test;
    private String dbBackupRepositoryPath;
    private String dbBackupProgramPath;
    private String databaseAccessLogLevel;
    private String ldapLogLevel;
    private String schedulerLogLevel;
    private String templateLogLevel;
    private String accountLockoutThreshold;
    private String accountLockoutDurationMinutes;
    private boolean loadCustomFields;

    @Override
    public void setRequest(RequestContext requestContext) {
        cmd = requestContext.getParameterString("cmd");

        if (cmd.equals(AdminUtils.ADMIN_APP_EDIT_2_CMD)) {
            issuesColumns = requestContext.getParameterStrings("issuesColumns");
            hardwareColumns = requestContext.getParameterStrings("hardwareColumns");
            softwareColumns = requestContext.getParameterStrings("softwareColumns");
            contractColumns = requestContext.getParameterStrings("contractColumns");
            kbColumns = requestContext.getParameterStrings("kbColumns");

            licenseKey = requestContext.getParameterString("licenseKey");
            applicationUrl = requestContext.getParameterString("applicationUrl");
            currency = requestContext.getParameterString("currency");
            timezone = requestContext.getParameterString("timezone");
            locale = requestContext.getParameterString("locale");
            shortDateFormat = requestContext.getParameterString("shortDateFormat");
            timeFormat = requestContext.getParameterString("timeFormat");
            usersNumRows = requestContext.getParameter("usersNumRows");
            userNameDisplay = requestContext.getParameterString("userNameDisplay");
            companiesNumRows = requestContext.getParameter("companiesNumRows");
            contactsNumRows = requestContext.getParameter("contactsNumRows");
            contractsNumRows = requestContext.getParameter("contractsNumRows");
            contractsExpireCountdown = requestContext.getParameter("contractsExpireCountdown");
            issuesNumRows = requestContext.getParameter("issuesNumRows");
            issuesGuestSubmitModuleEnabled = requestContext.getParameterString("issuesGuestSubmitModuleEnabled");
            issuesGuestSubmitEnabled = requestContext.getParameterString("issuesGuestSubmitEnabled");
            issuesMultipleDeleteEnabled = requestContext.getParameterString("issuesMultipleDeleteEnabled");
            issuesDefaultDueDateDiff = requestContext.getParameterString("issuesDefaultDueDateDiff");
            hardwareNumRows = requestContext.getParameter("hardwareNumRows");
            hardwareExpireCountdown = requestContext.getParameter("hardwareExpireCountdown");
            blogPostsListNumRows = requestContext.getParameter("blogPostsListNumRows");
            blogPostCharactersList = requestContext.getParameter("blogPostCharactersList");
            numberOfPastYears = requestContext.getParameterString("numberOfPastYears");
            numberOfFutureYears = requestContext.getParameterString("numberOfFutureYears");
            softwareNumRows = requestContext.getParameter("softwareNumRows");
            softwareLicneseNotesNumChars = requestContext.getParameterString("softwareLicneseNotesNumChars");
            checkUniqueHardwareName = requestContext.getParameterBoolean("checkUniqueHardwareName");
            checkUniqueSerialNumber = requestContext.getParameterBoolean("checkUniqueSerialNumber");
            bulkHardwareDeleteEnabled = requestContext.getParameterBoolean("bulkHardwareDeleteEnabled");
            loadCustomFields = requestContext.getParameterBoolean("loadCustomFields");
            kbMediaWikiSyntaxEnabled = requestContext.getParameterBoolean("kbMediaWikiSyntaxEnabled");

        } else if (cmd.equals(AdminUtils.ADMIN_AUTH_EDIT_2_CMD)) {
            authType = requestContext.getParameterString("authType");
            authMethod = requestContext.getParameterString("authMethod");
            ldapUrl = requestContext.getParameterString("ldapUrl");
            ldapUrlScheme = requestContext.getParameterString("ldapUrlScheme");
            ldapSecurityPrincipal = requestContext.getParameterString("ldapSecurityPrincipal");
            domain = requestContext.getParameterString("domain");
            authTimeout = requestContext.getParameter("authTimeout");
            allowBlankUserPassword = requestContext.getParameter("allowBlankUserPassword");
            minimumPasswordLength = requestContext.getParameter("minimumPasswordLength");

            passwordComplexityEnabled = requestContext.getParameterBoolean("passwordComplexityEnabled");
            accountLockoutThreshold = requestContext.getParameterString("accountLockoutThreshold");
            accountLockoutDurationMinutes = requestContext.getParameterString("accountLockoutDurationMinutes");

        } else if (cmd.equals(AdminUtils.ADMIN_DB_BACKUP_EDIT_2_CMD)) {
            dbBackupProgramPath = requestContext.getParameterString("dbBackupProgramPath");
            dbBackupRepositoryPath = requestContext.getParameterString("dbBackupRepositoryPath");

        } else if (cmd.equals(AdminUtils.ADMIN_COMPANY_EDIT_2_CMD)) {
            companyName = requestContext.getParameterString("companyName");
            companyPath = requestContext.getParameterString("companyPath");
            companyLogoPath = requestContext.getParameterString("companyLogoPath");
            companyFooterNotes = requestContext.getParameterString("companyFooterNotes");

        } else if (cmd.equals(AdminUtils.ADMIN_EMAIL_SMTP_EDIT_2_CMD)){
            issueNotificationFromUiOn = requestContext.getParameterBoolean("issueNotificationFromUiOn");
            issueNotificationFromEmailOn = requestContext.getParameterBoolean("issueNotificationFromEmailOn");
            contractExpireNotificationOn = requestContext.getParameterBoolean("contractExpireNotificationOn");
            allowedDomains = requestContext.getParameterString("allowedDomains");
            smtpHost = requestContext.getParameterString("smtpHost");
            smtpPort = requestContext.getParameterString("smtpPort");
            smtpUsername = requestContext.getParameterString("smtpUsername");
            smtpPassword = requestContext.getParameterString("smtpPassword");
            smtpFrom = requestContext.getParameterString("smtpFrom");
            smtpTo = requestContext.getParameterString("smtpTo");
            smtpStarttls = requestContext.getParameterBoolean("smtpStarttls");
            reportIssueEmailTemplate = requestContext.getParameterString("reportIssueEmailTemplate");
            issueAddEmailTemplate = requestContext.getParameterString("issueAddEmailTemplate");
            issueUpdateEmailTemplate = requestContext.getParameterString("issueUpdateEmailTemplate");
            contractExpireNotifyEmailTemplate = requestContext.getParameterString("contractExpireNotifyEmailTemplate");
            test = requestContext.getParameterString("test").equals("1");

        } else if (cmd.equals(AdminUtils.ADMIN_FILE_EDIT_2_CMD)){
            kilobyteUnits = requestContext.getParameter("kilobyteUnits");
            fileRepositoryCompany = requestContext.getParameterString("fileRepositoryCompany");
            fileRepositoryIssue = requestContext.getParameterString("fileRepositoryIssue");
            fileRepositoryHardware = requestContext.getParameterString("fileRepositoryHardware");
            fileRepositorySoftware = requestContext.getParameterString("fileRepositorySoftware");
            fileRepositoryContract = requestContext.getParameterString("fileRepositoryContract");
            fileRepositoryKb = requestContext.getParameterString("fileRepositoryKb");

        } else if (cmd.equals(AdminUtils.ADMIN_LDAP_TEST_2_CMD)) {
            ldapUrlScheme = requestContext.getParameterString("ldapUrlScheme");
            ldapUrl = requestContext.getParameterString("ldapUrl");
            ldapUsername = requestContext.getParameterString("ldapUsername");
            ldapSecurityPrincipal = requestContext.getParameterString("ldapSecurityPrincipal");
            ldapPassword = requestContext.getParameterString("ldapPassword");

        } else if (cmd.equals(AdminUtils.ADMIN_LOGGING_EDIT_2_CMD)) {
            databaseAccessLogLevel = requestContext.getParameterString("databaseAccessLogLevel");
            ldapLogLevel = requestContext.getParameterString("ldapLogLevel");
            schedulerLogLevel = requestContext.getParameterString("schedulerLogLevel");
            templateLogLevel = requestContext.getParameterString("templateLogLevel");

        } else if (cmd.equals(AdminUtils.ADMIN_LOOK_FEEL_EDIT_2_CMD)) {
            theme = requestContext.getParameterString("theme");
            stylesheet = requestContext.getParameterString("stylesheet");
            homeCustomDescription = requestContext.getParameterString("homeCustomDescription");

        } else if (cmd.equals(AdminUtils.ADMIN_EMAIL_POP_EDIT_2_CMD)){
            popHost = requestContext.getParameterString("popHost");
            popPort = requestContext.getParameterString("popPort");
            popUsername = requestContext.getParameterString("popUsername");
            popUseSSL = requestContext.getParameterBoolean("popUseSSL");
            popIgnoreSender = requestContext.getParameterString("popIgnoreSender");
            popPassword = requestContext.getParameterString("popPassword");
            popRetrievalFrequency = requestContext.getParameter("popRetrievalFrequency");
            popMessageBatchSize = requestContext.getParameter("popMessageBatchSize");
            test = requestContext.getParameterString("test").equals("1");
        }
    }

    public String getCmd() {
        return cmd;
    }

    public String getLocale() {
        return locale;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
    public String getApplicationUrl() {
        return applicationUrl;
    }
    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }
    public String getTheme() {
        return theme;
    }
    public String getStylesheet() {
        return stylesheet;
    }
    public void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }
    public String getTimeFormat() {
        return timeFormat;
    }
    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }
    public String getShortDateFormat() {
        return shortDateFormat;
    }
    public void setShortDateFormat(String shortDateFormat) {
        this.shortDateFormat = shortDateFormat;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    public int getCompaniesNumRows() {
        return companiesNumRows;
    }
    public void setCompaniesNumRows(int companiesNumRows) {
        this.companiesNumRows = companiesNumRows;
    }
    public String getCompanyFooterNotes() {
        return companyFooterNotes;
    }
    public void setCompanyFooterNotes(String companyFooterNotes) {
        this.companyFooterNotes = companyFooterNotes;
    }
    public int getContactsNumRows() {
        return contactsNumRows;
    }
    public void setContactsNumRows(int contactsNumRows) {
        this.contactsNumRows = contactsNumRows;
    }
    public List<String> getHardwareColumns() {
        return hardwareColumns;
    }
    public void setHardwareColumns(List<String> hardwareColumns) {
        this.hardwareColumns = hardwareColumns;
    }
    public int getHardwareNumRows() {
        return hardwareNumRows;
    }
    public void setHardwareNumRows(int hardwareNumRows) {
        this.hardwareNumRows = hardwareNumRows;
    }
    public int getIssuesNumRows() {
        return issuesNumRows;
    }
    public void setIssuesNumRows(int issuesNumRows) {
        this.issuesNumRows = issuesNumRows;
    }
    public int getUsersNumRows() {
        return usersNumRows;
    }
    public void setUsersNumRows(int usersNumRows) {
        this.usersNumRows = usersNumRows;
    }
    public int getBlogPostsListNumRows() {
        return blogPostsListNumRows;
    }
    public void setBlogPostsListNumRows(int blogPostsListNumRows) {
        this.blogPostsListNumRows = blogPostsListNumRows;
    }
    public int getBlogPostCharactersList() {
        return blogPostCharactersList;
    }
    public void setBlogPostCharactersList(int blogPostCharactersList) {
        this.blogPostCharactersList = blogPostCharactersList;
    }

    public int getKilobyteUnits() {
        return kilobyteUnits;
    }

    public void setKilobyteUnits(int kilobyteUnits) {
        this.kilobyteUnits = kilobyteUnits;
    }

    public String getFileRepositoryCompany() {
        return fileRepositoryCompany;
    }
    public void setFileRepositoryCompany(String fileRepositoryCompany) {
        this.fileRepositoryCompany = fileRepositoryCompany;
    }
    public String getFileRepositoryIssue() {
        return fileRepositoryIssue;
    }
    public void setFileRepositoryIssue(String fileRepositoryIssue) {
        this.fileRepositoryIssue = fileRepositoryIssue;
    }
    public String getFileRepositoryHardware() {
        return fileRepositoryHardware;
    }
    public void setFileRepositoryHardware(String fileRepositoryHardware) {
        this.fileRepositoryHardware = fileRepositoryHardware;
    }
    public String getFileRepositorySoftware() {
        return fileRepositorySoftware;
    }
    public void setFileRepositorySoftware(String fileRepositorySoftware) {
        this.fileRepositorySoftware = fileRepositorySoftware;
    }
    public String getFileRepositoryContract() {
        return fileRepositoryContract;
    }
    public void setFileRepositoryContract(String fileRepositoryContract) {
        this.fileRepositoryContract = fileRepositoryContract;
    }
    public String getAuthMethod() {
        return authMethod;
    }
    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }
    public String getAuthType() {
        return authType;
    }
    public void setAuthType(String authType) {
        this.authType = authType;
    }
    public int getAuthTimeout() {
        return authTimeout;
    }
    public void setAuthTimeout(int authTimeout) {
        this.authTimeout = authTimeout;
    }
    public String getLdapUrl() {
        return ldapUrl;
    }
    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }    
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getCompanyPath() {
        return companyPath;
    }
    public void setCompanyPath(String companyPath) {
        this.companyPath = companyPath;
    }
    public String getCompanyLogoPath() {
        return companyLogoPath;
    }
    public void setCompanyLogoPath(String companyLogoPath) {
        this.companyLogoPath = companyLogoPath;
    }

    public boolean isIssueNotificationFromUiOn() {
        return issueNotificationFromUiOn;
    }

    public void setIssueNotificationFromUiOn(boolean issueNotificationFromUiOn) {
        this.issueNotificationFromUiOn = issueNotificationFromUiOn;
    }

    public boolean isIssueNotificationFromEmailOn() {
        return issueNotificationFromEmailOn;
    }

    public void setIssueNotificationFromEmailOn(boolean issueNotificationFromEmailOn) {
        this.issueNotificationFromEmailOn = issueNotificationFromEmailOn;
    }

    public boolean isContractExpireNotificationOn() {
        return contractExpireNotificationOn;
    }

    public void setContractExpireNotificationOn(boolean contractExpireNotificationOn) {
        this.contractExpireNotificationOn = contractExpireNotificationOn;
    }

    public String getAllowedDomains() {
        return allowedDomains;
    }
    public void setAllowedDomains(String allowedDomains) {
        this.allowedDomains = allowedDomains;
    }
    public String getSmtpHost() {
        return smtpHost;
    }
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }
    public String getSmtpPort() {
        return smtpPort;
    }
    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }
    public String getSmtpUsername() {
        return smtpUsername;
    }
    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }
    public String getSmtpPassword() {
        return smtpPassword;
    }
    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }
    public String getSmtpFrom() {
        return smtpFrom;
    }
    public void setSmtpFrom(String smtpFrom) {
        this.smtpFrom = smtpFrom;
    }
    public String getSmtpTo() {
        return smtpTo;
    }
    public void setSmtpTo(String smtpTo) {
        this.smtpTo = smtpTo;
    }
    public String getHomeCustomDescription() {
        return homeCustomDescription;
    }
    public void setHomeCustomDescription(String homeCustomDescription) {
        this.homeCustomDescription = homeCustomDescription;
    }
    public String getNumberOfPastYears() {
        return numberOfPastYears;
    }
    public void setNumberOfPastYears(String numberOfPastYears) {
        this.numberOfPastYears = numberOfPastYears;
    }
    public String getNumberOfFutureYears() {
        return numberOfFutureYears;
    }
    public void setNumberOfFutureYears(String numberOfFutureYears) {
        this.numberOfFutureYears = numberOfFutureYears;
    }
    public List<String> getContractColumns() {
        return contractColumns;
    }
    public void setContractColumns(List<String> contractColumns) {
        this.contractColumns = contractColumns;
    }
    public boolean isTest() {
        return test;
    }
    public void setTest(boolean test) {
        this.test = test;
    }
    public int getAllowBlankUserPassword() {
        return allowBlankUserPassword;
    }
    public void setAllowBlankUserPassword(int allowBlankUserPassword) {
        this.allowBlankUserPassword = allowBlankUserPassword;
    }
    public int getContractsExpireCountdown() {
        return contractsExpireCountdown;
    }
    public void setContractsExpireCountdown(int contractsExpireCountdown) {
        this.contractsExpireCountdown = contractsExpireCountdown;
    }
    public String getDbBackupProgramPath() {
        return dbBackupProgramPath;
    }
    public void setDbBackupProgramPath(String dbBackupProgramPath) {
        this.dbBackupProgramPath = dbBackupProgramPath;
    }
    public String getDbBackupRepositoryPath() {
        return dbBackupRepositoryPath;
    }
    public void setDbBackupRepositoryPath(String dbBackupRepositoryPath) {
        this.dbBackupRepositoryPath = dbBackupRepositoryPath;
    }
    public boolean getSmtpStarttls() {
        return smtpStarttls;
    }
    public void setSmtpStarttls(boolean smtpStarttls) {
        this.smtpStarttls = smtpStarttls;
    }
    public List<String> getIssuesColumns() {
        return issuesColumns;
    }
    public void setIssuesColumns(List<String> issuesColumns) {
        this.issuesColumns = issuesColumns;
    }
    public String getLdapUsername() {
        return ldapUsername;
    }
    public void setLdapUsername(String ldapUsername) {
        this.ldapUsername = ldapUsername;
    }
    public String getLdapPassword() {
        return ldapPassword;
    }

    public String getPopHost() {
        return popHost;
    }
    public void setPopHost(String popHost) {
        this.popHost = popHost;
    }
    public String getPopPort() {
        return popPort;
    }
    public void setPopPort(String popPort) {
        this.popPort = popPort;
    }
    public String getPopUsername() {
        return popUsername;
    }
    public void setPopUsername(String popUsername) {
        this.popUsername = popUsername;
    }
    public String getPopPassword() {
        return popPassword;
    }
    public void setPopPassword(String popPassword) {
        this.popPassword = popPassword;
    }
    public String getPopIgnoreSender() {
        return popIgnoreSender;
    }
    public void setPopIgnoreSender(String popIgnoreSender) {
        this.popIgnoreSender = popIgnoreSender;
    }
    public String getFileRepositoryKb() {
        return fileRepositoryKb;
    }
    public void setFileRepositoryKb(String fileRepositoryKb) {
        this.fileRepositoryKb = fileRepositoryKb;
    }
    public int getHardwareExpireCountdown() {
        return hardwareExpireCountdown;
    }
    public void setHardwareExpireCountdown(int hardwareExpireCountdown) {
        this.hardwareExpireCountdown = hardwareExpireCountdown;
    }
    public List<String> getSoftwareColumns() {
        return softwareColumns;
    }
    public void setSoftwareColumns(List<String> softwareColumns) {
        this.softwareColumns = softwareColumns;
    }

    public int getContractsNumRows() {
        return contractsNumRows;
    }

    public void setContractsNumRows(int contractsNumRows) {
        this.contractsNumRows = contractsNumRows;
    }

    public int getSoftwareNumRows() {
        return softwareNumRows;
    }

    public void setSoftwareNumRows(int softwareNumRows) {
        this.softwareNumRows = softwareNumRows;
    }

    public boolean getPopUseSSL() {
        return popUseSSL;
    }

    public void setPopUseSSL(boolean popUseSSL) {
        this.popUseSSL = popUseSSL;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public List<String> getKbColumns() {
        return kbColumns;
    }

    public void setKbColumns(List<String> kbColumns) {
        this.kbColumns = kbColumns;
    }

    public String getLdapSecurityPrincipal() {
        return ldapSecurityPrincipal;
    }

    public void setLdapSecurityPrincipal(String ldapSecurityPrincipal) {
        this.ldapSecurityPrincipal = ldapSecurityPrincipal;
    }

    public String getDatabaseAccessLogLevel() {
        return databaseAccessLogLevel;
    }

    public void setDatabaseAccessLogLevel(String databaseAccessLogLevel) {
        this.databaseAccessLogLevel = databaseAccessLogLevel;
    }

    public String getLdapLogLevel() {
        return ldapLogLevel;
    }

    public void setLdapLogLevel(String ldapLogLevel) {
        this.ldapLogLevel = ldapLogLevel;
    }

    public String getSchedulerLogLevel() {
        return schedulerLogLevel;
    }

    public void setSchedulerLogLevel(String schedulerLogLevel) {
        this.schedulerLogLevel = schedulerLogLevel;
    }

    public String getIssuesGuestSubmitEnabled() {
        return issuesGuestSubmitEnabled;
    }

    public void setIssuesGuestSubmitEnabled(String issuesGuestSubmitEnabled) {
        this.issuesGuestSubmitEnabled = issuesGuestSubmitEnabled;
    }

    public String getSoftwareLicneseNotesNumChars() {
        return softwareLicneseNotesNumChars;
    }

    public void setSoftwareLicneseNotesNumChars(String softwareLicneseNotesNumChars) {
        this.softwareLicneseNotesNumChars = softwareLicneseNotesNumChars;
    }

    public int getMinimumPasswordLength() {
        return minimumPasswordLength;
    }

    public void setMinimumPasswordLength(int minimumPasswordLength) {
        this.minimumPasswordLength = minimumPasswordLength;
    }

    public String getUserNameDisplay() {
        return userNameDisplay;
    }

    public void setUserNameDisplay(String userNameDisplay) {
        this.userNameDisplay = userNameDisplay;
    }

    public boolean getCheckUniqueHardwareName() {
        return checkUniqueHardwareName;
    }

    public void setCheckUniqueHardwareName(boolean checkUniqueHardwareName) {
        this.checkUniqueHardwareName = checkUniqueHardwareName;
    }

    public boolean isPasswordComplexityEnabled() {
        return passwordComplexityEnabled;
    }

    public void setPasswordComplexityEnabled(boolean passwordComplexityEnabled) {
        this.passwordComplexityEnabled = passwordComplexityEnabled;
    }

    public String getAccountLockoutThreshold() {
        return accountLockoutThreshold;
    }

    public void setAccountLockoutThreshold(String accountLockoutThreshold) {
        this.accountLockoutThreshold = accountLockoutThreshold;
    }

    public String getAccountLockoutDurationMinutes() {
        return accountLockoutDurationMinutes;
    }

    public void setAccountLockoutDurationMinutes(String accountLockoutDurationMinutes) {
        this.accountLockoutDurationMinutes = accountLockoutDurationMinutes;
    }

    public String getLdapUrlScheme() {
        return ldapUrlScheme;
    }

    public void setLdapUrlScheme(String ldapUrlScheme) {
        this.ldapUrlScheme = ldapUrlScheme;
    }

    public String getIssuesGuestSubmitModuleEnabled() {
        return issuesGuestSubmitModuleEnabled;
    }

    public void setIssuesGuestSubmitModuleEnabled(String issuesGuestSubmitModuleEnabled) {
        this.issuesGuestSubmitModuleEnabled = issuesGuestSubmitModuleEnabled;
    }

    public boolean isLoadCustomFields() {
        return loadCustomFields;
    }

    public void setLoadCustomFields(boolean loadCustomFields) {
        this.loadCustomFields = loadCustomFields;
    }

    public String getIssuesMultipleDeleteEnabled() {
        return issuesMultipleDeleteEnabled;
    }

    public void setIssuesMultipleDeleteEnabled(String issuesMultipleDeleteEnabled) {
        this.issuesMultipleDeleteEnabled = issuesMultipleDeleteEnabled;
    }

    public boolean isCheckUniqueSerialNumber() {
        return checkUniqueSerialNumber;
    }

    public void setCheckUniqueSerialNumber(boolean checkUniqueSerialNumber) {
        this.checkUniqueSerialNumber = checkUniqueSerialNumber;
    }

    public String getTemplateLogLevel() {
        return templateLogLevel;
    }

    public void setTemplateLogLevel(String templateLogLevel) {
        this.templateLogLevel = templateLogLevel;
    }

    public void setReportIssueEmailTemplate(String reportIssueEmailTemplate) {
        this.reportIssueEmailTemplate = reportIssueEmailTemplate;
    }

    public String getReportIssueEmailTemplate() {
        return reportIssueEmailTemplate;
    }

    public String getIssueAddEmailTemplate() {
        return issueAddEmailTemplate;
    }

    public void setIssueAddEmailTemplate(String issueAddEmailTemplate) {
        this.issueAddEmailTemplate = issueAddEmailTemplate;
    }

    public String getIssueUpdateEmailTemplate() {
        return issueUpdateEmailTemplate;
    }

    public void setIssueUpdateEmailTemplate(String issueUpdateEmailTemplate) {
        this.issueUpdateEmailTemplate = issueUpdateEmailTemplate;
    }

    public String getIssuesDefaultDueDateDiff() {
        return issuesDefaultDueDateDiff;
    }

    public void setIssuesDefaultDueDateDiff(String issuesDefaultDueDateDiff) {
        this.issuesDefaultDueDateDiff = issuesDefaultDueDateDiff;
    }

	public boolean isKbMediaWikiSyntaxEnabled() {
		return kbMediaWikiSyntaxEnabled;
	}

	public void setKbMediaWikiSyntaxEnabled(boolean kbMediaWikiSyntaxEnabled) {
		this.kbMediaWikiSyntaxEnabled = kbMediaWikiSyntaxEnabled;
	}

    public String getContractExpireNotifyEmailTemplate() {
        return contractExpireNotifyEmailTemplate;
    }

    public void setContractExpireNotifyEmailTemplate(String contractExpireNotifyEmailTemplate) {
        this.contractExpireNotifyEmailTemplate = contractExpireNotifyEmailTemplate;
    }

    public boolean isBulkHardwareDeleteEnabled() {
        return bulkHardwareDeleteEnabled;
    }

    public void setBulkHardwareDeleteEnabled(boolean bulkHardwareDeleteEnabled) {
        this.bulkHardwareDeleteEnabled = bulkHardwareDeleteEnabled;
    }

    public int getPopRetrievalFrequency() {
        return popRetrievalFrequency;
    }

    public void setPopRetrievalFrequency(int popRetrievalFrequency) {
        this.popRetrievalFrequency = popRetrievalFrequency;
    }

    public int getPopMessageBatchSize() {
        return popMessageBatchSize;
    }

    public void setPopMessageBatchSize(int popMessageBatchSize) {
        this.popMessageBatchSize = popMessageBatchSize;
    }
}
