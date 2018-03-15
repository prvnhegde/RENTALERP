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
package com.kwoksys.framework.util;

import com.kwoksys.biz.system.core.configs.ConfigManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * UrlUtils.
 */
public class UrlUtils {

    /**
     * This method does URL encoding.
     *
     * @param url
     * @return ..
     */
    public static String encode(String url) {
        if (url == null) {
            return "";
        }
        try {
            return URLEncoder.encode(url, ConfigManager.system.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Returns a formatted url by taking a url path and url queries, and depends on whether there is
     * anything in the url queries, appends a question mark if necessary.
     *
     * @param pathInfo
     * @param queryInfo
     * @return ..
     */
    public static String formatQueryString(String pathInfo, String queryInfo) {
        return queryInfo == null ? pathInfo : pathInfo + "?" + queryInfo;
    }
}
