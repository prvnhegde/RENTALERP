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
package com.kwoksys.framework.struts2;

import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.AuthService;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.AuthUtils;
import com.kwoksys.biz.auth.dto.AccessPage;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.biz.system.dto.SystemInfo;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.exceptions.AjaxAuthenticationRequiredException;
import com.kwoksys.framework.exceptions.AuthenticationRequiredException;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.NonSupportedBrowserException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.servlets.SystemInitServlet;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.session.CookieManager;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.validations.BrowserValidator;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class RequestInterceptor extends AbstractInterceptor {

    private static final Logger LOGGER = Logger.getLogger(RequestInterceptor.class.getName());

	public String intercept(ActionInvocation actionInvocation) throws Exception {
        RequestContext requestContext = new RequestContext(ServletActionContext.getRequest());
        ResponseContext responseContext = new ResponseContext(ServletActionContext.getResponse());

        try {
            ActionProxy actionProxy = actionInvocation.getProxy();
            requestContext.setActionConfig(actionProxy.getConfig());

            Action2 action2 = (Action2) actionProxy.getAction();
            action2.setRequestContext(requestContext);

            requestContext.setPageName("/" + actionProxy.getActionName() + ConfigManager.system.getExtension());

            if (!validate(requestContext, responseContext)) {
                return null;
            }

            return actionProxy.getInvocation().invoke();

        } catch (AuthenticationRequiredException e) {
            // HTTP 401
            responseContext.sendUnauthorized();
            return Action2.AUTHENTICATION_REQUIRED_TEMPLATE;

        } catch (AjaxAuthenticationRequiredException e) {
            // HTTP 401
            AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);
            ajaxTemplate.setAjaxTemplatePath(AjaxTemplate.getTemplatePath(requestContext, Action2.AJAX_AUTHENTICATION_TEMPLATE));
            
            // Applies this template. Normally, the same result can be achieved using findTemplate(), if called from
            // an Action.
            ajaxTemplate.applyTemplate();
            
            requestContext.getRequest().setAttribute("redirectUrl", e.getLoginUrl());

            return Action2.AJAX_TEMPLATE;

        } catch (AccessDeniedException e) {
            // HTTP 403
            return Action2.ACCESS_DENIED;

        } catch (FileNotFoundException e) {
            // HTTP 404
            return Action2.FILE_NOT_FOUND;

        } catch (NonSupportedBrowserException e) {
            return Action2.NON_SUPPORTED_BROWSER;

        } catch (ObjectNotFoundException e) {
            LOGGER.log(Level.WARNING, "Object not found. " + e.getMessage());
            return Action2.OBJECT_NOT_FOUND;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Problem processing request.", e);
            responseContext.sendServerError();
            return null;
        }
    }

    public static boolean validate(RequestContext requestContext, ResponseContext responseContext) throws Exception {
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = responseContext.getResponse();

        // This line is required for utf-8 encoding to work.
        request.setCharacterEncoding(ConfigManager.system.getCharacterEncoding());

        // Pass pageStartTime to jsp pages.
        request.setAttribute(RequestContext.PAGE_START_TIME, System.currentTimeMillis());

        // Use Edge document mode. Until IE compatibility mode is dropped.
        response.setHeader("X-UA-Compatible", "IE=edge"); 
        
        // These lines are for making browsers not to cache pages, especially for AJAX calls to work correctly.
        response.setHeader("Pragma", "no-cache"); // For HTTP/1.0 backward compatibility.
        response.setHeader("Cache-Control", "no-cache"); // For HTTP/1.1.

        response.setCharacterEncoding(ConfigManager.system.getCharacterEncoding());

        if (!SystemInitServlet.init) {
            // HTTP 503.
            LOGGER.warning("System not initialized. " + SystemInitServlet.initError);
            responseContext.sendServiceUnavailable();
            return false;
        }

        // Checks database availability by executing a query, also check to see if the current cache key matches
        // cached cache key. This is for cache flushing across multiple servers.
        SystemService systemService = ServiceProvider.getSystemService(requestContext);

        SystemInfo systemInfo;
        try {
            systemInfo = systemService.getSystemInfo();
            if (!systemInfo.getCacheKey().equals(ConfigManager.system.getCacheKey())) {
                // Refresh the cache.
                ConfigManager.getInstance().init();
            }

            // Run a process to check if there are caches to remove
            new CacheManager(requestContext).checkRemoveCaches(systemInfo.getSysdate().getTime());

        } catch (DatabaseException e) {
            // HTTP 503.
            responseContext.sendServiceUnavailable();
            return false;
        }

        // Do this once for a session
        initSession(requestContext);

        String pageName = requestContext.getPageName();
        AccessPage accessPage = Access.getAccessPage(pageName);

        request.setAttribute(RequestContext.MODULE_KEY, (accessPage == null ? 0 : accessPage.getModuleId()));
        requestContext.setSysdate(systemInfo.getSysdate());

        Cookie[] cookies = request.getCookies();
        AccessUser user = Access.getCookieUser(cookies);

        String sessionToken = CookieManager.getSessionToken(cookies).trim();

        // Ready to serve the page, let's log it first.
        LOGGER.info(LogConfigManager.PAGE_REQUEST_PREFIX + " (" + request.getMethod() + ") "+ AppPaths.ROOT + requestContext.getPageName() + ", user ID: " + user.getId());

        AuthService authService = ServiceProvider.getAuthService(requestContext);
        boolean isValidSessionToken = authService.isValidUserSession(user.getId(), sessionToken);

        // Check if the session is valid
        if (!isValidSessionToken) {
            // Since basic auth doesn't have a dedicated login page, this serve as the login mechanism.
            if (ConfigManager.auth.isBasicAuth()) {
                if (!sessionToken.isEmpty() || !authService.isValidBasicAuthentication(user)) {
                    AuthUtils.resetAuthCookies(response, user);
                    Access.requestBasicAuthCredential(requestContext, responseContext);
                    return false;
                }

                // Initialize user session
                authService.initializeUserSession(request, response, user);

                // Reset isValidSessionToken to true.
                isValidSessionToken = true;
            } else {
                AuthUtils.resetAuthCookies(response, user);
            }
        }

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        user = adminService.getUser(user.getId());

        request.setAttribute(RequestContext.USER_KEY, user);

        if (user.hasPermission(pageName)) {
            return true;
        }

        if (accessPage == null) {
            // HTTP 404. We don't have such page.
            throw new FileNotFoundException();
        }

        if (!user.isLoggedOn()) {
            // HTTP 401. Request username/password with error code loginRequired.
            Access.requestCredential(requestContext, responseContext, "loginRequired");
            return false;
        }

        if (!isValidSessionToken) {
            // HTTP 401. Request username/password with error code sessionExpired.
            Access.requestCredential(requestContext, responseContext, "sessionExpired");
            return false;
        }

         // HTTP 403. Access denied.
         throw new AccessDeniedException();
    }

    private static void initSession(RequestContext requestContext) throws NonSupportedBrowserException {
        HttpSession session = requestContext.getSession();

        if (session.getAttribute(SessionManager.SESSION_INIT) == null) {
            session.setAttribute(SessionManager.SESSION_INIT, true);

            Localizer.setSessionLocale(session, ConfigManager.system.getLocaleString());
        }

        if (session.getAttribute(SessionManager.BROWSER_CHECKED) == null
                && !requestContext.getPageName().equals(AppPaths.HOME_NON_SUPPORTED_BROWSER)) {
            if (!BrowserValidator.isSupportedBrowser(requestContext.getRequest().getHeader("user-agent"))) {
                throw new NonSupportedBrowserException();
            }
            session.setAttribute(SessionManager.BROWSER_CHECKED, true);
        }
    }

    private static void printRequest(HttpServletRequest request) {
        System.out.println("\n=== Method ===");
        System.out.println(request.getMethod());
        
        System.out.println("\n=== Headers ===");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            System.out.println(name + ": " + request.getHeader(name));
        }

        System.out.println("\n=== Parameters ===");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            System.out.println(name + ": " + request.getParameter(name));
        }
    }
}
