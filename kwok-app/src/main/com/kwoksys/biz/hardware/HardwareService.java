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
package com.kwoksys.biz.hardware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.hardware.core.HardwareSearch;
import com.kwoksys.biz.hardware.dao.HardwareDao;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareComponent;
import com.kwoksys.biz.hardware.dto.HardwareSoftwareMap;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.dto.linking.ContractHardwareLink;
import com.kwoksys.biz.system.dto.linking.HardwareIssueLink;
import com.kwoksys.biz.system.dto.linking.HardwareMemberLink;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.validations.ColumnField;
import com.kwoksys.framework.validations.InputValidator;

/**
 * HardwareService
 */
public class HardwareService {

    private RequestContext requestContext;

    public HardwareService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    // RentOut - Start
    public List<Hardware> getHardwareReturnList(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getHardwareReturnList(query);
    }
    
    public int getHardwareReturnCount(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getHardwareReturnCount(query);
    }
    
    public List<Hardware> getRentoutHardwareList(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getRentoutHardwareList(query);
    }

    public int getRentoutHardwareCount(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getRentoutCount(query);
    }
    
    public List<AttributeFieldCount> getHardwareRentoutTypeCount(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getRentoutHardwareTypeCount(query);
    }
    
    public List<AttributeFieldCount> getHardwareRentoutByCustomerCount(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getRentoutHardwareByCustomerCount(query);
    }

    public List<AttributeFieldCount> getHardwareRentoutByDurationCount(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getRentoutHardwareByDurationCount(query);
    }
    
    public Integer getHardwareReturn(Integer hardwareId) throws DatabaseException, ObjectNotFoundException {
        return new HardwareDao(requestContext).getHardwareReturn(hardwareId);
    }
 // RentOut - End
    
    // Hardware
    public List<Hardware> getHardwareList(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getHardwareList(query);
    }

    public List<Hardware> getLinkedHardwareList(QueryCriteria query, ObjectLink objectMap) throws DatabaseException {
        return new HardwareDao(requestContext).getLinkedHardwareList(query, objectMap);
    }
        
    public int getHardwareCount(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getCount(query);
    }

    public Map<String, String> getWarrantyExpirationCounts() throws DatabaseException {
        return new HardwareDao(requestContext).getWarrantyExpirationCounts();
    }

    public List<AttributeFieldCount> getHardwareTypeCount(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getHardwareTypeCount(query);
    }

    public List<AttributeFieldCount> getHardwareStatusCount(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getHardwareStatusCount(query);
    }

    public List<AttributeFieldCount> getHardwareLocationCount(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getHardwareLocationCount(query);
    }

    public Hardware getHardware(Integer hardwareId) throws DatabaseException, ObjectNotFoundException {
        return new HardwareDao(requestContext).getHardware(hardwareId);
    }

    public List<Hardware> getHardwareParents(QueryCriteria query, Integer hardwareId) throws DatabaseException {
        HardwareMemberLink memberMap = new HardwareMemberLink();
        memberMap.setMemberHardwareId(hardwareId);

        return new HardwareDao(requestContext).getLinkedHardwareList(query, memberMap.createObjectMap());
    }

    /**
     * Get a list of Hardware Members.
     * @param query
     * @param hardwareId
     * @return
     * @throws DatabaseException
     */
    public List<Hardware> getHardwareMembers(QueryCriteria query, Integer hardwareId) throws DatabaseException {
        HardwareMemberLink memberMap = new HardwareMemberLink(hardwareId);
        return new HardwareDao(requestContext).getLinkedHardwareList(query, memberMap.createObjectMap());
    }

    /**
     * Given a hardware id, get linked contracts.
     * @param query
     * @param hardwareId
     * @return
     * @throws com.kwoksys.framework.exceptions.DatabaseException
     */
    public void fetchLinkedContracts(QueryCriteria queryCriteria, Integer hardwareId) throws DatabaseException {
        ContractHardwareLink contractMap = new ContractHardwareLink();
        contractMap.setHardwareId(hardwareId);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        contractService.fetchLinkedContracts(queryCriteria, contractMap.createObjectMap());
    }

