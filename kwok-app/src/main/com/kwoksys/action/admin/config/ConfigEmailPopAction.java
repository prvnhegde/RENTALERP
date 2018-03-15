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
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.ConfigKeys;
import com.kwoksys.framework.connections.mail.PopConnection;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * Action class editing POP configuratons.
 */
public class ConfigEmailPopAction extends Action2 {

    public String edit() throws Exception {
        ConfigForm actionForm = getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setPopHost(ConfigManager.email.getPopHost());
            actionForm.setPopPort(ConfigManager.email.getPopPort());
            actionForm.setPopUsername(ConfigManager.email.getPopUsername());
            actionForm.setPopIgnoreSender(ConfigManager.email.getPopSenderIgnoreList());
            actionForm.setPopUseSSL(ConfigManager.email.isPopSslEnabled());
            actionForm.setPopPassword("");
            actionForm.setPopRetrievalFrequency(ConfigManager.email.getPopRepeatInterval());
            actionForm.setPopMessageBatchSize(ConfigManager.email.getPopMessagesLimit());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_EMAIL_POP_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_EMAIL_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_EMAIL_POP_EDIT_2_CMD);

        List<LabelValueBean> retrievalOptions = new ArrayList<>();
        for (Integer option : new Integer[]{1, 2, 3, 4, 5}) {
            retrievalOptions.add(new LabelValueBean(String.valueOf(option), String.valueOf(option * DatetimeUtils.ONE_MINUTE_MILLISECONDS)));
        }
        standardTemplate.setAttribute("popRetrievalFrequencyOptions", retrievalOptions);

        List<LabelValueBean> popMessageBatchSizeOptions = new ArrayList<>();
        for (Integer option : new Integer[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100}) {
            popMessageBatchSizeOptions.add(new LabelValueBean(String.valueOf(option), String.valueOf(option)));
        }
        standardTemplate.setAttribute("popMessageBatchSizeOptions", popMessageBatchSizeOptions);
        
        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.config.email.incomingServer.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setMessageKey("admin.config.email.incomingServer.sectionHeader");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());

        PopConnection conn = new PopConnection();
        // Retrieves 1 message at a time for testing.
        conn.setMessagesLimit(1);

        if (actionForm.isTest()) {
            conn.setHost(actionForm.getPopHost());
            conn.setPort(actionForm.getPopPort());
            conn.setUsername(actionForm.getPopUsername());
            conn.setSslEnabled(actionForm.getPopUseSSL());
            conn.setSenderIgnoreList(actionForm.getPopIgnoreSender());

            // Only use the password given in submit form if it's not empty.
            if (actionForm.getPopPassword().isEmpty()) {
                conn.setPassword(ConfigManager.email.getPopPassword());
            } else {
                conn.setPassword(actionForm.getPopPassword());
            }

            IssueService issueService = ServiceProvider.getIssueService(requestContext);
            ActionMessages errors = issueService.retrieveIssueEmails(conn);

            if (errors.isEmpty()) {
                errors.add("fetchSuccess", new ActionMessage("admin.config.email.pop.success", new Object[]{conn.getMessagesRetrieved()}));
            }

            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_EMAIL_POP_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        }

        List<SystemConfig> list = new ArrayList<>();
        list.add(new SystemConfig(ConfigKeys.POP_HOST, actionForm.getPopHost()));
        list.add(new SystemConfig(ConfigKeys.POP_PORT, actionForm.getPopPort()));
        list.add(new SystemConfig(ConfigKeys.POP_USERNAME, actionForm.getPopUsername()));
        list.add(new SystemConfig(ConfigKeys.POP_SSL_ENABLED, actionForm.getPopUseSSL()));
        list.add(new SystemConfig(ConfigKeys.POP_SENDER_IGNORE_LIST, actionForm.getPopIgnoreSender()));
        list.add(new SystemConfig(ConfigKeys.POP_REPEAT_INTERVAL, actionForm.getPopRetrievalFrequency()));
        list.add(new SystemConfig(ConfigKeys.POP_MESSAGES_LIMIT, actionForm.getPopMessageBatchSize()));
        
        if (!actionForm.getPopPassword().isEmpty()) {
            list.add(new SystemConfig(ConfigKeys.POP_PASSWORD, StringUtils.encodeBase64Codec(actionForm.getPopPassword())));
        }

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        ActionMessages errors = adminService.updateConfig(list);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_EMAIL_POP_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_EMAIL_CMD);
        }
    }
}
