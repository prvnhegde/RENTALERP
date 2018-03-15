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
package com.kwoksys.test;

import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.test.cases.BrowserValidatorTest;
import com.kwoksys.test.cases.ContactServiceTest;
import com.kwoksys.test.cases.CustomFieldsTest;
import com.kwoksys.test.cases.HardwareServiceTest;
import com.kwoksys.test.cases.IssueEmailTest;
import com.kwoksys.test.cases.IssueServiceTest;
import com.kwoksys.test.cases.KwokTestCase;
import com.kwoksys.test.cases.SoftwareServiceTest;
import com.kwoksys.test.cases.StringUtilTest;
import com.kwoksys.test.cases.UrlRequestTest;

/**
 * 
 */
public class MainTest extends KwokTestCase {

    public MainTest(RequestContext requestContext) {
        super(requestContext);
    }

    public void execute() throws Exception {
        KwokTestCase[] testCases = {
                new ContactServiceTest(requestContext),
                new CustomFieldsTest(requestContext),
                new HardwareServiceTest(requestContext),
                new IssueEmailTest(requestContext),
                new IssueServiceTest(requestContext),
                new SoftwareServiceTest(requestContext),
                new UrlRequestTest(requestContext),
                new StringUtilTest(requestContext),
                new BrowserValidatorTest(requestContext)
        };
        for (KwokTestCase testCase : testCases) {
            testCase.execute();
        }
    }
}