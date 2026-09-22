package com.example.tts.service;

import com.example.tts.config.VoiceCatalogConfig;
import com.example.tts.exception.TtsProviderException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElevenLabsClient {

    private final WebClient elevenLabsWebClient;

    @Value("${elevenlabs.api.key}")
    private String apiKey;


    public byte[] textToSpeech(String text, String voiceId) {
        Map<String, Object> requestBody = Map.of(
                "text", text,
                "model_id", VoiceCatalogConfig.MODEL_ID, // "eleven_v3" — required for full language coverage
                "voice_settings", Map.of(
                        "stability", 0.5,
                        "similarity_boost", 0.75
                )
        );

        try {
            return elevenLabsWebClient.post()
                    .uri("/text-to-speech/{voiceId}", voiceId)
                    .header("xi-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.parseMediaType("audio/mpeg"))
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(status -> status.value() == 401, response ->
                            Mono.error(new TtsProviderException("ElevenLabs authentication failed — check API key")))
                    .onStatus(status -> status.value() == 429, response ->
                            Mono.error(new TtsProviderException("ElevenLabs rate limit or quota exceeded")))
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response ->
                            Mono.error(new TtsProviderException("ElevenLabs request failed with status " + response.statusCode())))
                    .bodyToMono(byte[].class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new TtsProviderException("ElevenLabs API error: " + e.getMessage());
        } catch (Exception e) {
            throw new TtsProviderException("Failed to reach ElevenLabs service: " + e.getMessage());
        }
    }
}