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
package com.kwoksys.biz.system.core.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.kwoksys.biz.system.core.configs.LogConfigManager;

/**
 * This is to control the trigger and scheduled jobs.
 */
public class TaskScheduler {

    private static final Logger LOGGER = Logger.getLogger(TaskScheduler.class.getName());

    private static ScheduledExecutorService scheduler;

    public static void init() throws Exception {
        try {
            LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Scheduling jobs...");

            final Runnable runnable = new Runnable() {
                public void run() {
                    new TaskRunner().execute();
                }
            };
            
            // Scheduling SampleTask
            scheduler = Executors.newScheduledThreadPool(3);
            scheduler.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.MINUTES);

            LOGGER.info(LogConfigManager.CONFIG_PREFIX + " Scheduler started");
            
        } catch (Exception e) {
            throw new Exception("Failed to start scheduler", e);
        }
    }
    
    public static void destroy() {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
                LOGGER.info(LogConfigManager.SHUTDOWN_PREFIX + " Scheduler shutdown");
    
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, LogConfigManager.SHUTDOWN_PREFIX + " Error shutting down scheduler", e);
            }
        }
    }
}
