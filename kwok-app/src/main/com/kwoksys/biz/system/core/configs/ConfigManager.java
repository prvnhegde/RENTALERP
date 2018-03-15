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

import com.kwoksys.framework.license.LicenseManager;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.util.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * System configuration. Please remember that some of these configuration may not exist if users forgot to upgrade
 * the database repository.
 */
public class ConfigManager {

    private static final Logger LOGGER = Logger.getLogger(ConfigManager.class.getName());

    private static final ConfigManager INSTANCE = new ConfigManager();
    
    public static AdminConfigManager admin = AdminConfigManager.getInstance();
    public static AuthConfigManager auth = AuthConfigManager.getInstance();
    public static EmailConfigManager email = EmailConfigManager.getInstance();
    public static FileConfigManager file = FileConfigManager.getInstance();
    public static AppConfigManager app = AppConfigManager.getInstance();
    public static ReportConfigManager reports = ReportConfigManager.getInstance();
    public static SystemConfigManager system = SystemConfigManager.getInstance();

    public static List<BaseConfigManager> list = Arrays.asList(admin, auth, email, file, reports, app, system);

    private static String schemaVersion;
    
    private static boolean devMode = false;

    private Map<String, String> configMap;

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public void init() throws Exception {
        LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Initializing " + ConfigManager.class.getSimpleName() + "...");
        configMap = new CacheManager().cacheSystemConfigs();

        try {
            schemaVersion = configMap.get(ConfigKeys.SCHEMA_VERSION);

            for (BaseConfigManager configMgr : list) {
                configMgr.init(this);
            }

            LogConfigManager.init(configMap);
            LicenseManager.init();
            
        } catch (Exception e) {
            throw new Exception("Problem setting system config values", e);
        }
    }

    public static String getSchemaVersion() {
        return schemaVersion;
    }
    
    protected boolean getBoolean(String configKey) {
        return Boolean.valueOf(configMap.get(configKey));
    }

    protected String getString(String configKey) {
        return configMap.get(configKey);
    }

    protected int getInt(String configKey) {
        return NumberUtils.replaceNull(configMap.get(configKey));
    }

    protected long getLong(String configKey) {
        return Long.valueOf(configMap.get(configKey));
    }

    protected String[] getStringArray(String configKey) {
        return configMap.get(configKey).split(",");
    }

    protected List<String> getStringList(String configKey) {
        return Arrays.asList(configMap.get(configKey).split(","));
    }

    public static boolean isDevMode() {
        return devMode;
    }
}
