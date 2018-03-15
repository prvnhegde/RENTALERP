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
package com.kwoksys.biz.issues;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.action.files.FileUploadForm;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.core.UserSearch;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.Permissions;
import com.kwoksys.biz.contacts.dao.CompanyDao;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.hardware.HardwareService;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.issues.core.IssueSearch;
import com.kwoksys.biz.issues.core.IssueUtils;
import com.kwoksys.biz.issues.dao.IssueDao;
import com.kwoksys.biz.issues.dto.Issue;
import com.kwoksys.biz.issues.dto.IssueFile;
import com.kwoksys.biz.software.SoftwareService;
import com.kwoksys.biz.software.dto.Software;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Keywords;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.LogConfigManager;
import com.kwoksys.biz.system.dto.linking.CompanyIssueLink;
import com.kwoksys.biz.system.dto.linking.HardwareIssueLink;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.biz.system.dto.linking.SoftwareIssueLink;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.connections.mail.EmailMessage;
import com.kwoksys.framework.connections.mail.Pop3;
import com.kwoksys.framework.connections.mail.PopConnection;
import com.kwoksys.framework.connections.mail.SmtpService;
import com.kwoksys.framework.exceptions.AccessDeniedException;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.parsers.email.IssueEmailParser;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.validations.ColumnField;
import com.kwoksys.framework.validations.InputValidator;

/**
 * Issue Service
 */
public class IssueService {

    private static final Logger LOGGER = Logger.getLogger(IssueService.class.getName());

    private RequestContext requestContext;

    public IssueService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public Map<String, String> getGroupByRecentCreatedIssues(RequestContext requestContext, QueryCriteria query, int numDays) 
            throws DatabaseException {
        
        double offsetHours = DatetimeUtils.getTimeOffsetHours(requestContext.getSysdate());

        Map<String, String> recentlyCreatedIssues = new IssueDao(requestContext).getGroupByRecentCreatedIssues(query, numDays, offsetHours);

        Map<String, String> resultMap = new LinkedHashMap<>();
        Calendar cal = DatetimeUtils.newLocalCalendar(requestContext);
        cal.add(Calendar.DATE, -numDays);
        
        for (int i = 0; i < numDays; i++) {
            cal.add(Calendar.DATE, 1);
            String dateString = DatetimeUtils.toShortDate(cal.getTime());
            resultMap.put(dateString, recentlyCreatedIssues.containsKey(dateString) ? recentlyCreatedIssues.get(dateString) : "0");
        }
        
        return resultMap;
    }

