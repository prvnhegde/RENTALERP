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
package com.kwoksys.action.contacts;

import com.kwoksys.biz.base.BaseForm;
import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.framework.http.RequestContext;

/**
 * Action class for Company Bookmarks page.
 */
public class CompanyBookmarkForm extends BaseForm {

    private Integer companyId;

    // Bookmarks
    private Integer bookmarkId;
    private String bookmarkName;
    private String bookmarkPath;

    @Override
    public void setRequest(RequestContext requestContext) {
        companyId = requestContext.getParameterInteger("companyId");
        bookmarkId = requestContext.getParameterInteger("bookmarkId");
        bookmarkName = requestContext.getParameterString("bookmarkName");
        bookmarkPath = requestContext.getParameterString("bookmarkPath");
    }

    public void setBookmark(Bookmark bookmark) {
        bookmarkName = bookmark.getName();
        bookmarkPath = bookmark.getPath();
    }

    public Integer getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    public Integer getBookmarkId() {
        return bookmarkId;
    }
    public void setBookmarkId(Integer bookmarkId) {
        this.bookmarkId = bookmarkId;
    }
    public String getBookmarkName() {
        return bookmarkName;
    }

    public String getBookmarkPath() {
        return bookmarkPath;
    }
}
