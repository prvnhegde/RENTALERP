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
package com.kwoksys.framework.parsers.email;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class IssueEmailParser {

    public static final String EMAIL_BODY_SEPARATOR = "----------------------------------------";

    /**
     * Issue email address can be in the form of "Firstname Lastname <email address>" or just "<email address>".
     * Either case, we only want "email address"
     * @param string
     * @return
     */
    public static String parseEmailAddress(String content) {
        String regex = ".*?<(.*?)>";

        Matcher m = Pattern.compile(regex).matcher(content.trim());

        if (m.matches()) {
            try {
                content = m.group(1);
            } catch (Exception e) {/* ignored */}
        }
        return content.trim();
    }

    /**
     * Extracts issue id from an email title. Returns null if there is no match.
     * An issue email title looks like this: [Issue 1], and we match "*[*:<issue id>]*"
     * @param content
     * @return
     */
    public static Integer parseEmailIssueId(String content) {
        Integer issueId = null;

        if (content == null) {
            return issueId;
        }

        String regex = ".*?\\[.*? (.*?)\\].*?";

        Matcher m  = Pattern.compile(regex).matcher(content);

        if (m.matches()) {
            try {
                issueId = Integer.parseInt(m.group(1));
            } catch (Exception e) {/* ignored */}
        }
        return issueId;
    }

    public static String parseEmailBody(String content) {
        if (content.contains(EMAIL_BODY_SEPARATOR)) {
            return content.substring(0, content.indexOf(EMAIL_BODY_SEPARATOR));
        } else {
            return content;
        }
    }
}
