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
package com.kwoksys.biz.admin.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.dao.AdminQueries;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeGroup;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.biz.system.core.AttributeFieldIds;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.Image;
import com.kwoksys.biz.system.core.Links;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.properties.PropertiesManager;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * This class contain admin related functions.
 */
public class AdminUtils {

    public static final String ADMIN_APP_CMD = "app";
    public static final String ADMIN_APP_EDIT_CMD = "appEdit";
    public static final String ADMIN_APP_EDIT_2_CMD = "appEdit2";

    public static final String ADMIN_AUTH_CMD = "auth";
    public static final String ADMIN_AUTH_EDIT_CMD = "authEdit";
    public static final String ADMIN_AUTH_EDIT_2_CMD = "authEdit2";

    public static final String ADMIN_DB_BACKUP_CMD = "backup";
    public static final String ADMIN_DB_BACKUP_EDIT_CMD = "backupEdit";
    public static final String ADMIN_DB_BACKUP_EDIT_2_CMD = "backupEdit2";
    public static final String ADMIN_DB_BACKUP_EXECUTE = "backupExecute";

    public static final String ADMIN_COMPANY_CMD = "company";
    public static final String ADMIN_COMPANY_EDIT_CMD = "companyEdit";
    public static final String ADMIN_COMPANY_EDIT_2_CMD = "companyEdit2";

    public static final String ADMIN_DB_SEQUENCES_CMD = "dbSequences";

    public static final String ADMIN_EMAIL_CMD = "email";
    public static final String ADMIN_EMAIL_SMTP_EDIT_CMD = "emailEdit";
    public static final String ADMIN_EMAIL_SMTP_EDIT_2_CMD = "emailEdit2";
    public static final String ADMIN_EMAIL_POP_EDIT_CMD = "popEmailEdit";
    public static final String ADMIN_EMAIL_POP_EDIT_2_CMD = "popEmailEdit2";

    public static final String ADMIN_FILE_CMD = "file";
    public static final String ADMIN_FILE_EDIT_CMD = "fileEdit";
    public static final String ADMIN_FILE_EDIT_2_CMD = "fileEdit2";

    public static final String ADMIN_LDAP_TEST_CMD = "ldapTest";
    public static final String ADMIN_LDAP_TEST_2_CMD = "ldapTest2";

    public static final String ADMIN_LOOK_FEEL_CMD = "look";
    public static final String ADMIN_LOOK_FEEL_EDIT_CMD = "lookEdit";
    public static final String ADMIN_LOOK_FEEL_EDIT_2_CMD = "lookEdit2";

    public static final String ADMIN_LOGGING_EDIT_CMD = "loggingEdit";
    public static final String ADMIN_LOGGING_EDIT_2_CMD = "loggingEdit2";

    public static final String ADMIN_REPORTS_EDIT_CMD = "reportsEdit";
    public static final String ADMIN_REPORTS_EDIT_2_CMD = "reportsEdit2";

    public static final String ADMIN_SYSTEM_INFO_CMD = "system";

    public static final String ADMIN_RELOAD_LOCALIZER_CMD = "reloadLocalizer";
    public static final String ADMIN_RUN_TESTS_CMD = "runTests";
    
    public static final String USER_DISPLAY_NAME = "user_display_name";
    public static final String USER_USERNAME = "username";

    static final Map<Integer, Integer> objectTypeMap = new HashMap<>();
    static {
        objectTypeMap.put(ObjectTypes.HARDWARE, Attributes.HARDWARE_TYPE);
        objectTypeMap.put(ObjectTypes.SOFTWARE, Attributes.SOFTWARE_TYPE);
        objectTypeMap.put(ObjectTypes.ISSUE, Attributes.ISSUE_TYPE);
        objectTypeMap.put(ObjectTypes.CONTRACT, Attributes.CONTRACT_TYPE);
    }

    public static boolean isAttributeTypeMappingEnabled(Integer attrId) {
        if (attrId == null) {
            return false;
        }
        return objectTypeMap.containsValue(attrId);
    }

