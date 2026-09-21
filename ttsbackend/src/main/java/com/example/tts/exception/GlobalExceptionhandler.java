package com.example.tts.exception;

import com.example.tts.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionhandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Invalid request");

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }


    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRequest(InvalidRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ElevenLabs-side failures — distinguish auth vs rate-limit vs generic outage
    @ExceptionHandler(TtsProviderException.class)
    public ResponseEntity<ErrorResponseDto> handleProviderError(TtsProviderException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "";

        HttpStatus status;
        if (msg.contains("authentication")) {
            status = HttpStatus.UNAUTHORIZED;          // 401
        } else if (msg.contains("rate limit") || msg.contains("quota")) {
            status = HttpStatus.TOO_MANY_REQUESTS;      // 429
        } else if (msg.contains("Failed to reach")) {
            status = HttpStatus.SERVICE_UNAVAILABLE;    // 503 — network failure reaching ElevenLabs
        } else {
            status = HttpStatus.SERVICE_UNAVAILABLE;    // 503 — generic provider failure
        }

        return buildResponse(status, ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnexpected(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<ErrorResponseDto> buildResponse(HttpStatus status, String message) {
        ErrorResponseDto body = new ErrorResponseDto(false, message, status.value(), LocalDateTime.now());
        return ResponseEntity.status(status).body(body);
    }

}