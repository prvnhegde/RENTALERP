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
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.test.cases.KwokTestCase;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;

import org.apache.struts.action.ActionMessages;

/**
 * SoftwareServiceTest class.
 */
public class SoftwareServiceTest extends KwokTestCase {

    public SoftwareServiceTest(RequestContext requestContext) {
        super(requestContext);
    }

    public void execute() throws Exception {
        testGetSoftwareCount();
        testAddSoftware();
    }

    public void testGetSoftwareCount() throws DatabaseException {
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        int count;
        try {
            count = softwareService.getSoftwareCount(new QueryCriteria());
        } catch (Exception e) {
            count = -1;
        }
        addResult("Number of software:" + count, (count != -1));
    }

    public void testAddSoftware() throws DatabaseException {
        Software software = new Software();
        software.setName("Test Software " + System.currentTimeMillis());

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        // Add the software
        ActionMessages errors = softwareService.addSoftware(software, null);
        addResult("Adding software... " + software.getName() + ", " + errors, errors.isEmpty());
    }
}
