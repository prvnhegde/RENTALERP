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
package com.kwoksys.biz.base;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeValue;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.DatetimeUtils;
import org.apache.struts2.ServletActionContext;

import java.io.Serializable;
import java.util.*;

public class BaseObject implements Serializable {

    public static final String ROWNUM = "rownum";

    public static final String CREATOR_NAME = "creator_name";

    public static final String CREATION_DATE = "creation_date";

    public static final String MODIFIER_NAME = "modifier_name";

    public static final String MODIFICATION_DATE = "modification_date";

    public static final String REL_DESCRIPTION = "relationship_description";

    private Integer id;
    private Integer creatorId;
    private AccessUser creator;
    private Date creationDate;
    private Integer modifierId;
    private AccessUser modifier;
    private Date modificationDate;
    private Map<Integer, Attribute> attributes;

    /**
     * When an object is retrieved as a linked object, there is a relationship name in object_map table to describe
     * the linking, will use relDescription for now.
     */
    private String relDescription;

    private Integer objectTypeId;

    private Map<Integer, AttributeValue> customValues = new LinkedHashMap<>();

    public BaseObject() {}

    public BaseObject(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    /**
     * Overrides this to return correct value.
     * @param attrId
     * @return
     */
    public boolean isAttrEmpty(String attrName) {
        return false;
    }

    public void loadAttrs(RequestContext requestContext) {
        attributes = new AttributeManager(requestContext).getSystemAttributes(this);
    }

    public boolean isAttrRequired(String attrName) {
        return attributes.get(Attributes.getNameIdMap().get(attrName)).isRequired();
    }

    public String getAttrRequiredText(String attrName) {
        return isAttrRequired(attrName) ? Localizer.getText(RequestContext.getLocale(ServletActionContext.getRequest()),
                "common.requiredFieldIndicator.true") : "";
    }

    public String getCreationDate() {
        return DatetimeUtils.toLocalDatetime(creationDate);
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getModificationDate() {
        return DatetimeUtils.toLocalDatetime(modificationDate);
    }
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public AccessUser getCreator() {
        return creator;
    }

    public void setCreator(AccessUser creator) {
        this.creator = creator;
    }

    public AccessUser getModifier() {
        return modifier;
    }

    public void setModifier(AccessUser modifier) {
        this.modifier = modifier;
    }

    public Integer getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getRelDescription() {
        return relDescription;
    }

    public void setRelDescription(String relDescription) {
        this.relDescription = relDescription;
    }

    public Map<Integer, AttributeValue> getCustomValues() {
        return customValues;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getModifierId() {
        return modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }
}
