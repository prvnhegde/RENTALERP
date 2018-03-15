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
package com.kwoksys.action.software;

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AjaxGetSoftwareByMaker
 */
public class AjaxGetSoftwareByMakerAction extends Action2 {

    public String execute() throws Exception {
        Integer formSoftwareId = 0;

        // The label value must be empty so that the search would skip softwareId.
        List<LabelValueBean> softwareList = new ArrayList<>();
        softwareList.add(new SelectOneLabelValueBean(requestContext));

        // This would show Software made by selected maker.
        Integer manufacturerId = requestContext.getParameter("manufacturerId");
        if (manufacturerId != 0) {
            QueryCriteria query = new QueryCriteria();
            query.addSortColumn(SoftwareQueries.getOrderByColumn(Software.NAME));

            SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

            for (Map software : softwareService.getCompanySoftwareList(query, manufacturerId)) {
                Object softwareId = software.get("software_id");
                Object softwareName = software.get("software_name");
                softwareList.add(new LabelValueBean(softwareName.toString(), softwareId.toString()));
            }
        }

        //
        // Template: AjaxTemplate
        //
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext);
        request.setAttribute("softwareId", formSoftwareId);
        request.setAttribute("softwareList", softwareList);

        return ajaxTemplate.findTemplate(AJAX_TEMPLATE);
    }
}
