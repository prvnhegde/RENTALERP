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

import java.util.HashMap;
import java.util.Map;

/**
 * FileTypeHelper
 */
public class FileTypeHelper {

    private static final Map<String, String> extMap = new HashMap<>();

    public static final String IMAGE_IMG = "file_image.png";
    public static final String PDF_IMG = "file_pdf.png";
    public static final String UNKNOWN_IMG = "page.png";
    public static final String MS_EXCEL_IMG = "page_excel.png";
    public static final String MS_POWERPOINT_IMG = "file_powerpoint.png";
    public static final String MS_WORD_IMG = "file_word.png";
    public static final String RAR_IMG = "file_zip.png";
    public static final String ZIP_IMG = "file_zip.png";

    static {
        extMap.put("xls", MS_EXCEL_IMG);
        extMap.put("xlsx", MS_EXCEL_IMG);
        extMap.put("ppt", MS_POWERPOINT_IMG);
        extMap.put("pptx", MS_POWERPOINT_IMG);
        extMap.put("doc", MS_WORD_IMG);
        extMap.put("docx", MS_WORD_IMG);
        extMap.put("pdf", PDF_IMG);
        extMap.put("gif", IMAGE_IMG);
        extMap.put("jpg", IMAGE_IMG);
        extMap.put("png", IMAGE_IMG);
        extMap.put("rar", RAR_IMG);
        extMap.put("zip", ZIP_IMG);
    }

    public static String getExtImage(String fileTitle) {
        String ext = parseExt(fileTitle)[1];
        String extImage = extMap.get(ext.toLowerCase());
        return extImage == null ? UNKNOWN_IMG : extImage;
    }

    public static String[] parseExt(String fileName) {
        int lastDot = fileName.lastIndexOf(".");

        if (lastDot == -1) {
            return new String[]{fileName, ""};
        } else {
            return new String[]{fileName.substring(0, lastDot), fileName.substring(lastDot+1, fileName.length())};
        }
    }
}