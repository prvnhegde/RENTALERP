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

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

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
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * ConfigFileAction
 */
public class ConfigFileAction extends Action2 {

    public String index() throws Exception {
        AccessUser user = requestContext.getUser();

        FileService fileService = ServiceProvider.getFileService(requestContext);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_FILE_CMD);

        standardTemplate.setAttribute("maxFileUploadSize", FileUtils.formatFileSize(requestContext, ConfigManager.file.getMaxFileUploadSize()));
        standardTemplate.setAttribute("kilobyteUnits", ConfigManager.file.getKilobyteUnits());

        standardTemplate.setAttribute("companyRepositoryPath", ConfigManager.file.getCompanyFileRepositoryLocation());
        standardTemplate.setAttribute("validCompanyRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getCompanyFileRepositoryLocation()));

        standardTemplate.setAttribute("issueRepositoryPath", ConfigManager.file.getIssueFileRepositoryLocation());
        standardTemplate.setAttribute("validIssueRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getIssueFileRepositoryLocation()));

        standardTemplate.setAttribute("hardwareRepositoryPath", ConfigManager.file.getHardwareFileRepositoryLocation());
        standardTemplate.setAttribute("validHardwareRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getHardwareFileRepositoryLocation()));

        standardTemplate.setAttribute("softwareRepositoryPath", ConfigManager.file.getSoftwareFileRepositoryLocation());
        standardTemplate.setAttribute("validSoftwareRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getSoftwareFileRepositoryLocation()));

        standardTemplate.setAttribute("contractRepositoryPath", ConfigManager.file.getContractFileRepositoryLocation());
        standardTemplate.setAttribute("validContractRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getContractFileRepositoryLocation()));

        standardTemplate.setAttribute("kbRepositoryPath", ConfigManager.file.getKbFileRepositoryLocation());
        standardTemplate.setAttribute("validKbRepositoryPath", fileService.isDirectoryExist(ConfigManager.file.getKbFileRepositoryLocation()));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.config.file");
        
        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_FILE_EDIT_CMD);
            link.setTitleKey("common.command.Edit");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        Link link = new Link(requestContext);
        link.setTitleKey("admin.config.file");
        header.addNavLink(link);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit() throws Exception {
        ConfigForm actionForm = getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setKilobyteUnits(ConfigManager.file.getKilobyteUnits());
            actionForm.setFileRepositoryCompany(ConfigManager.file.getCompanyFileRepositoryLocation());
            actionForm.setFileRepositoryIssue(ConfigManager.file.getIssueFileRepositoryLocation());
            actionForm.setFileRepositoryHardware(ConfigManager.file.getHardwareFileRepositoryLocation());
            actionForm.setFileRepositorySoftware(ConfigManager.file.getSoftwareFileRepositoryLocation());
            actionForm.setFileRepositoryContract(ConfigManager.file.getContractFileRepositoryLocation());
            actionForm.setFileRepositoryKb(ConfigManager.file.getKbFileRepositoryLocation());
        }

        List<LabelValueBean> kbUnitOptions = new ArrayList<>();
        for (Integer option : ConfigManager.file.getKilobyteUnitsList()) {
            kbUnitOptions.add(new LabelValueBean(String.valueOf(option), String.valueOf(option)));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_FILE_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_FILE_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_FILE_EDIT_2_CMD);
        standardTemplate.setAttribute("disableFilePathUpdate", FeatureManager.isMultiAppsInstance());
        standardTemplate.setAttribute("kbUnitOptions", kbUnitOptions);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.config.file.edit");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());

        // Check inputs
        List<SystemConfig> list = new ArrayList<>();
        list.add(new SystemConfig(ConfigKeys.FILES_KILOBYTE_UNITS, String.valueOf(actionForm.getKilobyteUnits())));

        // This is so that we can make multi-app environment work better
        if (!FeatureManager.isMultiAppsInstance()) {
            list.add(new SystemConfig(ConfigKeys.COMPANY_FILE_PATH, actionForm.getFileRepositoryCompany()));
            list.add(new SystemConfig(ConfigKeys.ISSUE_FILE_PATH, actionForm.getFileRepositoryIssue()));
            list.add(new SystemConfig(ConfigKeys.HARDWARE_FILE_PATH, actionForm.getFileRepositoryHardware()));
            list.add(new SystemConfig(ConfigKeys.SOFTWARE_FILE_PATH, actionForm.getFileRepositorySoftware()));
            list.add(new SystemConfig(ConfigKeys.CONTRACT_FILE_PATH, actionForm.getFileRepositoryContract()));
            list.add(new SystemConfig(ConfigKeys.KB_FILE_PATH, actionForm.getFileRepositoryKb()));
        }

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        ActionMessages errors = adminService.updateConfig(list);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_FILE_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_FILE_CMD);
        }
    }    
}