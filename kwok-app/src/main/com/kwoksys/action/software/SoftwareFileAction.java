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
package com.kwoksys.action.software;

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
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.core.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.SoftwareUtils;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.software.dto.SoftwareFile;
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
 * Action class for managing software files.
 */
public class SoftwareFileAction extends Action2 {

    public String list() throws Exception {
        AccessUser accessUser = requestContext.getUser();

        // Get request parameters
        Integer softwareId= requestContext.getParameter("softwareId");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.FILES_ORDER_BY, File.NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.FILES_ORDER, QueryCriteria.ASCENDING);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        String fileDownloadPath = AppPaths.SOFTWARE_FILE_DOWNLOAD + "?softwareId=" + softwareId + "&fileId=";
        String fileDeletePath = AppPaths.SOFTWARE_FILE_DELETE + "?softwareId=" + softwareId + "&fileId=";
        String fileEditPath = AppPaths.SOFTWARE_FILE_EDIT + "?softwareId=" + softwareId + "&fileId=";

        // These are for Software Files.
        boolean canDeleteFile = accessUser.hasPermission(AppPaths.SOFTWARE_FILE_DELETE);
        boolean canDownloadFile = accessUser.hasPermission(AppPaths.SOFTWARE_FILE_DOWNLOAD);

        QueryCriteria query = new QueryCriteria();
        if (FileUtils.isSortableColumn(orderBy)) {
            query.addSortColumn(FileQueries.getOrderByColumn(orderBy), order);
        }

        List<File> files = softwareService.getSoftwareFiles(query, softwareId);

