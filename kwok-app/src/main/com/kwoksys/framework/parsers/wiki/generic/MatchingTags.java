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
package com.kwoksys.framework.parsers.wiki.generic;

import com.kwoksys.framework.parsers.wiki.Tag;
import com.kwoksys.framework.parsers.wiki.Parser;
import com.kwoksys.framework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MatchingTags
 */
public class MatchingTags extends Tag {

    public MatchingTags(String regex) {
        super(regex);
    }

    public StringBuffer parseContent(StringBuffer content, Parser parser) {
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile(getRegex(), getPattern()).matcher(content);
        
        while (m.find()) {
        	setHasMatches(true);
        	
            String replace = m.group(1);
            m.appendReplacement(sb, getOpenTag() + StringUtils.encodeMatcherReplacement(replace) + getCloseTag());
        }
        m.appendTail(sb);
        return sb;
    }
}
