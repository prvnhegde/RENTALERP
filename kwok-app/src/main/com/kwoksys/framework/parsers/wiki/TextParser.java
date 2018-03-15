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
package com.kwoksys.framework.parsers.wiki;

import java.util.Map;

import com.kwoksys.framework.http.RequestContext;

/**
 * TextParser
 */
public class TextParser extends Parser {

    public static final String OPTION_ENCODE_HTML = "encodehtml";

    public static final String OPTION_REPLACE_LINE_BREAKS = "replacelinebreak";

    public static final String OPTION_CONVERT_URLS = "converturls";

    public String parseHtml(RequestContext requestContext, String content) {
        Map<String, String> options = getOptions();

        if (options != null) {
            if (options.containsKey(OPTION_ENCODE_HTML) && options.get(OPTION_ENCODE_HTML) == "true") {
                content = encodeHtml(content);
            }

            if (options.containsKey(OPTION_REPLACE_LINE_BREAKS) && options.get(OPTION_REPLACE_LINE_BREAKS) == "true") {
                content = replaceLineBreaks(content);
            }

            if (options.containsKey(OPTION_CONVERT_URLS) && options.get(OPTION_CONVERT_URLS) == "true") {
                content = convertUrls(content);
            }
        }
        return content;
    }
}