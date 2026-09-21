package com.example.tts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${elevenlabs.api.base-url}")
    private String elevenLabsBaseUrl;

    @Bean
    public WebClient elevenLabsWebClient() {

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10 MB
                .build();

        return WebClient.builder()
                .baseUrl(elevenLabsBaseUrl)
                .exchangeStrategies(strategies)
                .build();
    }

}