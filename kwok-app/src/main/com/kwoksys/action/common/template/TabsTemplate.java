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
import com.kwoksys.framework.ui.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * TabsTemplate class.
 */
public class TabsTemplate extends BaseTemplate {

    private String tabActive;
    private List<Link> tabList = new ArrayList<>();

    public TabsTemplate() {
        super(TabsTemplate.class);
    }

    public void init() {}

    public void applyTemplate() {
        for (Link link : tabList) {
            if (tabActive.equals(link.getName())) {
                link.setPath(null);
                link.setOnclick(null);
            }
            link.setStyleClass("inactive");
        }
    }

    @Override
    public String getJspPath() {
        return "/jsp/common/template/Tabs.jsp";
    }

    public void setTabActive(String tabActive) {
        this.tabActive = tabActive;
    }
    public List<Link> getTabList() {
        return tabList;
    }
    public void setTabList(List<Link> tabList) {
        this.tabList = tabList;
    }
}
