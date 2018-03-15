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
package com.kwoksys.framework.util;

/**
 * NumberUtils.
 */
public class NumberUtils {
    /**
     * Checks whether a given input is integer.
     */
    public static boolean isInteger(String temp) {
        try {
            // We catch format error here.
            Integer.parseInt(temp);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether a given input is a non-negative integer.
     */
    public static boolean isNonNegativeInteger(String temp) {
        return (isInteger(temp) && Integer.parseInt(temp) >= 0);
    }

    /**
     * If the given object is null or empty, returns the defaultValue.
     *
     * @param temp
     * @param defaultValue
     * @return ..
     */
    public static Integer replaceNull(String temp, Integer defaultValue) {
        if (temp == null || temp.isEmpty()) {
            return defaultValue;
        } else {
            try {
                // Catch format error here
                return Integer.valueOf(temp);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }

    /**
     * Returns integer value of the given object.
     */
    public static Integer replaceNull(Object temp) {
        return replaceNull(String.valueOf(temp), 0);
    }

    public static Integer replaceNull(String temp) {
        return replaceNull(temp, 0);
    }

    public static double setDefaultDouble(Object temp, double defaultValue) {
        if (temp == null || temp.equals("")) {
            return defaultValue;
        } else {
            return Double.parseDouble(String.valueOf(temp));
        }
    }

    public static double setDefaultDouble(Object temp) {
        return setDefaultDouble(temp, 0);
    }
}
