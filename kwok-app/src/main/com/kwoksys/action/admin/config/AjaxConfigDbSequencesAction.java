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
package com.kwoksys.action.admin.config;

import com.kwoksys.action.common.template.AjaxTemplate;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for displaying DB sequences.
 */
public class AjaxConfigDbSequencesAction extends Action2 {

    public String execute() throws Exception {
        AdminService adminService = new AdminService(requestContext);

        //
        // Template: AjaxTemplate
        //
        AjaxTemplate ajaxTemplate = new AjaxTemplate(requestContext, AdminUtils.ADMIN_DB_SEQUENCES_CMD);
        ajaxTemplate.setAttribute("sequences", adminService.getDbSequences());

        return ajaxTemplate.findTemplate(AJAX_TEMPLATE);
    }
}
