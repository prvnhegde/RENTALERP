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
package com.kwoksys.biz.system.core;

import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.properties.PropertiesManager;

/**
 * This page sets all the page urls.
 */
public class AppPaths {

    private static final AppPaths INSTANCE = new AppPaths();

    private static final String EXT = ConfigManager.system.getExtension();

    public static String ROOT = "";

    public static String COOKIE_ROOT = "/";
    
    private AppPaths() {}

    public static void init(String root) {
        ROOT = root;
        if (!root.isEmpty()) {
            COOKIE_ROOT = root;
        }
    }

    public static AppPaths getInstance() {
        return INSTANCE;
    }

    // Localization properties files are defined in struts-config.xml's message-resources attribute.
    public static final String BUILD_PROPERTIES = "/build.properties";
    public static final String CACHE_PROPERTIES = "/jcs.properties";
    public static final String APPLICATION_PROPERTIES = "/properties/Application.properties";
    public static final String LOCALIZATION_PROPERTIES = "properties.localization.Localization";

    // Administration module
    public static final String ADMIN_INDEX = "/admin/index" + EXT;
    public static final String ADMIN_DATA_IMPORT_INDEX = "/admin/data-import-index" + EXT;
    public static final String ADMIN_DATA_IMPORT_VALIDATE = "/admin/data-import-validate" + EXT;
    public static final String ADMIN_DATA_IMPORT_EXECUTE = "/admin/data-import-execute" + EXT;
    public static final String ADMIN_DATA_IMPORT_RESULTS = "/admin/data-import-results" + EXT;
    public static final String ADMIN_GROUP_LIST = "/admin/group-list" + EXT;
    public static final String ADMIN_GROUP_DETAIL = "/admin/group-detail" + EXT;
    public static final String ADMIN_GROUP_ADD = "/admin/group-add" + EXT;
    public static final String ADMIN_GROUP_ADD_2 = "/admin/group-add-2" + EXT;
    public static final String ADMIN_GROUP_DELETE = "/admin/group-delete" + EXT;
    public static final String ADMIN_GROUP_DELETE_2 = "/admin/group-delete-2" + EXT;
    public static final String ADMIN_GROUP_EDIT = "/admin/group-edit" + EXT;
    public static final String ADMIN_GROUP_EDIT_2 = "/admin/group-edit-2" + EXT;
    public static final String ADMIN_GROUP_ACCESS = "/admin/group-access" + EXT;
    public static final String ADMIN_GROUP_ACCESS_EDIT = "/admin/group-access-edit" + EXT;
    public static final String ADMIN_GROUP_ACCESS_EDIT_2 = "/admin/group-access-edit-2" + EXT;
    public static final String ADMIN_USER_INDEX = "/admin/user-index" + EXT;
    public static final String ADMIN_USER_LIST = "/admin/user-list" + EXT;
    public static final String ADMIN_USER_LIST_EXPORT = "/admin/user-list-export" + EXT;
    public static final String ADMIN_USER_DETAIL = "/admin/user-detail" + EXT;
    public static final String ADMIN_USER_ACCESS = "/admin/user-access" + EXT;
    public static final String ADMIN_USER_ACCESS_EDIT = "/admin/user-access-edit" + EXT;
    public static final String ADMIN_USER_ACCESS_EDIT_2 = "/admin/user-access-edit-2" + EXT;
    public static final String ADMIN_USER_HARDWARE = "/admin/user-hardware" + EXT;
    public static final String ADMIN_USER_ADD = "/admin/user-add" + EXT;
    public static final String ADMIN_USER_ADD_2 = "/admin/user-add-2" + EXT;
    public static final String ADMIN_USER_EDIT = "/admin/user-edit" + EXT;
    public static final String ADMIN_USER_EDIT_2 = "/admin/user-edit-2" + EXT;
    public static final String ADMIN_USER_DELETE = "/admin/user-delete" + EXT;
    public static final String ADMIN_USER_DELETE_2 = "/admin/user-delete-2" + EXT;
    public static final String ADMIN_USER_LOGOUT = "/admin/user-logout" + EXT;
    public static final String ADMIN_USER_PASSWORD_RESET = "/admin/user-pw-reset" + EXT;
    public static final String ADMIN_USER_PASSWORD_RESET_2 = "/admin/user-pw-reset-2" + EXT;
    public static final String ADMIN_CONFIG = "/admin/config" + EXT;
    public static final String ADMIN_CONFIG_WRITE = "/admin/config-write" + EXT;
    public static final String ADMIN_ATTRIBUTE_GROUP_ADD = "/admin/attribute-group-add" + EXT;
    public static final String ADMIN_ATTRIBUTE_GROUP_ADD_2 = "/admin/attribute-group-add-2" + EXT;
    public static final String ADMIN_ATTRIBUTE_GROUP_EDIT = "/admin/attribute-group-edit" + EXT;
    public static final String ADMIN_ATTRIBUTE_GROUP_EDIT_2 = "/admin/attribute-group-edit-2" + EXT;
    public static final String ADMIN_ATTRIBUTE_GROUP_DELETE = "/admin/attribute-group-delete" + EXT;
    public static final String ADMIN_ATTRIBUTE_GROUP_DELETE_2 = "/admin/attribute-group-delete-2" + EXT;
    public static final String ADMIN_ATTRIBUTE_LIST = "/admin/attribute-list" + EXT;
    public static final String ADMIN_ATTRIBUTE_DETAIL = "/admin/attribute-detail" + EXT;
    public static final String ADMIN_ATTRIBUTE_EDIT = "/admin/attribute-edit" + EXT;
    public static final String ADMIN_ATTRIBUTE_EDIT_2 = "/admin/attribute-edit-2" + EXT;
    public static final String ADMIN_ATTR_FIELD_EDIT = "/admin/attribute-field-edit" + EXT;
    public static final String ADMIN_ATTR_FIELD_EDIT_2 = "/admin/attribute-field-edit-2" + EXT;
    public static final String ADMIN_ATTR_FIELD_ADD = "/admin/attribute-field-add" + EXT;
    public static final String ADMIN_ATTR_FIELD_ADD_2 = "/admin/attribute-field-add-2" + EXT;
    public static final String ADMIN_CUSTOM_ATTR_LIST = "/admin/cust-attr" + EXT;
    public static final String ADMIN_CUSTOM_ATTR_DETAIL = "/admin/cust-attr-detail" + EXT;
    public static final String ADMIN_CUSTOM_ATTR_ADD = "/admin/cust-attr-add" + EXT;
    public static final String ADMIN_CUSTOM_ATTR_ADD_2 = "/admin/cust-attr-add-2" + EXT;
    public static final String ADMIN_CUSTOM_ATTR_EDIT = "/admin/cust-attr-edit" + EXT;
    public static final String ADMIN_CUSTOM_ATTR_EDIT_2 = "/admin/cust-attr-edit-2" + EXT;
    public static final String ADMIN_CUSTOM_ATTR_DELETE = "/admin/cust-attr-delete" + EXT;
    public static final String ADMIN_CUSTOM_ATTR_DELETE_2 = "/admin/cust-attr-delete-2" + EXT;

