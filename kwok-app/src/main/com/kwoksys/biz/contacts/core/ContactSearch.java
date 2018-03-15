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
package com.kwoksys.biz.contacts.core;

import com.kwoksys.action.contacts.ContactSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.SqlUtils;

/**
 * This is for building search queries.
 */
public class ContactSearch extends BaseSearch {

    public static final String CONTACT_ID_EQUALS = "contactIdEquals";
    public static final String CONTACT_TYPE = "companyContactType";

    /**
     * This would generate searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(ContactSearchForm contactSearchForm) {
        // Search by first name matches.
        String firstNameEquals = contactSearchForm.getContactFirstName();
        if (!firstNameEquals.isEmpty()) {
            searchCriteriaMap.put("firstNameMatches", firstNameEquals);
        }
        // Search by last name matches.
        String lastNameEquals = contactSearchForm.getContactLastName();
        if (!lastNameEquals.isEmpty()) {
            searchCriteriaMap.put("lastNameMatches", lastNameEquals);
        }
        // Search by contact title matches.
        String titleEquals = contactSearchForm.getContactTitle();
        if (!titleEquals.isEmpty()) {
            searchCriteriaMap.put("titleMatches", titleEquals);
        }
        // Search by any email matches.
        String emailEquals = contactSearchForm.getContactEmail();
        if (!emailEquals.isEmpty()) {
            searchCriteriaMap.put("emailMatches", emailEquals);
        }
    }

    public void applyMap(QueryCriteria query) {
        if (searchCriteriaMap == null) {
            return;
        }
        // Search by Company contact type.
        if (searchCriteriaMap.containsKey(CONTACT_TYPE)) {
            query.appendWhereClause("c.company_contact_type = " + SqlUtils.encodeString(searchCriteriaMap.get(CONTACT_TYPE)) + "");
        }
        // Search by first name matches.
        if (searchCriteriaMap.containsKey("firstNameMatches")) {
            query.appendWhereClause("lower(c.contact_first_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("firstNameMatches")) + "%')");
        }
        // Search by first name matches.
        if (searchCriteriaMap.containsKey("lastNameMatches")) {
            query.appendWhereClause("lower(c.contact_last_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("lastNameMatches")) + "%')");
        }
        // Search by contact title matches.
        if (searchCriteriaMap.containsKey("titleMatches")) {
            query.appendWhereClause("lower(c.contact_title) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("titleMatches")) + "%')");
        }
        // Search by any email matches.
        if (searchCriteriaMap.containsKey("emailMatches")) {
            query.appendWhereClause("(lower(c.contact_email_primary) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("emailMatches"))
                    + "%') or lower(c.contact_email_secondary) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("emailMatches")) + "%'))");
        }

        if (searchCriteriaMap.containsKey(CONTACT_ID_EQUALS)) {
            query.appendWhereClause("c.contact_id=" + SqlUtils.encodeInteger(searchCriteriaMap.get(CONTACT_ID_EQUALS)));
        }
    }
}
