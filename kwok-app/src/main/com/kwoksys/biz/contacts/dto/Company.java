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
package com.kwoksys.biz.contacts.dto;

import java.util.Arrays;
import java.util.List;

import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.ObjectTypes;

/**
 * Company Object.
 */
public class Company extends BaseObject {

    public static final String COMPANY_ID = "company_id";
    public static final String COMPANY_NAME = "company_name";
    public static final String DESCRIPTION = "company_description";
    public static final String TYPES = "company_types";

    private Integer id;
    private String name;
    private String description;
    private String tags;
    private int countMainContact;
    private int countEmployeeContact;
    private int countFile;
    private int countBookmark;
    private int countNote;
    private Integer fileId;
    private Integer contactType;
    private Integer tagId;
    private List<Integer> typeIds;

    /**
     * Specifies the sortable Company columns allowed.
     */
    public static List<String> getSortableCompanyColumnList() {
        return Arrays.asList("company_name");
    }

    /**
     * Check whether a column is sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableCompanyColumn(String columnName) {
        return getSortableCompanyColumnList().contains(columnName);
    }

    public Company() {
        super(ObjectTypes.COMPANY);
        tags = "";
        countMainContact = 0;
        countEmployeeContact = 0;
        countFile = 0;
        countBookmark = 0;
        countNote = 0;
        fileId = 0;
    }

    /**
     * This would do pattern matching to find out whether the tags
     * are in valid format.
     */
    public boolean isValidTagFormat() {
        return (!tags.contains(";"));
    }

    /**
     * This specifies the number of allowed tags.
     *
     * @return ..
     */
    public int numTagsAllowed() {
        return 50;
    }

    public boolean isAttrEmpty(String attrName) {
        if (attrName.equals(Company.TYPES)) {
            return typeIds.isEmpty();
        }
        return false;
    }

    //
    // Getter and Setter
    //
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getFileId() {
        return fileId;
    }
    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }
    public Integer getId() {
        return id;
    }
    public String[] getCompanyTagsCommaSeparated() {
        return tags.split(",");
    }
    public int getCountMainContact() {
        return countMainContact;
    }
    public int getCountEmployeeContact() {
        return countEmployeeContact;
    }
    public int getCountFile() {
        return countFile;
    }
    public int getCountBookmark() {
        return countBookmark;
    }
    public int getCountNote() {
        return countNote;
    }
    public Integer getContactType() {
        return contactType;
    }
    public Integer getTagId() {
        return tagId;
    }
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setCountMainContact(int countMainContact) {
        this.countMainContact = countMainContact;
    }
    public void setCountEmployeeContact(int countEmployeeContact) {
        this.countEmployeeContact = countEmployeeContact;
    }
    public void setCountFile(int countFile) {
        this.countFile = countFile;
    }
    public void setCountBookmark(int countBookmark) {
        this.countBookmark = countBookmark;
    }
    public void setCountNote(int countNote) {
        this.countNote = countNote;
    }

    public List<Integer> getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(List<Integer> typeIds) {
        this.typeIds = typeIds;
    }
}
