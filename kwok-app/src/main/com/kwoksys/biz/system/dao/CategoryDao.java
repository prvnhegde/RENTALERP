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
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.StringUtils;

/**
 * CategoryDao
 */
public class CategoryDao extends BaseDao {

    public CategoryDao(RequestContext requestContext) {
        super(requestContext);
    }

    /**
     * This is to return a list of categories.
     *
     * @param query
     * @return ..
     */
    public List<Category> getCategoryList(QueryCriteria query, Integer objectTypeId) throws DatabaseException {
        List<Category> categories = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(CategoryQueries.selectCategoryListQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Category category = new Category();
                category.setId(rs.getInt("category_id"));
                category.setName(StringUtils.replaceNull(rs.getString("category_name")));
                category.setCountObjects(rs.getInt("object_count"));
                category.setObjectTypeId(rs.getInt("object_type_id"));

                categories.add(category);
            }
        };
        
        queryHelper.addInputInt(objectTypeId);
        
        executeQuery(queryHelper);
        
        return categories;
    }

    /**
     * Get a category.
     *
     * @return ..
     */
    public Category getCategory(Integer categoryId, Integer objectTypeId) throws DatabaseException, ObjectNotFoundException {
        List<Category> categories = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(CategoryQueries.selectCategoryDetailQuery()) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                Category category = new Category();
                category.setId(rs.getInt("category_id"));
                category.setName(StringUtils.replaceNull(rs.getString("category_name")));
                category.setDescription(StringUtils.replaceNull(rs.getString("category_description")));
                category.setObjectTypeId(rs.getInt("object_type_id"));
                
                categories.add(category);
            }
        };
        
        queryHelper.addInputInt(categoryId);
        queryHelper.addInputInt(objectTypeId);

        executeSingleRecordQuery(queryHelper);

        if (!categories.isEmpty()) {
            return categories.get(0);
        } else {
            throw new ObjectNotFoundException("Category ID: " + categoryId);
        }
    }

    /**
     * Add a new category.
     *
     * @return ..
     */
    public ActionMessages addCategory(Category category) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(CategoryQueries.insertCategoryQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(category.getName());
        queryHelper.addInputStringConvertNull(category.getDescription());
        queryHelper.addInputInt(category.getObjectTypeId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        executeProcedure(queryHelper);

        // Put some values in the result
        if (errors.isEmpty()) {
            category.setId((Integer)queryHelper.getSqlOutputs().get(0));
        }

        return errors;
    }

    /**
     * Edits a category.
     *
     * @return ..
     */
    public ActionMessages editCategory(Category category) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(CategoryQueries.updateCategoryQuery());
        queryHelper.addInputInt(category.getId());
        queryHelper.addInputStringConvertNull(category.getName());
        queryHelper.addInputStringConvertNull(category.getDescription());
        queryHelper.addInputInt(category.getObjectTypeId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        return executeProcedure(queryHelper);
    }
}
