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
package com.kwoksys.biz.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AttributeSearch;
import com.kwoksys.biz.admin.dao.AccessGroupDao;
import com.kwoksys.biz.admin.dao.AccessUserDao;
import com.kwoksys.biz.admin.dao.AdminDao;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.admin.dto.AttributeValue;
import com.kwoksys.biz.admin.dto.DbSequence;
import com.kwoksys.biz.admin.dto.GroupPermissionMap;
import com.kwoksys.biz.admin.dto.Icon;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.biz.admin.dto.UserPermissionMap;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.auth.core.AuthUtils;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.biz.system.core.configs.ConfigKeys;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.framework.validations.ColumnField;
import com.kwoksys.framework.validations.InputValidator;

/**
 * AdminService
 */
public class AdminService {

    private RequestContext requestContext;

    public AdminService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * Get database names
     * @return
     * @throws DatabaseException
     */
    public List<Map<String, String>> getDatabases() throws DatabaseException {
        return new AdminDao(requestContext).getDatabases();
    }

    public List<DbSequence> getDbSequences() throws DatabaseException {
        return new AdminDao(requestContext).getDbSequences();
    }

    public ActionMessages updateConfig(List<SystemConfig> list) throws DatabaseException {
        ActionMessages errors = new AdminDao(requestContext).updateConfig(list);
        if (errors.isEmpty()) {
            // Reset system key cache if there is no errors.
            resetSystemCacheConfig();
        }
        return errors;
    }

    /**
     * Reset system cache key with unix timestamp
     * @return
     * @throws DatabaseException
     */
    private ActionMessages resetSystemCacheConfig() throws DatabaseException {
        SystemService systemService = ServiceProvider.getSystemService(requestContext);

        List<SystemConfig> list = new ArrayList<>();
        list.add(new SystemConfig(ConfigKeys.SYSTEM_CACHE_KEY, String.valueOf(systemService.getSystemInfo().getSysdate().getTime())));

        return new AdminDao(requestContext).updateConfig(list);
    }

    // Attributes
    public Map<Integer, AttributeGroup> getAttributeGroups(Integer objectTypeId) throws DatabaseException {
        return new AttributeDao(requestContext).getAttributeGroups(objectTypeId);
    }

    public AttributeGroup getAttributeGroup(Integer attributeGroupId, Integer objectTypeId) throws DatabaseException, ObjectNotFoundException {
        return new AttributeDao(requestContext).getAttributeGroup(attributeGroupId, objectTypeId);
    }

    public Map<Integer, Attribute> getAttributes(QueryCriteria query) throws DatabaseException {
        return new AttributeDao(requestContext).getAttributeList(query);
    }

    public boolean hasCustomFields(Integer objectTypeId) throws DatabaseException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put(AttributeSearch.IS_CUSTOM_ATTR, true);
        attributeSearch.put(AttributeSearch.OBJECT_TYPE_ID_EUALS, objectTypeId);

        QueryCriteria query = new QueryCriteria(attributeSearch);

