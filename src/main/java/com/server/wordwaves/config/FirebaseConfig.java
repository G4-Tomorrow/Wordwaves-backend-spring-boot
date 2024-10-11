package com.server.wordwaves.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${app.firebase.bucket-name}")
    private String bucketName;

    @Bean
    FirebaseApp firebaseApp() throws IOException {
        ClassPathResource serviceAccount = new ClassPathResource("firebase-credentials.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                .setStorageBucket(bucketName)
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
