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
package com.kwoksys.biz.admin.core.dataimport;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.ImportItem;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HardwareImport extends ImportManager {

    private static final Logger LOGGER = Logger.getLogger(HardwareImport.class.getName());

    protected void importData(List<String[]> data, boolean validateOnly) throws Exception {
        Iterator<String[]> rowIter = data.iterator();

        // First row contains headers.
        List<String> columnHeaders = Arrays.asList(rowIter.next());

        AttributeManager attributeManager = new AttributeManager(requestContext);

        Map<String, Integer> userIdMap = new HashMap<>();
        Map<String, AttributeField> locationMap = attributeManager.getAttrNameMapCache(Attributes.HARDWARE_LOCATION);
        Map<String, AttributeField> statusMap = attributeManager.getAttrNameMapCache(Attributes.HARDWARE_STATUS);
        Map<String, AttributeField> typeMap = attributeManager.getAttrNameMapCache(Attributes.HARDWARE_TYPE);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        // Get custom field values from csv.
        Map<Integer, Attribute> typeCustomFields = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.HARDWARE);

        while (rowIter.hasNext()) {
            boolean overwrite = !dataImportForm.isAllowDuplicate();
            ImportItem importItem = new ImportItem();
            importItem.setRowNum(++rowNum);

            try {
                String[] columns = rowIter.next();
                Hardware hardware = null;

                // If overwrite is enabled, get hardware record by name.
                if (overwrite) {
                    int colNumber = columnHeaders.indexOf(Hardware.HARDWARE_NAME);
                    if (colNumber != -1) {
                        String hardwareName = columns[colNumber].trim();
                        hardware = hardwareService.getSingleHardwareByName(hardwareName);
                    }
                }

                // When hardware is null, Update only when there is exactly one record.
                if (hardware == null) {
                    hardware = new Hardware();
                    overwrite = false;
                }

                Map<String, String> importCustomFields = new HashMap<>();

                ActionMessages errors = new ActionMessages();

                for (int i = 0; i<columns.length; i++) {
                    String key = columnHeaders.get(i).trim();
                    String value = columns[i].trim();
                    //System.out.println(key + ": " + value);

                    if (value.isEmpty()) {
                        continue;
                    }

                    if (key.equals(Hardware.HARDWARE_NAME)) {
                        hardware.setName(value);
                        importItem.setTitle(value);
                    } else if (key.equals(Hardware.HARDWARE_DESCRIPTION)) {
                        hardware.setDescription(value);
                    } else if (key.equals(Hardware.MODEL_NAME)) {
                        hardware.setModelName(value);
                    } else if (key.equals(Hardware.MODEL_NUMBER)) {
                        hardware.setModelNumber(value);
                    } else if (key.equals(Hardware.SERIAL_NUMBER)) {
                        hardware.setSerialNumber(value);
                    } else if (key.equals(Hardware.PURCHASE_PRICE)) {
                        hardware.setPurchasePrice(value);
                    } else if (key.equals(Hardware.PURCHASE_DATE)) {
                        try {
                            hardware.setHardwarePurchaseDate(value);
                        } catch (Exception e) {
                            errors.add("invalidDate", new ActionMessage("import.validate.message.invalidDate",
                                    new Object[]{Localizer.getText(requestContext, "common.column.hardware_purchase_date"), value}));
                        }
                    } else if (key.equals(Hardware.WARRANTY_EXPIRATION)) {
                        try {
                            hardware.setHardwareWarrantyExpireDate(value);
                        } catch (Exception e) {
                            errors.add("invalidDate", new ActionMessage("import.validate.message.invalidDate",
                                    new Object[]{Localizer.getText(requestContext, "common.column.hardware_warranty_expire_date"), value}));
                        }
                    } else if (key.equals(Hardware.OWNER_USERNAME)) {
                        Integer userId;
                        if (userIdMap.containsKey(value)) {
                            userId = userIdMap.get(value);
                        } else {
                            userId = adminService.getUserIdByUsername(value);
                            userIdMap.put(value, userId);
                        }
                        if (userId != null) {
                            if (!userId.equals(hardware.getOwnerId())) {
                                // Remove user cache.
                                if (hardware.getOwnerId() != null && hardware.getOwnerId() != 0) {
                                    new CacheManager(requestContext).removeUserCache(hardware.getOwnerId());
                                }
                                new CacheManager(requestContext).removeUserCache(userId);
                            }
                            hardware.setOwnerId(userId);
                        } else {
                            importItem.getWarningMessages().add(Localizer.getText(requestContext, "import.validate.message.noMatches",
                                    new String[]{Localizer.getText(requestContext, "common.column.hardware_owner_name"), value}));
                        }
                    } else if (key.equals(Hardware.MANUFACTURER_NAME)) {
                        Integer companyId = getCompanyId(value);
                        if (companyId != null) {
                            hardware.setManufacturerId(companyId);
                        } else {
                            importItem.getWarningMessages().add(Localizer.getText(requestContext, "import.validate.message.noMatches",
                                    new String[]{Localizer.getText(requestContext, "common.column.hardware_manufacturer_name"), value}));
                        }
                    } else if (key.equals(Hardware.VENDOR_NAME)) {
                        Integer companyId = getCompanyId(value);
                        if (companyId != null) {
                            hardware.setVendorId(companyId);
                        } else {
                            importItem.getWarningMessages().add(Localizer.getText(requestContext, "import.validate.message.noMatches",
                                    new String[]{Localizer.getText(requestContext, "common.column.hardware_vendor_name"), value}));
                        }
                    } else if (key.equals(Hardware.LOCATION)) {
                        AttributeField attrValue = locationMap.get(value.toLowerCase());
                        if (attrValue != null) {
                            hardware.setLocation(attrValue.getId());
                        } else {
                            importItem.getWarningMessages().add(Localizer.getText(requestContext, "import.validate.message.noMatches",
                                    new String[]{Localizer.getText(requestContext, "common.column.hardware_location"), value}));
                        }
                    } else if (key.equals(Hardware.STATUS)) {
                        AttributeField attrValue = statusMap.get(value.toLowerCase());
                        if (attrValue != null) {
                            hardware.setStatus(attrValue.getId());
                        } else {
                            importItem.getWarningMessages().add(Localizer.getText(requestContext, "import.validate.message.noMatches",
                                    new String[]{Localizer.getText(requestContext, "common.column.hardware_status"), value}));
                        }
                    } else if (key.equals(Hardware.TYPE)) {
                        AttributeField attrValue = typeMap.get(value.toLowerCase());
                        if (attrValue != null) {
                            hardware.setType(attrValue.getId());
                        } else {
                            importItem.getWarningMessages().add(Localizer.getText(requestContext, "import.validate.message.noMatches",
                                    new String[]{Localizer.getText(requestContext, "common.column.hardware_type"), value}));
                        }
                    } else {
                        importCustomFields.put(key, value);
                    }
                }

                AttributeManager.populateCustomFieldValues(importCustomFields, hardware, typeCustomFields);

                if (errors.isEmpty()) {
                    if (validateOnly) {
                        errors.add(hardwareService.validateHardware(hardware, typeCustomFields));
                    } else {
                        if (overwrite) {
                            errors.add(hardwareService.updateHardware(hardware, typeCustomFields));
                        } else {
                            errors.add(hardwareService.addHardware(hardware, typeCustomFields));
                        }
                    }
                }

                if (!errors.isEmpty()) {
                    for (Iterator<ActionMessage> iter = errors.get(); iter.hasNext();) {
                        ActionMessage error = iter.next();
                        importItem.getErrorMessages().add(Localizer.getText(requestContext, error.getKey(), error.getValues()));
                    }
                    importItem.setAction(ImportItem.ACTION_ERROR);
                } else {
                    importItem.setAction(overwrite ? ImportItem.ACTION_UPDATE : ImportItem.ACTION_ADD);
                }

            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Problem importing hardware", e);
                importItem.getErrorMessages().add(e.getMessage());
                importItem.setAction(ImportItem.ACTION_ERROR);
            }

            addImportItem(importItem);
        }

        buildImportResultsMessage();
    }
}
