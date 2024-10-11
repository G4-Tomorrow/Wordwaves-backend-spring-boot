package com.server.wordwaves.utils;

public class MyStringUtils {
    public static boolean isNotNullAndNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
