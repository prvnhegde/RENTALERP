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

import java.util.Arrays;
import java.util.List;

import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.software.dto.Software;

/**
 * ModuleConfigManager
 */
public class AppConfigManager extends BaseConfigManager {

    private static AppConfigManager instance = new AppConfigManager();

    public static final int ISSUE_DUE_DATE_DEFAULT = 0;

    private boolean loadCustomFields;

    /*
     * Blog module
     */
    private String[] blogsNumberOfPostCharactersOptions;
    private String[] blogsNumberOfPostsOptions;
    private int blogsNumPosts;
    private int blogsNumberOfPostCharacters;

    /*
     * Contacts module
     */
    private int companyRows;
    private int contactRows;
    private String[] contactColumns;
    private List<String> contactsCompanyColumnList;
    private List<String> contactsCompanyExportColumns;
    private List<String> contactsExportColumns;

    /*
     * Contracts module
     */
    private String[] contractsColumns;
    private List<String> contractsExportColumns;
    private int contractsRowsToShow;
    private int contractsExpireCountdown;

    /*
     * Issues module
     */
    private int issueRows;
    private List<String> issuesColumns;
    private List<String> issueExportColumns;
    private boolean issuesGuestSubmitModuleEnabled;
    private boolean issuesGuestSubmitFooterEnabled;
    private static final String issuesExportFilename = "issue-list.csv";
    private boolean issuesMultipleDeleteEnabled;

    /**
     * Number of dates to add to default due date
     */
    private int issueDueDateDiff;

    /*
     * Hardware module
     */
    private int hardwareRowsToShow;
    private List<String> hardwareColumns;
    private List<String> hardwareExportColumns;
    private List<String> hardwareMembersExportColumns;
    private List<String> hardwareLicenseExportColumns;
    private List<String> hardwareImportExportColumns;
    private int hardwareWarrantyExpireCountdown;
    private boolean checkUniqueHardwareName;
    private boolean checkUniqueSerialNumber;
    private boolean hardwareBulkDeleteEnabled;
    
    /*
     * Knowledge Base module
     */
    private static final int kbArticleNumberOfSearchResults = 10;
    private String[] kbArticleColumns;
    private int kbArticleCharLimit;
    private boolean kbArticleMediaWikiSyntaxEnabled;

    /*
     * RSS module
     */
    private int rssCacheTimeInMinutes = 20 * 60 * 1000;

    /*
     * Portal module
     */
    private final int portalNumberOfSitesToShow = 10;
    private String[] portalColumns;

    /*
     * Software module
     */
    private String[] softwareColumns;
    private List<String> softwareExportColumns;
    private int softwareRowsToShow;
    private int softwareLicenseNotesNumChars;

    private int userRows;

    /*
     * Calendar module
     */
    private int calendarMinYear;
    private int calendarMaxYearPlus;

    private int numPastYears;
    private int numFutureYears;

    private int dataImportRowLimit;

    private String[] expireCountdownOptions;

    private AppConfigManager() {}

    static AppConfigManager getInstance() {
        return instance;
    }

