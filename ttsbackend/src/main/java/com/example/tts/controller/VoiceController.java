package com.example.tts.controller;


import com.example.tts.config.VoiceCatalogConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class VoiceController {
    private final VoiceCatalogConfig voiceCatalogConfig;

    @GetMapping("/voices")
    public Map<String, Object> getVoices() {
        return Map.of(
                "voices", voiceCatalogConfig.getVoices(),
                "languages", VoiceCatalogConfig.LANGUAGE_CODE_MAP);
    }
}
