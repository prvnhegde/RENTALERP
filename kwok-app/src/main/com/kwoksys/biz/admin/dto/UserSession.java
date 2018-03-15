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
package com.kwoksys.biz.admin.dto;

/**
 * User Session Object.
 */
public class UserSession {

    private Integer userId;              // User Id. The ID of User being modified.
    private String lastLogon;
    private String lastVisit;
    private String sessionKey;

    //
    // Getter and Setter
    //
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getLastLogon() {
        return lastLogon;
    }
    public void setLastLogon(String lastLogon) {
        this.lastLogon = lastLogon;
    }
    public String getLastVisit() {
        return lastVisit;
    }
    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }
    public String getSessionKey() {
        return sessionKey;
    }
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
