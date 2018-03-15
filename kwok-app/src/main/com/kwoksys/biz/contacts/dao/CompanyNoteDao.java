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
package com.kwoksys.biz.contacts.dao;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.contacts.dto.CompanyNote;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * CompanyNoteDao
 */
public class CompanyNoteDao extends BaseDao {

    public CompanyNoteDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Returns all notes of a company record.
     *
     * @return ..
     */
    public List<CompanyNote> getNoteList(QueryCriteria query, Integer companyId) throws DatabaseException {
        List<CompanyNote> companyNotes = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(ContactQueries.selectCompanyNoteQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                CompanyNote companyNote = new CompanyNote(companyId);
                companyNote.setNoteId(rs.getInt("note_id"));
                companyNote.setNoteName(StringUtils.replaceNull(rs.getString("note_name")));
                companyNote.setNoteDescription(StringUtils.replaceNull(rs.getString("note_description")));
                companyNote.setNoteTypeId(rs.getInt("note_type"));
                companyNote.setCompanyId(rs.getInt("company_id"));
                companyNote.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));

                companyNotes.add(companyNote);
            }
        };
        
        queryHelper.addInputInt(companyId);

        executeQuery(queryHelper);
        
        return companyNotes;
    }

    /**
     * Add company note.
     *
     * @return ..
     */
    public ActionMessages addNote(CompanyNote note) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.insertCompanyNoteQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(note.getNoteName());
        queryHelper.addInputStringConvertNull(note.getNoteDescription());
        queryHelper.addInputInt(note.getNoteTypeId());
        queryHelper.addInputInt(note.getCompanyId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        if (errors.isEmpty()) {
            note.setNoteId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }
}
