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
<%@ page contentType="text/xml; charset=UTF-8" %>
<%@ taglib uri="http://www.kwoksys.com/tags" prefix="k" %>
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>

<k:define id="rootTemplate" name="StandardTemplate" type="com.kwoksys.action.common.template.RootTemplate"/>

<response>
    <result><![CDATA[
        <jsp:include page="${rootTemplate.ajaxTemplatePath}"/>
        ]]>
    </result>
</response>