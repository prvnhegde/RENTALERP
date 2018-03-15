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
package com.kwoksys.framework.validations;

import com.kwoksys.framework.util.StringUtils;

/**
 * ColumnField
 */
public class ColumnField {

    private String name;
    private String columnName;
    private String titleKey;
    private int length;
    private boolean nullable = true;

    public String getName() {
        return name;
    }

    public ColumnField setName(String name) {
        this.name = name;
        return this;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public ColumnField setTitleKey(String titleKey) {
        this.titleKey = titleKey;
        return this;
    }

    public int getLength() {
        return length;
    }

    public ColumnField calculateLength(String value) {
        this.length = StringUtils.isEmpty(value) ? 0 : value.length();
        return this;
    }

    public String getColumnName() {
        return columnName;
    }

    public ColumnField setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public ColumnField setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }
}
