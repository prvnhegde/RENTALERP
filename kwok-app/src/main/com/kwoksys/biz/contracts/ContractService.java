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
package com.kwoksys.biz.contracts;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.auth.core.Permissions;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.contracts.core.ContractUtils;
import com.kwoksys.biz.contracts.dao.ContractDao;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Keywords;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.biz.system.dto.linking.ContractContactLink;
import com.kwoksys.biz.system.dto.linking.ContractHardwareLink;
import com.kwoksys.biz.system.dto.linking.ContractSoftwareLink;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.mail.EmailMessage;
import com.kwoksys.framework.connections.mail.SmtpService;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.validations.ColumnField;
import com.kwoksys.framework.validations.InputValidator;

/**
 * ContractService.
 */
public class ContractService {

    private static final Logger LOGGER = Logger.getLogger(ContractService.class.getName());
    
    private RequestContext requestContext;

    public ContractService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public void fetchContracts(QueryCriteria queryCriteria) throws DatabaseException {
        new ContractDao(requestContext).fetchContracts(queryCriteria);
    }

    public void fetchLinkedContracts(QueryCriteria queryCriteria, ObjectLink objectMap) throws DatabaseException {
        new ContractDao(requestContext).fetchContracts(queryCriteria, objectMap);
    }

    public int getContractCount(QueryCriteria queryCriteria) throws DatabaseException {
        return new ContractDao(requestContext).getContractCount(queryCriteria);
    }

    public Contract getContract(Integer contractId) throws DatabaseException, ObjectNotFoundException {
        return new ContractDao(requestContext).getContract(contractId);
    }

    public List<Hardware> getContractHardwareList(QueryCriteria query, Integer contractId) throws DatabaseException {
        ContractHardwareLink contractHardware = new ContractHardwareLink(contractId);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        return hardwareService.getLinkedHardwareList(query, contractHardware.createObjectMap());
    }

    public List<Software> getContractSoftwareList(QueryCriteria query, Integer contractId) throws DatabaseException {
        ContractSoftwareLink contractSoftware = new ContractSoftwareLink(contractId);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        return softwareService.getLinkedSoftwareList(query, contractSoftware.createObjectMap());
    }

    public Map<String, String> getContractsSummary() throws DatabaseException {
        ContractDao contractDao = new ContractDao(requestContext);
        return contractDao.getContractsSummary();
    }

    public ActionMessages addContract(Contract contract, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("contractName").setTitleKey("common.column.contract_name")
                .calculateLength(contract.getName()).setColumnName(Schema.CONTRACT_NAME).setNullable(false));