    void init(ConfigManager configManager) {
        expireCountdownOptions = configManager.getStringArray(ConfigKeys.EXPIRE_COUNTDOWN_OPTIONS);
        loadCustomFields = configManager.getBoolean(ConfigKeys.CUSTOM_FIELDS_EXPAND);

        dataImportRowLimit = 200;

        /*
         * Blogs module
         */
        blogsNumberOfPostCharactersOptions = configManager.getString("portal.numberOfBlogPostCharactersOptions").split(",");
        blogsNumberOfPostsOptions = configManager.getStringArray("portal.numberOfBlogPostsOptions");
        blogsNumPosts = configManager.getInt(ConfigKeys.BLOG_NUM_POSTS);
        blogsNumberOfPostCharacters = configManager.getInt(ConfigKeys.BLOG_NUM_POST_CHARS);

        /*
         * Contacts module
         */
        contactColumns = configManager.getStringArray(ConfigKeys.CONTACTS_COLUMNS);
        // Default: rownum,company_name
        contactsCompanyColumnList = Arrays.asList((configManager.getString("contacts.companyColumnList")).split(","));

        contactsCompanyExportColumns = Arrays.asList(Company.COMPANY_NAME, Company.DESCRIPTION);

        // Default: rownum,contact_first_name,contact_last_name,contact_title,company_name
        contactsExportColumns = Arrays.asList(Contact.COMPANY_NAME, Contact.FIRST_NAME, Contact.LAST_NAME, Contact.TITLE, Contact.PRIMARY_EMAIL,
            Contact.WORK_PHONE);

        companyRows = configManager.getInt(ConfigKeys.COMPANY_ROWS);
        contactRows = configManager.getInt(ConfigKeys.CONTACT_ROWS);

        /*
         * Issues module
         */
        issueRows = configManager.getInt(ConfigKeys.ISSUE_ROWS);
        issuesColumns = Arrays.asList((configManager.getString("issues.columnList")).split(","));
        issueExportColumns = Arrays.asList(Issue.ID, Issue.TITLE, Issue.DESCRIPTION, Issue.TYPE, Issue.STATUS, Issue.PRIORITY,
            Issue.ASSIGNEE_NAME, Issue.CREATOR_NAME, Issue.CREATION_DATE, Issue.MODIFIER_NAME, Issue.MODIFICATION_DATE, Issue.DUE_DATE);
        issuesGuestSubmitModuleEnabled = configManager.getBoolean(ConfigKeys.ISSUE_GUEST_SUBMIT_MODULE_ENABLED);
        issuesGuestSubmitFooterEnabled = configManager.getBoolean(ConfigKeys.ISSUE_GUEST_SUBMIT_FOOTER_ENABLED);
        issuesMultipleDeleteEnabled = configManager.getBoolean(ConfigKeys.ISSUE_MULTIPLE_DELETE_ENABLED);
        issueDueDateDiff = configManager.getInt(ConfigKeys.ISSUE_DUE_DATE_DIFF);

        /*
         * Hardware module
         */
        hardwareRowsToShow = configManager.getInt(ConfigKeys.HARDWARE_ROWS);
        hardwareColumns = Arrays.asList(configManager.getStringArray(ConfigKeys.HARDWARE_COLUMNS));
        hardwareExportColumns = Arrays.asList("hardware_id", "hardware_name","hardware_description","hardware_manufacturer_name",
                "hardware_vendor_name","hardware_type","hardware_status","hardware_model_name","hardware_model_number",
                "hardware_serial_number",Hardware.PURCHASE_PRICE,"hardware_purchase_date","hardware_warranty_expire_date",
                "hardware_last_service_date","hardware_location", Hardware.OWNER_NAME,
                Hardware.CREATOR_NAME, Hardware.CREATION_DATE, Hardware.MODIFIER_NAME, Hardware.MODIFICATION_DATE);

        // Another set of columns that are for exporting in importable format.
        hardwareImportExportColumns = Arrays.asList("hardware_id", "hardware_name", "hardware_description");

        hardwareMembersExportColumns = Arrays.asList("hardware_id", "hardware_name", "hardware_member_id", "hardware_member_name");
        hardwareLicenseExportColumns = Arrays.asList("hardware_id", "hardware_name", "software_name", "license_key", "license_note");
        hardwareWarrantyExpireCountdown = configManager.getInt(ConfigKeys.HARDWARE_WARRANTY_EXPIRE_COUNTDOWN);
        checkUniqueHardwareName = configManager.getBoolean(ConfigKeys.HARDWARE_CHECK_UNIQUE_NAME);
        checkUniqueSerialNumber = configManager.getBoolean(ConfigKeys.HARDWARE_CHECK_SERIAL_NUMBER);
        hardwareBulkDeleteEnabled = configManager.getBoolean(ConfigKeys.HARDWARE_BULK_DELETE_ENABLED);

        /**
         * Knowledge Base module
         */
        kbArticleColumns = configManager.getStringArray(ConfigKeys.KB_ARTICLE_COLUMNS);
        kbArticleCharLimit = configManager.getInt(ConfigKeys.KB_ARTICLE_CHAR_LIMIT);
        kbArticleMediaWikiSyntaxEnabled = configManager.getBoolean(ConfigKeys.KB_ARTICLE_MEDIAWIKI_SYNTAX_ENABLED);

        /**
         * Portal module
         */
        portalColumns = configManager.getStringArray(ConfigKeys.PORTAL_COLUMNS);

        /*
         * Software module
         */
        softwareColumns = (configManager.getString("software.columnList")).split(",");
        softwareExportColumns = Arrays.asList(Software.NAME, Software.VERSION, Software.DESCRIPTION,
                Software.EXPIRE_DATE, Software.OWNER_USERNAME, Software.OWNER_DISPLAY_NAME,
                Software.MANUFACTURER, Software.VENDOR, Software.OS, Software.TYPE,
                Software.QUOTED_RETAIL_PRICE, Software.QUOTED_OEM_PRICE,
                Software.LICENSE_PUCHASED, Software.LICENSE_INSTALLED, Software.LICENSE_AVAILABLE);
        softwareRowsToShow = configManager.getInt(ConfigKeys.SOFTWARE_ROWS);
        softwareLicenseNotesNumChars = configManager.getInt(ConfigKeys.SOFTWARE_LICENSE_NOTES_NUM_CHARS);

        /*
         * Contracts module
         */
        contractsRowsToShow = configManager.getInt(ConfigKeys.CONTRACT_ROWS);
        contractsExportColumns = Arrays.asList(Contract.NAME, Contract.TYPE, Contract.STAGE,
                Contract.CONTRACT_OWNER_USERNAME, Contract.CONTRACT_OWNER_DISPLAY_NAME,
                Contract.CONTRACT_EXPIRE_DATE, Contract.CONTRACT_EFFECT_DATE, Contract.RENEWAL_TYPE,
                Contract.CONTRACT_RENEWAL_DATE);
        contractsExpireCountdown = configManager.getInt(ConfigKeys.CONTRACT_EXPIRE_COUNTDOWN);
        contractsColumns = configManager.getStringArray(ConfigKeys.CONTRACT_COLUMNS);

        userRows = configManager.getInt(ConfigKeys.USER_ROWS);

        numPastYears = configManager.getInt(ConfigKeys.NUM_PAST_YEARS);
        numFutureYears = configManager.getInt(ConfigKeys.NUM_FUTURE_YEARS);
        calendarMinYear = configManager.getInt("calendar.minYear");
        calendarMaxYearPlus = configManager.getInt("calendar.maxYearPlus");
    }

