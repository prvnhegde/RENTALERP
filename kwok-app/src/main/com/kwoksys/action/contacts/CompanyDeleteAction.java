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
package com.kwoksys.action.contacts;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.ObjectDeleteTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for deleting company.
 */
public class CompanyDeleteAction extends Action2 {

    public String delete() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDelete.title");

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: ObjectDeleteTemplate
        //
        ObjectDeleteTemplate delete = standardTemplate.addTemplate(new ObjectDeleteTemplate());
        delete.setFormAjaxAction(AppPaths.CONTACTS_COMPANY_DELETE_2 + "?companyId=" + companyId);
        delete.setFormCancelAction(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + companyId);
        delete.setConfirmationMsgKey("contactMgmt.companyDelete.confirm");
        delete.setSubmitButtonKey("contactMgmt.companyDelete.submitButton");

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String delete2() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);

        // Check whether the object exists
        Company company = contactService.getCompany(companyId);

        ActionMessages errors = contactService.deleteCompany(company);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_DELETE + "?companyId=" + companyId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_LIST);
        }
    }
}
