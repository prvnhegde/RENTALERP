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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AttributeSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeValue;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

/**
 * AttributeManager.
 */
public class AttributeManager {

    private List<Integer> selectedAttrFieldIds = new ArrayList<>();
    private boolean isOptional = false;
    private boolean hideDisabledAttr = false;

    private RequestContext requestContext;

    public AttributeManager(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * Returns Attribute data type, such as One line text, Drop-down, Date, etc.
     * @param requestContext
     * @return
     */
    public static List<LabelValueBean> getAttrDataTypeOptions(RequestContext requestContext) {
        List<LabelValueBean> attrTypeOptions = new ArrayList<>();
        for (Integer option : Attribute.ATTR_TYPE_OPTION_LIST) {
            attrTypeOptions.add(new LabelValueBean(Localizer.getText(requestContext, "admin.attribute.attribute_type."
                    + option), String.valueOf(option)));
        }
        return attrTypeOptions;
    }

    /**
     * Gets an attribute field list and returns the list in Object format.
     *
     * @return ..
     */
    public Map<Integer, AttributeField> getAttrFieldMapCache(Integer attributeId) throws DatabaseException {
        // Get object attributes from cache.
        return new CacheManager(requestContext).getAttributeFieldsCache(attributeId);
    }

    /**
     * Returns a lowercase name/AttributeField map for lookup by names.
     * @param attributeId
     * @return
     * @throws DatabaseException
     */
    public Map<String, AttributeField> getAttrNameMapCache(Integer attributeId) throws DatabaseException {
        Map<String, AttributeField> map = new HashMap<>();

        for (AttributeField attrField : getAttrFieldMapCache(attributeId).values()) {
            map.put(attrField.getName().toLowerCase(), attrField);
        }
        return map;
    }

    /**
     * Returns an attribute field list in List<LabelValueBean> format.
     */
    public List<LabelValueBean> getAttrFieldOptionsCache(Integer attributeId) throws DatabaseException {
        List<LabelValueBean> options = new ArrayList<>();
        if (isOptional) {
            options.add(new SelectOneLabelValueBean(requestContext, "0"));
        }
        // Get object attributes from cache.
        for (AttributeField attrField : getAttrFieldMapCache(attributeId).values()) {
            if (!hideDisabledAttr || (!attrField.isDisabled() || selectedAttrFieldIds.contains(attrField.getId()))) {
                options.add(new LabelValueBean(getAttrFieldName(requestContext, attrField), String.valueOf(attrField.getId())));
            }
        }
        return options;
    }

    /**
     * Returns an attribute field list. This only shows attribute fields that are not disabled
     *
     * selectedAttrFieldId: If attribute field is disabled but has been used previously, we want to show it too.
     */
    public List<LabelValueBean> getActiveAttrFieldOptionsCache(Integer attributeId) throws DatabaseException {
        hideDisabledAttr = true;
        return getAttrFieldOptionsCache(attributeId);
    }

    public AttributeManager setSelectedAttrFieldIds(List<Integer> selectedAttrFieldIds) {
        this.selectedAttrFieldIds = selectedAttrFieldIds;
        return this;
    }

    public AttributeManager setSelectedAttrFieldId(Integer selectedAttrFieldId) {
        this.selectedAttrFieldIds = Arrays.asList(selectedAttrFieldId);
        return this;
    }

    public AttributeManager setOptional(boolean optional) {
        isOptional = optional;
        return this;
    }

    /**
     * Gets attribute field name for a given attrFieldId.
     *
     * @return ..
     */
    public String getAttrFieldNameCache(Integer attributeId, Integer attrFieldId) throws DatabaseException {
        AttributeField attrField = getAttrFieldMapCache(attributeId).get(attrFieldId);
        if (attrField == null) {
            return "";
        } else {
            return getAttrFieldName(requestContext, attrField);
        }
    }

    public static String getAttrFieldName(RequestContext requestContext, AttributeField attrField) {
        if (attrField.getId() < 0) {
            return Localizer.getText(requestContext, "system.attribute_field." + attrField.getName());
        } else {
            return attrField.getName();
        }
    }

    public Map<Integer, Attribute> getSystemAttributes(BaseObject baseObject) {
        // Get system attributes for this object type
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put(AttributeSearch.OBJECT_TYPE_ID_EUALS, baseObject.getObjectTypeId());
        attributeSearch.put(AttributeSearch.IS_CUSTOM_ATTR, false);

        QueryCriteria query = new QueryCriteria(attributeSearch);

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        Map<Integer, Attribute> map = new LinkedHashMap<>();
        try {
            Collection<Attribute> attrs = adminService.getAttributes(query).values();
            for (Attribute attr : attrs) {
                map.put(attr.getId(), attr);
            }
        } catch (Exception e) {
            /* ignored */
        }
        return map;
    }

    /**
     * Returns a list of object types allowed for custom fields.
     * @return
     */
    public static List<Integer> getCustomFieldObjectTypes() {
        return Arrays.asList(ObjectTypes.HARDWARE, ObjectTypes.HARDWARE_COMPONENT, ObjectTypes.SOFTWARE, ObjectTypes.SOFTWARE_LICENSE,
                ObjectTypes.COMPANY, ObjectTypes.CONTRACT, ObjectTypes.ISSUE, ObjectTypes.USER);
    }

    public Map<Integer, Attribute> getCustomFieldMap(Integer objectTypeId) throws DatabaseException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put(AttributeSearch.OBJECT_TYPE_ID_EUALS, objectTypeId);
        attributeSearch.put(AttributeSearch.IS_CUSTOM_ATTR, true);

        QueryCriteria query = new QueryCriteria(attributeSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(Attribute.ATTR_NAME));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        return adminService.getAttributes(query);
    }

