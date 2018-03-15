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

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.contacts.dto.CompanyNote;
import com.kwoksys.framework.http.RequestContext;

/**
 * ActionForm for adding company note.
 */
public class CompanyNoteAddForm extends BaseForm {

    private Integer companyId;
    private String noteName;
    private String noteDescription;
    private Integer noteType;
    private CompanyNote note;

    @Override
    public void setRequest(RequestContext requestContext) {
        companyId = requestContext.getParameterInteger("companyId");
        noteName = requestContext.getParameterString("noteName");
        noteDescription = requestContext.getParameterString("noteDescription");
        noteType = requestContext.getParameterInteger("noteType");
    }

    public void setNote(CompanyNote note) {
        this.note = note;
        noteName = note.getNoteName();
        noteDescription = note.getNoteDescription();
        noteType = note.getNoteTypeId();
    }

    public Integer getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    public String getNoteName() {
        return noteName;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public Integer getNoteType() {
        return noteType;
    }

    public CompanyNote getNote() {
        return note;
    }
}
