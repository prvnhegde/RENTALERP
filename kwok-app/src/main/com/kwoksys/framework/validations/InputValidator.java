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
package com.kwoksys.framework.validations;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.AttributeValue;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.Schema;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.util.CurrencyUtils;
import com.kwoksys.framework.util.CustomFieldFormatter;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.swing.text.MaskFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InputValidator
 */
public class InputValidator {

    private RequestContext requestContext;
    private ActionMessages errors;

    public InputValidator(RequestContext requestContext, ActionMessages errors) {
        this.requestContext = requestContext;
        this.errors = errors;
    }

    public void validate(ColumnField columnField) {
        if (!columnField.isNullable() && columnField.getLength() == 0) {
                errors.add(columnField.getName(), new ActionMessage("common.form.fieldRequired",
                        Localizer.getText(requestContext, columnField.getTitleKey())));
        }

        if (columnField.getColumnName() != null) {
            int maxLength = Schema.getColumnLength(columnField.getColumnName());

            if (columnField.getLength() > maxLength) {
                errors.add(columnField.getName(), new ActionMessage("common.form.fieldExceededMaxLen", new String[]{
                        Localizer.getText(requestContext, columnField.getTitleKey()), String.valueOf(maxLength)}));
            }
        }
    }

    /**
     * Validates system and custom attributes
     * @param baseObject
     */
    public void validateAttrs(BaseObject baseObject, Map<Integer, Attribute> customAttributes) {
        // Validate system attributes
        Map<Integer, Attribute> map = new AttributeManager(requestContext).getSystemAttributes(baseObject);

        for (Attribute attr : map.values()) {
            if (attr.isRequired() && baseObject.isAttrEmpty(attr.getName())) {
                errors.add(attr.getName(), new ActionMessage("common.form.fieldRequired",
                        Localizer.getText(requestContext, "common.column." + attr.getName())));
            }
        }

        // Validate custom attributes
        if (baseObject.getCustomValues() != null) {
            for (AttributeValue attrValue : baseObject.getCustomValues().values()) {
                Attribute attr = customAttributes.get(attrValue.getAttributeId());

                if (attr.isRequired() && attrValue.getAttributeValue().isEmpty()) {
                    errors.add(attr.getName(), new ActionMessage("common.form.fieldRequired", attr.getName()));

                } else if (attr.getType().equals(Attribute.ATTR_TYPE_DATE)) {
                    if (!DatetimeUtils.isValidDateString(attrValue.getAttributeValue())) {
                        errors.add(attr.getName(), new ActionMessage("common.form.fieldDateInvalid", attr.getName()));
                    } else {
                        attrValue.setRawValue(new CustomFieldFormatter(attr, attrValue.getAttributeValue()).getAttributeRawValue());
                    }
                } else if (attr.getType().equals(Attribute.ATTR_TYPE_CURRENCY)) {
                    attrValue.setRawValue(new CustomFieldFormatter(attr, attrValue.getAttributeValue()).getAttributeRawValue());
                    if (!CurrencyUtils.isValidFormat(attrValue.getRawValue())) {
                        errors.add(attr.getName(), new ActionMessage("common.form.fieldFormatError", attr.getName()));
                    }
                } else if (!attr.getInputMask().isEmpty() && !attrValue.getAttributeValue().isEmpty()) {
                    try {
                        MaskFormatter formatter = new MaskFormatter(attr.getInputMask());
                        formatter.stringToValue(attrValue.getAttributeValue());
                        attrValue.setRawValue(formatter.valueToString(attrValue.getAttributeValue()));
                    } catch (Exception e) {
                        errors.add(attr.getName(), new ActionMessage("common.form.fieldFormatError", attr.getName(), attr.getDescription()));
                    }
                }
            }
        }
    }

    public void validatePassword(String passwordMessageKey, AccessUser accessUser) {
        if (accessUser.getPasswordNew() != null && accessUser.getPasswordConfirm() != null &&
                !accessUser.getPasswordNew().equals(accessUser.getPasswordConfirm())) {

            errors.add("password", new ActionMessage("admin.userEdit.error.passwordMismatch"));

        } else if (accessUser.isAccountEnabled()) {
            if (StringUtils.isEmpty(accessUser.getPasswordNew())) {
                if (!ConfigManager.admin.isAllowBlankUserPassword()) {
                    errors.add("password", new ActionMessage("common.form.fieldRequired",
                            Localizer.getText(requestContext, passwordMessageKey)));
                }
            } else if (accessUser.getPasswordNew().length() < ConfigManager.auth.getSecurityMinPasswordLength()) {
                errors.add("password", new ActionMessage("admin.userEdit.error.passwordMinLen", ConfigManager.auth.getSecurityMinPasswordLength()));

            } else if (!validatePasswordComplexity(accessUser)) {
                errors.add("password", new ActionMessage("admin.config.security.passwordComplexity.desc"));
            }
        }
    }

    /**
     * Validates new user password against password complexity requirement.
     * @param newPassword
     * @param accessUser
     * @return
     */
    private static boolean validatePasswordComplexity(AccessUser accessUser) {
        String newPassword = accessUser.getPasswordNew();

        if (!ConfigManager.admin.isSecurityPasswordComplexityEnabled()) {
            return true;
        }

        if (newPassword.toLowerCase().contains(accessUser.getFirstName().toLowerCase())) {
            return false;
        }

        if (newPassword.toLowerCase().contains(accessUser.getLastName().toLowerCase())) {
            return false;
        }

        // Password complexity MUST meet 3 of the 4 following: at least 1 upper letter, 1 lower letter, 1 number, and
        // 1 special character.
        List<String> requirements = Arrays.asList("([a-z])", "([A-Z])", "([0-9])", "([\\p{Punct}])");
        int found = 0;
        for (String regex : requirements) {
            Matcher m = Pattern.compile(regex).matcher(newPassword);
            if (m.find()) {
                found++;
            }
        }
        if (found < 3) {
            return false;
        }
        return true;
    }
}
