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
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.properties.Localizer;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectDeleteTemplate
 */
public class ObjectDeleteTemplate extends BaseTemplate {

    private ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();

    private String formAction;

    private String formCancelAction;

    private String confirmationMsgKey;

    private String titleText;

    private String submitButtonKey;

    private String onsubmit;
    
    private Map<String, Object> formHiddenVariableMap = new HashMap<>();

    public ObjectDeleteTemplate() {
        super(ObjectDeleteTemplate.class);
    }

    public void init() {
        addTemplate(errorsTemplate);
    }

    public void applyTemplate() throws DatabaseException {
    }

    @Override
    public String getJspPath() {
        return "/jsp/common/template/ObjectDelete.jsp";
    }

    public void setFormAjaxAction(String formAction) {
        this.formAction = AppPaths.ROOT + formAction;
        onsubmit = "return App.submitFormUpdate(this, {'disable' : this.deleteBtn})";
    }
    
    public String getFormAction() {
        return formAction;
    }

    public String getFormCancelLink() {
        return Links.getCancelLink(requestContext, formCancelAction).getString();
    }

    public void setFormCancelAction(String formCancelAction) {
        this.formCancelAction = formCancelAction;
    }

    public String getConfirmationMsgKey() {
        return confirmationMsgKey;
    }

    public void setConfirmationMsgKey(String confirmationMsgKey) {
        this.confirmationMsgKey = confirmationMsgKey;
    }

    public String getSubmitButtonKey() {
        return submitButtonKey;
    }

    public void setSubmitButtonKey(String submitButtonKey) {
        this.submitButtonKey = submitButtonKey;
    }

    public void setTitleKey(String titleKey) {
        titleText = Localizer.getText(requestContext, titleKey);
    }

    public String getTitleText() {
        return titleText;
    }

    public Map<String, Object> getFormHiddenVariableMap() {
        return formHiddenVariableMap;
    }

    public ActionErrorsTemplate getErrorsTemplate() {
        return errorsTemplate;
    }

    public String getOnsubmit() {
        return onsubmit;
    }
}
