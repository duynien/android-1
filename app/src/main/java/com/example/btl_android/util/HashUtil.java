package com.example.btl_android.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

import com.example.btl_android.model.User;

public class HashUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    // SHA-256 Hashing function
    public static String sha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(hash, Base64.DEFAULT);  // Use Android's Base64
    }

    // Generate random salt
    public static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Base64.encodeToString(salt, Base64.DEFAULT);  // Use Android's Base64
    }

    // Hash password with salt
    public static String hashPasswordWithSalt(String password, String salt) throws Exception {
        return sha256(password + salt); // Combine password and salt before hashing
    }

    public static String encrypt(String input, String key) throws Exception {
        return Base64.encodeToString(doCrypto(Cipher.ENCRYPT_MODE, input, key), Base64.DEFAULT);  // Use Android's Base64
    }

    public static String decrypt(String input, String key) throws Exception {
        return new String(doCrypto(Cipher.DECRYPT_MODE, Base64.decode(input, Base64.DEFAULT), key));  // Use Android's Base64
    }

    private static byte[] doCrypto(int cipherMode, String input, String key) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, secretKey);
        return cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] doCrypto(int cipherMode, byte[] input, String key) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, secretKey);
        return cipher.doFinal(input);
    }

    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256);
        Key key = keyGen.generateKey();
        return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);  // Use Android's Base64
    }

    public static void main(String[] args) {

        try {
            String hashedInputPassword = HashUtil.hashPasswordWithSalt("123456aA@", "tZYm5c9HlyKisKMCwdGO/g==");
            if (hashedInputPassword.equals("NUb3s1TkvYTfEYa9mZqML2aBxQ6tBL6rEMj/b8goKoo=")) {
                System.out.println("Password is correct");
            } else {
                System.out.println("Password is incorrect");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
