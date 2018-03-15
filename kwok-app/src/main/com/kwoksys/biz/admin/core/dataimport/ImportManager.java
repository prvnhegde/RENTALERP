/*
 * Copyright 2015 Kwoksys
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
package com.kwoksys.biz.admin.core.dataimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kwoksys.action.admin.dataimport.DataImportForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.ImportItem;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;

/**
 * ImportManager
 */
public abstract class ImportManager {

    public static final String IMPORT_TYPE_HARDWARE = "hardware_import";

    public static final String IMPORT_TYPE_USER = "user_import";

    public static final List<String> IMPORT_TYPES = Arrays.asList(IMPORT_TYPE_HARDWARE, IMPORT_TYPE_USER);

    private Map<String, Integer> companyIdMap = new HashMap<>();

    protected List<ImportItem> importItems = new ArrayList<>();

    protected RequestContext requestContext;

    protected DataImportForm dataImportForm;

    private Map<String, Integer> counts = new HashMap<>();

    protected int rowNum = 1;

    public static ImportManager newInstance(RequestContext requestContext, DataImportForm dataImportForm) {
        ImportManager importManager = null;

        if (dataImportForm.getImportType().equals(IMPORT_TYPE_HARDWARE)) {
            importManager = new HardwareImport();

        } else if (dataImportForm.getImportType().equals(IMPORT_TYPE_USER)) {
            importManager = new UserImport();
        }

        if (importManager != null) {
            importManager.setRequestContext(requestContext);
            importManager.setDataImportForm(dataImportForm);
            requestContext.getSession().setAttribute(SessionManager.IMPORT_RESULTS, importManager.getImportItems());
        }
        return importManager;
    }

    public Integer getCompanyId(String value) throws DatabaseException {
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        Integer companyId = null;
        if (companyIdMap.containsKey(value)) {
            companyId = companyIdMap.get(value);
        } else {
            Company company = contactService.getSingleCompanyByName(value);
            if (company != null) {
                companyId = company.getId();
            }
            companyIdMap.put(value, companyId);
        }
        return companyId;
    }

    protected void addImportItem(ImportItem importItem) {
        Integer count = counts.get(importItem.getAction());
        if (count == null) {
            count = 0;
        }
        counts.put(importItem.getAction(), count + 1);
        importItems.add(importItem);
    }

    protected void buildImportResultsMessage() {
        StringBuilder sb = new StringBuilder();
        if (counts.containsKey(ImportItem.ACTION_ADD)) {
            sb.append(Localizer.getText(requestContext, "import.result.message.ADD", new Object[]{counts.get(ImportItem.ACTION_ADD)})).append(" ");
        }
        if (counts.containsKey(ImportItem.ACTION_UPDATE)) {
            sb.append(Localizer.getText(requestContext, "import.result.message.UPDATE", new Object[]{counts.get(ImportItem.ACTION_UPDATE)})).append(" ");
        }
        if (counts.containsKey(ImportItem.ACTION_ERROR)) {
            sb.append(Localizer.getText(requestContext, "import.result.message.ERROR", new Object[]{counts.get(ImportItem.ACTION_ERROR)}));
        }
        requestContext.getSession().setAttribute(SessionManager.IMPORT_RESULTS_MESSAGE, sb.toString());
    }

    public List<ImportItem> getImportItems() {
        return importItems;
    }

    public void validate(List<String[]> data) throws Exception {
        boolean validateOnly = true;
        importData(data, validateOnly);
    }

    public void execute(List<String[]> data) throws Exception {
        boolean validateOnly = false;
        importData(data, validateOnly);
    }

    protected abstract void importData(List<String[]> data, boolean validateOnly) throws Exception;

    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public void setDataImportForm(DataImportForm dataImportForm) {
        this.dataImportForm = dataImportForm;
    }
}
