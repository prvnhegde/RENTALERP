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
package com.kwoksys.biz.base;

import com.kwoksys.framework.http.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * TemplateBase.
 */
public abstract class BaseTemplate {

    private List<BaseTemplate> templates = new ArrayList<>();

    protected String name;
    
    protected String prefix;

    protected HttpServletRequest request;

    protected RequestContext requestContext;

    public BaseTemplate(Class clazz) {
        this(clazz, null);
    }

    public BaseTemplate(Class clazz, String prefix) {
        this.name = clazz.getSimpleName();
        this.prefix = prefix;
    }

    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
        request = requestContext.getRequest();
    }

    public <T>T addTemplate(T template) {
        templates.add((BaseTemplate)template);
        ((BaseTemplate)template).setRequestContext(requestContext);
        ((BaseTemplate)template).init();
        return template;
    }

    /**
     * Initialize a template, e.g. setting RequestContext, adding sub-templates, etc.
     */
    public abstract void init();

    /**
     * Set and pass required attributes to JSP pages.
     * @throws Exception
     */
    public abstract void applyTemplate() throws Exception;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BaseTemplate> getTemplates() {
        return templates;
    }

    public String getJspPath() {
        return null;
    }
}