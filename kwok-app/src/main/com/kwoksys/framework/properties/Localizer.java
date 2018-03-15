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
package com.kwoksys.framework.properties;

import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.SessionManager;

/**
 * Localizer
 */
public class Localizer {

    private static final Logger LOGGER = Logger.getLogger(Localizer.class.getName());
    
    private static MessageResources messageResources;

    public static void init() {
        // Load MessageResources
        LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Loading MessageResources from " + Globals.MESSAGES_KEY + "...");
        messageResources = MessageResources.getMessageResources(AppPaths.LOCALIZATION_PROPERTIES);
    }

    /**
     * Sets session locale
     */
    public static void setSessionLocale(HttpSession session, String localeString) {
        String[] strings = localeString.split("_");
        session.setAttribute(SessionManager.SESSION_LOCALE, new Locale(strings[0], strings[1]));
    }

    public static String getText(Locale locale, String contentKey, Object[] params) {
        return messageResources.getMessage(locale, contentKey, params);
    }

    public static String getText(Locale locale, String contentKey) {
        return getText(locale, contentKey, null);
    }

    public static String getText(RequestContext requestContext, String contentKey, Object[] params) {
        return getText(requestContext.getLocale(), contentKey, params);
    }

    public static String getText(RequestContext requestContext, String contentKey) {
        return getText(requestContext, contentKey, null);
    }
}
