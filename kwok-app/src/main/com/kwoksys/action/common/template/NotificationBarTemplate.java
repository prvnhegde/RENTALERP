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
package com.kwoksys.action.common.template;

import com.kwoksys.biz.base.BaseTemplate;
import com.kwoksys.framework.properties.Localizer;

/**
 * NotificationBarTemplate
 */
public class NotificationBarTemplate extends BaseTemplate {

    private String notifyMessage;
    
    public NotificationBarTemplate() {
        super(NotificationBarTemplate.class);
    }

    public void init() {}

    public void applyTemplate() throws Exception {
    }

    @Override
    public String getJspPath() {
        return "/jsp/common/template/NotificationBar.jsp";
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(String notifyMessage) {
        this.notifyMessage = notifyMessage;
    }
    
    public void setNotifyMessageKey(String notifyMessageKey) {
        this.notifyMessage = Localizer.getText(requestContext, notifyMessageKey);
    }
}
