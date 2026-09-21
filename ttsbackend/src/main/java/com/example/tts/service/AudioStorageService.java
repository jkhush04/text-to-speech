package com.example.tts.service;

import com.example.tts.exception.TtsProviderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AudioStorageService {

    @Value("${audio.storage.path}")
    private String storagePath;


    public String saveAudio(byte[] audioBytes){
        try{
            Path storageDir= Paths.get(storagePath);
            if(!Files.exists(storageDir)){
                Files.createDirectories(storageDir);
            }

            String filename= UUID.randomUUID()+".mp3";
            Path filePath=storageDir.resolve(filename);
            Files.write(filePath,audioBytes);

            return filename;
        }catch(IOException e){
            throw new TtsProviderException("Failed to save generated audio file: "+e.getMessage());
        }
    }

    public Path resolveAudioPath(String filename){
        return Paths.get(storagePath).resolve(filename).normalize();
    }
}
