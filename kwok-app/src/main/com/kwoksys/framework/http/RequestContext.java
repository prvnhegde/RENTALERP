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
package com.kwoksys.framework.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.StringUtils;
import com.opensymphony.xwork2.config.entities.ActionConfig;

/**
 * RequestContext
 */
public class RequestContext {

    public static final String PAGE_START_TIME = "_pageStartTime";

    public static final String PAGE_EXEC_TIME = "_pageExecutionTime";

    public static final String MODULE_KEY = "_module";

    public static final String USER_KEY = "_user";
    
    public static final String ERROR_MESSAGES_KEY = "_errors";

    public static final String FORM_KEY = "form";

    public static final String URL_PARAM_RESUBMIT = "_resubmit";

    public static final String URL_PARAM_AJAX = "_ajax";
    
    public static final String URL_PARAM_NOTIFY = "_notify";

    public static final String URL_PARAM_NOTIFY_TRUE = URL_PARAM_NOTIFY + "=true";
    
    public static final String URL_PARAM_ERROR = "_error";

    public static final String URL_PARAM_ERROR_TRUE = URL_PARAM_ERROR + "=true";

    private HttpServletRequest request;

    private String pageName;

    private ActionConfig actionConfig;

    private AccessUser user = Access.GUESS_USER;

    private Date sysDate;
    
    public RequestContext() {}

    public RequestContext(HttpServletRequest request) {
        this.request = request;
    }

    public AccessUser getUser() {
        if (request != null) {
            user = (AccessUser) request.getAttribute(USER_KEY);
        }
        return user;
    }

    public void setSysdate(Date sysDate) {
        this.sysDate = sysDate;
    }
    
    public Date getSysdate() {
        return sysDate;
    }

    public Locale getLocale() {
       return getLocale(request);
    }

    public static Locale getLocale(HttpServletRequest request) {
        if (request != null) {
            // Get locale from session attribute
            return (Locale)request.getSession().getAttribute(SessionManager.SESSION_LOCALE);

        } else {
            // Returns default locale
            return ConfigManager.system.getLocale();
        }
    }

    public int getParameter(String paramName) {
        String requestValue = request.getParameter(paramName);
        return NumberUtils.replaceNull(requestValue);
    }

    public int getParameter(String paramName, int defaultValue) {
        String requestValue = request.getParameter(paramName);
        return NumberUtils.replaceNull(requestValue, defaultValue);
    }

    public Integer getParameterInteger(String paramName) {
        String requestValue = request.getParameter(paramName);
        return requestValue == null ? null : NumberUtils.replaceNull(requestValue);
    }

    public String getParameterString(String paramName) {
        String requestValue = request.getParameter(paramName);
        return StringUtils.replaceNull(requestValue);
    }

    public String getParameterString(String paramName, String defaultValue) {
        String requestValue = request.getParameter(paramName);
        return StringUtils.replaceNull(requestValue, defaultValue);
    }

    public List<String> getParameterStrings(String paramName, List<String> defaultValues) {
        if (request.getParameterValues(paramName) == null) {
            return defaultValues;
        }
        return getParameterStrings(paramName);
    }

    public List<String> getParameterStrings(String paramName) {
        String[] strings = request.getParameterValues(paramName);
        List<String> list = new ArrayList<>();
        if (strings != null) {
            for (String value : strings) {
                if (!value.isEmpty()) {
                    list.add(value);
                }
            }
        }
        return list;
    }

    public boolean getParameterBoolean(String paramName) {
        String requestValue = request.getParameter(paramName);
        return "true".equals(requestValue);
    }

    public boolean getParameterBoolean(String paramName, boolean defaultValue) {
        String requestValue = request.getParameter(paramName);

        if (requestValue == null) {
            return defaultValue;
        }
        return "true".equals(requestValue);
    }

    public List<Integer> getParameters(String paramName) {
        String[] strings = request.getParameterValues(paramName);
        List<Integer> list = new ArrayList<>();
        if (strings != null) {
            for (String value : strings) {
                if (!value.isEmpty()) {
                    list.add(Integer.parseInt(value));
                }
            }
        }
        return list;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpSession getSession() {
        return request.getSession();
    }    
    
    public void setUser(AccessUser user) {
        this.user = user;
    }

    public boolean isAjax() {
        return getParameterBoolean(URL_PARAM_AJAX);
    }

    public boolean hasErrors() {
        return getParameterBoolean(RequestContext.URL_PARAM_ERROR) || 
                "true".equals(request.getAttribute(RequestContext.URL_PARAM_ERROR));
    }

    public String getPageName() {
        return pageName == null ? request.getServletPath() : pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public ActionConfig getActionConfig() {
        return actionConfig;
    }

    public void setActionConfig(ActionConfig actionConfig) {
        this.actionConfig = actionConfig;
    }
}

