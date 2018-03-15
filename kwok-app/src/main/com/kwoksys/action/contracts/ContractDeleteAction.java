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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting contract.
 */
public class ContractDeleteAction extends Action2 {

    public String delete() throws Exception {
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
        header.setPageTitleKey("itMgmt.cmd.contractDelete");

        //
        // Template: ContractSpecTemplate
        //
        standardTemplate.addTemplate(new ContractSpecTemplate(contract));

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = standardTemplate.addTemplate(new ObjectDeleteTemplate());
        delete.setFormAjaxAction(AppPaths.CONTRACTS_DELETE_2 + "?contractId=" + contract.getId());
        delete.setFormCancelAction(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
        delete.setTitleKey("itMgmt.cmd.contractDelete");
        delete.setConfirmationMsgKey("itMgmt.contractDelete.confirm");
        delete.setSubmitButtonKey("itMgmt.contractDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String delete2() throws Exception {
        Integer contractId = requestContext.getParameter("contractId");

        ContractService contractService = ServiceProvider.getContractService(requestContext);

        // Check to make sure the contract exists
        contractService.getContract(contractId);

        ActionMessages errors = contractService.deleteContract(contractId);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTRACTS_DELETE + "?" + RequestContext.URL_PARAM_ERROR_TRUE + "&contractId=" + contractId);
            
        } else {
            return ajaxUpdateView(AppPaths.CONTRACTS_LIST);
        }
    }
}
