package com.example.tts.model;

import lombok.Getter;

public enum Language {
    ENGLISH("en", "English"),
    HINDI("hi", "Hindi"),
    GUJARATI("gu", "Gujarati"),
    MARATHI("mr", "Marathi"),
    SPANISH("es", "Spanish"),
    FRENCH("fr", "French"),
    GERMAN("de", "German");


    private final String code;
    private final String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public static boolean isSupported(String code) {
        for (Language l : values()) {
            if (l.code.equalsIgnoreCase(code)) return true;
        }
        return false;
    }
}
