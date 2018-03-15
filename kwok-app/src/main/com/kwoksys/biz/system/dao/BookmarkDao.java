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
package com.kwoksys.biz.system.dao;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.system.dto.Bookmark;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.StringUtils;

/**
 * BookmarkDao.
 */
public class BookmarkDao extends BaseDao {

    public BookmarkDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * Return all Bookmarks for a particular object.
     */
    public List<Bookmark> getBookmarks(QueryCriteria query, Integer objectTypeId, Integer objectId) throws DatabaseException {
        List<Bookmark> bookmarks = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(BookmarkQueries.selectBookmarkListQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Bookmark bookmark = new Bookmark();
                bookmark.setId(rs.getInt("bookmark_id"));
                bookmark.setName(StringUtils.replaceNull(rs.getString("bookmark_name")));
                bookmark.setDescription(StringUtils.replaceNull(rs.getString("bookmark_description")));
                bookmark.setPath(StringUtils.replaceNull(rs.getString("bookmark_path")));
                bookmarks.add(bookmark);
            }
        };
        
        queryHelper.addInputInt(objectTypeId);
        queryHelper.addInputInt(objectId);

        executeQuery(queryHelper);
        
        return bookmarks;
    }

    /**
     * Return a Bookmark.
     */
    public Bookmark getBookmark(Integer objectTypeId, Integer objectId, Integer bookmarkId) throws DatabaseException,
            ObjectNotFoundException {

        List<Bookmark> bookmarks = new ArrayList<>();
        
        QueryHelper queryHelper = new QueryHelper(BookmarkQueries.selectBookmarkDetailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Bookmark bookmark = new Bookmark(objectTypeId, objectId);
                bookmark.setId(rs.getInt("bookmark_id"));
                bookmark.setName(StringUtils.replaceNull(rs.getString("bookmark_name")));
                bookmark.setDescription(StringUtils.replaceNull(rs.getString("bookmark_description")));
                bookmark.setPath(StringUtils.replaceNull(rs.getString("bookmark_path")));
                bookmarks.add(bookmark);
            }
        };
        
        queryHelper.addInputInt(objectTypeId);
        queryHelper.addInputInt(objectId);
        queryHelper.addInputInt(bookmarkId);

        executeSingleRecordQuery(queryHelper);
        
        if (!bookmarks.isEmpty()) {
            return bookmarks.get(0);
        } else {
            throw new ObjectNotFoundException("Bookmark ID: " + bookmarkId);
        }
    }

    /**
     * Add a Bookmark.
     *
     * @param bookmark
     * @return ..
     */
    public ActionMessages add(Bookmark bookmark) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(BookmarkQueries.insertBookmarkQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(bookmark.getName());
        queryHelper.addInputStringConvertNull(bookmark.getPath());
        queryHelper.addInputStringConvertNull(bookmark.getDescription());
        queryHelper.addInputInt(bookmark.getObjectTypeId());
        queryHelper.addInputInt(bookmark.getObjectId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Since this is a new company, we'll need to reset this.
        if (errors.isEmpty()) {
            bookmark.setId((Integer) queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    /**
     * Update a Bookmark.
     *
     * @param bookmark
     * @return ..
     */
    public ActionMessages update(Bookmark bookmark) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(BookmarkQueries.updateBookmarkQuery());
        queryHelper.addInputInt(bookmark.getId());
        queryHelper.addInputStringConvertNull(bookmark.getName());
        queryHelper.addInputStringConvertNull(bookmark.getPath());
        queryHelper.addInputStringConvertNull(bookmark.getDescription());
        queryHelper.addInputInt(bookmark.getObjectTypeId());
        queryHelper.addInputInt(bookmark.getObjectId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }

    /**
     * Delete a Bookmark.
     *
     * @param bookmark
     * @return ..
     */
    public ActionMessages delete(Bookmark bookmark) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(BookmarkQueries.deleteBookmarkQuery());
        queryHelper.addInputInt(bookmark.getObjectTypeId());
        queryHelper.addInputInt(bookmark.getObjectId());
        queryHelper.addInputInt(bookmark.getId());

        return executeProcedure(queryHelper);
    }
}