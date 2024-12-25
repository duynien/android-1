package com.example.btl_android.util;
import android.util.Patterns;
public class CommonUtil {
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.length() >= 6 && username.length() <= 20;
    }

    public static boolean isContainXSSorSqlInjection(String input) {
        String[] patterns = {
                "<script>", "</script>", "javascript:", "onload=", "onerror=", "alert(", "document.cookie", "<img>",
                "eval(", "expression(", "window.location",
                "select", "insert", "update", "delete", "drop", "from", "where", "--", ";", "/*", "*/", "or", "and"
        };

        input = input.toLowerCase();

        for (String pattern : patterns) {
            if (input.contains(pattern)) {
                return true;
            }
        }
        return false;
    }
}
