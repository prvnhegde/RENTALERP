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
package com.kwoksys.biz;

import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.auth.AuthService;
import com.kwoksys.biz.blogs.BlogService;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.issues.IssueService;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.portal.PortalService;
import com.kwoksys.biz.reports.ReportService;
import com.kwoksys.biz.rss.RssService;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.system.BookmarkService;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.framework.http.RequestContext;

/**
 * ServiceAPI
 */
public class ServiceProvider {

    public static AdminService getAdminService(RequestContext requestContext) {
        return new AdminService(requestContext);
    }

    public static AuthService getAuthService(RequestContext requestContext) {
        return new AuthService(requestContext);
    }

    public static BlogService getBlogService(RequestContext requestContext) {
        return new BlogService(requestContext);
    }

    public static BookmarkService getBookmarkService(RequestContext requestContext) {
        return new BookmarkService(requestContext);
    }

    public static ContactService getContactService(RequestContext requestContext) {
        return new ContactService(requestContext);
    }

    public static ContractService getContractService(RequestContext requestContext) {
        return new ContractService(requestContext);
    }

    public static FileService getFileService(RequestContext requestContext) {
        return new FileService(requestContext);
    }

    public static HardwareService getHardwareService(RequestContext requestContext) {
        return new HardwareService(requestContext);
    }

    public static IssueService getIssueService(RequestContext requestContext) {
        return new IssueService(requestContext);
    }

    public static KbService getKbService(RequestContext requestContext) {
        return new KbService(requestContext);
    }

    public static PortalService getPortalService(RequestContext requestContext) {
        return new PortalService(requestContext);
    }

    public static ReportService getReportService(RequestContext requestContext) {
        return new ReportService(requestContext);
    }

    public static RssService getRssService(RequestContext requestContext) {
        return new RssService(requestContext);
    }

    public static SoftwareService getSoftwareService(RequestContext requestContext) {
        return new SoftwareService(requestContext);
    }

    public static SystemService getSystemService(RequestContext requestContext) {
        return new SystemService(requestContext);
    }
}
