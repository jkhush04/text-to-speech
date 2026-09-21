package com.example.tts.service;

import com.example.tts.dto.AuthResponseDto;
import com.example.tts.dto.LoginRequestDto;
import com.example.tts.dto.RegisterRequestDto;
import com.example.tts.exception.InvalidRequestException;
import com.example.tts.model.User;
import com.example.tts.repository.UserRepository;
import com.example.tts.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("An account with this email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponseDto(token, user.getEmail());
    }

    public AuthResponseDto login(LoginRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidRequestException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponseDto(token, request.getEmail());
    }
}