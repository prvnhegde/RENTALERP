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
package com.kwoksys.action.files;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;

/**
 * Template for Delete File.
 */
public class FileDeleteTemplate extends BaseTemplate {

    private String formAction;
    private String formCancelAction;
    private File file;

    private String fileName;
    private String fileTitle;
    private String fileSize;

    public FileDeleteTemplate() {
        super(FileDeleteTemplate.class);
    }

    public void init() {
        addTemplate(new ActionErrorsTemplate());
    }

    public void applyTemplate() {
        fileName = file.getLogicalName();
        fileTitle = file.getTitle();
        fileSize = file.getFormattedFileSize(requestContext);
    }

    @Override
    public String getJspPath() {
        return "/jsp/files/FileDeleteTemplate.jsp";
    }

    public void setFormAction(String formAction) {
        this.formAction = AppPaths.ROOT + formAction;
    }

    public String getFormAction() {
        return formAction;
    }

    public String getFormCancelLink() {
        return Links.getCancelLink(requestContext, formCancelAction).getString();
    }

    public void setFormCancelAction(String formCancelAction) {
        this.formCancelAction = formCancelAction;
    }
    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public String getFileSize() {
        return fileSize;
    }
}