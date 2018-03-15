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
package com.kwoksys.framework.properties;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.LogConfigManager;

/**
 * This is for getting application properties.
 */
public class PropertiesManager {

    private static final Logger LOGGER = Logger.getLogger(PropertiesManager.class.getName());
    
    private static final Properties props = new Properties();
    
    public static final String APP_VERSION_KEY = "app.version";
    public static final String APP_PATCH_KEY = "app.patch";
    public static final String BUILD_DATE = "build.date";
    public static final String DB_TYPE_KEY = "db.type";
    public static final String DB_SERVERHOST_KEY = "db.serverHost";
    public static final String DB_SERVERPORT_KEY = "db.serverPort";
    public static final String DB_NAME_KEY = "db.name";
    public static final String DB_USERNAME_KEY = "db.username";
    public static final String DB_PASSWORD_KEY = "db.password";

    private PropertiesManager() {}
    
    public static void init() throws Exception {
        try {
            for (String path : new String[]{AppPaths.BUILD_PROPERTIES, AppPaths.APPLICATION_PROPERTIES}) {
                LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Loading " + path + "...");
                InputStream in = PropertiesManager.class.getResourceAsStream(path);
                props.load(in);
                in.close();
            }

            props.put(DB_TYPE_KEY, "postgres");

        } catch (Exception e) {
            throw new Exception("Problem loading property file", e);
        }
    }

    public static String get(String key) {
        return props.containsKey(key) ? props.getProperty(key).trim() : null;
    }
    
    public static String getVersion() {
        return get(PropertiesManager.APP_VERSION_KEY);
    }

    public static String getPatchVersion() {
        return getVersion() + (get(PropertiesManager.APP_PATCH_KEY).isEmpty() ? "" : "." + get(PropertiesManager.APP_PATCH_KEY));
    }
}
