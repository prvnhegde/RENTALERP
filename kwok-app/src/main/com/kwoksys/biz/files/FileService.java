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
package com.kwoksys.biz.files;

import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.files.dao.FileDao;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.http.ResponseContext;
import org.apache.commons.io.FileUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts2.dispatcher.multipart.UploadedFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FileService
 */
public class FileService {

    private static final Logger LOGGER = Logger.getLogger(FileService.class.getName());

    private RequestContext requestContext;

    public FileService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public boolean isDirectoryExist(String directoryPath) {
        java.io.File file = new java.io.File(directoryPath);
        return file.isDirectory();
    }

    public boolean isFileExist(String filePath) {
        java.io.File file = new java.io.File(filePath);
        return file.isFile();
    }

    public List<File> getFiles(QueryCriteria query, Integer objectTypeId, Integer objectId) throws DatabaseException {
        FileDao fileDao = new FileDao(requestContext);
        return fileDao.getList(query, objectTypeId, objectId);
    }

    public File getFile(Integer objectTypeId, Integer objectId, Integer fileId) throws DatabaseException,
            ObjectNotFoundException {
        FileDao fileDao = new FileDao(requestContext);
        return fileDao.getFile(objectTypeId, objectId, fileId);
    }

    /**
     * Will be moved to FileService class
     * @param actionForm
     * @return
     * @throws DatabaseException
     */
    private ActionMessages upload(File file, FileUploadForm uploadForm) throws DatabaseException {
        UploadedFile[] files = uploadForm.getFiles();
        String[] fileNames = uploadForm.getFileNames();
        String[] mineTypes = uploadForm.getMineTypes();

        ActionMessages errors = new ActionMessages();

        if (files == null) {
            errors.add("fileUpload", new ActionMessage("files.error.emptyFilePath"));
            return errors;
        }

        // Set file detail.
        file.setLogicalName(fileNames[0]);
        file.setMimeType(mineTypes[0]);
        file.setSize(new Long(files[0].length()).intValue());
        file.setAttachedFile(new java.io.File(files[0].getAbsolutePath()));

        if (file.getLogicalName().isEmpty()) {
            errors.add("emptyFilePath", new ActionMessage("files.error.emptyFilePath"));
            return errors;
        }
        if (file.getSize() > ConfigManager.file.getMaxFileUploadSize()
                || file.getSize() < 0) {
            errors.add("fileMaxSize", new ActionMessage("files.error.fileMaxSize"));
            return errors;
        }

        FileDao fileDao = new FileDao(requestContext);

        try {
            // Create a record in the database.
            errors = fileDao.add(file);

            if (!errors.isEmpty()) {
                return errors;
            }

            java.io.File uploadFile = new java.io.File(file.getConfigRepositoryPath(), file.getConfigUploadedFilePrefix() + file.getId());

            // Check to see if file already exists
            if (uploadFile.exists()) {
                throw new Exception("File already exists");
            }

            // Move file from temp upload to storage.
            FileUtils.moveFile(file.getAttachedFile(), uploadFile);

        } catch (Throwable t) {
            // Upload failed, do some clean up.
            fileDao.deleteNew(file.getId());

            errors.add("fileUpload", new ActionMessage("files.error.fileUpload"));
            LOGGER.log(Level.SEVERE, "Problem writing file to repository. " +
                    "Original file name: " + file.getLogicalName()
                    + ". Physical file name: " +  file.getConfigUploadedFilePrefix() + file.getId()
                    + ". File repository: " + file.getConfigRepositoryPath(), t);
        }
        return errors;
    }

    /**
     * Will be moved to FileService class
     * @param request
     * @param actionForm
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addFile(File file, FileUploadForm actionForm) throws DatabaseException {
        // Upload the actual file.
        ActionMessages errors = upload(file, actionForm);
        if (!errors.isEmpty()) {
            return errors;
        }

        // Put together the uploaded file name.
        file.setTitle(actionForm.getFileName0());
        file.setFileuploadedFileName(file.getConfigUploadedFilePrefix() + file.getId());

        // Update file record in the FILES table.
        FileDao fileDao = new FileDao(requestContext);
        errors = fileDao.update(file);
        if (!errors.isEmpty()) {
            return errors;
        }
        return errors;
    }

    public ActionMessages updateFile(File file, FileUploadForm actionForm) throws DatabaseException {
        // Update file record in the FILES table.
        FileDao fileDao = new FileDao(requestContext);
        
        file.setTitle(actionForm.getFileName0());
        
        ActionMessages errors = fileDao.updateTitle(file);
        if (!errors.isEmpty()) {
            return errors;
        }

        return errors;
    }

    /**
     * Instead of deleting the file, we'll rename it.
     * @param repositoryPath
     * @param uploadedFileName
     */
    private void deletePhysicalFile(String repositoryPath, String uploadedFileName) {
        java.io.File delfile = new java.io.File(repositoryPath, uploadedFileName);
        delfile.renameTo(new java.io.File(repositoryPath, ConfigManager.file.getDeleteFilePrefix() + uploadedFileName));
    }

    /**
     * First gather objectTypeId, objectId, fileId.
     * Query the database to see if there is such record.
     * If it cannot find any record, bail out and don't do anything, cuz I really don't know what to do.
     * If it can find a record, great, delete the database record first.
     * Then, delete the actual file according to the file repository path, and uploaded file name.
     *
     * @return ..
     */
    public ActionMessages deleteFile(File file) throws DatabaseException {
        // Update file record in the FILES table.
        FileDao fileDao = new FileDao(requestContext);
        ActionMessages errors = fileDao.delete(file);
        if (!errors.isEmpty()) {
            return errors;
        }

        deletePhysicalFile(file.getConfigRepositoryPath(), file.getPhysicalName());
        return errors;
    }

    /**
     * This would accept a list of files to delete.
     */
    public void bulkDelete(String configRepositoryPath, List<File> fileList) {
        // Loop through the fileList to do deletion.
        for (File file : fileList) {
            // Debug
            //System.out.println("debug: deleting " + configRepositoryPath + file.getPhysicalName());
            deletePhysicalFile(configRepositoryPath, file.getPhysicalName());
        }
    }

    public void download(ResponseContext responseContext, File file) throws FileNotFoundException {
        HttpServletResponse response = responseContext.getResponse();

        response.setContentType(file.getMimeType());
        response.setContentLength(file.getSize());

        // To work around file download problems on IE
        response.setHeader("Pragma", "public"); // For HTTP/1.0 backward compatibility.
        response.setHeader("Cache-Control", "public"); // For HTTP/1.1.
        
        response.setHeader("Content-Disposition", "filename=\"" + file.getLogicalName() + "\"");

        // Get the file in file input stream.
        java.io.File downloadFile = new java.io.File(file.getConfigRepositoryPath(), file.getPhysicalName());

        FileInputStream input = null;
        ServletOutputStream output = null;

        try {
            input = new FileInputStream(downloadFile);
            output = response.getOutputStream();

            byte[] buffer = new byte[10240];
            int read;
            while ((read = input.read(buffer)) > 0) {
                output.write(buffer, 0, read);
            }
        } catch (Exception e) {
            LOGGER.warning("Problem downloading a file: " + e.getMessage());
            throw new FileNotFoundException();

        } finally {
            close(input);
            close(output);
        }
    }

    private static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignore) {/* ignored */}
        }
    }
}
