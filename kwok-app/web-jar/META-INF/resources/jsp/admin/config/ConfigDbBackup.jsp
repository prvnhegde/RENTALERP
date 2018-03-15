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

<table class="standard">
    <tr>
        <th width="20%"><k:message key="admin.config.db.backup.pgDumpPath"/>:</th>
        <td width="80%"><k:write value="${backupCmdPath}"/>
            <k:equal name="validBackupCmdPath" value="false">
                <br><img src="${image.warning}" class="standard" alt="">
                <span class="error"><k:message key="admin.config.file.invalidPathWarning"/></span>
            </k:equal>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.db.backup.repositoryPath"/>:</th>
        <td><k:write value="${backupRepoPath}"/>
            <k:equal name="validBackupRepoPath" value="false">
                <br><img src="${image.warning}" class="standard" alt="">
                <span class="error"><k:message key="admin.config.file.invalidPathWarning"/></span>
            </k:equal>
        </td>
    </tr>
    <tr>
        <th><k:message key="admin.config.db.backup.cmdPath"/>:</th>
        <td><k:write value="${backupCmd}"/>
            <k:equal name="backupCmdEnabled" value="true">
                [${backupExecutePath}] <span id="backExecLoading">&nbsp;</span>
            </k:equal>
            <k:equal name="backupCmdEnabled" value="false">
                [<span class="inactive"><k:message key="admin.config.db.backup.execute.cmd"/></span>]
            </k:equal>
        </td>
    </tr>
</table>

<p>
<h3><k:message key="admin.config.db.backup.filesHeader"/></h3>
<table border="1" cellpadding="4">
    <tr>
        <th><k:message key="files.colName.file_name"/></th>
        <th><k:message key="files.colName.modification_date"/></th>
        <th><k:message key="files.colName.file_byte_size"/></th>
    </tr>
    <k:notEmpty name="backupFiles">
        <k:foreach id="file" name="backupFiles">
            <tr>
                <td><k:write value="${file.filename}"/></td>
                <td><k:write value="${file.fileModifiedDate}"/></td>
                <td><k:write value="${file.filesize}"/></td>
            </tr>
        </k:foreach>
    </k:notEmpty>
</table>
