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
package com.kwoksys.framework.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.system.core.configs.ConfigManager;

/**
 * CustomFieldFormatter
 */
public class CustomFieldFormatter {

    private Attribute attr;

    private Object fieldValue;

    private boolean editPage = false;

    public CustomFieldFormatter(Attribute attr, Object fieldValue) {
        this.attr = attr;
        this.fieldValue = fieldValue;
    }

    public String getAttributeValue() {
        if (editPage) {
            return getAttributeTextValue();
        } else {
            return getAttributeHtmlValue();
        }
    }

    public String getAttributeHtmlValue() {
        if (attr.getType().equals(Attribute.ATTR_TYPE_DATE)) {
            try {
                fieldValue = DatetimeUtils.toShortDate(Long.valueOf((String) fieldValue));
            } catch (Exception e) {/* ignore */}

        } else if (attr.getType().equals(Attribute.ATTR_TYPE_MULTISELECT)) {
            if (fieldValue instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> values = HtmlUtils.encode((List<String>) fieldValue);
                fieldValue = StringUtils.join(values, "<br>");
            }
        } else if (attr.getType().equals(Attribute.ATTR_TYPE_CURRENCY)) {
            fieldValue = CurrencyUtils.formatCurrency(fieldValue, attr.getTypeCurrencySymbol());

        } else {
            // Multi-line attribute
            if (attr.getType().equals(Attribute.ATTR_TYPE_MULTILINE)) {
                fieldValue = HtmlUtils.formatMultiLineDisplay((String) fieldValue);
                
            } else {
                fieldValue = HtmlUtils.encode((String) fieldValue);
            }

            // Convert urls
            if (attr.isConvertUrl()) {
                fieldValue = convertUrl((String) fieldValue);
            }
        }
        return (String) fieldValue;
    }

    public String getAttributeTextValue() {
        if (attr.getType().equals(Attribute.ATTR_TYPE_DATE)) {
            try {
                fieldValue = DatetimeUtils.toShortDate(Long.valueOf((String) fieldValue));
            } catch (Exception e) {/* ignore */}

        } else if (attr.getType().equals(Attribute.ATTR_TYPE_MULTISELECT)) {
            fieldValue = StringUtils.join((List) fieldValue, "\n");

        } else if (attr.getType().equals(Attribute.ATTR_TYPE_CURRENCY)) {
            if (!editPage) {
                fieldValue = CurrencyUtils.formatCurrency(fieldValue, attr.getTypeCurrencySymbol());
            }
        }
        return (String)fieldValue;
    }

    public String getAttributeRawValue() {
        if (attr.getType().equals(Attribute.ATTR_TYPE_DATE)) {
            try {
                fieldValue = String.valueOf(DatetimeUtils.parseDate((String)fieldValue, ConfigManager.system.getDateFormat()).getTime());
            } catch (Exception e) {/* ignore */}

        } else if (attr.getType().equals(Attribute.ATTR_TYPE_CURRENCY)) {
            fieldValue = ((String)fieldValue).replace(",", "");
        }
        return (String)fieldValue;
    }

    private static String convertUrl(String fieldValue) {
        String regex = "(?<!\")((http|https|ftp)" + HtmlUtils.encode("://") + "[^\\s]+)(?!\")";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fieldValue);
        if (m.find()) {
            fieldValue = m.replaceAll("<a href=\"$1\" target=\"_blank\">$1</a>");
        }
        return fieldValue;
    }

    public boolean isEditPage() {
        return editPage;
    }

    public void setEditPage(boolean editPage) {
        this.editPage = editPage;
    }
}
