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
package com.kwoksys.action.contracts;

import java.io.FileNotFoundException;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.files.FileAddTemplate;
import com.kwoksys.action.files.FileDeleteTemplate;
import com.kwoksys.action.files.FileEditTemplate;
import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.contracts.dto.ContractFile;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for managing contract files.
 */
public class ContractFileAction extends Action2 {

    public String add() throws Exception {
        // Get request parameters
        Integer contractId = requestContext.getParameter("contractId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        //
        // Template: ContractSpecTemplate
        //
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));

        //
        // Template: FileAddTemplate
        //
        FileAddTemplate fileAdd = standardTemplate.addTemplate(new FileAddTemplate(getBaseForm(FileUploadForm.class)));
        fileAdd.setFileName(requestContext.getParameterString("fileName0"));
        fileAdd.setFormAction(AppPaths.CONTRACTS_FILE_ADD_2 + "?contractId=" + contractId);
        fileAdd.setFormCancelAction(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contractId);
        fileAdd.getErrorsTemplate().setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add2() throws Exception {
        Integer contractId = requestContext.getParameter("contractId");

        FileUploadForm actionForm = getBaseForm(FileUploadForm.class);

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        File file = new ContractFile(contractId);

        FileService fileService = ServiceProvider.getFileService(requestContext);
        ActionMessages errors = fileService.addFile(file, actionForm);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.CONTRACTS_FILE_ADD + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&contractId=" + contractId);

        } else {
            return redirect(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
        }
    }
    
    public String delete() throws Exception {
        Integer contractId = requestContext.getParameter("contractId");
        Integer fileId = requestContext.getParameter("fileId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        File file = contractService.getContractFile(contractId, fileId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("contractId", contractId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new Object[] {contract.getName()});

        //
        // Template: ContractSpecTemplate
        //
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));

        //
        // Template: FileDeleteTemplate
        //
        FileDeleteTemplate fileDelete = standardTemplate.addTemplate(new FileDeleteTemplate());
        fileDelete.setFile(file);
        fileDelete.setFormAction(AppPaths.CONTRACTS_FILE_DELETE_2 + "?contractId=" + contractId + "&fileId=" + file.getId());
        fileDelete.setFormCancelAction(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contractId);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String delete2() throws Exception {
        Integer contractId = requestContext.getParameter("contractId");
        Integer fileId = requestContext.getParameter("fileId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        // Instantiate File.
        File file = contractService.getContractFile(contract.getId(), fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete the file
        fileService.deleteFile(file);

        return ajaxUpdateView(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contractId);
    }
    
    public String download() throws Exception {
        ResponseContext responseContext = new ResponseContext(response);

        try {
            // Get request parameters
            Integer contractId = requestContext.getParameter("contractId");
            Integer fileId = requestContext.getParameter("fileId");

            ContractService contractService = ServiceProvider.getContractService(requestContext);
            contractService.getContract(contractId);

            File file = contractService.getContractFile(contractId, fileId);

            FileService fileService = ServiceProvider.getFileService(requestContext);
            fileService.download(responseContext, file);

        } catch (ObjectNotFoundException e) {
            throw new FileNotFoundException();
        }
        return null;
    }
    
    public String edit() throws Exception {
        Integer contractId = requestContext.getParameter("contractId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = contractService.getContractFile(contractId, fileId);
       
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.contractDetail.header", new String[] {contract.getName()});

        //
        // Template: ContractSpecTemplate
        //
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));

        // Template: FileEditTemplate
        FileEditTemplate fileEdit = standardTemplate.addTemplate(new FileEditTemplate());
        fileEdit.setFile(file);
        fileEdit.setFormAction(AppPaths.CONTRACTS_FILE_EDIT_2 + "?contractId=" + contractId + "&fileId=" + fileId);
        fileEdit.setFormCancelAction(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contractId);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = fileEdit.getErrorsTemplate();
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        FileUploadForm actionForm = saveActionForm(new FileUploadForm());
        
        Integer contractId = requestContext.getParameter("contractId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);
        Contract contract = contractService.getContract(contractId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = contractService.getContractFile(contract.getId(), fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Update the file
        ActionMessages errors = fileService.updateFile(file, actionForm);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTRACTS_FILE_EDIT + "?contractId=" + contract.getId() + "&fileId=" + fileId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contractId);
        }
    }
}
