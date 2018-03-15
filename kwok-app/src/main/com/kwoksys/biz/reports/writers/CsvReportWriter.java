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
import com.kwoksys.framework.csv.CsvWriter;

import java.util.List;

/**
 * CsvReportWriter
 */
public class CsvReportWriter extends ReportWriter {

    private CsvWriter writer;

    public void init(ResponseContext responseContext) throws Exception {
        init(responseContext, null);
    }

    @Override
    public void init(ResponseContext responseContext, Report report) throws Exception {
        writer = new CsvWriter(responseContext, ',');

        // Output optional Report Title
        if (report != null && report.getTitle() != null && !report.getTitle().isEmpty()) {
            writer.writeNext(new String[]{report.getTitle()});
        }
    }

    @Override
    public void addHeaderRow(List<String> columnHeaders) {
        writer.writeNext(columnHeaders.toArray(new String[columnHeaders.size()]));
    }

    @Override
    public void addRow(List<String> row) throws Exception {
        writer.writeNext(row.toArray(new String[row.size()]));
    }

    @Override
    public String close() throws Exception {
        writer.close();
        return null;
    }
}