        return new AttributeDao(requestContext).hasCustomFields(query);
    }

    public Attribute getSystemAttribute(Integer attributeId) throws DatabaseException, ObjectNotFoundException {
        Attribute attr = new AttributeDao(requestContext).getAttribute(attributeId);

        // Make sure we allow adding attribute field for this attribute id
        if (attr.isCustomAttr() || !attr.isAttrFieldsEditable()) {
            throw new ObjectNotFoundException();
        }
        return attr;
    }

    public Attribute getCustomAttribute(Integer attributeId) throws DatabaseException, ObjectNotFoundException {
        AttributeDao attrDao = new AttributeDao(requestContext);
        Attribute attr = attrDao.getAttribute(attributeId);

        // Make sure we allow custom attribute for this object type
        if (!attr.isCustomAttr()) {
            throw new ObjectNotFoundException();
        }
        return attr;
    }

    public Map<Integer, AttributeField> getEditAttributeFields(QueryCriteria query) throws DatabaseException {
        return new AttributeDao(requestContext).getAttributeFields(query);
    }

    public Map<Integer, AttributeField> getAttributeFields(Integer attributeId) throws DatabaseException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put("attributeIdEquals", attributeId);

        QueryCriteria query = new QueryCriteria(attributeSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AttributeField.NAME));

        return new AttributeDao(requestContext).getAttributeFields(query);
    }

    public AttributeField getAttributeField(Integer attrFieldId) throws DatabaseException, ObjectNotFoundException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put("isEditable", true);

        return new AttributeDao(requestContext).getAttributeField(new QueryCriteria(attributeSearch), attrFieldId);
    }

    /**
     * Given an object_type_id, object_id, return a list of custom attributes.
     * @return
     * @throws DatabaseException
     */
    public Map<Integer, Object> getCustomAttributeValueMap(Integer objectTypeId, Integer objectId) throws DatabaseException {
        AttributeSearch attributeSearch = new AttributeSearch();
        attributeSearch.put("objectTypeId", objectTypeId);
        attributeSearch.put("objectId", objectId);

        QueryCriteria query = new QueryCriteria(attributeSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(AttributeValue.ATTR_KEY));

        return new AttributeDao(requestContext).getCustomAttributeValueMap(query);
    }

    public List<Integer> getSavedAttributeFieldTypes(Integer attrFieldId) throws DatabaseException {
        if (attrFieldId == null || attrFieldId == 0) {
            return new ArrayList<>();
        }
        return new AttributeDao(requestContext).getSavedAttributeFieldTypes(new QueryCriteria(), attrFieldId);
    }

    public Set<Integer> getAttributeFieldTypesByField(Integer attrFieldId) throws DatabaseException {
        if (attrFieldId == null || attrFieldId == 0) {
            return new HashSet<>();
        }
        return new AttributeDao(requestContext).getAttributeFieldTypesByField(new QueryCriteria(), attrFieldId);
    }

    /**
     * Adds attribute group.
     * @param attr
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addAttributeGroup(AttributeGroup attributeGroup) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (attributeGroup.getName().isEmpty()) {
            errors.add("emptyField", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "admin.attribute.attribute_group_name")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).addAttributeGroup(attributeGroup);
    }

    /**
     * Updates attribute group.
     * @param attributeGroup
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateAttributeGroup(AttributeGroup attributeGroup) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (attributeGroup.getName().isEmpty()) {
            errors.add("emptyField", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "admin.attribute.attribute_group_name")));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).updateAttributeGroup(attributeGroup);
    }

    public ActionMessages deleteAttributeGroup(AttributeGroup attributeGroup) throws DatabaseException {
        return new AttributeDao(requestContext).deleteAttributeGroup(attributeGroup);
    }

    /**
     * Add custom attribute.
     * @param attr
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addAttribute(Attribute attr) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("fieldName").setTitleKey("admin.attribute.attribute_name")
                .setNullable(false).calculateLength(attr.getName()));

        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).addCustomAttribute(attr);
    }

    /**
     * Updating custom attribute.
     * @param attr
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateAttribute(Attribute attr) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        InputValidator validator = new InputValidator(requestContext, errors);

        validator.validate(new ColumnField().setName("fieldName").setTitleKey("admin.attribute.attribute_name")
                .setNullable(false).calculateLength(attr.getName()));

        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).updateCustomAttribute(attr);
    }

    public ActionMessages updateSystemAttribute(Attribute attr) throws DatabaseException {
        return new AttributeDao(requestContext).updateSystemAttribute(attr);
    }

    /**
     * This is for deleting custom attributes.
     * @param attr
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteAttribute(Attribute attr) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).deleteAttribute(attr);
    }

    public ActionMessages addAttributeField(AttributeField attrField) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (attrField.getName().isEmpty()) {
            errors.add("emptyField", new ActionMessage("admin.attributeAdd.error.emptyField"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).addAttributeField(attrField);
    }

    public ActionMessages updateAttributeField(AttributeField attrField) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        if (attrField.getName().isEmpty()) {
            errors.add("emptyField", new ActionMessage("admin.attributeAdd.error.emptyField"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AttributeDao(requestContext).updateAttributeField(attrField);
    }

    /**
     * Get icons.
     */
    public List<Icon> getIcons(Integer attributeId) throws DatabaseException {
        return new AttributeDao(requestContext).getIcons(attributeId);
    }

    /**
     * Get group list
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<AccessGroup> getGroups(QueryCriteria query) throws DatabaseException {
        return new AccessGroupDao(requestContext).getGroups(query);
    }

    public AccessGroup getGroup(Integer groupId) throws DatabaseException, ObjectNotFoundException {
        return new AccessGroupDao(requestContext).getGroup(groupId);
    }

    public ActionMessages addGroup(AccessGroup group) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (group.getName().isEmpty()) {
            errors.add("groupName", new ActionMessage("admin.groupEdit.error.groupName"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AccessGroupDao(requestContext).addGroup(group);
    }

    public ActionMessages updateGroup(AccessGroup group) throws DatabaseException {
        ActionMessages errors = new ActionMessages();

        if (group.getName().isEmpty()) {
            errors.add("groupName", new ActionMessage("admin.groupEdit.error.groupName"));
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        return new AccessGroupDao(requestContext).editGroup(group);
    }

    public ActionMessages deleteGroup(AccessGroup group) throws DatabaseException {
        return new AccessGroupDao(requestContext).deleteGroup(group);
    }

    public List<GroupPermissionMap> getGroupAccess(QueryCriteria query, Integer groupId) throws DatabaseException {
        return new AccessGroupDao(requestContext).getGroupAccess(query, groupId);
    }

    public ActionMessages updateGroupAccess(GroupPermissionMap groupperm) throws DatabaseException {
        return new AccessGroupDao(requestContext).updateGroupAccess(groupperm);
    }

    public List<AccessUser> getAvailableMembers(Integer groupId) throws DatabaseException {
        return new AccessGroupDao(requestContext).getAvailableMembers(groupId);
    }

    public List<AccessUser> getGroupMembers(Integer groupId) throws DatabaseException {
        return new AccessGroupDao(requestContext).getGroupMembers(groupId);
    }

    /**
     * Get user list
     * @param query
     * @return
     * @throws DatabaseException
     */
    public List<AccessUser> getUsers(QueryCriteria query) throws DatabaseException {
        return new AccessUserDao(requestContext).getUsers(query);
    }

    public List<AccessUser> getExtendedUsers(QueryCriteria query) throws DatabaseException {
        return new AccessUserDao(requestContext).getExtendedUsers(query);
    }

    public int getUserCount(QueryCriteria query) throws DatabaseException {
        return new AccessUserDao(requestContext).getUserCount(query);
    }

    public AccessUser getUser(Integer userId) throws DatabaseException, ObjectNotFoundException {
        return new AccessUserDao(requestContext).getUser(userId);
    }

    public Integer getUserIdByUsername(String username) throws DatabaseException {
        return new AccessUserDao(requestContext).getUserIdByUsername(username);
    }

    public Integer getUserIdByEmail(String email) throws DatabaseException {
        return new AccessUserDao(requestContext).getUserIdByEmail(email);
    }

    public List<UserPermissionMap> getUserAccess(QueryCriteria query, Integer userId) throws DatabaseException {
        return new AccessUserDao(requestContext).getUserAccess(query, userId);
    }

    public ActionMessages addUser(AccessUser user, AccessGroup group, Contact contact,
                                  Map<Integer, Attribute> customAttributes) throws DatabaseException {

        ActionMessages errors = validateUser(user, contact, customAttributes);
        InputValidator validator = new InputValidator(requestContext, errors);

        // Validate new password
        validator.validatePassword("common.column.user_password", user);

        if (!errors.isEmpty()) {
            return errors;
        }
        return new AccessUserDao(requestContext).addUser(user, group, contact);
    }

    public ActionMessages updateUser(AccessUser user, AccessGroup group, Contact contact,
                                     Map<Integer, Attribute> customAttributes) throws DatabaseException {

        ActionMessages errors = validateUser(user, contact, customAttributes);

        if (!errors.isEmpty()) {
            return errors;
        }
        return new AccessUserDao(requestContext).updateUser(user, group, contact);
    }

    public ActionMessages validateUser(AccessUser user, Contact contact, Map<Integer, Attribute> customAttributes)
            throws DatabaseException {

        ActionMessages errors = new ActionMessages();

        if (StringUtils.isEmpty(user.getUsername())) {
            errors.add("username", new ActionMessage("common.form.fieldRequired", Localizer.getText(requestContext, "common.column.username")));
        } else {
            Integer queryUserId = getUserIdByUsername(user.getUsername());
            if (queryUserId != null && !queryUserId.equals(user.getId())) {
                errors.add("usernameInUse", new ActionMessage("admin.userEdit.error.usernameInUse"));
            }
        }

        if (StringUtils.isEmpty(user.getEmail())) {
            errors.add("email", new ActionMessage("common.form.fieldRequired", Localizer.getText(requestContext,
                    "common.column.user_email")));
        }

        InputValidator validator = new InputValidator(requestContext, errors);
        validateContactInputs(validator, errors, contact);

        validator.validate(new ColumnField().setName("displayName").setTitleKey("common.column.user_display_name")
                .calculateLength(user.getDisplayName()).setColumnName(Schema.ACCESS_USER_DISPLAY_NAME).setNullable(false));

        validator.validate(new ColumnField().setName("firstName").setTitleKey("common.column.contact_first_name")
                .calculateLength(user.getFirstName()).setColumnName(Schema.CONTACT_FIRST_NAME).setNullable(false));

        validator.validate(new ColumnField().setName("lastName").setTitleKey("common.column.contact_last_name")
                .calculateLength(user.getLastName()).setColumnName(Schema.CONTACT_LAST_NAME).setNullable(false));

        // Validate attributes
        validator.validateAttrs(user, customAttributes);

        return errors;
    }

    public ActionMessages updateUserContact(AccessUser user, Contact contact) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        validateContactInputs(new InputValidator(requestContext, errors), errors, contact);

        if (!errors.isEmpty()) {
            return errors;
        }
        return new AccessUserDao(requestContext).updateUserContact(user, contact);
    }

    private void validateContactInputs(InputValidator validator, ActionMessages errors, Contact contact) {
        if (contact != null) {
            if ((contact.getMessenger1Type() != 0 && StringUtils.isEmpty(contact.getMessenger1Id()))
                    || (contact.getMessenger1Type() == 0 && !StringUtils.isEmpty(contact.getMessenger1Id()))
                    || (contact.getMessenger2Type() != 0 && StringUtils.isEmpty(contact.getMessenger2Id()))
                    || (contact.getMessenger2Type() == 0 && !StringUtils.isEmpty(contact.getMessenger2Id()))) {
                errors.add("invalidMessengerInput", new ActionMessage("admin.userEdit.error.invalidMessengerInput"));
            } else {
                validator.validate(new ColumnField().setName("im1").setTitleKey("common.column.contact_im")
                        .calculateLength(contact.getMessenger1Id()).setColumnName(Schema.CONTACT_MESSENGER_1_ID));

                validator.validate(new ColumnField().setName("im2").setTitleKey("common.column.contact_im")
                        .calculateLength(contact.getMessenger2Id()).setColumnName(Schema.CONTACT_MESSENGER_2_ID));
            }

            validator.validate(new ColumnField().setName("contactHomePhone").setTitleKey("common.column.contact_phone_home")
                    .calculateLength(contact.getPhoneHome()).setColumnName(Schema.CONTACT_HOME_PHONE));

            validator.validate(new ColumnField().setName("contactMobilePhone").setTitleKey("common.column.contact_phone_mobile")
                    .calculateLength(contact.getPhoneMobile()).setColumnName(Schema.CONTACT_CELL_PHONE));

            validator.validate(new ColumnField().setName("contactWorkPhone").setTitleKey("common.column.contact_phone_work")
                    .calculateLength(contact.getPhoneWork()).setColumnName(Schema.CONTACT_WORK_PHONE));

            validator.validate(new ColumnField().setName("contactFax").setTitleKey("common.column.contact_fax")
                    .calculateLength(contact.getFax()).setColumnName(Schema.CONTACT_FAX));

            validator.validate(new ColumnField().setName("emailPrimary").setTitleKey("common.column.contact_main_email")
                    .calculateLength(contact.getEmailPrimary()).setColumnName(Schema.CONTACT_PRIMARY_EMAIL));

            validator.validate(new ColumnField().setName("emailAlternate").setTitleKey("common.column.contact_email_secondary")
                    .calculateLength(contact.getEmailSecondary()).setColumnName(Schema.CONTACT_SECONDARY_EMAIL));

            validator.validate(new ColumnField().setName("addressStreet").setTitleKey("common.column.contact_address_street_primary")
                    .calculateLength(contact.getAddressStreetPrimary()).setColumnName(Schema.CONTACT_ADDRESS_STREET));

            validator.validate(new ColumnField().setName("addressCity").setTitleKey("common.column.contact_address_city_primary")
                    .calculateLength(contact.getAddressCityPrimary()).setColumnName(Schema.CONTACT_ADDRESS_CITY));

            validator.validate(new ColumnField().setName("addressState").setTitleKey("common.column.contact_address_state_primary")
                    .calculateLength(contact.getAddressStatePrimary()).setColumnName(Schema.CONTACT_ADDRESS_STATE));
        }
    }

    /**
     * Updates user password from User Preferences. The code checks old password against what's saved in the database.
     * @param user
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateUserPassword(AccessUser accessUser) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        InputValidator validator = new InputValidator(requestContext, errors);

        // Check whether the old password is empty.
        validator.validate(new ColumnField().setName("password").setNullable(false)
                .calculateLength(accessUser.getRequestedPassword())
                .setTitleKey("userPref.passwordEdit.passwordOld"));

        if (StringUtils.isEmpty(accessUser.getPasswordNew())) {
            errors.add("password", new ActionMessage("common.form.fieldRequired",
                    Localizer.getText(requestContext, "admin.userPasswordReset.passwordNew")));
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        // Run a queries to check whether the old password is the same as what we have in the database.
        try {
            if (!AuthUtils.hashPassword(accessUser.getRequestedPassword()).equals(accessUser.getHashedPassword())) {
                errors.add("oldPasswordMismatch", new ActionMessage("userPref.passwordEdit.error.oldPasswordMismatch"));
            }
        } catch (Exception e) {
            errors.add("application", new ActionMessage("common.error.application"));
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        return resetUserPassword(accessUser);
    }

    /**
     * Updates user password. This is for admin to reset a user's password. Therefore, no need to check old password.
     * @param user
     * @return
     * @throws DatabaseException
     */

    public ActionMessages resetUserPassword(AccessUser user) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        InputValidator inputValidator = new InputValidator(requestContext, errors);

        inputValidator.validatePassword("admin.userPasswordReset.passwordNew", user);

        if (!errors.isEmpty()) {
            return errors;
        }

        return new AccessUserDao(requestContext).editUserPassword(user);
    }

    public ActionMessages deleteUser(AccessUser user) throws DatabaseException {
        return new AccessUserDao(requestContext).deleteUser(user);
    }

    public ActionMessages updateUserAccess(UserPermissionMap userperm) throws DatabaseException {
        return new AccessUserDao(requestContext).updateUserAccess(userperm);
    }

    /**
     * Returns true if the application allows user password update.
     * When LDAP authentication is turned on, and when all authentication are done via LDAP, no user password update
     * needed anyway.
     * @return
     */
    public boolean allowPasswordUpdate() {
        return !ConfigManager.auth.getAuthMethod().equals(Access.AUTH_LDAP);
    }
}
