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

import com.kwoksys.biz.system.core.AppPaths;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * LogConfigManager
 */
public class LogConfigManager {

    private static Map<String, String> map = new HashMap<>();

    public static final String PAGE_REQUEST_PREFIX = "[Page Request" + AppPaths.ROOT + "]";

    public static final String DATABASE_ACCESS_PREFIX = "[Database Access" + AppPaths.ROOT + "]";

    public static final String AUTHENTICATION_PREFIX = "[Authentication" + AppPaths.ROOT + "]";

    public static final String SCHEDULER_PREFIX = "[Scheduler" + AppPaths.ROOT + "]";

    public static final String CONFIG_PREFIX = "[Config" + AppPaths.ROOT + "]";

    public static final String EMAIL_PREFIX = "[Email" + AppPaths.ROOT + "]";

    public static final String SHUTDOWN_PREFIX = "[Shutdown" + AppPaths.ROOT + "]";

    public static final String TEMPLATE_PREFIX = "[Template" + AppPaths.ROOT + "]";

    public static final String CACHE_PREFIX = "[Cache" + AppPaths.ROOT + "]";

    public static final String TEST_PREFIX = "[Test]";

    public static final String LOG_LEVEL_INFO = "INFO";
    public static final String LOG_LEVEL_OFF = "OFF";

    public static void init(Map<String, String> configMap) {
        map.put(DATABASE_ACCESS_PREFIX, configMap.get(ConfigKeys.LOGGING_LEVEL_DATABASE));
        map.put(AUTHENTICATION_PREFIX, configMap.get(ConfigKeys.LOGGING_LEVEL_LDAP));
        map.put(SCHEDULER_PREFIX, configMap.get(ConfigKeys.LOGGING_LEVEL_SCHEDULER));
        map.put(TEMPLATE_PREFIX, configMap.get(ConfigKeys.LOGGING_LEVEL_TEMPLATE));
    }
    
    public static Level getLogLevel(String prefix) {
        String level = map.get(prefix);
        if (LOG_LEVEL_OFF.equals(level)) {
            return LogLevel.OFF;
        } else {
            return Level.INFO;
        }
    }

    private static class LogLevel extends Level {
        public static final LogLevel OFF = new LogLevel("OFF", 0);

        protected LogLevel(String name, int value) {
            super(name, value);
        }
    }
}
