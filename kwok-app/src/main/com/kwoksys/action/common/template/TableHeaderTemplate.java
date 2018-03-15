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
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SortByIconLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TableHeaderTemplate class.
 */
public class TableHeaderTemplate extends BaseTemplate {

    private List<String> columnList;
    private List<String> sortableColumnList;
    private String columnPath;
    private String columnTextKey;
    private Object rowCmd;
    private String orderBy;
    private String order;
    private List<Map> headerList;
    private String orderParamName = "order";
    private String orderByParamName = "orderBy";

    public TableHeaderTemplate(String prefix) {
        super(TableHeaderTemplate.class, prefix);
    }

    public void init() {}

    public void applyTemplate() {
        if (columnPath != null) {
            String columnPathSeparator = !columnPath.contains("?") ? "?" : "&";

            columnPath += columnPathSeparator;
        }

        headerList = new ArrayList<>();

        StringBuilder urlParams = new StringBuilder();

        SortByIconLink sortByUi = new SortByIconLink(order);
        sortByUi.setOrderParamName(orderParamName);
        urlParams.append(sortByUi.getUrl());

        if (rowCmd != null) {
            urlParams.append("&rowCmd=").append(rowCmd);
        }

        // Loop through the header list.
        for (String column : columnList) {
            Link link = new Link(requestContext);
            link.setEscapeTitle(column.isEmpty() || column.equals(BaseObject.ROWNUM) ? "&nbsp;" : Localizer.getText(requestContext, columnTextKey + column));

            // Display the column headings and sort icons.
            if (sortableColumnList!=null && sortableColumnList.contains(column)) {
                link.setImgSrc(sortByUi.getImg(orderBy, column));
                link.setAjaxPath(columnPath + orderByParamName + "=" + column + urlParams);
            }
            Map<String, String> map = new HashMap<>();
            // We use this to control column size for Rownum and empty heading
            map.put("key", column.isEmpty() ? BaseObject.ROWNUM : column);
            map.put("name", link.getString());
            headerList.add(map);
        }
    }

    public void setColumnPath(String columnPath) {
        this.columnPath = columnPath;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }
    public void setSortableColumnList(List<String> sortableColumnList) {
        this.sortableColumnList = sortableColumnList;
    }

    public void setColumnTextKey(String columnTextKey) {
        this.columnTextKey = columnTextKey;
    }
    public void setRowCmd(Object rowCmd) {
        this.rowCmd = rowCmd;
    }
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    public void setOrder(String order) {
        this.order = order;
    }
    public List<Map> getHeaderList() {
        return headerList;
    }

    public String getOrderParamName() {
        return orderParamName;
    }

    public void setOrderParamName(String orderParamName) {
        this.orderParamName = orderParamName;
    }

    public String getOrderByParamName() {
        return orderByParamName;
    }

    public void setOrderByParamName(String orderByParamName) {
        this.orderByParamName = orderByParamName;
    }
}
