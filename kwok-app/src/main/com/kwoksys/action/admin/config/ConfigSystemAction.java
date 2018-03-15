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

import java.util.Map;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.FeatureManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.connections.database.DatabaseManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.properties.PropertiesManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.StringUtils;

/**
 * Action class for config system page.
 */
public class ConfigSystemAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Call the service
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Get database info
        Map dbmap = systemService.getDatabaseInfo();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_SYSTEM_INFO_CMD);
        standardTemplate.setAttribute("browserUserAgent", requestContext.getRequest().getHeader("user-agent"));
        standardTemplate.setAttribute("os", Localizer.getText(requestContext, "admin.configData.os.value",
                new Object[]{System.getProperty("os.name"), System.getProperty("os.version")}));
        standardTemplate.setAttribute("osArch", System.getProperty("os.arch"));
        standardTemplate.setAttribute("jvmVersion", System.getProperty("java.version"));
        standardTemplate.setAttribute("jvmVendor", System.getProperty("java.vendor"));
        standardTemplate.setAttribute("jvmHome", System.getProperty("java.home"));
        standardTemplate.setAttribute("jvmFreeMemory", FileUtils.formatFileSize(requestContext, Runtime.getRuntime().freeMemory()));
        standardTemplate.setAttribute("jvmTotalMemory", FileUtils.formatFileSize(requestContext, Runtime.getRuntime().totalMemory()));
        standardTemplate.setAttribute("jvmMaxMemory", FileUtils.formatFileSize(requestContext, Runtime.getRuntime().maxMemory()));
        standardTemplate.setAttribute("userHome", System.getProperty("user.home"));
        standardTemplate.setAttribute("dbProductName", dbmap.get("DatabaseProductName"));
        standardTemplate.setAttribute("dbProductVersion", dbmap.get("DatabaseProductVersion"));

        if (!FeatureManager.isMultiAppsInstance()) {
            standardTemplate.setAttribute("databases", StringUtils.join(adminService.getDatabases(), "database_name", ", "));
        }

        // Database repository
        standardTemplate.setAttribute("dbHost", PropertiesManager.get(PropertiesManager.DB_SERVERHOST_KEY));
        standardTemplate.setAttribute("dbPort", PropertiesManager.get(PropertiesManager.DB_SERVERPORT_KEY));
        standardTemplate.setAttribute("dbName", PropertiesManager.get(PropertiesManager.DB_NAME_KEY));
        standardTemplate.setAttribute("dbMaxPoolSize", ConfigManager.system.getDbMaxConnection());
        standardTemplate.setAttribute("dbPoolSizeCurrent", Localizer.getText(requestContext, "admin.config.db.poolSize" ,
                new Integer[]{DatabaseManager.getInstance().getCheckedOutPoolSize(), DatabaseManager.getInstance().getAvailablePoolSize()}));

        standardTemplate.setAttribute("backupLink", new Link(requestContext).setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd="
                + AdminUtils.ADMIN_DB_BACKUP_CMD).setTitleKey("admin.config.db.backup.cmd").getString());

        // Database sequences
        standardTemplate.setAttribute("loadDbSequencesLink", new Link(requestContext).setTitleKey("admin.config.system.loadDbSequences")
                .setJavascript("App.toggleViewUpdate('dbSequences', '" + AppPaths.ROOT + AppPaths.ADMIN_CONFIG + "?cmd="
                                + AdminUtils.ADMIN_DB_SEQUENCES_CMD + "')").getString());

        // Logging
        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            standardTemplate.setAttribute("loggingLink", new Link(requestContext).setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE
                    + "?cmd=" + AdminUtils.ADMIN_LOGGING_EDIT_CMD).setTitleKey("common.command.Edit").getString());
        }
        standardTemplate.setAttribute("databaseAccessLogLevel", LogConfigManager.getLogLevel(LogConfigManager.DATABASE_ACCESS_PREFIX));
        standardTemplate.setAttribute("ldapLogLevel", LogConfigManager.getLogLevel(LogConfigManager.AUTHENTICATION_PREFIX));
        standardTemplate.setAttribute("schedulerLogLevel", LogConfigManager.getLogLevel(LogConfigManager.SCHEDULER_PREFIX));
        standardTemplate.setAttribute("templateLogLevel", LogConfigManager.getLogLevel(LogConfigManager.TEMPLATE_PREFIX));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleClassNoLine();
        header.setTitleKey("admin.configHeader.system_info");

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.configHeader.system_info"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
