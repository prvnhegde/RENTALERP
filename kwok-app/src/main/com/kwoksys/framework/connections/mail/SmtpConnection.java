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
 * SmtpConnection.
 */
public class SmtpConnection {

    private String host = ConfigManager.email.getSmtpHost();
    private String port = ConfigManager.email.getSmtpPort();
    private String username = ConfigManager.email.getSmtpUsername();
    private String password = ConfigManager.email.getSmtpPassword();
    private boolean starttls = ConfigManager.email.getSmtpStartTls();

    public boolean isAuthRequired() {
        return !username.isEmpty() && !password.isEmpty();
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
    public boolean getStarttls() {
        return starttls;
    }
    public void setStarttls(boolean starttls) {
        this.starttls = starttls;
    }
}
