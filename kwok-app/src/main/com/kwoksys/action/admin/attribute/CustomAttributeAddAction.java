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
import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
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
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.WidgetUtils;

/**
 * Action class for adding custom attributes.
 */
public class CustomAttributeAddAction extends Action2 {

    public String add() throws Exception {
        CustomAttributeForm actionForm = getBaseForm(CustomAttributeForm.class);

        Integer objectTypeId = actionForm.getObjectTypeId();

        // Make sure we allow custom attribute for this object type
        if (!AttributeManager.getCustomFieldObjectTypes().contains(objectTypeId)) {
            throw new ObjectNotFoundException();
        }

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setAttribute(new Attribute());
        }

        // Attribute type options
        List<LabelValueBean> attrTypeOptions = AttributeManager.getAttrDataTypeOptions(requestContext);

        Integer objectAttrType = AdminUtils.getObjectTypeMap().get(objectTypeId);

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
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_CUSTOM_ATTR_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CUSTOM_ATTR_LIST).getString());
        request.setAttribute("objectTypeId", objectTypeId);
        request.setAttribute("attrTypeId", actionForm.getAttrType());
        request.setAttribute("attrTypeOptions", attrTypeOptions);
        request.setAttribute("attrConvertUrlOptions", WidgetUtils.getYesNoOptions(requestContext));
        request.setAttribute("attrGroupOptions", AdminUtils.getAttributeGroupOptions(requestContext, objectTypeId));
        request.setAttribute("attrUrlExample", Keywords.CUSTOM_FIELD_VALUE_VAR_EXAMPLE);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add2() throws Exception {
        Integer objectTypeId = requestContext.getParameter("objectTypeId");
        List<Integer> systemFieldIds = requestContext.getParameters("systemFields");

        // Make sure we allow custom attribute for this object type
        if (!AttributeManager.getCustomFieldObjectTypes().contains(objectTypeId)) {
            throw new ObjectNotFoundException();
        }

        CustomAttributeForm actionForm = saveActionForm(new CustomAttributeForm());
        Attribute attr = new Attribute();
        attr.setObjectTypeId(actionForm.getObjectTypeId());
        attr.setName(actionForm.getAttrName());
        attr.setDescription(actionForm.getDescription());
        attr.setType(actionForm.getAttrType());
        attr.setAttributeOption(actionForm.getAttrOption());
        attr.setConvertUrl(actionForm.getAttrConvertUrl());
        attr.setUrl(actionForm.getAttrUrl());
        attr.setAttributeGroupId(actionForm.getAttrGroupId());
        attr.setInputMask(actionForm.getInputMask());
        attr.setAttrFieldIds(systemFieldIds);
        attr.setTypeCurrencySymbol(actionForm.getCurrencySymbol());
        
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        ActionMessages errors = adminService.addAttribute(attr);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CUSTOM_ATTR_ADD + "?objectTypeId=" + attr.getObjectTypeId() + "&" 
                    + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
        }
    }
}
