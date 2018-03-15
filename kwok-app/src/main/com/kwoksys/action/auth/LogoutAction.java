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
package com.kwoksys.action.auth;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.AuthService;
import com.kwoksys.biz.auth.core.AuthUtils;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for user logout.
 */
public class LogoutAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        // Clear the session token in the database table for this user.
        if (user.isLoggedOn()) {
            // Call the service
            AuthService authService = ServiceProvider.getAuthService(requestContext);

            authService.updateUserLogoutSession(user.getId());
        }

        // Empty all auth cookies
        AuthUtils.resetAuthCookies(response, user);

        // Purge cached perm
        new CacheManager(requestContext).removeUserPermissionsCache(user.getId());

        return redirect(AppPaths.HOME_INDEX);
    }
}
