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

import com.kwoksys.action.home.IndexForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.AuthService;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.*;

/**
 * Action class for verifying user password.
 */
public class VerifyPasswordAction extends Action2 {

    public String execute() throws Exception {
        IndexForm actionForm = saveActionForm(new IndexForm());

        AccessUser user = new AccessUser();

        // Construct a username with domain
        String domain = actionForm.getDomain();
        if (domain == null) {
            domain = "";
        } else if (!domain.isEmpty()) {
            domain = "@" + domain;
        }

        user.setUsername(actionForm.getUsername().trim() + domain);
        user.setRequestedPassword(actionForm.getPassword().trim());

        // Get data access object
        AuthService authService = ServiceProvider.getAuthService(requestContext);
        ActionMessages errors = authService.authenticateUser(user);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.HOME_INDEX + "?" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Initialize user session
            authService.initializeUserSession(request, response, user);

            // See if redirectPath variable is available
            String redirectPath = actionForm.getRedirectPath().isEmpty() ? AppPaths.HOME_INDEX
                    : actionForm.getRedirectPath();

            return redirect(redirectPath);
        }
    }
}
