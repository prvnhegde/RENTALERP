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
package com.kwoksys.action.common.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Modules;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.ui.CssStyles;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * HeaderTemplate class.
 */
public class HeaderTemplate extends HeaderSimpleTemplate {

    private String titleClass; /** CSS style for title **/
    private String sectionText;
    private List<Map<String, String>> moduleTabs = new ArrayList<>();
    private List<Link> headerCmds = new ArrayList<>();
    private List<Link> navLinks = new ArrayList<>();
    private Integer moduleId;
    private String appLogoPath;
    private String notifyMessage;
    private String navCmds;
    private String userMessage;
    private String onloadJavascript;

    public HeaderTemplate() {
        super(HeaderTemplate.class);
    }

    public void init() {}

    public void applyTemplate() throws Exception {
        super.applyTemplate();

        AccessUser user = requestContext.getUser();

        moduleId = (Integer) request.getAttribute(RequestContext.MODULE_KEY);
        for (String strModuleId : ConfigManager.system.getModuleTabs()) {
            Integer thisModuleId = Integer.parseInt(strModuleId);
            String modulePath = Modules.getModulePath(thisModuleId);

            if (user.hasPermission(modulePath)) {
                Map<String, String> moduleMap = new HashMap<>();
                moduleMap.put("modulePath", new Link(requestContext).setAjaxPath(modulePath)
                        .setTitleKey("core.moduleName." + strModuleId)
                        .setId("headerModule" + thisModuleId)
                        .setStyleClass("themeBorder")
                        .getString());
                moduleTabs.add(moduleMap);
            }
        }

        // Show portal sites that are shown as tabs
        List<Site> sites = new CacheManager(requestContext).getModuleTabsCache();
        for (Site site : sites) {
            Map<String, String> moduleMap = new HashMap<>();
            moduleMap.put("modulePath", new Link(requestContext).setPath(site.getPath())
                    .setTitle(site.getName()).setStyleClass("themeBorder").getString());
            moduleTabs.add(moduleMap);
        }

        List<String> userMessages = new ArrayList<>();
        if (user.isLoggedOn()) {
            Object[] args = {HtmlUtils.encode(AdminUtils.getSystemUsername(requestContext, user))};
            userMessages.add(Localizer.getText(requestContext, "core.template.header.welcomeUser", args));

            if (!ConfigManager.auth.isBasicAuth()) {
                Link link = new Link(requestContext)
                        .setAppPath(AppPaths.AUTH_LOGOUT)
                        .setMaterialIcon(Image.getInstance().getLogout())
                        .setTitleKey("core.template.header.logout");
                userMessages.add(link.getString());
            }
        }
        
        // Check if the user has access to user preference module.
        if (user.hasPermission(AppPaths.USER_PREF_INDEX)) {
            Link link = new Link(requestContext)
                .setAjaxPath(AppPaths.USER_PREF_INDEX)
                .setMaterialIcon(Image.getInstance().getPreference())
                .setTitleKey("core.template.header.userPreference");
            userMessages.add(link.getString());
        }

        // Show admin path if user has permission to see the page
        if (user.hasPermission(AppPaths.ADMIN_INDEX)) {
            Link link = new Link(requestContext)
                .setAjaxPath(AppPaths.ADMIN_INDEX)
                .setMaterialIcon(Image.getInstance().getAdminIcon())
                .setTitleKey("admin.index.title");
            userMessages.add(link.getString());
        }
        
        userMessage = StringUtils.join(userMessages, " | ");

        // Navigation commands
        if (!navLinks.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Link link : navLinks) {
                if (sb.length() != 0) {
                    sb.append(" / ");
                }
                sb.append(link.getString());
            }
            navCmds = sb.toString();
        }

        Link link = new Link(requestContext)
                .setExternalPath(ConfigManager.system.getCompanyPath())
                .setImgSrc(ConfigManager.system.getCompanyLogoPath().isEmpty() ? Image.getInstance().getAppLogo()
                        : ConfigManager.system.getCompanyLogoPath());
        appLogoPath = link.getString();
        
        // If notify message is not set, try reading it from session.
        if (notifyMessage == null) {
            String sessionNotifyMessage = (String) requestContext.getSession().getAttribute(RequestContext.URL_PARAM_NOTIFY);
            
            if (sessionNotifyMessage != null) {
                requestContext.getSession().removeAttribute(RequestContext.URL_PARAM_NOTIFY);
                
                if (requestContext.getParameterBoolean(RequestContext.URL_PARAM_NOTIFY)) {
                    notifyMessage = sessionNotifyMessage;
                }
            }
        }
        
        NotificationBarTemplate notificationTemplate = addTemplate(new NotificationBarTemplate());

        if (notifyMessage != null) {
            notificationTemplate.setNotifyMessage(notifyMessage);
        }
    }

    @Override
    public String getJspPath() {
        return "/jsp/common/template/Header.jsp";
    }

    public void setTitleClassNoLine() {
        this.titleClass = CssStyles.NO_LINE;
    }

    public void addNavLink(Link link) {
        if (link != null) {
            navLinks.add(link);
        }
    }

    public void addHeaderCmds(Link headerCmd) {
        headerCmd.setStyleClass(CssStyles.BTN);
        headerCmds.add(headerCmd);
    }

    public List<Link> getHeaderCmds() {
        return headerCmds;
    }

    public String getTitleClass() {
        return titleClass;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public String getModuleIds() {
        return StringUtils.join(ConfigManager.system.getModuleTabs(), ",");
    }

    public String getAppLogoPath() {
        return appLogoPath;
    }

    public String getNotificationMsg() {
        return notifyMessage;
    }

    public void setNotifyMessageKey(String notifyMessageKey) {
        this.notifyMessage = Localizer.getText(requestContext, notifyMessageKey);
    }

    public List<Map<String, String>> getModuleTabs() {
        return moduleTabs;
    }

    public String getSectionText() {
        return sectionText;
    }

    public void setSectionKey(String sectionKey) {
        sectionText = Localizer.getText(requestContext, sectionKey);
    }

    public void setSectionKey(String sectionKey, Object[] sectionMsgParams) {
        sectionText = Localizer.getText(requestContext, sectionKey, sectionMsgParams);
    }

    public void setSectionText(String sectionText) {
        this.sectionText = sectionText;
    }

    public String getNavCmds() {
        return navCmds;
    }

    public String getUserMessage() {
        return userMessage;
    }
    
    public String getOnloadJavascript() {
        return onloadJavascript;
    }

    public void setOnloadJavascript(String onloadJavascript) {
        this.onloadJavascript = onloadJavascript;
    }
}
