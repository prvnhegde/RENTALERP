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
package com.kwoksys.framework.connections.database;

import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.StringUtils;

import java.util.List;

/**
 * SqlUtils
 */
public class SqlUtils {

    /**
     * Converts Integer List to a sql string (common seperated value)
     *
     * @param integers
     * @return ..
     */
    public static String encodeIntegers(List<Integer> integers) {
        return StringUtils.joinIntegers(integers, ",");
    }

    public static Integer encodeInteger(Object input) {
        return NumberUtils.replaceNull(input);
    }

    /**
     * Encodes string for passing to sql queries.
     *
     * @param input
     * @return ..
     */
    public static String encodeString(Object input) {
        return input.toString().replace("'", "''");
    }
}
