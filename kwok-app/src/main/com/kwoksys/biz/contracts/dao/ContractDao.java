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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * ContractDao class.
 */
public class ContractDao extends BaseDao {

    public ContractDao(RequestContext requestContext) {
        super(requestContext);
    }

    public void fetchContracts(QueryCriteria queryCriteria) throws DatabaseException {
        fetchContracts(queryCriteria, null);
    }
    
    /**
     * Gets contracts
     * @param query
     * @param objectMap
     * @return
     * @throws DatabaseException
     */
    public void fetchContracts(QueryCriteria queryCriteria, ObjectLink objectMap) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper() {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Contract contract = new Contract();
                contract.setId(rs.getInt("contract_id"));
                contract.setName(rs.getString("contract_name"));
                contract.setTypeName(rs.getString("contract_type"));
                contract.setStage(rs.getInt("contract_stage"));
                contract.setDescription(StringUtils.replaceNull(rs.getString("contract_description")));
                contract.setOwnerId(rs.getInt(Contract.CONTRACT_OWNER_ID));
                contract.setRenewalTypeName(rs.getString("contract_renewal_type"));
                contract.setEffectiveDate(DatetimeUtils.getDate(rs, "contract_effective_date"));
                contract.setExpireDate(DatetimeUtils.getDate(rs, "contract_expiration_date"));
                contract.setRenewalDate(DatetimeUtils.getDate(rs, "contract_renewal_date"));
                contract.setContractProviderName(rs.getString("contract_provider_name"));
                contract.setContractProviderId(rs.getInt(Contract.CONTRACT_PROVIDER_ID));
                queryCriteria.getCallback().run(contract);
            }
        };
        
        if (objectMap != null) {
            queryHelper.setSqlStatement(ContractQueries.selectLinkedContractsQuery(queryCriteria));
            queryHelper.addInputInt(objectMap.getObjectTypeId());
            queryHelper.addInputInt(objectMap.getLinkedObjectId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
        } else {
            queryHelper.setSqlStatement(ContractQueries.selectContractsQuery(queryCriteria));
        }
        
        executeQuery(queryHelper);
    }

    public int getContractCount(QueryCriteria query) throws DatabaseException {
        return getRowCount(ContractQueries.getContractCountQuery(query));
    }

    public Contract getContract(Integer contractId) throws DatabaseException, ObjectNotFoundException {
        Contract contract = new Contract();

        QueryHelper queryHelper = new QueryHelper(ContractQueries.selectContractDetailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                contract.setId(rs.getInt("contract_id"));
                contract.setName(StringUtils.replaceNull(rs.getString("contract_name")));
                contract.setDescription(StringUtils.replaceNull(rs.getString("contract_description")));
                contract.setType(rs.getInt("contract_type"));
                contract.setStage(rs.getInt("contract_stage"));
                contract.setOwnerId(rs.getInt(Contract.CONTRACT_OWNER_ID));
                contract.setEffectiveDate(DatetimeUtils.getDate(rs, "contract_effective_date"));
                contract.setExpireDate(DatetimeUtils.getDate(rs, "contract_expiration_date"));
                contract.setRenewalType(rs.getInt("contract_renewal_type"));
                contract.setRenewalDate(DatetimeUtils.getDate(rs, "contract_renewal_date"));
                contract.setContractProviderName(rs.getString("contract_provider_name"));
                contract.setContractProviderId(rs.getInt("contract_provider_id"));
                contract.setCreatorId(rs.getInt("creator"));
                contract.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                contract.setModifierId(rs.getInt("modifier"));
                contract.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));
            }
        };
        
        queryHelper.addInputInt(contractId);
        
        executeSingleRecordQuery(queryHelper);
        
        if (contract.getId() != null) {
            return contract;
        } else {
            throw new ObjectNotFoundException("Contract ID: " + contractId);
        }
    }

    public Map<String, String> getContractsSummary() throws DatabaseException {
        Map<String, String> map = new TreeMap<>();

        QueryHelper queryHelper = new QueryHelper(ContractQueries.selectContractsSummary()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                map.put(rs.getString("interval"), rs.getString("count"));
            };
        };
        
        executeQuery(queryHelper);
        
        return map;
    }

    public ActionMessages addContract(Contract contract) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContractQueries.addContractQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(contract.getName());
        queryHelper.addInputStringConvertNull(contract.getDescription());
        queryHelper.addInputInt(contract.getType());
        queryHelper.addInputInt(contract.getStage());

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getEffectiveDateYear(), contract.getEffectiveDateMonth(), contract.getEffectiveDateDate()));

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getExpireDateYear(), contract.getExpireDateMonth(), contract.getExpireDateDate()));

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getRenewalDateYear(), contract.getRenewalDateMonth(), contract.getRenewalDateDate()));

        queryHelper.addInputInt(contract.getRenewalType());
        queryHelper.addInputIntegerConvertNull(contract.getContractProviderId());
        queryHelper.addInputIntegerConvertNull(contract.getOwnerId());
        queryHelper.addInputIntegerConvertNull(contract.getContractProviderContactId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);
            // Put some values in the result.
            contract.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update custom fields
            if (!contract.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, contract.getId(), contract.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages updateContract(Contract contract) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContractQueries.updateContractQuery());
        queryHelper.addInputInt(contract.getId());
        queryHelper.addInputStringConvertNull(contract.getName());
        queryHelper.addInputStringConvertNull(contract.getDescription());
        queryHelper.addInputInt(contract.getType());
        queryHelper.addInputInt(contract.getStage());

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getEffectiveDateYear(), contract.getEffectiveDateMonth(), contract.getEffectiveDateDate()));

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getExpireDateYear(), contract.getExpireDateMonth(), contract.getExpireDateDate()));

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getRenewalDateYear(), contract.getRenewalDateMonth(), contract.getRenewalDateDate()));

        queryHelper.addInputInt(contract.getRenewalType());
        queryHelper.addInputIntegerConvertNull(contract.getContractProviderId());
        queryHelper.addInputIntegerConvertNull(contract.getOwnerId());
        queryHelper.addInputIntegerConvertNull(contract.getContractProviderContactId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update custom fields
            if (!contract.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, contract.getId(), contract.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Delete a contract.
     *
     * @return ..
     */
    public ActionMessages delete(Integer contractId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContractQueries.deleteContractQuery());
        queryHelper.addInputInt(ObjectTypes.CONTRACT);
        queryHelper.addInputInt(contractId);

        return executeProcedure(queryHelper);
    }
    
    public ActionMessages updateContractNotification(Contract contract, int notificationThreshold) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContractQueries.updateContractNotification());
        queryHelper.addInputInt(notificationThreshold);
        queryHelper.addInputInt(contract.getId());

        return executeUpdate(queryHelper);
    }
}