        if (contract.hasContractEffectiveDate() && !contract.isValidContractEffectiveDate()) {
            errors.add("invalidEffectiveDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_effective_date")));
        }
        if (contract.hasContractExpirationDate() && !contract.isValidContractExpirationDate()) {
            errors.add("invalidExpirationDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_expiration_date")));
        }
        if (contract.hasContractRenewalDate() && !contract.isValidContractRenewalDate()) {
            errors.add("invalidRenewalDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_renewal_date")));
        }

        validator.validateAttrs(contract, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        ContractDao contractDao = new ContractDao(requestContext);
        return contractDao.addContract(contract);
    }

    public ActionMessages updateContract(Contract contract, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("contractName").setTitleKey("common.column.contract_name")
                .calculateLength(contract.getName()).setColumnName(Schema.CONTRACT_NAME).setNullable(false));

        if (contract.hasContractEffectiveDate() && !contract.isValidContractEffectiveDate()) {
            errors.add("invalidEffectiveDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_effective_date")));
        }
        if (contract.hasContractExpirationDate() && !contract.isValidContractExpirationDate()) {
            errors.add("invalidExpirationDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_expiration_date")));
        }
        if (contract.hasContractRenewalDate() && !contract.isValidContractRenewalDate()) {
            errors.add("invalidRenewalDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.contract_renewal_date")));
        }

        validator.validateAttrs(contract, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        ContractDao contractDao = new ContractDao(requestContext);
        return contractDao.updateContract(contract);
    }

    public ActionMessages deleteContract(Integer contractId) throws DatabaseException {
        /**
         * Here is what i can do. First, collect a list of file names to be deleted (probably in a dataset).
         * Then, delete the object, which would also delete the File records. Next, delete the actual files.
         */
        List<File> deleteFileList = getContractFiles(new QueryCriteria(), contractId);

        ContractDao contractDao = new ContractDao(requestContext);
        ActionMessages errors = contractDao.delete(contractId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete actual files
        if (errors.isEmpty()) {
            fileService.bulkDelete(ConfigManager.file.getContractFileRepositoryLocation(), deleteFileList);
        }

        return errors;
    }

    /**
     * Add Contract Hardware map.
     * @param contractHardware
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addContractHardware(ContractHardwareLink contractHardware) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contractHardware.getHardwareId() == 0) {
            errors.add("emptyHardwareId", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.hardware_id")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(contractHardware.createObjectMap());
    }

    /**
     * Delete Contract Hardware map.
     * @param contractHardware
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteContractHardware(ContractHardwareLink contractHardware) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(contractHardware.createObjectMap());
    }

    public ActionMessages addContractSoftware(ContractSoftwareLink contractSoftware) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (contractSoftware.getSoftwareId() == 0) {
            errors.add("emptySoftwareId", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.software_id")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(contractSoftware.createObjectMap());
    }

    /**
     * Delete Contract Software map.
     * @param contractSoftware
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteContractSoftware(ContractSoftwareLink contractSoftware) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(contractSoftware.createObjectMap());
    }

    public List<File> getContractFiles(QueryCriteria query, Integer contractId) throws DatabaseException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        return fileService.getFiles(query, ObjectTypes.CONTRACT, contractId);
    }

    public File getContractFile(Integer contractId, Integer fileId) throws DatabaseException, ObjectNotFoundException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        File file = fileService.getFile(ObjectTypes.CONTRACT, contractId, fileId);
        file.setConfigRepositoryPath(ConfigManager.file.getContractFileRepositoryLocation());
        file.setConfigUploadedFilePrefix(ConfigManager.file.getContractUploadedFilePrefix());
        return file;
    }

    /**
     * Contract contacts
     * @param query
     * @param contractId
     * @return
     * @throws DatabaseException
     */
    public List<Contact> getContractContacts(QueryCriteria query, Integer contractId) throws DatabaseException {
        ContractContactLink link = new ContractContactLink();
        link.setContractId(contractId);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        return contactService.getLinkedContacts(query, link.createObjectMap());
    }

    public ActionMessages addContractContact(ContractContactLink contactLink) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.addObjectMapping(contactLink.createObjectMap());
    }

    public ActionMessages deleteContractContact(ContractContactLink contactLink) throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        return systemService.deleteObjectMapping(contactLink.createObjectMap());
    }
    
    /**
     * Sends contract expiration notification.
     * @throws DatabaseException 
     * @throws ObjectNotFoundException 
     */
    public void sendContractExpirationReminder(Contract contract, int nextThreshold) throws DatabaseException {
        AccessUser contractOwner = new CacheManager(requestContext).getUserCache(contract.getOwnerId());
        boolean hasPermission = false;
        
        if (contractOwner == null) {
            LOGGER.warning(LogConfigManager.SCHEDULER_PREFIX + " Contract owner ID " + contract.getOwnerId() 
            + " was not found. Skipping notification.");

        } else {
            hasPermission = contractOwner.hasPermission(Permissions.CONTRACT_READ_PERMISSION);
            
            if (!hasPermission) {
                LOGGER.warning(LogConfigManager.SCHEDULER_PREFIX + " Contract owner " + contractOwner.getDisplayName()
                + " has no permission to contract " + contract.getName() + ". Skipping notification.");

            } else {
                try {
                    EmailMessage message = new EmailMessage();
        
                    // Set TO field
                    message.getToField().add(contractOwner.getEmail());
        
                    // Set FROM field
                    message.setFromField(ConfigManager.email.getSmtpFrom());
    
                    String contractExpirationDate = ContractUtils.formatExpirationDateText(requestContext, requestContext.getSysdate(),
                            contract.getExpireDate());
        
                    // Set SUBJECT field
                    String subject = Localizer.getText(requestContext, "contracts.expirationNotification.subject", 
                            new String[]{contract.getName()});
        
                    message.setSubjectField(subject);
        
                    // Set BODY field
                    String body = ConfigManager.email.getContractExpireNotificationEmailTemplate().isEmpty() ? 
                            Localizer.getText(requestContext, "contracts.expirationNotification.body", 
                            new String[]{String.valueOf(contract.getId()), contract.getName(), contractExpirationDate}) : 
                                ConfigManager.email.getContractExpireNotificationEmailTemplate();
                    
                    AttributeManager attributeManager = new AttributeManager(requestContext);
                    
                    body = body.replace(Keywords.CONTRACT_NAME_VAR, contract.getName())
                            .replace(Keywords.CONTRACT_EXPIRATION_VAR, contractExpirationDate)
                            .replace(Keywords.CONTRACT_DESCRIPTION_VAR, contract.getDescription())
                            .replace(Keywords.CONTRACT_TYPE_VAR, attributeManager.getAttrFieldNameCache(Attributes.CONTRACT_TYPE, contract.getType()))
                            .replace(Keywords.CONTRACT_URL_VAR, ConfigManager.system.getAppUrl() + AppPaths.ROOT + AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
                    
                    message.setBodyField(body);
                    
                    // Debug:
    //                 logger.log(Level.INFO, "From: " + message.getFromField());
    //                 logger.log(Level.INFO, "To: " + message.getToField());
    //                 logger.log(Level.INFO, "CC: " + message.getCcField());
    //                 logger.log(Level.INFO, "Subject: " + message.getSubjectField());
    //                 logger.log(Level.INFO, "Message: " + message.getBodyField());
                
                     new SmtpService().send(message);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Problem sending contract expiration notification", e);
                }
            }
        }
        
        // Update database to not process the same contract again.
        ContractDao contractDao = new ContractDao(requestContext);
        contractDao.updateContractNotification(contract, nextThreshold);
    }
}
