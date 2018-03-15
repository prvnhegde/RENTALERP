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
package com.kwoksys.framework.ui;

import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;

/**
 * LinkHelper
 */
public class Link {

    private RequestContext requestContext;
    private String name;
    private String title;
    private String path;
    private String id;
    private String materialIcon;
    private boolean imgAlignRight = false;
    private String imgSrc;
    private String imgId;
    private String imgAltText = "";
    private String imgStyleClass;
    private int imgWidth;
    private int imgHeight;
    private String imgOnclick;
    private String target;
    private String styleClass;
    private String onclick;

    public Link(RequestContext requestContext) {
        this.requestContext= requestContext;
    }

    @Deprecated
    public String toString() {
        return getString();
    }

    public String getString() {
        StringBuilder link = new StringBuilder();

        if (!StringUtils.isEmpty(imgSrc)) {
            link.append("<img src=\"").append(imgSrc).append("\" class=\"standard");
            if (imgStyleClass != null) {
                link.append(" ").append(imgStyleClass);
            }
            link.append("\" alt=\"").append(imgAltText).append("\" title=\"").append(imgAltText).append("\"");

            if (imgId != null) {
                link.append(" id=\"").append(imgId).append("\"");
            }

            if (imgWidth != 0 && imgHeight != 0) {
                link.append(" width=\"").append(imgWidth).append("\" height=\"").append(imgHeight).append("\"");
            }
            if (imgOnclick != null) {
                link.append(" onclick=\"" + imgOnclick + "\"");
            }
            link.append(">");
        }

        if (!StringUtils.isEmpty(materialIcon)) {
            if (imgAlignRight) {
                if (title != null) {
                    link.append(title);
                }
                link.append(materialIcon);
            } else {
                link.append(materialIcon);
                if (title != null) {
                    link.append(title);
                }
            }
        } else if (title != null) {
            if (imgAlignRight) {
                link.insert(0, title);
            } else {
                link.append(title);
            }
        }

        if (!StringUtils.isEmpty(path)) {
            StringBuilder sb = new StringBuilder();
            if (target != null) {
                sb.append(" target=\"" + target + "\"");
            }
            if (styleClass != null) {
                sb.append(" class=\"" + styleClass + "\"");
            }
            if (onclick != null) {
                sb.append(" onclick=\"" + onclick + "\"");
            }
            if (!StringUtils.isEmpty(id)) {
                sb.append(" id=\"" + id + "\"");
            }
            link.insert(0, "<a href=\"" + path + "\"" + sb.toString() + ">");
            link.append("</a>");
        }
        return link.toString();
    }

    /**
     * Set both href and onclick paths, using javascript for onclick event.
     *
     * "return false;" is needed to get rid of the spinning image on IE.
     */
    public Link setAjaxPath(String path) {
        this.path = AppPaths.ROOT + path;
        onclick = "return App.updateViewHistory(this.href);";
        return this;
    }

    /**
     * For export.
     * @param path
     */
    public Link setExportPath(String path) {
        this.path = AppPaths.ROOT + path;
        this.target = "_blank";
        return this;
    }

    public Link setAppPath(String path) {
        this.path = AppPaths.ROOT + path;
        return this;
    }

    public Link setPath(String path) {
        this.path = path;
        return this;
    }

    public Link setJavascript(String javascript) {
        path = "javascript:void(0);";
        onclick = javascript;
        return this;
    }

    public Link setExternalPath(String path) {
        this.path = HtmlUtils.encode(path);
        this.target = "_blank";
        return this;
    }

    public Link setOnclick(String onclick) {
        this.onclick = onclick;
        return this;
    }

    public Link setTitle(String title) {
        this.title = HtmlUtils.encode(title);
        return this;
    }

    public Link setEscapeTitle(String title) {
        this.title = title;
        return this;
    }

    public Link setTitleKey(String contentKey) {
        this.title = Localizer.getText(requestContext, contentKey);
        return this;
    }

    public Link setImgSrc(String iconPath) {
        this.imgSrc = iconPath;
        return this;
    }

    public Link setAppImgSrc(String iconPath) {
        this.imgSrc = AppPaths.ROOT + iconPath;
        return this;
    }

    public String getPath() {
        return path;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public Link setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public Link setStyleClass(String styleClass) {
        this.styleClass = styleClass;
        return this;
    }

    public void setImgAltText(String imgAltText) {
        this.imgAltText = imgAltText;
    }

    public void setImgAltKey(String imgAltKey) {
        this.imgAltText = Localizer.getText(requestContext, imgAltKey);
    }

    public void setImgSize(int width, int height) {
        this.imgWidth = width;
        this.imgHeight = height;
    }

    public Link setImgStyleClass(String imgStyleClass) {
        this.imgStyleClass = imgStyleClass;
        return this;
    }

    public Link setImgAlignRight() {
        this.imgAlignRight = true;
        return this;
    }

    public String getName() {
        return name;
    }

    public Link setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public Link setId(String id) {
        this.id = id;
        return this;
    }

    public Link setImgId(String imgId) {
        this.imgId = imgId;
        return this;
    }

    public Link setImgOnclick(String imgOnclick) {
        this.imgOnclick = imgOnclick;
        return this;
    }

    public Link setMaterialIcon(String materialIcon) {
        this.materialIcon = materialIcon;
        return this;
    }
}
