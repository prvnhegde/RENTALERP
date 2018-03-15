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
package com.kwoksys.biz.auth.core;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.reports.ReportUtils;
import com.kwoksys.framework.exceptions.DatabaseException;

/**
 * This class determines whether a user has permission to see a certain reports.
 */
public class ReportAccess {

    /**
     * Returns whether the given user has permission to see the given report type.
     * @param user
     * @param reportType
     * @return
     */
    public static boolean hasPermission(AccessUser user, String reportType) throws DatabaseException {
        if (reportType.equals(ReportUtils.ISSUE_REPORT_TYPE)) {
            return user.hasPermission(Permissions.ISSUE_READ_PERMISSION) ||
                    user.hasPermission(Permissions.ISSUE_READ_LIMITED_PERMISSION);

        } else if (reportType.equals(ReportUtils.HARDWARE_REPORT_TYPE)
                || reportType.equals(ReportUtils.HARDWARE_MEMBER_REPORT_TYPE)
                || reportType.equals(ReportUtils.HARDWARE_LICENSE_REPORT_TYPE)) {
            return user.hasPermission(Permissions.HARDWARE_READ_PERMISSION);

        } else if (reportType.equals(ReportUtils.SOFTWARE_REPORT_TYPE)
                || reportType.equals(ReportUtils.SOFTWARE_USAGE_REPORT_TYPE) ) {
            return user.hasPermission(Permissions.SOFTWARE_READ_PERMISSION);

        } else if (reportType.equals(ReportUtils.CONTRACT_REPORT_TYPE)) {
            return user.hasPermission(Permissions.CONTRACT_READ_PERMISSION);
        }
        return false;
    }
}
