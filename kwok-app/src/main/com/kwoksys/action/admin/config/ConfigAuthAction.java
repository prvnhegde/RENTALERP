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

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.ConfigKeys;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.NumberUtils;

/**
 * Action class for security settings.
 */
public class ConfigAuthAction extends Action2 {

    public String index() throws Exception {
        AccessUser user = requestContext.getUser();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_AUTH_CMD);

        request.setAttribute("authType", Localizer.getText(requestContext,
                "admin.config.auth.type." + ConfigManager.auth.getAuthType()));

        request.setAttribute("authenticationMethod", Localizer.getText(requestContext,
                "admin.config.auth.authenticationMethod." + ConfigManager.auth.getAuthMethod()));

        request.setAttribute("authLdapUrl", ConfigManager.auth.getLdapUrlScheme() + ConfigManager.auth.getAuthLdapUrl());

        request.setAttribute("authLdapSecurityPrincipal", ConfigManager.auth.getAuthLdapSecurityPrincipal());

        request.setAttribute("authDomain", ConfigManager.auth.getAuthDomain());

        request.setAttribute("authSessionTimeout", Localizer.getText(requestContext, "common.calendar.time.hours_x",
                new Object[]{ConfigManager.auth.getSessionTimeoutHours()}));

        request.setAttribute("allowBlankUserPassword", Localizer.getText(requestContext,
                "common.boolean.yes_no." + ConfigManager.admin.isAllowBlankUserPassword()));

        request.setAttribute("minimumPasswordLength", ConfigManager.auth.getSecurityMinPasswordLength());

        request.setAttribute("passwordComplexity", Localizer.getText(requestContext,
                "common.boolean.true_false." + ConfigManager.admin.isSecurityPasswordComplexityEnabled()));

        request.setAttribute("accountLockoutThreshold", ConfigManager.admin.getAccountLockoutThreshold());

        request.setAttribute("accountLockoutDuration", ConfigManager.admin.getAccountLockoutDurationMinutes());

