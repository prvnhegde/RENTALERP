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
package com.kwoksys.action.hardware;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.data.DataRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HardwareListTemplate
 */
public class HardwareListTemplate extends BaseTemplate {

    private List<Hardware> hardwareList;
    private List<DataRow> formattedList;
    private Counter counter;
    private String hardwarePath;
    private boolean canRemoveHardware;
    private int colspan;
    private String listHeader;
    private Map<String, String> formHiddenVariableMap = new HashMap<>();

    public HardwareListTemplate(String prefix) {
        super(HardwareListTemplate.class, prefix);
    }

    public void init() {}

    public void applyTemplate() throws Exception {
        if (hardwarePath == null) {
            hardwarePath = AppPaths.HARDWARE_DETAIL;
        }

        if (counter == null) {
            counter = new Counter();
        }

        formattedList = HardwareUtils.formatHardwareList(requestContext, hardwareList, counter, hardwarePath);
    }

    public void setHardwareList(List<Hardware> hardwareList) {
        this.hardwareList = hardwareList;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public boolean isCanRemoveHardware() {
        return canRemoveHardware;
    }

    public void setCanRemoveHardware(boolean canRemoveHardware) {
        this.canRemoveHardware = canRemoveHardware;
    }

    public List<DataRow> getFormattedList() {
        return formattedList;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public String getHardwarePath() {
        return hardwarePath;
    }

    public void setHardwarePath(String hardwarePath) {
        this.hardwarePath = hardwarePath;
    }

    public String getListHeader() {
        return listHeader;
    }

    public void setListHeader(String listHeader) {
        this.listHeader = listHeader;
    }

    public Map<String, String> getFormHiddenVariableMap() {
        return formHiddenVariableMap;
    }
}