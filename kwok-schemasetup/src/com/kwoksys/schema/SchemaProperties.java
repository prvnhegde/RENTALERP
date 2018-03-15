/*
 * Copyright 2015 Kwoksys
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
package com.kwoksys.schema;

import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * This is for getting application properties.
 */
public class SchemaProperties {

    private static final Logger logger = Logger.getLogger(SchemaProperties.class.getName());

    public static final String SCHEMA_VERSION = "schema.version";

    private static Properties props;

    static {
        if (props == null) {
            logger.info("Loading schemasetup properties...");

            try {
                props = new Properties();
                props.load(SchemaProperties.class.getResourceAsStream("Schema.properties"));

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Problem loading property file.", e);
            }
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
