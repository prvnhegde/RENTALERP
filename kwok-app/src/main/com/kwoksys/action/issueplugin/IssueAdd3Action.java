/*
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
 */
package com.kwoksys.action.issueplugin;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding issue.
 */
public class IssueAdd3Action extends Action2 {

    public String execute() throws Exception {
        Integer issueId = requestContext.getParameter("issueId");

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("issueId", issueId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("issuePlugin.issueAdd3.header");
        header.setSectionKey("issuePlugin.issueAdd3.thankYou", new String[]{String.valueOf(issueId)});

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
}
