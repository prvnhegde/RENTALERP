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
package com.kwoksys.biz.system.core;

/**
 * This has ids of different event types.
 */
public class EventTypes {

    public final static Integer SMTP_ERROR = 1;

    public final static Integer LDAP_ERROR = 2;

    public final static Integer DB_BACKUP_ERROR = 3;

    public final static Integer USER_LOGIN = 4;

    public final static Integer KB_ARTICLE_ADD = 5;

    public final static Integer KB_ARTICLE_UPDATE = 6;

    public final static Integer KB_ARTICLE_DELETE = 7;
}
