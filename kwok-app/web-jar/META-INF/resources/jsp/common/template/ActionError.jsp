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

<k:isPresent name="ActionErrorsTemplate">
    <k:define id="actionErrorsTemplate" name="ActionErrorsTemplate" type="com.kwoksys.action.common.template.ActionErrorsTemplate"/>

    <p class="section">
    <k:notEmpty name="actionErrorsTemplate" property="message">
        ${actionErrorsTemplate.message}&nbsp;
    </k:notEmpty>

    <k:equal name="actionErrorsTemplate" property="showRequiredFieldMsg" value="true">
        <k:message key="requiredFieldBlurb"/>
    </k:equal>
</k:isPresent>

<k:notEmpty name="_errors">
<div class="error section"><b><k:message key="form.error.input"/></b>
    <ul>
        <k:foreach id="error" name="_errors">
            <li>${error}</li>
        </k:foreach>
    </ul>
</div><p>
</k:notEmpty>