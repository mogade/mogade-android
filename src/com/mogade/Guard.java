package com.mogade;

public class Guard {
    public static void NotNullOrEmpty(String value, String message) {
        if (value == null || value.trim().length() == 0)
            throw new IllegalArgumentException(message);
    }

    public static void GreaterThanZero(int value, String message) {
        if (value <= 0)
            throw new IllegalArgumentException(message);
    }

    public static void NotNull(Object value, String message) {
        if (value == null)
            throw new IllegalArgumentException(message);
    }
}
