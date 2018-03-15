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
package com.kwoksys.schema;

import com.kwoksys.biz.system.license.LicenseValidator;
import com.kwoksys.schema.output.BaseOutput;
import com.kwoksys.schema.output.SystemOutput;

import java.util.Arrays;
import java.util.List;

/**
 * SchemaSetupInput
 */
public class SchemaSetupParams {

    public static final String CREATE = "install";
    public static final String UPGRADE = "upgrade";
    public static final String ENCRYPT = "encrypt";
    public static final String STATS = "stats";
    public static final List<String> VALID_COMMANDS = Arrays.asList(CREATE, UPGRADE, ENCRYPT, STATS);

    public static final String DATABASE_PARAM = "database";
    public static final String USERNAME_PARAM = "username";
    public static final String PASSWORD_PARAM = "password";
    public static final String HOST_PARAM = "host";
    public static final String PORT_PARAM = "port";
    public static final String DEBUG_PARAM = "debug";
    public static final String LICENSE_PARAM = "license";

    //
    // Required fields
    //
    // New install or upgrade
    private String installType = "";
    // Database name
    private String database = "";
    private String username = "";
    private String password = "";

    //
    // Non-required fields
    //
    private String host = "localhost";
    private String port = "5432";

    // Required for encrypt and stats commands.
    private String license = "";

    //
    // We don't expect user to change these
    //
    // We run a check to get installed version
    private String installedVersion = "";
    // In theory, user could use a different postgres/db driver
    private String driverName = "org.postgresql.Driver";

    private boolean isDebug = false;

    public boolean validateInputs(String args[]) {
        boolean valid = true;

        for (String arg : args) {
            String[] keyValue = arg.split("=");

            try {
                if (SchemaSetupParams.VALID_COMMANDS.contains(keyValue[0])) {
                    setInstallType(keyValue[0]);

                } else if (keyValue[0].equals(SchemaSetupParams.DATABASE_PARAM)) {
                    setDatabase(keyValue[1]);

                } else if (keyValue[0].equals(SchemaSetupParams.USERNAME_PARAM)) {
                    setUsername(keyValue[1]);

                } else if (keyValue[0].equals(SchemaSetupParams.PASSWORD_PARAM)) {
                    setPassword(keyValue[1]);

                } else if (keyValue[0].equals(SchemaSetupParams.HOST_PARAM)) {
                    setHost(keyValue[1]);

                } else if (keyValue[0].equals(SchemaSetupParams.PORT_PARAM)) {
                    setPort(keyValue[1]);

                } else if (keyValue[0].equals(SchemaSetupParams.LICENSE_PARAM)) {
                    setLicense(keyValue[1]);

                } else if (keyValue[0].equals(SchemaSetupParams.DEBUG_PARAM)) {
                    setDebug(keyValue[1].equals("true"));
                }
            } catch (Exception e) {
                // don't need to output anything
            }
        }

        BaseOutput out = SystemOutput.getInstance();

        if (getInstallType().isEmpty()) {
            valid = false;
            out.println("You must specify either install or upgrade option");

        } else if (getInstallType().equals(SchemaSetupParams.CREATE)
                || getInstallType().equals(SchemaSetupParams.UPGRADE)
                || getInstallType().equals(SchemaSetupParams.STATS)) {
            if (getDatabase().isEmpty()) {
                valid = false;
                out.println("\"" + SchemaSetupParams.DATABASE_PARAM + "\" option is missing");
            }
            if (getDatabase().equals("postgres")) {
                valid = false;
                out.println("postgres is default database, we don't support installing on it");
            }
            if (getUsername().isEmpty()) {
                valid = false;
                out.println("\"" + SchemaSetupParams.USERNAME_PARAM + "\" option is missing");
            }
        }
        if (getInstallType().equals(SchemaSetupParams.STATS)
                || getInstallType().equals(SchemaSetupParams.ENCRYPT)) {
            if (getLicense().isEmpty()) {
                valid = false;
                out.println("\"" + SchemaSetupParams.LICENSE_PARAM + "\" option is missing or empty");
            }

            boolean validLicense = LicenseValidator.isValid(getLicense());
            if (!validLicense) {
                valid = false;
                out.println("Invalid license key " + getLicense());
            }
        }
        if (getPassword().isEmpty()) {
            valid = false;
            out.println("\"" + SchemaSetupParams.PASSWORD_PARAM + "\" option is missing");
        }
        return valid;
    }

    //
    // Getters and setters
    //
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
    public String getDatabase() {
        return database;
    }
    public void setDatabase(String database) {
        this.database = database;
    }
    public String getInstalledVersion() {
        return installedVersion;
    }
    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion;
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
    public String getDriverName() {
        return driverName;
    }
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public String getInstallType() {
        return installType;
    }
    public void setInstallType(String installType) {
        this.installType = installType;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
