/*
 * Copyright 2017 Kwoksys
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
package com.kwoksys.test.cases;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.test.cases.KwokTestCase;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;

import org.apache.struts.action.ActionMessages;

/**
 * HardwareServiceTest class.
 */
public class HardwareServiceTest extends KwokTestCase {

    public HardwareServiceTest(RequestContext requestContext) {
        super(requestContext);
    }

    public void execute() throws Exception {
        testGetHardwareCount();
        testAddHardware();
    }

    private void testGetHardwareCount() throws DatabaseException {
        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        int count;
        try {
            count = hardwareService.getHardwareCount(new QueryCriteria());
        } catch (Exception e) {
            count = -1;
        }
        addResult("Number of hardware:" + count, (count != -1));
    }

    private void testAddHardware() throws DatabaseException {
        String hardwareName = "test_hardware_" + System.currentTimeMillis();

        Hardware hardware = new Hardware();
        hardware.setName(hardwareName);
        hardware.setDescription("This is a test hardware.");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);

        // Add the hardware
        ActionMessages errors = hardwareService.addHardware(hardware, null);
        addResult("Adding hardware... " + hardware.getName() + ", " + errors, errors.isEmpty());

        // Test it again with the same name. If unique hardware name check is on, it should throw an error, and vice
        // versa.
        hardware = new Hardware();
        hardware.setName(hardwareName);
        hardware.setDescription("This is a test hardware.");

        errors = hardwareService.addHardware(hardware, null);
        addResult("Adding duplicate hardware... " + hardware.getName() + ", " + errors,
                ConfigManager.app.isCheckUniqueHardwareName() ? !errors.isEmpty() : errors.isEmpty());
    }
}
