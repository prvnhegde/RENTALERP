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
package com.kwoksys.framework.parsers.rss;

import com.kwoksys.framework.util.HttpUtils;

/**
 * RssExample
 */
public class RssExample {

    public static void main(String args[]) throws Exception {
        /*
         * Example of converting rss feed to java model
         */
        RssModelHelper helper = new RssModelHelper();
        helper.xmlToModel(HttpUtils.getContent("http://news.google.com/nwshp?ie=UTF-8&tab=wn&output=rss"));

        RssModel model = helper.getRssModel();
        System.out.println(model.getTitle());

        /*
         * Example of converting java model to rss feed xml string
         */
        RssModel model2 = new RssModel();
        model2.setTitle("RSS Feed");
        model2.setDescription("RSS Description");

        helper = new RssModelHelper();
        helper.modelToXml(model2);

        System.out.println(helper.getXmlString());
    }
}
