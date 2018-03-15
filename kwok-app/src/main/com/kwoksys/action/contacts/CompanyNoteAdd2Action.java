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

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.CompanyNote;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.struts2.Action2;
import org.apache.struts.action.ActionMessages;

/**
 * Action class for adding company note.
 */
public class CompanyNoteAdd2Action extends Action2 {

    public String execute() throws Exception {
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        CompanyNoteAddForm actionForm = saveActionForm(new CompanyNoteAddForm());

        Integer companyId = actionForm.getCompanyId();

        // Check whether the object exists
        contactService.getCompany(companyId);

        // Instantiate CompanyBookmarkDAO class
        CompanyNote note = new CompanyNote(companyId);
        note.setNoteName(actionForm.getNoteName());
        note.setNoteDescription(actionForm.getNoteDescription());
        note.setNoteTypeId(actionForm.getNoteType());
        note.setCompanyId(companyId);

        ActionMessages errors = contactService.addCompanyNote(note);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_NOTE_ADD + "?companyId=" + companyId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset company note count.
            contactService.resetCompanyNoteCount(companyId);

            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_NOTE + "?companyId=" + companyId + "&noteId=" + note.getNoteId());
        }
    }
}
