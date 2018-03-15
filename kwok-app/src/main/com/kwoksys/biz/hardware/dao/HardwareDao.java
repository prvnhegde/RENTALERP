/*
 * Copyright 2015 Kwoksys
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
package com.kwoksys.biz.hardware.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareComponent;
import com.kwoksys.biz.hardware.dto.HardwareSoftwareMap;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.CurrencyUtils;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * HardwareDao.
 */
public class HardwareDao extends BaseDao {

    public HardwareDao(RequestContext requestContext) {
        super(requestContext);
    }

    //Rentout - start
    
    public List<Hardware> getHardwareReturnList(QueryCriteria queryCriteria) throws DatabaseException {
        return getHardwareReturnList(queryCriteria, null);
    }
    
    public int getHardwareReturnCount(QueryCriteria query) throws DatabaseException {
        return getRowCount(HardwareQueries.getHardwareReturnCountQuery(query));
    }
    
    public int getRentoutCount(QueryCriteria query) throws DatabaseException {
        return getRowCount(HardwareQueries.getHardwareCountQuery(query));
    }
    
    public List<Hardware> getRentoutHardwareList(QueryCriteria queryCriteria) throws DatabaseException {
        return getRentoutHardwareList(queryCriteria, null);
    }
    
    
    private List<Hardware> getRentoutHardwareList(QueryCriteria queryCriteria, ObjectLink objectLink) throws DatabaseException {
        List<Hardware> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper() {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Hardware hardware = newHardware(rs);
                list.add(hardware);
            }
        };
        
        if (objectLink != null) {
            if (objectLink.getLinkedObjectId() == null || objectLink.getLinkedObjectId() == 0) {
                queryHelper.setSqlStatement(HardwareQueries.selectLinkedHardwareListQuery(queryCriteria));
                queryHelper.addInputInt(objectLink.getObjectId());
                queryHelper.addInputInt(objectLink.getObjectTypeId());
                queryHelper.addInputInt(objectLink.getLinkedObjectTypeId());
    
            } else {
                queryHelper.setSqlStatement(HardwareQueries.selectObjectHardwareListQuery(queryCriteria));
                queryHelper.addInputInt(objectLink.getLinkedObjectId());
                queryHelper.addInputInt(objectLink.getLinkedObjectTypeId());
                queryHelper.addInputInt(objectLink.getObjectTypeId());
            }
        } else {
            queryHelper.setSqlStatement(HardwareQueries.selectHardwareRentoutListQuery(queryCriteria));
        }
        
        executeQuery(queryHelper);
        
