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

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Links;

/**
 * Template for editing file.
 */
public class FileEditTemplate extends BaseTemplate {

    private ActionErrorsTemplate errorsTemplate = new ActionErrorsTemplate();
    private String formAction;
    private String formCancelAction;
    private File file;
    private String fileName;
    private String fileTitle;
    private String fileSize;

    public FileEditTemplate() {
        super(FileEditTemplate.class);
    }

    public void init() {
        addTemplate(errorsTemplate);
    }

    public void applyTemplate() {
        fileName = file.getLogicalName();
        fileTitle = file.getTitle();
        fileSize = file.getFormattedFileSize(requestContext);
    }

    @Override
    public String getJspPath() {
        return "/jsp/files/FileEditTemplate.jsp";
    }

    public String getFormCancelLink() {
        return Links.getCancelLink(requestContext, formCancelAction).getString();
    }

    public String getFormAction() {
        return formAction;
    }
    public void setFormAction(String formAction) {
        this.formAction = AppPaths.ROOT + formAction;
    }

    public void setFormCancelAction(String formCancelAction) {
        this.formCancelAction = formCancelAction;
    }
    
    public String getFileTitle() {
        return fileTitle;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileSize() {
        return fileSize;
    }
   
    public String getFileName() {
        return fileName;
    }

    public ActionErrorsTemplate getErrorsTemplate() {
        return errorsTemplate;
    }
}