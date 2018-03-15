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

import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeField;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.files.core.FileTypeHelper;
import com.kwoksys.biz.portal.dto.Site;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.ui.Link;

/**
 * Links.
 */
public class Links {

    public static Link getCancelLink(RequestContext requestContext, String cancelAction) {
        return new Link(requestContext).setAjaxPath(cancelAction).setStyleClass("cancel").setTitleKey("form.button.cancel");
    }

    public static Link getAdminHomeLink(RequestContext requestContext) throws Exception {
        AccessUser accessUser = requestContext.getUser();

        if (Access.hasPermission(accessUser, AppPaths.ADMIN_INDEX)) {
            return new Link(requestContext).setAjaxPath(AppPaths.ADMIN_INDEX).setTitleKey("admin.index.title");
        } else {
            return null;
        }
    }

    public static Link getSystemSettingsImageLink(RequestContext requestContext) {
        Link link = new Link(requestContext);
        link.setImgSrc(Image.getInstance().getAdminSystemIcon());
        link.setTitleKey("admin.configHeader.system_info");
        link.setAjaxPath(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_SYSTEM_INFO_CMD);
        return link;
    }

    public static Link getCompanyDetailsLink(RequestContext requestContext, String companyName, Integer companyId) throws DatabaseException {
        Link link = new Link(requestContext);
        link.setTitle(companyName);
        if (Access.hasPermission(requestContext.getUser(), AppPaths.CONTACTS_COMPANY_DETAIL)) {
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + companyId);
        }
        return link;
    }

    public static Link getCompanyDetailsLink(RequestContext requestContext, boolean hasCompoanyDetailsAccess, String companyName, Integer companyId) {
        Link link = new Link(requestContext);
        link.setTitle(companyName);
        if (hasCompoanyDetailsAccess) {
            link.setAjaxPath(AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + companyId);
        }
        return link;
    }

    public static Link getAttrFieldIcon(RequestContext requestContext, AttributeField attrField) {
        Link link = new Link(requestContext);
        if (attrField != null) {
            link.setTitle(AttributeManager.getAttrFieldName(requestContext, attrField));
            if (attrField.isSystemIcon()) {
                link.setAppImgSrc(attrField.getIconPath());
            } else {
                link.setImgSrc(attrField.getIconPath());
            }
            link.setImgSize(16, 16);
        }
        return link;
    }

    public static Link getFileIconLink(RequestContext requestContext, boolean hasPermission, String fileName, String downloadPath) {
        Link link = new Link(requestContext);
        link.setTitle(fileName);
        link.setImgSrc(Image.getInstance().getFileImageDirIcon() + FileTypeHelper.getExtImage(fileName));
        if (hasPermission) {
            link.setExportPath(downloadPath);
        }
        return link;
    }

    public static Link getUserIconLink(RequestContext requestContext, AccessUser accessUser, boolean hasPermission, boolean showIcon) {
        Link link = new Link(requestContext);

        if (!accessUser.getId().equals(0)) {
            // If username is empty, chances are the user has been deleted.
            if (hasPermission && accessUser.getUsername() != null && !accessUser.getUsername().isEmpty()) {
                link.setAjaxPath(AppPaths.ADMIN_USER_DETAIL + "?userId=" + accessUser.getId());
            }

            link.setTitle(AdminUtils.getSystemUsername(requestContext, accessUser));
            if (showIcon) {
                link.setImgSrc(Image.getInstance().getUserIcon());
            }
        }
        return link;
    }

    public static Link getHelpIconLink(RequestContext requestContext, String messageKey) {
        Link link = new Link(requestContext);
        link.setImgAltKey(messageKey);
        link.setImgSrc(Image.getInstance().getSignHelp());
        link.setImgStyleClass("tooltip");
        return link;
    }

    public static Link getPortalSiteLink(RequestContext requestContext, Site site) {
        Link link = new Link(requestContext);
        link.setTitle(site.getName());

        if (site.getSupportIframe() == 1) {
            link.setPath(site.getPath());
            link.setOnclick("Js.Display.toggle('iframeDiv');Js.Element.setSrc('iframe', '" + site.getPath() + "');return false;");
        } else {
            link.setExternalPath(site.getPath());
            link.setImgSrc(Image.getInstance().getExternalPopupIcon());
            link.setImgAltKey("common.image.externalPopup");
            link.setImgAlignRight();
        }
        return link;
    }
}
