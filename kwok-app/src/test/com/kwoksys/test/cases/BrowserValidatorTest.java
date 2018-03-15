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

import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.validations.BrowserValidator;

/**
 * BrowserValidatorTest
 */
public class BrowserValidatorTest extends KwokTestCase {

    public BrowserValidatorTest(RequestContext requestContext) {
        super(requestContext);
    }

    public static void main(String args[]) throws Exception {
        new BrowserValidatorTest(null).execute();
    }

    public void execute() throws Exception {
        /// Edge
        String userAgent = "Edge/14.14393";
        addResult(userAgent, BrowserValidator.isSupportedBrowser(userAgent) == true);
        
        // IE 11
        userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko";
        addResult(userAgent, BrowserValidator.isSupportedBrowser(userAgent) == true);
        
        // IE 10.6 
        userAgent = "Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1)";
        addResult(userAgent, BrowserValidator.isSupportedBrowser(userAgent) == true);
        
        // IE 10
        userAgent = "Mozilla/4.0 (Compatible; MSIE 8.0; Windows NT 5.2; Trident/6.0)";
        addResult(userAgent, BrowserValidator.isSupportedBrowser(userAgent) == true);
        
        // IE 10
        userAgent = "Mozilla/4.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/5.0)";
        addResult(userAgent, BrowserValidator.isSupportedBrowser(userAgent) == true);
        
        // IE 9
        userAgent = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
        addResult(userAgent, BrowserValidator.isSupportedBrowser(userAgent) == false);

        // IE 6
        userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)";
        addResult(userAgent, BrowserValidator.isSupportedBrowser(userAgent) == false);

        // Firefox 40
        userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0";
        addResult(userAgent, BrowserValidator.isSupportedBrowser(userAgent) == true);

        // Safari
        userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";
        addResult(userAgent, BrowserValidator.isSupportedBrowser(userAgent) == true);
    }
}
