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
package com.kwoksys.biz.auth.core;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.framework.exceptions.DatabaseException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Access control for Issues module
 */
public class IssueAccess {

    private boolean hasReadAllPermission;

    private Set<Integer> allowedObjects = new HashSet<>();

    public IssueAccess(AccessUser accessUser) throws DatabaseException {
        hasReadAllPermission = accessUser.hasPermission(Permissions.ISSUE_READ_PERMISSION);
    }

    public boolean hasReadAllPermission() {
        return hasReadAllPermission;
    }

    public boolean hasPermission(Integer objectId) {
        return hasReadAllPermission || allowedObjects.contains(objectId);
    }

    public void setAllowedIssues(Set<Integer> allowedObjects) {
        this.allowedObjects = allowedObjects;
    }

    public void setAllowedIssues(List<Issue> issues) {
        if (issues != null) {
            for (Issue issue : issues) {
                allowedObjects.add(issue.getId());
            }
        }
    }
}
