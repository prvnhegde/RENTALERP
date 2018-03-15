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
package com.kwoksys.biz.contacts.dao;

import com.kwoksys.framework.connections.database.QueryCriteria;

/**
 * This is for storing all Companies sql queries
 * Format: select_, insert_, update_, delete_, _Query
 */
public class ContactQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals("company_name")) {
            return "lower(company_name)";

        } else if (column.equals("contact_first_name")) {
            return "lower(contact_first_name)";

        } else if (column.equals("contact_last_name")) {
            return "lower(contact_last_name)";

        } else if (column.equals("contact_title")) {
            return "lower(contact_title)";

        } else if (column.equals("tag_name")) {
            return "lower(tag_name)";
        }
        return column;
    }

    /**
     * Return a list of Companies.
     */
    public static String selectCompanyListQuery(QueryCriteria query) {
        return "select c.company_id, c.company_name, c.company_description from company c " + query.createWhereClause();
    }

    /**
     * Return all linked Companies.
     */
    public static String selectLinkedCompanyListQuery(QueryCriteria query) {
        return selectCompanyListQuery(new QueryCriteria()) +
                "where c.company_id in (select om.linked_object_id from object_map om where om.object_id=? and om.object_type_id=? " +
                "and om.linked_object_type_id=?) "
                + query.createWhereClause();
    }

    public static String selectObjectCompanyListQuery(QueryCriteria query) {
        return selectCompanyListQuery(new QueryCriteria()) +
                "where c.company_id in (select om.object_id from object_map om where om.linked_object_id=? and om.linked_object_type_id=? " +
                "and om.object_type_id=?) "
                + query.createAndClause();
    }

    /**
     * Return a count of Companies.
     */
    public static String getCompanyCountQuery(QueryCriteria query) {
        return "select count(c.company_id) as row_count from company c " + query.createWhereCountClause();
    }

    /**
     * Return the detail of a Companies.
     *
     * @return ..
     */
    public static String selectCompanyDetailQuery() {
        return "select company_id, company_name, company_description, main_contact_count, employee_contact_count, file_count, bookmark_count, note_count, " +
                "creator, creation_date, creator_username, creator_display_name, " +
                "modifier, modification_date, modifier_username, modifier_display_name " +
                "from company_view " +
                "where company_id = ? ";
    }

    /**
     * Return a list of tags tagged to a specific Company.
     *
     * @return ..
     */
    public static String selectCompanyTagsQuery(QueryCriteria query) {
        return "select ctmv.tag_id, ctmv.tag_name from company_tag_map_view ctmv " +
                "where ctmv.company_id = ? " + query.createAndClause();
    }

    /**
     * Return a list of all existing Company tags.
     *
     * @return ..
     */
    public static String selectCompanyExistingTagsQuery(QueryCriteria query) {
        return "select ct.tag_id, ct.tag_name, ct.creation_date from company_tag ct " + query.createWhereClause();
    }

    /**
     * Select notes associated with a company.
     *
     * @return
     */
    public static String selectCompanyNoteQuery(QueryCriteria query) {
        return "select cn.note_id, cn.note_name, cn.note_description, cn.note_type, cn.company_id, cn.creator, cn.creation_date " +
                "from company_note cn " +
                "where cn.company_id = ? " + query.createAndClause();
    }

    /**
     * Return a list of Contacts. This has more columns than selectContactListQuery.
     *
     * @return ..
     */
    public static String selectExpandedContactListQuery(QueryCriteria query) {
        return selectExpandedContactsQuery() +
                "where cc.company_id=? and cc.company_contact_type=? "
                + query.createAndClause();
    }

    public static String selectContactsReportQuery(QueryCriteria query) {
        return selectExpandedContactsQuery() +
                "where cc.company_contact_type=? "
                + query.createAndClause();
    }

    public static String selectExpandedContactsQuery() {
        return "select cc.contact_id, cc.contact_first_name, cc.contact_last_name, cc.contact_title, " +
                "cc.contact_phone_home, cc.contact_phone_mobile, cc.contact_phone_work, cc.contact_description, " +
                "cc.contact_fax, cc.contact_email_primary, cc.contact_email_secondary, cc.contact_homepage_url, " +
                "c.company_name, c.company_id, cc.address_street_primary, cc.address_city_primary, " +
                "cc.address_state_primary, cc.address_zipcode_primary, cc.address_country_primary " +
                "from contact cc " +
                "left outer join company c on cc.company_id = c.company_id ";
    }

    /**
     * Return a list of Contacts.
     *
     * @return ..
     */
    public static String selectContactListQuery(QueryCriteria query) {
        return "select c.contact_id, c.contact_first_name, c.contact_last_name, c.contact_title, c.contact_email_primary, c.company_id, " +
                "c.company_name " +
                "from contact_view c " + query.createWhereClause();
    }

    /**
     * Return linked contacts.
     */
    public static String selectLinkedContactsQuery(QueryCriteria query) {
        return "select c.contact_id, c.contact_first_name, c.contact_last_name, c.contact_title, c.contact_email_primary, c.company_id, " +
                "c.company_name, om.relationship_name " +                
                "from contact_view c " +
                "join object_map om on c.contact_id=om.linked_object_id and om.object_id=? and om.object_type_id=? " +
                "and om.linked_object_type_id=? "
                + query.createWhereClause();
    }

    /**
     * Return a count of Contacts.
     *
     * @return ..
     */
    public static String getContactCountQuery(QueryCriteria query) {
        return "select count(c.contact_id) as row_count " +
                " from contact c " + query.createWhereCountClause();
    }

    /**
     * Return the detail of a Contact.
     *
     * @return ..
     */
    public static String selectContactDetailQuery() {
        return "select c.contact_id, c.contact_first_name, c.contact_last_name, c.contact_title, c.contact_phone_home, c.contact_phone_mobile, c.contact_phone_work, c.contact_description, " +
                "c.contact_fax, c.contact_email_primary, c.contact_email_secondary, c.contact_homepage_url, c.company_name, c.company_id, " +
                "c.address_street_primary, c.address_city_primary, c.address_state_primary, c.address_zipcode_primary, c.address_country_primary, " +
                "c.messenger_1_type, c.messenger_1_id, c.messenger_2_type, c.messenger_2_id, c.user_id, " +
                "c.creator, c.creation_date, c.creator_username, c.creator_display_name, " +
                "c.modifier, c.modification_date, c.modifier_username, c.modifier_display_name " +
                "from contact_view c where c.contact_id = ? ";
    }

    /**
     * Insert Company.
     */
    public static String insertCompanyQuery() {
        return "{call sp_company_add(?,?,?,?)}";
    }

    /**
     * Update Company.
     */
    public static String updateCompanyQuery() {
        return "{call sp_company_update(?,?,?,?)}";
    }

    /**
     * Delete company.
     */
    public static String deleteCompanyQuery() {
        return "{call sp_company_delete(?,?,?)}";
    }

    /**
     * Reset Company note counter.
     *
     * @return ..
     */
    public static String updateCompanyNoteCountQuery() {
        return "{call sp_company_count_note_update(?)}";
    }

    /**
     * Reset Company File counter.
     *
     * @return ..
     */
    public static String updateCompanyFileCountQuery() {
        return "{call sp_company_count_file_update(?,?)}";
    }

    /**
     * Update Company Bookmark count.
     *
     * @return ..
     */
    public static String updateCompanyBookmarkCountQuery() {
        return "{call sp_company_count_bookmark_update(?,?)}";
    }

    /**
     * Insert Company note.
     */
    public static String insertCompanyNoteQuery() {
        return "{call sp_company_note_add(?,?,?,?,?,?)}";
    }

    /**
     * Insert Company tags.
     */
    public static String insertCompanyTagQuery() {
        return "{call sp_company_tag_add(?,?,?,?)}";
    }

    /**
     * This deletes Company tags.
     */
    public static String deleteCompanyTagQuery() {
        return "{call sp_company_tag_delete(?,?)}";
    }

    /**
     * Insert Contact detail.
     */
    public static String insertContactQuery() {
        return "{call sp_contact_add(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Update Contact detail.
     */
    public static String updateContactQuery() {
        return "{call sp_contact_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Delete a Contact.
     */
    public static String deleteContactQuery() {
        return "{call sp_contact_delete(?,?,?,?)}";
    }
}
