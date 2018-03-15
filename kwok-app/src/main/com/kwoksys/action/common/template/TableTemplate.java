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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.framework.data.DataRow;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;

/**
 * TableTemplate
 */
public class TableTemplate extends BaseTemplate {

    public static final String STYLE_TAB = "tabBody";
    public static final String STYLE_LIST = "listTable stripedListTable";

    // Headers
    private List<String> columnHeaders;
    private List<String> sortableColumnHeaders;
    private String columnPath;
    private String columnTextKey;
    private String rowCmd;
    private String orderBy;
    private String order;
    private String orderByParamName;
    private String orderParamName;
    private int colSpan;
    private boolean canRemoveItem;
    private String formRemoveItemAction;
    // Checkbox or radio buttons
    private boolean formSelectMultipleRows;
    private Map<String, String> formHiddenVariableMap = new LinkedHashMap<>();
    private Map<String, String> formButtons = new LinkedHashMap<>();
    private String formRowIdName;
    private String formName = RequestContext.FORM_KEY;

    // Table content
    private String style = STYLE_LIST;
    
    private List<DataRow> dataList = new ArrayList<>();

    // Empty table
    private String emptyRowMsgKey = "";

    private TableHeaderTemplate tableHeader;
    private TableEmptyTemplate tableEmptyTemplate;

    public TableTemplate() {
        this(null);
    }

    public TableTemplate(String prefix) {
        super(TableTemplate.class, prefix);
    }

    public void init() {
        tableHeader = addTemplate(new TableHeaderTemplate(prefix));
        tableEmptyTemplate = addTemplate(new TableEmptyTemplate(prefix));
    }

    public void applyTemplate() throws Exception {
        if (requestContext.getUser() != null && formRemoveItemAction != null) {
            canRemoveItem = requestContext.getUser().hasPermission(formRemoveItemAction);
        }

        if (canRemoveItem) {
            columnHeaders = new ArrayList<>(columnHeaders);
            // Add an extra blank column to the headers, that's for the radio button to remove hardware
            columnHeaders.add(0, "");
            
            if (formButtons.isEmpty()) {
                formButtons.put("form.button.remove", "common.form.confirmRemove");
            }
        }
        
        tableHeader.setColumnList(columnHeaders);
        tableHeader.setSortableColumnList(sortableColumnHeaders);
        tableHeader.setColumnPath(columnPath);
        tableHeader.setColumnTextKey(columnTextKey);
        tableHeader.setRowCmd(rowCmd);
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        if (orderByParamName != null) {
            tableHeader.setOrderByParamName(orderByParamName);
        }
        if (orderParamName != null) {
            tableHeader.setOrderParamName(orderParamName);
        }

        colSpan = columnHeaders.size();

        if (dataList == null || dataList.isEmpty()) {
            tableEmptyTemplate.setColSpan(columnHeaders.size());

            if (!emptyRowMsgKey.isEmpty()) {
                tableEmptyTemplate.setRowText(Localizer.getText(requestContext, emptyRowMsgKey));
            }
        }
    }

    public void addRow(List<String> columns) {
        DataRow dataRow = new DataRow();
        dataRow.setColumns(columns);
        dataList.add(dataRow);
    }

    @Override
    public String getJspPath() {
        return "/jsp/common/template/Table.jsp";
    }

    public void setColumnPath(String columnPath) {
        this.columnPath = columnPath;
    }

    public void setColumnHeaders(List<String> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public void setSortableColumnHeaders(List<String> sortableColumnHeaders) {
        this.sortableColumnHeaders = sortableColumnHeaders;
    }

    public void setColumnTextKey(String columnTextKey) {
        this.columnTextKey = columnTextKey;
    }

    public void setRowCmd(String rowCmd) {
        this.rowCmd = rowCmd;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setDataList(List<DataRow> dataList) {
        this.dataList = dataList;
    }

    public List<DataRow> getDataList() {
        return dataList;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    public String getOrderByParamName() {
        return orderByParamName;
    }

    public void setOrderByParamName(String orderByParamName) {
        this.orderByParamName = orderByParamName;
    }

    public String getOrderParamName() {
        return orderParamName;
    }

    public void setOrderParamName(String orderParamName) {
        this.orderParamName = orderParamName;
    }

    public int getColSpan() {
        return colSpan;
    }

    public String getFormRemoveItemAction() {
        return formRemoveItemAction;
    }

    public void setFormRemoveItemAction(String formRemoveItemAction) {
        this.formRemoveItemAction = formRemoveItemAction;
    }

    public boolean isCanRemoveItem() {
        return canRemoveItem;
    }

    public String getFormRowIdName() {
        return formRowIdName;
    }

    public void setFormRowIdName(String formRowIdName) {
        this.formRowIdName = formRowIdName;
    }

    public Map<String, String> getFormButtons() {
        return formButtons;
    }

    public Map<String, String> getFormHiddenVariableMap() {
        return formHiddenVariableMap;
    }

    public void setFormHiddenVariableMap(Map<String, String> formHiddenVariableMap) {
        this.formHiddenVariableMap = formHiddenVariableMap;
    }

    public void setEmptyRowMsgKey(String emptyRowMsgKey) {
        this.emptyRowMsgKey = emptyRowMsgKey;
    }

    public void setFormSelectMultipleRows(boolean formSelectMultipleRows) {
        this.formSelectMultipleRows = formSelectMultipleRows;
    }

    public boolean isFormSelectMultipleRows() {
        return formSelectMultipleRows;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
