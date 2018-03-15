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
package com.kwoksys.biz.auth.core;

import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.system.license.LicenseValidator;
import com.kwoksys.framework.session.CookieManager;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * Auth common.
 */
public class AuthUtils {

    public static String generateRandomChars(int numCharacters) {
        String characterList = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.";

        Random r = new Random();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numCharacters; i++) {
            result.append(characterList.charAt(r.nextInt(characterList.length())));
        }
        return result.toString();
    }

    /**
     * Gets the hash value of an input password.
     *
     * @param input
     * @return ..
     */
    public static String hashPassword(String input) throws Exception {
        return LicenseValidator.hash(input);
    }

    /**
     * Empties all auth cookies.
     *
     * @param response
     */
    public static void resetAuthCookies(HttpServletResponse response, AccessUser accessUser) {
        CookieManager.clearAllCookies(response);
        accessUser.setId(Access.GUEST_USER_ID);
    }
}
