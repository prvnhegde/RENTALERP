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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.framework.http.RequestContext;

/**
 *
 */
public abstract class KwokTestCase {

    private static final Logger LOGGER = Logger.getLogger(KwokTestCase.class.getName());
    
    protected RequestContext requestContext;

    public KwokTestCase(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public abstract void execute() throws Exception;

    public void addResult(String message, boolean pass) {
        LOGGER.log(Level.INFO, LogConfigManager.TEST_PREFIX + " [{0}] {1} [{2}]", 
                new String[] {(pass ? "passed" : "failed"), message, getClass().getCanonicalName()});
    }
}
