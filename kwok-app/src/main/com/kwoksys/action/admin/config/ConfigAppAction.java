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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.AppConfigManager;
import com.kwoksys.biz.system.core.configs.ConfigKeys;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.license.LicenseManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.properties.PropertiesManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * ConfigListGeneralAction
 */
public class ConfigAppAction extends Action2 {

    public String index() throws Exception {
        AccessUser user = requestContext.getUser();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_APP_CMD);
        standardTemplate.setAttribute("appVersion", PropertiesManager.getPatchVersion());

        Link link = new Link(requestContext);
        link.setTitleKey(LicenseManager.getContentKey());
        if (!LicenseManager.isCommercialEdition()) {
            link.setExternalPath(AppPaths.SITE_COMPARE_EDITIONS);
            link.setImgSrc(Image.getInstance().getExternalPopupIcon()).setImgAlignRight();
        }
        request.setAttribute("appEdition", link);
        request.setAttribute("licenseKey", ConfigManager.system.getLicenseKey());
        request.setAttribute("applicationUrl", ConfigManager.system.getAppUrl());
        request.setAttribute("timezoneLocal", Localizer.getText(requestContext, "admin.config.timezone." +
                ConfigManager.system.getTimezoneLocalString()));
        
        request.setAttribute("timezoneOffset", Localizer.getText(requestContext, "common.calendar.time.hours_x",
                new String[] {String.valueOf(DatetimeUtils.getTimeOffsetHours(requestContext.getSysdate()))})); 
        
        request.setAttribute("serverTime", DatetimeUtils.toLocalDatetime(requestContext.getSysdate()));
        request.setAttribute("shortDateFormat", ConfigManager.system.getDateFormat());
        request.setAttribute("locale", ConfigManager.system.getLocaleString());
        request.setAttribute("timeFormat", ConfigManager.system.getTimeFormat());
        request.setAttribute("currencyOptions", ConfigManager.system.getCurrencySymbol());
        request.setAttribute("numberOfPastYearsToShow", ConfigManager.app.getNumPastYears());
        request.setAttribute("numberOfFutureYearsToShow", ConfigManager.app.getNumFutureYears());
        request.setAttribute("usernameDisplay", ConfigManager.system.getUsernameDisplay());
        request.setAttribute("usersRowsToShow", ConfigManager.app.getUserRows());
        request.setAttribute("companiesRowsToShow", ConfigManager.app.getCompanyRows());
        request.setAttribute("contactsRowsToShow", ConfigManager.app.getContactRows());
        request.setAttribute("contractsRowsToShow", ConfigManager.app.getContractsRowsToShow());
        request.setAttribute("issuesRowsToShow", ConfigManager.app.getIssueRows());
        request.setAttribute("issuesMultipleDeleteEnabled", ConfigManager.app.isIssuesMultipleDeleteEnabled());

        String defaultDueDate = ConfigManager.app.getIssueDueDateDiff() != AppConfigManager.ISSUE_DUE_DATE_DEFAULT ?
                Localizer.getText(requestContext, "common.calendar.days", new String[] {String.valueOf(ConfigManager.app.getIssueDueDateDiff())}) : "";

        request.setAttribute("issuesDefaultDueDate", defaultDueDate);

        request.setAttribute("hardwareRowsToShow", ConfigManager.app.getHardwareRowsToShow());
        request.setAttribute("softwareRowsToShow", ConfigManager.app.getSoftwareRowsToShow());
        request.setAttribute("softwareLicneseNotesNumChars", ConfigManager.app.getSoftwareLicenseNotesNumChars());

        List<String> hardwareColumns = new ArrayList<>();
        for (String column : ConfigManager.app.getHardwareColumns()) {
            hardwareColumns.add(Localizer.getText(requestContext, "common.column." + column));
        }
        request.setAttribute("hardwareColumns", StringUtils.join(hardwareColumns, ", "));

