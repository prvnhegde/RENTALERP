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

import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.parsers.wiki.generic.MatchingTags;
import com.kwoksys.framework.parsers.wiki.generic.SingleTag;
import com.kwoksys.framework.parsers.wiki.mediawiki.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * MediawikiParser
 */
public class MediawikiParser extends Parser {

    public static List<Tag> list =  new ArrayList<>();
    
    private Map nowikiMap = new HashMap();

    private int externalLinkCounter = 1;

    private StringBuffer sb;
    
    public String parseHtml(RequestContext requestContext, String content) {
        sb = new StringBuffer(content);

        // Strip <nowiki> tags
        NowikiStripTag stripTag = new NowikiStripTag("<nowiki>(([\\S\\s\r\n])*?)</nowiki>");
        stripTag.setTags("<nowiki>", "</nowiki>");
        stripTag.setRestoreGroup(1);
        parseContent(stripTag);

        stripTag = new NowikiStripTag("<pre(.*?)>(([\\S\\s\r\n])*?)</pre>");
        stripTag.setTags("<pre$1>", "</pre>");
        stripTag.setRestoreGroup(2);
        parseContent(stripTag);

        Tag tag = new SingleTag("<!-{2,} (.+?) -{2,}>");
        tag.setExample("<!-- This is a comment -->");
        tag.setTag("");
        parseContent(tag);

        tag = new HtmlEncodeTag();
        parseContent(tag);

        // Restore allowed HTML tags
        tag = new HtmlRestoreTag("&lt;(i|b|del|s|strike|blockquote|(br(|( *?/)|/))" +
                "|((code|tt|div|font|span|table|td|tr)(.*?))" +
                "|((/)(i|b|del|s|strike|blockquote|code|tt|font|div|span|table|td|tr)))&gt;");
        parseContent(tag);

        tag = new MatchingTags("'''''(.+?)'''''");
        tag.setExample("'''''bold & italic'''''");
        tag.setTags("<i><b>", "</b></i>");
        parseContent(tag);

        tag = new MatchingTags("'''(.+?)'''");
        tag.setExample("'''bold'''");
        tag.setTags("<b>", "</b>");
        parseContent(tag);

        tag = new MatchingTags("''(.+?)''");
        tag.setExample("''italic''");
        tag.setTags("<i>", "</i>");
        parseContent(tag);

        tag = new MatchingTags("^======(.+?)======");
        tag.setExample("======level 6======");
        tag.setTags("<h6>", "</h6>");
        parseContent(tag);

        tag = new MatchingTags("^=====(.+?)=====");
        tag.setExample("=====level 5=====");
        tag.setTags("<h5>", "</h5>");
        parseContent(tag);

        tag = new MatchingTags("^====(.+?)====");
        tag.setExample("====level 4====");
        tag.setTags("<h4>", "</h4>");
        parseContent(tag);

        tag = new MatchingTags("^===(.+?)===");
        tag.setExample("===level 3===");
        tag.setTags("<h3>", "</h3>");
        parseContent(tag);

        tag = new MatchingTags("^==(.+?)==");
        tag.setExample("==level 2==");
        tag.setTags("<h2 class=\"mediawikiH2\">", "</h2>");
        parseContent(tag);

        tag = new MatchingTags("^=(.+?)=");
        tag.setExample("=level 1=");
        tag.setTags("<h1 class=\"mediawikiH1\">", "</h1>");
        parseContent(tag);

        tag = new SingleTag("^----");
        tag.setExample("----");
        tag.setTag("<hr/>");
        parseContent(tag);

        tag = new SingleTag("^$");
        tag.setTag("\n<p>");
        parseContent(tag);

        tag = new MatchingTags("\n (([\\S\\s\r\n])*?)(\n[^ ]|$)");
        tag.setPattern(Pattern.DOTALL);
        tag.setExample(" preformatted text is done with a space at the beginning of the line");
        tag.setTags("<pre> ", "\n</pre>$3");
        parseContent(tag);

        // Recursively process create <ul> tags.
        boolean hasMatches = true;
        int ulCharCount = 1;
        while (hasMatches) {
	        tag = new MatchingTags("\n(" + getCharacters("\\*", ulCharCount++) + "(.+?))(\n[^*]|$)");
	        tag.setPattern(Pattern.DOTALL);
	        tag.setExample("\n* Bullet 1\n* Bullet 2");
	        tag.setTags("\n<ul>\n", "\n</ul>$3");
	        parseContent(tag);
	        hasMatches = tag.hasMatches();
        }

        tag = new MatchingTags("^\\*+(.+)$");
        tag.setExample("* one");
        tag.setTags("<li>\n", "\n</li>");
        parseContent(tag);

        hasMatches = true;
        int olCharCount = 1;
        while (hasMatches) {
	        tag = new MatchingTags("\n(" + getCharacters("\\#", olCharCount++) + "(.+?))(\n[^#]|$)");
	        tag.setPattern(Pattern.DOTALL);
	        tag.setExample("\n# Bullet 1\n# Bullet 2");
	        tag.setTags("\n<ol>\n", "\n</ol>$3");
	        parseContent(tag);
	        hasMatches = tag.hasMatches();
        }
        
        tag = new MatchingTags("^\\#+(.+)$");
        tag.setExample("# one");
        tag.setTags("<li>\n", "\n</li>");
        parseContent(tag);

        // Not sure what characters should be allowed in namespaces, use w (A word character) and spaces for now.
        tag = new InternalLabelLinkTag(requestContext, "\\[\\[([\\w ]+?)\\|(.+?)\\]\\]");
        tag.setExample("[[Main Page|different text]]");
        parseContent(tag);

        // Not sure what characters should be allowed in namespaces, use w (A word character) and spaces for now.
        tag = new InternalLinkTag(requestContext, "\\[\\[([\\w ]+?)\\]\\]");
        tag.setExample("[[Main Page]]");
        parseContent(tag);

        tag = new ExternalLinkTag("(^|.)((https|http|ftp)://.+?)($|[^\\w\\.:/\\?=&#;])");
        tag.setExample("http://mediawiki.org");
        parseContent(tag);

        tag = new ExternalLabelLinkTag("\\[((https|http|ftp)://.+?) (.+?)\\]");
        tag.setExample("[http://mediawiki.org MediaWiki]");
        parseContent(tag);

        tag = new ExternalNumberLinkTag("\\[((https|http|ftp)://.+?)\\]");
        tag.setExample("[http://mediawiki.org]");
        parseContent(tag);

        // Put <nowiki> tags back
        NowikiRestoreTag restoreTag = new NowikiRestoreTag("&lt;nowiki&gt;([0-9]+)&lt;/nowiki&gt;");
        restoreTag.setRestoreGroup(1);
        restoreTag.setTags("", "");
        parseContent(restoreTag);

        restoreTag = new NowikiRestoreTag("&lt;pre(.*?)&gt;([0-9]+)&lt;/pre&gt;");
        restoreTag.setRestoreGroup(2);
        restoreTag.setTags("<pre$1>", "</pre>");
        parseContent(restoreTag);

        return sb.toString();
    }

    public void parseContent(Tag tag) {
        sb = tag.parseContent(sb, this);
    }

    /**
     * Returns protocol prefix, e.g. http, https, etc.
     * @param prefix
     * @return
     */
    public static String getLinkCssClass(String prefix) {
        if ("https".equals(prefix) || "http".equals(prefix) || "ftp".equals(prefix)) {
            return "link_" + prefix;
        }
        return "";
    }

    public int incrExternalLinkLevel() {
        return externalLinkCounter++;
    }

    public Map getNowikiMap() {
        return nowikiMap;
    }
    
    private String getCharacters(String character, int numberOfTimes) {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < numberOfTimes; i++) {
    		sb.append(character);
    	}
    	return sb.toString();
    }
}