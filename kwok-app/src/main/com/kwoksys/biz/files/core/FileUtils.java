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
package com.kwoksys.biz.files.core;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;

/**
 * FileUtil
 */
public class FileUtils {

    public static final String CSV_EXTENSION = ".csv";
    public static final String PDF_EXTENSION = ".pdf";
    
    public static List<String> getFileColumnHeaders() {
        return Arrays.asList(File.ROWNUM, File.NAME, File.LABEL, File.CREATION_DATE, File.FILE_SIZE);
    }

    public static boolean isSortableColumn(String columnName) {
        return getSortableColumns().contains(columnName);
    }

    public static List<String> getSortableColumns() {
        return Arrays.asList(File.NAME, File.LABEL, File.CREATION_DATE, File.FILE_SIZE);
    }

    public static String formatFileSize(RequestContext requestContext, long fileSize) {
        int kilobyteUnits = ConfigManager.file.getKilobyteUnits();

        String fileSizeString;
        String[] sizeIndex = {"bytes", "KB", "MB", "GB"};
        int logIndex = 0;

        if (fileSize >= kilobyteUnits) {
            logIndex = (int) Math.floor(Math.log(fileSize) / Math.log(kilobyteUnits));
            if (logIndex > sizeIndex.length - 1) {
                logIndex = sizeIndex.length - 1;
            }
            NumberFormat formatter = new DecimalFormat("0.00");
            fileSizeString = formatter.format(fileSize / Math.pow(kilobyteUnits, logIndex));

        } else {
            NumberFormat formatter = new DecimalFormat("0");
            fileSizeString = formatter.format(fileSize);
        }

        String[] args = {fileSizeString, Localizer.getText(requestContext, "files.colData.file_size." +
                sizeIndex[logIndex])};
        return Localizer.getText(requestContext, "files.colData.file_byte_size", args);
    }
}
