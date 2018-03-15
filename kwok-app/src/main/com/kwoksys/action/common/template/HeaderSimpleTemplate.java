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

import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.util.StringUtils;

/**
 * HeaderSimpleTemplate class.
 */
public class HeaderSimpleTemplate extends BaseTemplate {

    private String pageTitleText;
    private String titleText;
    private String themeTitleText;
    private String fontSize;
    private String jqueryDateFormat;
    private String sessionTheme;

    public HeaderSimpleTemplate() {
        super(HeaderSimpleTemplate.class);
    }

    public HeaderSimpleTemplate(Class clazz) {
        super(clazz);
    }

    public void init() {}

    public void applyTemplate() throws Exception {
        sessionTheme = SessionManager.getAppSessionTheme(request.getSession());

        // Replace yy as y because jquery treats y as two-digit year, yy becomes four digits
        jqueryDateFormat = ConfigManager.system.getDateFormat().toLowerCase().replace("yy", "y");

        if (StringUtils.isEmpty(fontSize)) {
            fontSize = SessionManager.getAttribute(request, SessionManager.FONT_SIZE, String.valueOf(ConfigManager.system.getFontOptions()[1]));
        }

        pageTitleText = AdminUtils.getTitleText(requestContext, pageTitleText == null ? titleText : pageTitleText);
    }

    public String getCustomStylesheet() {
        return ConfigManager.system.getSytlesheet();
    }

    public String getThemeStylePath() {
        return AppPaths.getInstance().getThemeCss(sessionTheme);
    }
    
    public String getJqueryDateFormat() {
        return jqueryDateFormat;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public void setTitleKey(String titleKey) {
        this.titleText = Localizer.getText(requestContext, titleKey);
    }

    public void setTitleKey(String titleKey, String[] titleKeyParams) {
        this.titleText = Localizer.getText(requestContext, titleKey, titleKeyParams);
    }

    public void setPageTitleKey(String pageTitleKey) {
        pageTitleText = Localizer.getText(requestContext, pageTitleKey);
    }

    public void setPageTitleKey(String pageTitleKey, Object[] pageTitleParams) {
        pageTitleText = Localizer.getText(requestContext, pageTitleKey, pageTitleParams);
    }

    public String getPageTitleText() {
        return pageTitleText;
    }

    public String getTitleText() {
        return titleText;
    }

    public String getThemeTitleText() {
        return themeTitleText;
    }

    public void setThemeTitleText(String themeTitleText) {
        this.themeTitleText = themeTitleText;
    }
}
