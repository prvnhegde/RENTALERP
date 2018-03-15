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
package com.kwoksys.framework.connections.ldap;

import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.biz.system.core.Keywords;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.logging.Logger;

/**
 * LDAP
 */
public class Ldap {

    private static final Logger LOGGER = Logger.getLogger(Ldap.class.getName());

    private String url;
    private String username;
    private String password;
    private String securityPrincipal;

    public ActionMessages authenticate() {
        if (securityPrincipal.isEmpty()) {
            securityPrincipal = username;
        } else {
            securityPrincipal = securityPrincipal.replace(Keywords.USER_USERNAME_VAR, username);
        }

        LOGGER.log(LogConfigManager.getLogLevel(LogConfigManager.AUTHENTICATION_PREFIX), LogConfigManager.AUTHENTICATION_PREFIX + " URL: " + url
                + ". Username: " + username
                + ". Security principal: " + securityPrincipal);

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        //env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, password);

        DirContext ctx = null;
        ActionMessages errors = new ActionMessages();

        try {
            // Create the initial context
            ctx = new InitialDirContext(env);
            LOGGER.info(LogConfigManager.AUTHENTICATION_PREFIX + " Login success for user: " + username);

        } catch (Exception e) {
            LOGGER.warning(LogConfigManager.AUTHENTICATION_PREFIX + " Login failed for user: " + username
                    + ". Class: " + e.getClass().getName() + ". Message: " + e.getMessage()
                    + ". Cause: " + e.getCause());

            errors.add("connectError", new ActionMessage("admin.config.auth.ldap.error.connect", username
                    + ". Message: " + e.getMessage()
                    + ". Cause: " + e.getCause()
                    + ". Class: " + e.getClass().getName()));

        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    /* ignored */
                }
            }
        }
        return errors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityPrincipal() {
        return securityPrincipal;
    }

    public void setSecurityPrincipal(String securityPrincipal) {
        this.securityPrincipal = securityPrincipal;
    }
}
