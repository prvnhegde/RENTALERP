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

import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.util.HtmlUtils;
import org.apache.struts.util.LabelValueBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * WidgetUtils
 */
public class WidgetUtils {

    public static String formatCreatorInfo(RequestContext requestContext, String creationDate, AccessUser accessUser) {
        String output = "";

        if (!creationDate.isEmpty()) {
            String username = AdminUtils.getSystemUsername(requestContext, accessUser);
            output = Localizer.getText(requestContext, "common.audit.creator",
                    new String[] {creationDate, HtmlUtils.encode(username)});
        }
        return output;
    }

    /**
     * Returns a generic on/off option list.
     * @param request
     * @return
     */
    public static List<LabelValueBean> getOnOffOptions(RequestContext requestContext) {
        return Arrays.asList(
                new LabelValueBean(Localizer.getText(requestContext, "common.boolean.on_off.true"), "true"),
                new LabelValueBean(Localizer.getText(requestContext, "common.boolean.on_off.false"), "false"));
    }

    /**
     * Returns a generic true/false option list.
     * @param request
     * @return
     */
    public static List<LabelValueBean> getBooleanOptions(RequestContext requestContext) {
        return Arrays.asList(
                new LabelValueBean(Localizer.getText(requestContext, "common.boolean.true_false.true"), "true"),
                new LabelValueBean(Localizer.getText(requestContext, "common.boolean.true_false.false"), "false"));
    }

    /**
     * Returns yes/no LabelValueBean option list.
     * @param request
     * @return
     */
    public static List<LabelValueBean> getYesNoOptions(RequestContext requestContext) {
        return Arrays.asList(
                new LabelValueBean(Localizer.getText(requestContext, "common.boolean.yes_no.true"), "1"),
                new LabelValueBean(Localizer.getText(requestContext, "common.boolean.yes_no.false"), "0"));
    }

    public static List<LabelValueBean> getLocaleOptions(RequestContext requestContext) {
        List<LabelValueBean> localeOptions = new ArrayList<>();
        for (String localeString : ConfigManager.system.getLocaleOptions()) {

            String[] strings = localeString.split("_");
            Locale locale = new Locale(strings[0], strings[1]);

            String label = Localizer.getText(requestContext, "admin.config.locale."+localeString);

            if (!locale.equals(requestContext.getLocale())) {
                label += " - " + Localizer.getText(locale, "admin.config.locale."+localeString);
            }
            localeOptions.add(new LabelValueBean(label, localeString));
        }
        return localeOptions;
    }
}
