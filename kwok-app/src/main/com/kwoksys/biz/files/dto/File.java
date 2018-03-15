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
package com.kwoksys.biz.files.dto;

import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.framework.http.RequestContext;

/**
 * File Object.
 */
public class File extends BaseObject {

    public static final String NAME = "file_name";
    public static final String LABEL = "file_friendly_name";
    public static final String FILE_SIZE = "file_byte_size";

    private Integer id;

    /**
     * e.g. expense-report.xls is a logical name. When it's saved in the file repository, it will have a physical name
     * such as KB-12543
     */
    private String logicalName;

    /**
     * e.g. File expense-report.xls will be persisted in the file repository with the naming convention
     * <objectPrefix>-<fileId>, e.g. KB-12543.
     */
    private String physicalName;

    /**
     * Alternative name supplied by user during file upload
     */
    private String title;

    private String description;
    private String mimeType;    // e.g. text/html, application/excel, we may want a column like this in case customers are savy enough to edit mime type themselve.
    private int size;           // e.g. 1,256 bytes (in terms of bytes).
    protected Integer objectTypeId;
    protected Integer objectId;

    // This is hardly exposed to be seen by anyone. Basically, when we insert a file record,
    // we need a file name, a name to use even before we can get any information from file upload.
    private static final String tempFileName = "TEMP";

    private java.io.File attachedFile;

    protected String configRepositoryPath;  // e.g. c:\temp\vault, we need something like this to know where to retrieve file.
    protected String configUploadedFilePrefix;

    public File() {
        size = 0;
        objectTypeId = 0;
        objectId = 0;
    }

    // Get localized file size.
    public String getFormattedFileSize(RequestContext requestContext) {
        return FileUtils.formatFileSize(requestContext, size);
    }

    //
    // Getter and Setter
    //
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getLogicalName() {
        return logicalName;
    }
    public void setLogicalName(String logicalName) {
        this.logicalName = logicalName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setObjectTypeId(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }
    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }
    public void setConfigRepositoryPath(String configRepositoryPath) {
        this.configRepositoryPath = configRepositoryPath;
    }
    public void setConfigUploadedFilePrefix(String configUploadedFilePrefix) {
        this.configUploadedFilePrefix = configUploadedFilePrefix;
    }
    public String getConfigRepositoryPath() {
        return configRepositoryPath;
    }
    public String getConfigUploadedFilePrefix() {
        return configUploadedFilePrefix;
    }
    public String getMimeType() {
        return mimeType;
    }
    public int getSize() {
        return size;
    }
    public String getPhysicalName() {
        return physicalName;
    }
    public Integer getObjectTypeId() {
        return objectTypeId;
    }
    public Integer getObjectId() {
        return objectId;
    }
    public String getTitle() {
        return title;
    }
    public String getTempFileName() {
        return tempFileName;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public void setFileuploadedFileName(String fileUploadedFileName) {
        this.physicalName = fileUploadedFileName;
    }

    public java.io.File getAttachedFile() {
        return attachedFile;
    }

    public void setAttachedFile(java.io.File attachedFile) {
        this.attachedFile = attachedFile;
    }
}
