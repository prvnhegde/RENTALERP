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

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.StringUtils;

/**
 * CompanyContactDao
 */
public class CompanyContactDao extends BaseDao {

    public CompanyContactDao(RequestContext requestContext) {
        super(requestContext);
    }

    public List<Contact> getList(QueryCriteria query, Integer companyId, Integer companyContactType) throws DatabaseException {
        List<Contact> contacts = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper() {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Contact contact = new Contact();
                contact.setContactId(rs.getInt("contact_id"));
                contact.setCompanyId(rs.getInt("company_id"));
                contact.setCompanyName(StringUtils.replaceNull(rs.getString("company_name")));
                contact.setFirstName(StringUtils.replaceNull(rs.getString("contact_first_name")));
                contact.setLastName(StringUtils.replaceNull(rs.getString("contact_last_name")));
                contact.setDescription(StringUtils.replaceNull(rs.getString("contact_description")));
                contact.setTitle(StringUtils.replaceNull(rs.getString("contact_title")));
                contact.setPhoneHome(StringUtils.replaceNull(rs.getString("contact_phone_home")));
                contact.setPhoneMobile(StringUtils.replaceNull(rs.getString("contact_phone_mobile")));
                contact.setPhoneWork(StringUtils.replaceNull(rs.getString("contact_phone_work")));
                contact.setFax(StringUtils.replaceNull(rs.getString("contact_fax")));
                contact.setEmailPrimary(StringUtils.replaceNull(rs.getString("contact_email_primary")));
                contact.setEmailSecondary(StringUtils.replaceNull(rs.getString("contact_email_secondary")));
                contact.setHomepageUrl(StringUtils.replaceNull(rs.getString("contact_homepage_url")));
                contact.setAddressStreetPrimary(StringUtils.replaceNull(rs.getString("address_street_primary")));
                contact.setAddressCityPrimary(StringUtils.replaceNull(rs.getString("address_city_primary")));
                contact.setAddressStatePrimary(StringUtils.replaceNull(rs.getString("address_state_primary")));
                contact.setAddressZipcodePrimary(StringUtils.replaceNull(rs.getString("address_zipcode_primary")));
                contact.setAddressCountryPrimary(StringUtils.replaceNull(rs.getString("address_country_primary")));
                contacts.add(contact);
            }
        };
        
        if (companyId == null) {
            queryHelper.setSqlStatement(ContactQueries.selectContactsReportQuery(query));
            queryHelper.addInputInt(companyContactType);
        } else {
            queryHelper.setSqlStatement(ContactQueries.selectExpandedContactListQuery(query));
            queryHelper.addInputInt(companyId);
            queryHelper.addInputInt(companyContactType);
        }

        executeQuery(queryHelper);
        
