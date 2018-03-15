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
package com.kwoksys.action.blogs;

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.http.RequestContext;

/**
 * ActionForm for adding/edting post category.
 */
public class CategoryForm extends BaseForm {

    private Integer categoryId;
    private String categoryName;

    @Override
    public void setRequest(RequestContext requestContext) {
        categoryId = requestContext.getParameterInteger("categoryId");
        categoryName = requestContext.getParameterString("categoryName");
    }

    public void setCategory(Category category) {
        categoryName = category.getName();
    }

    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
}