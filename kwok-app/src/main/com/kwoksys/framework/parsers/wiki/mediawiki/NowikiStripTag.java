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
package com.kwoksys.framework.parsers.wiki.mediawiki;

import com.kwoksys.framework.parsers.wiki.Tag;
import com.kwoksys.framework.parsers.wiki.Parser;
import com.kwoksys.framework.parsers.wiki.MediawikiParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

/**
 * NowikiStripTag
 */
public class NowikiStripTag extends Tag {

    private int restoreGroup;

    public NowikiStripTag(String regex) {
        super(regex);
    }

    /**
     * Strips out <nowiki>*</nowiki> and replace it with <nowiki>1</nowiki>.
     * @param content
     * @param parser
     * @return
     */
    public StringBuffer parseContent(StringBuffer content, Parser parser) {
        Map<Integer, String> nowikiMap = ((MediawikiParser)parser).getNowikiMap();

        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile(getRegex(), Pattern.MULTILINE).matcher(content);
        while (m.find()) {
            int index = nowikiMap.size();
            nowikiMap.put(index, m.group(restoreGroup));

            String replace = getOpenTag() + index + getCloseTag();
            m.appendReplacement(sb, replace);
        }
        m.appendTail(sb);
        return sb;
    }

    public void setRestoreGroup(int restoreGroup) {
        this.restoreGroup = restoreGroup;
    }
}