/*
 * Copyright 2017 Kwoksys
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
package com.kwoksys.test.cases;

import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.test.cases.KwokTestCase;

import javax.swing.text.MaskFormatter;
import java.util.*;

/**
 * CustomFieldsTest class.
 */
public class CustomFieldsTest extends KwokTestCase {

    public static void main(String[] args) throws Exception {
        new CustomFieldsTest(null).testInputMask();
    }

    public CustomFieldsTest(RequestContext requestContext) {
        super(requestContext);
    }

    public void execute() throws Exception {
        testInputMask();
    }

    public void testInputMask() throws Exception {
        System.out.println("Positive tests");
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"###:####", "123:4567"}); // Any number
        list.add(new String[]{"???-????", "Abc-defg"}); // Any character
        list.add(new String[]{"AAA-AAAA", "123-Abcd"}); // Any character or number
        list.add(new String[]{"###-LLLL", "123-Abcd"}); // Lowercase the value

        for (String[] strings : list) {
            boolean pass;
            String mask = strings[0];
            String value = strings[1];
            try {
                MaskFormatter formatter = new MaskFormatter(mask);
                formatter.stringToValue(value);
                System.out.println("Input: " + value + "; output: " + formatter.valueToString(value));
                pass = true;
            } catch (Exception e) {
                pass = false;
            }
            addResult("Input mask... " + mask + ": " + value, pass);
        }

        System.out.println("Negative tests");
        list = new ArrayList<>();
        list.add(new String[]{"###-####", ""});
        list.add(new String[]{"'A###", "B737"}); // Expects A####, where A is the character A
        list.add(new String[]{"###-####", "1234-12345"});

        for (String[] strings : list) {
            boolean pass;
            String mask = strings[0];
            String value = strings[1];
            try {
                MaskFormatter formatter = new MaskFormatter(mask);
                formatter.stringToValue(value);
                pass = false;
            } catch (Exception e) {
                pass = true;
            }
            addResult("Input mask... " + mask + ": " + value, pass);
        }
    }
}
