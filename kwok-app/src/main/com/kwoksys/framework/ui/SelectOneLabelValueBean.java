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

import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.http.RequestContext;
import org.apache.struts.util.LabelValueBean;

/**
 * SelectOneLabelValueBean
 */
public class SelectOneLabelValueBean extends LabelValueBean {

    public SelectOneLabelValueBean(RequestContext requestContext, String value) {
        setLabel(Localizer.getText(requestContext, "core.selectbox.selectOne"));
        setValue(value);
    }

    public SelectOneLabelValueBean(RequestContext requestContext) {
        setLabel(Localizer.getText(requestContext, "core.selectbox.selectOne"));
        setValue(null);
    }
}