        List<String> softwareColumns = new ArrayList<>();
        for (String column : ConfigManager.app.getSoftwareColumns()) {
            softwareColumns.add(Localizer.getText(requestContext, "common.column." + column));
        }
        request.setAttribute("softwareColumns", StringUtils.join(softwareColumns, ", "));

        request.setAttribute("hardwareExpirationCountdown", Localizer.getText(requestContext, "common.calendar.days", new String[]
                                {String.valueOf(ConfigManager.app.getHardwareWarrantyExpireCountdown())}));
        request.setAttribute("checkUniqueHardwareName",ConfigManager.app.isCheckUniqueHardwareName());
        request.setAttribute("checkUniqueSerialNumber", ConfigManager.app.isCheckUniqueSerialNumber());
        request.setAttribute("bulkHardwareDelete", ConfigManager.app.isHardwareBulkDeleteEnabled());

        List<String> issuesColumns = new ArrayList<>();
        for (String column : ConfigManager.app.getIssuesColumns()) {
            issuesColumns.add(Localizer.getText(requestContext, "common.column." + column));
        }
        request.setAttribute("issuesColumns", StringUtils.join(issuesColumns, ", "));

        request.setAttribute("issuesGuestSubmitModuleEnabled", ConfigManager.app.isIssuesGuestSubmitModuleEnabled());
        request.setAttribute("issuesGuestSubmitEnabled", ConfigManager.app.isIssuesGuestSubmitFooterEnabled());

        List<String> contractsColumns = new ArrayList<>();
        for (String column : ConfigManager.app.getContractsColumns()) {
            contractsColumns.add(Localizer.getText(requestContext, "common.column." + column));
        }
        request.setAttribute("contractsColumns", StringUtils.join(contractsColumns, ", "));
        request.setAttribute("contractsExpirationCountdown",
                Localizer.getText(requestContext, "common.calendar.days", new String[]
                        {String.valueOf(ConfigManager.app.getContractsExpireCountdown())}));

        List<String> kbColumns = new ArrayList<>();
        for (String column : ConfigManager.app.getKbArticleColumns()) {
            kbColumns.add(Localizer.getText(requestContext, "common.column." + column));
        }
        request.setAttribute("kbColumns", StringUtils.join(kbColumns, ", "));
        request.setAttribute("isMediaWikiSyntaxEnabled", ConfigManager.app.isKbArticleMediaWikiSyntaxEnabled());
        request.setAttribute("portal_numberOfBlogPostsToShowOnList", ConfigManager.app.getBlogsNumPosts());
        request.setAttribute("portal_numberOfBlogPostCharactersOnList", ConfigManager.app.getBlogsNumberOfPostCharacters());

        request.setAttribute("hardwareReportFilenameHeader", Localizer.getText(requestContext, "reports.workflow.type.hardware_report"));
        request.setAttribute("hardwareLicensesReportFilenameHeader", Localizer.getText(requestContext, "reports.workflow.type.hardware_license_report"));
        request.setAttribute("hardwareMembersReportFilenameHeader", Localizer.getText(requestContext, "reports.workflow.type.hardware_member_report"));
        request.setAttribute("softwareReportFilenameHeader", Localizer.getText(requestContext, "reports.workflow.type.software_report"));
        request.setAttribute("softwareUsageReportFilenameHeader", Localizer.getText(requestContext, "reports.workflow.type.software_usage_report"));
        request.setAttribute("issuesReportFilenameHeader", Localizer.getText(requestContext, "reports.workflow.type.issue_report"));
        request.setAttribute("contractsReportFilenameHeader", Localizer.getText(requestContext, "reports.workflow.type.contract_report"));
        
        String filename = ConfigManager.reports.getHardwareReportFilename();
        if (StringUtils.isEmpty(filename)) {
            filename = Localizer.getText(requestContext, "admin.config.reports.defaultName", new String[] {Localizer.getText(requestContext, "reports.workflow.type.hardware_report")});
        }

        request.setAttribute("hardwareReportFilename", filename);
        
