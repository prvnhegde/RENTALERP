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
package com.kwoksys.biz.system;

import java.util.Date;

import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.DatetimeUtils;

/**
 * SystemUtil
 */
public class SystemUtils {

    public static String formatExpirationDateHtml(RequestContext requestContext, Date currentDate, Date expirationDate,
                                              int expirationCountdown) {
        String expireDateString = "";
        long daysLimit = expirationCountdown * DatetimeUtils.ONE_DAY_MILLISECONDS;

        if (expirationDate != null) {
            long diff = expirationDate.getTime() - (currentDate.getTime() + DatetimeUtils.getTimeOffsetMs(currentDate));
            expireDateString = DatetimeUtils.toShortDate(expirationDate);

            if (diff > daysLimit) {
                // Use expireDateString

            } else if (diff > 0 && diff < DatetimeUtils.ONE_DAY_MILLISECONDS) {
                expireDateString += "<span class=\"expiredDate\"> (" + Localizer.getText(requestContext,
                        "contracts.expiration.counter.lessThanOne") + ")</span>";

            } else if (diff > 0) {
                expireDateString += "<span class=\"expiredDate\"> (" + Localizer.getText(requestContext,
                        "contracts.expiration.counter.oneOrMore", new Object[]{diff/DatetimeUtils.ONE_DAY_MILLISECONDS}) + ")</span>";

            } else {
                expireDateString += "<span class=\"red\"> (" + Localizer.getText(requestContext,
                        "itMgmt.contractList.contract_expiration_date.expired") + ")</span>";
            }
        }
        return expireDateString;
    }
    
    public static String formatExpirationDateText(RequestContext requestContext, Date currentDate,
            Date expirationDate, int expirationCountdown) {
        
        String expireDateString = "";
        long daysLimit = expirationCountdown * DatetimeUtils.ONE_DAY_MILLISECONDS;

        if (expirationDate != null) {
            long diff = expirationDate.getTime() - (currentDate.getTime() + DatetimeUtils.getTimeOffsetMs(currentDate));
            expireDateString = DatetimeUtils.toShortDate(expirationDate);

            if (diff > daysLimit) {
                // Use expireDateString

            } else if (diff > 0 && diff < DatetimeUtils.ONE_DAY_MILLISECONDS) {
                expireDateString += " (" + Localizer.getText(requestContext, "contracts.expiration.counter.lessThanOne") + ")";

            } else if (diff > 0) {
                expireDateString += " (" + Localizer.getText(requestContext,
                        "contracts.expiration.counter.oneOrMore", new Object[] { diff / DatetimeUtils.ONE_DAY_MILLISECONDS }) + ")";

            } else {
                expireDateString += " ("
                        + Localizer.getText(requestContext, "itMgmt.contractList.contract_expiration_date.expired")
                        + ")";
            }
        }
        return expireDateString;
    }    
}
