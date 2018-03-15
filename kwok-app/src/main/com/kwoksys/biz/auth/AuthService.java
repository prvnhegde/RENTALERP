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
package com.kwoksys.biz.auth;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.AuthManager;
import com.kwoksys.biz.auth.core.AuthUtils;
import com.kwoksys.biz.auth.dao.AuthDao;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.ldap.Ldap;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.UsernameNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.CookieManager;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.framework.validations.ColumnField;
import com.kwoksys.framework.validations.InputValidator;

/**
 * AuthService
 */
public class AuthService {

    private RequestContext requestContext;

    public AuthService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public Set<Integer> getAccessGroupPerms(Integer groupId) throws DatabaseException {
        return new AuthDao(requestContext).getAccessGroupPerms(groupId);
    }

    public Set<Integer> getAccessUserPerms(Integer userId) throws DatabaseException {
        return new AuthDao(requestContext).getAccessUserPerms(userId);
    }

    public Map<Integer, Set<Integer>> getAccessPermPages() throws DatabaseException {
        return new AuthDao(requestContext).getAccessPermPages();
    }

    public Map getAccessPages() throws DatabaseException {
        return new AuthDao(requestContext).getAccessPages();
    }

    public ActionMessages updateUserLogoutSession(Integer userId) throws DatabaseException {
        return new AuthDao(requestContext).updateUserLogoutSession(userId);
    }

    public boolean isValidUserSession(Integer userId, String sessionTokenCookie) throws DatabaseException {
        // Make sure sessionUserId and sessionTokenCookie are not empty.
        if (sessionTokenCookie.isEmpty()) {
            return false;
        }

        return new AuthDao(requestContext).isValidUserSession(userId, sessionTokenCookie);
    }

    public ActionMessages authenticateUser(AccessUser accessUser) throws DatabaseException {
        AuthManager authManager = new AuthManager(requestContext, accessUser);

        ActionMessages errors = authManager.validateCredentialFormat();
        if (!errors.isEmpty()) {
            return errors;
        }

        // Check whether user is in database
        errors = authManager.validateUserName();
        if (!errors.isEmpty()) {
            return errors;
        }

        errors = authManager.validateUserStatus();
        if (!errors.isEmpty()) {
            return errors;
        }

        if (authManager.isConcurrentUsersLimitExceeded(accessUser)) {
            errors.add("login", new ActionMessage("auth.login.error.concurrentLimitExceeded"));
            return errors;
        }

        boolean isValidLogin = false;

        if (ConfigManager.auth.isDbAuthEnabled()) {
            try {
                isValidLogin = authManager.authenticateDbUser();
            } catch (Exception e) {
                errors.add("login", new ActionMessage("common.error.application"));
                return errors;
            }
        }
        if (!isValidLogin && ConfigManager.auth.isLdapAuthEnabled()) {
            isValidLogin = authManager.authenticateLdapUser();
        }

        AuthDao authDao = new AuthDao(requestContext);
        if (isValidLogin) {
            // No error, update user session
            authDao.updateUserLogonSession(accessUser, AuthUtils.generateRandomChars(13));

        } else {
            // Invalid user.
            int accountLockout = ConfigManager.admin.getAccountLockoutValue();
            int accountLockoutLimit = ConfigManager.admin.getAccountLockoutThreshold();
            boolean isValidateAcctLockout = ConfigManager.admin.isValidateAcctLockout();
            int accountLockoutDurationMinutes = ConfigManager.admin.getAccountLockoutDurationMinutes();

            if (isValidateAcctLockout) {
                int invalidLoginCount = accessUser.getInvalidLogonCount();
                if (invalidLoginCount == accountLockout) {
                    invalidLoginCount = 1;
                } else {
                    invalidLoginCount++;
                }

                if (invalidLoginCount >= accountLockoutLimit) {
                    invalidLoginCount = accountLockout;
                    errors.add("login", new ActionMessage("auth.login.error.accountLocked"));

                } else {
                    errors.add("login", new ActionMessage("auth.login.error.wrongCredentialLimit",
                            accountLockoutDurationMinutes, (accountLockoutLimit - invalidLoginCount)));
                }
                authDao.updateUserInvalidLogon(accessUser, invalidLoginCount);

            } else {
                // If account lockout validation is not on, just returns wrong credential message
                errors.add("login", new ActionMessage("auth.login.error.wrongCredential"));
            }
        }
        return errors;
    }

