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
package com.kwoksys.framework.session;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;

import com.kwoksys.biz.reports.ReportFactory;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;

/**
 * Class for managing session variables.
 */
public class SessionManager {

    /**
     * This is for storing session locale.
     * We'll use Globals.LOCALE_KEY key to make what Strut uses, so that we
     * can localize bean:message.
     */
    public static final String SESSION_INIT = "SESSION_INIT";

    public static final String BROWSER_CHECKED = "BROWSER_CHECKED";

    public static final String SESSION_LOCALE = Globals.LOCALE_KEY;

    public static final String SESSION_THEME = "SESSION_THEME";

    public static final String FONT_SIZE = "FONT_SIZE";

    public static final String USER_SEARCH_CRITERIA_MAP = "USER_SEARCH_CRITERIA_MAP";

    public static final String COMPANY_SEARCH_CRITERIA_MAP = "COMPANY_SEARCH_CRITERIA_MAP";

    public static final String CONTACT_SEARCH_CRITERIA_MAP = "CONTACT_SEARCH_CRITERIA_MAP";

    public static final String CONTRACT_SEARCH_CRITERIA_MAP = "CONTRACT_SEARCH_CRITERIA_MAP";

    public static final String CONTRACT_REPORT_CRITERIA_MAP = "CONTRACT_REPORT_CRITERIA_MAP";

    public static final String ISSUE_SEARCH_CRITERIA_MAP = "ISSUE_SEARCH_CRITERIA_MAP";

    public static final String ISSUE_REPORT_CRITERIA_MAP = "ISSUE_REPORT_CRITERIA_MAP";

    public static final String HARDWARE_SEARCH_CRITERIA_MAP = "HARDWARE_SEARCH_CRITERIA_MAP";

    public static final String HARDWARE_REPORT_CRITERIA_MAP = "HARDWARE_REPORT_CRITERIA_MAP";

    public static final String SOFTWARE_SEARCH_CRITERIA_MAP = "SOFTWARE_SEARCH_CRITERIA_MAP";

    public static final String SOFTWARE_REPORT_CRITERIA_MAP = "SOFTWARE_REPORT_CRITERIA_MAP";

    public static final String SOFTWARE_USAGE_REPORT_CRITERIA_MAP = "SOFTWARE_USAGE_REPORT_CRITERIA_MAP";

    public static final String BLOG_POST_SEARCH_CRITERIA_MAP = "BLOG_POST_SEARCH_CRITERIA_MAP";

    public static final String IMPORT_RESULTS = "IMPORT_RESULTS";

    public static final String IMPORT_RESULTS_MESSAGE = "IMPORT_RESULTS_MESSAGE";

    public static final String BLOG_POSTS_ROW_START = "blogPostsRowStart";

    public static final String COMPANIES_ORDER = "COMPANIES_ORDER";
    public static final String COMPANIES_ORDER_BY = "COMPANIES_ORDER_BY";
    public static final String COMPANIES_ROW_START = "COMPANIES_ROW_START";
    public static final String COMPANY_CONTACTS_ORDER = "COMPANY_CONTACTS_ORDER";
    public static final String COMPANY_CONTACTS_ORDER_BY = "COMPANY_CONTACTS_ORDER_BY";

    public static final String CONTACTS_ROW_START = "CONTACTS_ROW_START";
    public static final String CONTACTS_ORDER = "CONTACTS_ORDER";
    public static final String CONTACTS_ORDER_BY = "CONTACTS_ORDER_BY";
    public static final String CONTRACTS_ORDER = "CONTRACTS_ORDER";
    public static final String CONTRACTS_ORDER_BY = "CONTRACTS_ORDER_BY";
    public static final String CONTRACTS_ROW_START = "CONTRACTS_ROW_START";

    public static final String FILES_ORDER = "FILES_ORDER";
    public static final String FILES_ORDER_BY = "FILES_ORDER_BY";

