package com.example.tts.repository;

import com.example.tts.model.SpeechRequest;
import com.example.tts.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeechRequestRepository extends JpaRepository<SpeechRequest, Long> {
    List<SpeechRequest> findByUserOrderByCreatedAtDesc(User user);
}