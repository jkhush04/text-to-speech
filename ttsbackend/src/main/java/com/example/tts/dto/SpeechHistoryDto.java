package com.example.tts.dto;

import com.example.tts.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpeechHistoryDto {
    private Long id;
    private String inputText;
    private String language;
    private String voice;
    private String audioUrl;
    private RequestStatus status;
    private LocalDateTime createdAt;
}