    public static final String HARDWARE_ORDER = "HARDWARE_ORDER";
    public static final String HARDWARE_ORDER_BY = "HARDWARE_ORDER_BY";
    public static final String HARDWARE_CONTACTS_ORDER = "HARDWARE_CONTACTS_ORDER";
    public static final String HARDWARE_CONTACTS_ORDER_BY = "HARDWARE_CONTACTS_ORDER_BY";
    public static final String HARDWARE_MEMBER_ORDER = "HARDWARE_MEMBER_ORDER";
    public static final String HARDWARE_MEMBER_ORDER_BY = "HARDWARE_MEMBER_ORDER_BY";
    public static final String HARDWARE_MEMBER_OF_ORDER = "HARDWARE_MEMBER_OF_ORDER";
    public static final String HARDWARE_MEMBER_OF_ORDER_BY = "HARDWARE_MEMBER_OF_ORDER_BY";
    public static final String HARDWARE_ROW_START = "HARDWARE_ROW_START";

    public static final String ISSUES_ORDER = "ISSUES_ORDER";
    public static final String ISSUES_ORDER_BY = "ISSUES_ORDER_BY";
    public static final String ISSUES_ROW_START = "ISSUES_ROW_START";
    public static final String ISSUE_HISTORY_ORDER = "ISSUE_HISTORY_ORDER";
    public static final String ISSUE_HISTORY_ORDER_BY = "ISSUE_HISTORY_ORDER_BY";

    public static final String SITES_ORDER = "SITES_ORDER";
    public static final String SITES_ORDER_BY = "SITES_ORDER_BY";
    public static final String SITES_ROW_START = "SITES_ROW_START";

    public static final String SOFTWARE_ORDER = "SOFTWARE_ORDER";
    public static final String SOFTWARE_ORDER_BY = "SOFTWARE_ORDER_BY";
    public static final String SOFTWARE_ROW_START = "SOFTWARE_ROW_START";

    public static final String USERS_ORDER = "USERS_ORDER";
    public static final String USERS_ORDER_BY = "USERS_ORDER_BY";
    public static final String USERS_ROW_START = "USERS_ROW_START";

    /**
     * Sets session theme.
     *
     * @param session
     * @param reqTheme
     * @return ..
     */
    public static String setAppSessionTheme(HttpSession session, String reqTheme) {
        if (!reqTheme.isEmpty()) {
            List<String> themes = Arrays.asList(ConfigManager.system.getThemeOptions());

            // Check to see if it's a valid theme
            if (themes.contains(reqTheme)) {
                session.setAttribute(SESSION_THEME, reqTheme);
                return reqTheme;
            }
        }
        return ConfigManager.system.getTheme();
    }

    /**
     * Get sessionTheme.
     *
     * @return ..
     */
    public static String getAppSessionTheme(HttpSession session) {
        String reqSessionTheme = (String) session.getAttribute(SESSION_THEME);
        return reqSessionTheme != null ? reqSessionTheme : ConfigManager.system.getTheme();
    }


    /**
     * Clear form data for Reports.
     * @param session
     * @param reportType
     */
    public static void clearReportFromSession(RequestContext requestContext, String reportType) throws Exception {
         requestContext.getSession().setAttribute(ReportFactory.getReport(requestContext, reportType).getReportFormName(), null);
    }

    public static String getOrSetAttribute(RequestContext requestContext, String paramName, String sessionKey, String sessionValue) {
        String returnValue = requestContext.getParameterString(paramName);

        if (returnValue.isEmpty()) {
            returnValue = (String) requestContext.getSession().getAttribute(sessionKey);
            if (returnValue != null) {
                return returnValue;
            }
            return sessionValue;
        } else {
            requestContext.getSession().setAttribute(sessionKey, returnValue);
            return returnValue;
        }
    }

    public static Integer getOrSetAttribute(RequestContext requestContext, String paramName, String sessionKey, Integer sessionValue) {
        Integer returnValue = requestContext.getParameterInteger(paramName);

        if (returnValue == null) {
            returnValue = (Integer) requestContext.getSession().getAttribute(sessionKey);
            if (returnValue != null) {
                return returnValue;
            }
            return sessionValue;
        } else {
            requestContext.getSession().setAttribute(sessionKey, returnValue);
            return returnValue;
        }
    }

    public static String getAttribute(HttpServletRequest request, String sessionKey, String sessionValue) {
        String returnValue = (String) request.getSession().getAttribute(sessionKey);
        if (returnValue != null) {
            return returnValue;
        } else {
            return sessionValue;
        }
    }
}
