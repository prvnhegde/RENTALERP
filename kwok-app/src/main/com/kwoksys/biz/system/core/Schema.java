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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Schema
 */
public class Schema {

    public static final String ACCESS_USER_DISPLAY_NAME = "access_user.display_name";
    public static final String ASSET_HARDWARE_MODEL_NAME = "asset_hardware.hardware_model_name";
    public static final String ASSET_HARDWARE_MODEL_NUMBER = "asset_hardware.hardware_model_number";
    public static final String ASSET_HARDWARE_NAME = "asset_hardware.hardware_name";
    public static final String ASSET_HARDWARE_SERIAL_NUMBER = "asset_hardware.hardware_serial_number";
    public static final String COMPANY_NAME = "company.company_name";
    public static final String CONTACT_ADDRESS_STREET = "contact.address_street_primary";
    public static final String CONTACT_ADDRESS_CITY = "contact.address_city_primary";
    public static final String CONTACT_ADDRESS_STATE = "contact.address_state_primary";
    public static final String CONTACT_FIRST_NAME = "contact.first_name";
    public static final String CONTACT_LAST_NAME = "contact.last_name";
    public static final String CONTACT_HOME_PHONE = "contact.contact_phone_home";
    public static final String CONTACT_CELL_PHONE = "contact.contact_phone_mobile";
    public static final String CONTACT_WORK_PHONE = "contact.contact_phone_work";
    public static final String CONTACT_FAX = "contact.contact_fax";
    public static final String CONTACT_PRIMARY_EMAIL = "contact.contact_email_primary";
    public static final String CONTACT_SECONDARY_EMAIL = "contact.contact_email_secondary";
    public static final String CONTACT_MESSENGER_1_ID = "contact.messenger_1_id";
    public static final String CONTACT_MESSENGER_2_ID = "contact.messenger_2_id";
    public static final String CONTRACT_NAME = "contract.contract_name";
    public static final String ISSUE_DESCRIPTION = "issue.issue_description";
    public static final String ISSUE_NAME = "issue.issue_name";
    public static final String KB_ARTICLE_NAME = "kb_article.article_name";
    public static final String SOFTWARE_NAME = "asset_software.software_name";

    /**
     * e.g. <access_user.display_name, 50>
     */
    public static Map<String, Integer> columnMap = new LinkedHashMap<>();

    static {
        columnMap.put(ACCESS_USER_DISPLAY_NAME, 50);
        columnMap.put(ASSET_HARDWARE_MODEL_NAME, 50);
        columnMap.put(ASSET_HARDWARE_MODEL_NUMBER, 50);
        columnMap.put(ASSET_HARDWARE_NAME, 100);
        columnMap.put(ASSET_HARDWARE_SERIAL_NUMBER, 50);
        columnMap.put(COMPANY_NAME, 100);
        columnMap.put(CONTACT_ADDRESS_STREET, 50);
        columnMap.put(CONTACT_ADDRESS_CITY, 50);
        columnMap.put(CONTACT_ADDRESS_STATE, 50);
        columnMap.put(CONTACT_FIRST_NAME, 50);
        columnMap.put(CONTACT_LAST_NAME, 50);
        columnMap.put(CONTACT_HOME_PHONE, 50);
        columnMap.put(CONTACT_CELL_PHONE, 50);
        columnMap.put(CONTACT_WORK_PHONE, 50);
        columnMap.put(CONTACT_FAX, 50);
        columnMap.put(CONTACT_PRIMARY_EMAIL, 50);
        columnMap.put(CONTACT_SECONDARY_EMAIL, 50);
        columnMap.put(CONTACT_MESSENGER_1_ID, 50);
        columnMap.put(CONTACT_MESSENGER_2_ID, 50);
        columnMap.put(CONTRACT_NAME, 100);

        // This limit is applied to the UI only. The issue description column in the database is defined as text type,
        // which supports a very large length.
        columnMap.put(ISSUE_DESCRIPTION, 4000);

        columnMap.put(ISSUE_NAME, 120);
        columnMap.put(KB_ARTICLE_NAME, 225);
        columnMap.put(SOFTWARE_NAME, 100);
    }

    public static int getColumnLength(String columnName) {
        return columnMap.get(columnName);
    }
}
