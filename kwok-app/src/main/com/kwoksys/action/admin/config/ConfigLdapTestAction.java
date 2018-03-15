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
package com.kwoksys.action.admin.config;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.auth.AuthService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Action class for LDAP connection test.
 */
public class ConfigLdapTestAction extends Action2 {

    public String test() throws Exception {
        ConfigForm actionForm = getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setLdapUrlScheme(ConfigManager.auth.getLdapUrlScheme());
            actionForm.setLdapUrl(ConfigManager.auth.getAuthLdapUrl());
            actionForm.setLdapUsername("");
            actionForm.setLdapSecurityPrincipal("");
        }

        List<LabelValueBean> ldapUrlSchemeOptions = new ArrayList<>();
        for (String option : ConfigManager.auth.getLdapUrlSchemeOptions()) {
            ldapUrlSchemeOptions.add(new LabelValueBean(option, option));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_LDAP_TEST_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_AUTH_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_LDAP_TEST_2_CMD);
        standardTemplate.setAttribute("ldapUrlSchemeOptions", ldapUrlSchemeOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.config.auth.ldapTest.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String test2() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());

        AuthService authService = ServiceProvider.getAuthService(requestContext);

        ActionMessages errors = authService.testLdapConnection(actionForm.getLdapUrlScheme(),
                actionForm.getLdapUrl(), actionForm.getLdapUsername(), actionForm.getLdapPassword(),
                actionForm.getLdapSecurityPrincipal());

        if (errors.isEmpty()) {
            errors.add("connectPassed", new ActionMessage("admin.config.auth.ldapTest.passed"));
        }

        saveActionErrors(errors);
        return ajaxUpdateView(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_LDAP_TEST_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
    }
}