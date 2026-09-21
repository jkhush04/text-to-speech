package com.example.tts.service;

import com.example.tts.dto.TtsRequestDto;
import com.example.tts.dto.TtsResponseDto;
import com.example.tts.exception.InvalidRequestException;
import com.example.tts.exception.TtsProviderException;
import com.example.tts.model.Language;
import com.example.tts.model.RequestStatus;
import com.example.tts.model.SpeechRequest;
import com.example.tts.model.User;
import com.example.tts.repository.SpeechRequestRepository;
import com.example.tts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TtsService {
    private final ElevenLabsClient elevenLabsClient;
    private final AudioStorageService audioStorageService;
    private final SpeechRequestRepository speechRequestRepository;
    private final UserRepository userRepository;

    public TtsResponseDto generateSpeech(TtsRequestDto request){
        if(!Language.isSupported(request.getLanguage())){
            throw new InvalidRequestException("Unsupported language: " + request.getLanguage());
        }


        SpeechRequest logEntry = SpeechRequest.builder()
                .inputText(request.getText())
                .language(request.getLanguage())
                .voice(request.getVoice())
                .user(getCurrentUser())
                .build();


        try {
            // 2. Call ElevenLabs
            byte[] audioBytes = elevenLabsClient.textToSpeech(request.getText(), request.getVoice());

            // 3. Save audio to disk
            String filename = audioStorageService.saveAudio(audioBytes);

            // 4. Log success
            logEntry.setStatus(RequestStatus.SUCCESS);
            logEntry.setAudioFilePath(filename);
            speechRequestRepository.save(logEntry);

            // 5. Return response
            return new TtsResponseDto(true, "/audio/" + filename);

        } catch (TtsProviderException e) {
            // 6. Log failure, then re-throw so the controller/exception handler returns the right HTTP status
            logEntry.setStatus(RequestStatus.FAILED);
            logEntry.setErrorMessage(e.getMessage());
            speechRequestRepository.save(logEntry);
            throw e;
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidRequestException("Authenticated user not found"));
    }
}
