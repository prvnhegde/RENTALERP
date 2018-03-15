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

import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CurrencyUtils
 */
public class CurrencyUtils {

    private static final Logger LOGGER = Logger.getLogger(CurrencyUtils.class.getName());

    public static String formatCurrency(double input, String currencySymbol) {
        if (input == 0) {
            return "";
        } else {
            DecimalFormat currency = new DecimalFormat(currencySymbol + "###,###,###.00");
            return currency.format(input);
        }
    }

    public static String formatCurrency(Object input, String currencySymbol) {
        if (input == null) {
            return "";
        }
        try {
            DecimalFormat currency = new DecimalFormat(currencySymbol + "###,###,###.00");
            return currency.format(NumberUtils.setDefaultDouble(input));

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Problem formatting currency " + input, e);
            return currencySymbol + String.valueOf(input);
        }
    }

    public static boolean isValidFormat(String input) {
        // \\d means decimal point.
        if (input != null && !input.isEmpty()) {
            try {
                NumberUtils.setDefaultDouble(input);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
