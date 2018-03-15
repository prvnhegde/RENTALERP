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
package com.kwoksys.framework.exceptions;

/**
 * This is thrown when the application queries to get an object
 * , but the object doesn't exist in the database.
 */
public class ObjectNotFoundException extends Exception {
    
    // TODO: Remove this. And force every exception to have its own message
    public ObjectNotFoundException() {
        super("");
    }
    
    public ObjectNotFoundException(String message) {
        super(message);
    }

}
