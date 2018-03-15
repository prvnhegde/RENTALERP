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
package com.kwoksys.biz.system.core.configs;

import java.util.ArrayList;
import java.util.List;

import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * EmailConfigManager
 */
public class EmailConfigManager extends BaseConfigManager {

    private static final EmailConfigManager INSTANCE = new EmailConfigManager();

    private boolean issueNotificationFromUiEnabled;
    
    private boolean issueNotificationFromEmailEnabled;
    
    private boolean contractExpireNotificationEnabled;

    private String allowedDomains;

    private List<String> allowedDomainLowercaseOptions;

    private String smtpFrom;

    private String smtpTo;

    private String smtpHost;

    private String smtpPort;

    private String smtpUsername;

    private String smtpPassword;

    private boolean smtpStartTls;

    private String popPassword;

    private String popHost;

    private String popPort;

    private String popUsername;

    private boolean popSslEnabled;

    private String popSenderIgnoreList;

    private int popRepeatInterval;

    private int popMessagesLimit;

    /**
     * How often do we run the contract expiration notification scheduler (in milliseconds).
     */
    private long contractNotificationInterval;
    
    /**
     * Number of contracts to check for expiration each time.
     */
    private int contractNotificationBatchSize;
    
    private String contractExpireNotificationEmailTemplate;
    
    /**
     * A comma-separated expiration ranges, e.g. "7,30,60" means send out an email when a contract's expiration
     * crosses each expiration "within date". 
     */
    private String[] contractExpirationThresholds;
    
    private String issueReportEmailTemplate;
    private String issueAddEmailTemplate;
    private String issueUpdateEmailTemplate;

    private EmailConfigManager() {}

    static EmailConfigManager getInstance() {
        return INSTANCE;
    }

    /**
     * Initializes configurations.
     * @param configMap
     * @return
     */
    void init(ConfigManager configManager) {
        issueNotificationFromUiEnabled = configManager.getBoolean(ConfigKeys.ISSUE_NOTIFICATION_FROM_UI_ENABLED);
        issueNotificationFromEmailEnabled = configManager.getBoolean(ConfigKeys.ISSUE_NOTIFICATION_FROM_EMAIL_ENABLED);
        contractExpireNotificationEnabled = configManager.getBoolean(ConfigKeys.CONTRACT_EXPIRE_NOTIFY_ENABLED);
        allowedDomains = configManager.getString(ConfigKeys.EMAIL_ALLOWED_DOMAINS);
        
        allowedDomainLowercaseOptions = new ArrayList<>();
        for (String name : allowedDomains.split(",")) {
            if (!name.trim().isEmpty()) {
                allowedDomainLowercaseOptions.add(name.trim().toLowerCase());
            }
        }
        
        issueReportEmailTemplate = configManager.getString(ConfigKeys.ISSUE_REPORT_EMAIL_TEMPLATE);
        issueAddEmailTemplate = configManager.getString(ConfigKeys.ISSUE_ADD_EMAIL_TEMPLATE);
        issueUpdateEmailTemplate = configManager.getString(ConfigKeys.ISSUE_UPDATE_EMAIL_TEMPLATE);

        smtpFrom = configManager.getString(ConfigKeys.SMTP_FROM);
        smtpTo = configManager.getString(ConfigKeys.SMTP_TO);
        smtpHost = configManager.getString(ConfigKeys.SMTP_HOST);
        smtpPort = configManager.getString(ConfigKeys.SMTP_PORT);
        smtpUsername = configManager.getString(ConfigKeys.SMTP_USERNAME);
        smtpPassword = StringUtils.decodeBase64Codec(configManager.getString(ConfigKeys.SMTP_PASSWORD));
        smtpStartTls = configManager.getBoolean(ConfigKeys.SMTP_STARTTLS);

        popHost = configManager.getString(ConfigKeys.POP_HOST);
        popPort = configManager.getString(ConfigKeys.POP_PORT);
        popUsername = configManager.getString(ConfigKeys.POP_USERNAME);
        popPassword = StringUtils.decodeBase64Codec(configManager.getString(ConfigKeys.POP_PASSWORD));
        popSslEnabled = configManager.getBoolean(ConfigKeys.POP_SSL_ENABLED);
        popSenderIgnoreList = configManager.getString(ConfigKeys.POP_SENDER_IGNORE_LIST);
        popRepeatInterval = configManager.getInt(ConfigKeys.POP_REPEAT_INTERVAL);
        popMessagesLimit = configManager.getInt(ConfigKeys.POP_MESSAGES_LIMIT);
        
        contractNotificationInterval = DatetimeUtils.ONE_HOUR_MILLISECONDS;
        contractNotificationBatchSize = 20;
        contractExpireNotificationEmailTemplate = configManager.getString(ConfigKeys.CONTRACT_EXPIRE_NOTIFY_TEMPLATE);
        contractExpirationThresholds = "-1,0,7,30,60".split(",");
    }

    public String getSmtpFrom() {
        return smtpFrom;
    }

    public String getSmtpTo() {
        return smtpTo;
    }

    public boolean isIssueNotificationFromUiEnabled() {
        return issueNotificationFromUiEnabled;
    }

    public boolean isContractExpireNotificationEnabled() {
        return contractExpireNotificationEnabled;
    }

    public List<String> getAllowedDomainLowercaseOptions() {
        return allowedDomainLowercaseOptions;
    }
    public String getSmtpPassword() {
        return smtpPassword;
    }

    public String getPopPassword() {
        return popPassword;
    }

    public String getAllowedDomains() {
        return allowedDomains;
    }

    public boolean isPopSslEnabled() {
        return popSslEnabled;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public String getPopHost() {
        return popHost;
    }

    public String getPopPort() {
        return popPort;
    }

    public boolean getSmtpStartTls() {
        return smtpStartTls;
    }

    public String getPopUsername() {
        return popUsername;
    }

    public String getPopSenderIgnoreList() {
        return popSenderIgnoreList;
    }

    public int getPopRepeatInterval() {
        return popRepeatInterval;
    }

    public int getPopMessagesLimit() {
        return popMessagesLimit;
    }

    public String getIssueReportEmailTemplate() {
        return issueReportEmailTemplate;
    }

    public String getIssueAddEmailTemplate() {
        return issueAddEmailTemplate;
    }

    public String getIssueUpdateEmailTemplate() {
        return issueUpdateEmailTemplate;
    }

    public long getContractNotificationInterval() {
        return contractNotificationInterval;
    }

    public int getContractNotificationBatchSize() {
        return contractNotificationBatchSize;
    }

    public String[] getContractExpirationThresholds() {
        return contractExpirationThresholds;
    }

    public String getContractExpireNotificationEmailTemplate() {
        return contractExpireNotificationEmailTemplate;
    }

    public boolean isIssueNotificationFromEmailEnabled() {
        return issueNotificationFromEmailEnabled;
    }
}
