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

<script type="text/javascript">
var perms = [${permissionList}];
</script>

<jsp:include page="/jsp/admin/manageusers/UserSpecTemplate.jsp"/>

<jsp:include page="/jsp/common/template/ActionError.jsp"/>

<jsp:include page="/jsp/common/template/Tabs.jsp"/>

<div class="tabBody">
<div class="themeHeader emptyHeader">&nbsp;</div>

<k:equal name="formDisabled" value="true">
    <div class="inactive section"><k:message key="admin.userAccessEdit.warning"/></div>
</k:equal>

<form action="${formAction}" method="post" id="userAccessEditForm" onsubmit="return App.submitFormUpdate(this, {'disable' : this.submitBtn})">
<table>
<k:equal name="cmd" value="edit">
<tr>
    <td><b><k:message key="form.button.selectAll"/></b></td>
    <td><k:foreach id="option" name="accessOptions">
            <input type="radio" name="selectall" value="${option.value}" onclick="App.selectAllAccessItems(this, perms)" ${formButtonDisabled}/>${option.label}&nbsp;
        </k:foreach>
    </td>
</tr>
</k:equal>
    
<k:foreach id="row" name="accessList" indexId="index">
<tr>
    <td>${row.accessText}</td>
    <td>
    <k:equal name="cmd" value="edit">
        <%-- Radio buttons here--%>
        <k:foreach id="subrow" name="accessOptions">
            <html:radio name="row" property="${row.accessName}" idName="subrow" value="value" disabled="${formDisabled}"/>
            ${subrow.label}&nbsp;
        </k:foreach>
    </k:equal>
    <k:equal name="cmd" value="detail">
        ${row.accessValue}
    </k:equal>
    &nbsp;</td>
</tr>
</k:foreach>

<k:equal name="cmd" value="edit">
    <tr>
        <td>&nbsp;</td>
        <td>
            <button type="submit" name="submitBtn" ${formButtonDisabled}><k:message key="form.button.save"/></button>
            ${formCancelLink}
        </td>
    </tr>
</k:equal>

</table>
</form>
</div>
