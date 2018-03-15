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

import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.CompanyNote;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.software.dto.Software;

import java.util.HashMap;
import java.util.Map;

/**
 * AttributeTypes
 */
public class Attributes {

    public static final Integer COMPANY_TYPES = -18;

    public static final Integer COMPANY_NOTE_TYPE = -16;

    public static final Integer CONTACT_IM = -11;

    public static final Integer ISSUE_TYPE = -1;

    public static final Integer ISSUE_STATUS = -2;

    public static final Integer ISSUE_PRIORITY = -3;

    public static final Integer ISSUE_RESOLUTION = -4;

    public static final Integer ISSUE_ASSIGNEE = -20;

    public static final Integer SOFTWARE_OS = -7;

    public static final Integer SOFTWARE_TYPE = -8;

    public static final Integer HARDWARE_COMPONENT_TYPE = -17;

    public static final Integer HARDWARE_LOCATION = -6;

    public static final Integer HARDWARE_TYPE = -10;

    public static final Integer HARDWARE_STATUS = -12;

    public static final Integer CONTRACT_STAGE = -21;

    public static final Integer CONTRACT_TYPE = -14;

    public static final Integer CONTRACT_RENEWAL_TYPE = -15;

    public static final Integer KB_ARTICLE_SYNTAX_TYPE = -19;

    public static final Integer USER_STATUS_TYPE = -13;

    private static final Map<String, Integer> nameIdMap = new HashMap<>();

    static {
        nameIdMap.put(Company.TYPES, COMPANY_TYPES);
        nameIdMap.put(CompanyNote.COMPANY_NOTE_TYPE, COMPANY_NOTE_TYPE);

        nameIdMap.put(Contract.TYPE, CONTRACT_TYPE);
        nameIdMap.put(Contract.STAGE, CONTRACT_STAGE);
        nameIdMap.put(Contract.RENEWAL_TYPE, CONTRACT_RENEWAL_TYPE);

        nameIdMap.put(Hardware.LOCATION, HARDWARE_LOCATION);
        nameIdMap.put(Hardware.STATUS, HARDWARE_STATUS);
        nameIdMap.put(Hardware.TYPE, HARDWARE_TYPE);

        nameIdMap.put(Software.TYPE, SOFTWARE_TYPE);
        nameIdMap.put(Software.OS, SOFTWARE_OS);

        nameIdMap.put(Issue.ASSIGNEE, ISSUE_ASSIGNEE);
    }

    public static Map<String, Integer> getNameIdMap() {
        return nameIdMap;
    }
}