        request.setAttribute("accountLockoutDescription", ConfigManager.admin.isValidateAcctLockout() ?
                Localizer.getText(requestContext, "admin.config.security.accountLockoutOn.description",
                        new Object[]{ConfigManager.admin.getAccountLockoutDurationMinutes(),
                        ConfigManager.admin.getAccountLockoutThreshold()}) :
                Localizer.getText(requestContext, "admin.config.security.accountLockoutOff.description"));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.auth");
        
        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_AUTH_EDIT_CMD);
            link.setTitleKey("common.command.Edit");
            header.addHeaderCmds(link);
        }
        if (user.hasPermission(AppPaths.ADMIN_CONFIG)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_LDAP_TEST_CMD);
            link.setTitleKey("admin.cmd.ldapTest");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.config.auth"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit() throws Exception {
        ConfigForm actionForm = getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setAuthType(ConfigManager.auth.getAuthType());
            actionForm.setAuthMethod(ConfigManager.auth.getAuthMethod());
            actionForm.setAuthTimeout(ConfigManager.auth.getSessionTimeoutSeconds());
            actionForm.setLdapSecurityPrincipal(ConfigManager.auth.getAuthLdapSecurityPrincipal());
            actionForm.setLdapUrlScheme(ConfigManager.auth.getLdapUrlScheme());
            actionForm.setLdapUrl(ConfigManager.auth.getAuthLdapUrl());
            actionForm.setDomain(ConfigManager.auth.getAuthDomain());
            actionForm.setMinimumPasswordLength(ConfigManager.auth.getSecurityMinPasswordLength());
            actionForm.setPasswordComplexityEnabled(ConfigManager.admin.isSecurityPasswordComplexityEnabled());
            actionForm.setAllowBlankUserPassword(ConfigManager.admin.isAllowBlankUserPassword()? 1 : 0);
            actionForm.setAccountLockoutThreshold(String.valueOf(ConfigManager.admin.getAccountLockoutThreshold()));
            actionForm.setAccountLockoutDurationMinutes(String.valueOf(ConfigManager.admin.getAccountLockoutDurationMinutes()));
        }

        List<LabelValueBean> authTypeOptions = new ArrayList<>();
        for (String option : ConfigManager.auth.getAuthTypeOptions()) {
            authTypeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.auth.type." + option), option));
        }

        List<LabelValueBean> authMethodOptions = new ArrayList<>();
        for (String option : ConfigManager.auth.getAuthMethodOptions()) {
            authMethodOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.auth.authenticationMethod." + option), option));
        }

        List<LabelValueBean> sessionTimeoutOptions = new ArrayList<>();
        for (String option : ConfigManager.auth.getSessionTimeoutSecondsOptions()) {
            sessionTimeoutOptions.add(new LabelValueBean(String.valueOf(Integer.parseInt(option) / 3600), option));
        }

        List<LabelValueBean> ldapUrlSchemeOptions = new ArrayList<>();
        for (String option : ConfigManager.auth.getLdapUrlSchemeOptions()) {
            ldapUrlSchemeOptions.add(new LabelValueBean(option, option));
        }

        List<LabelValueBean> passwordLenOptions = new ArrayList<>();
        for (int i = 1; i <= 14; i++) {
            passwordLenOptions.add(new LabelValueBean(String.valueOf(i), String.valueOf(i)));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_AUTH_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd="
                + AdminUtils.ADMIN_AUTH_CMD).getString());
        request.setAttribute("cmd", AdminUtils.ADMIN_AUTH_EDIT_2_CMD);
        request.setAttribute("authTypeOptions", authTypeOptions);
        request.setAttribute("authMethodOptions", authMethodOptions);
        request.setAttribute("sessionTimeoutOptions", sessionTimeoutOptions);
        request.setAttribute("allowBlankUserPasswordOptions", WidgetUtils.getYesNoOptions(requestContext));
        request.setAttribute("passwordLenOptions", passwordLenOptions);
        request.setAttribute("passwordComplexityOptions", WidgetUtils.getBooleanOptions(requestContext));
        request.setAttribute("ldapUrlSchemeOptions", ldapUrlSchemeOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.auth.edit");
        header.setSectionKey("admin.config.sectionHeader");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());
        String authType = actionForm.getAuthType();
        String authMethod = actionForm.getAuthMethod();
        String ldapUrl = actionForm.getLdapUrl();

        ActionMessages errors = new ActionMessages();

        if (authMethod.equals(Access.AUTH_LDAP) && ldapUrl.isEmpty()) {
            errors.add("emptyLdapUrl", new ActionMessage("admin.config.error.emptyLdapUrl"));
        }

        if (!NumberUtils.isInteger(actionForm.getAccountLockoutThreshold())) {
            errors.add("invalidAccountLockoutThreshold", new ActionMessage("common.form.fieldNumberInvalid",
                    Localizer.getText(requestContext, "admin.config.security.accountLockoutThreshold")));
        }
     
        if (!NumberUtils.isInteger(actionForm.getAccountLockoutDurationMinutes())) {
            errors.add("invalidAccountLockoutDurationMinutes", new ActionMessage("common.form.fieldNumberInvalid",
                    Localizer.getText(requestContext, "admin.config.security.accountLockoutDuration")));
        }
        
        if (errors.isEmpty()) {
            List<SystemConfig> list = new ArrayList<>();
            list.add(new SystemConfig(ConfigKeys.AUTH_TYPE, authType));
            list.add(new SystemConfig(ConfigKeys.AUTH_METHOD, authMethod));
            list.add(new SystemConfig(ConfigKeys.AUTH_LDAP_URL_SCHEME, actionForm.getLdapUrlScheme()));
            list.add(new SystemConfig(ConfigKeys.AUTH_LDAP_URL, ldapUrl));
            list.add(new SystemConfig(ConfigKeys.AUTH_LDAP_SECURITY_PRINCIPAL, actionForm.getLdapSecurityPrincipal()));
            list.add(new SystemConfig(ConfigKeys.AUTH_DOMAIN, actionForm.getDomain()));
            list.add(new SystemConfig(ConfigKeys.AUTH_TIMEOUT, String.valueOf(actionForm.getAuthTimeout())));
            list.add(new SystemConfig(ConfigKeys.SECURITY_ALLOW_BLANK_USER_PASSWORD, String.valueOf(actionForm.getAllowBlankUserPassword())));
            list.add(new SystemConfig(ConfigKeys.SECURITY_MIN_PASSWORD_LENGTH, String.valueOf(actionForm.getMinimumPasswordLength())));
            list.add(new SystemConfig(ConfigKeys.SECURITY_USER_PASSWORD_COMPLEX_ENABLED, actionForm.isPasswordComplexityEnabled()));
            list.add(new SystemConfig(ConfigKeys.SECURITY_ACCOUNT_LOCKOUT_THRESHOLD, actionForm.getAccountLockoutThreshold()));
            list.add(new SystemConfig(ConfigKeys.SECURITY_ACCOUNT_LOCKOUT_DURATION_MINUTES, actionForm.getAccountLockoutDurationMinutes()));

            // Get the service
            AdminService adminService = ServiceProvider.getAdminService(requestContext);
            errors = adminService.updateConfig(list);
        }

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_AUTH_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_AUTH_CMD);
        }
    }    
}
