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
package com.kwoksys.action.contracts;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.biz.contacts.core.CompanySearch;
import com.kwoksys.biz.contacts.core.CompanyUtils;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.SelectOneLabelValueBean;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

/**
 * ContractSearchTemplate
 */
public class ContractSearchTemplate extends BaseTemplate {

    private String formAction;
    private boolean hideSearchButton;
    private String clearSearchLink;

    public ContractSearchTemplate() {
        super(ContractSearchTemplate.class);
    }

    public void init() {}

    public void applyTemplate() throws DatabaseException {
        // Get contract provider list
        List<LabelValueBean> providerOptions = new ArrayList<>();
        providerOptions.add(new SelectOneLabelValueBean(requestContext, "0"));

        CompanySearch companySearch = new CompanySearch();
        companySearch.put(CompanySearch.CONTRACT_PROVIDERS, true);

        QueryCriteria queryCriteria = new QueryCriteria(companySearch);
        queryCriteria.addSortColumn(Company.COMPANY_NAME);

        providerOptions.addAll(CompanyUtils.getCompanyOptions(requestContext, queryCriteria));

        request.setAttribute("customFieldsOptions", new AttributeManager(requestContext).getCustomFieldOptions(ObjectTypes.CONTRACT));
        request.setAttribute("contractTypeOptions", new AttributeManager(requestContext).setOptional(true)
                .getAttrFieldOptionsCache(Attributes.CONTRACT_TYPE));
        request.setAttribute("contractStageOptions", new AttributeManager(requestContext).setOptional(true)
                .getAttrFieldOptionsCache(Attributes.CONTRACT_STAGE));
        request.setAttribute("contractProviderOptions", providerOptions);

        if (!hideSearchButton) {
            clearSearchLink = new Link(requestContext).setAjaxPath(AppPaths.CONTRACTS_INDEX + "?cmd=clear").setTitleKey("common.form.clearCriteria").getString();
        }
    }

    public String getFormAction() {
        return formAction;
    }
    public void setFormAction(String formAction) {
        this.formAction = AppPaths.ROOT + formAction;
    }
    public boolean getHideSearchButton() {
        return hideSearchButton;
    }

    public void setHideSearchButton(boolean hideSearchButton) {
        this.hideSearchButton = hideSearchButton;
    }

    public String getClearSearchLink() {
         return clearSearchLink;
     }
}
