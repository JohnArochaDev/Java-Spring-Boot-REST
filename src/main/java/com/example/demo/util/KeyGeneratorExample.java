package com.example.demo.util;

import javax.crypto.SecretKey;

public class KeyGeneratorExample {
    public static void main(String[] args) throws Exception {
        SecretKey secretKey = EncryptionUtil.generateKey();
        String encodedKey = EncryptionUtil.getStringFromKey(secretKey);
        System.out.println("Generated Secret Key: \n" + encodedKey);
    }
}