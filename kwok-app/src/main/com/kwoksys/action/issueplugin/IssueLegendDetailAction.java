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

import com.kwoksys.action.common.template.HeaderSimpleTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AttributeSearch;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;

/**
 * Issue legend details
 */
public class IssueLegendDetailAction extends Action2 {

    public String execute() throws Exception {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put("attributeIdEquals", requestContext.getParameter("attributeId"));
        attributeSearch.put("attrFieldEnabled", true);
        attributeSearch.put("isEditable", true);

        QueryCriteria query = new QueryCriteria(attributeSearch);
        query.addSortColumn("attribute_field_name");

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("attributeFieldList", adminService.getEditAttributeFields(query));

        //
        // Template: HeaderSimpleTemplate
        //
        HeaderSimpleTemplate header = standardTemplate.addTemplate(new HeaderSimpleTemplate());
        header.setTitleText(Localizer.getText(requestContext, "issuePlugin.issueAdd.legendTitle"));
        header.setThemeTitleText(Localizer.getText(requestContext, "issuePlugin.issueAdd.legendTitle"));

        return standardTemplate.findTemplate(SUCCESS);
    }
}
