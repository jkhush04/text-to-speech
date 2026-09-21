package com.example.tts.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Getter
public class VoiceCatalogConfig {

    public record VoiceOption(String voiceId ,String name, String gender){}

    private final List<VoiceOption> voices = List.of(
            new VoiceOption("EXAVITQu4vr4xnSDxMaL", "Sarah", "female"),
            new VoiceOption("JBFqnCBsd6RMkjVDRZzb", "George", "male"),
            new VoiceOption("Xb7hH8MSUJpSbSDYk0k2", "Alice", "female"),
            new VoiceOption("nPczCjzI2devNBz1zQrb", "Brian", "male")
    );

    public static final String MODEL_ID = "eleven_v3";

    public static final Map<String, String> LANGUAGE_CODE_MAP = Map.of(
            "en", "English", "hi", "Hindi", "gu", "Gujarati",
            "mr", "Marathi", "es", "Spanish", "fr", "French", "de", "German"
    );

}