    // Auth module
    public static final String AUTH_LOGOUT = "/auth/logout" + EXT;
    public static final String AUTH_VERIFY = "/auth/verify-password" + EXT;

    // Contacts module
    public static final String CONTACTS_INDEX = "/contacts/index" + EXT;
    public static final String CONTACTS_COMPANY_LIST = "/contacts/company-list" + EXT;
    public static final String CONTACTS_COMPANY_LIST_EXPORT = "/contacts/company-list-export" + EXT;
    public static final String CONTACTS_COMPANY_DETAIL = "/contacts/company-detail" + EXT;
    public static final String CONTACTS_COMPANY_CONTACT_ADD = "/contacts/company-contact-add" + EXT;
    public static final String CONTACTS_COMPANY_CONTACT_ADD_2 = "/contacts/company-contact-add-2" + EXT;
    public static final String CONTACTS_COMPANY_CONTACT_EDIT = "/contacts/company-contact-edit" + EXT;
    public static final String CONTACTS_COMPANY_CONTACT_EDIT_2 = "/contacts/company-contact-edit-2" + EXT;
    public static final String CONTACTS_COMPANY_CONTACT_DELETE = "/contacts/company-contact-delete" + EXT;
    public static final String CONTACTS_COMPANY_CONTACT_DELETE_2 = "/contacts/company-contact-delete-2" + EXT;
    public static final String CONTACTS_COMPANY_CONTACT = "/contacts/company-contact" + EXT;
    public static final String CONTACTS_COMPANY_CONTACT_EXPORT = "/contacts/company-contact-export" + EXT;
    public static final String CONTACTS_COMPANY_CONTRACTS = "/contacts/company-contracts" + EXT;
    public static final String CONTACTS_COMPANY_ADD = "/contacts/company-add" + EXT;
    public static final String CONTACTS_COMPANY_ADD_2 = "/contacts/company-add-2" + EXT;
    public static final String CONTACTS_COMPANY_EDIT = "/contacts/company-edit" + EXT;
    public static final String CONTACTS_COMPANY_EDIT_2 = "/contacts/company-edit-2" + EXT;
    public static final String CONTACTS_COMPANY_DELETE = "/contacts/company-delete" + EXT;
    public static final String CONTACTS_COMPANY_DELETE_2 = "/contacts/company-delete-2" + EXT;
    public static final String CONTACTS_COMPANY_NOTE = "/contacts/company-note" + EXT;
    public static final String CONTACTS_COMPANY_NOTE_ADD = "/contacts/company-note-add" + EXT;
    public static final String CONTACTS_COMPANY_NOTE_ADD_2 = "/contacts/company-note-add-2" + EXT;
    public static final String CONTACTS_COMPANY_BOOKMARK = "/contacts/company-bookmark" + EXT;
    public static final String CONTACTS_COMPANY_BOOKMARK_ADD = "/contacts/company-bookmark-add" + EXT;
    public static final String CONTACTS_COMPANY_BOOKMARK_ADD_2 = "/contacts/company-bookmark-add-2" + EXT;
    public static final String CONTACTS_COMPANY_BOOKMARK_EDIT = "/contacts/company-bookmark-edit" + EXT;
    public static final String CONTACTS_COMPANY_BOOKMARK_EDIT_2 = "/contacts/company-bookmark-edit-2" + EXT;
    public static final String CONTACTS_COMPANY_BOOKMARK_DELETE_2 = "/contacts/company-bookmark-delete-2" + EXT;
    public static final String CONTACTS_COMPANY_FILES = "/contacts/company-file" + EXT;
    public static final String CONTACTS_COMPANY_FILE_ADD = "/contacts/company-file-add" + EXT;
    public static final String CONTACTS_COMPANY_FILE_ADD_2 = "/contacts/company-file-add-2" + EXT;
    public static final String CONTACTS_COMPANY_FILE_EDIT = "/contacts/company-file-edit" + EXT;
    public static final String CONTACTS_COMPANY_FILE_EDIT_2 = "/contacts/company-file-edit-2" + EXT;
    public static final String CONTACTS_COMPANY_FILE_DELETE = "/contacts/company-file-delete" + EXT;
    public static final String CONTACTS_COMPANY_FILE_DELETE_2 = "/contacts/company-file-delete-2" + EXT;
    public static final String CONTACTS_COMPANY_FILE_DOWNLOAD = "/contacts/company-file-download" + EXT;
    public static final String CONTACTS_COMPANY_ISSUES = "/contacts/company-issue" + EXT;
    public static final String CONTACTS_COMPANY_ISSUE_ADD = "/contacts/company-issue-add" + EXT;
    public static final String CONTACTS_COMPANY_ISSUE_ADD_2 = "/contacts/company-issue-add-2" + EXT;
    public static final String CONTACTS_COMPANY_ISSUE_REMOVE_2 = "/contacts/company-issue-remove-2" + EXT;
    public static final String CONTACTS_CONTACT_LIST = "/contacts/contact-list" + EXT;
    public static final String CONTACTS_CONTACT_DETAIL = "/contacts/contact-detail" + EXT;
    public static final String CONTACTS_CONTACT_EDIT = "/contacts/contact-edit" + EXT;
    public static final String CONTACTS_CONTACT_EDIT_2 = "/contacts/contact-edit-2" + EXT;
    public static final String CONTACTS_CONTACT_ADD = "/contacts/contact-add" + EXT;
    public static final String CONTACTS_CONTACT_ADD_2 = "/contacts/contact-add-2" + EXT;
    public static final String CONTACTS_CONTACT_DELETE = "/contacts/contact-delete" + EXT;
    public static final String CONTACTS_CONTACT_DELETE_2 = "/contacts/contact-delete-2" + EXT;
    public static final String CONTACTS_CONTACT_EXPORT_VCARD = "/contacts/contact-detail-vcard" + EXT;

