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

import java.util.List;

import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.ui.Link;

public class HtmlUtils {

    /**
     * Creates a Link with a given URL.
     *
     * @return ..
     */
    public static String formatExternalLink(RequestContext requestContext, String url) {
        if (!StringUtils.isEmpty(url)) {
            return new Link(requestContext).setExternalPath(url).setTitle(url).setImgSrc(Image.getInstance().getExternalPopupIcon()).setImgAlignRight().getString();
        } else {
            return "";
        }
    }

    /**
     * https://www.w3schools.com/charsets/ref_html_ascii.asp
     */
    public static String encode(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
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
                    int cp = input.codePointAt(i);
                    if ((cp > 32 && cp < 48)
                            || (cp > 57 && cp < 65)
                            || (cp > 90 && cp < 97)
                            || (cp > 122 && cp < 127)) {
                        sb.append("&#").append(cp).append(";");
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    /**
     * Converts an email address into a link.
     *
     * @param emailAddress
     * @return ..
     */
    public static String formatMailtoLink(String emailAddress) {
        if (emailAddress == null || emailAddress.isEmpty()) {
            return "";
        } else {
            return new Link(null).setExternalPath("mailto:" + encode(emailAddress)).setTitle(emailAddress).getString();
        }
    }

    public static List<String> encode(List<String> values) {
        for (int i = 0; i < values.size(); i++) {
            values.set(i, encode(values.get(i)));
        }
        return values;
    }

    /**
     * Replaces line breaks with html break tag.
     *
     * @param input
     * @return ..
     */
    public static String formatMultiLineDisplay(String input) {
        return input == null ? "" : encode(input).replace("\n", "<br>");
    }

    /**
     * Removes all html contents for a given string.
     * @param input
     * @return
     */
    public static String removeHtmlTags(String input) {
        return input == null ? "" : input.replace("\n", "").replaceAll("\\<script.*?\\</script\\>", "").replaceAll("\\<.*?\\>","");
    }
}
