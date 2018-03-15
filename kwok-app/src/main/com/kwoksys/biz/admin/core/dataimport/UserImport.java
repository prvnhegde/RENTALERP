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
package com.kwoksys.biz.admin.core.dataimport;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.ImportItem;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Keywords;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.framework.validations.InputValidator;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserImport extends ImportManager {

    private static final Logger LOGGER = Logger.getLogger(UserImport.class.getName());

    /**
     * Group names and groups mapping.
     */
    private Map<String, AccessGroup> groupMap = new HashMap<>();

    protected void importData(List<String[]> data, boolean validateOnly) throws Exception {
        Iterator<String[]> rowIter = data.iterator();

        // First row contains headers.
        List<String> columnHeaders = Arrays.asList(rowIter.next());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        // Get custom field values from csv.
        Map<Integer, Attribute> typeCustomFields = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.USER);

        // Put groups in map
        for (AccessGroup group : adminService.getGroups(new QueryCriteria())) {
            groupMap.put(group.getName().toLowerCase(), group);
        }

        while (rowIter.hasNext()) {
            boolean overwrite = false;
            ImportItem importItem = new ImportItem();
            importItem.setRowNum(++rowNum);

            try {
                String[] columns = rowIter.next();
                AccessUser accessUser = null;
                AccessGroup accessGroup = null;

                // Get user record by name.
                int colNumber = columnHeaders.indexOf(AccessUser.USERNAME);
                if (colNumber != -1) {
                    String username = columns[colNumber].trim();
                    Integer userId = adminService.getUserIdByUsername(username);

                    if (userId != null) {
                        accessUser = adminService.getUser(userId);
                    }
                }

                // When AccessUser is null, create a new user.
                if (accessUser == null) {
                    accessUser = new AccessUser();
                } else {
                    overwrite = true;
                }

                Map<String, String> importCustomFields = new HashMap<>();

                ActionMessages errors = new ActionMessages();
                InputValidator inputValidator = new InputValidator(requestContext, errors);

                for (int i = 0; i<columns.length; i++) {
                    String key = columnHeaders.get(i).trim();
                    String value = columns[i].trim();
                    //System.out.println(key + ": " + value);

                    if (value.isEmpty()) {
                        continue;
                    }

                    if (key.equals(AccessUser.USERNAME)) {
                        accessUser.setUsername(value);
                        importItem.setTitle(value);

                    } else if (key.equals("first_name")) {
                        accessUser.setFirstName(value);

                    } else if (key.equals("last_name")) {
                        accessUser.setLastName(value);

                    } else if (key.equals("display_name")) {
                        accessUser.setDisplayName(value);

                    } else if (key.equals("email")) {
                        accessUser.setEmail(value);

                    } else if (key.equals("password")) {
                        accessUser.setPasswordNew(value);
                        accessUser.setPasswordConfirm(value);

                    } else if (key.equals("status")) {
                        if (value.equals(Keywords.USER_STATUS_ENABLED)) {
                            accessUser.setStatus(AttributeFieldIds.USER_STATUS_ENABLED);
                        } else if (value.equals(Keywords.USER_STATUS_DISABLED)) {
                            accessUser.setStatus(AttributeFieldIds.USER_STATUS_DISABLED);
                        } else {
                            errors.add("status", new ActionMessage("import.validate.message.invalidUserStatus", new String[]{StringUtils.join(Keywords.USER_STATUS_LIST, ", ")}));
                        }
                    } else if (key.equals("group_name")) {
                        accessGroup = groupMap.get(value.toLowerCase());
                        if (accessGroup == null) {
                            importItem.getWarningMessages().add(Localizer.getText(requestContext, "import.validate.message.noMatches",
                                    new String[]{Localizer.getText(requestContext, "common.column.group_name"), value}));
                        }
                    } else {
                        importCustomFields.put(key, value);
                    }
                }

                Contact contact = contactService.getOptionalContact(accessUser.getContactId());
                contact.setUserId(accessUser.getId());
                contact.setEmailPrimary(accessUser.getEmail());
                contact.setFirstName(accessUser.getFirstName());
                contact.setLastName(accessUser.getLastName());

                AttributeManager.populateCustomFieldValues(importCustomFields, accessUser, typeCustomFields);

                if (errors.isEmpty()) {
                    if (validateOnly) {
                        errors.add(adminService.validateUser(accessUser, null, typeCustomFields));

                        // Need to validate password for new users.
                        if (!overwrite || !StringUtils.isEmpty(accessUser.getPasswordNew())) {
                            inputValidator.validatePassword("common.column.user_password", accessUser);
                        }
                    } else {
                        if (overwrite) {
                            errors.add(adminService.updateUser(accessUser, accessGroup, contact, typeCustomFields));

                            // Only need this for user update. When creating a new user, it updates the password already.
                            if (!StringUtils.isEmpty(accessUser.getPasswordNew())) {
                                errors.add(adminService.resetUserPassword(accessUser));
                            }
                            
                            // If no errors, remove cache.
                            new CacheManager(requestContext).removeUserCache(accessUser.getId());
                            
                        } else {
                            errors.add(adminService.addUser(accessUser, accessGroup, contact, typeCustomFields));
                        }
                    }
                }

                if (!errors.isEmpty()) {
                    for (Iterator<ActionMessage> iter = errors.get(); iter.hasNext();) {
                        ActionMessage error = iter.next();
                        importItem.getErrorMessages().add(Localizer.getText(requestContext, error.getKey(), error.getValues()));
                    }
                    importItem.setAction(ImportItem.ACTION_ERROR);
                } else {
                    importItem.setAction(overwrite ? ImportItem.ACTION_UPDATE : ImportItem.ACTION_ADD);
                }

            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Problem importing user", e);
                importItem.getErrorMessages().add(e.getMessage());
                importItem.setAction(ImportItem.ACTION_ERROR);
            }

            addImportItem(importItem);
        }

        buildImportResultsMessage();
    }
}
