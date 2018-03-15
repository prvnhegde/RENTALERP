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
package com.kwoksys.framework.struts2;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessages;
import org.apache.struts2.ServletActionContext;

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.properties.Localizer;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action2
 */
public class Action2 extends ActionSupport {

    public static final String STANDARD_TEMPLATE = "standard_template";
    public static final String STANDARD_AUTOGEN_TEMPLATE = "standard_autogen_template";
    public static final String AJAX_TEMPLATE = "ajax_template";
    public static final String SUCCESS = "success";

    // HTTP errors
    public static final String AJAX_AUTHENTICATION_TEMPLATE = "ajax_authentication_required";
    public static final String AUTHENTICATION_REQUIRED_TEMPLATE = "authentication_required";
    public static final String ACCESS_DENIED = "access_denied";
    public static final String FILE_NOT_FOUND = "file_not_found";

    // Special pages
    public static final String OBJECT_NOT_FOUND = "object_not_found";
    public static final String AJAX_UPDATE_VIEW = "ajax_updateview";
    public static final String AJAX_REDIRECT = "ajax_redirect";
    public static final String NON_SUPPORTED_BROWSER = "non_supported_browser";
    
    protected HttpServletRequest request;
    protected RequestContext requestContext;
    protected HttpServletResponse response = ServletActionContext.getResponse();
    protected ResponseContext responseContext = new ResponseContext(response);

    private BaseForm actionForm;

    public Action2 setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
        this.request = requestContext.getRequest();
        return this;
    }

    public String redirect(String path) throws IOException {
        response.sendRedirect(AppPaths.ROOT + path);
        return null;
    }

    public String ajaxUpdateView(String path) throws Exception {
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);
        ajaxTemplate.setAjaxTemplatePath(AjaxTemplate.getTemplatePath(requestContext, Action2.AJAX_UPDATE_VIEW));
        
        // Applies this template. Normally, the same result can be achieved using findTemplate(), if called from
        // an Action.
        ajaxTemplate.applyTemplate();
        
        request.setAttribute("url", AppPaths.ROOT + path);

        return Action2.AJAX_TEMPLATE;
    }

    public String ajaxRedirect(String path) throws Exception {
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);
        ajaxTemplate.setAjaxTemplatePath(AjaxTemplate.getTemplatePath(requestContext, Action2.AJAX_REDIRECT));
        
        // Applies this template. Normally, the same result can be achieved using findTemplate(), if called from
        // an Action.
        ajaxTemplate.applyTemplate();
        
        request.setAttribute("url", AppPaths.ROOT + path);

        return Action2.AJAX_TEMPLATE;
    }

    public void saveActionErrors(ActionMessages errors) {
        request.getSession().setAttribute(RequestContext.ERROR_MESSAGES_KEY, errors);
        request.getSession().setAttribute(RequestContext.FORM_KEY, actionForm);
    }

    /**
     * Save notification bar message.
     * @param messageKey
     */
    public void saveNotifyMessageKey(String messageKey) {
        request.getSession().setAttribute(RequestContext.URL_PARAM_NOTIFY, Localizer.getText(requestContext, messageKey));
    }
    
    /**
     * Get BaseForm.
     * @param clazz
     * @param requestKey
     * @return
     * @throws Exception
     */
    private BaseForm getBaseForm(Class<?> clazz, String requestKey) throws Exception {
        boolean resubmit = requestContext.getParameterBoolean(RequestContext.URL_PARAM_RESUBMIT);
        boolean error = requestContext.hasErrors();
        boolean preserveSession = !requestKey.equals(RequestContext.FORM_KEY);
        BaseForm sessionActionForm = (BaseForm) request.getSession().getAttribute(requestKey);
        boolean readRequesetParams = resubmit;

        if (error || preserveSession) {
            actionForm = sessionActionForm;
        }

        if (actionForm == null) {
            actionForm = (BaseForm) clazz.newInstance();
            readRequesetParams = true;

            if (preserveSession) {
                request.getSession().setAttribute(requestKey, actionForm);
            }
        }
        if (readRequesetParams) {
            actionForm.setRequest(requestContext);
        }

        if (!preserveSession) {
            request.getSession().removeAttribute(requestKey);
        }

        // Check both error and sessionActionForm. In case of a browser refresh, sessionActionForm may be null.
        actionForm.setResubmit((error && sessionActionForm != null) || resubmit);

        request.setAttribute(requestKey, actionForm);
        return actionForm;
    }

    public <T> T getBaseForm(Class<T> clazz) throws Exception {
        return (T) getBaseForm(clazz, RequestContext.FORM_KEY);
    }

    public <T> T getSessionBaseForm(Class<T> clazz) throws Exception {
        return (T) getBaseForm(clazz, clazz.getSimpleName());
    }

    public <T> T saveActionForm(T actionForm) {
        this.actionForm = (BaseForm) actionForm;
        ((BaseForm)actionForm).setRequest(requestContext);
        return actionForm;
    }

    public void clearSessionBaseForm(Class<?> clazz) {
        request.getSession().setAttribute(clazz.getSimpleName(), null);
    }
}