    public List<AttributeFieldCount> getGoupByStatusCount(QueryCriteria queryCriteria) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getGroupByStatusCount(queryCriteria);
    }

    public List<AttributeFieldCount> getGoupByPriorityCount(QueryCriteria queryCriteria) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getGroupByPriorityCount(queryCriteria);
    }

    public List<AttributeFieldCount> getGoupByTypeCount(QueryCriteria queryCriteria) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getGroupByTypeCount(queryCriteria);
    }

    public List<Map<String, String>> getGoupByAssigneeCount(QueryCriteria queryCriteria) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getGroupByAssigneeCount(queryCriteria);
    }

    public List<Issue> getIssues(QueryCriteria queryCriteria) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getIssues(queryCriteria, null);
    }

    public List<Issue> getLinkedIssues(QueryCriteria query, ObjectLink objectMap) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getLinkedIssueList(query, objectMap);
    }

    public Set<Integer> getIssueIds(QueryCriteria queryCriteria) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getIssueIds(queryCriteria);
    }

    public int getCount(QueryCriteria queryCriteria) throws DatabaseException {
        return new IssueDao(requestContext).getCount(queryCriteria);
    }

    public Issue getPublicIssue(Integer issueId) throws DatabaseException, ObjectNotFoundException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getIssue(issueId);
    }

    /**
     * Returns a given issue with advanced permission check. This is more expensive than getPublicIssue().
     * @param issueId
     * @return
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     * @throws com.kwoksys.framework.exceptions.AccessDeniedException
     */
    public Issue getIssue(Integer issueId) throws DatabaseException, ObjectNotFoundException, AccessDeniedException {
        IssueDao issueDao = new IssueDao(requestContext);

        AccessUser accessUser = requestContext.getUser();

        if (!accessUser.hasPermission(Permissions.ISSUE_READ_PERMISSION)) {
            IssueSearch issueSearch = new IssueSearch();

            // For access control
            issueSearch.put(IssueSearch.ISSUE_PERMITTED_USER_ID, accessUser.getId());
            issueSearch.put(IssueSearch.ISSUE_ID_EQUALS, issueId);

            int count = getCount(new QueryCriteria(issueSearch));
            if (count < 1) {
                throw new AccessDeniedException();
            }
        }
        return issueDao.getIssue(issueId);
    }

    public List<AccessUser> getAvailableSubscribers(Integer issueId) throws DatabaseException {
        // Get a list of available subscribers.
        UserSearch userSearch = new UserSearch();
        userSearch.put(UserSearch.USER_STATUS, AttributeFieldIds.USER_STATUS_ENABLED);
        userSearch.put("issueAvailableSubscribers", issueId);

        QueryCriteria queryCriteria = new QueryCriteria(userSearch);
        queryCriteria.addSortColumn(AdminQueries.getOrderByColumn(AdminUtils.getUsernameSort()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        return adminService.getUsers(queryCriteria);
    }

    public List<AccessUser> getSelectedSubscribers(Integer issueId) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getSelectedSubscribers(issueId);
    }

    public List<Map<String, String>> getHistory(QueryCriteria queryCriteria, Integer issueId) throws DatabaseException {
        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.getHistory(queryCriteria, issueId);
    }

    public List<Hardware> getIssueHardwareList(QueryCriteria queryCriteria, Integer issueId) throws DatabaseException {
        HardwareIssueLink map = new HardwareIssueLink();
        map.setIssueId(issueId);

        HardwareService hardwareService = ServiceProvider.getHardwareService(requestContext);
        return hardwareService.getLinkedHardwareList(queryCriteria, map.createObjectMap());
    }

    public List<Software> getIssueSoftwareList(QueryCriteria queryCriteria, Integer issueId) throws DatabaseException {
        SoftwareIssueLink link = new SoftwareIssueLink();
        link.setIssueId(issueId);

        SoftwareService softwareService = ServiceProvider.getSoftwareService(requestContext);
        return softwareService.getLinkedSoftwareList(queryCriteria, link.createObjectMap());
    }

    public List<Company> getIssueCompanyList(QueryCriteria queryCriteria, Integer issueId) throws DatabaseException {
        CompanyIssueLink map = new CompanyIssueLink();
        map.setIssueId(issueId);
        CompanyDao companyDao = new CompanyDao(requestContext);
        return companyDao.getLinkedCompanies(queryCriteria, map.createObjectMap());
    }

    public List<File> getIssueFiles(QueryCriteria query, Integer issueId) throws DatabaseException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        return fileService.getFiles(query, ObjectTypes.ISSUE, issueId);
    }

    public File getIssueFile(Integer issueId, Integer fileId) throws DatabaseException, ObjectNotFoundException {
        FileService fileService = ServiceProvider.getFileService(requestContext);
        File file = fileService.getFile(ObjectTypes.ISSUE, issueId, fileId);
        file.setConfigRepositoryPath(ConfigManager.file.getIssueFileRepositoryLocation());
        file.setConfigUploadedFilePrefix(ConfigManager.file.getIssueUploadedFilePrefix());
        return file;
    }

    public ActionMessages addIssueSimple(Issue issue) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("issueName").setTitleKey("common.column.issue_name")
                .setColumnName(Schema.ISSUE_NAME).calculateLength(issue.getSubject()).setNullable(false));

        validator.validate(new ColumnField().setName("issueDescription").setTitleKey("common.column.issue_description")
                .setColumnName(Schema.ISSUE_DESCRIPTION).calculateLength(issue.getDescription()).setNullable(false));

        if (!errors.isEmpty()) {
            return errors;
        }

        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.addIssueSimple(issue);
    }

    public ActionMessages retrieveIssueEmails(PopConnection conn) throws Exception {
        ActionMessages errors = new ActionMessages();

        Pop3 pop3 = new Pop3();
        List<EmailMessage> emailMessages = pop3.receive(conn);

        errors.add(pop3.getErrors());

        conn.setMessagesRetrieved(emailMessages.size());

        for (EmailMessage emailMessage : emailMessages) {
            errors.add(retrieveIssueEmail(emailMessage));
        }

        return errors;
    }

    public ActionMessages retrieveIssueEmail(EmailMessage message) throws Exception {
        ActionMessages errors = new ActionMessages();

        Integer issueId = IssueEmailParser.parseEmailIssueId(message.getSubjectField());

        RequestContext requestContext = new RequestContext();

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        Integer userId = adminService.getUserIdByEmail(message.getFromField());
        if (userId != null) {
            requestContext.setUser(new AccessUser(userId));
        }

        if (issueId == null) {
            // Add issue
            Issue issue = new Issue();
            issue.setSubject(message.getSubjectField().isEmpty() ?
                Localizer.getText(requestContext, "issues.email.emptySubject") : message.getSubjectField());

            issue.setDescription(message.getBodyField());
            issue.setFromEmail(message.getFromField());
            
            Set<String> subscriberEmails = new LinkedHashSet<>();
            for (String ccAddress : message.getCcField()) {
                userId = adminService.getUserIdByEmail(ccAddress);
                if (userId != null) {
                    issue.getSelectedSubscribers().add(userId);
                    subscriberEmails.add(ccAddress);
                }
            }

            IssueDao issueDao = new IssueDao(requestContext);
            errors = issueDao.addIssueSimple(issue);

            if (errors.isEmpty()) {
                if (ConfigManager.email.isIssueNotificationFromEmailEnabled()) {
                    
                    if (ConfigManager.email.getSmtpFrom().equals(message.getFromField())) {
                        LOGGER.log(Level.INFO, "Skip sending of issue email notification. SMTP From Address same as message From Address.");
                    } else {
                        sendSimpleIssueNotification(issue, subscriberEmails);
                    }
                }
            }
        } else {
            // Update an existing issue
            Issue issue = getPublicIssue(issueId);
            issue.setFollowup(message.getBodyField());
            issue.setFromEmail(message.getFromField());

            if (ConfigManager.email.isIssueNotificationFromEmailEnabled()
                && SmtpService.isMessageOriginatedFromApp(ConfigManager.email.getSmtpFrom(), message.getFromField())) {
                // Skip updating this issue to avoid recursive loop and redundant update.
                LOGGER.log(Level.INFO, LogConfigManager.EMAIL_PREFIX + " Skip updating Issue " + issue.getId() + " from email. SMTP From Address same as message's From Address.");
                
            } else {
                Integer prevAssigneeId = issue.getAssignee().getId();
                
                IssueDao issueDao = new IssueDao(requestContext);
                boolean updateSubscribers = false;
                issueDao.update(requestContext, issue, updateSubscribers);
                
                if (errors.isEmpty()) {
                    if (ConfigManager.email.isIssueNotificationFromEmailEnabled()) {
                        sendUpdateIssueNotification(issue, prevAssigneeId);
                    }
                }
            }
        }
        return errors;
    }

    public ActionMessages addIssue(Issue issue, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("issueName").setTitleKey("common.column.issue_name")
                .setColumnName(Schema.ISSUE_NAME).calculateLength(issue.getSubject()).setNullable(false));

        if (issue.getDescription().isEmpty()) {
            errors.add("emptyDescription", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "common.column.issue_description")));
        }
        if (issue.isHasDueDate() && !issue.isValidDueDate()) {
            errors.add("invalidDueDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.issue_due_date")));
        }

        // Validate attributes
        validator.validateAttrs(issue, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        IssueDao issueDao = new IssueDao(requestContext);
        return issueDao.add(requestContext, issue);
    }

    public ActionMessages updateIssue(Issue issue, Map<Integer, Attribute> customAttributes) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("issueName").setTitleKey("common.column.issue_name")
                .setColumnName(Schema.ISSUE_NAME).calculateLength(issue.getSubject()).setNullable(false));

        if (issue.getFollowup().isEmpty()) {
            errors.add("emptyFollowup", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "issueMgmt.colName.comment")));
        }
        if (issue.isHasDueDate() && !issue.isValidDueDate()) {
            errors.add("invalidDueDate", new ActionMessage("common.form.fieldDateInvalid",
                    Localizer.getText(requestContext, "common.column.issue_due_date")));
        }

        validator.validateAttrs(issue, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }

        IssueDao issueDao = new IssueDao(requestContext);
        boolean updateSubscribers = true;
        return issueDao.update(requestContext, issue, updateSubscribers);
    }

    public ActionMessages deleteIssue(Integer issueId) throws DatabaseException {
        /**
         * Here is what i can do. First, collect a list of file names to be deleted (probably in a dataset).
         * Then, delete the object, which would also delete the File records. Next, delete the actual files.
         */
        List<File> deleteFileList = getIssueFiles(new QueryCriteria(), issueId);

        IssueDao issueDao = new IssueDao(requestContext);
        ActionMessages errors = issueDao.delete(issueId);

        FileService fileService = ServiceProvider.getFileService(requestContext);

        // Delete actual files
        if (errors.isEmpty()) {
            fileService.bulkDelete(ConfigManager.file.getIssueFileRepositoryLocation(), deleteFileList);
        }

        return errors;
    }

    public ActionMessages addIssueFile(Issue issue, FileUploadForm actionForm) throws DatabaseException {
        FileService fileService = ServiceProvider.getFileService(requestContext);

        File file = new IssueFile(issue.getId());
        ActionMessages errors = fileService.addFile(file, actionForm);
        if (!errors.isEmpty()) {
            // We have error uploading the file, we're done.
            return errors;
        } else {
            // We get the uploaded file id, set it to fileId.
            issue.setFileId(file.getId());

            // File uploaded fine, add an entry in issue_history.
            IssueDao issueDao = new IssueDao(requestContext);
            return issueDao.addIssueFile(issue);
        }
    }
    
    /**
     * Sends issue notification. Used by Report Issue page.
     */
    public void sendSimpleIssueNotification(Issue issue, Set<String> subscriberEmails) throws Exception {
        // Send out an email.
        EmailMessage message = new EmailMessage();
        
        // Set FROM field
        message.setFromField(ConfigManager.email.getSmtpFrom());

        // Set TO field
        message.getToField().add(ConfigManager.email.getSmtpTo());
        
        // Set CC field
        if (subscriberEmails != null && !subscriberEmails.isEmpty()) {
            for (String subscriberEmail : subscriberEmails) {
                message.getCcField().add(subscriberEmail);
            }
        }

        // Here, we need to get the issue again because otherwise, we don't have creator name and creation date.
        issue = getPublicIssue(issue.getId());

        // Set SUBJECT field
        message.setSubjectField(IssueUtils.formatEmailSubject(requestContext, issue));

        AttributeManager attributeManager = new AttributeManager(requestContext);

        String emailBody = ConfigManager.email.getIssueReportEmailTemplate().isEmpty() ?
                Localizer.getText(requestContext, "issuePlugin.issueAdd2.emailBody") : ConfigManager.email.getIssueReportEmailTemplate();

        emailBody = emailBody.replace(Keywords.ISSUE_ID_VAR, String.valueOf(issue.getId()))
                .replace(Keywords.ISSUE_REPORTED_BY_VAR, AdminUtils.getSystemUsername(requestContext, issue.getCreator()))
                .replace(Keywords.ISSUE_REPORTED_DATE_VAR, issue.getCreationDate())
                .replace(Keywords.ISSUE_STATUS_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_STATUS, issue.getStatus()))
                .replace(Keywords.ISSUE_PRIORITY_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_PRIORITY, issue.getPriority()))
                .replace(Keywords.ISSUE_TYPE_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_TYPE, issue.getType()))
                .replace(Keywords.ISSUE_DESCRIPTION_VAR, issue.getDescription())
                .replace(Keywords.ISSUE_URL_VAR, ConfigManager.system.getAppUrl() + AppPaths.ROOT + AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());

        // Set BODY field
        message.setBodyField(IssueUtils.formatEmailBody(requestContext, emailBody));
        
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    new SmtpService().send(message);
                    
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Problem sending issue notification", e);
                }
            }
        };
        new Thread(run).start();
    }
    
    public void sendUpdateIssueNotification(Issue issue, Integer prevAssigneeId) {
        try {
            AttributeManager attributeManager = new AttributeManager(requestContext);
            
            // Here, we need to get the issue again because otherwise, we don't have correct modifier name and
            // modification date.
            Issue modifiedIssue = getPublicIssue(issue.getId());

            String emailBody = ConfigManager.email.getIssueUpdateEmailTemplate().isEmpty() ?
                                    Localizer.getText(requestContext, "issues.issueEdit2.emailBody") : ConfigManager.email.getIssueUpdateEmailTemplate();

            emailBody = emailBody.replace(Keywords.ISSUE_ASSIGNEE_VAR, AdminUtils.getSystemUsername(requestContext, issue.getAssignee()))
                    .replace(Keywords.ISSUE_ID_VAR, String.valueOf(issue.getId()))
                    .replace(Keywords.ISSUE_REPORTED_BY_VAR, AdminUtils.getSystemUsername(requestContext, issue.getCreator()))
                    .replace(Keywords.ISSUE_REPORTED_DATE_VAR, issue.getCreationDate())
                    .replace(Keywords.ISSUE_STATUS_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_STATUS, issue.getStatus()))
                    .replace(Keywords.ISSUE_PRIORITY_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_PRIORITY, issue.getPriority()))
                    .replace(Keywords.ISSUE_TYPE_VAR, attributeManager.getAttrFieldNameCache(Attributes.ISSUE_TYPE, issue.getType()))
                    .replace(Keywords.ISSUE_DESCRIPTION_VAR, issue.getDescription())
                    .replace(Keywords.ISSUE_COMMENT_VAR, issue.getFollowup())
                    .replace(Keywords.ISSUE_COMMENTED_BY_VAR, AdminUtils.getSystemUsername(requestContext, modifiedIssue.getModifier()))
                    .replace(Keywords.ISSUE_COMMENTED_DATE_VAR, modifiedIssue.getModificationDate())
                    .replace(Keywords.ISSUE_URL_VAR, ConfigManager.system.getAppUrl() + AppPaths.ROOT + AppPaths.ISSUES_DETAIL + "?issueId=" + issue.getId());

            sendMail(modifiedIssue, prevAssigneeId, emailBody);
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Problem sending issue notification", e);
        }
    }
    
    public  void sendMail(Issue issue, Integer prevAssigneeId, String bodyField) throws DatabaseException {
        EmailMessage message = new EmailMessage();
        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Set FROM field
        message.setFromField(ConfigManager.email.getSmtpFrom());

        if (issue.hasAssignee()) {
            try {
                // User may not exist anymore
                AccessUser assignee = new CacheManager(requestContext).getUserCache(issue.getAssignee().getId());
                if (!assignee.isRemoved()) {
                    message.getToField().add(assignee.getEmail());
                }
            } catch (Exception e) {
                /* ignore */
            }
        }

        if (prevAssigneeId != null) {
            try {
                // User may not exist anymore
                AccessUser prevAssignee = adminService.getUser(prevAssigneeId);
                message.addCcField(prevAssignee.getEmail());
            } catch (Exception e) {
                /* ignore */
            }
        }

        // Email to issue reporter if reporter is not default guest user
        AccessUser creator = issue.getCreator();
        if (!creator.getId().equals(Access.GUEST_USER_ID)) {
            try {
                // User may not exist anymore
                creator = adminService.getUser(creator.getId());
                message.getToField().add(creator.getEmail());
            } catch (ObjectNotFoundException e) {
                /* ignore */
            }
        }

        // Get a list of selected subscribers.
        List<AccessUser> selectedSubscribers = getSelectedSubscribers(issue.getId());
        if (!selectedSubscribers.isEmpty()) {
            for (AccessUser subscriber : selectedSubscribers) {
                message.addCcField(subscriber.getEmail());
            }
        }

        if (message.getToField().isEmpty() && !message.getCcField().isEmpty()) {
            message.getToField().addAll(message.getCcField());
            message.getCcField().clear();
        }

        // Set SUBJECT field
        message.setSubjectField(IssueUtils.formatEmailSubject(requestContext, issue));

        // Set BODY field
        message.setBodyField(IssueUtils.formatEmailBody(requestContext, bodyField));

        // Debug:
//        logger.log(Level.INFO, "From: " + message.getFromField());
//        logger.log(Level.INFO, "To: " + message.getToField());
//        logger.log(Level.INFO, "CC: " + message.getCcField());
//        logger.log(Level.INFO, "Subject: " + message.getSubjectField());
//        logger.log(Level.INFO, "Message: " + message.getBodyField());

        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    new SmtpService().send(message);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Problem sending email", e);
                }
            }
        };
        new Thread(run).start();
    }
}
