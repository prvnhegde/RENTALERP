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
 * Config names.
 */
public class ConfigKeys {

    // Placeholder keys
    public static final String PLACEHOLDER_CONFIG_KEY_1 = "_SpareConfigKey1";
    public static final String PLACEHOLDER_CONFIG_KEY_2 = "_SpareConfigKey1";
    
    public static final String BLOG_NUM_POSTS = "blogs.numberOfPostsToShow";
    public static final String BLOG_NUM_POST_CHARS = "blogs.numberOfPostCharsToShow";
    public static final String COMPANY_FOOTER_NOTES = "company.footerNotes";
    public static final String COMPANY_ROWS = "companies.numberOfRowsToShow";
    public static final String COMPANY_LOGO_PATH = "companyLogoPath";
    public static final String COMPANY_NAME = "companyName";
    public static final String COMPANY_PATH = "companyPath";
    public static final String CONTACT_ROWS = "contacts.numberOfRowsToShow";
    public static final String CONTRACT_COLUMNS = "contracts.columnList";
    public static final String CONTRACT_EXPIRE_COUNTDOWN = "contracts.expirationCountdown";
    public static final String CONTRACT_ROWS = "contracts.numberOfRowsToShow";
    public static final String CURRENCY_OPTION = "currency.options";
    public static final String CUSTOM_FIELDS_EXPAND = "CustomFields.Expand";
    public static final String SHORT_DATE = "datetime.shortDateFormat";
    public static final String TIME_FORMAT = "datetime.timeFormat";
    public static final String NUM_PAST_YEARS = "datetime.numberOfPastYears";
    public static final String NUM_FUTURE_YEARS = "datetime.numberOfUpcomingYears";
    public static final String DB_POSTGRES_PROGRAM_PATH = "db.postgresProgramPath";
    public static final String EXPIRE_COUNTDOWN_OPTIONS = "expirationCountdown.options";

    public static final String CONTACTS_COLUMNS = "contacts.columnList";

    // Files
    public static final String FILES_KILOBYTE_UNITS = "Files.KilobyteUnits";
    public static final String FILES_UPLOAD_BYTE_SIZE = "Files.MaxUploadByteSize";
    public static final String DB_BACKUP_REPOSITORY_PATH = "file.db.backup.repositoryPath";
    public static final String COMPANY_FILE_PATH = "file.company.repositoryPath";
    public static final String CONTRACT_FILE_PATH = "file.contract.repositoryPath";
    public static final String HARDWARE_FILE_PATH = "file.hardware.repositoryPath";
    public static final String ISSUE_FILE_PATH = "file.issue.repositoryPath";
    public static final String KB_FILE_PATH = "file.kb.repositoryPath";
    public static final String SOFTWARE_FILE_PATH = "file.software.repositoryPath";

    // Users
    public static final String USER_NAME_DISPLAY = "Users.NameDisplay";

    // Hardware
    public static final String HARDWARE_CHECK_UNIQUE_NAME = "Hardware.CheckUniqueHardwareName";
    public static final String HARDWARE_CHECK_SERIAL_NUMBER = "Hardware.CheckUniqueSerialNumber";
    public static final String HARDWARE_ROWS = "hardware.numberOfRowsToShow";
    public static final String HARDWARE_COLUMNS = "hardware.columnList";
    public static final String HARDWARE_WARRANTY_EXPIRE_COUNTDOWN = "hardware.warrantyExpireCountdown";
    public static final String HARDWARE_BULK_DELETE_ENABLED = "Hardware.BulkDeleteEnabled";
    
    public static final String ISSUE_ROWS = "issues.numberOfRowsToShow";
    public static final String ISSUE_COLUMNS = "issues.columnList";
    public static final String ISSUE_GUEST_SUBMIT_MODULE_ENABLED = "Issues.ReportIssueModuleEnabled";
    public static final String ISSUE_GUEST_SUBMIT_FOOTER_ENABLED = "Issues.ReportIssueFooterEnabled";
    public static final String ISSUE_MULTIPLE_DELETE_ENABLED = "Issues.multipleDeleteEnabled";
    public static final String ISSUE_REPORT_EMAIL_TEMPLATE = "Issues.IssueReportEmailTemplate";
    public static final String ISSUE_ADD_EMAIL_TEMPLATE = "Issues.IssueAddEmailTemplate";
    public static final String ISSUE_UPDATE_EMAIL_TEMPLATE = "Issues.IssueUpdateEmailTemplate";
    public static final String ISSUE_DUE_DATE_DIFF = "Issues.DueDateDiff";

    // Knowledge Base
    public static final String KB_ARTICLE_CHAR_LIMIT = "kb.article.charLimit";
    public static final String KB_ARTICLE_COLUMNS = "kb.article.columns";
    public static final String KB_ARTICLE_MEDIAWIKI_SYNTAX_ENABLED = "KB.Article.MediaWikiSyntaxEnabled";

    public static final String LOCALE = "locale";
    public static final String LOGGING_LEVEL_DATABASE = "logging.database.level";
    public static final String LOGGING_LEVEL_LDAP = "logging.ldap.level";
    public static final String LOGGING_LEVEL_SCHEDULER = "Logging.Level.Scheduler";
    public static final String LOGGING_LEVEL_TEMPLATE = "Logging.Level.Template";
    public static final String MULTI_APPS_INSTANCE = "System.MultiAppsInstance";

