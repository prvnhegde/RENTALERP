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
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for editing attribute.
 */
public class AttributeGroupEditAction extends Action2 {

    public String edit() throws Exception {
        CustomAttributeForm actionForm = getBaseForm(CustomAttributeForm.class);

        // Make sure the object exists
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AttributeGroup attributeGroup = adminService.getAttributeGroup(actionForm.getAttrGroupId(), actionForm.getObjectTypeId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setAttrGroupName(attributeGroup.getName());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_ATTRIBUTE_GROUP_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_CUSTOM_ATTR_LIST).getString());

        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("admin.attributeGroupEdit.header");

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
        AttributeGroup attributeGroup = adminService.getAttributeGroup(actionForm.getAttrGroupId(), actionForm.getObjectTypeId());
        attributeGroup.setName(actionForm.getAttrGroupName());

        ActionMessages errors = adminService.updateAttributeGroup(attributeGroup);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_ATTRIBUTE_GROUP_EDIT + "?objectTypeId=" + attributeGroup.getObjectTypeId()
                    + "&attrGroupId=" + attributeGroup.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            new CacheManager(requestContext).removeCustomAttrGroupsCache(attributeGroup.getObjectTypeId());
            return ajaxUpdateView(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
        }
    }
}