    // Contracts module
    public static final String CONTRACTS_INDEX = "/contracts/index" + EXT;
    public static final String CONTRACTS_LIST = "/contracts/list" + EXT;
    public static final String CONTRACTS_DETAIL = "/contracts/detail" + EXT;
    public static final String CONTRACTS_ADD = "/contracts/add" + EXT;
    public static final String CONTRACTS_ADD_2 = "/contracts/add-2" + EXT;
    public static final String CONTRACTS_EDIT = "/contracts/edit" + EXT;
    public static final String CONTRACTS_EDIT_2 = "/contracts/edit-2" + EXT;
    public static final String CONTRACTS_DELETE = "/contracts/delete" + EXT;
    public static final String CONTRACTS_DELETE_2 = "/contracts/delete-2" + EXT;
    public static final String CONTRACTS_CONTACTS = "/contracts/contacts" + EXT;
    public static final String CONTRACTS_CONTACT_ADD = "/contracts/contact-add" + EXT;
    public static final String CONTRACTS_CONTACT_ADD_2 = "/contracts/contact-add-2" + EXT;
    public static final String CONTRACTS_CONTACT_REMOVE_2 = "/contracts/contact-remove-2" + EXT;
    public static final String CONTRACTS_FILE_ADD = "/contracts/file-add" + EXT;
    public static final String CONTRACTS_FILE_ADD_2 = "/contracts/file-add-2" + EXT;
    public static final String CONTRACTS_FILE_EDIT = "/contracts/file-edit" + EXT;
    public static final String CONTRACTS_FILE_EDIT_2 = "/contracts/file-edit-2" + EXT;
    public static final String CONTRACTS_FILE_DELETE = "/contracts/file-delete" + EXT;
    public static final String CONTRACTS_FILE_DELETE_2 = "/contracts/file-delete-2" + EXT;
    public static final String CONTRACTS_FILE_DOWNLOAD = "/contracts/file-download" + EXT;
    public static final String CONTRACTS_ITEMS = "/contracts/item-list" + EXT;
    public static final String CONTRACTS_HARDWARE_ADD = "/contracts/hardware-add" + EXT;
    public static final String CONTRACTS_HARDWARE_ADD_2 = "/contracts/hardware-add-2" + EXT;
    public static final String CONTRACTS_HARDWARE_REMOVE_2 = "/contracts/hardware-remove-2" + EXT;
    public static final String CONTRACTS_SOFTWARE_ADD = "/contracts/software-add" + EXT;
    public static final String CONTRACTS_SOFTWARE_ADD_2 = "/contracts/software-add-2" + EXT;
    public static final String CONTRACTS_SOFTWARE_REMOVE_2 = "/contracts/software-remove-2" + EXT;

