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

import com.kwoksys.biz.system.core.AppPaths;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * CookieManager class.
 */
public class CookieManager {

    private static final String USER_ID = "userId";

    private static final String SESSION_TOKEN = "sessionToken";
    
    private static final String CUSTOM_FIELDS_EXPAND = "customFieldsExpand";

    /**
     * Gets a value from cookies.
     *
     * @param cookies
     * @param cookieName
     * @return ..
     */
    private static String getCookieValue(Cookie[] cookies, String cookieName) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }

    private static void setCookie(HttpServletResponse response, String cookieKey, String cookieValue) {
        Cookie cookie = new Cookie(cookieKey, cookieValue);
        cookie.setPath(AppPaths.COOKIE_ROOT);
        response.addCookie(cookie);
    }
    
    private static void clearCookie(HttpServletResponse response, String cookieKey) {
        Cookie cookie = new Cookie(cookieKey, "");
        cookie.setPath(AppPaths.COOKIE_ROOT);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * userId cookie.
     *
     * @param response
     * @param cookieValue
     */
    public static void setUserId(HttpServletResponse response, String cookieValue) {
        setCookie(response, USER_ID, cookieValue);
    }
    public static String getUserId(Cookie[] cookies) {
        return getCookieValue(cookies, USER_ID);
    }

    /**
     * sessionToken cookie.
     *
     * @param response
     * @param cookieValue
     */
    public static void setSessionToken(HttpServletResponse response, String cookieValue) {
        setCookie(response, SESSION_TOKEN, cookieValue);
    }
    public static String getSessionToken(Cookie[] cookies) {
        return getCookieValue(cookies, SESSION_TOKEN);
    }
    
    public static String getCustomFieldsExpand(Cookie[] cookies) {
        return getCookieValue(cookies, CUSTOM_FIELDS_EXPAND);
    }
    
    public static void clearAllCookies(HttpServletResponse response) {
        clearCookie(response, USER_ID);
        clearCookie(response, SESSION_TOKEN);
        clearCookie(response, CUSTOM_FIELDS_EXPAND);
    }
}
