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
package com.kwoksys.biz.reports.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.reports.dto.SoftwareUsage;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.StringUtils;

/**
 * ReportDao class.
 */
public class ReportDao extends BaseDao {

    public ReportDao(RequestContext requestContext) {
        super(requestContext);
    }

    public List<SoftwareUsage> getSoftwareUsage(QueryCriteria query) throws DatabaseException {
        List<SoftwareUsage> list = new ArrayList<>();

        QueryHelper queryHelper = new QueryHelper(ReportQueries.selectSoftwareUsageQuery(query)) {
            @Override
            public void callback(ResultSet rs) throws Exception {
                SoftwareUsage softwareUsage = new SoftwareUsage();
                softwareUsage.setId(rs.getInt("software_id"));
                softwareUsage.setName(rs.getString("software_name"));
                softwareUsage.setDescription(StringUtils.replaceNull(rs.getString("software_description")));
                softwareUsage.setTypeName(StringUtils.replaceNull(rs.getString("software_type_name")));
                softwareUsage.setHardwareId(rs.getInt("hardware_id"));
                softwareUsage.setHardwareName(StringUtils.replaceNull(rs.getString("hardware_name")));
                softwareUsage.setHardwareOwnerId(rs.getInt("hardware_owner_id"));
                softwareUsage.setHardwareOwnerName(StringUtils.replaceNull(rs.getString("hardware_owner_display_name")));
                list.add(softwareUsage);
            }
        };

        executeQuery(queryHelper);

        return list;
    }

    public int getSoftwareUsageCount(QueryCriteria query) throws DatabaseException {
        return getRowCount(ReportQueries.selectSoftwareUsageCountQuery(query));
    }

    public List<Map<String, String>> getHardwareMembers(QueryCriteria searchQuery, QueryCriteria query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ReportQueries.selectHardwareMembersQuery(searchQuery, query));

        return executeQueryReturnList(queryHelper);
    }

    public int getHardwareMembersCount(QueryCriteria query) throws DatabaseException {
        return getRowCount(ReportQueries.selectHardwareMembersCountQuery(query));
    }

    public int getHardwareLicenseCount(QueryCriteria query) throws DatabaseException {
        return getRowCount(ReportQueries.getHardwareLicenseCountQuery(query));
    }

    public List<Map<String, String>> getHardwareLicenses(QueryCriteria query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ReportQueries.getHardwareLicensesQuery(query));

        return executeQueryReturnList(queryHelper);
    }
}