    // Home module
    public static final String HOME_INDEX = "/home/index" + EXT;
    public static final String HOME_UNAUTHORIZED = "/home/unauthorized" + EXT;
    public static final String HOME_FORBIDDEN = "/home/forbidden" + EXT;
    public static final String HOME_FILE_NOT_FOUND = "/home/page-not-found" + EXT;
    public static final String HOME_OBJECT_NOT_FOUND = "/home/object-not-found" + EXT;
    public static final String HOME_NON_SUPPORTED_BROWSER = "/home/non-supported-browser" + EXT;

    // IssuePlugin module
    public static final String ISSUE_PLUGIN_ADD = "/issue-plugin/issue-add" + EXT;
    public static final String ISSUE_PLUGIN_ADD_2 = "/issue-plugin/issue-add-2" + EXT;
    public static final String ISSUE_PLUGIN_ADD_3 = "/issue-plugin/issue-add-3" + EXT;
    public static final String ISSUE_PLUGIN_LEGEND_DETAIL = "/issue-plugin/issue-legend-detail" + EXT;

    // Issues module
    public static final String ISSUES_INDEX = "/issues/index" + EXT;
    public static final String ISSUES_LIST = "/issues/list" + EXT;
    public static final String ISSUES_LIST_EXPORT = "/issues/list-export" + EXT;
    public static final String ISSUES_DETAIL = "/issues/detail" + EXT;
    public static final String ISSUES_ADD = "/issues/add" + EXT;
    public static final String ISSUES_ADD_2 = "/issues/add-2" + EXT;
    public static final String ISSUES_DELETE = "/issues/issue-delete" + EXT;
    public static final String ISSUES_DELETE_2 = "/issues/issue-delete-2" + EXT;
    public static final String ISSUES_EDIT = "/issues/edit" + EXT;
    public static final String ISSUES_EDIT_2 = "/issues/edit-2" + EXT;
    public static final String ISSUES_FILE_ADD = "/issues/file-add" + EXT;
    public static final String ISSUES_FILE_ADD_2 = "/issues/file-add-2" + EXT;
    public static final String ISSUES_FILE_DOWNLOAD = "/issues/file-download" + EXT;
    public static final String ISSUES_RELATIONSHIP = "/issues/issue-relationship" + EXT;

    // Hardware module
    public static final String IT_MGMT_AJAX_GET_SOFTWARE_BY_MAKER = "/IT/ajax-get-software-by-maker" + EXT;
    public static final String IT_MGMT_AJAX_GET_HARDWARE_DETAIL = "/IT/ajax-get-hardware-detail" + EXT;
    public static final String IT_MGMT_AJAX_HARDWARE_ASSIGN_LICENSE = "/IT/ajax-get-license-by-software" + EXT;
    public static final String HARDWARE_AJAX_COMP_CUSTOM_FIELDS = "/hardware/ajax-get-component-custom-fields" + EXT;

