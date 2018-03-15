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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigKeys;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.mail.EmailMessage;
import com.kwoksys.framework.connections.mail.SmtpConnection;
import com.kwoksys.framework.connections.mail.SmtpService;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.framework.validations.ColumnField;
import com.kwoksys.framework.validations.InputValidator;

public class ConfigEmailSmtpAction extends Action2 {
    
    public String edit() throws Exception {
        ConfigForm actionForm = getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setIssueNotificationFromUiOn(ConfigManager.email.isIssueNotificationFromUiEnabled());
            actionForm.setIssueNotificationFromEmailOn(ConfigManager.email.isIssueNotificationFromEmailEnabled());
            actionForm.setContractExpireNotificationOn(ConfigManager.email.isContractExpireNotificationEnabled());
            actionForm.setAllowedDomains(ConfigManager.email.getAllowedDomains());
            actionForm.setSmtpHost(ConfigManager.email.getSmtpHost());
            actionForm.setSmtpPort(ConfigManager.email.getSmtpPort());
            actionForm.setSmtpUsername(ConfigManager.email.getSmtpUsername());
            actionForm.setSmtpFrom(ConfigManager.email.getSmtpFrom());
            actionForm.setSmtpTo(ConfigManager.email.getSmtpTo());
            actionForm.setSmtpPassword("");
            actionForm.setSmtpStarttls(ConfigManager.email.getSmtpStartTls());
            actionForm.setReportIssueEmailTemplate(ConfigManager.email.getIssueReportEmailTemplate());
            actionForm.setIssueAddEmailTemplate(ConfigManager.email.getIssueAddEmailTemplate());
            actionForm.setIssueUpdateEmailTemplate(ConfigManager.email.getIssueUpdateEmailTemplate());
            actionForm.setContractExpireNotifyEmailTemplate(ConfigManager.email.getContractExpireNotificationEmailTemplate());
        } 

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_EMAIL_SMTP_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_EMAIL_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_EMAIL_SMTP_EDIT_2_CMD);
        standardTemplate.setAttribute("onOffOptions", WidgetUtils.getOnOffOptions(requestContext));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.configHeader.notification_config_edit");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());
        String password = actionForm.getSmtpPassword();

        ActionMessages errors;

        if (actionForm.isTest()) {
            errors = new ActionMessages();
            InputValidator validator = new InputValidator(requestContext, errors);

            validator.validate(new ColumnField().setName("toField").setTitleKey("admin.config.email.smtp.to")
                    .setNullable(false).calculateLength(actionForm.getSmtpTo()));

            if (errors.isEmpty()) { 
            EmailMessage message = new EmailMessage();

            // Set FROM field
            message.setFromField(actionForm.getSmtpFrom());

            // Set TO field
            message.getToField().add(actionForm.getSmtpTo());

            // Set SUBJECT field
            message.setSubjectField(Localizer.getText(requestContext, "admin.config.email.test.subject"));

            // Set BODY field
            message.setBodyField(Localizer.getText(requestContext, "admin.config.email.test.body"));

            SmtpConnection conn = new SmtpConnection();
            conn.setHost(actionForm.getSmtpHost());
            conn.setPort(actionForm.getSmtpPort());
            conn.setUsername(actionForm.getSmtpUsername());
            conn.setStarttls(actionForm.getSmtpStarttls());

            // Only use the password given in submit form if it's not empty.
            if (!password.isEmpty()) {
                conn.setPassword(actionForm.getSmtpPassword());
            }

            errors = new SmtpService(conn).send(message);
            if (errors.isEmpty()) {
                errors.add("sendSuccess", new ActionMessage("admin.config.email.success", actionForm.getSmtpTo()));
            }
            }
            
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_EMAIL_SMTP_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        }

        List<SystemConfig> list = new ArrayList<>();
        list.add(new SystemConfig(ConfigKeys.ISSUE_NOTIFICATION_FROM_UI_ENABLED, actionForm.isIssueNotificationFromUiOn()));
        list.add(new SystemConfig(ConfigKeys.ISSUE_NOTIFICATION_FROM_EMAIL_ENABLED, actionForm.isIssueNotificationFromEmailOn()));
        list.add(new SystemConfig(ConfigKeys.CONTRACT_EXPIRE_NOTIFY_ENABLED, actionForm.isContractExpireNotificationOn()));
        list.add(new SystemConfig(ConfigKeys.EMAIL_ALLOWED_DOMAINS, actionForm.getAllowedDomains()));
        list.add(new SystemConfig(ConfigKeys.SMTP_HOST, actionForm.getSmtpHost()));
        list.add(new SystemConfig(ConfigKeys.SMTP_PORT, actionForm.getSmtpPort()));
        list.add(new SystemConfig(ConfigKeys.SMTP_USERNAME, actionForm.getSmtpUsername()));
        list.add(new SystemConfig(ConfigKeys.SMTP_FROM, actionForm.getSmtpFrom()));
        list.add(new SystemConfig(ConfigKeys.SMTP_TO, actionForm.getSmtpTo()));
        list.add(new SystemConfig(ConfigKeys.SMTP_STARTTLS, actionForm.getSmtpStarttls()));
        list.add(new SystemConfig(ConfigKeys.ISSUE_REPORT_EMAIL_TEMPLATE, actionForm.getReportIssueEmailTemplate()));
        list.add(new SystemConfig(ConfigKeys.ISSUE_ADD_EMAIL_TEMPLATE, actionForm.getIssueAddEmailTemplate()));
        list.add(new SystemConfig(ConfigKeys.ISSUE_UPDATE_EMAIL_TEMPLATE, actionForm.getIssueUpdateEmailTemplate()));
        list.add(new SystemConfig(ConfigKeys.ISSUE_UPDATE_EMAIL_TEMPLATE, actionForm.getIssueUpdateEmailTemplate()));
        list.add(new SystemConfig(ConfigKeys.CONTRACT_EXPIRE_NOTIFY_TEMPLATE, actionForm.getContractExpireNotifyEmailTemplate()));

        if (!password.isEmpty()) {
            list.add(new SystemConfig(ConfigKeys.SMTP_PASSWORD, StringUtils.encodeBase64Codec(password)));
        }

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        errors = adminService.updateConfig(list);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_EMAIL_SMTP_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_EMAIL_CMD);
        }
    }
}
