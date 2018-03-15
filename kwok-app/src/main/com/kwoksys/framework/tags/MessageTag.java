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

import org.apache.struts2.ServletActionContext;

import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;

/**
 * Custom tag that retrieves a localized message.
 */
public class MessageTag extends TagSupport {

    /**
     * The key of the message to be retrieved.
     */
    protected String key;

    /**
     * Localizer parameters
     */
    protected String arg0;
    protected String arg1;
    protected String arg2;
    protected String arg3;
    protected String arg4;

    /**
     * Process the start tag.
     */
    public int doStartTag() throws JspException {
        // Retrieve the message from localization properties file.
        String message = Localizer.getText(RequestContext.getLocale(ServletActionContext.getRequest()), key, 
                new Object[] {arg0, arg1, arg2, arg3, arg4});

        if (message == null) {
            throw new JspException("Message for key " + key + " was not found.");
        }

        try {
            pageContext.getOut().write(message);
        } catch (Exception e) {
            throw new JspException(e);
        }

        return (SKIP_BODY);
    }

    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
        key = null;
        arg0 = null;
        arg1 = null;
        arg2 = null;
        arg3 = null;
        arg4 = null;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    public void setArg4(String arg4) {
        this.arg4 = arg4;
    }
}