    public static final String HARDWARE_INDEX = "/hardware/index" + EXT;
    public static final String HARDWARE_LIST = "/hardware/list" + EXT;
    public static final String HARDWARE_LIST_EXPORT = "/hardware/list-export" + EXT;
    public static final String HARDWARE_DETAIL = "/hardware/detail" + EXT;
    public static final String HARDWARE_ADD = "/hardware/add" + EXT;
    public static final String HARDWARE_ADD_2 = "/hardware/add-2" + EXT;
    public static final String HARDWARE_EDIT = "/hardware/edit" + EXT;
    public static final String HARDWARE_EDIT_2 = "/hardware/edit-2" + EXT;
    public static final String HARDWARE_DELETE = "/hardware/delete" + EXT;
    public static final String HARDWARE_DELETE_2 = "/hardware/delete-2" + EXT;
    public static final String HARDWARE_LICENSE_REMOVE_2 = "/hardware/license-remove-2" + EXT;
    public static final String HARDWARE_LICENSE_ADD_2 = "/hardware/license-add-2" + EXT;
    public static final String HARDWARE_COMP = "/hardware/comp" + EXT;
    public static final String HARDWARE_COMP_ADD = "/hardware/comp-add" + EXT;
    public static final String HARDWARE_COMP_ADD_2 = "/hardware/comp-add-2" + EXT;
    public static final String HARDWARE_COMP_EDIT = "/hardware/comp-edit" + EXT;
    public static final String HARDWARE_COMP_EDIT_2 = "/hardware/comp-edit-2" + EXT;
    public static final String HARDWARE_COMP_DELETE_2 = "/hardware/comp-delete-2" + EXT;
    public static final String HARDWARE_CONTACTS = "/hardware/contacts" + EXT;
    public static final String HARDWARE_CONTACT_ADD = "/hardware/contact-add" + EXT;
    public static final String HARDWARE_CONTACT_ADD_2 = "/hardware/contact-add-2" + EXT;
    public static final String HARDWARE_CONTACT_REMOVE = "/hardware/contact-remove" + EXT;
    public static final String HARDWARE_FILE = "/hardware/file" + EXT;
    public static final String HARDWARE_FILE_ADD = "/hardware/file-add" + EXT;
    public static final String HARDWARE_FILE_ADD_2 = "/hardware/file-add-2" + EXT;
    public static final String HARDWARE_FILE_EDIT = "/hardware/file-edit" + EXT;
    public static final String HARDWARE_FILE_EDIT_2 = "/hardware/file-edit-2" + EXT;
    public static final String HARDWARE_FILE_DELETE = "/hardware/file-delete" + EXT;
    public static final String HARDWARE_FILE_DELETE_2 = "/hardware/file-delete-2" + EXT;
    public static final String HARDWARE_FILE_DOWNLOAD = "/hardware/file-download" + EXT;
    public static final String HARDWARE_ISSUE = "/hardware/issue" + EXT;
    public static final String HARDWARE_ISSUE_ADD = "/hardware/issue-add" + EXT;
    public static final String HARDWARE_ISSUE_ADD_2 = "/hardware/issue-add-2" + EXT;
    public static final String HARDWARE_ISSUE_REMOVE_2 = "/hardware/issue-remove-2" + EXT;
    public static final String HARDWARE_MEMBER = "/hardware/hardware-member" + EXT;
    public static final String HARDWARE_MEMBER_ADD = "/hardware/hardware-member-add" + EXT;
    public static final String HARDWARE_MEMBER_ADD_2 = "/hardware/hardware-member-add-2" + EXT;
    public static final String HARDWARE_MEMBER_REMOVE_2 = "/hardware/hardware-member-remove" + EXT;

