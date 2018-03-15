<%--
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
--%>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th style="width:30%"><k:message key="admin.config.jvm.version"/>:</th>
        <td>${appVersion}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.system.licenseKey"/>:</th>
        <td><k:write value="${licenseKey}"/> (${appEdition})</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.applicationPath"/>:</th>
        <td><k:write value="${applicationUrl}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.locale"/>:</th>
        <td><k:message key="admin.config.locale.${locale}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.timezone"/>:</th>
        <td><k:write value="${timezoneLocal}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.timezoneOffset"/>:</th>
        <td><k:write value="${timezoneOffset}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.db.serverTime"/>:</th>
        <td>${serverTime}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.datetime.shortDateFormat"/>:</th>
        <td>${shortDateFormat}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.datetime.timeFormat"/>:</th>
        <td>${timeFormat}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.currency"/>:</th>
        <td><k:write value="${currencyOptions}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.numberOfPastYears"/>:</th>
        <td>${numberOfPastYearsToShow}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.numberOfFutureYears"/>:</th>
        <td>${numberOfFutureYearsToShow}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.users.displayOption"/>:</th>
        <td><k:message key="common.column.${usernameDisplay}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.users.numberOfRowsToShow"/>:</th>
        <td>${usersRowsToShow}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.blogs.numberOfPostsToShow"/>:</th>
        <td>${portal_numberOfBlogPostsToShowOnList}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.blogs.numberOfPostCharsToShow"/>:</th>
        <td>${portal_numberOfBlogPostCharactersOnList}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.customFields.expand"/>:</th>
        <td>${loadCustomFields}</td>
    </tr>
    <%-- Hardware module --%>
    <tr><td colspan="2">
        <h3><k:message key="core.moduleName.1"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.hardwareColumns"/>:</th>
        <td><k:write value="${hardwareColumns}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.hardware.numberOfRowsToShow"/>:</th>
        <td><k:write value="${hardwareRowsToShow}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.hardware.expireCountdown"/>:</th>
        <td>${hardwareExpirationCountdown}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.hardware.validateUniqueName"/>:</th>
        <td><k:message key="common.boolean.true_false.${checkUniqueHardwareName}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.hardware.validateSerialNumber"/>:</th>
        <td><k:message key="common.boolean.true_false.${checkUniqueSerialNumber}"/></td>
    </tr>
    
    <tr>
        <th><k:message key="admin.config.hardware.bulkDeleteEnabled"/>:</th>
        <td><k:message key="common.boolean.true_false.${bulkHardwareDelete}"/></td>
    </tr>
    
    <%-- Software module --%>
    <tr><td colspan="2">
        <h3><k:message key="core.moduleName.2"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.softwareColumns"/>:</th>
        <td>${softwareColumns}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.software.numberOfRowsToShow"/>:</th>
        <td>${softwareRowsToShow}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.software.licenseNotesCharacters"/>:</th>
        <td>${softwareLicneseNotesNumChars}</td>
    </tr>
    <%-- Issues module --%>
    <tr><td colspan="2">
        <h3><k:message key="core.moduleName.4"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.issues.columns"/>:</th>
        <td>${issuesColumns}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.issues.numberOfRowsToShow"/>:</th>
        <td>${issuesRowsToShow}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.issues.guestSubmitModuleEnabled"/>:</th>
        <td><k:message key="common.boolean.true_false.${issuesGuestSubmitModuleEnabled}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.issues.guestSubmitEnabled"/>:</th>
        <td><k:message key="common.boolean.true_false.${issuesGuestSubmitEnabled}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.issues.multipleIssueDeleteEnabled"/>:</th>
        <td><k:message key="common.boolean.true_false.${issuesMultipleDeleteEnabled}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.issues.defaultDueDate"/>:</th>
        <td>${issuesDefaultDueDate}</td>
    </tr>
    <%-- Contacts module --%>
    <tr><td colspan="2">
        <h3><k:message key="core.moduleName.5"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.companies.numberOfRowsToShow"/>:</th>
        <td>${companiesRowsToShow}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.contacts.numberOfRowsToShow"/>:</th>
        <td>${contactsRowsToShow}</td>
    </tr>
    <%-- Contracts module --%>
    <tr><td colspan="2">
        <h3><k:message key="core.moduleName.3"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.columnList"/>:</th>
        <td><k:write value="${contractsColumns}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.contracts.numberOfRowsToShow"/>:</th>
        <td><k:write value="${contractsRowsToShow}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.contracts.expireCountdown"/>:</th>
        <td><k:write value="${contractsExpirationCountdown}"/></td>
    </tr>
    <%-- Knowledge Base module --%>
    <tr><td colspan="2">
        <h3><k:message key="core.moduleName.14"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.columnList"/>:</th>
        <td>${kbColumns}</td>
    </tr>
    <tr>
        <th><k:message key="admin.config.kb.isMediaWikiSyntaxEnabled"/>:</th>
        <td><k:message key="common.boolean.true_false.${isMediaWikiSyntaxEnabled}"/></td>
    </tr>

    <%-- Reports module --%>
    <tr><td colspan="2">
            <h3><k:message key="core.moduleName.15"/>
                <k:notEmpty name="reportEditLink">
                    <span class="command"> [${reportEditLink}]</span>
                </k:notEmpty>
            </h3>
        </td>
    </tr>
    <tr>
        <th><k:message key="reports.filename" arg0="${hardwareReportFilenameHeader}"/>:</th>
        <td><k:write value="${hardwareReportFilename}"/></td>
    </tr>
    <tr>
        <th><k:message key="reports.filename" arg0="${hardwareLicensesReportFilenameHeader}"/>:</th>
        <td><k:write value="${hardwareLicensesReportFilename}"/></td>
    </tr>
    <tr>
        <th><k:message key="reports.filename" arg0="${hardwareMembersReportFilenameHeader}"/>:</th>
        <td><k:write value="${hardwareMembersReportFilename}"/></td>
    </tr>
    <tr>
        <th><k:message key="reports.filename" arg0="${softwareReportFilenameHeader}"/>:</th>
        <td><k:write value="${softwareReportFilename}"/></td>
    </tr>
    <tr>
        <th><k:message key="reports.filename" arg0="${softwareUsageReportFilenameHeader}"/>:</th>
        <td><k:write value="${softwareUsageReportFilename}"/></td>
    </tr>
    <tr>
        <th><k:message key="reports.filename" arg0="${issuesReportFilenameHeader}"/>:</th>
        <td><k:write value="${issuesReportFilename}"/></td>
    </tr>
    <tr>
        <th><k:message key="reports.filename" arg0="${contractsReportFilenameHeader}"/>:</th>
        <td><k:write value="${contractsReportFilename}"/></td>
    </tr>
</table>
