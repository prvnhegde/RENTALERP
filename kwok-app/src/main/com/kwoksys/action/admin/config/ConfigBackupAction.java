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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.FeatureManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.ConfigKeys;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.PropertiesManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.DatetimeUtils;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.struts.action.ActionMessages;

/**
 * Action class for DB backup.
 */
public class ConfigBackupAction extends Action2 {

    private static final Logger LOGGER = Logger.getLogger(ConfigBackupAction.class.getName());

    public String index() throws Exception {
        AccessUser user = requestContext.getUser();

        FileService fileService = ServiceProvider.getFileService(requestContext);
        boolean validBackupCmdPath = fileService.isFileExist(ConfigManager.file.getDbPostgresProgramPath());
        boolean validBackupRepoPath = fileService.isDirectoryExist(ConfigManager.file.getDbBackupRepositoryPath());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_DB_BACKUP_CMD);

        request.setAttribute("backupCmdPath", ConfigManager.file.getDbPostgresProgramPath());
        request.setAttribute("validBackupCmdPath", validBackupCmdPath);

        request.setAttribute("backupCmd", AdminUtils.getBackupCommandDisplay());

        request.setAttribute("backupRepoPath", ConfigManager.file.getDbBackupRepositoryPath());
        request.setAttribute("validBackupRepoPath", validBackupRepoPath);

        request.setAttribute("backupExecutePath", new Link(requestContext)
                .setAppPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_DB_BACKUP_EXECUTE)
                .setOnclick("App.dbBackup('" + AppPaths.ROOT + AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_DB_BACKUP_EXECUTE + "');return false;")
                .setTitleKey("admin.config.db.backup.execute.cmd"));

        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            request.setAttribute("backupCmdEnabled", (validBackupCmdPath && validBackupRepoPath));
        }

        File directory = new File(ConfigManager.file.getDbBackupRepositoryPath());
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            List<Map> fileMaps = new ArrayList<>();
            for (File file : files) {
                Map<String, Object> map = new HashMap<>();
                map.put("filename", file.getName());
                map.put("fileModifiedDate", DatetimeUtils.toLocalDatetime(file.lastModified()));

                map.put("filesize", FileUtils.formatFileSize(requestContext, file.length()));
                fileMaps.add(map);
            }
            request.setAttribute("backupFiles", fileMaps);
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.config.db.backup.header");

        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_DB_BACKUP_EDIT_CMD);
            link.setTitleKey("common.command.Edit");
            headerTemplate.addHeaderCmds(link);
        }

        // Back to admin home
        headerTemplate.addNavLink(Links.getAdminHomeLink(requestContext));

        if (user.hasPermission(AppPaths.ADMIN_CONFIG)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_SYSTEM_INFO_CMD);
            link.setTitleKey("admin.configHeader.system_info");
            headerTemplate.addNavLink(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit() throws Exception {
        ConfigForm actionForm = getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setDbBackupProgramPath(ConfigManager.file.getDbPostgresProgramPath());
            actionForm.setDbBackupRepositoryPath(ConfigManager.file.getDbBackupRepositoryPath());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_DB_BACKUP_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_DB_BACKUP_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_DB_BACKUP_EDIT_2_CMD);
        standardTemplate.setAttribute("disableFilePathUpdate", FeatureManager.isMultiAppsInstance());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.db.backup.header");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setMessageKey("admin.config.sectionHeader");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());

        List<SystemConfig> list = new ArrayList<>();
        if (!FeatureManager.isMultiAppsInstance()) {
            list.add(new SystemConfig(ConfigKeys.DB_POSTGRES_PROGRAM_PATH, actionForm.getDbBackupProgramPath()));
            list.add(new SystemConfig(ConfigKeys.DB_BACKUP_REPOSITORY_PATH, actionForm.getDbBackupRepositoryPath()));
        }

        // Get the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        ActionMessages errors = adminService.updateConfig(list);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_DB_BACKUP_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_DB_BACKUP_CMD);
        }
    }
    
    public String execute() throws Exception {
        ProcessBuilder pb = new ProcessBuilder(AdminUtils.getBackupCommand());
        pb.environment().put("PGPASSWORD", PropertiesManager.get(PropertiesManager.DB_PASSWORD_KEY));
        pb.redirectErrorStream(true);

        try{
            pb.start();

            saveNotifyMessageKey("admin.config.db.backup.success");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Problem with PostgreSQL backup.", e);
        }

        pb.environment().remove("PGPASSWORD");

        return ajaxUpdateView(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_DB_BACKUP_CMD 
                + "&" + RequestContext.URL_PARAM_NOTIFY_TRUE
                + "&" + RequestContext.URL_PARAM_AJAX + "=" + requestContext.isAjax());
    }
}