    // Knowledge Base
    public static final String KB_INDEX = "/kb/index" + EXT;
    public static final String KB_ARTICLE_SEARCH = "/kb/article-search" + EXT;
    public static final String KB_ARTICLE_LIST = "/kb/article-list" + EXT;
    public static final String KB_ARTICLE_DETAIL = "/kb/article-detail" + EXT;
    public static final String KB_ARTICLE_PRINT = "/kb/article-print" + EXT;
    public static final String KB_ARTICLE_ADD = "/kb/article-add" + EXT;
    public static final String KB_ARTICLE_ADD_2 = "/kb/article-add-2" + EXT;
    public static final String KB_ARTICLE_EDIT = "/kb/article-edit" + EXT;
    public static final String KB_ARTICLE_EDIT_2 = "/kb/article-edit-2" + EXT;
    public static final String KB_ARTICLE_DELETE = "/kb/article-delete" + EXT;
    public static final String KB_ARTICLE_DELETE_2 = "/kb/article-delete-2" + EXT;
    public static final String KB_ARTICLE_FILE_DOWNLOAD = "/kb/article-file-download" + EXT;
    // Used by KB articles
    public static final String KB_ARTICLE_FILE_DOWNLOAD_RELATIVE = "article-file-download" + EXT;
    public static final String KB_ARTICLE_FILE_ADD = "/kb/article-file-add" + EXT;
    public static final String KB_ARTICLE_FILE_ADD_2 = "/kb/article-file-add-2" + EXT;
    public static final String KB_ARTICLE_FILE_DELETE = "/kb/article-file-delete" + EXT;
    public static final String KB_ARTICLE_FILE_DELETE_2 = "/kb/article-file-delete-2" + EXT;
    public static final String KB_CATEGORY_LIST = "/kb/category-list" + EXT;
    public static final String KB_CATEGORY_ADD = "/kb/category-add" + EXT;
    public static final String KB_CATEGORY_ADD_2 = "/kb/category-add-2" + EXT;
    public static final String KB_CATEGORY_EDIT = "/kb/category-edit" + EXT;
    public static final String KB_CATEGORY_EDIT_2 = "/kb/category-edit-2" + EXT;

    // Software module
    public static final String SOFTWARE_AJAX_DETAILS = "/software/ajax-get-license-details" + EXT;
    public static final String SOFTWARE_AJAX_CUSTOM_FIELDS = "/software/ajax-get-license-custom-fields" + EXT;
    public static final String SOFTWARE_INDEX = "/software/index" + EXT;
    public static final String SOFTWARE_LIST = "/software/list" + EXT;
    public static final String SOFTWARE_LIST_EXPORT = "/software/list-export" + EXT;
    public static final String SOFTWARE_DETAIL = "/software/detail" + EXT;
    public static final String SOFTWARE_EDIT = "/software/edit" + EXT;
    public static final String SOFTWARE_EDIT_2 = "/software/edit-2" + EXT;
    public static final String SOFTWARE_ADD = "/software/add" + EXT;
    public static final String SOFTWARE_ADD_2 = "/software/add-2" + EXT;
    public static final String SOFTWARE_DELETE = "/software/delete" + EXT;
    public static final String SOFTWARE_DELETE_2 = "/software/delete-2" + EXT;
    public static final String SOFTWARE_LICENSE_ADD = "/software/license-add" + EXT;
    public static final String SOFTWARE_LICENSE_ADD_2 = "/software/license-add-2" + EXT;
    public static final String SOFTWARE_LICENSE_EDIT = "/software/license-edit" + EXT;
    public static final String SOFTWARE_LICENSE_EDIT_2 = "/software/license-edit-2" + EXT;
    public static final String SOFTWARE_LICENSE_DELETE_2 = "/software/license-delete-2" + EXT;
    public static final String SOFTWARE_BOOKMARK = "/software/bookmark" + EXT;
    public static final String SOFTWARE_BOOKMARK_ADD = "/software/bookmark-add" + EXT;
    public static final String SOFTWARE_BOOKMARK_ADD_2 = "/software/bookmark-add-2" + EXT;
    public static final String SOFTWARE_BOOKMARK_EDIT = "/software/bookmark-edit" + EXT;
    public static final String SOFTWARE_BOOKMARK_EDIT_2 = "/software/bookmark-edit-2" + EXT;
    public static final String SOFTWARE_BOOKMARK_DELETE_2 = "/software/bookmark-delete-2" + EXT;
    public static final String SOFTWARE_CONTACTS = "/software/contacts" + EXT;
    public static final String SOFTWARE_CONTACT_ADD = "/software/contact-add" + EXT;
    public static final String SOFTWARE_CONTACT_ADD_2 = "/software/contact-add-2" + EXT;
    public static final String SOFTWARE_CONTACT_REMOVE_2 = "/software/contact-remove-2" + EXT;
    public static final String SOFTWARE_FILE = "/software/file" + EXT;
    public static final String SOFTWARE_FILE_DOWNLOAD = "/software/file-download" + EXT;
    public static final String SOFTWARE_FILE_ADD = "/software/file-add" + EXT;
    public static final String SOFTWARE_FILE_ADD_2 = "/software/file-add-2" + EXT;
    public static final String SOFTWARE_FILE_EDIT = "/software/file-edit" + EXT;
    public static final String SOFTWARE_FILE_EDIT_2 = "/software/file-edit-2" + EXT;
    public static final String SOFTWARE_FILE_DELETE = "/software/file-delete" + EXT;
    public static final String SOFTWARE_FILE_DELETE_2 = "/software/file-delete-2" + EXT;
    public static final String SOFTWARE_ISSUE = "/software/issue" + EXT;
    public static final String SOFTWARE_ISSUE_ADD = "/software/issue-add" + EXT;
    public static final String SOFTWARE_ISSUE_ADD_2 = "/software/issue-add-2" + EXT;
    public static final String SOFTWARE_ISSUE_REMOVE_2 = "/software/issue-remove-2" + EXT;

