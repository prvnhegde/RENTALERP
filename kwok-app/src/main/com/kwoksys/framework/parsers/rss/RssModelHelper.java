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

import java.io.StringReader;
import java.util.Iterator;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.impl.llom.factory.OMXMLBuilderFactory;

import com.kwoksys.framework.util.XmlUtils;

/**
 * RssModelHelper
 */
public class RssModelHelper {

    private String xmlString;

    private RssModel rssModel;

    public void xmlToModel(String xmlString) throws Exception {
        this.xmlString = xmlString;
        rssModel = new RssModel();

        StringReader reader = new StringReader(xmlString);
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(reader);
        OMXMLParserWrapper builder = OMXMLBuilderFactory.createStAXOMBuilder(OMAbstractFactory.getOMFactory(), parser);

        OMElement rss = builder.getDocumentElement();
        OMElement channel = rss.getFirstElement();
        buildChannel(channel);
    }

    private void buildChannel(OMElement channel) {
        for (Iterator<OMElement> iter = channel.getChildElements(); iter.hasNext();) {
            OMElement elem = iter.next();
            String name = elem.getLocalName();

            if (name.equals("item")) {
                buildItem(elem);

            } else if (name.equals("title")) {
                rssModel.setTitle(elem.getText());

            } else if (name.equals("link")) {
                rssModel.setLink(elem.getText());

            } else if (name.equals("pubDate")) {
                rssModel.setPubDate(elem.getText());
            }
        }
    }

    private void buildItem(OMElement item) {
        RssModel.Item rssItem = rssModel.new Item();

        for (Iterator<OMElement> iter = item.getChildElements(); iter.hasNext();) {
            OMElement elem = iter.next();
            String name = elem.getLocalName();

            if (name.equals("title")) {
                rssItem.setTitle(elem.getText());

            } else if (name.equals("link")) {
                rssItem.setLink(elem.getText());

            } else if (name.equals("description")) {
                rssItem.setDescription(elem.getText());
            }
        }
        rssModel.addItem(rssItem);
    }

    public void modelToXml(RssModel rssModel) {
        this.rssModel = rssModel;

        OMFactory factory = OMAbstractFactory.getOMFactory();

        OMElement rss = factory.createOMElement("rss", null);
        rss.addAttribute("version", "2.0", null);

        OMElement channel = factory.createOMElement("channel", null, rss);

        if (rssModel.getTitle() != null) {
            factory.createOMElement("title", null, channel).setText(rssModel.getTitle());
        }

        if (rssModel.getLink() != null) {
            factory.createOMElement("link", null, channel).setText(rssModel.getLink());
        }

        if (rssModel.getDescription() != null) {
            factory.createOMElement("description", null, channel).setText(rssModel.getDescription());
        }

        if (rssModel.getLanguage() != null) {
            factory.createOMElement("language", null, channel).setText(rssModel.getLanguage());
        }

        for (RssModel.Item rssItem : rssModel.getItems()) {
            OMElement item = factory.createOMElement("item", null, channel);

            factory.createOMElement("title", null, item).setText(rssItem.getTitle());
            factory.createOMElement("link", null, item).setText(rssItem.getLink());
        }

        xmlString = XmlUtils.prependDocType(rss.toString());
    }

    public String getXmlString() {
        return xmlString;
    }

    public RssModel getRssModel() {
        return rssModel;
    }
}
