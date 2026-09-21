package com.example.tts.controller;

import com.example.tts.dto.SpeechHistoryDto;
import com.example.tts.model.SpeechRequest;
import com.example.tts.model.User;
import com.example.tts.repository.SpeechRequestRepository;
import com.example.tts.repository.UserRepository;
import com.example.tts.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HistoryController {

    private final SpeechRequestRepository speechRequestRepository;
    private final UserRepository userRepository;


    @GetMapping("/history")
    public List<SpeechHistoryDto> getHistory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidRequestException("Authenticated user not found"));

        List<SpeechRequest> requests = speechRequestRepository.findByUserOrderByCreatedAtDesc(user);

        return requests.stream()
                .map(r -> new SpeechHistoryDto(
                        r.getId(),
                        r.getInputText(),
                        r.getLanguage(),
                        r.getVoice(),
                        r.getAudioFilePath() != null ? "/audio/" + r.getAudioFilePath() : null,
                        r.getStatus(),
                        r.getCreatedAt()
                ))
                .toList();
    }
}