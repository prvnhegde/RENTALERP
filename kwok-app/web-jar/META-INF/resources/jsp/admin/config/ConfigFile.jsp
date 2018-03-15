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
        <th style="width:30%"><k:message key="admin.config.file.maxUploadSize"/>:</th>
        <td><k:write value="${maxFileUploadSize}"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.file.kilobyte"/>:</th>
        <td><k:write value="${kilobyteUnits}"/>&nbsp;<k:message key="files.colData.file_size.bytes"/></td>
    </tr>
    <tr>
        <th><k:message key="admin.config.file.company.repositoryPath"/>:</th>
        <td><k:write value="${companyRepositoryPath}"/>
            <k:equal name="validCompanyRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <k:message key="admin.configWarning.fileRepositoryPath"/></span>
            </k:equal>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.file.issue.repositoryPath"/>:</th>
        <td><k:write value="${issueRepositoryPath}"/>
            <k:equal name="validIssueRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <k:message key="admin.configWarning.fileRepositoryPath"/></span>
            </k:equal>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.file.hardware.repositoryPath"/>:</th>
        <td><k:write value="${hardwareRepositoryPath}"/>
            <k:equal name="validHardwareRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <k:message key="admin.configWarning.fileRepositoryPath"/></span>
            </k:equal>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.file.software.repositoryPath"/>:</th>
        <td><k:write value="${softwareRepositoryPath}"/>
            <k:equal name="validSoftwareRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <k:message key="admin.configWarning.fileRepositoryPath"/></span>
            </k:equal>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.file.contract.repositoryPath"/>:</th>
        <td><k:write value="${contractRepositoryPath}"/>
            <k:equal name="validContractRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <k:message key="admin.configWarning.fileRepositoryPath"/></span>
            </k:equal>
        </td>
    </tr>
        <tr>
        <th><k:message key="admin.config.file.kb.repositoryPath"/>:</th>
        <td><k:write value="${kbRepositoryPath}"/>
            <k:equal name="validKbRepositoryPath" value="false">
                <br>
                <span class="error"><img src="${image.warning}" class="standard" alt="">
                    <k:message key="admin.configWarning.fileRepositoryPath"/></span>
            </k:equal>
        </td>
    </tr>
</table>
