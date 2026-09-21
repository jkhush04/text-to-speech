package com.example.tts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TtsRequestDto {
    @NotBlank(message = "Text must not be empty")
    @Size(max = 2000, message = "Text must not exceed 2000 characters")
    private String text;

    @NotBlank(message = "Language must be specified")
    private String language;

    @NotBlank(message = "Voice must be specified")
    private String voice;
}
