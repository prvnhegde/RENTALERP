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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;

/**
 * We do some datetime conversion here.
 */
public class DatetimeUtils {

    public static final long ONE_DAY_MILLISECONDS = 24 * 60 * 60 * 1000;
    
    public static final long ONE_HOUR_MILLISECONDS = 60 * 60 * 1000;
    
    public static final long ONE_MINUTE_MILLISECONDS = 60 * 1000;
    
    public static final long ONE_SECOND_MILLISECONDS = 1000;
    
    public static Calendar newCalendar() {
        return new GregorianCalendar(ConfigManager.system.getTimezoneBase());
    }

    public static Calendar newLocalCalendar(RequestContext requestContext) {
        Calendar calendar = new GregorianCalendar(ConfigManager.system.getTimezoneLocal());
        
        calendar.setTimeInMillis(requestContext.getSysdate().getTime() 
                + DatetimeUtils.getTimeOffsetMs(requestContext.getSysdate()));
        return calendar;
    }

    /**
     * Converts milliseconds to Date object.
     * @param milliseconds
     * @return
     */
    public static Date newDate(long milliseconds) {
        Calendar calendar = new GregorianCalendar(ConfigManager.system.getTimezoneBase());
        calendar.setTimeInMillis(milliseconds);
        return calendar.getTime();
    }
    
    public static String createDatetimeString(String year, String month, String date) {
        if (year.isEmpty() || month.isEmpty() || date.isEmpty()) {
            return "";
        } else {
            return year + "-" + month + "-" + date + " 00:00:00";
        }
    }

    /**
     * Gets Date object from resultset with GMT timezone.
     * @param datetime
     * @return
     */
    public static Date getDate(ResultSet rs, String column) throws SQLException {
        return rs.getTimestamp(column, newCalendar());
    }

    public static String toLocalDate(Date date) {
        return toLocalString(date, ConfigManager.system.getDateFormat());
    }

    public static String toLocalDatetime(Date date) {
        return toLocalString(date, ConfigManager.system.getDateFormat() + " " + ConfigManager.system.getTimeFormat());
    }

    public static String toLocalDatetime(long milliseconds) {
        return toLocalString(newDate(milliseconds), ConfigManager.system.getDateFormat() + " " + ConfigManager.system.getTimeFormat());
    }

    public static String toShortDate(Date date) {
        return toString(date, ConfigManager.system.getDateFormat());
    }

    public static String toShortDate(long milliseconds) {
        return toString(newDate(milliseconds), ConfigManager.system.getDateFormat());
    }

    public static String toYearString(Date date) {
        return toString(date, "yyyy");
    }

    public static String toMonthString(Date date) {
        return toString(date, "MM");
    }

    public static String toDateString(Date date) {
        return toString(date, "dd");
    }

    private static String formatDate(Date date, String pattern, TimeZone timezone) {
        if (date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(timezone);
        return dateFormat.format(date);
    }

    /**
     * Format using local timezone, in Application Settings > Timezone.
     */
    private static String toLocalString(Date date, String pattern) {
        return formatDate(date, pattern, ConfigManager.system.getTimezoneLocal());
    }

    /**
     * Formats date using base timezone, GMT.
     */
    private static String toString(Date date, String pattern) {
        return formatDate(date, pattern, ConfigManager.system.getTimezoneBase());
    }

    /**
     * Converts datetime string to Date object with GMT timezone.
     * @param datetime
     * @return
     */
    public static Date parseDate(String datetime, String pattern) throws Exception {
        if (datetime == null || datetime.isEmpty()) {
            return null;
        }

        // Set it to the base timezone
        DateFormat dateFormat = new SimpleDateFormat(pattern);

        // must do this, so that the month/date don't wrap
        dateFormat.setLenient(false);
        dateFormat.setTimeZone(ConfigManager.system.getTimezoneBase());

        return dateFormat.parse(datetime);
    }

    public static boolean isValidDate(String year, String month, String date) {
        try {
            Calendar gc = newCalendar();
            // must do this, so that the month/date don't wrap
            gc.setLenient(false);
            gc.set(GregorianCalendar.YEAR, Integer.parseInt(year));
            // There is a -1 because month start with 0
            gc.set(GregorianCalendar.MONTH, Integer.parseInt(month)-1);
            gc.set(GregorianCalendar.DATE, Integer.parseInt(date));

            gc.getTime();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidDateString(String fieldValue) {
        try {
            DatetimeUtils.parseDate(fieldValue, ConfigManager.system.getDateFormat());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets time offset in milliseconds, including the adjustment for daylight saving.
     * @return
     */
    public static int getTimeOffsetMs(Date currentDate) {
        int offset = ConfigManager.system.getTimezoneLocal().getRawOffset();
        
        if (ConfigManager.system.getTimezoneLocal().inDaylightTime(currentDate)) {
            offset += ConfigManager.system.getTimezoneLocal().getDSTSavings();
        }
        
        return offset;
    }
    
    /**
     * Gets time offset in hours, including the adjustment for daylight saving.
     * @return
     */
    public static double getTimeOffsetHours(Date currentDate) {
        double offsetMs = getTimeOffsetMs(currentDate);
        return offsetMs/ONE_HOUR_MILLISECONDS;
    }
}
