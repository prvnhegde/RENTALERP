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

<table class="standard">
<k:foreach id="objectTypes" name="objectTypesMap">
    <tr><td width="30%">
        <h3><k:message key="common.objectType.${objectTypes.key}"/>&nbsp;
            <k:notEmpty name="attrAddPath">
                <span class="command">[<html:link action="${attrAddPath}${objectTypes.key}"><k:message key="admin.attributeAdd.command"/></html:link>]</span>
            </k:notEmpty>
            <k:notEmpty name="attrGroupAddPath">
                <span class="command">[<html:link action="${attrGroupAddPath}${objectTypes.key}"><k:message key="admin.attributeGroupAdd.command"/></html:link>]</span>
            </k:notEmpty>
        </h3>
        <blockquote>
        <k:foreach id="typeGroupsMap" name="objectTypes" property="value">
            <k:define id="groupMap" name="typeGroupsMap" property="value"/>
            <k:notEmpty name="typeGroupsMap" property="key">
            <h3>${typeGroupsMap.key}&nbsp;
                <k:isPresent name="groupMap" property="attrGroupEditLink">
                    <span class="command">[${groupMap.attrGroupEditLink}]</span>
                </k:isPresent>
                <k:isPresent name="groupMap" property="attrGroupDeleteLink">
                    <span class="command">[${groupMap.attrGroupDeleteLink}]</span>
                </k:isPresent>
            </h3>
            </k:notEmpty>
            <ul>
            <k:foreach id="attr" name="groupMap" property="customFields">
                <li>${attr.attrName}</li>
            </k:foreach>
            </ul>
        </k:foreach>
        </blockquote>
    </td></tr>
</k:foreach>
</table>
