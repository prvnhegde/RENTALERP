<%--
 * Copyright 2017 Kwoksys
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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formAction}" method="post" id="configAppForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="cmd" value="${cmd}"/>
<table class="${StandardTemplate.detailsTableStyle}">
    <tr>
        <th><k:message key="admin.config.system.licenseKey"/>:</th>
        <td><html:text name="form" property="licenseKey" size="60"/>
            <br><span class="formFieldDesc"><k:write value="${licenseKeyExample}"/></span>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.applicationPath"/>:</th>
        <td><html:text name="form" property="applicationUrl" size="60"/>
            <br><span class="formFieldDesc"><k:message key="admin.configDesc.url.application"/></span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.locale"/>:</th>
        <td><html:select name="form" property="locale">
            <html:options collection="localeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.timezone"/>:</th>
        <td><html:select name="form" property="timezone">
            <html:options collection="timezoneOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.datetime.shortDateFormat"/>:</th>
        <td><html:select name="form" property="shortDateFormat">
            <html:options collection="shortDateFormatOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.datetime.timeFormat"/>:</th>
        <td><html:select name="form" property="timeFormat">
            <html:options collection="timeFormatOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.currency"/>:</th>
        <td><html:text name="form" property="currency" size="8"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.numberOfPastYears"/>:</th>
        <td><html:text name="form" property="numberOfPastYears" size="8"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.numberOfFutureYears"/>:</th>
        <td><html:text name="form" property="numberOfFutureYears" size="8"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.users.displayOption"/>:</th>
        <td><html:select name="form" property="userNameDisplay">
            <html:options collection="userNameDisplayOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.users.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="usersNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.blogs.numberOfPostsToShow"/>:</th>
        <td><html:select name="form" property="blogPostsListNumRows">
            <html:options collection="numBlogPostsOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.blogs.numberOfPostCharsToShow"/>:</th>
        <td><html:select name="form" property="blogPostCharactersList">
            <html:options collection="numBlogPostCharactersOptions" property="value" labelProperty="label"/>
            </html:select>
            <br><span class="formFieldDesc"><k:message key="admin.configDesc.portal.numberOfBlogPostCharacters"/></span>
            </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.customFields.expand"/>:</th>
        <td><html:select name="form" property="loadCustomFields">
            <html:options collection="booleanOptions" property="value" labelProperty="label"/>
            </html:select>
            <br><span class="formFieldDesc"><k:message key="admin.config.customFields.expand.description"/></span>
        </td>
    </tr>
    <%-- Hardware module --%>
    <tr><td colspan="2">
        <br><h3><k:message key="core.moduleName.1"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.hardwareColumns"/>:</th>
        <td><k:foreach id="column" name="hardwareColumnOptions">
            <k:isPresent name="column" property="disabled">
                <input type="hidden" name="hardwareColumns" value="${column.name}">
            </k:isPresent>

            <input type="checkbox" name="hardwareColumns" value="${column.name}" ${column.checked} ${column.disabled}/>
            <k:message key="common.column.${column.name}"/>&nbsp;
            </k:foreach></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.hardware.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="hardwareNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.hardware.expireCountdown"/>:</th>
        <td><html:select name="form" property="hardwareExpireCountdown">
            <html:options collection="expireCountdownOptions" property="value" labelProperty="label"/>
            </html:select>
            <br><span class="formFieldDesc"><k:message key="admin.config.contracts.expireCountdown.desc"/></span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.hardware.validateUniqueName"/>:</th>
        <td><html:checkbox name="form" property="checkUniqueHardwareName" value="true"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.hardware.validateSerialNumber"/>:</th>
        <td><html:checkbox name="form" property="checkUniqueSerialNumber" value="true"/>
            <br><span class="formFieldDesc"><k:message key="admin.config.hardware.validateSerialNumber.description"/></span></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.hardware.bulkDeleteEnabled"/>:</th>
        <td><html:select name="form" property="bulkHardwareDeleteEnabled">
            <html:options collection="booleanOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>

    <%-- Software module --%>
    <tr><td colspan="2">
        <br><h3><k:message key="core.moduleName.2"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.softwareColumns"/>:</th>
        <td><k:foreach id="column" name="softwareColumnOptions">
            <k:isPresent name="column" property="disabled">
                <input type="hidden" name="softwareColumns" value="${column.name}">
            </k:isPresent>

            <input type="checkbox" name="softwareColumns" value="${column.name}" ${column.checked} ${column.disabled}/>
            <k:message key="common.column.${column.name}"/>&nbsp;
            </k:foreach></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.software.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="softwareNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.software.licenseNotesCharacters"/>:</th>
        <td><html:text name="form" property="softwareLicneseNotesNumChars" size="4"/>
            <br><span class="formFieldDesc"><k:message key="admin.config.software.licenseNotesCharacters.desc"/></span>
        </td>
    </tr>    
    <%-- Issues module --%>
    <tr><td colspan="2">
        <br><h3><k:message key="core.moduleName.4"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.issues.columns"/>:</th>
        <td><k:foreach id="column" name="issuesColumnOptions">
            <k:isPresent name="column" property="disabled">
                <input type="hidden" name="issuesColumns" value="${column.name}">
            </k:isPresent>

            <input type="checkbox" name="issuesColumns" value="${column.name}" ${column.checked} ${column.disabled}/>
            <k:message key="common.column.${column.name}"/>&nbsp;
            </k:foreach></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.issues.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="issuesNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.issues.guestSubmitModuleEnabled"/>:</th>
        <td><html:select name="form" property="issuesGuestSubmitModuleEnabled">
            <html:options collection="booleanOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.issues.guestSubmitEnabled"/>:</th>
        <td><html:select name="form" property="issuesGuestSubmitEnabled">
            <html:options collection="booleanOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.issues.multipleIssueDeleteEnabled"/>:</th>
        <td><html:select name="form" property="issuesMultipleDeleteEnabled">
            <html:options collection="booleanOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.issues.defaultDueDate"/>:</th>
        <td><html:text name="form" property="issuesDefaultDueDateDiff" size="2" maxlength="2"/></td>
    </tr>
    <%-- Contacts module --%>
    <tr><td colspan="2">
        <br><h3><k:message key="core.moduleName.5"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.companies.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="companiesNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.contacts.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="contactsNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>    
    <%-- Contracts module --%>
    <tr><td colspan="2">
        <br><h3><k:message key="core.moduleName.3"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.columnList"/>:</th>
        <td><k:foreach id="column" name="contractsColumnOptions">
            <k:isPresent name="column" property="disabled">
                <input type="hidden" name="contractColumns" value="${column.name}">
            </k:isPresent>

            <input type="checkbox" name="contractColumns" value="${column.name}" ${column.checked} ${column.disabled}/>
            <k:message key="common.column.${column.name}"/>&nbsp;
            </k:foreach></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.contracts.numberOfRowsToShow"/>:</th>
        <td><html:select name="form" property="contractsNumRows">
            <html:options collection="numrowOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.contracts.expireCountdown"/>:</th>
        <td><html:select name="form" property="contractsExpireCountdown">
            <html:options collection="expireCountdownOptions" property="value" labelProperty="label"/>
            </html:select>
            <br><span class="formFieldDesc"><k:message key="admin.config.contracts.expireCountdown.desc"/></span></td>
    </tr>
    <%-- KB module --%>
    <tr><td colspan="2">
        <br><h3><k:message key="core.moduleName.14"/></h3>
    </td></tr>
    <tr>
        <th><k:message key="admin.config.columnList"/>:</th>
        <td><k:foreach id="column" name="kbColumnOptions">
            <k:isPresent name="column" property="disabled">
                <input type="hidden" name="kbColumns" value="${column.name}">
            </k:isPresent>

            <input type="checkbox" name="kbColumns" value="${column.name}" ${column.checked} ${column.disabled}/>
            <k:message key="common.column.${column.name}"/>&nbsp;
            </k:foreach></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.kb.isMediaWikiSyntaxEnabled"/>:</th>
        <td>
            <html:select name="form" property="kbMediaWikiSyntaxEnabled">
            <html:options collection="booleanOptions" property="value" labelProperty="label"/>
            </html:select>
        </td>
    <tr>
        <td style="width:30%">&nbsp;</td>
        <td><button type="submit" name="submitBtn"><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
