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
package com.kwoksys.action.hardware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableEmptyTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareComponent;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * Hardware components page.
 */
public class HardwareComponentsAction extends Action2 {

    public String list() throws Exception {
        AccessUser user = requestContext.getUser();

        // Get request parameters
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Hardware components
        boolean canEditComponent = user.hasPermission(AppPaths.HARDWARE_COMP_EDIT);
        boolean canDeleteComponent = user.hasPermission(AppPaths.HARDWARE_COMP_DELETE_2);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        boolean hasCompCustomFields = adminService.hasCustomFields(ObjectTypes.HARDWARE_COMPONENT);

        List<Map> components = new ArrayList<>();

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(HardwareQueries.getOrderByColumn(HardwareComponent.COMP_NAME));

        int colspan = 4;

        List<HardwareComponent> componentList = hardwareService.getHardwareComponents(queryCriteria, hardware.getId());
        if (!componentList.isEmpty()) {
            for (HardwareComponent component : componentList) {
                Map<String, Object> map = new HashMap<>();
                map.put("component", component);
                if (hasCompCustomFields) {
                    Link link = new Link(requestContext);
                    link.setJavascript("App.toggleViewUpdate('cf"+component.getId()+"','"
                                                + AppPaths.ROOT + AppPaths.HARDWARE_AJAX_COMP_CUSTOM_FIELDS + "?hardwareId=" + hardwareId + "&compId=" + component.getId() + "')");
                    link.setImgSrc(Image.getInstance().getMagGlassIcon());
                    map.put("compPath", link.getString());
                }
                if (canEditComponent) {
                    map.put("editLink", new Link(requestContext).setTitleKey("form.button.edit")
                            .setAjaxPath(AppPaths.HARDWARE_COMP_EDIT + "?hardwareId=" + hardwareId + "&compId=" + component.getId()));
                }
                components.add(map);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("hardwareId", hardwareId);
        standardTemplate.setAttribute("components", components);
        standardTemplate.setAttribute("colspan", colspan);
        standardTemplate.setAttribute("canEditComponent", canEditComponent);
        standardTemplate.setAttribute("canDeleteComponent", canDeleteComponent);
        standardTemplate.setPathAttribute("deletePath", AppPaths.HARDWARE_COMP_DELETE_2);

        if (componentList.isEmpty()) {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = standardTemplate.addTemplate(new TableEmptyTemplate());
            empty.setColSpan(colspan);
            empty.setRowText(Localizer.getText(requestContext, "itMgmt.hardwareComp.emptyTableMessage"));
        }

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});
        HardwareUtils.addHardwareHeaderCommands(requestContext, header, hardwareId);
        
        // Add hardware components.
        if (user.hasPermission(AppPaths.HARDWARE_COMP_ADD)) {
            header.addHeaderCmds(new Link(requestContext).setTitleKey("itMgmt.cmd.hardwareComponentAdd")
                    .setAjaxPath(AppPaths.HARDWARE_COMP_ADD + "?hardwareId=" + hardwareId));
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_COMP_TAB);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String add() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Hardware component
        HardwareComponent component = new HardwareComponent();

        // If not a resubmit, set some defaults
        HardwareComponentForm actionForm = getBaseForm(HardwareComponentForm.class);
        if (!actionForm.isResubmit()) {
            actionForm.setHardwareComponentType(component.getType());
            actionForm.setCompDescription(component.getDescription());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("hardwareId", hardwareId);
        standardTemplate.setPathAttribute("formAction", AppPaths.HARDWARE_COMP_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_COMP + "?hardwareId=" + hardwareId).getString());
        standardTemplate.setAttribute("compOptions", new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.HARDWARE_COMPONENT_TYPE));

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE_COMPONENT);
        customFieldsTemplate.setObjectId(component.getId());
        customFieldsTemplate.setForm(actionForm);
        customFieldsTemplate.setPartialTable(true);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String add2() throws Exception {
        HardwareComponentForm actionForm = saveActionForm(new HardwareComponentForm());

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        HardwareComponent component = new HardwareComponent();
        component.setHardwareId(hardware.getId());
        component.setType(actionForm.getHardwareComponentType());
        component.setDescription(actionForm.getCompDescription());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.HARDWARE_COMPONENT);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, component, customAttributes);

        ActionMessages errors = hardwareService.addHardwareComponent(component, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_COMP_ADD + "?hardwareId=" + hardware.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset hardware component counter.
            hardwareService.resetHardwareComponentCount(hardware.getId());

            return ajaxUpdateView(AppPaths.HARDWARE_COMP + "?hardwareId=" + component.getHardwareId());
        }
    }
    
    public String edit() throws Exception {
        AccessUser user = requestContext.getUser();

        Integer hardwareId = requestContext.getParameter("hardwareId");
        Integer compId = requestContext.getParameter("compId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Hardware component
        HardwareComponent component = hardwareService.getHardwareComponent(hardwareId, compId);

        AttributeManager attributeManager = new AttributeManager(requestContext);

        // If not a resubmit, set some defaults
        HardwareComponentForm actionForm = getBaseForm(HardwareComponentForm.class);
        if (!actionForm.isResubmit()) {
            actionForm.setHardwareComponentType(component.getType());
            actionForm.setCompDescription(component.getDescription());
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        request.setAttribute("hardwareId", hardwareId);
        standardTemplate.setPathAttribute("formAction", AppPaths.HARDWARE_COMP_EDIT_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_COMP + "?hardwareId=" + hardwareId).getString());
        request.setAttribute("compId", component.getId());
        request.setAttribute("compOptions", attributeManager.getActiveAttrFieldOptionsCache(Attributes.HARDWARE_COMPONENT_TYPE));

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        // Back to hardware list.
        if (Access.hasPermission(user, AppPaths.HARDWARE_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_LIST);
            link.setTitleKey("itMgmt.cmd.hardwareList");
            header.addHeaderCmds(link);
        }

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE_COMPONENT);
        customFieldsTemplate.setObjectId(component.getId());
        customFieldsTemplate.setForm(actionForm);
        customFieldsTemplate.setPartialTable(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        HardwareComponentForm actionForm = saveActionForm(new HardwareComponentForm());

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        HardwareComponent component = hardwareService.getHardwareComponent(actionForm.getHardwareId(), actionForm.getCompId());

        component.setType(actionForm.getHardwareComponentType());
        component.setDescription(actionForm.getCompDescription());

        // Get custom field values from request
        Map<Integer, Attribute> customAttributes = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.HARDWARE_COMPONENT);
        AttributeManager.populateCustomFieldValues(requestContext, actionForm, component, customAttributes);

        ActionMessages errors = hardwareService.updateHardwareComponent(component, customAttributes);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_COMP_EDIT + "?hardwareId=" + component.getHardwareId() + "&compId=" + component.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.HARDWARE_COMP + "?hardwareId=" + component.getHardwareId());
        }
    }
    
    public String delete2() throws Exception {
        HardwareComponentForm actionForm = saveActionForm(new HardwareComponentForm());

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        HardwareComponent component = hardwareService.getHardwareComponent(actionForm.getHardwareId(), actionForm.getCompId());

        ActionMessages errors = hardwareService.deleteHardwareComponent(component);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_COMP + "?hardwareId=" + component.getHardwareId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset hardware component counter.
            hardwareService.resetHardwareComponentCount(actionForm.getHardwareId());

            return ajaxUpdateView(AppPaths.HARDWARE_COMP + "?hardwareId=" + component.getHardwareId());
        }
    }    
}
