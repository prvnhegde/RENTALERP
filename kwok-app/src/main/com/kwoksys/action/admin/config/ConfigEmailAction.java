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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * ConfigEmailAction
 */
public class ConfigEmailAction extends Action2 {

    public String index() throws Exception {
        AccessUser user = requestContext.getUser();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_EMAIL_CMD);

        standardTemplate.setAttribute("issueNotificationFromUi", Localizer.getText(requestContext, "common.boolean.on_off." +
                ConfigManager.email.isIssueNotificationFromUiEnabled()));
        
        standardTemplate.setAttribute("issueNotificationFromEmail", Localizer.getText(requestContext, "common.boolean.on_off." +
                ConfigManager.email.isIssueNotificationFromEmailEnabled()));

        standardTemplate.setAttribute("contractExpireNotification", Localizer.getText(requestContext, "common.boolean.on_off." +
                ConfigManager.email.isContractExpireNotificationEnabled()));
        
        standardTemplate.setAttribute("emailAllowedDomains", ConfigManager.email.getAllowedDomains());
        standardTemplate.setAttribute("smtpHost", ConfigManager.email.getSmtpHost());
        standardTemplate.setAttribute("smtpPort", ConfigManager.email.getSmtpPort());
        standardTemplate.setAttribute("smtpUsername", ConfigManager.email.getSmtpUsername());
        standardTemplate.setAttribute("smtpPassword", ConfigManager.email.getSmtpPassword());
        standardTemplate.setAttribute("emailFrom", ConfigManager.email.getSmtpFrom());
        standardTemplate.setAttribute("emailTo", ConfigManager.email.getSmtpTo());
        standardTemplate.setAttribute("issueReportEmailTemplate", HtmlUtils.formatMultiLineDisplay(ConfigManager.email.getIssueReportEmailTemplate()));
        standardTemplate.setAttribute("issueAddEmailTemplate", HtmlUtils.formatMultiLineDisplay(ConfigManager.email.getIssueAddEmailTemplate()));
        standardTemplate.setAttribute("issueUpdateEmailTemplate", HtmlUtils.formatMultiLineDisplay(ConfigManager.email.getIssueUpdateEmailTemplate()));
        standardTemplate.setAttribute("contractExpireNotifyEmailTemplate", HtmlUtils.formatMultiLineDisplay(ConfigManager.email.getContractExpireNotificationEmailTemplate()));
        standardTemplate.setAttribute("contractExpireNotificationDesc", Localizer.getText(requestContext, 
                "admin.config.email.notification.contractExpirationDesc", new String[]{String.valueOf(ConfigManager.email.getContractNotificationBatchSize()), 
                        String.valueOf(ConfigManager.email.getContractNotificationInterval()/DatetimeUtils.ONE_HOUR_MILLISECONDS)}));
        standardTemplate.setAttribute("starttls", Localizer.getText(requestContext, "common.boolean.on_off."+ ConfigManager.email.getSmtpStartTls()));
        standardTemplate.setAttribute("popHost", ConfigManager.email.getPopHost());
        standardTemplate.setAttribute("popPort", ConfigManager.email.getPopPort());
        standardTemplate.setAttribute("popUsername", ConfigManager.email.getPopUsername());
        standardTemplate.setAttribute("popPassword", ConfigManager.email.getPopPassword());
        standardTemplate.setAttribute("popUseSSL", ConfigManager.email.isPopSslEnabled());
        standardTemplate.setAttribute("popIgnoreSender", HtmlUtils.formatMultiLineDisplay(ConfigManager.email.getPopSenderIgnoreList()));
        standardTemplate.setAttribute("popRetrievalFrequency", Localizer.getText(requestContext, "common.calendar.time.minutes_x", new String[] {String.valueOf(ConfigManager.email.getPopRepeatInterval() / DatetimeUtils.ONE_MINUTE_MILLISECONDS)}));
        standardTemplate.setAttribute("popRetrievalBatchSize", ConfigManager.email.getPopMessagesLimit());
        standardTemplate.setAttribute("canEditSmtpSettings", user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE));
        standardTemplate.setAttribute("editSmtpSettingsLink", new Link(requestContext).setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE
                            + "?cmd=" + AdminUtils.ADMIN_EMAIL_SMTP_EDIT_CMD).setTitleKey("admin.cmd.configEmailEdit"));
                
        standardTemplate.setAttribute("canEditPopSettings", user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE));
        standardTemplate.setAttribute("editPopSettingsLink", new Link(requestContext).setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE
                    + "?cmd=" + AdminUtils.ADMIN_EMAIL_POP_EDIT_CMD).setTitleKey("admin.cmd.configEmailEdit"));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.email.title");
        header.setTitleClassNoLine();
        
        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.config.email.title"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
