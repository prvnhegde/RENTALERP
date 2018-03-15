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

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;

/**
 * IssueServiceTest class.
 */
public class IssueServiceTest extends KwokTestCase {

    public IssueServiceTest(RequestContext requestContext) {
        super(requestContext);
    }

    public void execute() throws Exception {
        testGetIssueCount();
        testAddIssue();
    }

    public void testGetIssueCount() throws DatabaseException {
        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        try {
            int count = issueService.getCount(new QueryCriteria());
            addResult("Number of issues:" + count, true);

        } catch (Exception e) {
            addResult("Number of issues failed: " + e.getMessage(), false);
        }
    }

    public void testAddIssue() throws DatabaseException {
        Issue issue = new Issue();
        issue.setSubject("Test issue");
        issue.setDescription("This is an issue test.");

        IssueService issueService = ServiceProvider.getIssueService(requestContext);

        // Add the issue
        ActionMessages errors = issueService.addIssue(issue, null);
        addResult("Adding issue...", errors.isEmpty());
    }
}
