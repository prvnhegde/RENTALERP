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
package com.kwoksys.framework.parsers.wiki;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;

/**
 * Parser
 */
public abstract class Parser {

    public static final String WIKI_URL = AppPaths.KB_ARTICLE_DETAIL + "?title=";

    private Map<String, String> options;

    public abstract String parseHtml(RequestContext requestContext, String content);

    public void setOption(String propName, String propValue) {
        if (options == null) {
            options = new HashMap<>();
        }
        options.put(propName, propValue);
    }

    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * Replaces line breaks with an html BR tag.
     * @param content
     * @return
     */
    public static String replaceLineBreaks(String content) {
        return content == null ? "" : content.replace("\n", "<br>");
    }

    /**
     * Encodes content to be html friendly.
     * @param content
     * @return
     */
    public static String encodeHtml(String content) {
        return StringEscapeUtils.escapeHtml4(content);
    }

    public static StringBuffer encodeHtml(StringBuffer input) {
        if (input == null || input.length() == 0) {
            return new StringBuffer();
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb;
    }

    public static String decodeHtml(String input) {
        return StringEscapeUtils.unescapeHtml4(input);
    }

    /**
     * Converts http, https, etc to html href link.
     * @param fieldValue
     * @return
     */
    public static String convertUrls(String fieldValue) {
        String regex = "(?<!\")((http|https|ftp)://[^\\s]+)(?!\")";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fieldValue);
        if (m.find()) {
            fieldValue = m.replaceAll("<a href=\"$1\" target=\"_blank\">$1</a>");
        }
        return fieldValue;
    }
}