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
package com.kwoksys.action.kb;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.framework.struts2.Action2;

/**
 * Action class for showing Article print view.
 */
public class ArticlePrintAction extends Action2 {

    public String execute() throws Exception {
        // Get request parameters
        Integer articleId = requestContext.getParameter("articleId");

        KbService kbService = ServiceProvider.getKbService(requestContext);
        Article article = kbService.getArticle(articleId);

        //
        // Template: success
        //
        request.setAttribute("articleId", article.getId());
        request.setAttribute("articleTitle", article.getName());
        request.setAttribute("articleText", article.getContent());
        request.setAttribute("articleCreator", AdminUtils.getSystemUsername(requestContext, article.getCreator()));
        request.setAttribute("articleCreationDate", article.getCreationDate());

        return SUCCESS;
    }
}