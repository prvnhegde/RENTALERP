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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Keywords;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.WidgetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;

/**
 * Action class for editing attribute.
 */
public class CustomAttributeEditAction extends Action2 {

    public String edit() throws Exception {
        CustomAttributeForm actionForm = getBaseForm(CustomAttributeForm.class);

        // Make sure the object exists
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        Attribute attr = adminService.getCustomAttribute(actionForm.getAttrId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setAttribute(attr);

            // Get a list of System Fields supposed to be for this attribute id
            actionForm.getSystemFieldIds().addAll(adminService.getAttributeFieldTypesByField(attr.getId()));
        }

        Integer objectAttrType = AdminUtils.getObjectTypeMap().get(attr.getObjectTypeId());

        // Get a list of System Fields for a particular object type
        if (AdminUtils.isAttributeTypeMappingEnabled(objectAttrType)) {
            List<Map> systemFields = new ArrayList<>();
            for (AttributeField attrField : new CacheManager(requestContext).getAttributeFieldsCache(objectAttrType).values()) {
                Map<String, Object> map = new HashMap<>();
                map.put("fieldName", attrField.getName());
                map.put("fieldId", attrField.getId());
                map.put("checked", actionForm.getSystemFieldIds().contains(attrField.getId()) ? "checked" : "");
                systemFields.add(map);
            }
            request.setAttribute("systemFields", systemFields);
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CUSTOM_ATTR_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CUSTOM_ATTR_DETAIL + "?attrId=" + attr.getId()).getString());
        request.setAttribute("attrTypeId", actionForm.getAttrType());
        request.setAttribute("attrTypeOptions", AttributeManager.getAttrDataTypeOptions(requestContext));
        request.setAttribute("attrConvertUrlOptions", WidgetUtils.getYesNoOptions(requestContext));
        request.setAttribute("attrGroupOptions", AdminUtils.getAttributeGroupOptions(requestContext, attr.getObjectTypeId()));
        request.setAttribute("attrUrlExample", Keywords.CUSTOM_FIELD_VALUE_VAR_EXAMPLE);
        standardTemplate.setAttribute("isRequiredOptions", WidgetUtils.getYesNoOptions(requestContext));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.customAttrEdit.header");
        headerTemplate.setOnloadJavascript("App.updateAttrOptions(" +attr.getType() + ");");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String edit2() throws Exception {
        CustomAttributeForm actionForm = saveActionForm(new CustomAttributeForm());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Make sure the object exists
        Attribute attr = adminService.getCustomAttribute(actionForm.getAttrId());
        attr.setName(actionForm.getAttrName());
        attr.setDescription(actionForm.getDescription());
        attr.setType(actionForm.getAttrType());
        attr.setAttributeOption(actionForm.getAttrOption());
        attr.setConvertUrl(actionForm.getAttrConvertUrl());
        attr.setUrl(actionForm.getAttrUrl());
        attr.setAttributeGroupId(actionForm.getAttrGroupId());
        attr.setInputMask(actionForm.getInputMask());
        attr.setAttrFieldIds(actionForm.getSystemFieldIds());
        attr.setTypeCurrencySymbol(actionForm.getCurrencySymbol());
        attr.setRequired(actionForm.getRequired() == 1);

        ActionMessages errors = adminService.updateAttribute(attr);
        
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CUSTOM_ATTR_EDIT + "?objectTypeId=" + attr.getObjectTypeId() 
                    + "&attrId=" + attr.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.ADMIN_CUSTOM_ATTR_DETAIL + "?attrId=" + attr.getId());
        }
    }
}
