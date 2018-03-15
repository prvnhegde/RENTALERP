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
package com.kwoksys.action.kb;

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.framework.http.RequestContext;

/**
 * Action form for article search.
 */
public class ArticleSearchForm extends BaseForm {

    private String articleText;
    private Integer categoryId;
    private String namespace;

    @Override
    public void setRequest(RequestContext requestContext) {
        articleText = requestContext.getParameterString("articleText");
        categoryId = requestContext.getParameterInteger("categoryId");
        namespace = requestContext.getParameterString("namespace");
    }

    //
    // Getters and setters
    //
    public String getArticleText() {
        return articleText;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getNamespace() {
        return namespace;
    }
}