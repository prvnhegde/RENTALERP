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
package com.kwoksys.action.admin;

import java.util.ArrayList;
import java.util.List;

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.NotificationBarTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.ThisTemplate;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.test.MainTest;

/**
 * Action class for Admin Index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            if (requestContext.getParameterString("cmd").equals(AdminUtils.ADMIN_RELOAD_LOCALIZER_CMD)) {
                return reloadLocalizer();
            }            
            if (ConfigManager.isDevMode() && requestContext.getParameterString("cmd").equals(AdminUtils.ADMIN_RUN_TESTS_CMD)) {
                new MainTest(requestContext).execute();
                return null;
            }
        }

        List<String> configList = new ArrayList<>();
        // Link to database config page.
        if (user.hasPermission(AppPaths.ADMIN_CONFIG)) {
            configList.add(Links.getSystemSettingsImageLink(requestContext).getString());

            // Link to application config page.
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminAppConfIcon());
            link.setTitleKey("admin.config.app");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_APP_CMD);
            configList.add(link.getString());

            // Link to security settings
            link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminSecurityIcon());
            link.setTitleKey("admin.config.auth");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_AUTH_CMD);
            configList.add(link.getString());

            // Link to email settings page.
            link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminEmailIcon());
            link.setTitleKey("admin.config.email.title");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_EMAIL_CMD);
            configList.add(link.getString());

            // Link to company config page.
            link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminCompanyIcon());
            link.setTitleKey("admin.configCompany.title");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_COMPANY_CMD);
            configList.add(link.getString());

            // File Settings
            link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminFileSettingsIcon());
            link.setTitleKey("admin.config.file");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_FILE_CMD);
            configList.add(link.getString());

            // Link to look and feel config page.
            link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminLookFeelIcon());
            link.setTitleKey("admin.index.lookAndFeel");
            link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_LOOK_FEEL_CMD);
            configList.add(link.getString());
        }

        // Link to Attributes page.
        if (user.hasPermission(AppPaths.ADMIN_ATTRIBUTE_LIST)) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminSystemFieldsIcon());
            link.setTitleKey("admin.attributeList");
            link.setAjaxPath(AppPaths.ADMIN_ATTRIBUTE_LIST);
            configList.add(link.getString());
        }

        // Link to Custom Attributes page.
        if (user.hasPermission(AppPaths.ADMIN_CUSTOM_ATTR_LIST)) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminCustomFieldsIcon());
            link.setTitleKey("admin.customAttrList");
            link.setAjaxPath(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
            configList.add(link.getString());
        }

        // Link to groups page
        if (user.hasPermission(AppPaths.ADMIN_GROUP_LIST)) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getGroupIcon());
            link.setTitleKey("admin.index.groupList");
            link.setAjaxPath(AppPaths.ADMIN_GROUP_LIST);
            configList.add(link.getString());
        }

        // Link to Users page
        if (user.hasPermission(AppPaths.ADMIN_USER_LIST)) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getUserIcon());
            link.setTitleKey("admin.config.users");
            link.setAjaxPath(AppPaths.ADMIN_USER_INDEX);
            configList.add(link.getString());
        }

        // Link to data import
        if (user.hasPermission(AppPaths.ADMIN_DATA_IMPORT_INDEX)) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getAdminDataImport());
            link.setTitleKey("import");
            link.setAjaxPath(AppPaths.ADMIN_DATA_IMPORT_INDEX);
            configList.add(link.getString());
        }

        if (user.hasPermission(AppPaths.ADMIN_CONFIG_WRITE)) {
            configList.add(new Link(requestContext).setImgSrc(Image.getInstance().getAdminLocalizationReloadIcon())
                .setTitleKey("admin.config.localizationReload")
                .setJavascript("App.updateView({loadImage: WIDGET_LOADING_IMAGE, loadElemId:'localizationReload',"
                        + " targetElemId:'notificationBar'}, '" + AppPaths.ROOT + AppPaths.ADMIN_INDEX + "?cmd=" 
                        + AdminUtils.ADMIN_RELOAD_LOCALIZER_CMD + "'); return false;").getString()
                        + " <span id=\"localizationReload\">&nbsp;</span>");
        }

        List<String> configListB = new ArrayList<>();
        // Documentation
        Link link = new Link(requestContext);
        link.setImgSrc(Image.getInstance().getAdiminDocIcon());
        link.setTitleKey("admin.index.documentation");
        link.setExternalPath(AppPaths.SITE_DOCUMENTATION);
        configListB.add(link.getString());

        // Check to see if it should show Send Feedback link
        link = new Link(requestContext);
        link.setImgSrc(Image.getInstance().getAdminSurveyIcon());
        link.setTitleKey("admin.index.usageSurvey");
        link.setExternalPath(AppPaths.SITE_SURVEY + "?ver=" + ConfigManager.getSchemaVersion());
        configListB.add(link.getString());

        if (ConfigManager.isDevMode()) {
            configList.add(new Link(requestContext)
                    .setTitleKey("admin.config.runTests")
                    .setAppPath(AppPaths.ADMIN_INDEX + "?cmd=" + AdminUtils.ADMIN_RUN_TESTS_CMD).getString());
        }
        
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("configList", configList);
        request.setAttribute("configListB", configListB);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.index.title");

        //
        // Template: ThisTemplate
        //
        standardTemplate.addTemplate(new ThisTemplate());

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    private String reloadLocalizer() throws Exception {
        Localizer.init();

        //
        // Template: AjaxTemplate
        //
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);

        //
        // Template: NotificationBarTemplate
        //
        NotificationBarTemplate notificationBarTemplate = ajaxTemplate.addTemplate(new NotificationBarTemplate());
        notificationBarTemplate.setNotifyMessageKey("admin.config.localizationReload.success");        

        return ajaxTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
}
