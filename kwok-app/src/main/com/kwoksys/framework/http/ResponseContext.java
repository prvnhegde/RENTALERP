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
package com.kwoksys.framework.http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ResponseContext
 */
public class ResponseContext {

    HttpServletResponse response;

    /** For Content-Disposition attachment filename header */
    private String attachementName;

    public ResponseContext(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Http 401: Unauthorized
     * "Authentication is required and has failed or has not yet been provided".
     */
    public void sendUnauthorized() throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    /**
     * Http 403: Forbidden
     */
    public void sendForbidden() throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Http 404: File Not Found
     */
    public void sendFileNotFound() throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * Http 500: Server Error
     * Unknown error that needs to be fixed in the application code.
     */
    public void sendServerError() throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * Http 503: Service Unavailable
     * Server is up but cannot connect to the database.
     */
    public void sendServiceUnavailable() throws IOException {
        response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public String getAttachementName() {
        return attachementName;
    }

    public void setAttachementName(String attachementName) {
        this.attachementName = attachementName;
    }
}
