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
package com.kwoksys.biz.admin.dto;

import com.kwoksys.biz.system.core.Image;
import com.kwoksys.framework.ui.Link;

/**
 * DbSequence
 */
public class DbSequence {

    private String name;

    private long lastValue;

    private String tableColumnName;

    private long tableMaxValue;

    public String getStatusIcon() {
        return tableMaxValue > lastValue ?
                new Link(null).setMaterialIcon(Image.getInstance().getStatusError()).getString() :
                new Link(null).setMaterialIcon(Image.getInstance().getStatusCheck()).getString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastValue() {
        return lastValue;
    }

    public void setLastValue(long lastValue) {
        this.lastValue = lastValue;
    }

    public String getTableColumnName() {
        return tableColumnName;
    }

    public void setTableColumnName(String tableColumnName) {
        this.tableColumnName = tableColumnName;
    }

    public long getTableMaxValue() {
        return tableMaxValue;
    }

    public void setTableMaxValue(long tableMaxValue) {
        this.tableMaxValue = tableMaxValue;
    }
}
