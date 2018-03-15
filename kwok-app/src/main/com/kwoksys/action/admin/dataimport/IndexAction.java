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
package com.kwoksys.action.admin.dataimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.ThisTemplate;
import com.kwoksys.biz.admin.core.dataimport.ImportManager;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;

/**
 * DataImportIndex
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        getSessionBaseForm(DataImportForm.class);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_DATA_IMPORT_VALIDATE);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_INDEX).getString());

        standardTemplate.setAttribute("sampleFileLinks", Arrays.asList(
                new Link(requestContext)
                        .setExportPath(AppPaths.ADMIN_DATA_IMPORT_HARDWARE_SAMPLE_FILE)
                        .setTitleKey("import.type.hardware.sampleFile").getString(),
                new Link(requestContext)
                        .setExportPath(AppPaths.ADMIN_DATA_IMPORT_USER_SAMPLE_FILE)
                        .setTitleKey("import.type.user.sampleFile").getString()));

        List<LabelValueBean> options = new ArrayList<>();
        for (String importType : ImportManager.IMPORT_TYPES) {
            options.add(new LabelValueBean(Localizer.getText(requestContext, "import.type." + importType), importType));
        }
        standardTemplate.setAttribute("importTypeOptions", options);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("import.step.selectType");

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);
        errorsTemplate.setMessage(Localizer.getText(requestContext, "import.step.selectType.rowLimit",
                new Object[]{ConfigManager.app.getDataImportRowLimit()}));

        //
        // Template: ThisTemplate
        //
        standardTemplate.addTemplate(new ThisTemplate());
        standardTemplate.setAttribute("isCheckUniqueHardwareName", ConfigManager.app.isCheckUniqueHardwareName());

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
}
