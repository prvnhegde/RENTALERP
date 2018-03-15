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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.ConfigKeys;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for company configuration.
 */
public class ConfigCompanyAction extends Action2 {

    public String index() throws Exception {
        AccessUser user = requestContext.getUser();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_COMPANY_CMD);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.configCompany.title");

        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            Link link = new Link(requestContext)
                    .setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_COMPANY_EDIT_CMD)
                    .setTitleKey("common.command.Edit");
            header.addHeaderCmds(link);
        }
        
        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        header.addNavLink(new Link(requestContext).setTitleKey("admin.configCompany.title"));

        //
        // Template: DetailTableTemplate
        //
        DetailTableTemplate detailTableTemplate = standardTemplate.addTemplate(new DetailTableTemplate());
        DetailTableTemplate.Td td = detailTableTemplate.newTd();
        td.setHeaderKey("admin.config.companyName");
        td.setValue(HtmlUtils.encode(ConfigManager.system.getCompanyName()));
        
        td = detailTableTemplate.newTd();
        td.setHeaderKey("admin.config.companyPath");
        td.setValue(HtmlUtils.encode(ConfigManager.system.getCompanyPath()));
        
        td = detailTableTemplate.newTd();
        td.setHeaderKey("admin.config.companyLogoPath");
        td.setValue(HtmlUtils.encode(ConfigManager.system.getCompanyLogoPath()));
        
        td = detailTableTemplate.newTd();
        td.setHeaderKey("admin.config.companyFooterNotes");
        td.setValue(ConfigManager.system.getCompanyFooterNotes());

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String edit() throws Exception {
        ConfigForm actionForm = getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setCompanyName(ConfigManager.system.getCompanyName());
            actionForm.setCompanyPath(ConfigManager.system.getCompanyPath());
            actionForm.setCompanyLogoPath(ConfigManager.system.getCompanyLogoPath());
            actionForm.setCompanyFooterNotes(ConfigManager.system.getCompanyFooterNotes());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_COMPANY_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG
                + "?cmd=" + AdminUtils.ADMIN_COMPANY_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_COMPANY_EDIT_2_CMD);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.configCompanyEdit.header");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());

        List<SystemConfig> list = new ArrayList<>();
        list.add(new SystemConfig(ConfigKeys.COMPANY_NAME, actionForm.getCompanyName()));
        list.add(new SystemConfig(ConfigKeys.COMPANY_PATH, actionForm.getCompanyPath()));
        list.add(new SystemConfig(ConfigKeys.COMPANY_LOGO_PATH, actionForm.getCompanyLogoPath()));
        list.add(new SystemConfig(ConfigKeys.COMPANY_FOOTER_NOTES, actionForm.getCompanyFooterNotes()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        ActionMessages errors = adminService.updateConfig(list);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_COMPANY_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_COMPANY_CMD);
        }
    }    
}