    /**
     * Gets a list of custom field objects.
     * @param objectTypeId
     * @return
     * @throws Exception
     */
    public Collection<Attribute> getCustomFieldList(Integer objectTypeId) throws DatabaseException {
        return getCustomFieldMap(objectTypeId).values();
    }

    /**
     * This is a wrapper to put custom fields in LabelValueBean format.
     * @param objectTypeId
     * @return
     * @throws Exception
     */
    public List<LabelValueBean> getCustomFieldOptions(Integer objectTypeId) throws DatabaseException {
        List<LabelValueBean> fieldOptions = new ArrayList<>();

        for (Attribute attr : getCustomFieldList(objectTypeId)) {
            fieldOptions.add(new LabelValueBean(attr.getName(), String.valueOf(attr.getId())));
        }
        return fieldOptions;
    }

    /**
     * Gets a list of attribute values from form.
     */
    public static void populateCustomFieldValues(RequestContext requestContext, BaseObjectForm form, BaseObject baseObject,
            Map<Integer, Attribute> customAttributes) {

        for (Attribute attr : customAttributes.values()) {
            AttributeValue value = new AttributeValue();
            value.setAttributeId(attr.getId());
            value.setAttributeValue(requestContext.getParameterString("attrId"+attr.getId()));
            baseObject.getCustomValues().put(attr.getId(), value);
        }

        form.setCustomValues(baseObject.getCustomValues());
    }

    /**
     * Gets a list of attribute values from import file.
     */
    public static void populateCustomFieldValues(Map<String, String> importCustomFields, BaseObject baseObject,
            Map<Integer, Attribute> customAttributes) {

        for (Attribute attr : customAttributes.values()) {
            String attrValue = importCustomFields.get(attr.getName());
            if (attrValue != null) {
                AttributeValue value = new AttributeValue();
                value.setAttributeId(attr.getId());
                value.setAttributeValue(attrValue);
                baseObject.getCustomValues().put(attr.getId(), value);
            }
        }
    }
}