    public ActionMessages validateHardware(Hardware hardware, Map<Integer, Attribute> customAttributes)
            throws DatabaseException {

        ActionMessages errors = new ActionMessages();

        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("hardwareName").setTitleKey("common.column.hardware_name")
                .setNullable(false).calculateLength(hardware.getName()).setColumnName(Schema.ASSET_HARDWARE_NAME));

        if (!errors.get("hardwareName").hasNext()) {
            if (isDuplicatedHardwareName(hardware.getId(), hardware.getName())) {
                // Check unique name
                errors.add("duplicatedName", new ActionMessage("itMgmt.hardwareAdd.error.nameDuplicated", hardware.getName()));
            }
        }

        validator.validate(new ColumnField().setName("modelName").setTitleKey("common.column.hardware_model_name")
                .calculateLength(hardware.getModelName()).setColumnName(Schema.ASSET_HARDWARE_MODEL_NAME));

        validator.validate(new ColumnField().setName("modelNumber").setTitleKey("common.column.hardware_model_number")
                .calculateLength(hardware.getModelNumber()).setColumnName(Schema.ASSET_HARDWARE_MODEL_NUMBER));

        if (hardware.getSerialNumber().length() > Schema.getColumnLength(Schema.ASSET_HARDWARE_SERIAL_NUMBER)) {
            String fieldName = Localizer.getText(requestContext, "common.column.hardware_serial_number");
            errors.add("serialNumberMaxLength", new ActionMessage("common.form.fieldExceededMaxLen", new Object[]{fieldName, Schema.getColumnLength(Schema.ASSET_HARDWARE_SERIAL_NUMBER)}));
        } else if (validateDuplicatedHardwareSerialNumber(hardware)) {
            errors.add("duplicatedSerialNumber", new ActionMessage("itMgmt.hardwareAdd.error.duplicatedSerialNumber", new String[]{hardware.getSerialNumber()}));
        }
        if (!hardware.isValidHardwareCost()) {
            errors.add("validCostFormat", new ActionMessage("common.form.fieldFormatError",
                    Localizer.getText(requestContext, "common.column.hardware_purchase_price")));
        }
        if (hardware.hasHardwarePurchaseDate() && !hardware.isValidPurchaseDate()) {
            errors.add("validPurchaseDateFormat", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.hardware_purchase_date")));
        }
        if (hardware.hasHardwareWarrantyExpireDate() && !hardware.isValidWarrantyExpireDate()) {
            errors.add("validWarrantyExpireDateFormat", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.hardware_warranty_expire_date")));
        }

        // Validate attributes
        validator.validateAttrs(hardware, customAttributes);
        return errors;
    }

    public ActionMessages addHardware(Hardware hardware, Map<Integer, Attribute> customAttributes)
            throws DatabaseException {

        ActionMessages errors = validateHardware(hardware, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        return new HardwareDao(requestContext).addHardware(hardware);
    }

    public ActionMessages updateHardware(Hardware hardware, Map<Integer, Attribute> customAttributes)
            throws DatabaseException {

        ActionMessages errors = validateHardware(hardware, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        return new HardwareDao(requestContext).update(hardware);
    }

    public ActionMessages deleteHardware(Hardware hardware) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (hardware.getId() == 0) {
            errors.add("emptyHardwareId", new ActionMessage("itMgmt.hardwareEdit.error.emptyHardwareId"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        /**
         * Here is what i can do. First, collect a list of file names to be deleted (probably in a dataset).
         * Then, delete the Hardware, which would also delete the File records. Next, delete the actual files.
         */
        List<File> deleteFileList = getHardwareFiles(new QueryCriteria(), hardware.getId());

        errors = new HardwareDao(requestContext).delete(hardware);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete actual files
        if (errors.isEmpty()) {
            fileService.bulkDelete(ConfigManager.file.getHardwareFileRepositoryLocation(), deleteFileList);
        }
        return errors;
    }
    
    /**
     * 
     * @param hardware
     * @return
     * @throws DatabaseException
     */
    public ActionMessages rentOutHardware(Hardware hardware) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        
        // Check inputs
        if (hardware.getId() == 0) {
            errors.add("emptyHardwareId", new ActionMessage("itMgmt.hardwareEdit.error.emptyHardwareId"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        
        errors = new HardwareDao(requestContext).rentOut(hardware);
        return errors;
    }
    
    /**
     * 
     * @param hardware
     * @return
     * @throws DatabaseException
     */
    public ActionMessages hardwareReturn(Integer rentalId) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (rentalId == 0) {
            errors.add("emptyHardwareId", new ActionMessage("itMgmt.hardwareEdit.error.emptyHardwareId"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        
        errors = new HardwareDao(requestContext).hardwareReturn(rentalId);
        return errors;
    }
    

    // Hardware installed license
    public List<Map<String, String>> getAvailableSoftware(QueryCriteria query) throws DatabaseException {
        return new HardwareDao(requestContext).getAvailableSoftware(query);
    }

    public List<SoftwareLicense> getAvailableLicenses(QueryCriteria query, Integer softwareId) throws DatabaseException {
        return new HardwareDao(requestContext).getAvailableLicense(query, softwareId);
    }

    public List<HardwareSoftwareMap> getInstalledLicense(QueryCriteria query, Integer hardwareId) throws DatabaseException {
        return new HardwareDao(requestContext).getInstalledLicense(query, hardwareId);
    }

    public ActionMessages assignSoftwareLicense(HardwareSoftwareMap hsm) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (hsm.getHardwareId() == 0) {
            errors.add("emptyHardwareId", new ActionMessage("hardware.error.emptyHardwareId"));
        }
        if (hsm.getSoftwareId() == 0) {
            errors.add("emptySoftwareId", new ActionMessage("hardware.error.emptySoftwareId"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new HardwareDao(requestContext).assignSoftwareLicense(hsm);
    }

    public ActionMessages unassignSoftwareLicense(HardwareSoftwareMap hsm) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (hsm.getMapId() == 0) {
            errors.add("emptyMapId", new ActionMessage("hardware.error.emptyMapId"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        return new HardwareDao(requestContext).unassignSoftwareLicense(hsm);
    }

    public ActionMessages resetHardwareSoftwareCount(Integer hardwareId) throws DatabaseException {
        return new HardwareDao(requestContext).resetHardwareSoftwareCount(hardwareId);
    }

    /**
     * Get hardware components.
     */
    public List<HardwareComponent> getHardwareComponents(QueryCriteria query, Integer hardwareId) throws DatabaseException {
        return new HardwareDao(requestContext).getHardwareComponents(query, hardwareId);
    }

    /**
     * Get a hardware component.
     */
    public HardwareComponent getHardwareComponent(Integer hardwareId, Integer componentId) throws DatabaseException,
            ObjectNotFoundException {
        return new HardwareDao(requestContext).getHardwareComponentDetail(hardwareId, componentId);
    }

    /**
     * This is for adding hardware component
     * @param component
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addHardwareComponent(HardwareComponent component,
                                               Map<Integer, Attribute> customAttributes) throws DatabaseException {
         ActionMessages errors = new ActionMessages();

        // Check inputs
        if (component.getType() == 0) {
            errors.add("emptyComponentType", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.hardware_component_type")));
        }

        // Validate attributes
        InputValidator validator = new InputValidator(requestContext, errors);
        validator.validateAttrs(component, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        return new HardwareDao(requestContext).addHardwareComponent(component);
    }

    /**
     * This is for updating hardware component
     * @param component
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateHardwareComponent(HardwareComponent component, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Validate attributes
        InputValidator validator = new InputValidator(requestContext, errors);
        validator.validateAttrs(component, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        return new HardwareDao(requestContext).updateHardwareComponent(component);
    }

    /**
     * This is for deleting a hardware component
     * @param component
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteHardwareComponent(HardwareComponent component) throws DatabaseException {
        return new HardwareDao(requestContext).deleteHardwareComponent(component);
    }

    /**
     * Get a list of Hardware files.
     *
     * @param query
     * @return ..
     */
    public List<File> getHardwareFiles(QueryCriteria query, Integer hardwareId) throws DatabaseException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        return fileService.getFiles(query, ObjectTypes.HARDWARE, hardwareId);
    }

    public ActionMessages resetHardwareFileCount(Integer hardwareId) throws DatabaseException {
        return new HardwareDao(requestContext).resetFileCount(hardwareId);
    }

    /**
     * This is for resetting hardware component count.
     * @param hardwareId
     * @return
     * @throws DatabaseException
     */
    public ActionMessages resetHardwareComponentCount(Integer hardwareId) throws DatabaseException {
        return new HardwareDao(requestContext).resetComponentCount(hardwareId);
    }

    public File getHardwareFile(Integer hardwareId, Integer fileId) throws DatabaseException, ObjectNotFoundException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        File file = fileService.getFile(ObjectTypes.HARDWARE, hardwareId, fileId);
        file.setConfigRepositoryPath(ConfigManager.file.getHardwareFileRepositoryLocation());
        file.setConfigUploadedFilePrefix(ConfigManager.file.getHardwareUploadedFilePrefix());
        return file;
    }

    /**
     * Add Hardware Issue map.
     * @param issueMap
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addHardwareIssue(HardwareIssueLink issueMap) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(issueMap.createObjectMap());
    }

    /**
     * Delete Hardware Issue map.
     * @param issueMap
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteHardwareIssue(HardwareIssueLink issueMap) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(issueMap.createObjectMap());
    }

    public ActionMessages addHardwareMember(HardwareMemberLink memberMap) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(memberMap.createObjectMap());
    }

    public ActionMessages removeHardwareMember(HardwareMemberLink memberMap) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(memberMap.createObjectMap());
    }

    /**
     * Checks whether the given hardwareName already exists in the database (case-insensitive) with a different id.
     * @param hardwareService
     * @param hardwareId
     * @param hardwareName
     * @return
     * @throws Exception
     */
    public boolean isDuplicatedHardwareName(Integer hardwareId, String hardwareName)
            throws DatabaseException {

        if (ConfigManager.app.isCheckUniqueHardwareName()) {
            HardwareSearch hardwareSearch = new HardwareSearch();
            if (hardwareId != null) {
                hardwareSearch.put(HardwareSearch.HARDWARE_ID_NOT_EQUALS, hardwareId);
            }
            hardwareSearch.put(HardwareSearch.HARDWARE_NAME_EQUALS, hardwareName);
            return getHardwareCount(new QueryCriteria(hardwareSearch)) > 0;
        } else {
            return false;
        }
    }

    private boolean validateDuplicatedHardwareSerialNumber(Hardware hardware)
            throws DatabaseException {

        if (ConfigManager.app.isCheckUniqueSerialNumber()) {
            HardwareSearch hardwareSearch = new HardwareSearch();
            if (hardware.getId() != null) {
                hardwareSearch.put(HardwareSearch.HARDWARE_ID_NOT_EQUALS, hardware.getId());
            }
            hardwareSearch.put(HardwareSearch.HARDWARE_MANUFACTURER_EQUALS, hardware.getManufacturerId());
            hardwareSearch.put(HardwareSearch.HARDWARE_SERIAL_NUMBER_EQUALS, hardware.getSerialNumber());

            return getHardwareCount(new QueryCriteria(hardwareSearch)) > 0;
        } else {
            return false;
        }
    }

    public Hardware getSingleHardwareByName(String hardwareName) throws DatabaseException {
        HardwareSearch hardwareSearch = new HardwareSearch();
        hardwareSearch.put(HardwareSearch.HARDWARE_NAME_EQUALS, hardwareName);
        QueryCriteria queryCriteria = new QueryCriteria(hardwareSearch);
        queryCriteria.setLimit(2, 0);

        List<Hardware> hardwareList = getHardwareList(queryCriteria);

        if (hardwareList.size() == 1) {
            return hardwareList.get(0);
        } else {
            return null;
        }
    }
}