        return contacts;
    }

    public ActionMessages addCompanyContact(Contact contact) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.insertContactQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(ObjectTypes.COMPANY_MAIN_CONTACT);
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull(contact.getTitle());
        queryHelper.addInputInt(contact.getCompanyId());
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull(contact.getPhoneWork());
        queryHelper.addInputStringConvertNull(contact.getFax());
        queryHelper.addInputStringConvertNull(contact.getEmailPrimary());
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputInt(0);
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputInt(0);
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull(contact.getHomepageUrl());
        queryHelper.addInputStringConvertNull(contact.getDescription());
        queryHelper.addInputStringConvertNull(contact.getAddressStreetPrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressCityPrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressStatePrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressZipcodePrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressCountryPrimary());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Put some values in the result.
        if (errors.isEmpty()) {
            contact.setContactId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    public ActionMessages addEmployeeContact(Contact contact) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.insertContactQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(ObjectTypes.COMPANY_EMPLOYEE_CONTACT);
        queryHelper.addInputStringConvertNull(contact.getFirstName());
        queryHelper.addInputStringConvertNull(contact.getLastName());
        queryHelper.addInputStringConvertNull(contact.getTitle());
        queryHelper.addInputInt(contact.getCompanyId());
        queryHelper.addInputStringConvertNull(contact.getPhoneHome());
        queryHelper.addInputStringConvertNull(contact.getPhoneMobile());
        queryHelper.addInputStringConvertNull(contact.getPhoneWork());
        queryHelper.addInputStringConvertNull(contact.getFax());
        queryHelper.addInputStringConvertNull(contact.getEmailPrimary());
        queryHelper.addInputStringConvertNull(contact.getEmailSecondary());
        queryHelper.addInputInt(contact.getMessenger1Type());
        queryHelper.addInputStringConvertNull(contact.getMessenger1Id());
        queryHelper.addInputInt(contact.getMessenger2Type());
        queryHelper.addInputStringConvertNull(contact.getMessenger2Id());
        queryHelper.addInputStringConvertNull(contact.getHomepageUrl());
        queryHelper.addInputStringConvertNull(contact.getDescription());
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Put some values in the result.
        if (errors.isEmpty()) {
            contact.setContactId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    public ActionMessages updateCompanyContact(Contact contact) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.updateContactQuery());
        queryHelper.addInputInt(contact.getId());
        queryHelper.addInputInt(contact.getCompanyId());
        queryHelper.addInputInt(ObjectTypes.COMPANY_MAIN_CONTACT);
        queryHelper.addInputStringConvertNull(null);
        queryHelper.addInputStringConvertNull(null);
        queryHelper.addInputStringConvertNull(contact.getTitle());
        queryHelper.addInputStringConvertNull(null);
        queryHelper.addInputStringConvertNull(null);
        queryHelper.addInputStringConvertNull(contact.getPhoneWork());
        queryHelper.addInputStringConvertNull(contact.getFax());
        queryHelper.addInputStringConvertNull(contact.getEmailPrimary());
        queryHelper.addInputStringConvertNull(null);
        queryHelper.addInputInt(0);
        queryHelper.addInputStringConvertNull(null);
        queryHelper.addInputInt(0);
        queryHelper.addInputStringConvertNull(null);
        queryHelper.addInputStringConvertNull(contact.getHomepageUrl());
        queryHelper.addInputStringConvertNull(contact.getDescription());
        queryHelper.addInputStringConvertNull(contact.getAddressStreetPrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressCityPrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressStatePrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressZipcodePrimary());
        queryHelper.addInputStringConvertNull(contact.getAddressCountryPrimary());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    public ActionMessages updateEmployeeContact(Contact contact) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContactQueries.updateContactQuery());
        queryHelper.addInputInt(contact.getId());
        queryHelper.addInputInt(contact.getCompanyId());
        queryHelper.addInputInt(ObjectTypes.COMPANY_EMPLOYEE_CONTACT);
        queryHelper.addInputStringConvertNull(contact.getFirstName());
        queryHelper.addInputStringConvertNull(contact.getLastName());
        queryHelper.addInputStringConvertNull(contact.getTitle());
        queryHelper.addInputStringConvertNull(contact.getPhoneHome());
        queryHelper.addInputStringConvertNull(contact.getPhoneMobile());
        queryHelper.addInputStringConvertNull(contact.getPhoneWork());
        queryHelper.addInputStringConvertNull(contact.getFax());
        queryHelper.addInputStringConvertNull(contact.getEmailPrimary());
        queryHelper.addInputStringConvertNull(contact.getEmailSecondary());
        queryHelper.addInputInt(contact.getMessenger1Type());
        queryHelper.addInputStringConvertNull(contact.getMessenger1Id());
        queryHelper.addInputInt(contact.getMessenger2Type());
        queryHelper.addInputStringConvertNull(contact.getMessenger2Id());
        queryHelper.addInputStringConvertNull(contact.getHomepageUrl());
        queryHelper.addInputStringConvertNull(contact.getDescription());
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }
}