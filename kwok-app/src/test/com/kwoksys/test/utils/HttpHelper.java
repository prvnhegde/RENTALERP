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
package com.kwoksys.test.utils;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.kwoksys.framework.util.HttpUtils;

/**
 * HttpUtils
 */
public class HttpHelper {

    private static final Logger LOGGER = Logger.getLogger(HttpHelper.class.getName());

    private boolean printResponse;
    
    public static void main(String[] args) throws Exception {
        new HttpHelper().setPrintResponse(true).get("http://localhost:8080/dev/home/index.htm");
    }

    public void get(String endpoint) throws Exception {
        URL url = new URL(endpoint);

        // Ignore certificate error
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType){}
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType){}
            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Start connection
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept-Encoding", "gzip,deflate");

        Map<String, List<String>> map = conn.getHeaderFields();
        
        if (!printResponse) {
            return;
        }
        
        LOGGER.log(Level.INFO, "Request URL: " + endpoint);

        boolean hasContentEncoding = false;
        StringBuilder response = new StringBuilder();
        response.append("Resonse content:\n");
        
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {

            // if ("Content-Type".equals(entry.getKey())) {
                if (entry.getKey() != null) {
                    response.append(entry.getKey() + ": ");
                }

                for (String string : entry.getValue()) {
                    response.append(string + "\n");
                }
           // }

            if ("Content-Encoding".equals(entry.getKey())) {
                hasContentEncoding = true;
            }
        }
        LOGGER.log(Level.INFO, response.toString());
        printContents(conn);
//        if (!hasContentEncoding) {
//            throw new Exception();
//        }
    }

    public static void printContents(URLConnection conn) throws Exception {
        // Get the response
        String result = HttpUtils.convertInputstream(conn.getInputStream());
        LOGGER.log(Level.INFO, result);
    }

    public HttpHelper setPrintResponse(boolean printResponse) {
        this.printResponse = printResponse;
        return this;
    }
}
