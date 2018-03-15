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

import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;

/**
 * Images
 */
public class Image {
    private static final Image INSTANCE = new Image(AppPaths.ROOT);

    private String rootPath = "";

    private Image(String rootPath) {
        this.rootPath = rootPath;
    }

    public static Image getInstance() {
        return INSTANCE;
    }

    public String getFavicon() {
        return rootPath + "/favicon.ico";
    }

    public String getAdminIcon() {
        return "<i class=\"material-icons md-default\">settings</i>";
    }

    public String getAdminDataImport() {
        return rootPath + "/common/silkicons/data_import.png";
    }

    public String getAdiminDocIcon() {
        return rootPath + "/common/silkicons/admin_documentation.png";
    }

    public String getAdminAppConfIcon() {
        return rootPath + "/common/silkicons/application_config.png";
    }

    public String getAdminLookFeelIcon() {
        return rootPath + "/common/wikimedia-commons/looknfeel.png";
    }

    public String getAdminLocalizationReloadIcon() {
        return rootPath + "/common/silkicons/localization_properties_reload.png";
    }

    public String getAdminSecurityIcon() {
        return rootPath + "/common/silkicons/admin_security_settings.png";
    }

    public String getAdminSystemIcon() {
        return rootPath + "/common/silkicons/cog.png";
    }

    public String getAdminCompanyIcon() {
        return rootPath + "/common/silkicons/company.png";
    }

    public String getAdminCustomFieldsIcon() {
        return rootPath + "/common/silkicons/custom_fields.png";
    }

    public String getAdminEmailIcon() {
        return rootPath + "/common/silkicons/email_settings.png";
    }

    public String getAdminFileSettingsIcon() {
        return rootPath + "/common/silkicons/file_settings.png";
    }

    public String getAdminSurveyIcon() {
        return rootPath + "/common/silkicons/usage_survey.png";
    }

    public String getAdminSystemFieldsIcon() {
        return rootPath + "/common/silkicons/system_fields.png";
    }

    public String getAppLogo() {
        return rootPath + "/common/kwok/images/logo.png";
    }

    public String getBlogComment() {
        return "<i class=\"material-icons md-default\" style=\"color:rgb(133, 167, 203)\">chat</i>";
    }

    public String getStatusCheck() {
        return "<i class=\"material-icons md-default\" style=\"color:rgb(110, 181, 88)\">check_circle</i>";
    }

    public String getStatusError() {
        return "<i class=\"material-icons md-default\" style=\"color:rgb(240, 120, 95)\">error</i>";
    }

    public String getCalendar() {
        return rootPath + "/common/silkicons/calendar_view_month.png";
    }

    public String getCsvFileIcon() {
        return rootPath + "/common/silkicons/page_excel.png";
    }

    public String getFileAddIcon() {
        return rootPath + "/common/silkicons/page_add.png";
    }

    public String getFileImageDirIcon() {
        return rootPath + "/common/silkicons/";
    }

    public String getGroupIcon() {
        return rootPath + "/common/silkicons/group.png";
    }

    public String getGroupAddIcon() {
        return rootPath + "/common/silkicons/group_add.png";
    }

    public String getGroupDeleteIcon() {
        return rootPath + "/common/silkicons/group_delete.png";
    }

    public String getGroupEditIcon() {
        return rootPath + "/common/silkicons/group_edit.png";
    }

    public String getSignWarning() {
        return rootPath + "/common/silkicons/error.png";
    }

    public String getSignHelp() {
        return rootPath + "/common/silkicons/help.png";
    }

    public String getSignInfo() {
        return rootPath + "/common/silkicons/information.png";
    }

    public String getLogout() {
        return "<i class=\"material-icons md-default\">exit_to_app</i>";
    }

    public String getMagGlassIcon() {
        return rootPath + "/common/kevin-potts/mag-glass.gif";
    }

    public String getNoteAddIcon() {
        return rootPath + "/common/silkicons/note_add.png";
    }

    public String getPermissionYesIcon() {
        return "<i class=\"material-icons md-default\" style=\"color:rgb(110, 181, 88)\">check_circle</i>";
    }

    public String getPermissionNoIcon() {
        return "<i class=\"material-icons md-default\" style=\"color:rgb(230, 101, 86)\">cancel</i>";
    }

    public String getPopupClose(RequestContext requestContext, String messageKey) {
        return "<i class=\"material-icons md-default\" title=\"" + Localizer.getText(requestContext, messageKey) + "\">close</i>";
    }

    public String getPreference() {
        return "<i class=\"material-icons md-default\">account_box</i>";
    }

    public String getPrintIcon() {
        return "<i class=\"material-icons md-default\">print</i>";
    }
    public String getRssFeedIcon() {
        return rootPath + "/common/silkicons/feed.png";
    }

    public String getRssFeedAddIcon() {
        return rootPath + "/common/silkicons/feed_add.png";
    }

    public String getSortIcon() {
        return rootPath + "/common/kwok/images/sort.gif";
    }

    public String getSortAscIcon() {
        return rootPath + "/common/kwok/images/sort-asc.gif";
    }

    public String getSortDesc() {
        return rootPath + "/common/kwok/images/sort-desc.gif";
    }

    public String getToggleExpandIcon() {
        return rootPath + "/common/kwok/images/toggle-expand-15.png";
    }

    public String getToggleCollapseIcon() {
        return rootPath + "/common/kwok/images/toggle-collapse-15.png";
    }

    public String getExternalPopupIcon() {
        return rootPath + "/common/kevin-potts/external.gif";
    }

    public String getDeleteIcon() {
        return rootPath + "/common/wikimedia-commons/user-trash-48.gif";
    }

    public String getUserIcon() {
        return rootPath + "/common/silkicons/user.png";
    }

    public String getUserAddIcon() {
        return rootPath + "/common/silkicons/user_add.png";
    }

    public String getUserDeleteIcon() {
        return rootPath + "/common/silkicons/user_delete.png";
    }

    public String getUserEditIcon() {
        return rootPath + "/common/silkicons/user_edit.png";
    }

    public String getUserLogoutIcon() {
        return rootPath + "/common/silkicons/user_logout.png";
    }

    public String getVCardIcon() {
        return rootPath + "/common/silkicons/vcard.png";
    }
}
