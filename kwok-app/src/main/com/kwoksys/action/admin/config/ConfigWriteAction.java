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

import java.io.FileNotFoundException;

import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for admin commands.
 */
public class ConfigWriteAction extends Action2 {

    public String execute() throws Exception {
        String cmd = requestContext.getParameterString("cmd");

        if (cmd.equals(AdminUtils.ADMIN_APP_EDIT_CMD)) {
            return ((ConfigAppAction) new ConfigAppAction().setRequestContext(requestContext)).edit();

        } else if (cmd.equals(AdminUtils.ADMIN_APP_EDIT_2_CMD)) {
            return ((ConfigAppAction) new ConfigAppAction().setRequestContext(requestContext)).edit2();

        } else if (cmd.equals(AdminUtils.ADMIN_AUTH_EDIT_CMD)) {
            return ((ConfigAuthAction) new ConfigAuthAction().setRequestContext(requestContext)).edit();

        } else if (cmd.equals(AdminUtils.ADMIN_AUTH_EDIT_2_CMD)) {
            return ((ConfigAuthAction) new ConfigAuthAction().setRequestContext(requestContext)).edit2();

        } else if (cmd.equals(AdminUtils.ADMIN_DB_BACKUP_EDIT_CMD)) {
            return ((ConfigBackupAction) new ConfigBackupAction().setRequestContext(requestContext)).edit();

        } else if (cmd.equals(AdminUtils.ADMIN_DB_BACKUP_EDIT_2_CMD)) {
            return ((ConfigBackupAction) new ConfigBackupAction().setRequestContext(requestContext)).edit2();

        } else if (cmd.equals(AdminUtils.ADMIN_DB_BACKUP_EXECUTE)) {
            return ((ConfigBackupAction) new ConfigBackupAction().setRequestContext(requestContext)).execute();

        } else if (cmd.equals(AdminUtils.ADMIN_COMPANY_EDIT_CMD)) {
            return ((ConfigCompanyAction) new ConfigCompanyAction().setRequestContext(requestContext)).edit();

        } else if (cmd.equals(AdminUtils.ADMIN_COMPANY_EDIT_2_CMD)) {
            return ((ConfigCompanyAction) new ConfigCompanyAction().setRequestContext(requestContext)).edit2();

        } else if (cmd.equals(AdminUtils.ADMIN_EMAIL_SMTP_EDIT_CMD)) {
            return ((ConfigEmailSmtpAction) new ConfigEmailSmtpAction().setRequestContext(requestContext)).edit();

        } else if (cmd.equals(AdminUtils.ADMIN_EMAIL_SMTP_EDIT_2_CMD)) {
            return ((ConfigEmailSmtpAction) new ConfigEmailSmtpAction().setRequestContext(requestContext)).edit2();

        } else if (cmd.equals(AdminUtils.ADMIN_FILE_EDIT_CMD)) {
            return ((ConfigFileAction) new ConfigFileAction().setRequestContext(requestContext)).edit();

        } else if (cmd.equals(AdminUtils.ADMIN_FILE_EDIT_2_CMD)) {
            return ((ConfigFileAction) new ConfigFileAction().setRequestContext(requestContext)).edit2();

        } else if (cmd.equals(AdminUtils.ADMIN_LOGGING_EDIT_CMD)) {
            return ((ConfigLoggingAction) new ConfigLoggingAction().setRequestContext(requestContext)).edit();

        } else if (cmd.equals(AdminUtils.ADMIN_LOGGING_EDIT_2_CMD)) {
            return ((ConfigLoggingAction) new ConfigLoggingAction().setRequestContext(requestContext)).edit2();

        } else if (cmd.equals(AdminUtils.ADMIN_LOOK_FEEL_EDIT_CMD)) {
            return ((ConfigLookAction) new ConfigLookAction().setRequestContext(requestContext)).edit();

        } else if (cmd.equals(AdminUtils.ADMIN_LOOK_FEEL_EDIT_2_CMD)) {
            return ((ConfigLookAction) new ConfigLookAction().setRequestContext(requestContext)).edit2();

        } else if (cmd.equals(AdminUtils.ADMIN_EMAIL_POP_EDIT_CMD)) {
            return ((ConfigEmailPopAction) new ConfigEmailPopAction().setRequestContext(requestContext)).edit();

        } else if (cmd.equals(AdminUtils.ADMIN_EMAIL_POP_EDIT_2_CMD)) {
            return ((ConfigEmailPopAction) new ConfigEmailPopAction().setRequestContext(requestContext)).edit2();
        }

        throw new FileNotFoundException();
    }
}