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
package com.kwoksys.biz.reports.writers;

import com.kwoksys.biz.reports.Report;
import com.kwoksys.framework.http.ResponseContext;

import java.util.List;

/**
 * ReportWriter
 */
public abstract class ReportWriter {

    protected List<String> columnHeaders;

    public abstract void init(ResponseContext responseContext, Report report) throws Exception;

    public abstract void addHeaderRow(List<String> columnHeaders);

    public abstract void addRow(List<String> row) throws Exception;

    public abstract String close() throws Exception;

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }
}
