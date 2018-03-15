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
import com.kwoksys.framework.util.StringUtils;
import com.kwoksys.test.cases.KwokTestCase;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtilTest
 */
public class StringUtilTest extends KwokTestCase {

    public StringUtilTest(RequestContext requestContext) {
        super(requestContext);
    }

    public static void main(String args[]) throws Exception {
        new StringUtilTest(null).execute();
    }

    public void execute() throws Exception {
        testJoin();
    }

    private void testJoin() {
        String expectedString = "a1, b1, c1";

        List<String> strings = Arrays.asList("a1", "b1", "", "c1");
        String joinedString = StringUtils.join(strings, ", ");
        addResult("Result... " + joinedString, expectedString.equals(joinedString));

        Set<String> set = new LinkedHashSet<>();
        set.add("a1");
        set.add("b1");
        set.add("");
        set.add("c1");
        joinedString = StringUtils.join(set, ", ");
        addResult("Result... " + joinedString, expectedString.equals(joinedString));
    }

    private static void test() {
        String httpRegex = "(?<!\")((http|https)://[^\\s]+)(?!\")";
//        String emailRegex = "[a-z0-9\\-_\\.]++@[a-z0-9\\-]++(\\.[a-z0-9\\-]++)++" ;

        String string = "URL conversion test" +
                "Lowercase: http://example.com \n" +
                "Lowercase with punctuation: http://example.com. \n" +
                "URL parameters: http://example.com?a=1 \n" +
                "URL parameters: http://example.com?a=1, \n"+
                "Already a link: <a href=\"http://example.com\">example</a> \n" +
                "Upper case https: Https://example.com \n";

        Pattern p = Pattern.compile(httpRegex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        if (m.find()) {
            string = m.replaceAll("<a href=\"$1\">$1</a>");
        }
        System.out.println(string);
    }
}