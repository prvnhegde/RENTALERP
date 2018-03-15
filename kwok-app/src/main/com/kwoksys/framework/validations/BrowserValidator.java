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
package com.kwoksys.framework.validations;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BrowserValidator
 */
public class BrowserValidator {

    private static final Logger LOGGER = Logger.getLogger(BrowserValidator.class.getName());

    private static final String TRIDENT = "TRIDENT"; // Trident/7.0 - > IE11, Trident/6.0 -> IE 10.
    private static final String MSIE = "MSIE"; 
    private static final String CHROME = "CHROME";
    private static final String FIREFOX = "FIREFOX";

    public static Map<String, Integer> minimumVersions = new HashMap<>();

    static {
        minimumVersions.put(TRIDENT, 6);
        minimumVersions.put(MSIE, 10);
        minimumVersions.put(CHROME, 30);
        minimumVersions.put(FIREFOX, 30);
    }

    /**
     * Defaults to true, unless it finds a pattern of non-supported browser header.
     * @param userAgent
     * @return
     */
    public static boolean isSupportedBrowser(String userAgent) {
        if (userAgent == null) { // Not much we can check here.
            return true;
        }
        try {
            // Internet Explorer has some inconsistent user agent usage of trident and msie strings.
            if (userAgent.contains("Trident/") || userAgent.contains("MSIE ")) {
                Pattern pattern = Pattern.compile(".*Trident/(.*?)\\.");

                Matcher m = pattern.matcher(userAgent);
                if (m.find()) {
                    Double tridentVersion = new Double(m.group(1));
                    
                    if (tridentVersion >= minimumVersions.get(TRIDENT)) {
                        return true;
                    }
                }

                pattern = Pattern.compile(".*MSIE (.*?)\\.");
                m = pattern.matcher(userAgent);
                
                if (m.find()) {
                    Double msieVersion = new Double(m.group(1));
                    
                    if (msieVersion >= minimumVersions.get(MSIE)) {
                        return true;
                    }
                }
                
                return false;
            }
            
            String browser = null;
            Double version = null;

            if (userAgent.contains("Firefox/")) {
                Pattern pattern = Pattern.compile(".*Firefox/(.*?)\\.");

                Matcher m = pattern.matcher(userAgent);
                if (m.find()) {
                    browser = FIREFOX;
                    version = new Double(m.group(1));
                }

            } else if (userAgent.contains("Chrome/")) {
                Pattern pattern = Pattern.compile(".*Chrome/(.*?)\\.");

                Matcher m = pattern.matcher(userAgent);
                if (m.find()) {
                    browser = CHROME;
                    version = new Double(m.group(1));
                }
            }

            if (version != null && version < minimumVersions.get(browser)) {
                return false;
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Problem parsing user-agent header: " + userAgent, e);
        }
        return true;
    }
}
