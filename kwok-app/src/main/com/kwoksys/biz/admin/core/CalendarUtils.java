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
package com.kwoksys.biz.admin.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.NumberUtils;

/**
 * Calendar utility functions.
 */
public class CalendarUtils {

    public static final String[] DATE_LIST = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    public static final String[] MONTH_LIST = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    public static final String[] WEEKDAY_LIST = {"0", "1", "2", "3", "4", "5", "6"};

    /**
     * I don't know if there is such a thing as valid year.
     * I guess I need to make up something.
     *
     * @return ..
     */
    public static boolean isValidYear(String reqYear) {
        if (!NumberUtils.isInteger(reqYear)) {
            return false;
        }

        int minYear = ConfigManager.app.getCalendarMinYear();   // Minimum year is 1970.
        Calendar calendar = new GregorianCalendar();
        int curYear = calendar.get(Calendar.YEAR);
        int maxYear = curYear + ConfigManager.app.getCalendarMaxYearPlus();  // Maximum is current year + 20.

        int year = Integer.parseInt(reqYear);
        return (year > minYear && year < maxYear);
    }

    public static int getCurYear() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.YEAR);
    }

    public static List<LabelValueBean> getDateOptions(RequestContext requestContext) {
        List<LabelValueBean> dateOptions = new ArrayList<>();
        dateOptions.add(new LabelValueBean(Localizer.getText(requestContext, "common.calendar.selectDate"), ""));
        for (String date : DATE_LIST) {
            dateOptions.add(new LabelValueBean(date, date));
        }
        return dateOptions;
    }

    public static List<LabelValueBean> getMonthOptions(RequestContext requestContext) {
        List<LabelValueBean> monthOptions = new ArrayList<>();
        monthOptions.add(new LabelValueBean(Localizer.getText(requestContext, "common.calendar.selectMonth"), ""));
        for (String month : MONTH_LIST) {
            monthOptions.add(new LabelValueBean(Localizer.getText(requestContext, "common.calendar.month." + month), month));
        }
        return monthOptions;
    }

    /**
     * Creates an option list.
     * @param request
     * @return
     */
    public static List<LabelValueBean> getYearOptions(RequestContext requestContext) {
        return getExtraYearOptions(requestContext, 0);
    }

    /**
     * Creates an option list and allows adding a previous year if necessary.
     * @param request
     * @param previousYear
     * @return
     */
    public static List<LabelValueBean> getExtraYearOptions(RequestContext requestContext, int previousYear) {
        int startYear = CalendarUtils.getCurYear() - ConfigManager.app.getNumPastYears();
        int endYear = CalendarUtils.getCurYear() + ConfigManager.app.getNumFutureYears();

        List<LabelValueBean> yearOptions = new ArrayList<>();
        yearOptions.add(new LabelValueBean(Localizer.getText(requestContext, "common.calendar.selectYear"), ""));

        if (previousYear != 0 && previousYear < startYear) {
            yearOptions.add(new LabelValueBean(String.valueOf(previousYear), String.valueOf(previousYear)));
        }

        for (int year = startYear; year <= endYear; year++) {
            yearOptions.add(new LabelValueBean(String.valueOf(year), String.valueOf(year)));
        }
        return yearOptions;
    }

    /**
     * Create an option list. This is for the case where only past years are relevant.
     * @param request
     * @return
     */
    public static List<LabelValueBean> getPastYearOptions(RequestContext requestContext) {
        return getExtraPastYearOptions(requestContext, 0);
    }

    /**
     * Creates an option list. This is for the case where only past years are relevant. This allows adding a
     * previous year if necessary.
     * @param request
     * @param previousYear
     * @return
     */
    public static List<LabelValueBean> getExtraPastYearOptions(RequestContext requestContext, int previousYear) {
        int startYear = CalendarUtils.getCurYear() - ConfigManager.app.getNumPastYears();

        List<LabelValueBean> yearOptions = new ArrayList<>();
        yearOptions.add(new LabelValueBean(Localizer.getText(requestContext, "common.calendar.selectYear"), ""));

        for (int year = CalendarUtils.getCurYear(); year >= startYear; year--) {
            yearOptions.add(new LabelValueBean(String.valueOf(year), String.valueOf(year)));
        }

        if (previousYear != 0 && previousYear < startYear) {
            yearOptions.add(new LabelValueBean(String.valueOf(previousYear), String.valueOf(previousYear)));
        }
        return yearOptions;
    }
}
