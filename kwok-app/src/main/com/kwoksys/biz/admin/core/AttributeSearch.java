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
package com.kwoksys.biz.admin.core;

import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.SqlUtils;

/**
 * This is for building search queries.
 */
public class AttributeSearch extends BaseSearch {

    public static final String IS_EDITABLE = "isEditable";

    public static final String IS_CUSTOM_ATTR = "isCustomAttr";

    public static final String ATTRIBUTE_ID_EQUALS = "attributeIdEquals";

    public static final String OBJECT_TYPE_ID_EUALS = "objectTypeId";

    public static final String OBJECT_ID_EQUALS = "objectId";

    public void applyMap(QueryCriteria query) {
        if (searchCriteriaMap == null) {
            return;
        }
        // For attribute_id equals.
        if (searchCriteriaMap.containsKey(ATTRIBUTE_ID_EQUALS)) {
            query.appendWhereClause("a.attribute_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(ATTRIBUTE_ID_EQUALS)));
        }
        // For object_type_id equals.
        if (searchCriteriaMap.containsKey(OBJECT_TYPE_ID_EUALS)) {
            query.appendWhereClause("a.object_type_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(OBJECT_TYPE_ID_EUALS)));
        }
        // For object_id equals.
        if (searchCriteriaMap.containsKey("objectId")) {
            query.appendWhereClause("a.object_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get("objectId")));
        }
        // For showing editable attribute fields
        if (searchCriteriaMap.containsKey(IS_EDITABLE)) {
            if (Boolean.parseBoolean(searchCriteriaMap.get(IS_EDITABLE).toString())) {
                query.appendWhereClause("a.is_editable = 1");
            } else {
                query.appendWhereClause("a.is_editable = 0");
            }
        }
        // For showing custom attributes
        if (searchCriteriaMap.containsKey(IS_CUSTOM_ATTR)) {
            if (Boolean.parseBoolean(searchCriteriaMap.get(IS_CUSTOM_ATTR).toString())) {
                query.appendWhereClause("a.is_custom_attr = 1");
            } else {
                query.appendWhereClause("a.is_custom_attr = 0");
            }
        }
        // For showing attribute fields that are not disabled
        if (searchCriteriaMap.containsKey("attrFieldEnabled")) {
            query.appendWhereClause("af.is_disabled is null");
        }
    }
}
