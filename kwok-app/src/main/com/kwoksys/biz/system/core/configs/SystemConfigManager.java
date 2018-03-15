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

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * SystemConfigManager
 */
public class SystemConfigManager extends BaseConfigManager {

    private static SystemConfigManager instance = new SystemConfigManager();

    private static final String EXTENSION = ".htm";

    private static final String CHARACTER_ENCODING = "UTF-8";

    private String appUrl;

    private String cacheKey;

    private Long cacheTime;

    private String licenseKey;

    private String trailingSlash;

    private String currencySymbol;

    private String customHomeDescription;

    private String[] localeOptions;

    private TimeZone timezoneBase;

    private String timezoneBaseString;
    
    private TimeZone timezoneLocal;
    
    private String timezoneLocalString;

    private String[] timezoneLocalOptions;

    private String timeFormat;

    private String[] timeFormatOptions;

    private String usernameDisplay;

    private String dateFormat;

    private String datetimeBase;

    private List<String> moduleTabs;

    private String theme;

    private String[] themeOptions;

    private int[] fontOptions;

    private String[] dateFormatOptions;

    private String localeString;

    private Locale locale;

    private String companyName;

    private String companyPath;

    private String companyLogoPath;

    private String companyFooterNotes;

    private String sytlesheet;
    
    private String detailsTableStyle;

    /**
     * Number of database connections allowed in the connection pool.
     */
    public static final int DB_MAX_CONNECTION_UNLIITED = -1;
    private int dbMaxConnection = DB_MAX_CONNECTION_UNLIITED;

    private SystemConfigManager() {}

    static SystemConfigManager getInstance() {
        return instance;
    }

    void init(ConfigManager configManager) {
        appUrl = configManager.getString(ConfigKeys.APP_URL);

        cacheKey = configManager.getString(ConfigKeys.SYSTEM_CACHE_KEY);

        currencySymbol = configManager.getString(ConfigKeys.CURRENCY_OPTION);

        companyName = configManager.getString(ConfigKeys.COMPANY_NAME);

        companyPath = configManager.getString(ConfigKeys.COMPANY_PATH);

        companyLogoPath = configManager.getString(ConfigKeys.COMPANY_LOGO_PATH);

        companyFooterNotes = configManager.getString(ConfigKeys.COMPANY_FOOTER_NOTES);

        customHomeDescription = configManager.getString(ConfigKeys.CUSTOM_HOME_DESCRIPTION);

        datetimeBase = configManager.getString("datetime.base");

        dateFormat = configManager.getString(ConfigKeys.SHORT_DATE);

        dateFormatOptions = configManager.getStringArray("datetime.shortDateFormat.options");

        licenseKey = configManager.getString(ConfigKeys.SYSTEM_LICENSE_KEY);

        localeString = configManager.getString(ConfigKeys.LOCALE);

        String[] strings = localeString.split("_");
        locale = new Locale(strings[0], strings[1]);

        localeOptions = configManager.getStringArray(ConfigKeys.LOCALE_OPTIONS);

        moduleTabs = configManager.getStringList("template.moduleTabs");

        theme = configManager.getString(ConfigKeys.THEME_DEFAULT);

        // TODO: update database with new theme
        //themeOptions = getStringArray(SystemConfigNames.THEME_OPTIONS);
        themeOptions = new String[] {"blue", "green", "red", "orange", "purple", "gray"};

        fontOptions = new int[]{12, 13, 14};

        timezoneBaseString = configManager.getString(ConfigKeys.TIMEZONE_BASE);
        
        timezoneBase = TimeZone.getTimeZone(timezoneBaseString);

        timezoneLocalString = configManager.getString(ConfigKeys.LOCAL_TIMEZONE);
        
        timezoneLocal = TimeZone.getTimeZone(timezoneLocalString);

        timezoneLocalOptions = configManager.getStringArray("timezone.local.options");

        trailingSlash = System.getProperty("file.separator");

        sytlesheet = configManager.getString(ConfigKeys.UI_STYLESHEET);

        detailsTableStyle = "standard details"; // Another option is "standardForm"
                
        timeFormat = configManager.getString(ConfigKeys.TIME_FORMAT);

        timeFormatOptions = configManager.getStringArray("datetime.timeFormat.options");

        usernameDisplay = configManager.getString(ConfigKeys.USER_NAME_DISPLAY);

        dbMaxConnection = configManager.getInt(ConfigKeys.SYSTEM_DB_MAX_CONNECTION);
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String getLocaleString() {
        return localeString;
    }

    public String getTrailingSlash() {
        return trailingSlash;
    }

    public String[] getTimeFormatOptions() {
        return timeFormatOptions;
    }

    public String[] getDateFormatOptions() {
        return dateFormatOptions;
    }

    public String[] getLocaleOptions() {
        return localeOptions;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String[] getTimezoneLocalOptions() {
        return timezoneLocalOptions;
    }

    public String getDatetimeBase() {
        return datetimeBase;
    }

    public List<String> getModuleTabs() {
        return moduleTabs;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCharacterEncoding() {
        return CHARACTER_ENCODING;
    }

    public String[] getThemeOptions() {
        return themeOptions;
    }

    public String getCustomHomeDescription() {
        return customHomeDescription;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public String getTheme() {
        return theme;
    }

    public String getTimezoneBaseString() {
        return timezoneBaseString;
    }
    
    public TimeZone getTimezoneBase() {
        return timezoneBase;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public String getCompanyLogoPath() {
        return companyLogoPath;
    }

    public String getCompanyPath() {
        return companyPath;
    }

    public String getCompanyFooterNotes() {
        return companyFooterNotes;
    }

    public String getSytlesheet() {
        return sytlesheet;
    }

    public TimeZone getTimezoneLocal() {
        return timezoneLocal;
    }

    public String getTimezoneLocalString() {
        return timezoneLocalString;
    }

    public String getUsernameDisplay() {
        return usernameDisplay;
    }

    public Locale getLocale() {
        return locale;
    }

    public Long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(Long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public int[] getFontOptions() {
        return fontOptions;
    }

    public int getDbMaxConnection() {
        return dbMaxConnection;
    }

    public String getExtension() {
        return EXTENSION;
    }

    public String getDetailsTableStyle() {
        return detailsTableStyle;
    }
}