        List<String> columnHeaders = new ArrayList<>(FileUtils.getFileColumnHeaders());
        if (canDeleteFile) {
            columnHeaders.add("command");
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        SoftwareUtils.addSoftwareHeaderCommands(requestContext, header, softwareId);
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        // Add Software File.
        FileService fileService = ServiceProvider.getFileService(requestContext);
        if (accessUser.hasPermission(AppPaths.SOFTWARE_FILE_ADD)) {
            Link link = new Link(requestContext);
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getSoftwareFileRepositoryLocation())) {
                link.setAjaxPath(AppPaths.SOFTWARE_FILE_ADD + "?softwareId=" + softwareId);
                link.setImgSrc(Image.getInstance().getFileAddIcon());
            } else {
                link.setImgAltKey("files.warning.invalidPath");
                link.setImgSrc(Image.getInstance().getSignWarning());
            }
            header.addHeaderCmds(link);
        }

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        //
        // Template: ActionErrorsTemplate
        //
        standardTemplate.addTemplate(new ActionErrorsTemplate());

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(SoftwareUtils.getSoftwareTabs(requestContext, software));
        tabs.setTabActive(SoftwareUtils.FILES_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableTemplate = standardTemplate.addTemplate(new TableTemplate());
        tableTemplate.setColumnPath(AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId);
        tableTemplate.setColumnHeaders(columnHeaders);
        tableTemplate.setColumnTextKey("files.colName.");
        tableTemplate.setSortableColumnHeaders(FileUtils.getSortableColumns());
        tableTemplate.setOrderBy(orderBy);
        tableTemplate.setOrder(order);
        tableTemplate.setEmptyRowMsgKey("files.noAttachments");

        if (!files.isEmpty()) {
            Counter counter = new Counter();

            for (File file : files) {
                List<String> columns = new ArrayList<>();
                columns.add(counter.incr() + ".");
                // Show a download link when the user is allowed to download the file.
                columns.add(Links.getFileIconLink(requestContext, canDownloadFile, file.getLogicalName(), fileDownloadPath + file.getId()).getString());
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
                tableTemplate.addRow(columns);
            }
        }

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    /**
     * Adds software file 
     * @return
     * @throws Exception
     */
    public String add() throws Exception {
        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

        Integer softwareId= requestContext.getParameter("softwareId");
        Software software = softwareService.getSoftware(softwareId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[] {software.getName()});

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        //
        // Template: FileAddTemplate
        //
        FileAddTemplate fileAdd = standardTemplate.addTemplate(new FileAddTemplate(getBaseForm(FileUploadForm.class)));
        fileAdd.setFileName(requestContext.getParameterString("fileName0"));
        fileAdd.setFormAction(AppPaths.SOFTWARE_FILE_ADD_2 + "?softwareId=" + software.getId());
        fileAdd.setFormCancelAction(AppPaths.SOFTWARE_FILE + "?softwareId=" + software.getId());
        fileAdd.getErrorsTemplate().setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    public String add2() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        softwareService.getSoftware(softwareId);

        File file = new SoftwareFile(softwareId);

        FileService fileService = ServiceProvider.getFileService(requestContext);
        ActionMessages errors = fileService.addFile(file, saveActionForm(new FileUploadForm()));

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.SOFTWARE_FILE_ADD + "?softwareId=" + softwareId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset Software File counter.
            softwareService.resetSoftwareFileCount(softwareId);

            return redirect(AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId);
        }
    }
    
    public String delete() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = softwareService.getSoftwareFile(softwareId, fileId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("softwareId", softwareId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.softwareDetail.header", new Object[]{software.getName()});

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        //
        // Template: FileDeleteTemplate
        //
        FileDeleteTemplate fileDelete = standardTemplate.addTemplate(new FileDeleteTemplate());
        fileDelete.setFile(file);
        fileDelete.setFormAction(AppPaths.SOFTWARE_FILE_DELETE_2 + "?softwareId=" + softwareId + "&fileId=" + fileId);
        fileDelete.setFormCancelAction(AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String delete2() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        softwareService.getSoftware(softwareId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = softwareService.getSoftwareFile(softwareId, fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete the file
        ActionMessages errors = fileService.deleteFile(file);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);
        } else {
            // Reset Software File count
            softwareService.resetSoftwareFileCount(softwareId);

            return ajaxUpdateView(AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId);
        }
    }

    /**
     * Download software file.
     * @return
     * @throws Exception
     */
    public String download() throws Exception {
        ResponseContext responseContext = new ResponseContext(response);

        try {
            SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);

            // Get request parameters
            Integer softwareId= requestContext.getParameter("softwareId");
            softwareService.getSoftware(softwareId);

            Integer fileId = requestContext.getParameter("fileId");
            File file = softwareService.getSoftwareFile(softwareId, fileId);

            FileService fileService = ServiceProvider.getFileService(requestContext);
            fileService.download(responseContext, file);

        } catch (ObjectNotFoundException e) {
            throw new FileNotFoundException();
        }
        return null;
    }
    
    public String edit() throws Exception {
        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = softwareService.getSoftwareFile(softwareId, fileId);
       
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.softwareDetail.header", new String[] {software.getName()});

        //
        // Template: SoftwareSpecTemplate
        //
        standardTemplate.addTemplate(new SoftwareSpecTemplate(software));

        // Template: FileEditTemplate
        FileEditTemplate fileEdit = standardTemplate.addTemplate(new FileEditTemplate());
        fileEdit.setFile(file);
        fileEdit.setFormAction(AppPaths.SOFTWARE_FILE_EDIT_2 + "?softwareId=" + softwareId + "&fileId=" + fileId);
        fileEdit.setFormCancelAction(AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = fileEdit.getErrorsTemplate();
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String edit2() throws Exception {
        FileUploadForm actionForm = saveActionForm(new FileUploadForm());
        
        Integer softwareId = requestContext.getParameter("softwareId");

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        Software software = softwareService.getSoftware(softwareId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = softwareService.getSoftwareFile(software.getId(), fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Update the file
        ActionMessages errors = fileService.updateFile(file, actionForm);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.SOFTWARE_FILE_EDIT + "?softwareId=" + software.getId() + "&fileId=" + fileId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.SOFTWARE_FILE + "?softwareId=" + softwareId);
        }
    }    
}
