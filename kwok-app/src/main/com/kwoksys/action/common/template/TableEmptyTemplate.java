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
package com.kwoksys.action.common.template;

import com.kwoksys.biz.base.BaseTemplate;

/**
 * TableEmptyTemplate.
 */
public class TableEmptyTemplate extends BaseTemplate {

    private int colSpan = 1;
    private String rowText;

    public TableEmptyTemplate() {
        this(null);
    }

    public TableEmptyTemplate(String prefix) {
        super(TableEmptyTemplate.class, prefix);
    }

    public void init() {}

    public void applyTemplate() {
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }
    public void setRowText(String rowText) {
        this.rowText = rowText;
    }
    public int getColSpan() {
        return colSpan;
    }

    public String getRowText() {
        return rowText;
    }
}
