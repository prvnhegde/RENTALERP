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
package com.kwoksys.framework.parsers.wiki.twiki;

import com.kwoksys.framework.parsers.wiki.Parser;
import com.kwoksys.framework.parsers.wiki.TwikiParser;
import com.kwoksys.framework.parsers.wiki.Tag;
import com.kwoksys.framework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WikiWordTag
 */
public class WikiWordTag extends Tag {

    public WikiWordTag(String regex) {
        super(regex);
    }

    /**
     * Replaces "WebStatistics" to "<a href="${URL}/WebStatistics">WebStatistics</a>"
     * @param content
     * @return
     */
    public StringBuffer parseContent(StringBuffer content, Parser parser) {
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile(getRegex(), Pattern.MULTILINE).matcher(content);
        while (m.find()) {
            String replace;
            if (m.group(1).equals("!")) {
                replace = m.group(2);
            } else {
                replace = m.group(1) + "<a href=\"" + TwikiParser.WIKI_URL + m.group(2) + "\">" + m.group(2) + "</a>" + m.group(7);
            }
            m.appendReplacement(sb, StringUtils.encodeMatcherReplacement(replace));
        }
        m.appendTail(sb);
        return sb;
    }
}