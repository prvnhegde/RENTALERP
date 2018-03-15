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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.common.template.ActionErrorsTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.action.common.template.TableTemplate;
import com.kwoksys.action.common.template.TabsTemplate;
import com.kwoksys.action.files.FileAddTemplate;
import com.kwoksys.action.files.FileDeleteTemplate;
import com.kwoksys.action.files.FileEditTemplate;
import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.core.CompanyTabs;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.CompanyFile;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.data.Counter;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.http.ResponseContext;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * Action class for managing Company files.
 */
public class CompanyFileAction extends Action2 {

    public String list() throws Exception {
        AccessUser user = requestContext.getUser();

        // Get request parameters
        Integer companyId = requestContext.getParameter("companyId");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.FILES_ORDER_BY, File.NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.FILES_ORDER, QueryCriteria.ASCENDING);

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        // These are for Company Files.
        boolean canDeleteFile = user.hasPermission(AppPaths.CONTACTS_COMPANY_FILE_DELETE);
        String fileDeletePath = AppPaths.CONTACTS_COMPANY_FILE_DELETE + "?companyId=" + companyId + "&fileId=";
        String fileEditPath = AppPaths.CONTACTS_COMPANY_FILE_EDIT + "?companyId=" + companyId + "&fileId=";

        boolean canDownloadFile = user.hasPermission(AppPaths.CONTACTS_COMPANY_FILE_DOWNLOAD);
        String fileDownloadPath = AppPaths.CONTACTS_COMPANY_FILE_DOWNLOAD + "?companyId=" + companyId + "&fileId=";

        QueryCriteria queryCriteria = new QueryCriteria();
        if (FileUtils.isSortableColumn(orderBy)) {
            queryCriteria.addSortColumn(FileQueries.getOrderByColumn(orderBy), order);
        }

        List<File> files = contactService.getCompanyFiles(queryCriteria, company.getId());

