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
 * SoftwareContactLink
 */
public class ContractContactLink extends BaseObject {

    private Integer contractId;
    private Integer contactId;

    public ContractContactLink() {}

    public ContractContactLink(Integer contractId) {
        this.contractId = contractId;
    }

    public ObjectLink createObjectMap() {
        ObjectLink objectMap = new ObjectLink();
        objectMap.setObjectId(contractId);
        objectMap.setObjectTypeId(ObjectTypes.CONTRACT);
        objectMap.setLinkedObjectId(contactId);
        objectMap.setLinkedObjectTypeId(ObjectTypes.CONTACT);
        objectMap.setRelDescription(getRelDescription());
        return objectMap;
    }

    public Integer getContractId() {
        return contractId;
    }
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }
    public Integer getContactId() {
        return contactId;
    }
    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
}