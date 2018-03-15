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
package com.kwoksys.action.home;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.HtmlUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for the index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        String requestLocale = requestContext.getParameterString("locale");
        String errorCode = requestContext.getParameterString("errorCode");
        String fontSize = requestContext.getParameterString("fontSize");

        HttpSession session = request.getSession();

        IndexForm actionForm = getBaseForm(IndexForm.class);
        actionForm.setPassword("");

        // Setting of locale must happen before anything using it
        if (!requestLocale.isEmpty()) {
            Localizer.setSessionLocale(session, requestLocale);
        }
        actionForm.setLocale(requestContext.getLocale().toString());
        SessionManager.setAppSessionTheme(session, actionForm.getTheme());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAdEnabled(false);
        standardTemplate.setPathAttribute("formLoginAction", AppPaths.AUTH_VERIFY);
        standardTemplate.setPathAttribute("formAction", AppPaths.HOME_INDEX);
        standardTemplate.setAttribute("isUserLoggedOn", user.isLoggedOn());
        standardTemplate.setAttribute("domain", ConfigManager.auth.getAuthDomain());
        standardTemplate.setAttribute("localeOptions", WidgetUtils.getLocaleOptions(requestContext));

        List<String> themeLinks = new ArrayList<>();
        for (String theme : ConfigManager.system.getThemeOptions()) {
            themeLinks.add(new Link(requestContext).setAppPath(AppPaths.HOME_INDEX + "?theme=" + theme)
                    .setTitleKey("admin.config.theme." + theme).getString());
        }
        standardTemplate.setAttribute("themeLinks", themeLinks);

        List<String> fontLinks = new ArrayList<>();
        for (int fontOption : ConfigManager.system.getFontOptions()) {
            fontLinks.add(new Link(requestContext).setAppPath(AppPaths.HOME_INDEX + "?fontSize=" + fontOption)
                    .setTitle(String.valueOf(fontOption)).getString());
        }
        standardTemplate.setAttribute("fontLinks", fontLinks);

        standardTemplate.setAttribute("homeCustomDescription", ConfigManager.system.getCustomHomeDescription());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        if (!fontSize.isEmpty()) {
            request.getSession().setAttribute(SessionManager.FONT_SIZE, fontSize);
            header.setFontSize(fontSize);
        }

        // The user has logged on to the system.
        if (user.isLoggedOn()) {
            Map<String, String> userMap = new HashMap<>();
            standardTemplate.setAttribute("user", userMap);

            // Get some info about this user.
            userMap.put("username", user.getUsername());
            userMap.put("displayName", user.getDisplayName());
            userMap.put("email", HtmlUtils.formatMailtoLink(user.getEmail()));
            userMap.put("creationDate", user.getCreationDate());

        } else {
            // If not a resubmit, set some defaults
            if (!actionForm.isResubmit()) {
                actionForm.setUsername("");
                actionForm.setDomain(null);
            }

            // For login.
            List<LabelValueBean> domainOptions = new ArrayList<>();

            // Generate domain selectbox.
            for (String domain : ConfigManager.auth.getAuthDomainOptions()) {
                domainOptions.add(new LabelValueBean(domain.trim(), domain.trim()));
            }
            if (!ConfigManager.auth.getAuthDomainOptions().isEmpty() && !ConfigManager.auth.getAuthMethod().equals(Access.AUTH_LDAP)) {
                domainOptions.add(new SelectOneLabelValueBean(requestContext));
            }
            standardTemplate.setAttribute("domainOptions", domainOptions);

            // Display any error code the login may have
            if (!errorCode.isEmpty()) {
                ActionMessages errors = new ActionMessages();
                errors.add(errorCode, new ActionMessage("auth.login.error." + errorCode));
                saveActionErrors(errors);
            }

            standardTemplate.getHeaderTemplate().setOnloadJavascript("App.focusLogin(document.loginForm.username, document.loginForm.password);");
        }

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
