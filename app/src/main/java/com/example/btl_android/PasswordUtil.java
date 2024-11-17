package com.example.btl_android;

import java.util.regex.Pattern;

public class PasswordUtil {
    // Định nghĩa tiêu chí kiểm tra mật khẩu
    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            return false;
        }
        if (!UPPERCASE_PATTERN.matcher(password).find()) {
            return false;
        }
        if (!LOWERCASE_PATTERN.matcher(password).find()) {
            return false;
        }
        if (!DIGIT_PATTERN.matcher(password).find()) {
            return false;
        }
        if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String password1 = "Password123!";
        String password2 = "weakpass";

        System.out.println("Password1 is strong: " + isStrongPassword(password1)); // true
        System.out.println("Password2 is strong: " + isStrongPassword(password2)); // false
    }
}

