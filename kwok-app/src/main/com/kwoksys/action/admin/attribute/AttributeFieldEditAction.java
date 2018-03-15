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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for editing attribute.
 */
public class AttributeFieldEditAction extends Action2 {

    public String edit() throws Exception {
        AttributeForm actionForm = getBaseForm(AttributeForm.class);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        AttributeField attrField = adminService.getAttributeField(actionForm.getAttrFieldId());
        Attribute attr = adminService.getSystemAttribute(attrField.getAttributeId());

        actionForm.setAttributeId(attrField.getAttributeId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setAttributeField(attrField);

            // Get a list of attributes supposed to be shown for this attribute type
            actionForm.setCustomAttrs(adminService.getSavedAttributeFieldTypes(attrField.getId()));
        }

        if (AdminUtils.isAttributeTypeMappingEnabled(attrField.getAttributeId())) {
            List<Map<String, Object>> customAttrs = new ArrayList<>();
            for (Attribute custAttr : new AttributeManager(requestContext).getCustomFieldList(attr.getObjectTypeId())) {
                Map<String, Object> map = new HashMap<>();
                map.put("attr", custAttr);
                map.put("checked", actionForm.getCustomAttrs().contains(custAttr.getId()) ? "checked" : "");
                customAttrs.add(map);
            }
            request.setAttribute("customAttrs", customAttrs);
        }
        
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_ATTR_FIELD_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_ATTRIBUTE_DETAIL
                + "?attributeId=" + attr.getId()).getString());
        request.setAttribute("attribute", attr);
        request.setAttribute("iconList", adminService.getIcons(attr.getId()));
        request.setAttribute("statusList", AdminUtils.getAttributeStatusList(requestContext,
                attr.getDefaultAttrFieldId().equals(attrField.getId())));

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        AttributeForm actionForm = saveActionForm(new AttributeForm());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        Attribute attr = adminService.getSystemAttribute(actionForm.getAttributeId());

        AttributeField attrField = new AttributeField();
        attrField.setAttributeId(actionForm.getAttributeId());
        attrField.setId(actionForm.getAttrFieldId());
        attrField.setName(actionForm.getAttributeFieldName());
        attrField.setDescription(actionForm.getAttributeFieldDescription());
        attrField.setIconId(actionForm.getIconId());
        attrField.setDisabled(actionForm.getDisabled());
        attrField.setLinkedAttrIds(actionForm.getCustomAttrs());

        ActionMessages errors = adminService.updateAttributeField(attrField);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_ATTR_FIELD_EDIT + "?attrFieldId=" + actionForm.getAttrFieldId() + "&" 
                    + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Remove the cache
            new CacheManager(requestContext).removeAttributeFieldsCache(attr.getId());

            return ajaxUpdateView(AppPaths.ADMIN_ATTRIBUTE_DETAIL + "?attributeId=" + attr.getId());
        }
    }
}
