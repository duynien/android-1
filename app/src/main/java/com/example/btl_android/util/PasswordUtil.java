package com.example.btl_android.util;

import java.util.regex.Pattern;

public class PasswordUtil {
    // Định nghĩa tiêu chí kiểm tra mật khẩu
    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    public static boolean isStrongPassword(String password) {
        // Kiểm tra mật khẩu không null và độ dài
        if (password == null || password.length() < MIN_LENGTH) {
            return false; // Mật khẩu phải có ít nhất 8 ký tự
        }

        // Kiểm tra sự xuất hiện của các loại ký tự
        boolean hasUpperCase = UPPERCASE_PATTERN.matcher(password).find();
        boolean hasLowerCase = LOWERCASE_PATTERN.matcher(password).find();
        boolean hasDigit = DIGIT_PATTERN.matcher(password).find();
        boolean hasSpecialChar = SPECIAL_CHAR_PATTERN.matcher(password).find();

        // Kiểm tra tất cả các yêu cầu
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }

    public static void main(String[] args) {
        String password1 = "Password123!"; // Mật khẩu mạnh
        String password2 = "weakpass"; // Mật khẩu yếu

        System.out.println("Password1 is strong: " + isStrongPassword(password1)); // true
        System.out.println("Password2 is strong: " + isStrongPassword(password2)); // false
    }
}