    public int getNumPastYears() {
        return numPastYears;
    }

    public int getHardwareRowsToShow() {
        return hardwareRowsToShow;
    }

    public int getCompanyRows() {
        return companyRows;
    }

    public int getCalendarMinYear() {
        return calendarMinYear;
    }

    public int getUserRows() {
        return userRows;
    }

    public int getCalendarMaxYearPlus() {
        return calendarMaxYearPlus;
    }

    public String[] getBlogsNumberOfPostsOptions() {
        return blogsNumberOfPostsOptions;
    }
    public String[] getBlogsNumberOfPostCharactersOptions() {
        return blogsNumberOfPostCharactersOptions;
    }
    public int getRssCacheTimeInMinutes() {
        return rssCacheTimeInMinutes;
    }

    public List<String> getHardwareColumns() {
        return hardwareColumns;
    }
    public String[] getSoftwareColumns() {
        return softwareColumns;
    }
    public List<String> getIssuesColumns() {
        return issuesColumns;
    }
    public List<String> getContactsCompanyColumnList() {
        return contactsCompanyColumnList;
    }
    public String[] getContactColumns() {
        return contactColumns;
    }
    public String[] getPortalColumns() {
        return portalColumns;
    }
    public List<String> getHardwareExportColumns() {
        return hardwareExportColumns;
    }
    public String getIssuesExportFilename() {
        return issuesExportFilename;
    }
    public String[] getExpireCountdownOptions() {
        return expireCountdownOptions;
    }

    public int getPortalNumberOfSitesToShow() {
        return portalNumberOfSitesToShow;
    }

    public int getHardwareWarrantyExpireCountdown() {
        return hardwareWarrantyExpireCountdown;
    }

    public int getKbArticleNumberOfSearchResults() {
        return kbArticleNumberOfSearchResults;
    }

    public int getSoftwareRowsToShow() {
        return softwareRowsToShow;
    }

    public int getContractsRowsToShow() {
        return contractsRowsToShow;
    }

    public String[] getContractsColumns() {
        return contractsColumns;
    }

    public int getContractsExpireCountdown() {
        return contractsExpireCountdown;
    }

    public List<String> getIssueExportColumns() {
        return issueExportColumns;
    }

    public List<String> getSoftwareExportColumns() {
        return softwareExportColumns;
    }

    public List<String> getContractsExportColumns() {
        return contractsExportColumns;
    }

    public boolean isIssuesGuestSubmitFooterEnabled() {
        return issuesGuestSubmitFooterEnabled;
    }

    public List<String> getContactsExportColumns() {
        return contactsExportColumns;
    }

    public int getNumFutureYears() {
        return numFutureYears;
    }

    public int getBlogsNumPosts() {
        return blogsNumPosts;
    }

    public int getIssueRows() {
        return issueRows;
    }

    public String[] getKbArticleColumns() {
        return kbArticleColumns;
    }

    public int getBlogsNumberOfPostCharacters() {
        return blogsNumberOfPostCharacters;
    }

    public int getKbArticleCharLimit() {
        return kbArticleCharLimit;
    }

    public int getContactRows() {
        return contactRows;
    }

    public int getSoftwareLicenseNotesNumChars() {
        return softwareLicenseNotesNumChars;
    }

    public boolean isCheckUniqueHardwareName() {
        return checkUniqueHardwareName;
    }

    public boolean isIssuesGuestSubmitModuleEnabled() {
        return issuesGuestSubmitModuleEnabled;
    }

    public boolean isLoadCustomFields() {
        return loadCustomFields;
    }

    public List<String> getHardwareMembersExportColumns() {
        return hardwareMembersExportColumns;
    }

    public List<String> getHardwareLicenseExportColumns() {
        return hardwareLicenseExportColumns;
    }

    public List<String> getHardwareImportExportColumns() {
        return hardwareImportExportColumns;
    }

    public boolean isIssuesMultipleDeleteEnabled() {
        return issuesMultipleDeleteEnabled;
    }

    public int getDataImportRowLimit() {
        return dataImportRowLimit;
    }

    public List<String> getContactsCompanyExportColumns() {
        return contactsCompanyExportColumns;
    }

    public boolean isCheckUniqueSerialNumber() {
        return checkUniqueSerialNumber;
    }

    public int getIssueDueDateDiff() {
        return issueDueDateDiff;
    }

	public boolean isKbArticleMediaWikiSyntaxEnabled() {
		return kbArticleMediaWikiSyntaxEnabled;
	}

    public boolean isHardwareBulkDeleteEnabled() {
        return hardwareBulkDeleteEnabled;
    }
}
