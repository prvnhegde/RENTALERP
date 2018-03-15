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
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for attribute detail.
 */
public class AttributeDetailAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        Attribute attr = adminService.getSystemAttribute(requestContext.getParameter("attributeId"));

        AttributeManager attributeManager = new AttributeManager(requestContext);

        boolean canEditAttrField = user.hasPermission(AppPaths.ADMIN_ATTR_FIELD_EDIT);

        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put(AttributeSearch.ATTRIBUTE_ID_EQUALS, attr.getId());
        attributeSearch.put(AttributeSearch.IS_EDITABLE, true);

        QueryCriteria queryCriteria = new QueryCriteria(attributeSearch);
        queryCriteria.addSortColumn(AdminQueries.getOrderByColumn(AttributeField.NAME));

        List<Map> attrFields = new ArrayList<>();
        List<Map> attrFieldsDisabled = new ArrayList<>();

        for (AttributeField attrField : adminService.getEditAttributeFields(queryCriteria).values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("attrFieldName", Links.getAttrFieldIcon(requestContext, attrField));
            map.put("attrFieldDescription", HtmlUtils.formatMultiLineDisplay(attrField.getDescription()));
            map.put("attrFieldDefault", attr.getDefaultAttrFieldId().equals(attrField.getId()));

            if (canEditAttrField && (attrField.getId() > 0)) {
                map.put("attributeEditPath", "[" + new Link(requestContext).setAjaxPath(AppPaths.ADMIN_ATTR_FIELD_EDIT
                        + "?attrFieldId=" + attrField.getId()).setTitleKey("common.command.Edit") + "]");
            }
            
            if (attrField.isDisabled()) {
                attrFieldsDisabled.add(map);
            } else {
                attrFields.add(map);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        standardTemplate.setAttribute("attribute", attr);
        standardTemplate.setAttribute("attrFieldList", attrFields);
        standardTemplate.setAttribute("attrFieldDisabledList", attrFieldsDisabled);

        if (attr.isRequiredFieldEditable()) {
            request.setAttribute("attributeIsRequired", "common.boolean.yes_no." + attr.isRequired());
        }

        if (attr.isDefaultAttrFieldEditable()) {
            request.setAttribute("defaultAttrField", attributeManager.getAttrFieldMapCache(attr.getId()).get(attr.getDefaultAttrFieldId()));
        }

        // Temporary workaround to disable add dropdown for issue_assignee
        request.setAttribute("canAddAttrField", !attr.getId().equals(Attributes.ISSUE_ASSIGNEE));

        if (user.hasPermission(AppPaths.ADMIN_ATTR_FIELD_ADD)) {
            request.setAttribute("attributeFieldAddPath", "[" + new Link(requestContext).setAjaxPath(AppPaths.ADMIN_ATTR_FIELD_ADD
                    + "?attributeId=" + attr.getId()).setTitleKey("common.command.Add") + "]");
        }

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();

        if (user.hasPermission(AppPaths.ADMIN_ATTRIBUTE_EDIT)
                && (attr.isRequiredFieldEditable() || attr.isDefaultAttrFieldEditable())) {
            header.addHeaderCmds(new Link(requestContext)
                    .setAjaxPath(AppPaths.ADMIN_ATTRIBUTE_EDIT + "?attributeId=" + attr.getId())
                    .setTitleKey("admin.attributeEdit.header"));
        }

        header.addNavLink(Links.getAdminHomeLink(requestContext));

        if (user.hasPermission(AppPaths.ADMIN_ATTRIBUTE_LIST)) {
            header.addNavLink(new Link(requestContext)
                    .setAjaxPath(AppPaths.ADMIN_ATTRIBUTE_LIST)
                    .setTitleKey("admin.attributeList"));
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}