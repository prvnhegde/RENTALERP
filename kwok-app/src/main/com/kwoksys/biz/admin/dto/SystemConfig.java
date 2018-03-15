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
package com.kwoksys.biz.admin.dto;

/**
 * This maps to system_config table.
 */
public class SystemConfig {

    private String configKey;
    private String configValue;

    public SystemConfig(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    public SystemConfig(String configKey, boolean configValue) {
        this(configKey, String.valueOf(configValue));
    }

    public SystemConfig(String configKey, int configValue) {
        this(configKey, String.valueOf(configValue));
    }

    public String getConfigValue() {
        return configValue;
    }
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
    public String getConfigKey() {
        return configKey;
    }
    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }
}
