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
package com.kwoksys.biz.system;

import java.util.List;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.system.dao.BookmarkDao;
import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;

/**
 * BookmarkService
 */
public class BookmarkService {

    private RequestContext requestContext;

    public BookmarkService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public List<Bookmark> getBookmarks(QueryCriteria query, Integer objectTypeId, Integer objectId) throws DatabaseException {
        BookmarkDao bookmarkDao = new BookmarkDao(requestContext);
        return bookmarkDao.getBookmarks(query, objectTypeId, objectId);
    }

    public Bookmark getBookmark(Integer objectTypeId, Integer objectId, Integer bookmarkId) throws DatabaseException,
            ObjectNotFoundException {

        BookmarkDao bookmarkDao = new BookmarkDao(requestContext);
        return bookmarkDao.getBookmark(objectTypeId, objectId, bookmarkId);
    }

    public ActionMessages addBookmark(Bookmark bookmark) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (bookmark.getName().isEmpty()) {
            errors.add("emptyBookmarkName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.bookmark_name")));
        }
        if (bookmark.getPath().isEmpty()) {
            errors.add("emptyBookmarkPath", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.bookmark_path")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }        
        BookmarkDao bookmarkDao = new BookmarkDao(requestContext);
        return bookmarkDao.add(bookmark);
    }

    public ActionMessages updateBookmark(Bookmark bookmark) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        // Check inputs
        if (bookmark.getName().isEmpty()) {
            errors.add("emptyBookmarkName", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.bookmark_name")));
        }
        if (bookmark.getPath().isEmpty()) {
            errors.add("emptyBookmarkPath", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.bookmark_path")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        BookmarkDao bookmarkDao = new BookmarkDao(requestContext);
        return bookmarkDao.update(bookmark);
    }

    public ActionMessages deleteBookmark(Bookmark bookmark) throws DatabaseException {
        BookmarkDao bookmarkDao = new BookmarkDao(requestContext);
        return bookmarkDao.delete(bookmark);
    }
}
