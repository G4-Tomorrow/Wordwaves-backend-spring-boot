package com.server.wordwaves.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Bean
    FirebaseApp firebaseApp() throws IOException {
        // Thử lấy credentials từ biến môi trường trước
        String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS");

        FirebaseOptions options;

        if (firebaseCredentials != null) {
            log.info("Sử dụng FIREBASE_CREDENTIALS từ biến môi trường");

            // Chuyển đổi chuỗi JSON thành InputStream
            ByteArrayInputStream credentialsStream = new ByteArrayInputStream(firebaseCredentials.getBytes());

            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .setStorageBucket("petgarden-bda48.appspot.com")
                    .build();
        } else {
            log.info("Sử dụng firebase-credentials.json từ file");

            // Nếu không có biến môi trường, sử dụng file json từ hệ thống file
            FileInputStream credentialsStream = new FileInputStream("/app/config/firebase-credentials.json");

            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .setStorageBucket("petgarden-bda48.appspot.com")
                    .build();
        }

        return FirebaseApp.initializeApp(options);
    }
}
