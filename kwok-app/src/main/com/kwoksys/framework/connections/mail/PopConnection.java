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
package com.kwoksys.framework.connections.mail;

import com.kwoksys.biz.system.core.configs.ConfigManager;

/**
 * PopConnection.
 */
public class PopConnection {

    private String host;
    private String port;
    private String username;
    private String password;
    private int messagesLimit;
    private String senderIgnoreList;
    private boolean sslEnabled = ConfigManager.email.isPopSslEnabled();

    // Whether to delete message after reading. Default is true.
    private boolean deleteFlag = true;

    /**
     * Use this to keep track of the number of emails pulled.
     */
    private int messagesRetrieved;

    /**
     * Determines if a connection is configured by checking host, port, username, password, etc. If these don't exist,
     * connection probably won't work.
     * @return
     */
    public boolean isConfigured() {
        return !host.isEmpty() && !port.isEmpty() && !username.isEmpty() && !password.isEmpty();
    }

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getMessagesLimit() {
        return messagesLimit;
    }
    public void setMessagesLimit(int messagesLimit) {
        this.messagesLimit = messagesLimit;
    }
    public String getSenderIgnoreList() {
        return senderIgnoreList;
    }
    public void setSenderIgnoreList(String senderIgnoreList) {
        this.senderIgnoreList = senderIgnoreList;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public int getMessagesRetrieved() {
        return messagesRetrieved;
    }

    public void setMessagesRetrieved(int messagesRetrieved) {
        this.messagesRetrieved = messagesRetrieved;
    }
}