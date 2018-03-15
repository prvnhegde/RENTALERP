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
package com.kwoksys.framework.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import java.security.InvalidKeyException;
import java.security.Key;

/**
 * This is for encryption/decryption.
 */
public class Encrypter {

    private static final String algorithm = "DESede"; // DESede: Triple DES Encryption (DES-EDE).
    private static Key key;
    private static Cipher cipher;

    private static void setUp() throws Exception {
        key = KeyGenerator.getInstance(algorithm).generateKey();
        cipher = Cipher.getInstance(algorithm);
    }

    public static void test(String input) throws Exception {
        setUp();
        byte[] encryptionBytes;
        encryptionBytes = encrypt(input);
        System.out.println("Entered: " + input);
        System.out.println("Key: " + key.toString());
        System.out.println("Encrypted: " + encryptionBytes.toString());
        System.out.println("Recovered: " + decrypt(encryptionBytes));
    }

    private static byte[] encrypt(String input) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inputBytes = input.getBytes();
        return cipher.doFinal(inputBytes);
    }

    private static String decrypt(byte[] encryptionBytes) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
        return new String(recoveredBytes);
    }
}
