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

<k:define id="form" name="form" type="com.kwoksys.action.home.IndexForm"/>

<table>
    <tr>
    <td style="width:100%; vertical-align:top">
        <div class="section">
            <%-- We let users customize this home page  --%>
            <k:isEmpty name="homeCustomDescription">
                <h2 class="noLine"><k:message key="common.app.name"/></h2>
                <p><k:message key="common.app.description"/></p>
            </k:isEmpty>
            <k:notEmpty name="homeCustomDescription">
                ${homeCustomDescription}
            </k:notEmpty>
        </div>
    </td>

    <td style="width:340px; vertical-align:top; text-align:center">
        <%-- If user is not logged in, show login form. --%>
        <k:equal name="isUserLoggedOn" value="false">
        <form action="${formLoginAction}" name="loginForm" id="loginForm" method="post" onsubmit="return App.disableButtonSubmit(this.submitBtn)">
            <%-- Show redirect path if available--%>
            <html:hidden name="form" property="redirectPath"/>
            <div class="row1 userInfo">
                <k:notEmpty name="_errors">
                    <p><span class="error">
                        <k:foreach id="error" name="_errors">
                            ${error}
                        </k:foreach>
                    </span>
                </k:notEmpty>
                <p><k:message key="common.column.username"/>:
                <br/><html:text name="form" property="username" size="30" style="width:260px; padding:6px"/>
                
                <p><k:message key="common.column.user_password"/>:
                <br/><input type="password" name="password" size="30" style="width:260px; padding:6px"/>
                
                <k:notEmpty name="domain">
                    <p><k:message key="auth.login.domain"/>:
                    <br/><html:select name="form" property="domain" style="width:274px; padding:6px">
                    <html:options collection="domainOptions" property="value" labelProperty="label"/>
                    </html:select>
                </k:notEmpty>

                <p><button type="submit" name="submitBtn" class="loginButton">
                        <k:message key="home.index.sectionLoginButton"/>
                    </button>

                <p><span class="formFieldDesc"><k:message key="home.index.sectionLoginDesc"/></span>
            </div>
        </form>
        </k:equal>
        <%-- If user is logged in, show user info. --%>
        <k:equal name="isUserLoggedOn" value="true">
            <div class="themeHeader standardHeader"><k:message key="home.index.sectionUserHeader"/>: <k:write value="${user.displayName}"/></div>
            <div class="row1 userInfo">
                <ul class="hideIcon">
                <li>
                    <span class="header3"><b><k:message key="common.column.username"/></b>:</span>
                    <k:write value="${user.username}"/>
                <li>
                    <span class="header3"><b><k:message key="common.column.user_email"/></b>:</span>
                    ${user.email}
                <li>
                    <span class="header3"><b><k:message key="common.column.user_member_since"/></b>:</span>
                    ${user.creationDate}
                </ul>
            </div>
        </k:equal>

        <%-- Features --%>
        <p>
        <form action="${formAction}" method="get">
        <div class="row1 userInfo">
                <k:message key="home.index.localeHeader"/>:
                <br><html:select name="form" property="locale" onchange="App.changeLocaleSelectedOption(this);" style="width:274px; padding:6px">
                    <html:options collection="localeOptions" property="value" labelProperty="label"/>
                </html:select>
                <p>
                <k:message key="home.index.themeHeader"/>:
                <k:foreach id="themeLink" name="themeLinks">
                    ${themeLink}&nbsp;
                </k:foreach>
                <p>
                <k:message key="home.index.fontSizesHeader"/>:
                <k:foreach id="fontLink" name="fontLinks">
                    ${fontLink}&nbsp;
                </k:foreach>
        </div>
        </form>
    </td>
    </tr>
</table>
