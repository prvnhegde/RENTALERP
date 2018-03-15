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
package com.kwoksys.framework.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * Output texts to HTML.
 */
public class WriteTag extends TagSupport {

    protected String value;

    /**
     * Encodes javascript
     */
    protected boolean encodeJavascript = false;

    /**
     * Process the start tag.
     */
    public int doStartTag() throws JspException {
        if (encodeJavascript) {
            value = StringUtils.encodeJavascript(value); 
        } else {
            // If there's no other specific encoding specified, will do HTML encoding.
            value = HtmlUtils.encode(value);
        }
        
        try {
            pageContext.getOut().write(value);
        } catch (Exception e) {
            throw new JspException(e);
        }

        // Continue processing this page
        return (SKIP_BODY);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setEncodeJavascript(boolean encodeJavascript) {
        this.encodeJavascript = encodeJavascript;
    }

    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();
        value = null;
        encodeJavascript = false;
    }
}
