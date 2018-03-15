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
package com.kwoksys.action.admin.attribute;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AttributeSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing attributes.
 */
public class AttributeListAction extends Action2 {

    public String execute() throws Exception {

        // Do some sorting.
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put(AttributeSearch.IS_EDITABLE, true);
        attributeSearch.put(AttributeSearch.IS_CUSTOM_ATTR, false);

        QueryCriteria queryCriteria = new QueryCriteria(attributeSearch);
        queryCriteria.addSortColumn(AdminQueries.getOrderByColumn(Attribute.ATTR_NAME));

        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        List<Map> attributeList = new ArrayList<>();

        for (Attribute attr : adminService.getAttributes(queryCriteria).values()) {
            Map<String, String> map = new HashMap<>();
            map.put("attributeName", new Link(requestContext).setAjaxPath(AppPaths.ADMIN_ATTRIBUTE_DETAIL + "?attributeId="
                            + attr.getId()).setEscapeTitle(Localizer.getText(requestContext, "common.objectType." + attr.getObjectTypeId())
                    + " &raquo; " + Localizer.getText(requestContext, "common.column." + attr.getName())).getString());
            attributeList.add(map);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        request.setAttribute("attributeList", attributeList);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.attributeList");

        // Back to admin home
        header.addNavLink(Links.getAdminHomeLink(requestContext));
        header.addNavLink(new Link(requestContext).setTitleKey("admin.attributeList"));

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}