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
package com.kwoksys.action.common.calendar;

import com.kwoksys.biz.admin.core.CalendarUtils;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.http.RequestContext;

import java.util.*;

/**
 * Action class for admin/calendar.jsp.
 */
public class CalendarAction {

    private final List<Map> calendarList = new ArrayList<>();
    private final List<String> weekdayList = new ArrayList<>();
    private final String prevPath = null;
    private final String nextPath = null;

    //
    // Setters
    //
    public void setRequest(RequestContext requestContext) {
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int date = 1;

        // We will also let user choose a different year.
        if (CalendarUtils.isValidYear(requestContext.getRequest().getParameter("year"))) {
            year = Integer.parseInt(requestContext.getRequest().getParameter("year"));
        }

        // Set previous/next year links and texts.
//        prevPath = AppPaths.WEB_CALENDAR_YEAR_PATH + "?year=" + (year - 1);
//        nextPath = AppPaths.WEB_CALENDAR_YEAR_PATH + "?year=" + (year + 1);

        Object[] argsMonthYear = {String.valueOf(year)};

        // We need to set some counter so that the jsp template knows when to wrap.
//        Counter monthCounter = new Counter();
//        monthCounter.setColumns(4);

        // We need to set some counter so that the jsp template knows when to wrap.
//        Counter dayCounter = new Counter();
//        dayCounter.setColumns(7);

        // Will change to a list of months, starting from 01, 02, etc.
        for (int month = 0; month < 12; month++) {
            Map<String, Object> map = new HashMap<>();
//            map.put("monthCounter", monthCounter.incrCounter());
            map.put("name", Localizer.getText(requestContext, "webCalendar.monthYear." + month, argsMonthYear));

            calendar = new GregorianCalendar(year, month, date);

//            dayCounter.setCounter(0);
            List<Map> dayList = new ArrayList<>();

            // Pad some empty spaces here.
            int numPads = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            for (int mDate = 1; mDate <= numPads; mDate++) {
                Map<String, String> dayMap = new HashMap<>();
//                dayMap.put("dayCounter", dayCounter.incrCounter());
                dayMap.put("date", "&nbsp;");
                dayList.add(dayMap);
            }
            // This is for dates.
            int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int mDate = 1; mDate <= numDays; mDate++) {
                Map<String, Object> dayMap = new HashMap<>();
//                dayMap.put("dayCounter", dayCounter.incrCounter());
                dayMap.put("date", mDate);
                dayList.add(dayMap);
            }
            map.put("dayList", dayList);

            calendarList.add(map);
        }

        for (int weekday = 0; weekday < 7; weekday++) {
            weekdayList.add(Localizer.getText(requestContext, "webCalendar.shortDay." + weekday));
        }
    }

    //
    // Getters
    //
    public List<Map> getCalendarList() {
        return calendarList;
    }
    public List<String> getWeekdayList() {
        return weekdayList;
    }
    public String getPrevPath() {
        return prevPath;
    }
    public String getNextPath() {
        return nextPath;
    }
}
