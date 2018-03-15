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
package com.kwoksys.action.common.template;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * RootTemplate
 */
public abstract class RootTemplate extends BaseTemplate {

    private static final Logger LOGGER = Logger.getLogger(RootTemplate.class.getName());

    private List<String> jspTemplates = new ArrayList<>();

    protected String ajaxTemplatePath;
    
    protected String templatePath;

    protected String templateName = Action2.SUCCESS;

    private boolean ajax;

    /**
     * To support same template used multiple times.
     */
    private String prefix;

    public RootTemplate(Class clazz, RequestContext requestContext) {
        super(clazz);
        setRequestContext(requestContext);
        ajax = requestContext.isAjax();
    }

    public String findTemplate(String templateName) throws Exception {
        applyTemplate();

        // Special case for ajax templates. 
        if (ajax) {
            if (templateName.equals(Action2.STANDARD_TEMPLATE)
                    || templateName.equals(Action2.STANDARD_AUTOGEN_TEMPLATE)) {
                ajaxTemplatePath = getTemplatePath(requestContext, templateName);
                
            } else {
                ajaxTemplatePath = templatePath;
            }
            templateName = Action2.AJAX_TEMPLATE;
        }
        
        LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.TEMPLATE_PREFIX), LogConfigManager.TEMPLATE_PREFIX + " Forwarding to JSP");
        return templateName;
    }

    /**
     * Apply templates.
     * @param templates
     * @param applySubTemplates: Control whether to apply templates within templates to avoid duplication.
     * @throws Exception
     */
    private void applyTemplates(List<BaseTemplate> templates, boolean applySubTemplates) throws Exception {
        for (BaseTemplate template : templates) {
            template.applyTemplate();

            request.setAttribute(template.getName()
                    + (template.getPrefix() == null ? "" : template.getPrefix()), template);

            if (applySubTemplates) {
                if (template.getJspPath() != null) {
                    String jspPath = template.getJspPath();
                    LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.TEMPLATE_PREFIX), LogConfigManager.TEMPLATE_PREFIX + " Include: " + jspPath);
                    jspTemplates.add(jspPath);
                } else {
                    // Not using warning level here, because it cannot be turned off in System Settings > Logging page.
                    LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.TEMPLATE_PREFIX), LogConfigManager.TEMPLATE_PREFIX + " Missing template jsp: " + template);
                }
            }
                
            applyTemplates(template.getTemplates(), false);
        }
    }

    public void init() {}

    /**
     * Applies this template
     * @throws Exception
     */
    public void applyTemplate() throws Exception {
        applyTemplates(getTemplates(), true);

        request.setAttribute(name, this);
        request.setAttribute("image", Image.getInstance());
        request.setAttribute("path", AppPaths.getInstance());
        request.setAttribute(RootTemplate.class.getSimpleName(), this);

        // Get pageStartTime from servlet
        Long pageStartTime = (Long) request.getAttribute(RequestContext.PAGE_START_TIME);

        if (pageStartTime != null && requestContext.getUser().isLoggedOn()) {
            request.setAttribute(RequestContext.PAGE_EXEC_TIME, Localizer.getText(requestContext, "common.pageExecutionTime",
                new Object[]{new DecimalFormat("#0.000").format((double) (System.currentTimeMillis() - pageStartTime) / 1000)}));
        }

        templatePath = getTemplatePath(requestContext, templateName);
    }

    public static String getTemplatePath(RequestContext requestContext, String templateName) {
        String templatePath = null;
        ActionConfig actionConfig = requestContext.getActionConfig();
        if (actionConfig != null) {
            Map<String, ResultConfig> resultsMap = actionConfig.getResults();

            ResultConfig resultConfig = resultsMap.get(templateName);

            if (resultConfig != null) {
                templatePath = resultConfig.getParams().get("location");
            }
        }
        return templatePath;
    }

    public List<String> getJspTemplates() {
        return jspTemplates;
    }

    public void setAttribute(String key, Object value) {
        request.setAttribute(key, value);
    }

    public void setPathAttribute(String key, String value) {
        request.setAttribute(key, AppPaths.ROOT + value);
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isAjax() {
        return ajax;
    }

    public String getAjaxTemplatePath() {
        return ajaxTemplatePath;
    }

    public void setAjaxTemplatePath(String ajaxTemplatePath) {
        this.ajaxTemplatePath = ajaxTemplatePath;
    }
}