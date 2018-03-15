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
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for deleting custom attribute.
 */
public class CustomAttributeDeleteAction extends Action2 {

    public String delete() throws Exception {
        Integer attributeId = requestContext.getParameter("attrId");

        // Make sure the object exists
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        Attribute attr = adminService.getCustomAttribute(attributeId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("admin.customAttrDelete.header");

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate deleteTemplate = standardTemplate.addTemplate(new ObjectDeleteTemplate());
        deleteTemplate.setFormAjaxAction(AppPaths.ADMIN_CUSTOM_ATTR_DELETE_2);
        deleteTemplate.setFormCancelAction(AppPaths.ADMIN_CUSTOM_ATTR_DETAIL + "?attrId=" + attr.getId());
        deleteTemplate.getFormHiddenVariableMap().put("attrId", attr.getId());
        deleteTemplate.setConfirmationMsgKey("admin.customAttrDelete.confirm");
        deleteTemplate.setSubmitButtonKey("admin.customAttrDelete.submitButton");
        deleteTemplate.getErrorsTemplate().setMessage(Localizer.getText(requestContext, "admin.attribute.attribute_name")
                + ": " + HtmlUtils.encode(attr.getName()));

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String delete2() throws Exception {
        Integer attributeId = requestContext.getParameter("attrId");

        // Call the service
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Make sure the object exists
        Attribute attr = adminService.getCustomAttribute(attributeId);

        // Perform deletion
        ActionMessages errors = adminService.deleteAttribute(attr);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.ADMIN_CUSTOM_ATTR_DELETE + "?attrId=" + attributeId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.ADMIN_CUSTOM_ATTR_LIST);
        }
    }
}