        List<String> columnHeaders = new ArrayList<>(FileUtils.getFileColumnHeaders());
        if (canDeleteFile) {
            columnHeaders.add("command");
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        // Link to add company file page
        FileService fileService = ServiceProvider.getFileService(requestContext);
        if (user.hasPermission(AppPaths.CONTACTS_COMPANY_FILE_ADD)) {
            Link link = new Link(requestContext);
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getCompanyFileRepositoryLocation())) {
                link.setAjaxPath(AppPaths.CONTACTS_COMPANY_FILE_ADD + "?companyId=" + companyId);
                link.setImgSrc(Image.getInstance().getFileAddIcon());
            } else {
                link.setImgAltKey("files.warning.invalidPath");
                link.setImgSrc(Image.getInstance().getSignWarning());
            }
            header.addHeaderCmds(link);
        }

        // Link to back to company list page
        if (user.hasPermission(AppPaths.CONTACTS_COMPANY_LIST)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST);
            link.setTitleKey("contactMgmt.cmd.companyList");
            header.addHeaderCmds(link);
        }

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(CompanyUtils.getCompanyTabs(requestContext, company));
        tabs.setTabActive(CompanyTabs.FILES_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableHeader = standardTemplate.addTemplate(new TableTemplate());
        tableHeader.setColumnPath(AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + companyId);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setColumnTextKey("files.colName.");
        tableHeader.setSortableColumnHeaders(FileUtils.getSortableColumns());
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setEmptyRowMsgKey("files.noAttachments");

        if (!files.isEmpty()) {
            Counter counter = new Counter();

            for (File file : files) {
                List<String> columns = new ArrayList<>();
                columns.add(counter.incr() + ".");
                // Show a download link
                columns.add(Links.getFileIconLink(requestContext, canDownloadFile, file.getLogicalName(),
                        fileDownloadPath + file.getId()).getString());
                columns.add(HtmlUtils.encode(file.getTitle()));
                columns.add(file.getCreationDate());
                columns.add(FileUtils.formatFileSize(requestContext, file.getSize()));

                if (canDeleteFile) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(new Link(requestContext).setAjaxPath(fileEditPath + file.getId()).setTitleKey("form.button.edit").getString());
                    sb.append(" | ");
                    sb.append(new Link(requestContext).setAjaxPath(fileDeletePath + file.getId()).setTitleKey("form.button.delete").getString());
                    
                    columns.add(sb.toString());
                }
                tableHeader.addRow(columns);
            }
        }

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("files.fileAdd");

        //
        // Template: FileAddTemplate
        //
        FileAddTemplate fileAdd = standardTemplate.addTemplate(new FileAddTemplate(getBaseForm(FileUploadForm.class)));
        fileAdd.setFileName(requestContext.getParameterString("fileName0"));
        fileAdd.setFormAction(AppPaths.CONTACTS_COMPANY_FILE_ADD_2 + "?companyId=" + companyId);
        fileAdd.setFormCancelAction(AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + companyId);
        fileAdd.getErrorsTemplate().setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add2() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        contactService.getCompany(companyId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        File file = new CompanyFile(companyId);

        FileUploadForm actionForm = saveActionForm(new FileUploadForm());
        ActionMessages errors = fileService.addFile(file, actionForm);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.CONTACTS_COMPANY_FILE_ADD + "?companyId=" + companyId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset Company File counter.
            contactService.resetCompanyFileCount(companyId);

            return redirect(AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + companyId);
        }
    }
    
    public String delete() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = contactService.getCompanyFile(companyId, fileId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", companyId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new Object[] {company.getName()});

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        //
        // Template: FileDeleteTemplate
        //
        FileDeleteTemplate fileDelete = standardTemplate.addTemplate(new FileDeleteTemplate());
        fileDelete.setFile(file);
        fileDelete.setFormAction(AppPaths.CONTACTS_COMPANY_FILE_DELETE_2 + "?companyId=" + companyId + "&fileId=" + file.getId());
        fileDelete.setFormCancelAction(AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + companyId);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String delete2() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        contactService.getCompany(companyId);

        // Instantiate File class
        Integer fileId = requestContext.getParameter("fileId");
        File file = contactService.getCompanyFile(companyId, fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete the file
        fileService.deleteFile(file);

        // Reset File count
        contactService.resetCompanyFileCount(companyId);

        return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + companyId);
    }

    public String download() throws Exception {
        ResponseContext responseContext = new ResponseContext(response);
        Integer companyId = requestContext.getParameter("companyId");

        try {
            ContactService contactService = ServiceProvider.getContactService(requestContext);
            contactService.getCompany(companyId);

            Integer fileId = requestContext.getParameter("fileId");
            File file = contactService.getCompanyFile(companyId, fileId);

            FileService fileService = ServiceProvider.getFileService(requestContext);
            fileService.download(responseContext, file);

        } catch (ObjectNotFoundException e) {
            throw new FileNotFoundException();
        }
        return null;
    }
    
    public String edit() throws Exception {
        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = contactService.getCompanyFile(companyId, fileId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("companyId", companyId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("contactMgmt.companyDetail.header", new String[] {company.getName()});

        //
        // Template: CompanySpecTemplate
        //
        standardTemplate.addTemplate(new CompanySpecTemplate(company));

        // Template: FileEditTemplate
        FileEditTemplate fileEdit = standardTemplate.addTemplate(new FileEditTemplate());
        fileEdit.setFile(file);
        fileEdit.setFormAction(AppPaths.CONTACTS_COMPANY_FILE_EDIT_2 + "?companyId=" + companyId + "&fileId=" + fileId);
        fileEdit.setFormCancelAction(AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + companyId);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = fileEdit.getErrorsTemplate();
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String edit2() throws Exception {
        FileUploadForm actionForm = saveActionForm(new FileUploadForm());
        
        Integer companyId = requestContext.getParameter("companyId");

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        Company company = contactService.getCompany(companyId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = contactService.getCompanyFile(companyId, fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Update the file
        ActionMessages errors = fileService.updateFile(file, actionForm);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_FILE_EDIT + "?companyId=" + company.getId() + "&fileId=" + fileId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.CONTACTS_COMPANY_FILES + "?companyId=" + companyId);
        }
    }
}
