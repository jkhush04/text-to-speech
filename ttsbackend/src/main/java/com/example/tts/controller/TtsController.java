package com.example.tts.controller;

import com.example.tts.dto.TtsRequestDto;
import com.example.tts.dto.TtsResponseDto;
import com.example.tts.service.TtsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TtsController {

    private final TtsService ttsService;

    @PostMapping("/tts")
    public ResponseEntity<TtsResponseDto> generatedSpeech(@Valid @RequestBody TtsRequestDto request){
        TtsResponseDto response = ttsService.generateSpeech(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
