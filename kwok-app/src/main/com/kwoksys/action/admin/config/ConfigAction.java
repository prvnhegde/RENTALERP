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

import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.framework.struts2.Action2;

import java.io.FileNotFoundException;

/**
 * Action class for admin commands.
 */
public class ConfigAction extends Action2 {

    public String execute() throws Exception {
        String cmd = requestContext.getParameterString("cmd");

        if (cmd.equals(AdminUtils.ADMIN_APP_CMD)) {
            return ((ConfigAppAction) new ConfigAppAction().setRequestContext(requestContext)).index();

        } else if (cmd.equals(AdminUtils.ADMIN_AUTH_CMD)) {
            return ((ConfigAuthAction) new ConfigAuthAction().setRequestContext(requestContext)).index();

        } else if (cmd.equals(AdminUtils.ADMIN_DB_BACKUP_CMD)) {
            return ((ConfigBackupAction) new ConfigBackupAction().setRequestContext(requestContext)).index();

        } else if (cmd.equals(AdminUtils.ADMIN_DB_SEQUENCES_CMD)) {
            return new AjaxConfigDbSequencesAction().setRequestContext(requestContext).execute();

        } else if (cmd.equals(AdminUtils.ADMIN_COMPANY_CMD)) {
            return ((ConfigCompanyAction) new ConfigCompanyAction().setRequestContext(requestContext)).index();

        } else if (cmd.equals(AdminUtils.ADMIN_EMAIL_CMD)) {
            return ((ConfigEmailAction) new ConfigEmailAction().setRequestContext(requestContext)).index();

        } else if (cmd.equals(AdminUtils.ADMIN_FILE_CMD)) {
            return ((ConfigFileAction) new ConfigFileAction().setRequestContext(requestContext)).index();

        } else if (cmd.equals(AdminUtils.ADMIN_LDAP_TEST_CMD)) {
            return ((ConfigLdapTestAction) new ConfigLdapTestAction().setRequestContext(requestContext)).test();

        } else if (cmd.equals(AdminUtils.ADMIN_LDAP_TEST_2_CMD)) {
            return ((ConfigLdapTestAction) new ConfigLdapTestAction().setRequestContext(requestContext)).test2();

        } else if (cmd.equals(AdminUtils.ADMIN_LOOK_FEEL_CMD)) {
            return ((ConfigLookAction) new ConfigLookAction().setRequestContext(requestContext)).index();

        } else if (cmd.equals(AdminUtils.ADMIN_SYSTEM_INFO_CMD)) {
            return new ConfigSystemAction().setRequestContext(requestContext).execute();
        }

        throw new FileNotFoundException();
    }
}
