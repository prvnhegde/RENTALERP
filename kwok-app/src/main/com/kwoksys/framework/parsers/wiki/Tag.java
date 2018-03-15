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

import java.util.regex.Pattern;

import com.kwoksys.framework.http.RequestContext;

/**
 * AbstractTag
 */
public abstract class Tag {

	private RequestContext requestContext;
    private StringBuffer content;
    private String regex;

    private String openTag;
    private String closeTag;
    private String tag;
    private String example;

    private int pattern = Pattern.MULTILINE;
    
    // Indicates whether there is at least one match.
    private boolean hasMatches;
    
    public Tag() {}

    public Tag(String regex) {
        this.regex = regex;
    }
    
    public Tag(RequestContext requestContext, String regex) {
        this(regex);
    	this.requestContext = requestContext;
    }

    public abstract StringBuffer parseContent(StringBuffer content, Parser parser);

    public void setTags(String openTag, String closeTag) {
        this.openTag = openTag;
        this.closeTag = closeTag;
    }

    public StringBuffer getContent() {
        return content;
    }

    public void setContent(StringBuffer content) {
        this.content = content;
    }

    public String getRegex() {
        return regex;
    }

    public String getOpenTag() {
        return openTag;
    }

    public void setOpenTag(String openTag) {
        this.openTag = openTag;
    }

    public String getCloseTag() {
        return closeTag;
    }

    public void setCloseTag(String closeTag) {
        this.closeTag = closeTag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

	public int getPattern() {
		return pattern;
	}

	public void setPattern(int pattern) {
		this.pattern = pattern;
	}

	public boolean hasMatches() {
		return hasMatches;
	}

	public void setHasMatches(boolean hasMatches) {
		this.hasMatches = hasMatches;
	}

	public RequestContext getRequestContext() {
		return requestContext;
	}

	public void setRequestContext(RequestContext requestContext) {
		this.requestContext = requestContext;
	}	
}
