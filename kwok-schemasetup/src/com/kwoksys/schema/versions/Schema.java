/*
 * Copyright 2015 Kwoksys
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
package com.kwoksys.schema.versions;

import com.kwoksys.schema.SchemaFactory;
import com.kwoksys.schema.SchemaSetupParams;
import com.kwoksys.schema.SchemaUtil;
import com.kwoksys.schema.output.BaseOutput;
import com.kwoksys.schema.output.SystemOutput;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Schema creation class.
 */
public class Schema {

    protected static BaseOutput output = SystemOutput.getInstance();

    /** Schema version number **/
    protected String version;

    private String createSql;
    private String upgradeSql;
    private String refreshSql;

    public Schema(String version) {
        this.version = version;
        createSql = "versions/postgres/v" + version + "/create-schema-base-v" + version + ".sql";
        upgradeSql = "versions/postgres/v" + version + "/upgrade-to-v" + version + ".sql";
        refreshSql = "versions/postgres/v" + version + "/refresh-views-and-procedures-v" + version + ".sql";
    }

    /**
     * Creates a blank database.
     * @param stmt
     * @param params
     * @throws java.sql.SQLException
     */
    public void createDatabase(SchemaSetupParams params) throws SQLException {
        Connection conn = SchemaUtil.createConnection(params, "");
        Statement stmt = conn.createStatement();

        output.print("Creating database " + params.getDatabase() + "... ");
        stmt.executeUpdate("CREATE DATABASE "+ params.getDatabase() + " WITH ENCODING 'UTF8'");
        output.println("done");

        SchemaUtil.close(stmt);
        SchemaUtil.close(conn);
    }

    /**
     * Preppares database for install/upgrade
     * @param stmt
     * @throws java.sql.SQLException
     */
    public void prepDatabase(SchemaSetupParams params) throws SQLException {
        // Connecting to existing repository
        Connection conn = SchemaUtil.createConnection(params, params.getDatabase());
        Statement stmt = conn.createStatement();
        
        // Update timezone to GMT
        output.print("Updating database timezone to GMT...");
        stmt.executeUpdate("ALTER DATABASE " + params.getDatabase() + " SET TimeZone='GMT'");
        output.println("done");
        
        // Create plpgsql language
        try {
            output.print("Creating language 'plpgsql'... ");
            stmt.executeUpdate("CREATE TRUSTED PROCEDURAL LANGUAGE 'plpgsql' HANDLER plpgsql_call_handler VALIDATOR plpgsql_validator");
            output.println("done");

        } catch (Exception e) {
            if (params.isDebug()) {
                output.println(e);
            } else {
                output.println("Language 'plpgsql' already exists... done");
            }
        }
        SchemaUtil.close(stmt);
        SchemaUtil.close(conn);
    }

    /**
     * Creates database schema.
     * @param params
     * @throws java.sql.SQLException
     */
    public void create(SchemaSetupParams params) throws SQLException, IOException {
        Connection conn = SchemaUtil.createConnection(params, params.getDatabase());
        Statement stmt = conn.createStatement();

        // Create base schema
        createSchema(stmt);

        // Bring the schema to current version
        upgradeSchema(stmt);

        // Creating views and stored procedures
        createViewsProcedures(stmt);

        stmt.close();
        SchemaUtil.close(conn);
    }

    /**
     * Upgrades database schema.
     * @param conn
     * @param stmt
     * @param params
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public void upgrade(SchemaSetupParams params) throws SQLException, IOException {
        Schema previousSchema = SchemaFactory.getPreviousSchema(version);

        if (!params.getInstalledVersion().equals(previousSchema.getVersion())) {
            previousSchema.upgrade(params);
        }

        Connection conn = SchemaUtil.createConnection(params, params.getDatabase());
        Statement stmt = conn.createStatement();

        // Create tables
        upgradeSchema(stmt);

        // Creating views and stored procedures, only for the latest upgrade
        if (SchemaFactory.getPreviousSchema(SchemaFactory.getLatestSchema().getVersion()).equals(previousSchema)) {
            createViewsProcedures(stmt);
        }

        stmt.close();
        SchemaUtil.close(conn);
    }

    /**
     * Creates tables.
     * @param stmt
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    private void createSchema(Statement stmt) throws SQLException, IOException {
        output.print("Creating schema base... ");
        stmt.executeUpdate(SchemaUtil.loadSqlfile(createSql));
        output.println("done");
    }

    /**
     * Creates views and stored procedures.
     * @param stmt
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    private void createViewsProcedures(Statement stmt) throws SQLException, IOException {
        output.print("Creating views and stored procedures... ");
        stmt.executeUpdate(SchemaUtil.loadSqlfile(refreshSql));
        output.println("done");
    }

    /**
     * Runs upgrade statements.
     */
    private void upgradeSchema(Statement stmt) throws SQLException, IOException {
        output.print("Upgrading schema to version " + getVersion() + "... ");
        stmt.executeUpdate(SchemaUtil.loadSqlfile(upgradeSql));
        output.println("done");
    }

    //
    // Getters and setters
    //
    public String getVersion() {
        return version;
    }
}
