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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * ActionErrorsTemplate
 */
public class ActionErrorsTemplate extends BaseTemplate {

    private boolean showRequiredFieldMsg;

    private String message;

    public ActionErrorsTemplate() {
        super(ActionErrorsTemplate.class);
    }

    public void init() {}

    public void applyTemplate() throws Exception {
        ActionMessages errors = (ActionMessages) requestContext.getSession().getAttribute(RequestContext.ERROR_MESSAGES_KEY);

        if (errors != null) {
            // Remove errors from session and copy it over to request.
            requestContext.getSession().removeAttribute(RequestContext.ERROR_MESSAGES_KEY);
            
            if (requestContext.hasErrors()) {
                Set<String> set = new LinkedHashSet<>();

                for (Iterator<String> propIter = errors.properties(); propIter.hasNext();) {
                    for (Iterator<ActionMessage> errorIter = errors.get(propIter.next()); errorIter.hasNext();) {
                        ActionMessage actionMessage = errorIter.next();
                        set.add(HtmlUtils.encode(Localizer.getText(requestContext, actionMessage.getKey(), actionMessage.getValues())));
                    }
                }
                requestContext.getRequest().setAttribute(RequestContext.ERROR_MESSAGES_KEY, set);
            }
        }
    }

    @Override
    public String getJspPath() {
        return "/jsp/common/template/ActionError.jsp";
    }

    public boolean isShowRequiredFieldMsg() {
        return showRequiredFieldMsg;
    }

    public void setShowRequiredFieldMsg(boolean showRequiredFieldMsg) {
        this.showRequiredFieldMsg = showRequiredFieldMsg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessageKey(String key) {
        setMessageKey(key, null);
    }

    public void setMessageKey(String key, String[] params) {
        this.message = Localizer.getText(requestContext, key, params);
        if (this.message == null) {
            this.message = key;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
