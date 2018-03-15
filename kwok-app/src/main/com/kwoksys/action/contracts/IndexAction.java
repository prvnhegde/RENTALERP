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

import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contracts.ContractService;
import com.kwoksys.biz.system.core.AppPaths;
import com.kwoksys.framework.connections.database.QueryCriteria;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;
import com.kwoksys.framework.struts2.Action2;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.util.NumberUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class of Contract index page.
 */
public class IndexAction extends Action2 {

    public String execute() throws Exception {
        AccessUser user = requestContext.getUser();

        HttpSession session = request.getSession();
        ContractService contractService = ServiceProvider.getContractService(requestContext);

        if (requestContext.getParameterString("cmd").equals("clear")) {
            session.setAttribute(SessionManager.CONTRACT_SEARCH_CRITERIA_MAP, null);
            clearSessionBaseForm(ContractSearchForm.class);
        }

        getSessionBaseForm(ContractSearchForm.class);

        // Contract list
        boolean hasContractsAccess = user.hasPermission(AppPaths.CONTRACTS_LIST);
        List<String> links = new ArrayList<>();

        if (hasContractsAccess) {
            // The search criteria map is not null, that means the user has performed a search.
            if (session.getAttribute(SessionManager.CONTRACT_SEARCH_CRITERIA_MAP) != null) {
                links.add(new Link(requestContext).setAjaxPath(AppPaths.CONTRACTS_LIST)
                        .setTitleKey("common.search.showLastSearch").getString());
            }

            links.add(new Link(requestContext).setAjaxPath(AppPaths.CONTRACTS_LIST + "?cmd=showNonExpired")
                    .setTitleKey("contracts.filter.currentContracts").getString());

            links.add(new Link(requestContext).setAjaxPath(AppPaths.CONTRACTS_LIST + "?cmd=showAll")
                    .setTitleKey("contracts.filter.allContracts").getString());
        }

        // Contracts summary
        Map<String, String> summary = contractService.getContractsSummary();
        List<Map> formattedSummary = new ArrayList<>();

        for (Map.Entry<String, String> entry : summary.entrySet()) {
            Map<String, String> formattedMap = new HashMap<>();
            if (NumberUtils.replaceNull(entry.getValue()) > 0) {
                formattedMap.put("text", Localizer.getText(requestContext, "contracts.search.expire_"+entry.getKey()));
                formattedMap.put("path", new Link(requestContext).setAjaxPath(AppPaths.CONTRACTS_LIST + "?cmd=groupBy&contractExpire="+entry.getKey()).setEscapeTitle(entry.getValue()).getString());
                formattedSummary.add(formattedMap);
            }
        }

        //
        // Template: StandardTemplate
        //
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setAttribute("contractFilters", links);
        standardTemplate.setAttribute("contractsSummary", formattedSummary);

        //
        // Template: ContractSearchTemplate
        //
        ContractSearchTemplate searchTemplate = standardTemplate.addTemplate(new ContractSearchTemplate());
        searchTemplate.setFormAction(AppPaths.CONTRACTS_LIST);

        //
        // Template: HeaderTemplate
        //
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setTitleKey("core.moduleName.3");
        header.setTitleClassNoLine();
        header.setSectionKey("contracts.numContracts", new Integer[]{contractService.getContractCount(new QueryCriteria())});
        
        if (user.hasPermission(AppPaths.CONTRACTS_ADD)) {
            Link link = new Link(requestContext);
            link.setAjaxPath(AppPaths.CONTRACTS_ADD);
            link.setTitleKey("itMgmt.contractAdd.title");
            header.addHeaderCmds(link);
        }

        return standardTemplate.findTemplate(STANDARD_TEMPLATE);
    }
}