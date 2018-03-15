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
package com.kwoksys.action.hardware;

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
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.core.HardwareUtils;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareFile;
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
 * Action class for managing hardware files.
 */
public class HardwareFileAction extends Action2 {

    /**
     * Lists hardware files.
     */
    public String list() throws Exception {
        AccessUser user = requestContext.getUser();

        // Get request parameters
        Integer hardwareId = requestContext.getParameter("hardwareId");
        String orderBy = SessionManager.getOrSetAttribute(requestContext, "orderBy", SessionManager.FILES_ORDER_BY, File.NAME);
        String order = SessionManager.getOrSetAttribute(requestContext, "order", SessionManager.FILES_ORDER, QueryCriteria.ASCENDING);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        String fileDownloadPath = AppPaths.HARDWARE_FILE_DOWNLOAD + "?hardwareId=" + hardwareId + "&fileId=" ;
        String fileDeletePath = AppPaths.HARDWARE_FILE_DELETE + "?hardwareId=" + hardwareId + "&fileId=";
        String fileEditPath = AppPaths.HARDWARE_FILE_EDIT + "?hardwareId=" + hardwareId + "&fileId=";

        // These are for Hardware Files.
        boolean canDeleteFile = user.hasPermission(AppPaths.HARDWARE_FILE_DELETE);
        boolean canDownloadFile = user.hasPermission(AppPaths.HARDWARE_FILE_DOWNLOAD);

        QueryCriteria queryCriteria = new QueryCriteria();
        if (FileUtils.isSortableColumn(orderBy)) {
            queryCriteria.addSortColumn(FileQueries.getOrderByColumn(orderBy), order);
        }

        List<File> fileList = hardwareService.getHardwareFiles(queryCriteria, hardware.getId());

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
        HeaderTemplate headerTemplate = standardTemplate.getHeaderTemplate();
        headerTemplate.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});
        HardwareUtils.addHardwareHeaderCommands(requestContext, headerTemplate, hardwareId);

        // Add Hardware File.
        FileService fileService = ServiceProvider.getFileService(requestContext);
        if (user.hasPermission(AppPaths.HARDWARE_FILE_ADD)) {
            Link link = new Link(requestContext);
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getHardwareFileRepositoryLocation())) {
                link.setAjaxPath(AppPaths.HARDWARE_FILE_ADD + "?hardwareId=" + hardwareId);
                link.setImgSrc(Image.getInstance().getFileAddIcon());
            } else {
                link.setImgAltKey("files.warning.invalidPath");
                link.setImgSrc(Image.getInstance().getSignWarning());
            }
            headerTemplate.addHeaderCmds(link);
        }

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: TabsTemplate
        //
        TabsTemplate tabs = standardTemplate.addTemplate(new TabsTemplate());
        tabs.setTabList(HardwareUtils.hardwareTabList(hardware, requestContext));
        tabs.setTabActive(HardwareUtils.HARDWARE_FILE_TAB);

        //
        // Template: TableTemplate
        //
        TableTemplate tableHeader = standardTemplate.addTemplate(new TableTemplate());
        tableHeader.setColumnPath(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardwareId);
        tableHeader.setColumnHeaders(columnHeaders);
        tableHeader.setColumnTextKey("files.colName.");
        tableHeader.setSortableColumnHeaders(FileUtils.getSortableColumns());
        tableHeader.setOrderBy(orderBy);
        tableHeader.setOrder(order);
        tableHeader.setEmptyRowMsgKey("files.noAttachments");

        if (!fileList.isEmpty()) {
            Counter counter = new Counter();

            for (File file : fileList) {
                List<String> columns = new ArrayList<>();
                columns.add(counter.incr() + ".");
                // Show a download link
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
                tableHeader.addRow(columns);
            }
        }

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    /**
     * Adds hardware file.
     * @return
     * @throws Exception
     */
    public String add() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        FileUploadForm fileUploadForm = getBaseForm(FileUploadForm.class);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        // Template: FileAddTemplate
        FileAddTemplate fileAdd = standardTemplate.addTemplate(new FileAddTemplate(fileUploadForm));
        fileAdd.setFileName(fileUploadForm.getFileName0());
        fileAdd.setFormAction(AppPaths.HARDWARE_FILE_ADD_2 + "?hardwareId=" + hardwareId);
        fileAdd.setFormCancelAction(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardwareId);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = fileAdd.getErrorsTemplate();
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }
    
    /**
     * Adds hardware file.
     * @return
     * @throws Exception
     */
    public String add2() throws Exception {
        FileUploadForm actionForm = saveActionForm(new FileUploadForm());

        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        File file = new HardwareFile(hardwareId);

        FileService fileService = ServiceProvider.getFileService(requestContext);
        ActionMessages errors = fileService.addFile(file, actionForm);

        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return redirect(AppPaths.HARDWARE_FILE_ADD + "?hardwareId=" + hardware.getId() + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset Hardware File counter.
            hardwareService.resetHardwareFileCount(hardware.getId());

            return redirect(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardware.getId());
        }
    }

    public String edit() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = hardwareService.getHardwareFile(hardwareId, fileId);
       
        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        // Template: FileEditTemplate
        FileEditTemplate fileEdit = standardTemplate.addTemplate(new FileEditTemplate());
        fileEdit.setFile(file);
        fileEdit.setFormAction(AppPaths.HARDWARE_FILE_EDIT_2 + "?hardwareId=" + hardwareId + "&fileId=" + fileId);
        fileEdit.setFormCancelAction(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardwareId);

        //
        // Template: ActionErrorsTemplate
        //
        ActionErrorsTemplate errorsTemplate = fileEdit.getErrorsTemplate();
        errorsTemplate.setShowRequiredFieldMsg(true);

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String edit2() throws Exception {
        FileUploadForm actionForm = saveActionForm(new FileUploadForm());
        
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = hardwareService.getHardwareFile(hardware.getId(), fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Update the file
        ActionMessages errors = fileService.updateFile(file, actionForm);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_FILE_EDIT + "?hardwareId=" + hardware.getId() + "&fileId=" + fileId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            return ajaxUpdateView(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardwareId);
        }
    }

    /**
     * Deletes hardware file.
     * @return
     * @throws Exception
     */
    public String delete() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        Integer fileId = requestContext.getParameter("fileId");
        File file = hardwareService.getHardwareFile(hardwareId, fileId);

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("hardwareId", hardwareId);

        //
        // Template: HardwareSpecTemplate
        //
        standardTemplate.addTemplate(new HardwareSpecTemplate(hardware));

        //
        // Template: FileDeleteTemplate
        //
        FileDeleteTemplate fileDelete = standardTemplate.addTemplate(new FileDeleteTemplate());
        fileDelete.setFile(file);
        fileDelete.setFormAction(AppPaths.HARDWARE_FILE_DELETE_2 + "?hardwareId=" + hardwareId + "&fileId=" + file.getId());
        fileDelete.setFormCancelAction(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardwareId);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setPageTitleKey("itMgmt.hardwareDetail.header", new Object[] {hardware.getName()});

        return standardTemplate.findTemplate(STANDARD_AUTOGEN_TEMPLATE);
    }

    public String delete2() throws Exception {
        Integer hardwareId = requestContext.getParameter("hardwareId");

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        Hardware hardware = hardwareService.getHardware(hardwareId);

        // Instantiate File.
        Integer fileId = requestContext.getParameter("fileId");
        File file = hardwareService.getHardwareFile(hardware.getId(), fileId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete the file
        ActionMessages errors = fileService.deleteFile(file);
        if (!errors.isEmpty()) {
            saveActionErrors(errors);
            return ajaxUpdateView(AppPaths.HARDWARE_FILE_DELETE + "?hardwareId=" + hardware.getId() + "&fileId=" + fileId + "&" + RequestContext.URL_PARAM_ERROR_TRUE);

        } else {
            // Reset Hardware File count.
            hardwareService.resetHardwareFileCount(hardwareId);
            return ajaxUpdateView(AppPaths.HARDWARE_FILE + "?hardwareId=" + hardwareId);
        }
    }

    /**
     * Downloads hardware file.
     */
    public String download() throws Exception {
        ResponseContext responseContext = new ResponseContext(response);

        Integer hardwareId = requestContext.getParameter("hardwareId");

        try {
            HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
            hardwareService.getHardware(hardwareId);

            Integer fileId = requestContext.getParameter("fileId");
            File file = hardwareService.getHardwareFile(hardwareId, fileId);

            FileService fileService = ServiceProvider.getFileService(requestContext);
            fileService.download(responseContext, file);

        } catch (ObjectNotFoundException e) {
            throw new FileNotFoundException();
        }
        return null;
    }
    
}