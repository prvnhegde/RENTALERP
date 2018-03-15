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
package com.kwoksys.biz.reports;

import com.kwoksys.biz.reports.dao.ReportDao;
import com.kwoksys.biz.reports.dto.SoftwareUsage;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;

import java.util.List;
import java.util.Map;

/**
 * ReportService.
 */
public class ReportService {

    private RequestContext requestContext;

    public ReportService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public List<SoftwareUsage> getSoftwareUsage(QueryCriteria query) throws DatabaseException {
        ReportDao reportDao = new ReportDao(requestContext);
        return reportDao.getSoftwareUsage(query);
    }

    public int getSoftwareUsageCount(QueryCriteria query) throws DatabaseException {
        ReportDao reportDao = new ReportDao(requestContext);
        return reportDao.getSoftwareUsageCount(query);
    }

    public List<Map<String, String>> getHardwareMembers(QueryCriteria searchQuery, QueryCriteria query) throws DatabaseException {
        ReportDao reportDao = new ReportDao(requestContext);
        return reportDao.getHardwareMembers(searchQuery, query);
    }

    public int getHardwareMembersCount(QueryCriteria query) throws DatabaseException {
        ReportDao reportDao = new ReportDao(requestContext);
        return reportDao.getHardwareMembersCount(query);
    }

    public int getHardwareLicenseCount(QueryCriteria query) throws DatabaseException {
        return new ReportDao(requestContext).getHardwareLicenseCount(query);
    }

    public List<Map<String, String>> getHardwareLicenses(QueryCriteria query) throws DatabaseException {
        return new ReportDao(requestContext).getHardwareLicenses(query);
    }
}