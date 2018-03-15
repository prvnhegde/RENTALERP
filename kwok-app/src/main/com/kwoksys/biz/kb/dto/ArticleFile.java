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
package com.kwoksys.biz.kb.dto;

import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;

/**
 * ArticleFile class.
 */
public class ArticleFile extends File {

    public ArticleFile(Integer articleId) {
        objectTypeId = ObjectTypes.KB_ARTICLE;
        objectId = articleId;
        configRepositoryPath = ConfigManager.file.getKbFileRepositoryLocation();
        configUploadedFilePrefix = ConfigManager.file.getKbUploadedFilePrefix();
    }
}