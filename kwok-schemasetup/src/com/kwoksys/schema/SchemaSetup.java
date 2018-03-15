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
import com.kwoksys.schema.commands.SchemaSetupCommand;
import com.kwoksys.schema.commands.SchemaStatsCommand;
import com.kwoksys.schema.exceptions.DatabaseDriverException;
import com.kwoksys.schema.output.BaseOutput;
import com.kwoksys.schema.output.SystemOutput;

/**
 * SchemaSetup
 */
public class SchemaSetup {

    public static void main(String args[]) {
        BaseOutput out = SystemOutput.getInstance();

        SchemaSetupParams params = new SchemaSetupParams();

        if (!params.validateInputs(args)) {
            return;
        }

        try {
            if (params.getInstallType().equals(SchemaSetupParams.ENCRYPT)) {
                out.println(LicenseValidator.hash(params.getPassword()));
                return;
            }

            out.print("Loading database driver " + params.getDriverName() + "... ");
            try {
                Class.forName(params.getDriverName());
            } catch (ClassNotFoundException e) {
                throw new DatabaseDriverException("Problem loading driver: " + e.getMessage());
            }
            out.println("done");

            if (params.getInstallType().equals(SchemaSetupParams.STATS)) {
                new SchemaStatsCommand().execute(out, params);

            } else {
                new SchemaSetupCommand().execute(out, params);
            }
        } catch (Exception e) {
            out.println("failed. " + e.getMessage() + ". Cause: " + e.getCause());
            out.println("Schema setup encountered errors");
        }
    }
}
