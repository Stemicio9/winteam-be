package com.workonenight.winteambe.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.util.ResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;

@Slf4j
@Service
public class FBInitialize {

    @PostConstruct
    public void initialize() {
        try {
            File file = ResourceUtils.getFile("classpath:workonenight-team-firebase.json");
            FileInputStream serviceAccount = new FileInputStream(file);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            log.error("Error initializing Firebase", e);
        }

    }
}
