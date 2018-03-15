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
package com.kwoksys.biz.auth.core;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.dto.AccessPage;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.FeatureManager;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.exceptions.AjaxAuthenticationRequiredException;
import com.kwoksys.framework.exceptions.AuthenticationRequiredException;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.session.CookieManager;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.UrlUtils;

/**
 * Access
 */
public class Access {

    public static final Integer ADMIN_USER_ID = 1;
    public static final Integer GUEST_USER_ID = -1001;

    public static final String AUTH_APP = "app";
    public static final String AUTH_LDAP = "ldap";
    public static final String AUTH_MIXED = "mixed";

    private static final Set<String> publicPagesMap = new HashSet<>();

    private static final Set<String> issuesGuestSubmitPages = new HashSet<>();

    public static final AccessUser GUESS_USER = new AccessUser(Access.GUEST_USER_ID);

    static {
        // Auth module
        publicPagesMap.add(AppPaths.AUTH_LOGOUT);
        publicPagesMap.add(AppPaths.AUTH_VERIFY);

        // Home module
        publicPagesMap.add(AppPaths.HOME_INDEX);
        publicPagesMap.add(AppPaths.HOME_UNAUTHORIZED);
        publicPagesMap.add(AppPaths.HOME_FORBIDDEN);
        publicPagesMap.add(AppPaths.HOME_FILE_NOT_FOUND);
        publicPagesMap.add(AppPaths.HOME_OBJECT_NOT_FOUND);
        publicPagesMap.add(AppPaths.HOME_NON_SUPPORTED_BROWSER);

        issuesGuestSubmitPages.add(AppPaths.ISSUE_PLUGIN_ADD);
        issuesGuestSubmitPages.add(AppPaths.ISSUE_PLUGIN_ADD_2);
        issuesGuestSubmitPages.add(AppPaths.ISSUE_PLUGIN_ADD_3);
        issuesGuestSubmitPages.add(AppPaths.ISSUE_PLUGIN_LEGEND_DETAIL);
    }

    /**
     * Return true if the module is a public page. Public page usually doesn't require authentication.
     * @return
     */
    public static boolean isPublicPage(String path) {
        return publicPagesMap.contains(path);
    }

    public static boolean hasSpecialPagePermission(AccessUser accessUser, String pageName) {
        if (issuesGuestSubmitPages.contains(pageName)) {
            return FeatureManager.isIssueGuestSubmitFooterEnabled() || FeatureManager.isIssueGuestSubmitModuleEnabled(accessUser);
        }
        return false;
    }

    /**
     * Returns whether a user is granted a given permission
     * @param request
     * @param permId
     * @return
     * @throws DatabaseException
     */
    public static boolean hasPermission(AccessUser user, Integer permId) throws DatabaseException {
        // We want default administrator to always have permission to see every page
        if (isDefaultAdmin(user.getId())) {
            return true;
        }
        Set<Integer> permSet = getEffectivePermissions(user);
        return permSet.contains(permId);
    }

    /**
     * Returns whether the user can view a specific page.
     * Use AccessUser.hasPermission() instead. TODO: Take out public when done with refactoring
     */
    public static boolean hasPermission(AccessUser user, String pageName) throws DatabaseException {
        // If the user hasn't logged on yet, the user doesn't have much access
        if (user == null) {
            return false;
        }

        if (Access.isPublicPage(pageName) || Access.hasSpecialPagePermission(user, pageName)) {
            return true;
        }

        // Check pageId first to make sure the page is valid, before even letting admin see it
        AccessPage accessPage = getAccessPage(pageName);
        if (accessPage == null) {
            return false;
        }

        // We want default administrator to always have permission to see every page
        if (isDefaultAdmin(user.getId())) {
            return true;
        }

        Set<Integer> permSet = getEffectivePermissions(user);

        // If the perm map has the page id, the user has permission to see
        for (Integer permId : permSet) {
            Set<Integer> set = getCachedPermissionPages(permId);
            if (set != null && set.contains(accessPage.getPageId())) {
                return true;
            }
        }
        return false;
    }

    private static Set<Integer> getEffectivePermissions(AccessUser user) throws DatabaseException {
        // If user belongs to a group, use group permission, otherwise, use the user's permission
        if (user.getGroupId() != 0) {
            return new CacheManager().getGroupPermissionsCache(user.getGroupId());
        } else {
            return new CacheManager().getUserPermissionsCache(user.getId());
        }
    }

    /**
     * Get access page object from pageName.
     * @param pageName
     * @return
     */
    public static AccessPage getAccessPage(String pageName) throws DatabaseException {
        return new CacheManager().getPagesInfoCache().get(pageName);
    }

    /**
     * Permission's associations with pages
     */
    private static Set<Integer> getCachedPermissionPages(Integer permissionId) throws DatabaseException {
        return new CacheManager().getPermissionPagesCache().get(permissionId);
    }

    public static AccessUser getCookieUser(Cookie[] cookies) {
        Integer cookieUserId = NumberUtils.replaceNull(CookieManager.getUserId(cookies), GUEST_USER_ID);

        return new AccessUser(cookieUserId);
    }

    public static boolean isDefaultAdmin(Integer userId) {
        return (userId.equals(ADMIN_USER_ID));
    }

    public static void requestCredential(RequestContext requestContext, ResponseContext responseContext,
                                         String errorCode) throws Exception {
        HttpServletRequest request = requestContext.getRequest();
        StringBuilder sb = new StringBuilder();
        sb.append(AppPaths.ROOT + AppPaths.HOME_INDEX).append("?redirectPath=");

        if (!(AppPaths.AUTH_LOGOUT).equals(requestContext.getPageName())
                && !request.getMethod().equals("POST")
                && !requestContext.getPageName().contains("/ajax-")) {

            String queryString = request.getQueryString();
            // Also remove _ajax=true.
            if (queryString != null) {
                queryString = queryString.replace("&" + RequestContext.URL_PARAM_AJAX + "=true", "");
                queryString = queryString.replace(RequestContext.URL_PARAM_AJAX + "=true", "");
            }

            sb.append(UrlUtils.encode(UrlUtils.formatQueryString(requestContext.getPageName(), queryString)));
        }
        sb.append("&errorCode=").append(errorCode).append("&").append(RequestContext.URL_PARAM_ERROR_TRUE);

        if (requestContext.isAjax()) {
            throw new AjaxAuthenticationRequiredException(sb.toString());
        } else {
            responseContext.getResponse().sendRedirect(sb.toString());
        }
    }

    public static void requestBasicAuthCredential(RequestContext requestContext, ResponseContext responseContext) throws Exception {
        String realm = ConfigManager.system.getCompanyName().isEmpty()
                    ? Localizer.getText(requestContext, "common.app.name") : ConfigManager.system.getCompanyName();

        responseContext.getResponse().setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
        throw new AuthenticationRequiredException();
    }
}
