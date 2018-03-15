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

import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.parsers.email.IssueEmailParser;
import com.kwoksys.test.cases.KwokTestCase;

/**
 *
 */
public class IssueEmailTest extends KwokTestCase {

    public IssueEmailTest(RequestContext requestContext) {
        super(requestContext);
    }

    public void execute() throws Exception {
        Issue issue = new Issue();
        issue.setId(1);
        issue.setSubject("This is a test.");
        testEmailSubject(issue);

        issue.setId(1234);
        testEmailSubject(issue);

        testEmailBody();
    }

    private void testEmailSubject(Issue issue) {
        String emailSubject = IssueUtils.formatEmailSubject(requestContext, issue);
        Integer issueId = IssueEmailParser.parseEmailIssueId(emailSubject);

        addResult("Parsing issue id " + issue.getId() + " from email subject... ", (issue.getId().equals(issueId)));
    }

    public static void main(String[] args) {
        new IssueEmailTest(null).testEmailBody();
    }
    public void testEmailBody() {
        String bodyMessage = "This is an email test.\nLine 2 starts here. \n         \n";

        String emailMessage = bodyMessage + IssueEmailParser.EMAIL_BODY_SEPARATOR + "\nReply above this line\n\nMore contents here.";
        System.out.println("Original message:=====\n\"" + emailMessage + "\"");

        System.out.println("\nOriginal body:=====\n\"" + bodyMessage.trim() + "\"");

        String parsedBody = IssueEmailParser.parseEmailBody(emailMessage);
        System.out.println("\nParsed body: =====\n\"" + parsedBody.trim() + "\"");

        addResult("Parsing email body..."  , bodyMessage.trim().equals(parsedBody.trim()));
    }
}
