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

/**
 * ContentParserFactory
 */
public class WikiParserFactory {

    public static final String TYPE_TEXT = "text";

    public static final String TYPE_MEDIAWIKI = "mediawiki";

    public static final String TYPE_TWIKI = "twiki";

    public static Parser getParser(String type) throws Exception {
        if (TYPE_TEXT.equals(type)) {
            return new TextParser();

        } else if (TYPE_MEDIAWIKI.equals(type)) {
            return new MediawikiParser();

        } else if (TYPE_TWIKI.equals(type)) {
            return new TwikiParser();
        }
        throw new Exception();
    }
}
