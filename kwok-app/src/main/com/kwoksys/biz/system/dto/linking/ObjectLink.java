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

/**
 * ObjectLink
 */
public class ObjectLink extends BaseObject {

    private Integer objectId;
    private Integer objectTypeId;
    private Integer linkedObjectId;
    private Integer linkedObjectTypeId;

    public Integer getObjectId() {
        return objectId;
    }
    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }
    public Integer getObjectTypeId() {
        return objectTypeId;
    }
    public void setObjectTypeId(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }
    public Integer getLinkedObjectId() {
        return linkedObjectId;
    }
    public void setLinkedObjectId(Integer linkedObjectId) {
        this.linkedObjectId = linkedObjectId;
    }
    public Integer getLinkedObjectTypeId() {
        return linkedObjectTypeId;
    }
    public void setLinkedObjectTypeId(Integer linkedObjectTypeId) {
        this.linkedObjectTypeId = linkedObjectTypeId;
    }
}
