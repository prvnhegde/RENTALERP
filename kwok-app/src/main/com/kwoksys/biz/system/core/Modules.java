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
package com.kwoksys.biz.system.core;

/**
 * This contains all module IDs. Each module in the system has an id, the ids are not stored in database.
 * This is more for highlighting the correct module tabs for different pages.
 */
public class Modules {

    public static final int HARDWARE = 1;

    public static final int SOFTWARE = 2;

    public static final int CONTRACTS = 3;

    public static final int ISSUES = 4;

    public static final int CONTACTS = 5;

    public static final int BLOGS = 6;

    public static final int PORTAL = 7;

    public static final int RSS = 8;

    public static final int ISSUE_PLUGIN = 9;

    public static final int ADMIN = 10;

    public static final int PREFERENCES = 11;

    public static final int AUTH = 12;

    public static final int HOME = 13;

    public static final int KNOWLEDGE_BASE = 14;

    public static final int REPORTS = 15;

    /**
     * Given a module id, returns the path of that module's index page.
     * @param moduleId
     * @return
     */
    public static String getModulePath(Integer moduleId) {
        String path = null;
        switch (moduleId) {
            case HARDWARE:
                path = AppPaths.HARDWARE_INDEX;
                break;
            case SOFTWARE:
                path = AppPaths.SOFTWARE_INDEX;
                break;
            case CONTRACTS:
                path = AppPaths.CONTRACTS_INDEX;
                break;
            case ISSUES:
                path = AppPaths.ISSUES_INDEX;
                break;
            case CONTACTS:
                path = AppPaths.CONTACTS_INDEX;
                break;
            case BLOGS:
                path = AppPaths.BLOG_INDEX;
                break;
            case PORTAL:
                path = AppPaths.PORTAL_SITE_LIST_INDEX;
                break;
            case RSS:
                path = AppPaths.RSS_FEED_LIST;
                break;
            case HOME:
                path = AppPaths.HOME_INDEX;
                break;
            case KNOWLEDGE_BASE:
                path = AppPaths.KB_INDEX;
                break;
            case REPORTS:
                path = AppPaths.REPORTS_TYPE_SELECT;
                break;
        }
        return path;
    }
}