    // Blog module
    public static final String BLOG_INDEX = "/blogs/index" + EXT;
    public static final String BLOG_POST_LIST = "/blogs/post-list" + EXT;
    public static final String BLOG_POST_LIST_RSS = "/blogs/post-list-rss" + EXT;
    public static final String BLOG_POST_DETAIL = "/blogs/post-detail" + EXT;
    public static final String BLOG_POST_ADD = "/blogs/post-add" + EXT;
    public static final String BLOG_POST_ADD_2 = "/blogs/post-add-2" + EXT;
    public static final String BLOG_POST_DELETE = "/blogs/post-delete" + EXT;
    public static final String BLOG_POST_DELETE_2 = "/blogs/post-delete-2" + EXT;
    public static final String BLOG_POST_EDIT = "/blogs/post-edit" + EXT;
    public static final String BLOG_POST_EDIT_2 = "/blogs/post-edit-2" + EXT;
    public static final String BLOG_POST_COMMENT_ADD_2 = "/blogs/post-comment-add-2" + EXT;
    public static final String BLOG_CATEGORY_LIST = "/blogs/category-list" + EXT;
    public static final String BLOG_CATEGORY_ADD = "/blogs/category-add" + EXT;
    public static final String BLOG_CATEGORY_ADD_2 = "/blogs/category-add-2" + EXT;
    public static final String BLOG_CATEGORY_EDIT = "/blogs/category-edit" + EXT;
    public static final String BLOG_CATEGORY_EDIT_2 = "/blogs/category-edit-2" + EXT;

    // RSS module
    public static final String RSS_FEED_LIST = "/rss/feed-list" + EXT;
    public static final String RSS_FEED_LIST_TITLES = "/rss/feed-list-titles" + EXT;
    public static final String RSS_FEED_LIST_ITEMS = "/rss/feed-list-items" + EXT;
    public static final String RSS_FEED_ADD = "/rss/feed-add" + EXT;
    public static final String RSS_FEED_ADD_2 = "/rss/feed-add-2" + EXT;
    public static final String RSS_FEED_EDIT = "/rss/feed-edit" + EXT;
    public static final String RSS_FEED_EDIT_2 = "/rss/feed-edit-2" + EXT;
    public static final String RSS_FEED_DELETE = "/rss/feed-delete" + EXT;
    public static final String RSS_FEED_DELETE_2 = "/rss/feed-delete-2" + EXT;

    // Portal module
    public static final String PORTAL_SITE_LIST_INDEX = "/portal/site-list-index" + EXT;
    public static final String PORTAL_SITE_LIST_RSS = "/portal/site-list-rss" + EXT;
    public static final String PORTAL_SITE_LIST = "/portal/site-list" + EXT;
    public static final String PORTAL_SITE_DETAIL = "/portal/site-detail" + EXT;
    public static final String PORTAL_SITE_ADD = "/portal/site-add" + EXT;
    public static final String PORTAL_SITE_ADD_2 = "/portal/site-add-2" + EXT;
    public static final String PORTAL_SITE_EDIT = "/portal/site-edit" + EXT;
    public static final String PORTAL_SITE_EDIT_2 = "/portal/site-edit-2" + EXT;
    public static final String PORTAL_SITE_DELETE = "/portal/site-delete" + EXT;
    public static final String PORTAL_SITE_DELETE_2 = "/portal/site-delete-2" + EXT;
    public static final String PORTAL_CATEGORY_LIST = "/portal/site-category-list" + EXT;
    public static final String PORTAL_CATEGORY_ADD = "/portal/site-category-add" + EXT;
    public static final String PORTAL_CATEGORY_ADD_2 = "/portal/site-category-add-2" + EXT;
    public static final String PORTAL_CATEGORY_EDIT = "/portal/site-category-edit" + EXT;
    public static final String PORTAL_CATEGORY_EDIT_2 = "/portal/site-category-edit-2" + EXT;

