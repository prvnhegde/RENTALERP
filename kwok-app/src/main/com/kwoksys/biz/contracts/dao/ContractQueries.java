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
package com.kwoksys.biz.contracts.dao;

import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.biz.system.core.configs.ConfigManager;

/**
 * ContractQueries class.
 */
public class ContractQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals(Contract.NAME)) {
            return "lower(c.contract_name)";

        } else if (column.equals(Contract.OWNER)) {
            return ConfigManager.system.getUsernameDisplay().equals(AdminUtils.USER_USERNAME) ?
                    "lower(c.contract_owner_username)" : "lower(c.contract_owner_display_name)";

        } else {
            return column;
        }
    }

    /**
     * Return all contracts.
     */
    public static String selectContractsQuery(QueryCriteria query) {
        return "select c.contract_id, c.contract_name, c.contract_description, v.attribute_field_name as contract_type, c.contract_stage," +
                "c.contract_owner_id, v2.attribute_field_name as contract_renewal_type, " +
                "c.contract_expiration_date, c.contract_effective_date, c.contract_renewal_date," +
                "c.contract_provider_id, c.contract_provider_name " +
                "from contract_view c " +
                "left outer join attribute_field_view v on v.attribute_field_id = c.contract_type " +
                "left outer join attribute_field_view v2 on v2.attribute_field_id = c.contract_renewal_type "
                + query.createWhereClause();
    }

    /**
     * Returns a count of contracts.
     */
    public static String getContractCountQuery(QueryCriteria query) {
        return "select count(c.contract_id) as row_count from contract_view c " + query.createWhereCountClause();
    }

    /**
     * Return linked contracts
     * @param query
     * @return
     */
    public static String selectLinkedContractsQuery(QueryCriteria query) {
        return selectContractsQuery(new QueryCriteria()) +
                "join object_map om on c.contract_id=om.object_id and om.object_type_id=? and om.linked_object_id=? " +
                "and om.linked_object_type_id=? "
                + query.createWhereClause();
    }

    /**
     * Return detail for a specific contract.
     */
    public static String selectContractDetailQuery() {
        return "select c.contract_id, c.contract_name, c.contract_description, c.contract_type, c.contract_stage," 
                + "c.contract_owner_id, c.contract_effective_date, c.contract_expiration_date, c.contract_renewal_type, "
                + "c.contract_renewal_date, c.contract_provider_id, c.contract_provider_name, " 
                + "c.creator, c.creation_date, " 
                + "c.modifier, c.modification_date " 
                + "from contract_view c where contract_id = ?";
    }

    public static String selectContractsSummary() {
        return "select '0_30' as interval, count(contract_id) as count from contract c " +
                "where c.contract_expiration_date > now()::timestamp + '+0 day'::interval " +
                "and c.contract_expiration_date < now()::timestamp + '+30 day'::interval " +
                "union " +
                "select '30_60' as interval, count(contract_id) from contract c " +
                "where c.contract_expiration_date > now()::timestamp + '+30 day'::interval " +
                "and c.contract_expiration_date < now()::timestamp + '+60 day'::interval " +
                "union " +
                "select '60_90' as interval, count(contract_id) from contract c " +
                "where c.contract_expiration_date > now()::timestamp + '+60 day'::interval " +
                "and c.contract_expiration_date < now()::timestamp + '+90 day'::interval " +
                "union " +
                "select '0' as interval, count(contract_id) from contract c " +
                "where c.contract_expiration_date < now()::timestamp + '+0 day'::interval";
    }

    /**
     * Adds new contract.
     *
     * @return ..
     */
    public static String addContractQuery() {
        return "{call sp_contract_add(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Update the detail for a specific contract.
     *
     * @return ..
     */
    public static String updateContractQuery() {
        return "{call sp_contract_update(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Delete a specific contract.
     *
     * @return ..
     */
    public static String deleteContractQuery() {
        return "{call sp_contract_delete(?,?)}";
    }
    
    public static String updateContractNotification() {
        return "update contract set contract_expiration_notified = ? where contract_id = ?";
    }
}