    // Admin: POP Email
    public static final String POP_HOST = "mail.pop.host";
    public static final String POP_PORT = "mail.pop.port";
    public static final String POP_USERNAME = "mail.pop.username";
    public static final String POP_PASSWORD = "mail.pop.password";
    public static final String POP_MESSAGES_LIMIT = "mail.pop.messages.limit";
    public static final String POP_SENDER_IGNORE_LIST = "mail.pop.senderIgnoreList";
    public static final String POP_REPEAT_INTERVAL = "mail.pop.repeatInterval";
    public static final String POP_SSL_ENABLED = "mail.pop.ssl.enable";

    // Admin: SMTP Email
    public static final String EMAIL_ALLOWED_DOMAINS = "email.allowedDomains";
    public static final String ISSUE_NOTIFICATION_FROM_UI_ENABLED = "Email.IssueNotificationFromUiEnabled"; // For updates from UI.
    public static final String ISSUE_NOTIFICATION_FROM_EMAIL_ENABLED = "Email.IssueNotificationFromEmailEnabled"; // For updates from email pulls.
    public static final String CONTRACT_EXPIRE_NOTIFY_ENABLED = "Email.ContractExpirationNotificationEnabled";
    public static final String CONTRACT_EXPIRE_NOTIFY_TEMPLATE = "Email.ContractExpireNotificationTemplate";

    public static final String SMTP_HOST = "mail.smtp.host";
    public static final String SMTP_PORT = "mail.smtp.port";
    public static final String SMTP_USERNAME = "mail.smtp.username";
    public static final String SMTP_PASSWORD = "mail.smtp.password";
    public static final String SMTP_FROM = "mail.smtp.from";
    public static final String SMTP_TO = "mail.smtp.to";
    public static final String SMTP_STARTTLS = "mail.smtp.starttls";

    // Look and Feel
    public static final String THEME_DEFAULT = "theme.default";
    public static final String UI_STYLESHEET = "ui.stylesheet";
    public static final String CUSTOM_HOME_DESCRIPTION = "home.customDescription";
    public static final String TABLE_STYLE = "UI.TableStyle";

    // Reports
    public static final String CONTACT_REPORT = "Reports.Type.ContactReport.Filename";
    public static final String CONTRACT_REPORT = "Reports.Type.ContractReport.Filename";
    public static final String ISSUE_REPORT = "Reports.Type.IssueReport.Filename";
    public static final String HARDWARE_REPORT = "Reports.Type.HardwareReport.Filename";
    public static final String HARDWARE_MEMBER_REPORT = "Reports.Type.HardwareMemberReport.Filename";
    public static final String HARDWARE_LICENSE_REPORT = "Reports.Type.HardwareLicenseReport.Filename";
    public static final String SOFTWARE_REPORT = "Reports.Type.SoftwareReport.Filename";
    public static final String SOFTWARE_USAGE_REPORT = "Reports.Type.SoftwareUsageReport.Filename";
    
    // Portal
    public static final String PORTAL_COLUMNS = "portal.columnList";

    // Software
    public static final String SOFTWARE_COLUMNS = "software.columnList";
    public static final String SOFTWARE_ROWS = "software.numberOfRowsToShow";
    public static final String SOFTWARE_LICENSE_NOTES_NUM_CHARS = "software.numberLicenseNotesChars";
    
    public static final String SYSTEM_LICENSE_KEY = "system.licenseKey";
    public static final String SYSTEM_CACHE_KEY = "system.cacheKey";
    public static final String LOCAL_TIMEZONE = "timezone.local";
    public static final String APP_URL = "url.application";
    public static final String USER_ROWS = "users.numberOfRowsToShow";

    // System
    public static final String SCHEMA_VERSION = "schema.version";
    public static final String THEME_OPTIONS = "theme.options";
    public static final String TIMEZONE_BASE = "timezone.base";
    public static final String LOCALE_OPTIONS = "locale.options";

    // System: Security
    public static final String AUTH_METHOD = "auth.authenticationMethod";
    public static final String AUTH_METHOD_OPTIONS = "auth.authenticationMethod.options";
    public static final String AUTH_DOMAIN = "auth.domain";
    public static final String AUTH_LDAP_URL = "auth.ldapUrl";
    public static final String AUTH_LDAP_URL_SCHEME = "auth.ldap.url.scheme";
    public static final String AUTH_LDAP_SECURITY_PRINCIPAL = "auth.ldap.securityPrincipal";
    public static final String AUTH_TIMEOUT = "auth.sessionTimeoutSeconds";
    public static final String AUTH_TIMEOUT_OPTIONS = "auth.sessionTimeoutSeconds.options";
    public static final String AUTH_TYPE = "auth.type";
    public static final String AUTH_TYPE_OPTIONS = "auth.type.options";
    public static final String SECURITY_ALLOW_BLANK_USER_PASSWORD = "admin.allowBlankUserPassword";
    public static final String SECURITY_USER_PASSWORD_COMPLEX_ENABLED = "System.Security.UserPasswordComplexityEnabled";
    public static final String SECURITY_MIN_PASSWORD_LENGTH = "System.Security.UserPasswordLength";
    public static final String SECURITY_ACCOUNT_LOCKOUT_THRESHOLD = "System.Security.AccountLockoutThreshold";
    public static final String SECURITY_ACCOUNT_LOCKOUT_DURATION_MINUTES = "System.Security.AccountLockoutDurationMinutes";

    // System: Database
    public static final String SYSTEM_DB_MAX_CONNECTION = "System.DB.MaxConnection";
}