    // User Preference module
    public static final String USER_PREF_INDEX = "/user-preference/index" + EXT;
    public static final String USER_PREF_PASSWORD_EDIT = "/user-preference/password-edit" + EXT;
    public static final String USER_PREF_PASSWORD_EDIT_2 = "/user-preference/password-edit-2" + EXT;
    public static final String USER_PREF_CONTACT_EDIT = "/user-preference/contact-edit" + EXT;
    public static final String USER_PREF_CONTACT_EDIT_2 = "/user-preference/contact-edit-2" + EXT;

    // Reports module
    public static final String REPORTS_TYPE_SELECT = "/reports/report-type-select" + EXT;
    public static final String REPORTS_SEARCH_CRITERIA = "/reports/report-search-criteria" + EXT;
    public static final String REPORTS_HARDWARE_SEARCH = "/reports/report-hardware" + EXT;
    public static final String REPORTS_ISSUE_SEARCH = "/reports/report-issue" + EXT;
    public static final String REPORTS_SOFTWARE_SEARCH = "/reports/report-software" + EXT;
    public static final String REPORTS_SOFTWARE_USAGE_SEARCH = "/reports/report-software-usage" + EXT;
    public static final String REPORTS_CONTACT = "/reports/report-contact" + EXT;
    public static final String REPORTS_CONTRACT_SEARCH = "/reports/report-contract" + EXT;
    public static final String REPORTS_RESULTS_EXPORT = "/reports/report-results-export" + EXT;

    // External pages
    public static final String SITE = "http://www.kwoksys.com";
    public static final String SITE_DOCUMENTATION = "http://www.kwoksys.com/wiki/index.php";
    public static final String SITE_SURVEY = "http://www.kwoksys.com/pages/usage-survey.php";
    public static final String SITE_COMPARE_EDITIONS = "http://www.kwoksys.com/pages/compare-editions.php";
    public static final String SITE_SYS_REQUIREMENTS = "http://www.kwoksys.com/pages/system-requirements.php";

    public static final String ADMIN_DATA_IMPORT_HARDWARE_SAMPLE_FILE = "/common/kwok/imports/sample-hardware-import.csv";
    public static final String ADMIN_DATA_IMPORT_USER_SAMPLE_FILE = "/common/kwok/imports/sample-user-import.csv";
    
    
    // New links
    public static final String HARDWARE_RENTAL = "/hardware/rental" + EXT;
    public static final String HARDWARE_RENTAL_LIST = "/hardwarerental/list" + EXT;
    public static final String HARDWARE_RETURN_LIST = "/hardwarereturn/list" + EXT;
    public static final String HARDWARE_RETURN = "/hardwarereturn/return" + EXT;
    
    
    


    public String getCookieRoot() {
        return COOKIE_ROOT;
    }

    public String getRoot() {
        return ROOT;
    }

    public String getKwokJs() {
        return ROOT + "/common/kwok/kwok.js?v=" + PropertiesManager.get(PropertiesManager.BUILD_DATE);
    }

    public String getAppJs() {
        return ROOT + "/common/kwok/app.js?v=" + PropertiesManager.get(PropertiesManager.BUILD_DATE);
    }

    public String getDefaultStyle() {
        return ROOT + "/common/kwok/style.css?v=" + PropertiesManager.get(PropertiesManager.BUILD_DATE);
    }

    // Theme
    public String getThemeCss(String sessionTheme) {
        return ROOT + "/common/kwok/themes/" + sessionTheme + "/style.css?v=" + PropertiesManager.get(PropertiesManager.BUILD_DATE);
    }

    public String getMaterialDesignCss() {
        return ROOT + "/common/material-design/material-icons.css";
    }
    
    public String getChartJs() {
        return ROOT + "/common/chart.js-1.0.2/Chart.js";
    }

    public String getJqueryUiBase() {
        return ROOT + "/common/jquery-ui-1.12.1.custom";
    }

    public String getCkeditor() {
        return ROOT + "/common/ckeditor-4.6.2-customized/ckeditor.js";
    }
    
    public String getTooltipsterBase() {
        return ROOT + "/common/tooltipster";
    }

    public String getSiteVersion() {
        return "http://www.kwoksys.com/pages/current-version.xml";
    }

    public String getSiteDownload() {
        return "http://www.kwoksys.com/download";
    }

    public String getSponorAd() {
        return "http://www.kwoksys.com/pages/sponsor-ads.php?version=" + PropertiesManager.getPatchVersion();
    }

    public String getSiteLicenseKeys() {
        return "http://www.kwoksys.com/pages/license-keys.php";
    }

    public String getSiteDocInputMask() {
        return "http://www.kwoksys.com/documentation/Custom_Field_Input_Mask.php";
    }
}
