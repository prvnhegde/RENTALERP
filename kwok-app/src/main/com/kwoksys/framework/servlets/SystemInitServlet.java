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
package com.kwoksys.framework.servlets;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.biz.system.core.scheduler.TaskScheduler;
import com.kwoksys.framework.connections.database.DatabaseManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.properties.PropertiesManager;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.util.HttpUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * SystemInitServlet
 */
public class SystemInitServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SystemInitServlet.class.getName());

    public static boolean init = false;

    public static String initError = "";

    public void init(ServletConfig cfg) {
        ServletContext context = cfg.getServletContext();
        SystemService systemService = ServiceProvider.getSystemService(null);

        try {
            // Initialize root path
            AppPaths.init(context.getContextPath());

            PropertiesManager.init();
            
            LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Starting Kwok application version " + PropertiesManager.getPatchVersion());

            // Log system properties
            Properties props = System.getProperties();
            for (Map.Entry entry : props.entrySet()) {
                LOGGER.info(LogConfigManager.CONFIG_PREFIX + " " + entry.getKey() + ": " + entry.getValue());
            }

            // Initialize localization settings
            Localizer.init();

            CacheManager.init();

            // Initialize application settings
            ConfigManager.getInstance().init();

            // Compare app and schema versions
            if (!PropertiesManager.getVersion().equals(ConfigManager.getSchemaVersion())) {
                initError = Localizer.getText(new RequestContext(), "core.template.footer.versionMismatch",
                                    new String[]{PropertiesManager.getVersion(), ConfigManager.getSchemaVersion()});
                LOGGER.warning(LogConfigManager.CONFIG_PREFIX + " " + initError);
                return;
            }

            // Update database timezone to GMT.
            try {
                String dbTimezone = systemService.getDatabaseTimezone();
                LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Database timezone is: " + dbTimezone);

                if (!dbTimezone.equalsIgnoreCase(ConfigManager.system.getTimezoneBaseString())) {
                    LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Updating database timezone to: " + ConfigManager.system.getTimezoneBaseString());
                    ActionMessages messages = systemService.updateDatabaseTimezone();
                    if (!messages.isEmpty()) {
                        throw new Exception("Problem updating database timezone");
                    }
                    
                    LOGGER.warning(LogConfigManager.CONFIG_PREFIX + " Done updating database timezone, application server restart required!");
                    return;
                }
            } catch (DatabaseException e) {
                throw new Exception("Problem updating database timezone", e);
            }

            // Start scheduler.
            TaskScheduler.init();

            init = true;
            LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Kwok application started successfully. Ports: " + StringUtils.join(HttpUtils.getListeningPorts(), ". "));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, LogConfigManager.CONFIG_PREFIX + " " + e.getMessage(), e);
        }
    }
    
    public void destroy() {
        TaskScheduler.destroy();
        DatabaseManager.destroy();
        LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Kwok application shutdown completed");
    }
}