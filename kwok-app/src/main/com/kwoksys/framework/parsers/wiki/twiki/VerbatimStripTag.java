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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kwoksys.framework.parsers.wiki.Parser;
import com.kwoksys.framework.parsers.wiki.Tag;
import com.kwoksys.framework.parsers.wiki.TwikiParser;

/**
 * VerbatimStripTag
 */
public class VerbatimStripTag extends Tag {

    public VerbatimStripTag(String regex) {
        super(regex);
    }

    /**
     * HTML encode texts inside verbatim tags <verbatim>*</verbatim>.
     * @param content
     * @param parser
     * @return
     */
    public StringBuffer parseContent(StringBuffer content, Parser parser) {
        List<String> verbatimList = ((TwikiParser)parser).getVerbaimList();

        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile(getRegex(), Pattern.MULTILINE).matcher(content);
        while (m.find()) {
            verbatimList.add(m.group(1));
            m.appendReplacement(sb, "<verbatim>" + verbatimList.size() + "</verbatim>");
        }
        m.appendTail(sb);

        // Encode HTML
        sb = Parser.encodeHtml(sb);
        return sb;
    }
}