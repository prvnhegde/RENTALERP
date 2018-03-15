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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.admin.core.dataimport.ImportManager;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.csv.CsvReader;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * Data import.
 */
public class ValidateAction extends Action2 {

    public String execute() throws Exception {
        DataImportForm actionForm = getSessionBaseForm(DataImportForm.class);
        CsvReader csvReader = actionForm.getCsvReader();

        ActionMessages errors = new ActionMessages();

        if (actionForm.getError() != null) {
            errors.add("filename", actionForm.getError());

        } else {
            if (csvReader.getData().size()-1 > ConfigManager.app.getDataImportRowLimit()) {
                errors.add("filename", new ActionMessage("import.validate.message.exceededRowLimit",
                        ConfigManager.app.getDataImportRowLimit(), csvReader.getData().size()));
            }
        }

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.ADMIN_DATA_IMPORT_INDEX + "?" + RequestContext.URL_PARAM_ERROR_TRUE);
        }

        ImportManager importManager = ImportManager.newInstance(requestContext, actionForm);
        importManager.validate(csvReader.getData());

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("dataList", importManager.getImportItems());

        if (actionForm.getImportType().equals(ImportManager.IMPORT_TYPE_HARDWARE)) {
            standardTemplate.setAttribute("nameHeader", "common.column.hardware_name");
        } else if (actionForm.getImportType().equals(ImportManager.IMPORT_TYPE_USER)) {
            standardTemplate.setAttribute("nameHeader", "common.column.username");
        }

        standardTemplate.setPathAttribute("formAction", AppPaths.ADMIN_DATA_IMPORT_EXECUTE);
        standardTemplate.setPathAttribute("formBackAction", AppPaths.ADMIN_DATA_IMPORT_INDEX);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.ADMIN_INDEX).getString());

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setTitleKey("import.validate.title");
        headerTemplate.setTitleClassNoLine();

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setMessageKey("import.validate.description");

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}