        filename = ConfigManager.reports.getHardwareLicensesReportFilename();
        if (StringUtils.isEmpty(filename)) {
            filename = Localizer.getText(requestContext, "admin.config.reports.defaultName", new String[] {Localizer.getText(requestContext, "reports.workflow.type.hardware_license_report")});
        }
        
        request.setAttribute("hardwareLicensesReportFilename", filename);
        
        filename = ConfigManager.reports.getHardwareMembersReportFilename();
        if (StringUtils.isEmpty(filename)) {
            filename = Localizer.getText(requestContext, "admin.config.reports.defaultName", new String[] {Localizer.getText(requestContext, "reports.workflow.type.hardware_member_report")});
        }

        request.setAttribute("hardwareMembersReportFilename", filename);

        filename = ConfigManager.reports.getSoftwareReportFilename();
        if (StringUtils.isEmpty(filename)) {
            filename = Localizer.getText(requestContext, "admin.config.reports.defaultName", new String[] {Localizer.getText(requestContext, "reports.workflow.type.software_report")});
        }

        request.setAttribute("softwareReportFilename", filename);
        
        filename = ConfigManager.reports.getSoftwareUsageReportFilename();
        if (StringUtils.isEmpty(filename)) {
            filename = Localizer.getText(requestContext, "admin.config.reports.defaultName", new String[] {Localizer.getText(requestContext, "reports.workflow.type.software_usage_report")});
        }
        
        request.setAttribute("softwareUsageReportFilename", filename);

        filename = ConfigManager.reports.getIssuesReportFilename();
        if (StringUtils.isEmpty(filename)) {
            filename = Localizer.getText(requestContext, "admin.config.reports.defaultName", new String[] {Localizer.getText(requestContext, "reports.workflow.type.issue_report")});
        }

        request.setAttribute("issuesReportFilename", filename);
        
        filename = ConfigManager.reports.getContractsReportFilename();
        if (StringUtils.isEmpty(filename)) {
            filename = Localizer.getText(requestContext, "admin.config.reports.defaultName", new String[] {Localizer.getText(requestContext, "reports.workflow.type.contract_report")});
        }

