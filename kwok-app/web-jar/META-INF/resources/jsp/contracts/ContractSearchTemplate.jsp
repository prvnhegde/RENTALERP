<%--
 * Copyright 2015 Kwoksys
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

<k:define id="contractSearchTemplate" name="ContractSearchTemplate" type="com.kwoksys.action.contracts.ContractSearchTemplate"/>

<form action="${contractSearchTemplate.formAction}" method="post" name="ContractSearchForm" id="ContractSearchForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<input type="hidden" name="cmd" value="search">
<input type="hidden" name="_resubmit" value="true">
<input type="hidden" name="reportType" value="${reportType}">
<table class="standard section">
    <tr>
        <th><k:message key="common.column.contract_name"/>:</th>
        <td><html:text name="ContractSearchForm" property="contractName" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contract_description"/>:</th>
        <td><html:text name="ContractSearchForm" property="description" size="40"/></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contract_type"/>:</th>
        <td><html:select name="ContractSearchForm" property="contractTypeId">
            <html:options collection="contractTypeOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contract_stage"/>:</th>
        <td><html:select name="ContractSearchForm" property="stage">
            <html:options collection="contractStageOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <tr>
        <th><k:message key="common.column.contract_provider_name"/>:</th>
        <td><html:select name="ContractSearchForm" property="contractProviderId">
            <html:options collection="contractProviderOptions" property="value" labelProperty="label"/>
            </html:select></td>
    </tr>
    <k:notEmpty name="customFieldsOptions">
        <tr><th><k:message key="common.template.customFields"/>:</th>
            <td><html:select name="ContractSearchForm" property="attrId">
                <html:options collection="customFieldsOptions" property="value" labelProperty="label"/>
            </html:select> <html:text name="ContractSearchForm" property="attrValue" size="40"/>
        </td></tr>
    </k:notEmpty>
    <tr>
        <td colspan="2">
            <k:notEqual name="contractSearchTemplate" property="hideSearchButton" value="true">
                <button type="submit" name="submitBtn"><k:message key="form.button.search"/></button>
                &nbsp;&nbsp;${contractSearchTemplate.clearSearchLink}
            </k:notEqual>
        </td>
    </tr>
</table>
</form>
