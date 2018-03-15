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
package com.kwoksys.schema.commands;

import com.kwoksys.schema.SchemaSetupParams;
import com.kwoksys.schema.SchemaUtil;
import com.kwoksys.schema.output.BaseOutput;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * SchemaSetupStatCommand
 */
public class SchemaStatsCommand {

    public void execute(BaseOutput out, SchemaSetupParams params) throws Exception {
        out.println("\n=== Schema Stats ===");

        out.println("Host: " + params.getHost());
        out.println("Port: " + params.getPort());
        out.println("Database: " + params.getDatabase());
        out.println("Username: " + params.getUsername());

        String schemaName = "public";
        out.println("Schema: " + schemaName);

        // App version
        out.println("App version: " + SchemaUtil.checkDatabaseVersion(params));

        Connection conn = SchemaUtil.createConnection(params, params.getDatabase());

        // Table count
        PreparedStatement stmt = conn.prepareStatement("select count(1) as table_count from pg_stat_user_tables where schemaname = '" + schemaName + "'");
        stmt.execute();
        ResultSet rs = stmt.getResultSet();
        rs.next();

        out.println("\nTables: " + rs.getString("table_count"));

        // Table records
        stmt = conn.prepareStatement("select relname as table_name, n_tup_ins as row_count from pg_stat_user_tables where schemaname = '" + schemaName + "' order by relname");
        stmt.execute();
        rs = stmt.getResultSet();
        while (rs.next()) {
            out.println("- " + rs.getString("table_name") + ": " + rs.getString("row_count"));
        }

        // Function count
        stmt = conn.prepareStatement("select count(1) function_count FROM pg_proc p LEFT JOIN pg_namespace n ON n.oid = p.pronamespace where n.nspname='" + schemaName + "'");
        stmt.execute();
        rs = stmt.getResultSet();
        rs.next();

        out.println("\nFunctions: " + rs.getString("function_count"));

        // Sequence count
        stmt = conn.prepareStatement("select count(1) as seq_count from pg_statio_all_sequences where schemaname = '" + schemaName + "'");
        stmt.execute();
        rs = stmt.getResultSet();
        rs.next();

        out.println("Sequences: " + rs.getString("seq_count"));

        // View count
        stmt = conn.prepareStatement("select count(1) as view_count from pg_views where schemaname = '" + schemaName + "'");
        stmt.execute();
        rs = stmt.getResultSet();
        rs.next();

        out.println("Views: " + rs.getString("view_count"));

        try {
            rs.close();
            conn.close();
        } catch (Exception e) {}

        out.println("\n=== End ===");
    }
}
