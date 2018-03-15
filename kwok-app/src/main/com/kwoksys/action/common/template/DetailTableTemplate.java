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

import java.util.ArrayList;
import java.util.List;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.system.core.configs.ConfigManager;

/**
 * DetailTableTemplate
 */
public class DetailTableTemplate extends BaseTemplate {

    private List<List> table = new ArrayList<>();
    private List<Td> tr = new ArrayList<>();
    private int counter;
    private int numColumns = 1;
    private String width = "";
    private String style = ConfigManager.system.getDetailsTableStyle();

    public DetailTableTemplate() {
        this(null);
    }

    public DetailTableTemplate(String prefix) {
        super(DetailTableTemplate.class, prefix);
    }

    public void init() {}

    public void applyTemplate() throws Exception {
        if (numColumns != 1) {
            width = "35%";
        }
    }

    public Td newTd() {
        Td td = new Td();

        if (counter == 0) {
            table.add(tr);

        } else if (counter >= numColumns) {
            counter = 0;

            tr = new ArrayList<>();
            table.add(tr);
        }
        tr.add(td);
        counter++;
        return td;
    }

    public class Td {
        private String headerKey;
        private String headerText;
        private String value;

        private Td() {}

        public String getHeaderKey() {
            return headerKey;
        }

        public void setHeaderKey(String headerKey) {
            this.headerKey = headerKey;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setValue(Integer value) {
            this.value = String.valueOf(value);
        }

        public String getHeaderText() {
            return headerText;
        }

        public void setHeaderText(String headerText) {
            this.headerText = headerText;
        }
    }

    public String getJspPath() {
        return "/jsp/common/template/DetailTable.jsp";
    }

    public List getTable() {
        return table;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    public String getWidth() {
        return width;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