        return list;
    }
    
    
    private List<Hardware> getHardwareReturnList(QueryCriteria queryCriteria, ObjectLink objectLink) throws DatabaseException {
        List<Hardware> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper() {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Hardware hardware = newHardware(rs);
                list.add(hardware);
            }
        };
        
        if (objectLink != null) {
            if (objectLink.getLinkedObjectId() == null || objectLink.getLinkedObjectId() == 0) {
                queryHelper.setSqlStatement(HardwareQueries.selectLinkedHardwareListQuery(queryCriteria));
                queryHelper.addInputInt(objectLink.getObjectId());
                queryHelper.addInputInt(objectLink.getObjectTypeId());
                queryHelper.addInputInt(objectLink.getLinkedObjectTypeId());
    
            } else {
                queryHelper.setSqlStatement(HardwareQueries.selectObjectHardwareListQuery(queryCriteria));
                queryHelper.addInputInt(objectLink.getLinkedObjectId());
                queryHelper.addInputInt(objectLink.getLinkedObjectTypeId());
                queryHelper.addInputInt(objectLink.getObjectTypeId());
            }
        } else {
            queryHelper.setSqlStatement(HardwareQueries.selectHardwareRenturnListQuery(queryCriteria));
        }
        
        executeQuery(queryHelper);
        
        return list;
    }
    
    public List<AttributeFieldCount> getRentoutHardwareTypeCount(QueryCriteria query) throws DatabaseException {
        List<AttributeFieldCount> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectRentoutHardwareTypeCountQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_type"));
                count.setObjectCount(rs.getInt("hardware_count"));

                list.add(count);
            }
        };

        executeQuery(queryHelper);
        
        return list;
    }
    
    public List<AttributeFieldCount> getRentoutHardwareByCustomerCount(QueryCriteria query) throws DatabaseException {
        List<AttributeFieldCount> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectRentoutHardwareTypeCountQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_type"));
                count.setObjectCount(rs.getInt("hardware_count"));

                list.add(count);
            }
        };

        executeQuery(queryHelper);
        
        return list;
    }
    
    public List<AttributeFieldCount> getRentoutHardwareByDurationCount(QueryCriteria query) throws DatabaseException {
        List<AttributeFieldCount> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectRentoutHardwareByDurationCountQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_type"));
                count.setObjectCount(rs.getInt("hardware_count"));

                list.add(count);
            }
        };

        executeQuery(queryHelper);
        
        return list;
    } 
    
    
    public Integer getHardwareReturn(Integer hardwareId) throws DatabaseException, ObjectNotFoundException {
    	List<Integer> list = new ArrayList<>();
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareReturnDetailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
            	list.add(new Integer(rs.getInt("rental_id")));
            }
        };
        
        queryHelper.addInputInt(hardwareId);
        
        executeSingleRecordQuery(queryHelper);
        
        
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            throw new ObjectNotFoundException("Hardware ID: " + hardwareId);
        }
    }
    
    
  //Rentout - end
    
    
    public List<Hardware> getHardwareList(QueryCriteria queryCriteria) throws DatabaseException {
        return getHardwareList(queryCriteria, null);
    }

    private List<Hardware> getHardwareList(QueryCriteria queryCriteria, ObjectLink objectLink) throws DatabaseException {
        List<Hardware> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper() {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Hardware hardware = newHardware(rs);
                list.add(hardware);
            }
        };
        
        if (objectLink != null) {
            if (objectLink.getLinkedObjectId() == null || objectLink.getLinkedObjectId() == 0) {
                queryHelper.setSqlStatement(HardwareQueries.selectLinkedHardwareListQuery(queryCriteria));
                queryHelper.addInputInt(objectLink.getObjectId());
                queryHelper.addInputInt(objectLink.getObjectTypeId());
                queryHelper.addInputInt(objectLink.getLinkedObjectTypeId());
    
            } else {
                queryHelper.setSqlStatement(HardwareQueries.selectObjectHardwareListQuery(queryCriteria));
                queryHelper.addInputInt(objectLink.getLinkedObjectId());
                queryHelper.addInputInt(objectLink.getLinkedObjectTypeId());
                queryHelper.addInputInt(objectLink.getObjectTypeId());
            }
        } else {
            queryHelper.setSqlStatement(HardwareQueries.selectHardwareListQuery(queryCriteria));
        }
        
        executeQuery(queryHelper);
        
        return list;
    }

    public Hardware getHardware(Integer hardwareId) throws DatabaseException, ObjectNotFoundException {
        List<Hardware> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareDetailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Hardware hardware = newHardware(rs);
                hardware.setCountSoftware(rs.getInt("software_count"));
                hardware.setCountFile(rs.getInt("file_count"));
                hardware.setCountComponent(rs.getInt("component_count"));
                
                list.add(hardware);
            }
        };
        
        queryHelper.addInputInt(hardwareId);
        
        executeSingleRecordQuery(queryHelper);

        if (!list.isEmpty()) {
            return list.get(0);

        } else {
            throw new ObjectNotFoundException("Hardware ID: " + hardwareId);
        }
    }

    public int getCount(QueryCriteria query) throws DatabaseException {
        return getRowCount(HardwareQueries.getHardwareCountQuery(query));
    }

    public List<Hardware> getLinkedHardwareList(QueryCriteria queryCriteria, ObjectLink objectLink) throws DatabaseException {
        return getHardwareList(queryCriteria, objectLink);
    }

    public Map<String, String> getWarrantyExpirationCounts() throws DatabaseException {
        Map<String, String> map = new TreeMap<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectWarrantyExpirationCountQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                map.put(rs.getString("interval"), rs.getString("count"));
            }
        };
        
        executeQuery(queryHelper);
        
        return map;
    }

    /**
     * Return number of hardware grouped by type.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getHardwareTypeCount(QueryCriteria query) throws DatabaseException {
        List<AttributeFieldCount> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareTypeCountQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_type"));
                count.setObjectCount(rs.getInt("hardware_count"));

                list.add(count);
            }
        };

        executeQuery(queryHelper);
        
        return list;
    }

    /**
     * Return number of hardware grouped by status.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getHardwareStatusCount(QueryCriteria query) throws DatabaseException {
        List<AttributeFieldCount> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareCountByStatusQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_status"));
                count.setObjectCount(rs.getInt("hardware_count"));

                list.add(count);
            }
        };

        executeQuery(queryHelper);
        
        return list;
    }

    /**
     * Return number of hardware grouped by location.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getHardwareLocationCount(QueryCriteria query) throws DatabaseException {
        List<AttributeFieldCount> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareCountByLocationQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_location"));
                count.setObjectCount(rs.getInt("hardware_count"));

                list.add(count);
            }
        };

        executeQuery(queryHelper);
        
        return list;
    }

    public List<Map<String, String>> getAvailableSoftware(QueryCriteria query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareAvailableSoftwareQuery(query));

        return executeQueryReturnList(queryHelper);
    }

    public List<SoftwareLicense> getAvailableLicense(QueryCriteria query, Integer softwareId) throws DatabaseException {
        List<SoftwareLicense> licenses = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareAvailableLicensesQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                SoftwareLicense license = new SoftwareLicense();
                license.setId(rs.getInt("license_id"));
                license.setKey(rs.getString("license_key"));
                license.setNote(StringUtils.replaceNull(rs.getString("license_note")));

                licenses.add(license);
            }
        };
                
        queryHelper.addInputInt(softwareId);

        executeQuery(queryHelper);
        
        return licenses;
    }

    /**
     * Returns Software Licenses installed on a Hardware.
     *
     */
    public List<HardwareSoftwareMap> getInstalledLicense(QueryCriteria queryCriteria, Integer hardwareId) throws DatabaseException {
        List<HardwareSoftwareMap> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectInstalledLicenseQuery(queryCriteria)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                HardwareSoftwareMap map = new HardwareSoftwareMap();
                map.setMapId(rs.getInt("map_id"));
                map.setSoftwareId(rs.getInt("software_id"));
                map.getSoftware().setName(rs.getString("software_name"));
                map.setLicenseId(rs.getInt("license_id"));
                map.getLicense().setKey(rs.getString("license_key"));
                map.getLicense().setNote(StringUtils.replaceNull(rs.getString("license_note")));

                list.add(map);
            }
        };
        
        queryHelper.addInputInt(hardwareId);

        executeQuery(queryHelper);
        
        return list;
    }

    /**
     * Return Components for a particular Hardware.
     */
    public List<HardwareComponent> getHardwareComponents(QueryCriteria query, Integer hardwareId) throws DatabaseException {
        List<HardwareComponent> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareComponentsQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                HardwareComponent component = new HardwareComponent();
                component.setId(rs.getInt("comp_id"));
                component.setTypeName(rs.getString("comp_name"));
                component.setDescription(StringUtils.replaceNull(rs.getString("comp_description")));

                list.add(component);
            }
        };
        
        queryHelper.addInputInt(hardwareId);

        executeQuery(queryHelper);
        
        return list;
    }

    /**
     * Return a specified hardware component.
     */
    public HardwareComponent getHardwareComponentDetail(Integer hardwareId, Integer componentId) throws DatabaseException,
            ObjectNotFoundException {

        List<HardwareComponent> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareComponentDetailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                HardwareComponent component = new HardwareComponent();
                component.setHardwareId(rs.getInt("hardware_id"));
                component.setId(rs.getInt("comp_id"));
                component.setType(rs.getInt("hardware_component_type"));
                component.setDescription(StringUtils.replaceNull(rs.getString("comp_description")));
                
                list.add(component);
            }
        };
        
        queryHelper.addInputInt(hardwareId);
        queryHelper.addInputInt(componentId);

        executeSingleRecordQuery(queryHelper);
        
        if (!list.isEmpty()) {
            return list.get(0);
            
        } else {
            throw new ObjectNotFoundException("Component ID: " + componentId);
        }
    }

    public ActionMessages addHardware(Hardware hardware) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.insertHardwareQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(hardware.getName());
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull(hardware.getDescription());
        queryHelper.addInputIntegerConvertNull(hardware.getManufacturerId());
        queryHelper.addInputIntegerConvertNull(hardware.getVendorId());

        // We don't want hardware type to be null before it's hard to search for it.
        queryHelper.addInputInt(hardware.getType());
        queryHelper.addInputInt(hardware.getStatus());
        queryHelper.addInputIntegerConvertNull(hardware.getOwnerId());
        queryHelper.addInputInt(hardware.getLocation());
        queryHelper.addInputStringConvertNull(hardware.getModelName());
        queryHelper.addInputStringConvertNull(hardware.getModelNumber());
        queryHelper.addInputStringConvertNull(hardware.getSerialNumber());
        if (hardware.getPurchasePriceRaw() == 0) {
            queryHelper.addInputDoubleConvertNull(null);
        } else {
            queryHelper.addInputDouble(hardware.getPurchasePriceRaw());
        }
        queryHelper.addInputInt(hardware.getResetLastServiceDate());
        queryHelper.addInputStringConvertNull(hardware.getHardwarePurchaseDateString());
        queryHelper.addInputStringConvertNull(hardware.getWarrantyExpireDateString());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Put some values in the result.
            hardware.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update custom fields
            if (!hardware.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, hardware.getId(), hardware.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages update(Hardware hardware) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareQuery());
        queryHelper.addInputInt(hardware.getId());
        queryHelper.addInputStringConvertNull(hardware.getName());
        queryHelper.addInputStringConvertNull(null);
        queryHelper.addInputStringConvertNull(hardware.getDescription());
        queryHelper.addInputIntegerConvertNull(hardware.getManufacturerId());
        queryHelper.addInputIntegerConvertNull(hardware.getVendorId());

        // We don't want hardware type to be null before it's hard to search for it.
        queryHelper.addInputInt(hardware.getType());
        queryHelper.addInputInt(hardware.getStatus());
        queryHelper.addInputIntegerConvertNull(hardware.getOwnerId());
        queryHelper.addInputInt(hardware.getLocation());
        queryHelper.addInputStringConvertNull(hardware.getModelName());
        queryHelper.addInputStringConvertNull(hardware.getModelNumber());
        queryHelper.addInputStringConvertNull(hardware.getSerialNumber());
        if (hardware.getPurchasePriceRaw() == 0) {
            queryHelper.addInputDoubleConvertNull(null);
        } else {
            queryHelper.addInputDouble(hardware.getPurchasePriceRaw());
        }
        queryHelper.addInputInt(hardware.getResetLastServiceDate());
        queryHelper.addInputStringConvertNull(hardware.hasHardwarePurchaseDate() ?
                hardware.getHardwarePurchaseDateString() : null);
        queryHelper.addInputStringConvertNull(hardware.hasHardwareWarrantyExpireDate() ?
                hardware.getWarrantyExpireDateString() : null);
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update custom fields
            if (!hardware.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, hardware.getId(), hardware.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages delete(Hardware hardware) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.deleteHardwareQuery());
        queryHelper.addInputInt(ObjectTypes.HARDWARE);
        queryHelper.addInputInt(hardware.getId());

        return executeProcedure(queryHelper);
    }
    
    /**
     * 
     * @param hardware
     * @return
     * @throws DatabaseException
     */
    public ActionMessages rentOut(Hardware hardware) throws DatabaseException {
    	System.out.println("Class:HardwareDao :: Method:rentOut .. START");
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.rentOutHardwareQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        
	    System.out.println("***** Customer Name : *** "+ requestContext.getRequest().getParameter("customerName"));
        String rentalStrYr = requestContext.getRequest().getParameter("rentalstartdateYear");
        String rentalStrMn = requestContext.getRequest().getParameter("rentalstartdateMonth");
        String rentalStrDt = requestContext.getRequest().getParameter("rentalstartdateDate");
        
        String rentalEndYr = requestContext.getRequest().getParameter("rentalenddateYear");
        String rentalEndMn = requestContext.getRequest().getParameter("rentalenddateMonth");
        String rentalEndDt = requestContext.getRequest().getParameter("rentalenddateDate");
        
	    System.out.println("Rental Start Date : "+rentalStrYr+"/"+rentalStrMn+"/"+rentalStrDt);
	    System.out.println("Rental End Date : "+rentalEndYr+"/"+rentalEndMn+"/"+rentalEndDt);

        queryHelper.addInputInt(hardware.getId());
        queryHelper.addInputStringConvertNull(requestContext.getRequest().getParameter("customerName"));
        queryHelper.addInputStringConvertNull(requestContext.getRequest().getParameter("customerName"));
        queryHelper.addInputStringConvertNull("#43, Simsbury, Connecticut 06089");
        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(rentalStrYr, rentalStrMn, rentalStrDt));
        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(rentalEndYr, rentalEndMn, rentalEndDt));
        //img check / PO
        queryHelper.addInputStringConvertNull("8765432908");
        queryHelper.addInputStringConvertNull("tina@mymail.com");
        System.out.println("Class:HardwareDao :: Method:rentOut .. END");
        return executeProcedure(queryHelper);
    }
    
    /**
     * 
     * @param hardware
     * @return
     * @throws DatabaseException
     */
    public ActionMessages hardwareReturn(Integer rentalId) throws DatabaseException {
    	System.out.println("Class:HardwareDao :: Method:hardwareReturn .. START");
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.hardwareReturnQuery());
        
        String returnDate = requestContext.getRequest().getParameter("returnDate");
        String returnMonth = requestContext.getRequest().getParameter("returnMonth");
        String returnYear = requestContext.getRequest().getParameter("returnYear");
        
	    System.out.println("Return Date : "+returnYear+"/"+returnMonth+"/"+returnDate);

	    queryHelper.addInputInt(rentalId.intValue());
        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(returnYear, returnMonth, returnDate));
        System.out.println("Class:HardwareDao :: Method:hardwareReturn .. END");
        //return new ActionMessages();
        return executeProcedure(queryHelper);
    }

    public ActionMessages assignSoftwareLicense(HardwareSoftwareMap hsm) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.insertAssignLicenseQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(hsm.getHardwareId());
        queryHelper.addInputInt(hsm.getSoftwareId());
        queryHelper.addInputInt(hsm.getLicenseId());
        queryHelper.addInputInt(hsm.getLicenseEntitlement());

        executeProcedure(queryHelper);

        // Put some values in the result.
        if (errors.isEmpty()) {
            hsm.setMapId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    public ActionMessages unassignSoftwareLicense(HardwareSoftwareMap hsm) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.deleteAssignedLicenseQuery());
        queryHelper.addInputInt(hsm.getMapId());

        return executeProcedure(queryHelper);
    }

    /**
     * Adds hardware component.
     * @param component
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addHardwareComponent(HardwareComponent component) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.insertHardwareComponentQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(component.getHardwareId());
        queryHelper.addInputStringConvertNull(component.getDescription());
        queryHelper.addInputInt(component.getType());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);
            // Put some values in the result.
            component.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update custom fields
            if (!component.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, component.getId(), component.getCustomValues());
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
     * Updates hardware component.
     * @param component
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateHardwareComponent(HardwareComponent component) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareComponentQuery());
        queryHelper.addInputInt(component.getHardwareId());
        queryHelper.addInputInt(component.getId());
        queryHelper.addInputStringConvertNull(component.getDescription());
        queryHelper.addInputInt(component.getType());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update custom fields
            if (!component.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, component.getId(), component.getCustomValues());
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
     * Deletes hardware component.
     * @param component
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteHardwareComponent(HardwareComponent component) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.deleteHardwareComponentQuery());
        queryHelper.addInputInt(ObjectTypes.HARDWARE_COMPONENT);
        queryHelper.addInputInt(component.getHardwareId());
        queryHelper.addInputInt(component.getId());

        return executeProcedure(queryHelper);
    }

    public ActionMessages resetHardwareSoftwareCount(Integer hardwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareSoftwareCountQuery());
        queryHelper.addInputInt(hardwareId);

        return executeProcedure(queryHelper);
    }

    /**
     * Resets hardware file count.
     * @param hardwareId
     * @return
     * @throws DatabaseException
     */
    public ActionMessages resetFileCount(Integer hardwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareFileCountQuery());
        queryHelper.addInputInt(ObjectTypes.HARDWARE);
        queryHelper.addInputInt(hardwareId);

        return executeProcedure(queryHelper);
    }

    /**
     * Resets hardware component count.
     * @param hardwareId
     * @return
     * @throws DatabaseException
     */
    public ActionMessages resetComponentCount(Integer hardwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareComponentCountQuery());
        queryHelper.addInputInt(hardwareId);

        return executeProcedure(queryHelper);
    }

    private Hardware newHardware(ResultSet rs) throws SQLException, DatabaseException {
        Hardware hardware = new Hardware();
        hardware.setId(rs.getInt("hardware_id"));
        hardware.setName(rs.getString("hardware_name"));
        hardware.setDescription(StringUtils.replaceNull(rs.getString("hardware_description")));
        hardware.setSerialNumber(StringUtils.replaceNull(rs.getString("hardware_serial_number")));
        hardware.setModelName(StringUtils.replaceNull(rs.getString("hardware_model_name")));
        hardware.setModelNumber(StringUtils.replaceNull(rs.getString("hardware_model_number")));
        hardware.setManufacturerId(rs.getInt("manufacturer_company_id"));
        hardware.setManufacturerName(StringUtils.replaceNull(rs.getString("hardware_manufacturer_name")));
        hardware.setVendorId(rs.getInt("vendor_company_id"));
        hardware.setVendorName(StringUtils.replaceNull(rs.getString("hardware_vendor_name")));

        hardware.setLocation(rs.getInt("hardware_location"));
        hardware.setType(rs.getInt("hardware_type"));
        hardware.setStatus(rs.getInt("hardware_status"));
        hardware.setPurchasePrice(CurrencyUtils.formatCurrency(rs.getDouble("hardware_purchase_price"), ""));
        hardware.setLastServicedOn(DatetimeUtils.getDate(rs, "hardware_last_service_date"));

        hardware.setHardwarePurchaseDate(DatetimeUtils.getDate(rs, "hardware_purchase_date"));
        if (hardware.getHardwarePurchaseDate() != null) {
            hardware.setHardwarePurchaseDate(
                    DatetimeUtils.toYearString(hardware.getHardwarePurchaseDate()),
                    DatetimeUtils.toMonthString(hardware.getHardwarePurchaseDate()),
                    DatetimeUtils.toDateString(hardware.getHardwarePurchaseDate()));
        }

        hardware.setWarrantyExpireDate(DatetimeUtils.getDate(rs, "hardware_warranty_expire_date"));
        if (hardware.getWarrantyExpireDate() != null) {
            hardware.setHardwareWarrantyExpireDate(
                    DatetimeUtils.toYearString(hardware.getWarrantyExpireDate()),
                    DatetimeUtils.toMonthString(hardware.getWarrantyExpireDate()),
                    DatetimeUtils.toDateString(hardware.getWarrantyExpireDate()));
        }

        hardware.setOwnerId(rs.getInt("hardware_owner_id"));
        hardware.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
        hardware.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

        hardware.setCreator(new AccessUser());
        hardware.getCreator().setId(rs.getInt("creator"));
        hardware.getCreator().setUsername(rs.getString("creator_username"));
        hardware.getCreator().setDisplayName(rs.getString("creator_display_name"));

        hardware.setModifier(new AccessUser());
        hardware.getModifier().setId(rs.getInt("modifier"));
        hardware.getModifier().setUsername(rs.getString("modifier_username"));
        hardware.getModifier().setDisplayName(rs.getString("modifier_display_name"));
        return hardware;
    }
}
