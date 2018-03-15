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
package com.kwoksys.schema.commands;

import com.kwoksys.schema.SchemaFactory;
import com.kwoksys.schema.SchemaSetupParams;
import com.kwoksys.schema.SchemaUtil;
import com.kwoksys.schema.output.BaseOutput;
import com.kwoksys.schema.versions.Schema;

/**
 * SchemaSetupCommand
 */
public class SchemaSetupCommand {

    public void execute(BaseOutput out, SchemaSetupParams params) throws Exception {
        Schema schema = SchemaFactory.getLatestSchema();

        if (params.getInstallType().equals(SchemaSetupParams.CREATE)) {
            out.println("Preparing to install version " + schema.getVersion() + "... ");

            // Create database
            schema.createDatabase(params);
            
            schema.prepDatabase(params);

            // Create schema
            schema.create(params);

        } else if (params.getInstallType().equals(SchemaSetupParams.UPGRADE)) {
            // Get and set installed version
            params.setInstalledVersion(SchemaUtil.checkDatabaseVersion(params));

            // Checks installed version
            if (params.getInstalledVersion().equals(schema.getVersion())) {
                out.println("Schema has the lastest version " + params.getInstalledVersion() + ", no upgrade required");
                return;

            } else if (SchemaFactory.getSchema(params.getInstalledVersion()) == null) {
                throw new Exception("Upgrade from your installed version " + params.getInstalledVersion() + " is not supported");
            }

            out.println("Preparing to upgrade from version " + params.getInstalledVersion() + " to version " +
                    schema.getVersion() + "... ");

            schema.prepDatabase(params);

            schema.upgrade(params);
        }
        out.println("Schema setup completed successfully");
    }
}
