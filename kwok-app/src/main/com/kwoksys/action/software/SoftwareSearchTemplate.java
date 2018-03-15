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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dao.SoftwareQueries;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HardwareSearchTemplate
 */
public class SoftwareSearchTemplate extends BaseTemplate {

    private String formAction;
    private boolean hideSearchButton;
    private String clearSearchLink;

    public SoftwareSearchTemplate() {
        super(SoftwareSearchTemplate.class);
    }

    public void init() {}

    public void applyTemplate() throws DatabaseException {
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Software maker options
        List<LabelValueBean> manufacturerOptions = new ArrayList<>();
        manufacturerOptions.add(new SelectOneLabelValueBean(requestContext));

        QueryCriteria softQuery = new QueryCriteria();
        softQuery.addSortColumn(SoftwareQueries.getOrderByColumn(Software.MANUFACTURER));

        for (Map software : softwareService.getSoftwareCountGroupByCompany(softQuery)) {
            Object manufacturerId = software.get("manufacturer_company_id");
            Object manufacturerName = software.get("software_manufacturer");

            if (manufacturerName != null) {
                manufacturerOptions.add(new LabelValueBean(manufacturerName.toString(), manufacturerId.toString()));
            }
        }

        request.setAttribute("formGetSoftwareAction", AppPaths.ROOT + AppPaths.IT_MGMT_AJAX_GET_SOFTWARE_BY_MAKER + "?manufacturerId=");
        request.setAttribute("manufacturerOptions", manufacturerOptions);
        request.setAttribute("softwareTypeOptions", new AttributeManager(requestContext).getAttrFieldOptionsCache(Attributes.SOFTWARE_TYPE));
        request.setAttribute("customFieldsOptions", new AttributeManager(requestContext).getCustomFieldOptions(ObjectTypes.SOFTWARE));

        if (!hideSearchButton) {
            clearSearchLink = new Link(requestContext).setAppPath(AppPaths.SOFTWARE_INDEX + "?cmd=clear").setTitleKey("common.form.clearCriteria").getString();
        }
    }

    public String getFormAction() {
        return formAction;
    }
    public void setFormAction(String formAction) {
        this.formAction = AppPaths.ROOT + formAction;
    }
    public boolean getHideSearchButton() {
        return hideSearchButton;
    }

    public void setHideSearchButton(boolean hideSearchButton) {
        this.hideSearchButton = hideSearchButton;
    }

    public String getClearSearchLink() {
         return clearSearchLink;
    }
}
