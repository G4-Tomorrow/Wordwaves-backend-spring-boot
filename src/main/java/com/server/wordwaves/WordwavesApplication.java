package com.server.wordwaves;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableFeignClients
public class WordwavesApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordwavesApplication.class, args);
    }
}
