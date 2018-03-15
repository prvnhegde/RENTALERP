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

import java.util.ArrayList;
import java.util.List;

/**
 * TabBodyTemplate
 */
public class TabBodyTemplate extends BaseTemplate {

    public TabBodyTemplate() {
        super(TabBodyTemplate.class);
    }

    public void init() {}

    public void applyTemplate() throws Exception {
    }

    @Override
    public String getJspPath() {
        return "/jsp/common/template/TabBody.jsp";
    }

    private Table table = new Table();

    public class Table {
        private List<Tr> list = new ArrayList<>();

        public void add(Tr tr) {
            list.add(tr);
        }

        public List<Tr> getTr() {
            return list;
        }
    }

    public class Tr {
        private List<Td> list = new ArrayList<>();

        public void add(Td td) {
            list.add(td);
        }

        public List<Td> getTd() {
            return list;
        }
    }

    public class Td {
        private String value;
        private String style;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }
    }

    public void add(Tr tr) {
        table.add(tr);
    }

    public Table getTable() {
        return table;
    }
}
