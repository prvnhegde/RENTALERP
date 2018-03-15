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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.ThisTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding custom attribute groups.
 */
public class AttributeGroupAddAction extends Action2 {

    public String add() throws Exception {
        CustomAttributeForm actionForm = getBaseForm(CustomAttributeForm.class);
        Integer objectTypeId = actionForm.getObjectTypeId();

        // Make sure we allow custom attribute for this object type
        if (!AttributeManager.getCustomFieldObjectTypes().contains(objectTypeId)) {
            throw new ObjectNotFoundException();
        }

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            AttributeGroup group = new AttributeGroup();
            actionForm.setAttrGroupName(group.getName());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_ATTRIBUTE_GROUP_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CUSTOM_ATTR_LIST).getString());
        standardTemplate.setAttribute("objectTypeId", objectTypeId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleText(Localizer.getText(requestContext, "admin.attributeGroupAdd.header") + " - "
                + Localizer.getText(requestContext, "common.objectType." + objectTypeId));

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        //
        // Template: ThisTemplate
        //
        standardTemplate.addTemplate(new ThisTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }

    public String add2() throws Exception {
        CustomAttributeForm actionForm = saveActionForm(new CustomAttributeForm());
        Integer objectTypeId = actionForm.getObjectTypeId();

        // Make sure we allow custom attribute for this object type
        if (!AttributeManager.getCustomFieldObjectTypes().contains(objectTypeId)) {
            throw new ObjectNotFoundException();
        }

        AttributeGroup attributeGroup = new AttributeGroup();
        attributeGroup.setObjectTypeId(actionForm.getObjectTypeId());
        attributeGroup.setName(actionForm.getAttrGroupName());

        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        ActionMessages errors = adminService.addAttributeGroup(attributeGroup);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_ATTRIBUTE_GROUP_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE
                    + "&objectTypeId=" + attributeGroup.getObjectTypeId());

        } else {
            new CacheManager(requestContext).removeCustomAttrGroupsCache(attributeGroup.getObjectTypeId());
            return ajaxUpdateView(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
        }
    }
}