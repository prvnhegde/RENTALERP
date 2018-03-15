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
package com.kwoksys.action.admin.manageusers;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.admin.dto.AccessGroup;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.ui.WidgetUtils;

/**
 * TemplateGroupDetail
 */
public class GroupDetailTemplate extends BaseTemplate {

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    private AccessGroup group;

    public GroupDetailTemplate(AccessGroup group) {
        super(GroupDetailTemplate.class);
        this.group = group;
    }

    public void init() {
        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() {
        DetailTableTemplate.Td td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.group_id");
        td.setValue(String.valueOf(group.getId()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.group_name");
        td.setValue(HtmlUtils.encode(group.getName()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.group_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(group.getDescription()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.creator");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, group.getCreationDate(), group.getCreator()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.modifier");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, group.getModificationDate(), group.getModifier()));
    }

    @Override
    public String getJspPath() {
        return "/jsp/common/template/DetailTable.jsp";
    }
}
