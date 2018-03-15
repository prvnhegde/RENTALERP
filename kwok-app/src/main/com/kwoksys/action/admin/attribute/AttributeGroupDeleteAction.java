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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for deleting attribute group.
 */
public class AttributeGroupDeleteAction extends Action2 {

    public String delete() throws Exception {
        CustomAttributeForm actionForm = getBaseForm(CustomAttributeForm.class);

        // Make sure the object exists
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AttributeGroup attrGroup = adminService.getAttributeGroup(actionForm.getAttrGroupId(), actionForm.getObjectTypeId());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.attributeGroupDelete.header");

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate deleteTemplate = standardTemplate.addTemplate(new ObjectDeleteTemplate());
        deleteTemplate.setFormAjaxAction(AppPaths.ADMIN_ATTRIBUTE_GROUP_DELETE_2);
        deleteTemplate.setFormCancelAction(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
        deleteTemplate.getFormHiddenVariableMap().put("attrGroupId", actionForm.getAttrGroupId());
        deleteTemplate.getFormHiddenVariableMap().put("objectTypeId", actionForm.getObjectTypeId());
        deleteTemplate.setConfirmationMsgKey("admin.attributeGroupDelete.confirm");
        deleteTemplate.setSubmitButtonKey("admin.attributeGroupDelete.submitButton");
        deleteTemplate.getErrorsTemplate().setMessage(Localizer.getText(requestContext, "admin.attribute.attribute_group_name")
                + ": " + HtmlUtils.encode(attrGroup.getName()));

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String delete2() throws Exception {
        CustomAttributeForm actionForm = saveActionForm(new CustomAttributeForm());

        // Make sure the object exists
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        AttributeGroup attributeGroup = adminService.getAttributeGroup(actionForm.getAttrGroupId(), actionForm.getObjectTypeId());

        // Perform deletion
        ActionMessages errors = adminService.deleteAttributeGroup(attributeGroup);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_ATTRIBUTE_GROUP_DELETE + "?objectTypeId=" + actionForm.getObjectTypeId()
                    + "&attrGroupId=" + actionForm.getAttrGroupId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            new CacheManager(requestContext).removeCustomAttrGroupsCache(attributeGroup.getObjectTypeId());
            return ajaxUpdateView(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
        }
    }
}
