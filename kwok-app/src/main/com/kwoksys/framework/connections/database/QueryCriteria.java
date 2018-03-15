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
package com.kwoksys.framework.connections.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.framework.util.Callback;

/**
 * QueryCriteria
 */
public class QueryCriteria {

    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";

    private List<String> whereClauses = new ArrayList<>();

    private Map<String, String> sortColumns = new LinkedHashMap<>();

    private int rowOffset;
    private int rowLimit;

    private Callback callback;
    
    public QueryCriteria() {}
    
    public QueryCriteria(BaseSearch search) {
        search.applyMap(this);
    }

    public void setLimit(int rowLimit, int rowOffset) {
        this.rowLimit = rowLimit;
        this.rowOffset = rowOffset;
    }

    /**
     * Build where clauses.
     *
     * @param query
     */
    public void appendWhereClause(String query) {
        whereClauses.add(query);
    }

    public String createWhereClause() {
        return createQueryClause(" where ");
    }

    public String createAndClause() {
        return createQueryClause(" and ");
    }

    public String createClause() {
        return createQueryClause("");
    }

    private String createQueryClause(String operator) {
        StringBuilder query = new StringBuilder();

        if (!whereClauses.isEmpty() && !operator.isEmpty()) {
            query.append(operator);

            for (Iterator<String> iter = whereClauses.iterator(); iter.hasNext();) {
                query.append(iter.next());

                if (iter.hasNext()) {
                    query.append(" and ");
                }
            }
        }

        if (!sortColumns.isEmpty()) {
            query.append(" order by ");
            for (Iterator<Entry<String, String>> iter = sortColumns.entrySet().iterator(); iter.hasNext();) {
                Map.Entry<String, String> entry = iter.next();
                query.append(entry.getKey());
                if (entry.getValue().equals(DESCENDING)) {
                    query.append(" ").append(DESCENDING);
                }
                if (iter.hasNext()) {
                    query.append(", ");
                }
            }
        }
        if (rowLimit > 0) {
            query.append(" limit ").append(rowLimit);
            if (rowOffset > 0) {
                 query.append(" offset ").append(rowOffset);
            }
        }
        return query.toString();
    }

    public String createWhereCountClause() {
        return createQueryCountClause(" where ");
    }

    public String createAndCountClause() {
        return createQueryCountClause(" and ");
    }

    private String createQueryCountClause(String operator) {
        StringBuilder query = new StringBuilder();

        if (!whereClauses.isEmpty() && !operator.isEmpty()) {
            query.append(operator);

            for (Iterator<String> iter = whereClauses.iterator(); iter.hasNext();) {
                query.append(iter.next());

                if (iter.hasNext()) {
                    query.append(" and ");
                }
            }
        }
        return query.toString();
    }

    public void addSortColumn(String orderByColumn) {
        addSortColumn(orderByColumn, ASCENDING);
    }

    public void addSortColumn(String orderByColumn, String orderBy) {
        sortColumns.put(orderByColumn, orderBy);
    }
    
    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