    /**
     * @param contact
     * @param request
     * @return ..
     */
    public static DetailTableTemplate formatUserContact(Contact contact, RequestContext requestContext, boolean showAdminNotes) throws DatabaseException {
        DetailTableTemplate template = new DetailTableTemplate();
        template.setNumColumns(2);

        DetailTableTemplate.Td td = template.newTd();
        td.setHeaderKey("common.column.contact_id");
        td.setValue(contact.getId() == 0 ? "" : String.valueOf(contact.getId()));

        td = template.newTd();
        td.setHeaderKey("common.column.company_name");
        td.setValue(Links.getCompanyDetailsLink(requestContext, contact.getCompanyName(), contact.getCompanyId()).getString());

        td = template.newTd();
        td.setHeaderKey("common.column.contact_title");
        td.setValue(HtmlUtils.encode(contact.getTitle()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_phone_work");
        td.setValue(HtmlUtils.encode(contact.getPhoneWork()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_phone_home");
        td.setValue(HtmlUtils.encode(contact.getPhoneHome()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_phone_mobile");
        td.setValue(HtmlUtils.encode(contact.getPhoneMobile()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_fax");
        td.setValue(HtmlUtils.encode(contact.getFax()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_address_street_primary");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getAddressStreetPrimary()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_address_city_primary");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getAddressCityPrimary()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_address_state_primary");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getAddressStatePrimary()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_address_zipcode_primary");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getAddressZipcodePrimary()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_address_country_primary");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getAddressCountryPrimary()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_email_secondary");
        td.setValue(HtmlUtils.formatMailtoLink(contact.getEmailSecondary()));

        td = template.newTd();
        td.setHeaderKey("common.column.contact_homepage_url");
        td.setValue(HtmlUtils.formatExternalLink(requestContext, contact.getHomepageUrl()));

        td = template.newTd();
        if (contact.getMessenger1TypeAttribute(requestContext).isEmpty()) {
            td.setHeaderKey("common.column.contact_im");
        } else {
            td.setHeaderText(Localizer.getText(requestContext, "common.column.contact_im_not_null", new Object[]{HtmlUtils.encode(contact.getMessenger1TypeAttribute(requestContext))}));
        }
        td.setValue(HtmlUtils.encode(contact.getMessenger1Id()));

        td = template.newTd();
        if (contact.getMessenger2TypeAttribute(requestContext).isEmpty()) {
            td.setHeaderKey("common.column.contact_im");
        } else {
            td.setHeaderText(Localizer.getText(requestContext, "common.column.contact_im_not_null", new Object[]{HtmlUtils.encode(contact.getMessenger2TypeAttribute(requestContext))}));
        }
        td.setValue(HtmlUtils.encode(contact.getMessenger2Id()));

        if (showAdminNotes) {
            td = template.newTd();
            td.setHeaderKey("common.column.contact_description");
            td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getDescription()));
        }
        return template;
    }

    /**
     * This is to validate currency symbol.
     * For now, we accept everything except comma.
     *
     * @param input
     * @return ..
     */
    public static boolean validCurrencySymbol(Object input) {
        if (input != null) {
            if (input.toString().contains(",")) {
                return false;
            }
        }
        return true;
    }

    /**
     * This determines whether the Edit Access page should disable the
     * editing of permission.
     * @param user
     * @return
     */
    public static boolean disableAccessEdit(AccessUser user) {
        if (user.getId() == 1) {
            return true;

        } else if (user.getGroupId() != 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns allowed sortable columns.
     * @return
     */
    public static List<String> getSortableUserColumns() {
        return Arrays.asList(AccessUser.USERNAME, AccessUser.FIRST_NAME, AccessUser.LAST_NAME, AccessUser.DISPLAY_NAME,
                AccessUser.EMAIL);
    }

    /**
     * Returns whether a column can be sorted.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableUserColumn(String columnName) {
        return getSortableUserColumns().contains(columnName);
    }

    /**
     * Return the column header for user list page.
     */
    public static List<String> getUserColumnHeaders() {
        return Arrays.asList(AccessUser.ROWNUM, AccessUser.USERNAME, AccessUser.FIRST_NAME, AccessUser.LAST_NAME,
                AccessUser.DISPLAY_NAME, AccessUser.EMAIL, AccessUser.STATUS);
    }

    public static List<LabelValueBean> getUserAccessOptionList() {
        return Arrays.asList(new LabelValueBean(getUserAccessIcon(true), "1"),
                new LabelValueBean(getUserAccessIcon(false), "0"));
    }

    public static String getUserAccessIcon(boolean hasPermission) {
        if (hasPermission) {
            return Image.getInstance().getPermissionYesIcon();
        } else {
            return Image.getInstance().getPermissionNoIcon();
        }
    }

    public static List<LabelValueBean> getAttributeStatusList(RequestContext requestContext) {
        return getAttributeStatusList(requestContext, false);
    }

    public static List<LabelValueBean> getAttributeStatusList(RequestContext requestContext, boolean isDefaultAttr) {
        List<LabelValueBean> list = new ArrayList<>();
        list.add(new LabelValueBean(Localizer.getText(requestContext, "common.boolean.enabled_disabled.enabled"), "0"));

        if (!isDefaultAttr) {
            list.add(new LabelValueBean(Localizer.getText(requestContext, "common.boolean.enabled_disabled.disabled"), "1"));
        }
        return list;
    }

    public static String[] getBackupCommand() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");

        String backupFileLocation = ConfigManager.file.getDbBackupRepositoryPath()
                + ConfigManager.system.getTrailingSlash()
                + "BACKUP-"
                + PropertiesManager.get(PropertiesManager.DB_NAME_KEY)
                + "-"
                + formatter.format(Calendar.getInstance().getTime())
                + ConfigManager.file.getPostgresBackupExtension();

        return new String[]{ConfigManager.file.getDbPostgresProgramPath(),
                "-f", backupFileLocation,
                "-h", PropertiesManager.get(PropertiesManager.DB_SERVERHOST_KEY),
                "-p", PropertiesManager.get(PropertiesManager.DB_SERVERPORT_KEY),
                "-U", PropertiesManager.get(PropertiesManager.DB_USERNAME_KEY),
                "-F", "p",
                PropertiesManager.get(PropertiesManager.DB_NAME_KEY)
        };
    }

    public static String getBackupCommandDisplay() {
        String backupFileLocation = ConfigManager.file.getDbBackupRepositoryPath()
                + ConfigManager.system.getTrailingSlash()
                + "BACKUP-"
                + PropertiesManager.get(PropertiesManager.DB_NAME_KEY)
                + "-<timestamp>"
                + ConfigManager.file.getPostgresBackupExtension();

        StringBuilder command = new StringBuilder();
        command.append("\"").append(ConfigManager.file.getDbPostgresProgramPath()).append("\"");
        command.append(" -f ").append("\"").append(backupFileLocation).append("\"");
        command.append(" -h ").append(PropertiesManager.get(PropertiesManager.DB_SERVERHOST_KEY));
        command.append(" -p ").append(PropertiesManager.get(PropertiesManager.DB_SERVERPORT_KEY));
        command.append(" -U ").append(PropertiesManager.get(PropertiesManager.DB_USERNAME_KEY));
        command.append(" -i -F p ");
        command.append(PropertiesManager.get(PropertiesManager.DB_NAME_KEY));

        return command.toString();
    }

    public static String getTitleText(RequestContext requestContext, String titleText) {
        StringBuilder title = new StringBuilder();
        title.append(ConfigManager.system.getCompanyName().isEmpty() ?
                Localizer.getText(requestContext, "common.app.shortName") : ConfigManager.system.getCompanyName());

        if (titleText != null) {
            title.append(" - ");
            title.append(titleText);
        }
        return title.toString();
    }

    public static List<LabelValueBean> getGroupOptions(RequestContext requestContext) throws DatabaseException {
        AdminService adminService = ServiceProvider.getAdminService(requestContext);
        List<AccessGroup> groups = adminService.getGroups(new QueryCriteria());
        List<LabelValueBean> groupIdOptions = new ArrayList<>();
        groupIdOptions.add(new SelectOneLabelValueBean(requestContext, "0"));
        for (AccessGroup group : groups) {
            groupIdOptions.add(new LabelValueBean(group.getName(), String.valueOf(group.getId())));
        }
        return groupIdOptions;
    }

    /**
     * Returns system username, this function also handles users who have been removed.
     * @param request
     * @param username
     * @return
     */
    public static String getSystemUsername(RequestContext requestContext, AccessUser accessUser) {
        if (accessUser == null || accessUser.getId() == 0) {
            return "";

        } else {
            String username = ConfigManager.system.getUsernameDisplay().equals(AdminUtils.USER_USERNAME) ? accessUser.getUsername() : accessUser.getDisplayName();
            return getSystemUsername(requestContext, accessUser.getId(), username);
        }
    }

    public static String getSystemUsername(RequestContext requestContext, Integer userId, String username) {
        if (userId == 0) {
            return "";

        } else {
            // We have user id but not username. This means the user has been removed.
            if (StringUtils.isEmpty(username)) {
                username = Localizer.getText(requestContext, "admin.userDelete.userRemoved");
            }
            return username;
        }
    }

    public static String getUsernameSort() {
        return ConfigManager.system.getUsernameDisplay().equals(AdminUtils.USER_USERNAME) ? AccessUser.USERNAME : AccessUser.DISPLAY_NAME;
    }

    public static Map<Integer, Integer> getObjectTypeMap() {
        return objectTypeMap;
    }

    /**
     * Enabled users sort by display name.
     * @return
     */
    public static List<LabelValueBean> getUserOptions(RequestContext requestContext) throws Exception {
        return getUserOptions(requestContext, null);
    }

    /**
     * Returns a list of enabled users (plus the one specified), sort by display name.
     * @param request
     * @param owner
     * @return
     * @throws Exception
     */
    public static List<LabelValueBean> getUserOptions(RequestContext requestContext, Integer includedUserId) throws Exception {
        // We only want Users whose status is "Enable"
        UserSearch userSearch = new UserSearch();

        if (includedUserId != null) {
            userSearch.put(UserSearch.NON_DISABLED, includedUserId);
        } else {
            userSearch.put(UserSearch.USER_STATUS, AttributeFieldIds.USER_STATUS_ENABLED);
        }

        // Sort by display name
        QueryCriteria query = new QueryCriteria(userSearch);
        query.addSortColumn(AdminQueries.getOrderByColumn(getUsernameSort()));

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // User options
        List<LabelValueBean> userOptions = new ArrayList<>();

        for (AccessUser user : adminService.getUsers(query)) {
            userOptions.add(new LabelValueBean(getSystemUsername(requestContext, user), String.valueOf(user.getId())));
        }
        return userOptions;
    }

    public static String getAttributeGroupKey(RequestContext requestContext, AttributeGroup group) {
        String key = "";

        if (group != null) {
            key = HtmlUtils.encode(group.getName()) + "<a href=\"" + group.getId() + "\"></a>";

        } else if (requestContext != null) {
            key = "<a href=\"0\"></a>" + Localizer.getText(requestContext, "common.template.customFields");
        }
        return key;
    }

    public static String getAttributeGroupKey(AttributeGroup group) {
        return getAttributeGroupKey(null, group);
    }

    public static List<LabelValueBean> getAttributeGroupOptions(RequestContext requestContext, Integer objectTypeId) throws Exception {
        Map<Integer, AttributeGroup> groupMap = new CacheManager(requestContext).getCustomAttrGroupsCache(objectTypeId);

        List<LabelValueBean> attrGroupOptions = new ArrayList<>();
        if (!groupMap.values().isEmpty()) {
            attrGroupOptions.add(new SelectOneLabelValueBean(requestContext, "0"));

            for (AttributeGroup group : groupMap.values()) {
                attrGroupOptions.add(new LabelValueBean(group.getName(), String.valueOf(group.getId())));
            }
        }
        return attrGroupOptions;
    }

    public static String getPermissionLabel(RequestContext requestContext, String permName) {
        StringBuilder accessText = new StringBuilder();
        accessText.append(Localizer.getText(requestContext, "system.permissions." + permName));
        accessText.append(": ");

        String permDesc = Localizer.getText(requestContext, "system.permissions." + permName + ".desc");
        if (permDesc != null) {
            Link link = new Link(requestContext);
            link.setImgSrc(Image.getInstance().getSignInfo());
            link.setImgAltText(permDesc);
            accessText.append(link);
        }
        return accessText.toString();
    }
}
