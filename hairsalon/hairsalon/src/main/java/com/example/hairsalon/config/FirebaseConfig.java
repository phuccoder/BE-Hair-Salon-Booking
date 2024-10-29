package com.example.hairsalon.config;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        InputStream serviceAccount = new ClassPathResource("serviceAccountKey.json").getInputStream();
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("hair-salon-e9f1c.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);
    }

    @Bean
    public Storage storage() throws IOException {
        InputStream serviceAccount = new ClassPathResource("serviceAccountKey.json").getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}