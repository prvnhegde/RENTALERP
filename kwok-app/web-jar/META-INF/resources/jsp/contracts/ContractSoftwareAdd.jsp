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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>

<jsp:include page="/jsp/contracts/ContractSpecTemplate.jsp"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<form action="${formSearchAction}" id="contractSoftwareForm" method="post" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="_resubmit" value="true">
<table class="listTable">
    <tr class="themeHeader">
        <td colspan="2"><b><k:message key="common.linking.linkSoftware"/></b></td>
    </tr>
    <tr>
        <th><k:message key="itMgmt.index.filterSoftware"/>:</th>
        <td><k:message key="common.column.software_id"/>&nbsp;<input type="text" name="formSoftwareId" value="<k:write value="${form.formSoftwareId}"/>" size="40" autofocus>
            <button type="submit" name="submitBtn"><k:message key="form.button.search"/></button>
        </td>
    </tr>
    <tr>
        <th><k:message key="contracts.softwareAdd.selectSoftware"/>:</th>
        <td>
            <k:notPresent name="softwareList">
                <k:message key="${selectSoftwareMessage}"/>
            </k:notPresent>
            <k:isPresent name="softwareList">
                <table class="noBorder">
                    <k:foreach id="software" name="softwareList">
                        <tr><td><html:checkbox name="form" property="softwareId" value="${software.softwareId}"/>
                            ${software.softwareName}</td></tr>
                    </k:foreach>
                </table>
            </k:isPresent>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>
            <button type="button" onclick="App.submitFormUpdate(this.form, {'url': '${formSaveAction}', 'disable': this})" ${disableSaveButton}><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
    </tr>
</table>
</form>
