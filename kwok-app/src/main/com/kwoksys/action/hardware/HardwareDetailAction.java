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

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.CustomFieldsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableEmptyTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dao.HardwareQueries;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareSoftwareMap;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for hardware license.
 */
public class HardwareDetailAction extends Action2 {

    public String execute() throws Exception {
        HardwareLicenseForm actionForm = getBaseForm(HardwareLicenseForm.class);
        AccessUser user = requestContext.getUser();

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(actionForm.getHardwareId());

        int colSpan = 4;

        // For adding Software.
        List<LabelValueBean> softwareOptions = new ArrayList<>();
        String formSoftwareLicense = AppPaths.IT_MGMT_AJAX_HARDWARE_ASSIGN_LICENSE + "?softwareId=";
        String cmd = actionForm.getCmd();

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.addSortColumn(Software.NAME);
        queryCriteria.addSortColumn("license_key");

        List<HardwareSoftwareMap> maps = hardwareService.getInstalledLicense(queryCriteria, hardware.getId());
        List<Map> installedLicenses = new ArrayList<>();

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("softwareOptions", softwareOptions);
        standardTemplate.setPathAttribute("formAddLicAction", AppPaths.HARDWARE_LICENSE_ADD_2);
        standardTemplate.setAttribute("canRemoveLicense", user.hasPermission(AppPaths.HARDWARE_LICENSE_REMOVE_2));
        standardTemplate.setPathAttribute("formRemoveLicenseAction", AppPaths.HARDWARE_LICENSE_REMOVE_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.HARDWARE_DETAIL + "?hardwareId=" + hardware.getId()).getString());
        standardTemplate.setPathAttribute("formGetSoftwareLicenseAction", formSoftwareLicense);
        standardTemplate.setAttribute("colSpan", colSpan);

        //
        // Template: CustomFieldsTemplate
        //
        CustomFieldsTemplate customFieldsTemplate = standardTemplate.addTemplate(new CustomFieldsTemplate());
        customFieldsTemplate.setObjectTypeId(ObjectTypes.HARDWARE);
        customFieldsTemplate.setObjectId(hardware.getId());
        customFieldsTemplate.setObjectAttrTypeId(hardware.getType());
        customFieldsTemplate.setShowDefaultHeader(false);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        HardwareUtils.addHardwareHeaderCommands(requestContext, headerTemplate, hardware.getId());
        headerTemplate.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        // Assign Software link.
        if (user.hasPermission(AppPaths.HARDWARE_LICENSE_ADD_2)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.HARDWARE_DETAIL + "?cmd=add&hardwareId=" + hardware.getId());
            link.setTitleKey("itMgmt.cmd.softwareLicenseAssign");
            headerTemplate.addHeaderCmds(link);
        }

        if (cmd.equals("add")) {
            // The user is trying to add a Software, show him the Software list.
            queryCriteria = new QueryCriteria();
            queryCriteria.addSortColumn(HardwareQueries.getOrderByColumn(Software.NAME));

            List<Map<String, String>> softwareList = hardwareService.getAvailableSoftware(queryCriteria);
            for (Map<String, String> software : softwareList) {
                softwareOptions.add(new LabelValueBean(software.get("software_name").toString(),
                        software.get("software_id").toString()));
            }

            if (!softwareList.isEmpty()) {
                // Get the first software, we want to show licenses for the first software.
                LabelValueBean b = (LabelValueBean) softwareOptions.get(0);
                String softwareId = b.getValue();

                standardTemplate.getHeaderTemplate().setOnloadJavascript("App.updateView('softwareLicensesDiv', '" + AppPaths.ROOT + formSoftwareLicense + softwareId + "');");
            }
        }

        if (!maps.isEmpty()) {
            boolean viewSoftwareDetail = user.hasPermission(AppPaths.SOFTWARE_DETAIL);
            for (HardwareSoftwareMap map : maps) {
                Map<String, Object> datamap = new HashMap<>();
                datamap.put("mapId", map.getMapId());

                Link softwareLink = new Link(requestContext);
                softwareLink.setTitle(map.getSoftware().getName());

                if (viewSoftwareDetail) {
                    // Show the link only if user has access to the page.
                    softwareLink.setAjaxPath(AppPaths.SOFTWARE_DETAIL + "?softwareId=" + map.getSoftwareId());
                }
                datamap.put("softwareName", softwareLink);

                String licenseKey = HtmlUtils.encode(map.getLicense().getKey());
                if (licenseKey.isEmpty()) {
                    licenseKey = Localizer.getText(requestContext, "itMgmt.hardwareDetail.unknownLicense");
                }
                datamap.put("licenseId", map.getLicenseId());
                datamap.put("licenseKey", licenseKey);
                datamap.put("licenseNote", SoftwareUtils.formatLicenseKey(map.getLicense().getNote()));

                // Don't show the magnifying glass icon for invalid licenses.
                if (!map.getLicenseId().equals(0) && user.hasPermission(AppPaths.SOFTWARE_AJAX_DETAILS)) {
                    datamap.put("licenseInfoLink", new Link(requestContext).setImgOnclick("Js.Display.toggle('note"
                            + map.getLicenseId() + "');App.toggleViewUpdate('cf" + map.getLicenseId() + "','"
                            + AppPaths.ROOT + AppPaths.SOFTWARE_AJAX_DETAILS + "?softwareId=" + map.getSoftwareId()
                            + "&licenseId=" + map.getLicenseId() + "')").setImgSrc(Image.getInstance().getMagGlassIcon()));
                }

                installedLicenses.add(datamap);
            }
            standardTemplate.setAttribute("installedLicenses", installedLicenses);
        } else {
            //
            // Template: TableEmptyTemplate
            //
            TableEmptyTemplate empty = standardTemplate.addTemplate(new TableEmptyTemplate());
            empty.setColSpan(colSpan);
            empty.setRowText(Localizer.getText(requestContext, "itMgmt.hardwareDetail.emptyTableMessage"));
        }

        //
        // Template: HardwareSpecTemplate
        //
        HardwareSpecTemplate tmpl = standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));
        tmpl.setPopulateLinkedContract(!cmd.equals("add"));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_LICENSE_TAB);

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}