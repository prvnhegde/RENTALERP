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
package com.kwoksys.framework.connections.mail;

import java.util.HashSet;
import java.util.Set;

/**
 * EmailMessage
 */
public class EmailMessage {

    /** TO field of a email message **/
    private Set<String> toField = new HashSet<>();

    /** CC field of a email message **/
    private Set<String> ccField = new HashSet<>();

    /** FROM field of a email message **/
    private String fromField;

    /** SUBJECT field of a email message **/
    private String subjectField;

    /** BODY field of a email message **/
    private String bodyField;

    public void addCcField(String email) {
        if (!toField.contains(email)) {
            ccField.add(email);
        }
    }

    //
    // Getters and setters
    //
    public Set<String> getToField() {
        return toField;
    }

    public Set<String> getCcField() {
        return ccField;
    }

    public String getFromField() {
        return fromField;
    }
    public void setFromField(String fromField) {
        this.fromField = fromField;
    }
    public String getSubjectField() {
        return subjectField;
    }
    public void setSubjectField(String subjectField) {
        this.subjectField = subjectField;
    }
    public String getBodyField() {
        return bodyField;
    }
    public void setBodyField(String bodyField) {
        this.bodyField = bodyField;
    }
}
