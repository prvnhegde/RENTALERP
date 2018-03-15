/*
 * Copyright 2017 Kwoksys
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
package com.kwoksys.test.cases;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.test.cases.KwokTestCase;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;

import org.apache.struts.action.ActionMessages;

/**
 * ContactServiceTest class.
 */
public class ContactServiceTest extends KwokTestCase {

    public ContactServiceTest(RequestContext requestContext) {
        super(requestContext);
    }

    public void execute() throws Exception {
        testGetCompanyCount();
        testAddCompany();
    }

    public void testGetCompanyCount() throws DatabaseException {
        ContactService contactService = ServiceProvider.getContactService(requestContext);
        int count;
        try {
            count = contactService.getCompanyCount(new QueryCriteria());
        } catch (Exception e) {
            count = -1;
        }
        addResult("Number of companies:" + count, (count != -1));
    }

    public void testAddCompany() throws DatabaseException {
        Company company = new Company();
        company.setName("Test Company " + System.currentTimeMillis());
        company.setDescription("This is company description.");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        ActionMessages errors = contactService.addCompany(company, null);
        addResult("Adding company... " + company.getName() + ", " + errors, errors.isEmpty());
    }
}
