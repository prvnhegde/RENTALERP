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
package com.kwoksys.biz.system.dto.linking;

import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.ObjectTypes;

/**
 * SoftwareIssueLink
 */
public class SoftwareIssueLink extends BaseObject {

    private Integer softwareId;
    private Integer issueId;

    public SoftwareIssueLink() {}

    public SoftwareIssueLink(Integer softwareId) {
        this.softwareId = softwareId;
    }

    public ObjectLink createObjectMap() {
        ObjectLink objectMap = new ObjectLink();
        objectMap.setObjectId(softwareId);
        objectMap.setObjectTypeId(ObjectTypes.SOFTWARE);
        objectMap.setLinkedObjectId(issueId);
        objectMap.setLinkedObjectTypeId(ObjectTypes.ISSUE);
        return objectMap;
    }

    public Integer getSoftwareId() {
        return softwareId;
    }
    public void setSoftwareId(Integer softwareId) {
        this.softwareId = softwareId;
    }
    public Integer getIssueId() {
        return issueId;
    }
    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }
}