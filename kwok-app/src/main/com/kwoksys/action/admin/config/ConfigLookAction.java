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
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.ConfigKeys;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.CkeditorHelper;
import com.kwoksys.framework.util.StringUtils;

/**
 * Action class for look and feel configuration.
 */
public class ConfigLookAction extends Action2 {

    public String index() throws Exception {
        AccessUser user = requestContext.getUser();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_LOOK_FEEL_CMD);
        standardTemplate.setAttribute("theme", Localizer.getText(requestContext, "admin.config.theme." + ConfigManager.system.getTheme()));
        standardTemplate.setAttribute("stylesheet", ConfigManager.system.getSytlesheet());
        standardTemplate.setAttribute("homeCustomDescription", ConfigManager.system.getCustomHomeDescription());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.index.lookAndFeel");
        
        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_LOOK_FEEL_EDIT_CMD);
            link.setTitleKey("common.command.Edit");
            header.addHeaderCmds(link);
        }

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));

        header.addNavLink(new Link(requestContext).setTitleKey("admin.index.lookAndFeel"));
        
        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit() throws Exception {
        ConfigForm actionForm = getBaseForm(ConfigForm.class);

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setTheme(ConfigManager.system.getTheme());
            actionForm.setStylesheet(ConfigManager.system.getSytlesheet());
            actionForm.setHomeCustomDescription(ConfigManager.system.getCustomHomeDescription());
        }

        List<LabelValueBean> themeOptions = new ArrayList<>();
        for (String option : ConfigManager.system.getThemeOptions()) {
            themeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.config.theme." + option), option));
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext, AdminUtils.ADMIN_LOOK_FEEL_EDIT_CMD);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CONFIG_WRITE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_LOOK_FEEL_CMD).getString());
        standardTemplate.setAttribute("cmd", AdminUtils.ADMIN_LOOK_FEEL_EDIT_2_CMD);
        standardTemplate.setAttribute("themeOptions", themeOptions);
        standardTemplate.setAttribute("homepageContent", StringUtils.encodeCkeditorJs(actionForm.getHomeCustomDescription()));
        standardTemplate.setAttribute("language", CkeditorHelper.getLocaleKey(requestContext.getLocale()));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.configHeader.look_config_edit");

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        ConfigForm actionForm = saveActionForm(new ConfigForm());

        // Set session theme.
        String theme = actionForm.getTheme();

        List<SystemConfig> list = new ArrayList<>();
        list.add(new SystemConfig(ConfigKeys.THEME_DEFAULT, theme));
        list.add(new SystemConfig(ConfigKeys.UI_STYLESHEET, actionForm.getStylesheet()));
        list.add(new SystemConfig(ConfigKeys.CUSTOM_HOME_DESCRIPTION, actionForm.getHomeCustomDescription()));

        // Get the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        ActionMessages errors = adminService.updateConfig(list);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_LOOK_FEEL_EDIT_CMD + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Update session variable
            SessionManager.setAppSessionTheme(request.getSession(), theme);

            return redirect(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_LOOK_FEEL_CMD);
        }
    }
}