        request.setAttribute("contractsReportFilename", filename);

        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            standardTemplate.setAttribute("reportEditLink", new Link(requestContext).setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE
                    + "?cmd=" + AdminUtils.ADMIN_REPORTS_EDIT_CMD).setTitleKey("common.command.Edit").getString());
        }
        
        standardTemplate.setAttribute("loadCustomFields", Localizer.getText(requestContext, "common.boolean.true_false." +
                ConfigManager.app.isLoadCustomFields()));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.app");

        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_APP_EDIT_CMD);
            link.setTitleKey("common.command.Edit");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.config.app"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit() throws Exception {
        ConfigForm actionForm = getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setLicenseKey(ConfigManager.system.getLicenseKey());
            actionForm.setApplicationUrl(ConfigManager.system.getAppUrl());
            actionForm.setTimezone(ConfigManager.system.getTimezoneLocalString());
            actionForm.setLocale(ConfigManager.system.getLocaleString());
            actionForm.setShortDateFormat(ConfigManager.system.getDateFormat());
            actionForm.setTimeFormat(ConfigManager.system.getTimeFormat());
            actionForm.setTheme(ConfigManager.system.getTheme());
            actionForm.setCurrency(ConfigManager.system.getCurrencySymbol());
            actionForm.setNumberOfPastYears(String.valueOf(ConfigManager.app.getNumPastYears()));
            actionForm.setNumberOfFutureYears(String.valueOf(ConfigManager.app.getNumFutureYears()));
            actionForm.setUserNameDisplay(ConfigManager.system.getUsernameDisplay());
            actionForm.setUsersNumRows(ConfigManager.app.getUserRows());

            actionForm.setHardwareNumRows(ConfigManager.app.getHardwareRowsToShow());
            actionForm.setCheckUniqueHardwareName(ConfigManager.app.isCheckUniqueHardwareName());
            actionForm.setCheckUniqueSerialNumber(ConfigManager.app.isCheckUniqueSerialNumber());
            actionForm.setHardwareExpireCountdown(ConfigManager.app.getHardwareWarrantyExpireCountdown());
            actionForm.setBulkHardwareDeleteEnabled(ConfigManager.app.isHardwareBulkDeleteEnabled());

            actionForm.setCompaniesNumRows(ConfigManager.app.getCompanyRows());
            actionForm.setContactsNumRows(ConfigManager.app.getContactRows());
            actionForm.setContractsNumRows(ConfigManager.app.getContractsRowsToShow());
            actionForm.setContractsExpireCountdown(ConfigManager.app.getContractsExpireCountdown());
            actionForm.setIssuesNumRows(ConfigManager.app.getIssueRows());
            actionForm.setIssuesMultipleDeleteEnabled(String.valueOf(ConfigManager.app.isIssuesMultipleDeleteEnabled()));
            
            actionForm.setSoftwareNumRows(ConfigManager.app.getSoftwareRowsToShow());
            actionForm.setSoftwareLicneseNotesNumChars(String.valueOf(ConfigManager.app.getSoftwareLicenseNotesNumChars()));
            actionForm.setBlogPostsListNumRows(ConfigManager.app.getBlogsNumPosts());
            actionForm.setBlogPostCharactersList(ConfigManager.app.getBlogsNumberOfPostCharacters());
            actionForm.setIssuesGuestSubmitModuleEnabled(String.valueOf(ConfigManager.app.isIssuesGuestSubmitModuleEnabled()));
            actionForm.setIssuesGuestSubmitEnabled(String.valueOf(ConfigManager.app.isIssuesGuestSubmitFooterEnabled()));
            actionForm.setIssuesDefaultDueDateDiff(String.valueOf(ConfigManager.app.getIssueDueDateDiff()));
            actionForm.setLoadCustomFields(ConfigManager.app.isLoadCustomFields());
            actionForm.setIssuesColumns(IssueUtils.getIssueColumnHeaders());
            actionForm.setHardwareColumns(HardwareUtils.getColumnHeaderList());
            actionForm.setSoftwareColumns(SoftwareUtils.getColumnHeaderList());
            actionForm.setContractColumns(ContractUtils.getColumnHeaderList());
            actionForm.setKbColumns(KbUtils.getArticleColumnHeaderList());
            actionForm.setKbMediaWikiSyntaxEnabled(ConfigManager.app.isKbArticleMediaWikiSyntaxEnabled());
        }

        List<LabelValueBean> timezoneOptions = new ArrayList<>();
        for (String timezone : ConfigManager.system.getTimezoneLocalOptions()) {
            timezoneOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.timezone." + timezone), timezone));
        }

        List<LabelValueBean> dateFormatOptions = new ArrayList<>();
        for (String option : ConfigManager.system.getDateFormatOptions()) {
            dateFormatOptions.add(new LabelValueBean(option, option));
        }

        List<LabelValueBean> timeFormatOptions = new ArrayList<>();
        for (String option : ConfigManager.system.getTimeFormatOptions()) {
            timeFormatOptions.add(new LabelValueBean(option, option));
        }

        List<LabelValueBean> numrowOptions = new ArrayList<>();
        for (String option : ConfigManager.admin.getNumberOfRowsToShowOptions()) {
            numrowOptions.add(new LabelValueBean(option, option));
        }

        List<LabelValueBean> numBlogPostsOptions = new ArrayList<>();
        for (String option : ConfigManager.app.getBlogsNumberOfPostsOptions()) {
            numBlogPostsOptions.add(new LabelValueBean(option, option));
        }

        List<LabelValueBean> blogPostCharOptions = new ArrayList<>();
        for (String option : ConfigManager.app.getBlogsNumberOfPostCharactersOptions()) {
            blogPostCharOptions.add(new LabelValueBean(option, option));
        }

        List<Map<String, String>> issuesColumnOptions = new ArrayList<>();
        for (String column : IssueUtils.getIssuesDefaultColumns()) {
            Map<String, String> map = new HashMap<>();
            map.put("name", column);
            if (column.equals(Issue.TITLE)) {
                map.put("disabled", "disabled");
            }
            if (actionForm.getIssuesColumns().contains(column)) {
                map.put("checked", "checked=\"checked\" ");
            }
            issuesColumnOptions.add(map);
        }

        List<Map<String, String>> hwColumnOptions = new ArrayList<>();
        for (String column : HardwareUtils.HARDWARE_DEFAULT_COLUMNS) {
            Map<String, String> map = new HashMap<>();
            map.put("name", column);
            if (column.equals(Hardware.HARDWARE_NAME)) {
                map.put("disabled", "disabled");
            }
            if (actionForm.getHardwareColumns().contains(column)) {
                map.put("checked", "checked=\"checked\" ");
            }
            hwColumnOptions.add(map);
        }

        List<Map<String, String>> swColumnOptions = new ArrayList<>();
        for (String column : SoftwareUtils.SOFTWARE_DEFAULT_COLUMNS) {
            Map<String, String> map = new HashMap<>();
            map.put("name", column);
            if (column.equals(Software.NAME)) {
                map.put("disabled", "disabled");
            }
            if (actionForm.getSoftwareColumns().contains(column)) {
                map.put("checked", "checked=\"checked\" ");
            }
            swColumnOptions.add(map);
        }

        List<Map<String, String>> contractColumns = new ArrayList<>();
        for (String column : ContractUtils.CONTRACT_COLUMNS_DEFAULT) {
            Map<String, String> map = new HashMap<>();
            map.put("name", column);
            if (column.equals(Contract.NAME)) {
                map.put("disabled", "disabled");
            }
            if (actionForm.getContractColumns().contains(column)) {
                map.put("checked", "checked=\"checked\" ");
            }
            contractColumns.add(map);
        }

        List<Map<String, String>> kbColumnOptions = new ArrayList<>();
        for (String column : KbUtils.getArticleColumnsDefault()) {
            Map<String, String> map = new HashMap<>();
            map.put("name", column);
            if (column.equals(Article.ARTICLE_NAME)) {
                map.put("disabled", "disabled");
            }
            if (actionForm.getKbColumns().contains(column)) {
                map.put("checked", "checked=\"checked\" ");
            }
            kbColumnOptions.add(map);
        }

        List<LabelValueBean> expireCountdownOptions = new ArrayList<>();
        for (String option : ConfigManager.app.getExpireCountdownOptions()) {
            expireCountdownOptions.add(new LabelValueBean(option, option));
        }

        List<LabelValueBean> userNameDisplayOptions = new ArrayList<>();
        for (String option : ConfigManager.admin.getUsernameDisplayOptions()) {
            userNameDisplayOptions.add(new LabelValueBean(Localizer.getText(requestContext, "common.column." + option), option));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_APP_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd="
                + AdminUtils.ADMIN_APP_CMD).getString());
        request.setAttribute("cmd", AdminUtils.ADMIN_APP_EDIT_2_CMD);
        request.setAttribute("licenseKeyExample", Localizer.getText(requestContext, "common.example", new String[] {"KEY:************;EXPIRE:****-**-**"}));
        request.setAttribute("shortDateFormatOptions", dateFormatOptions);
        request.setAttribute("timeFormatOptions", timeFormatOptions);
        request.setAttribute("timezoneOptions", timezoneOptions);
        request.setAttribute("localeOptions", WidgetUtils.getLocaleOptions(requestContext));
        request.setAttribute("numrowOptions", numrowOptions);
        request.setAttribute("numBlogPostsOptions", numBlogPostsOptions);
        request.setAttribute("numBlogPostCharactersOptions", blogPostCharOptions);
        request.setAttribute("contractsColumnOptions", contractColumns);
        request.setAttribute("expireCountdownOptions", expireCountdownOptions);
        request.setAttribute("issuesColumnOptions", issuesColumnOptions);
        request.setAttribute("kbColumnOptions", kbColumnOptions);
        request.setAttribute("hardwareColumnOptions", hwColumnOptions);
        request.setAttribute("softwareColumnOptions", swColumnOptions);
        request.setAttribute("userNameDisplayOptions", userNameDisplayOptions);
        standardTemplate.setAttribute("booleanOptions", WidgetUtils.getBooleanOptions(requestContext));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.app.edit");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (!AdminUtils.validCurrencySymbol(actionForm.getCurrency())) {
            errors.add("invalidCurrency", new ActionMessage("admin.config.error.invalidCurrency"));
        }
        if (!NumberUtils.isInteger(actionForm.getIssuesDefaultDueDateDiff())) {
                    errors.add("invalidIssueDueDateDefault", new ActionMessage("common.form.fieldNumberInvalid",
                            Localizer.getText(requestContext, "admin.config.issues.defaultDueDate")));
                }
        if (!NumberUtils.isInteger(actionForm.getNumberOfPastYears())) {
            errors.add("invalidNumPastYears", new ActionMessage("common.form.fieldNumberInvalid",
                    Localizer.getText(requestContext, "admin.config.numberOfPastYears")));
        }
        if (!NumberUtils.isInteger(actionForm.getNumberOfFutureYears())) {
            errors.add("invalidNumFutureYears", new ActionMessage("common.form.fieldNumberInvalid",
                    Localizer.getText(requestContext, "admin.config.numberOfFutureYears")));
        }
        if (!NumberUtils.isInteger(actionForm.getSoftwareLicneseNotesNumChars())) {
            errors.add("invalidSoftwareLicneseNotesNumChars", new ActionMessage("common.form.fieldNumberInvalid",
                    Localizer.getText(requestContext, "admin.config.software.licenseNotesCharacters")));
        }
        if (errors.isEmpty()) {
            String issuesColumns = StringUtils.join(actionForm.getIssuesColumns(), ",");
            String hardwareColumns = StringUtils.join(actionForm.getHardwareColumns(), ",");
            String softwareColumns = StringUtils.join(actionForm.getSoftwareColumns(), ",");
            String contractColumns = StringUtils.join(actionForm.getContractColumns(), ",");
            String kbColumns = StringUtils.join(actionForm.getKbColumns(), ",");

            String licenseKey = actionForm.getLicenseKey();
            String licenseKeyPrefix = "KEY:";
            if (!licenseKey.startsWith(licenseKeyPrefix)) {
                licenseKey = licenseKeyPrefix + licenseKey;
            }

            List<SystemConfig> list = new ArrayList<>();
            list.add(new SystemConfig(ConfigKeys.SYSTEM_LICENSE_KEY, licenseKey));
            list.add(new SystemConfig(ConfigKeys.APP_URL, actionForm.getApplicationUrl()));
            list.add(new SystemConfig(ConfigKeys.CURRENCY_OPTION, actionForm.getCurrency()));
            list.add(new SystemConfig(ConfigKeys.LOCAL_TIMEZONE, actionForm.getTimezone()));
            list.add(new SystemConfig(ConfigKeys.LOCALE, actionForm.getLocale()));
            list.add(new SystemConfig(ConfigKeys.SHORT_DATE, actionForm.getShortDateFormat()));
            list.add(new SystemConfig(ConfigKeys.TIME_FORMAT, actionForm.getTimeFormat()));
            list.add(new SystemConfig(ConfigKeys.USER_ROWS, String.valueOf(actionForm.getUsersNumRows())));
            list.add(new SystemConfig(ConfigKeys.USER_NAME_DISPLAY, actionForm.getUserNameDisplay()));
            
            list.add(new SystemConfig(ConfigKeys.HARDWARE_ROWS, String.valueOf(actionForm.getHardwareNumRows())));
            list.add(new SystemConfig(ConfigKeys.HARDWARE_COLUMNS, hardwareColumns));
            list.add(new SystemConfig(ConfigKeys.HARDWARE_WARRANTY_EXPIRE_COUNTDOWN, String.valueOf(actionForm.getHardwareExpireCountdown())));
            list.add(new SystemConfig(ConfigKeys.HARDWARE_CHECK_UNIQUE_NAME, actionForm.getCheckUniqueHardwareName()));
            list.add(new SystemConfig(ConfigKeys.HARDWARE_CHECK_SERIAL_NUMBER, actionForm.isCheckUniqueSerialNumber()));
            list.add(new SystemConfig(ConfigKeys.HARDWARE_BULK_DELETE_ENABLED, actionForm.isBulkHardwareDeleteEnabled()));

            list.add(new SystemConfig(ConfigKeys.COMPANY_ROWS, String.valueOf(actionForm.getCompaniesNumRows())));
            list.add(new SystemConfig(ConfigKeys.CONTACT_ROWS, String.valueOf(actionForm.getContactsNumRows())));
            list.add(new SystemConfig(ConfigKeys.CONTRACT_COLUMNS, contractColumns));
            list.add(new SystemConfig(ConfigKeys.CONTRACT_ROWS, String.valueOf(actionForm.getContractsNumRows())));
            list.add(new SystemConfig(ConfigKeys.CONTRACT_EXPIRE_COUNTDOWN, String.valueOf(actionForm.getContractsExpireCountdown())));
            list.add(new SystemConfig(ConfigKeys.ISSUE_ROWS, String.valueOf(actionForm.getIssuesNumRows())));
            list.add(new SystemConfig(ConfigKeys.ISSUE_COLUMNS, issuesColumns));
            list.add(new SystemConfig(ConfigKeys.ISSUE_GUEST_SUBMIT_MODULE_ENABLED, actionForm.getIssuesGuestSubmitModuleEnabled()));
            list.add(new SystemConfig(ConfigKeys.ISSUE_GUEST_SUBMIT_FOOTER_ENABLED, actionForm.getIssuesGuestSubmitEnabled()));
            list.add(new SystemConfig(ConfigKeys.ISSUE_MULTIPLE_DELETE_ENABLED, actionForm.getIssuesMultipleDeleteEnabled()));
            list.add(new SystemConfig(ConfigKeys.ISSUE_DUE_DATE_DIFF, actionForm.getIssuesDefaultDueDateDiff()));
            list.add(new SystemConfig(ConfigKeys.KB_ARTICLE_COLUMNS, kbColumns));
            list.add(new SystemConfig(ConfigKeys.BLOG_NUM_POSTS, String.valueOf(actionForm.getBlogPostsListNumRows())));
            list.add(new SystemConfig(ConfigKeys.BLOG_NUM_POST_CHARS, String.valueOf(actionForm.getBlogPostCharactersList())));
            list.add(new SystemConfig(ConfigKeys.NUM_PAST_YEARS, actionForm.getNumberOfPastYears()));
            list.add(new SystemConfig(ConfigKeys.NUM_FUTURE_YEARS, actionForm.getNumberOfFutureYears()));
            list.add(new SystemConfig(ConfigKeys.SOFTWARE_COLUMNS, softwareColumns));
            list.add(new SystemConfig(ConfigKeys.SOFTWARE_ROWS, String.valueOf(actionForm.getSoftwareNumRows())));
            list.add(new SystemConfig(ConfigKeys.SOFTWARE_LICENSE_NOTES_NUM_CHARS, String.valueOf(actionForm.getSoftwareLicneseNotesNumChars())));
            list.add(new SystemConfig(ConfigKeys.CUSTOM_FIELDS_EXPAND, actionForm.isLoadCustomFields()));
            list.add(new SystemConfig(ConfigKeys.KB_ARTICLE_MEDIAWIKI_SYNTAX_ENABLED, actionForm.isKbMediaWikiSyntaxEnabled()));

            AdminService adminService = ServiceProvider.getAdminService(requestContext);
            errors = adminService.updateConfig(list);
        }

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_APP_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            Localizer.setSessionLocale(request.getSession(), actionForm.getLocale());

            return ajaxUpdateView(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_APP_CMD);
        }
    }
}
