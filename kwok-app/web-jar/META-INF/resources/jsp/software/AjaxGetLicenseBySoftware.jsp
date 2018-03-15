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

<k:isEmpty name="licenseOptions">
    <k:message key="itMgmt.hardwareDetail.noLicense"/>
</k:isEmpty>
<k:notEmpty name="licenseOptions">
    <html:select name="action" property="licenseId" value="${licenseId}">
        <html:options collection="licenseOptions" property="value" labelProperty="label" filter="false"/>
    </html:select>
</k:notEmpty>