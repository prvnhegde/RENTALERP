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

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.CompanyNote;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for adding company bookmark.
 */
public class CompanyNoteAddAction extends Action2 {

    public String execute() throws Exception {
        CompanyNoteAddForm actionForm = getBaseForm(CompanyNoteAddForm.class);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(actionForm.getCompanyId());

        CompanyNote note = new CompanyNote(company.getId());

        // If not a resubmit, set some defaults
        if (!actionForm.isResubmit()) {
            actionForm.setNote(note);
        }

        note.loadAttrs(requestContext);
        
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", company.getId());
        standardTemplate.setPathAttribute("formAction", AppPaths.CONTACTS_COMPANY_NOTE_ADD_2);
        standardTemplate.setAttribute("formCancelLink", Links.getCancelLink(requestContext, AppPaths.CONTACTS_COMPANY_NOTE + "?companyId=" + company.getId()).getString());
        standardTemplate.setAttribute("noteTypeOptions", new AttributeManager(requestContext).setOptional(true)
                .getActiveAttrFieldOptionsCache(Attributes.COMPANY_NOTE_TYPE));

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = standardTemplate.addTemplate(new ActionErrorsTemplate());
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}
