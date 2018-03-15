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

import com.kwoksys.biz.admin.core.AdminUtils;

/**
 * AdminConfigManager
 */
public class AdminConfigManager extends BaseConfigManager {

    private static final AdminConfigManager instance = new AdminConfigManager();

    private static final int ACCOUNT_LOCKOUT_VALUE = -1;

    private String[] numberOfRowsToShowOptions;

    private String[] usernameDisplayOptions;

    private boolean allowBlankUserPassword;

    private boolean securityPasswordComplexityEnabled;

    private int accountLockoutThreshold;

    private int accountLockoutDurationMinutes;

    /**
     * Account lockout duration in milliseconds
     */
    private long accountLockoutDurationMs;

    private boolean isMultiAppsInstance;

    private boolean isValidateAcctLockout;

    private AdminConfigManager() {}

    static AdminConfigManager getInstance() {
        return instance;
    }

    /**
     * Initializes configurations.
     * @param configMap
     * @return
     */
    void init(ConfigManager configManager) {
        allowBlankUserPassword = configManager.getString(ConfigKeys.SECURITY_ALLOW_BLANK_USER_PASSWORD).equals("1");

        isMultiAppsInstance = configManager.getBoolean(ConfigKeys.MULTI_APPS_INSTANCE);

        usernameDisplayOptions = new String[] {AdminUtils.USER_USERNAME, AdminUtils.USER_DISPLAY_NAME};

        numberOfRowsToShowOptions = configManager.getStringArray("module.numberOfRowsToShow.options");

        securityPasswordComplexityEnabled = configManager.getBoolean(ConfigKeys.SECURITY_USER_PASSWORD_COMPLEX_ENABLED);

        accountLockoutThreshold = configManager.getInt(ConfigKeys.SECURITY_ACCOUNT_LOCKOUT_THRESHOLD);

        accountLockoutDurationMinutes = configManager.getInt(ConfigKeys.SECURITY_ACCOUNT_LOCKOUT_DURATION_MINUTES);

        accountLockoutDurationMs = accountLockoutDurationMinutes * 60 * 1000;

        isValidateAcctLockout = accountLockoutThreshold != 0 && accountLockoutDurationMs != 0;
    }

    public boolean isAllowBlankUserPassword() {
        return allowBlankUserPassword;
    }
    public boolean isMultiAppsInstance() {
        return isMultiAppsInstance;
    }
    public String[] getNumberOfRowsToShowOptions() {
        return numberOfRowsToShowOptions;
    }

    public String[] getUsernameDisplayOptions() {
        return usernameDisplayOptions;
    }

    public boolean isSecurityPasswordComplexityEnabled() {
        return securityPasswordComplexityEnabled;
    }

    public int getAccountLockoutThreshold() {
        return accountLockoutThreshold;
    }

    public int getAccountLockoutDurationMinutes() {
        return accountLockoutDurationMinutes;
    }

    public long getAccountLockoutDurationMs() {
        return accountLockoutDurationMs;
    }

    public boolean isValidateAcctLockout() {
        return isValidateAcctLockout;
    }

    public int getAccountLockoutValue() {
        return ACCOUNT_LOCKOUT_VALUE;
    }
}
