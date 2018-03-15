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
package com.kwoksys.action.contacts;

import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dao.ContactQueries;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.contacts.dto.CompanyTag;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.WidgetUtils;
import com.kwoksys.framework.util.HtmlUtils;
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.framework.util.UrlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Template class for Company spec.
 * columnText
 * columnValue
 * rowClass
 */
public class CompanySpecTemplate extends BaseTemplate {

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    private Company company;
    private boolean populateCompanyTagList;

    public CompanySpecTemplate(Company company) {
        super(CompanySpecTemplate.class);
        this.company = company;
    }

    public void init() {
        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws Exception {
        AttributeManager attributeManager = new AttributeManager(requestContext);

        DetailTableTemplate.Td td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.company_id");
        td.setValue(String.valueOf(company.getId()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.company_name");
        td.setValue(HtmlUtils.encode(company.getName()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.company_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(company.getDescription()));

        ContactService contactService = ServiceProvider.getContactService(requestContext);
        company.setTypeIds(contactService.getCompanyTypes(company.getId()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.company_types");

        if (!company.getTypeIds().isEmpty()) {
            List<String> types = new ArrayList<>();
            for (Integer typeId : company.getTypeIds()) {
                types.add(attributeManager.getAttrFieldNameCache(Attributes.COMPANY_TYPES, typeId));
            }
            td.setValue(StringUtils.join(types, ", "));
        }

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.creator");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, company.getCreationDate(), company.getCreator()));

        td = detailTableTemplate.newTd();
        td.setHeaderKey("common.column.modifier");
        td.setValue(WidgetUtils.formatCreatorInfo(requestContext, company.getModificationDate(), company.getModifier()));

        if (populateCompanyTagList) {
            // Get a list of tags.
            QueryCriteria tagQuery = new QueryCriteria();
            tagQuery.addSortColumn(ContactQueries.getOrderByColumn(CompanyTag.TAG_NAME));

            StringBuilder tags = new StringBuilder();
            for (Map<String, String> map : contactService.getCompanyTags(tagQuery, company.getId())) {
                tags.append(new Link(requestContext).setTitle(map.get("tag_name")) .setAjaxPath(AppPaths.CONTACTS_COMPANY_LIST
                        + "?cmd=search&companyTag=" + UrlUtils.encode(map.get("tag_name"))).getString()).append("&nbsp; ");
            }

            td = detailTableTemplate.newTd();
            td.setHeaderKey("common.column.company_tags");
            td.setValue(tags.toString());
        }
    }

    @Override
    public String getJspPath() {
        return "/jsp/contacts/CompanySpecTemplate.jsp";
    }

    public String getCompanyHeaderText() {
        return Localizer.getText(requestContext, "contactMgmt.companyDetail.header", new String[]{company.getName()});
    }

    public void setPopulateCompanyTagList(boolean populateCompanyTagList) {
        this.populateCompanyTagList = populateCompanyTagList;
    }
}