    /**
     * Test LDAP connection
     * @param user
     * @return
     * @throws DatabaseException
     */
    public ActionMessages testLdapConnection(String urlScheme, String url, String username, String password,
            String securityPrincipal) throws DatabaseException {

        ActionMessages errors = new ActionMessages();
        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("url").setTitleKey("admin.config.auth.ldapUrl")
                .calculateLength(url).setNullable(false));

        validator.validate(new ColumnField().setName("username").setTitleKey("common.column.username")
                .calculateLength(username).setNullable(false));

        validator.validate(new ColumnField().setName("password").setTitleKey("common.column.user_password")
                .calculateLength(password).setNullable(false));

        if (!errors.isEmpty()) {
            return errors;
        }

        // Check whether user is in database
        AccessUser appUser = new AccessUser();
        appUser.setUsername(username);
        appUser.setHashedPassword(password);

        try {
            AuthDao authDao = new AuthDao(requestContext);
            errors = authDao.isValidUsername(appUser);
        } catch (UsernameNotFoundException e) {
            errors.add("invalidUsername", new ActionMessage("admin.config.auth.ldap.error.invalidUsername"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        // User exists in the database, check account status
        if (appUser.isAccountDisabled()) {
            errors.add("accountDisabled", new ActionMessage("admin.config.auth.ldap.error.accountDisabled"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        Ldap ldap = new Ldap();
        ldap.setUrl(urlScheme + url);
        ldap.setUsername(username);
        ldap.setSecurityPrincipal(securityPrincipal);
        ldap.setPassword(password);

        return ldap.authenticate();
    }

    public boolean isValidBasicAuthentication(AccessUser user) throws Exception {
        String authHeaderRequest = requestContext.getRequest().getHeader("authorization");

        if (authHeaderRequest == null) {
            return false;
        }

        // The authorization header looks like this:
        // authorization: Basic xxxxxx
        // where xxxxxx is Base64 encoded and can be decoded to username:password format
        String authHeader = StringUtils.decodeBase64(authHeaderRequest.replaceFirst("Basic ", ""));
        String[] credential = authHeader.split(":");
        if (credential.length != 2) {
            return false;
        }

        user.setUsername(credential[0]);
        user.setRequestedPassword(credential[1]);

        return authenticateUser(user).isEmpty();
    }

    public void initializeUserSession(HttpServletRequest request, HttpServletResponse response, AccessUser user) {
        // Get current session.
        HttpSession session = request.getSession();

        // Get the current locale, for use later.
        Locale locale = (Locale)session.getAttribute(SessionManager.SESSION_LOCALE);

        // Get the current theme, for use later.
        String theme = (String)session.getAttribute(SessionManager.SESSION_THEME);

        // Invalidate previous session variables.
        session.invalidate();

        // Get a new session.
        session = request.getSession();

        // Reset the max session timeout.
        session.setMaxInactiveInterval(ConfigManager.auth.getSessionTimeoutSeconds());

        // Reset locale so that the user won't lose it.
        if (locale != null) {
            session.setAttribute(SessionManager.SESSION_INIT, true);
            session.setAttribute(SessionManager.SESSION_LOCALE, locale);
        }

        // Reset theme so that the user won't lose it.
        if (theme != null) {
            session.setAttribute(SessionManager.SESSION_THEME, theme);
        }

        // Do some cookie tricks here.
        CookieManager.setUserId(response, String.valueOf(user.getId()));
        CookieManager.setSessionToken(response, user.getSessionToken());
    }
}
