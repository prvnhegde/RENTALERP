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

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.framework.http.RequestContext;

/**
 * Action form for the index page.
 */
public class IndexForm extends BaseForm {

    private String password;
    private String username;
    private String domain;
    private String redirectPath;// The page where we're redirecting user to
    private String theme = "";
    private String locale = "";

    @Override
    public void setRequest(RequestContext requestContext) {
        username = requestContext.getParameterString("username");
        password = requestContext.getParameterString("password");
        domain = requestContext.getParameterString("domain");
        redirectPath = requestContext.getParameterString("redirectPath");
        theme = requestContext.getParameterString("theme");
        locale = requestContext.getParameterString("locale");
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRedirectPath() {
        return redirectPath;
    }
    public void setRedirectPath(String redirectPath) {
        this.redirectPath = redirectPath;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getTheme() {
        return theme;
    }

    public String getLocale() {
        return locale;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
}
