/*
 * Copyright 2017 Kwoksys
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

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;

import com.kwoksys.framework.util.StringUtils;

/**
 * Define a variable.
 */
public class DefineTag extends TagSupport {

    protected String id;
    protected String name;
    protected String property;
    protected String type;

    /**
     * Process the start tag.
     */
    public int doStartTag() throws JspException {
        // Gets from pageContext first. Variable can sometimes be set within the JSP page itself.
        Object value = pageContext.getAttribute(name);
        
        if (value == null) {
            // If value is not specified, get it from request attributes.
            value = ServletActionContext.getRequest().getAttribute(name);
        }
        
        if (property != null) {
            if (value instanceof Map) {
                value = ((Map) value).get(property);
            
            } else if (value instanceof Map.Entry) {
                if (property.equals("key")) {
                    value = ((Map.Entry)value).getKey();
                    
                } else if (property.equals("value")) {
                    value = ((Map.Entry)value).getValue();
                    
                } else {
                    throw new JspException("Property not supported on Map.Entry");
                }
            } else {
                Method m = null;
                try {
                    m = value.getClass().getMethod("get" + StringUtils.capitalizeFirstLetter(property), (Class[]) null);
                    value = m.invoke(value);
                    
                } catch (Exception e) {
                    throw new JspException(e);
                }
            }
        }

        pageContext.setAttribute(id, value);

        // Continue processing this page
        return (SKIP_BODY);
    }

    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();
        id = null;
        name = null;
        type = null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
